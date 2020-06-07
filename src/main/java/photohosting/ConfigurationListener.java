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

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener which sets up and tears down the
 * singleton <code>Configuration</code> instance.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class ConfigurationListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		contextEvent.getServletContext().log("ConfigurationListener.contextInitialized");
		
		try {
			Configuration.initialize();
		}
		catch (NamingException ex) {
			throw new RuntimeException("Failure during configuration initialization", ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		Configuration.get().shutdown();
	}
	
}
