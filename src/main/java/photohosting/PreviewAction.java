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
package photohosting;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import photohosting.services.NonceService;
import photohosting.services.PhotoService;
import photohosting.services.beans.Photo;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class PreviewAction extends ActionSupport implements ServletContextAware {
	
	@Setter
	private PhotoService photoService;
	
	@Setter
	private ServletContext servletContext;
	
	@Getter @Setter
	private String type;
	
	@Getter @Setter
	private String id;
	
	@Getter
	private String filename;
	
	@Getter
	private InputStream stream;
	
	@Override
    public String execute() throws Exception {
		Photo photo = photoService.get(id);
		
		if (photo == null) {
			prepareFeedbackResource("image-not-found.jpg");
			
			return SUCCESS;
		}
		
		filename = photo.getId() + ".jpg";
		
		if (getType().equalsIgnoreCase("og")) {
			stream = new ByteArrayInputStream(photoService.retrieveOpenGraphImage(photo.getId()));
		}
		else if (getType().equalsIgnoreCase("reddit")) {
			stream = new ByteArrayInputStream(photoService.retrieveRedditImage(photo.getId()));
		}
		else if (getType().equalsIgnoreCase("twitter")) {
			stream = new ByteArrayInputStream(photoService.retrieveTwitterImage(photo.getId()));
		}
		else {
			prepareFeedbackResource("image-not-found.jpg");
		}
		
		return SUCCESS;
	}
	
	private void prepareFeedbackResource(String filename) {
		this.filename = filename;
		stream = servletContext.getResourceAsStream(String.format("/images/%s", filename));
	}
	
}
