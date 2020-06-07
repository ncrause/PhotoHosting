/*
 * Copyright (C) 2020 Nathan Crause <nathan@crause.name>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package photohosting.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import photohosting.Configuration;
import photohosting.services.DatabaseAccessService;
import photohosting.services.ImageService;
import photohosting.services.PhotoService;
import photohosting.services.UserService;
import photohosting.services.beans.Images;

/**
 * This class provides the mechanism for creating the various tables and
 * seeding.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class DatabaseBootstrap {
	
	public static final String EMAIL_ADDRESS = "nathan@crause.name".intern();
	
	private final ServletContext context;
	
	private final DatabaseAccessService databaseAccessService;
	
	public DatabaseBootstrap(ServletContext context, DatabaseAccessService databaseAccessService) {
		this.context = context;
		this.databaseAccessService = databaseAccessService;
	}

	/**
	 * This method checks to make sure that the necessary data structures are
	 * all in existence, and populates some "bootstrap" data.
	 * @throws java.io.IOException
	 */
	public void bootstrap() throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			Admin admin = connection.getAdmin();
			boolean flush = false;
			
			if (!namespaceExists(admin, DatabaseAccessService.NAMESPACE)) {
				admin.createNamespace(NamespaceDescriptor.create(DatabaseAccessService.NAMESPACE).build());
			}
			
			TableName userTableName = TableName.valueOf(DatabaseAccessService.NAMESPACE, UserService.TABLE_NAME);
			TableName photoTableName = TableName.valueOf(DatabaseAccessService.NAMESPACE, PhotoService.TABLE_NAME);
			
			if (Configuration.get().isLocal()) {
				if (admin.tableExists(userTableName)) {
					admin.disableTable(userTableName);
					admin.deleteTable(userTableName);
				}
				if (admin.tableExists(photoTableName)) {
					admin.disableTable(photoTableName);
					admin.deleteTable(photoTableName);
				}
			}

			flush |= bootstrapUsers(admin, userTableName);
			flush |= bootstrapPhotos(admin, photoTableName);

			if (flush || (count(userTableName) == 0 && count(photoTableName) == 0)) {
				updateSeededUsers(admin, userTableName, photoTableName);

				admin.flush(userTableName);
				admin.flush(photoTableName);
			}
		}
	}
	
	/**
	 * Tests if the specified namespace actually exists on our HBase instance.
	 * 
	 * @param admin
	 * @return <code>true</code> if the namespace already exists, 
	 * <code>false</code> otherwise
	 */
	private boolean namespaceExists(Admin admin, String namespace) throws IOException {
		for (NamespaceDescriptor ns : admin.listNamespaceDescriptors()) {
			if (ns.getName().equals(namespace)) {
				return true;
			}
		}
		
		return false;
	}

	private boolean bootstrapUsers(Admin admin, TableName tableName) throws IOException {
		if (!admin.tableExists(tableName)) {
			HTableDescriptor table = new HTableDescriptor(tableName);
			
			table.addFamily(new HColumnDescriptor(UserService.CF_IDENTIFICATION)
					.setCompressionType(Compression.Algorithm.NONE));
			table.addFamily(new HColumnDescriptor(UserService.CF_PHOTOS)
					.setCompressionType(Compression.Algorithm.NONE));
			
			admin.createTable(table);
			seedUsers(tableName);
			
			return true;
		}
		
		return false;
	}
	
	private int count(TableName tableName) throws IOException {
		int sum = 0;
		
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table source = connection.getTable(tableName)) {
				Scan scan = new Scan();
				
				scan.setFilter(new FirstKeyOnlyFilter());
				
				try (ResultScanner scanner = source.getScanner(scan)) {
					for (Result result : scanner) {
						++sum;
					}
					
					return sum;
				}
			}
		}
	}
	
	/**
	 * Invoked by bootstrapUsers to seed the users table with at least one
	 * default user. It intentionally uses a completely clean connection.
	 */
	private void seedUsers(TableName tableName) throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table target = connection.getTable(tableName)) {
				Put user = new Put(Bytes.toBytes(EMAIL_ADDRESS));

				user.addColumn(Bytes.toBytes(UserService.CF_IDENTIFICATION), 
						Bytes.toBytes(UserService.Q_PASSWORD), 
						DigestUtils.sha256("changeme"));
				user.addColumn(Bytes.toBytes(UserService.CF_PHOTOS), 
						Bytes.toBytes(UserService.Q_COUNT), 
						Bytes.toBytes(0));

				target.put(user);
			}
		}
	}

	private boolean bootstrapPhotos(Admin admin, TableName tableName) throws IOException {
		if (!admin.tableExists(tableName)) {
			HTableDescriptor table = new HTableDescriptor(tableName);
			
			table.addFamily(new HColumnDescriptor(PhotoService.CF_OWNERSHIP)
					.setCompressionType(Compression.Algorithm.NONE));
			table.addFamily(new HColumnDescriptor(PhotoService.CF_CREDIT)
					.setCompressionType(Compression.Algorithm.NONE));
			table.addFamily(new HColumnDescriptor(PhotoService.CF_CONTENT)
					.setCompressionType(Compression.Algorithm.NONE));
			
			admin.createTable(table);
			seedPhotos(tableName);
			
			return true;
		}
		
		return false;
	}

	private void seedPhotos(TableName tableName) throws IOException {
		List<String> ids = new ArrayList<>();
		List<Put> photos = new ArrayList<>();
		ImageService imageService = new ImageService();
		
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table target = connection.getTable(tableName)) {
				for (String file : new String[] {
							"body-of-water-across-forest", 
							"man-and-woman-nude-lying-together",
							"soccer-ball",
							"cooked-chicken-on-white-plate",
							"grayscale-photo-of-naked-woman",
							"football-players-in-blue-jersey-lined-under-grey-white"}) {
					String id = file + "-" + UUID.randomUUID().toString();
					Put photo = new Put(Bytes.toBytes(id));
					Images images = imageService.prepare(context.getResource("/images/bootstrap/" + file + ".jpg"));
					
					photo.addColumn(Bytes.toBytes(PhotoService.CF_OWNERSHIP), 
							Bytes.toBytes(PhotoService.Q_EMAIL), 
							Bytes.toBytes(EMAIL_ADDRESS));
					
					Properties props = new Properties();
					
					try (InputStream in = context.getResourceAsStream("/images/bootstrap/" + file + ".properties")) {
						props.load(in);
					}

					for (Object entryObject : Maps.start()
							.put(PhotoService.Q_SOURCE_NAME, "credit.source.name")
							.put(PhotoService.Q_SOURCE_URL, "credit.source.url")
							.put(PhotoService.Q_AUTHOR_NAME, "credit.author.name")
							.put(PhotoService.Q_AUTHOR_URL, "credit.author.url")
							.getMap().entrySet()) {
						Map.Entry entry = (Map.Entry) entryObject;
						
						photo.addColumn(Bytes.toBytes(PhotoService.CF_CREDIT), 
								Bytes.toBytes((String) entry.getKey()), 
								Bytes.toBytes(props.getProperty((String) entry.getValue())));
					}
					
					photo.addColumn(Bytes.toBytes(PhotoService.CF_CONTENT),
							Bytes.toBytes(PhotoService.Q_MIME_TYPE),
							Bytes.toBytes("image/jpeg"));
					photo.addColumn(Bytes.toBytes(PhotoService.CF_CONTENT),
							Bytes.toBytes(PhotoService.Q_HIGH_RES),
							images.getHighResolution());
					photo.addColumn(Bytes.toBytes(PhotoService.CF_CONTENT),
							Bytes.toBytes(PhotoService.Q_OG_IMAGE),
							images.getOpengraphImage());
					photo.addColumn(Bytes.toBytes(PhotoService.CF_CONTENT),
							Bytes.toBytes(PhotoService.Q_REDDIT_IMAGE),
							images.getRedditImage());
					photo.addColumn(Bytes.toBytes(PhotoService.CF_CONTENT),
							Bytes.toBytes(PhotoService.Q_TWITTER_IMAGE),
							images.getTwitterImage());
					
					photos.add(photo);
				}
				
				target.put(photos);
			}
		}
	}

	/**
	 * This method is used to update the user record with all seeded photos.
	 * 
	 * @param admin
	 * @param userTableName
	 * @param photoTableName 
	 */
	private void updateSeededUsers(Admin admin, TableName userTableName, TableName photoTableName) 
			throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table source = connection.getTable(photoTableName); Table destin = connection.getTable(userTableName)) {
				Scan scan = new Scan();
				int count = 0;
				Put put = new Put(Bytes.toBytes(EMAIL_ADDRESS));
				
				scan.setFilter(new FirstKeyOnlyFilter());
				
				try (ResultScanner scanner = source.getScanner(scan)) {
					for (Result result : scanner) {
						put.addColumn(Bytes.toBytes(UserService.CF_PHOTOS),
								Bytes.toBytes(String.format("%s%d", UserService.Q_PREFIX_KEY, ++count)),
								result.getRow());
					}
				}
				
				put.addColumn(Bytes.toBytes(UserService.CF_PHOTOS), 
						Bytes.toBytes(UserService.Q_COUNT), 
						Bytes.toBytes(count));
				
				destin.put(put);
			}
		}
	}
	
}
