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

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Setter;
import org.apache.commons.beanutils.PropertyUtils;
import photohosting.services.NonceService;
import photohosting.services.PhotoService;
import photohosting.services.UserService;
import photohosting.services.beans.Photo;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class SlideshowInterceptor extends AbstractInterceptor {
	
	@Setter
	private UserService userService;
	
	@Setter
	private PhotoService photoService;
	
	@Setter
	private NonceService nonceService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionSupport action = (ActionSupport) invocation.getAction();
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		List<Photo> slideshow = photoService.listFor(userService.get("nathan@crause.name"));
		Map<String, String> nonces = nonceService.generateNonces(session, slideshow.stream()
				.map(Photo::getId)
				.collect(Collectors.toList()));
		
		PropertyUtils.setProperty(action, "slideshow", slideshow);
		PropertyUtils.setProperty(action, "nonces", nonces);
		
		return invocation.invoke();
	}
	
}
