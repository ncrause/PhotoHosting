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

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public interface FlashAware extends SessionAware, ServletRequestAware {
	
	public static final String FLASH_CONTEXT = "flash context".intern();
	
	/**
	 * Symbiotic with SessionAware
	 * @return 
	 */
	public Map<String, Object> getSession();
	
	/**
	 * Symbiotic with ServletRequestAware
	 * @return 
	 */
	public HttpServletRequest getServletRequest();
	
	default String getFlash(String name) {
		if (getSession().containsKey(FLASH_CONTEXT)) {
			Map<String, String> map = (Map<String, String>) getSession().get(FLASH_CONTEXT);
			
			if (map.containsKey(name)) {
				return map.get(name);
			}
		}
		
		Map<String, String> map = (Map<String, String>) getServletRequest().getAttribute(FLASH_CONTEXT);
		
		if (map != null) {
			if (map.containsKey(name)) {
				return map.get(name);
			}
		}
		
		return null;
	}
	
	default void setFlash(String name, String value) {
		if (!getSession().containsKey(FLASH_CONTEXT)) {
			getSession().put(FLASH_CONTEXT, new HashMap<String, String>());
		}
		
		((Map<String, String>) getSession().get(FLASH_CONTEXT)).put(name, value);
	}
	
}
