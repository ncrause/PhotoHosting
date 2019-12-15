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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import photohosting.FlashAware;
import photohosting.services.beans.User;

/**
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
	
	
	
}
