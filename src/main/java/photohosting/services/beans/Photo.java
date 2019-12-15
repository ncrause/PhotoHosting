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

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import photohosting.services.PhotoService;

/**
 * Simple container class holding the photo's data, and ownership.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
@Data
public class Photo {
	
	private String id;
	
	private String owner;
	
	private String authorName;
	
	private String authorURL;
	
	private String sourceName;
	
	private String sourceURL;
	
	private byte[] data;
	
	public static Photo valueOf(Result result) {
		Photo instance = new Photo();
		
		instance.id = Bytes.toString(result.getRow());
		instance.owner = Bytes.toString(result.getValue(
				Bytes.toBytes(PhotoService.CF_OWNERSHIP), 
				Bytes.toBytes(PhotoService.Q_EMAIL)));
		instance.authorName = Bytes.toString(result.getValue(
				Bytes.toBytes(PhotoService.CF_CREDIT), 
				Bytes.toBytes(PhotoService.Q_AUTHOR_NAME)));
		instance.authorURL = Bytes.toString(result.getValue(
				Bytes.toBytes(PhotoService.CF_CREDIT), 
				Bytes.toBytes(PhotoService.Q_AUTHOR_URL)));
		instance.sourceName = Bytes.toString(result.getValue(
				Bytes.toBytes(PhotoService.CF_CREDIT), 
				Bytes.toBytes(PhotoService.Q_SOURCE_NAME)));
		instance.sourceURL = Bytes.toString(result.getValue(
				Bytes.toBytes(PhotoService.CF_CREDIT), 
				Bytes.toBytes(PhotoService.Q_SOURCE_URL)));
		instance.data = result.getValue(Bytes.toBytes(PhotoService.CF_CONTENT), 
				Bytes.toBytes(PhotoService.Q_DATA));
		
		return instance;
	}
	
}
