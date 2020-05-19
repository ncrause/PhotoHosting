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
	
	/**
	 * Full scale+quality image - 82% - <b>ONLY VISIBLE FROM OUR OWN PAGES</b>
	 */
	public static final String Q_HIGH_RES = "high resolution".intern();
	
	/**
	 * og:image - 600x600 - 20%
	 */
	public static final String Q_OG_IMAGE = "opengraph image".intern();
	
	/**
	 * image_src - 960x??? - 40% - basically width chosen on "Large" screen
	 * container size defined in Bootstrap
	 */
	public static final String Q_REDDIT_IMAGE = "reddit image".intern();
	
	/**
	 * twitter:image - 600x300 - 20%
	 */
	public static final String Q_TWITTER_IMAGE = "twitter image".intern();
	
	public Photo get(String id) throws IOException;
	
	public List<Photo> listFor(User user) throws IOException;
	
	public byte[] retrieveHighResolutionImage(String id) throws IOException;
	
	public byte[] retrieveOpenGraphImage(String id) throws IOException;
	
	public byte[] retrieveRedditImage(String id) throws IOException;
	
	public byte[] retrieveTwitterImage(String id) throws IOException;
	
	/**
	 * Performs an entire creation of a photo, from reading it, scaling it,
	 * associating it with the user, etc.
	 * 
	 * @param user the user to which the photo is to be associated
	 * @param file the local file (typically temp file uploaded)
	 * @param originalFilename in order to give the final "filename" some
	 * association to the original, we need to know the file's name as it
	 * existed on the user's workstation
	 * @return the new photo record's ID
	 * @throws IOException 
	 */
	public String create(User user, File file, String originalFilename) throws IOException;
	
	/**
	 * Write any data in the bean, regardless of a change has occurred or not
	 * (there is no implicit "dirty" checking - it serves no purpose).
	 * 
	 * Due to the nature of how HBase functions, we don't need to care if this
	 * is a new record, or an update - so long as we have an ID, we're good.
	 * 
	 * @param photo
	 * @throws IOException 
	 */
	public void save(Photo photo) throws IOException;
	
}
