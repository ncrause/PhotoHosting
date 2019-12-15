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

import org.apache.hadoop.hbase.client.Connection;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
@Log
public class DatabaseAccessServiceImpl implements DatabaseAccessService {
	
	@Getter
	private final Configuration config;
	
	public DatabaseAccessServiceImpl() {
		config = HBaseConfiguration.create();
	}

	@Override
	public Connection openConnection() throws IOException {
		return ConnectionFactory.createConnection(config);
	}
	
}
