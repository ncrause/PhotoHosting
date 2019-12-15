/*
 * Copyright (C) 2020 Nathan Crause <nathan@crause.name>
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

/**
 * Odd little bean which stores the 3 versions of an image.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
@Data
public class Images {
	
	private byte[] highResolution;
	
	private byte[] opengraphImage;
	
	private byte[] redditImage;
	
	private byte[] twitterImage;
	
}
