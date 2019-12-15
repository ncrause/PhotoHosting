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
import photohosting.errors.DuplicateRecordException;
import photohosting.services.beans.User;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public interface UserService {
	
	/**
	 * The table name used for storing user information.
	 */
	public static final String TABLE_NAME = "users".intern();
	
	/**
	 * The column family which contains identification details.
	 */
	public static final String CF_IDENTIFICATION = "ident".intern();
	
	/**
	 * Cell qualifier within the CF_INDENTIFICATION family.
	 */
	public static final String Q_PASSWORD = "password".intern();
	
	/**
	 * The column family which contains references to all the photos associated
	 * with this user.
	 */
	public static final String CF_PHOTOS = "photos".intern();
	
	/**
	 * Cell qualifier within CF_PHOTOS which holds a running count of the
	 * number of photos associated with a user.
	 */
	public static final String Q_COUNT = "count".intern();
	
	/**
	 * Cell qualifier prefix within CF_PHOTOS that contains the key to the
	 * actual "photos" record.
	 */
	public static final String Q_PREFIX_KEY = "key ".intern();
	
	public User get(String emailAddress) throws IOException;
	
	public void save(User user) throws IOException;
	
	/**
	 * 
	 * @param emailAddress the new email address
	 * @return the new password
	 * @throws IOException 
	 * @throws photohosting.errors.DuplicateRecordException 
	 */
	public String signup(String emailAddress) throws IOException, DuplicateRecordException;
	
}
