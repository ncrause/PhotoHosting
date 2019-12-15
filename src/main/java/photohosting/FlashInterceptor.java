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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import static org.apache.struts2.StrutsStatics.HTTP_REQUEST;

/**
 * This interceptor, along with the FlashAware interface, offers a "flash"
 * context where messages can be passed along to a redirected URL.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class FlashInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getInvocationContext();
		
		// move all session-stored flash values into the request scope, thus
		// making them available, but purging them from subsequent calls.
		Map<String, Object> session = context.getSession();
		
		if (session.containsKey(FlashAware.FLASH_CONTEXT)) {
			HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
			
			request.setAttribute(FlashAware.FLASH_CONTEXT, session.get(FlashAware.FLASH_CONTEXT));
			session.remove(FlashAware.FLASH_CONTEXT);
		}
		
		return invocation.invoke();
	}
	
}
