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
package photohosting.dashboard;

import com.opensymphony.xwork2.ActionSupport;
import java.awt.Dimension;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import photohosting.FlashAware;
import photohosting.services.ImageService;
import photohosting.services.PhotoService;
import photohosting.services.beans.Photo;
import photohosting.services.beans.User;
import photohosting.utils.Values;

/**
 * We might have been able to use validation annotations, but some of them are
 * just too specific, so I'm going to stick with coding validations myself.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class AddUploadAction extends ActionSupport implements AuthenticationAware, FlashAware {
	
	@Getter @Setter
	private User authenticatedUser;
	
	@Getter @Setter
	private Map<String, Object> session;
	
	@Getter @Setter
	private HttpServletRequest servletRequest;
	
	@Getter @Setter
	private ImageService imageService;
	
	@Getter @Setter
	private PhotoService photoService;
	
	@Getter @Setter
	private File photoSource;
	
	@Getter @Setter
	private String photoSourceContentType;
	
	@Getter @Setter
	private String photoSourceFileName;
	
	@Getter @Setter
	private String authorName;
	
	@Getter @Setter
	private String authorURL;
	
	@Getter @Setter
	private String sourceName;
	
	@Getter @Setter
	private String sourceURL;
	
	@Override
    public String execute() throws Exception {
		if (getPhotoSource() == null) {
			addActionError(getText("dashboard.upload.nofile.failure.message"));
			
			return INPUT;
		}
		// I've decided to use probeContentType instead of relying on
		// photoSourceContentType, because I don't know if clients can inject
		// a mime type, and thus injecting garbage content. However, MacOS
		// doesn't seem capable of identifying shit, so I do still need to
		// keep photoSourceContentType as a failover type :(
		String detectedContentType = Values.coalesce(
				Files.probeContentType(getPhotoSource().toPath()),
				photoSourceContentType);
		
		if (!Arrays.asList("image/jpeg", "image/png").contains(detectedContentType)) {
			addActionError(getText("dashboard.upload.type.failure.message", Arrays.asList(detectedContentType)));
			
			return INPUT;
		}
		
		Dimension photoSourceSize = imageService.getImageDimensions(getPhotoSource());
		
		if (photoSourceSize.width < 960 || photoSourceSize.height < 600) {
			addActionError(getText("dashboard.upload.size.failure.message"));
			
			return INPUT;
		}
		
		String id = photoService.create(authenticatedUser, photoSource, photoSourceFileName);
		// now that we have the photo record added, let's set the metadata
		Photo photo = photoService.get(id);
		
		if (authorName != null) {
			photo.setAuthorName(authorName);
			
			if (authorURL != null) {
				photo.setAuthorURL(authorURL);
			}
		}
		
		if (sourceName != null) {
			photo.setSourceName(sourceName);
			
			if (sourceURL != null) {
				photo.setSourceURL(sourceURL);
			}
		}
		
		photoService.save(photo);
		
		setFlash(SUCCESS_FLASH, "Successfully uploaded new photo.");
		
        return SUCCESS;
    }
	
}
