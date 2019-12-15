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
import lombok.Getter;
import lombok.Setter;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import static photohosting.services.PhotoService.TABLE_NAME;
import photohosting.services.beans.Photo;
import photohosting.services.beans.User;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class UserServiceImpl implements UserService {
	
	@Setter
	private DatabaseAccessService databaseAccessService;
	
	private static TableName getTableName() {
		return TableName.valueOf(DatabaseAccessService.NAMESPACE, TABLE_NAME);
	}

	@Override
	public User get(String emailAddress) throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table table = connection.getTable(getTableName())) {
				Get get = new Get(Bytes.toBytes(emailAddress));
				Result result = table.get(get);
				
				if (result.isEmpty()) {
					return null;
				}
				
				return User.valueOf(result);
			}
		}
	}
	
}
