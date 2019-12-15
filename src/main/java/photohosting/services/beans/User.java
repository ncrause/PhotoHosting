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
package photohosting.services.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import photohosting.services.UserService;

/**
 * Simple container bean holding the user's email address (which is the
 * primary key), the password hash (and helper methods to crate a hash), and
 * an array of all the photos associated with this user.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class User {
	
	@Getter @Setter
	private String emailAddress;
	
	@Getter @Setter
	private byte[] passwordHash;
	
	private final List<String> photoIDs;
	
	public User() {
		photoIDs = new ArrayList<>();
	}
	
	public User addPhotoID(String id) {
		photoIDs.add(id);
		
		return this;
	}
	
	public List<String> getPhotoIDs() {
		return Collections.unmodifiableList(photoIDs);
	}
	
	public int photoCount() {
		return photoIDs.size();
	}
	
	public User setPassword(String password) {
		passwordHash = DigestUtils.sha256(password);
		
		return this;
	}
	
	public boolean isValid(String password) {
		byte[] cmp = DigestUtils.sha256(password);
		
		return Arrays.equals(passwordHash, cmp);
	}
	
	public static User valueOf(Result result) {
		User instance = new User();
		
		instance.emailAddress = Bytes.toString(result.getRow());
		instance.passwordHash = result.getValue(
				Bytes.toBytes(UserService.CF_IDENTIFICATION), 
				Bytes.toBytes(UserService.Q_PASSWORD));
		
		int count = Bytes.toInt(result.getValue(
				Bytes.toBytes(UserService.CF_PHOTOS), 
				Bytes.toBytes(UserService.Q_COUNT)));
		
		for (int index = 1; index <= count; ++index) {
			instance.photoIDs.add(Bytes.toString(result.getValue(
					Bytes.toBytes(UserService.CF_PHOTOS), 
					Bytes.toBytes(String.format("%s%d", UserService.Q_PREFIX_KEY, index)))));
		}
		
		return instance;
	}
	
}
