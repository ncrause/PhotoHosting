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

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.Map;
import lombok.Setter;
import photohosting.services.UserService;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class AuthenticatedInterceptor extends AbstractInterceptor {
	
	public static String AUTHENTICATION_KEY = "authentication".intern();
	
	@Setter
	private UserService userService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionSupport action = (ActionSupport) invocation.getAction();
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		
		if (session.containsKey(AUTHENTICATION_KEY)) {
			// if the target action is aware of authentication, send it along
			if (action instanceof AuthenticationAware) {
				((AuthenticationAware) action).setAuthenticatedUser(userService.get(String.valueOf(session.get(AUTHENTICATION_KEY))));
			}
			
			return invocation.invoke();
		}
		
		return "home";
	}
	
}
