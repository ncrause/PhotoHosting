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
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.struts2.interceptor.SessionAware;
import photohosting.dashboard.AuthenticatedInterceptor;
import photohosting.services.UserService;
import photohosting.services.beans.Photo;
import photohosting.services.beans.User;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
@Log
public class LoginAction extends ActionSupport implements SessionAware, SlideshowRenderable {
	
	@Setter
	private UserService userService;
	
	@Setter
	private Map<String, Object> session;
	
	@Getter @Setter
	private List<Photo> slideshow;
	
	@Getter @Setter
	private Map<String, String> nonces;
	
	@Getter
	private String emailAddress;
	
	@Getter
	private String password;
	
	@RequiredFieldValidator(key = "global.required")
	@RequiredStringValidator(key = "global.required")
	@EmailValidator(key = "global.email.invalid")
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@RequiredFieldValidator(key = "global.required")
	@RequiredStringValidator(key = "global.required")
	public void setPassword(String password) {
		this.password = password;
	}
    
	@Override
    public String execute() throws Exception {
		User user = userService.get(getEmailAddress());
		
		if (user == null) {
			addActionError(getText("login.authentication.failure.message"));
			log.severe(String.format("Unknown email address '%s'", getEmailAddress()));
			
			return INPUT;
		}
		
		if (!user.isValid(getPassword())) {
			addActionError(getText("login.authentication.failure.message"));
			log.severe(String.format("Password not valid '%s'", getPassword()));
			
			return INPUT;
		}
		
		session.put(AuthenticatedInterceptor.AUTHENTICATION_KEY, getEmailAddress());
		
		return SUCCESS;
	}
	
}
