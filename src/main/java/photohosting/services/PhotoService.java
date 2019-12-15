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
import java.util.List;
import photohosting.services.beans.Photo;
import photohosting.services.beans.User;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public interface PhotoService {
	
	/**
	 * The table name used for storing user information.
	 */
	public static final String TABLE_NAME = "photos".intern();
	
	/**
	 * The column family which contains ownership details, such as the
	 * user's email address.
	 */
	public static final String CF_OWNERSHIP = "owner".intern();
	
	/**
	 * The column family which contains some credit info, such as the source's
	 * name, the source's URL, the author's name, and the author's URL.
	 */
	public static final String CF_CREDIT = "credit".intern();
	
	/**
	 * The column family which contains the actual content of the photos,
	 * such as the binary data, the mime type, and the original file's name.
	 */
	public static final String CF_CONTENT = "content".intern();
	
	public static final String Q_EMAIL = "email".intern();
	
	public static final String Q_SOURCE_NAME = "source name".intern();
	
	public static final String Q_SOURCE_URL = "source url".intern();
	
	public static final String Q_AUTHOR_NAME = "author name".intern();
	
	public static final String Q_AUTHOR_URL = "author url".intern();
	
	public static final String Q_MIME_TYPE = "mime type".intern();
	
	public static final String Q_DATA = "data".intern();
	
	public Photo get(String id) throws IOException;
	
	public List<Photo> listFor(User user) throws IOException;
	
}
