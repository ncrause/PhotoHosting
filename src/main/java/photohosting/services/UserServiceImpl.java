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
import java.util.Random;
import lombok.Setter;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import photohosting.errors.DuplicateRecordException;
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

	@Override
	public void save(User user) throws IOException {
		try (Connection connection = databaseAccessService.openConnection()) {
			try (Table table = connection.getTable(getTableName())) {
				Put put = new Put(Bytes.toBytes(user.getEmailAddress()));
				
				put.addColumn(Bytes.toBytes(CF_IDENTIFICATION), 
						Bytes.toBytes(Q_PASSWORD), 
						user.getPasswordHash());
				
				table.put(put);
			}
		}
	}
	
	@Override
	public String signup(String emailAddress) throws IOException, DuplicateRecordException {
		if (get(emailAddress) != null) {
			throw new DuplicateRecordException(emailAddress);
		}
		
		User user = new User();
		String password = randomPassword();
		
		user.setEmailAddress(emailAddress);
		user.setPassword(password);
		
		return password;
	}
	
	private String randomPassword() {
		Random random = new Random();
		
		return random.ints(48, 123)
			.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
			.limit(8)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
	
}
