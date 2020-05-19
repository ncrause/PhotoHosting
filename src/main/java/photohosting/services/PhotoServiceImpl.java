/*
 * Copyright (C) 2019 Nathan Crause <nathan@crause.name>
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
package photohosting.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Setter;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import photohosting.services.beans.Images;
import photohosting.services.beans.Photo;
import photohosting.services.beans.User;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class PhotoServiceImpl implements PhotoService {
	
	@Setter
	private DatabaseAccessService databaseAccessService;
	
	@Setter
	private ImageService imageService;
	
	@Setter
	private UserService userService;
	
	private static TableName getTableName() {
		return TableName.valueOf(DatabaseAccessService.NAMESPACE, TABLE_NAME);
	}

	@Override
	public Photo get(String id) throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table table = connection.getTable(getTableName())) {
				Get get = new Get(Bytes.toBytes(id));
				Result result = table.get(get);
				
				if (result.isEmpty()) {
					return null;
				}
				
				return Photo.valueOf(result);
			}
		}
	}

	@Override
	public List<Photo> listFor(User user) throws IOException {
		if (user.photoCount() == 0) {
			return new ArrayList<>();
		}
		
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table table = connection.getTable(getTableName())) {
				List<Get> gets = new ArrayList<>();
				List<Photo> list = new ArrayList<>();
				
				for (String id : user.getPhotoIDs()) {
					gets.add(new Get(Bytes.toBytes(id)));
				}
				
				for (Result result : table.get(gets)) {
					list.add(Photo.valueOf(result));
				}
				
				return list;
			}
		}
	}

	@Override
	public byte[] retrieveHighResolutionImage(String id) throws IOException {
		return retrieveImage(id, Q_HIGH_RES);
	}

	@Override
	public byte[] retrieveOpenGraphImage(String id) throws IOException {
		return retrieveImage(id, Q_OG_IMAGE);
	}

	@Override
	public byte[] retrieveRedditImage(String id) throws IOException {
		return retrieveImage(id, Q_REDDIT_IMAGE);
	}

	@Override
	public byte[] retrieveTwitterImage(String id) throws IOException {
		return retrieveImage(id, Q_TWITTER_IMAGE);
	}
	
	byte[] retrieveImage(String id, String qualifier) throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table table = connection.getTable(getTableName())) {
				Get get = new Get(Bytes.toBytes(id));
				Result result = table.get(get);
				
				if (result.isEmpty()) {
					return null;
				}
				
				return result.getValue(Bytes.toBytes(PhotoService.CF_CONTENT), 
						Bytes.toBytes(qualifier));
			}
		}
	}

	@Override
	public String create(User user, File file, String originalFilename) throws IOException {
		// just strip off known image extensions
		String useFilename = originalFilename.replaceAll("(?:\\.jpg)|(?:\\.png)", "");
		String id = useFilename + "-" + UUID.randomUUID().toString();
		
		
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table table = connection.getTable(getTableName())) {
				Put photo = new Put(Bytes.toBytes(id));
				Images images = imageService.prepare(file);

				photo.addColumn(Bytes.toBytes(PhotoService.CF_OWNERSHIP), 
						Bytes.toBytes(PhotoService.Q_EMAIL), 
						Bytes.toBytes(user.getEmailAddress()));
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
				
				table.put(photo);
				
				user.addPhotoID(id);
				
				userService.save(user);
		
				return id;
			}
		}
	}

	@Override
	public void save(Photo photo) throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table target = connection.getTable(getTableName())) {
				Put put = new Put(Bytes.toBytes(photo.getId()));
				
				put.addColumn(Bytes.toBytes(CF_CREDIT), 
						Bytes.toBytes(Q_EMAIL), 
						Bytes.toBytes(photo.getOwner()));
				put.addColumn(Bytes.toBytes(CF_CREDIT), 
						Bytes.toBytes(Q_AUTHOR_NAME), 
						Bytes.toBytes(photo.getAuthorName()));
				put.addColumn(Bytes.toBytes(CF_CREDIT), 
						Bytes.toBytes(Q_AUTHOR_URL), 
						Bytes.toBytes(photo.getAuthorURL()));
				put.addColumn(Bytes.toBytes(CF_CREDIT), 
						Bytes.toBytes(Q_SOURCE_NAME), 
						Bytes.toBytes(photo.getSourceName()));
				put.addColumn(Bytes.toBytes(CF_CREDIT), 
						Bytes.toBytes(Q_SOURCE_URL), 
						Bytes.toBytes(photo.getSourceURL()));
				
				target.put(put);
			}
		}
	}
	
}
