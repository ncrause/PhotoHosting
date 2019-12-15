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
package photohosting;

import com.opensymphony.xwork2.ActionSupport;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import photohosting.services.NonceService;
import photohosting.services.PhotoService;
import photohosting.services.beans.Photo;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 * @see https://struts.apache.org/core-developers/stream-result.html
 */
@Log
public class ShowAction extends ActionSupport implements ServletContextAware, SessionAware {
	
	@Setter
	private PhotoService photoService;
	
	@Setter
	private NonceService nonceService;
	
	@Setter
	private ServletContext servletContext;
	
	@Setter
	private Map<String, Object> session;
	
	@Getter @Setter
	private String id;
	
	@Getter @Setter
	private String nonce;
	
	@Getter
	private Photo photo;
	
	@Getter
	private String filename;
	
	@Getter
	private InputStream stream;
	
	@Override
    public String execute() throws Exception {
		photo = photoService.get(id);
		
		if (photo == null) {
			prepareFeedbackResource("image-not-found.jpg");
			
			return SUCCESS;
		}
		
		// if there's no NONCE, we're actually just rendering - this is
		// invoked when using the path "/show/id"
		if (nonce == null || nonce.isEmpty()) {
			prepareNonce();
			
			return SUCCESS;
		}
		
		// from this point on, we know we're in the path "/show/id/nonce"
		Map<String, String> nonces = nonceService.getNonces(session);
		
		if (!nonces.containsKey(id) || !nonces.remove(id).equals(nonce)) {
			prepareNonce();

			return "html";
		}
		
		filename = photo.getId() + ".jpg";
		stream = new ByteArrayInputStream(photoService.retrieveHighResolutionImage(photo.getId()));
		
		return SUCCESS;
	}
	
	private void prepareFeedbackResource(String filename) {
		this.filename = filename;
		stream = servletContext.getResourceAsStream(String.format("/images/%s", filename));
	}
	
	private void prepareNonce() {
		filename = photo.getId() + ".jpg";	// just for the "alt" attribute
		nonce = nonceService.generateNonce(session, photo.getId());
	}
	
}
