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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import photohosting.services.beans.Photo;
import photohosting.services.beans.User;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class PhotoServiceImpl implements PhotoService {
	
	@Setter
	private DatabaseAccessService databaseAccessService;
	
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
	
}
