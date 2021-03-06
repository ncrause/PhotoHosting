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

import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import photohosting.services.DatabaseAccessServiceImpl;
import photohosting.utils.DatabaseBootstrap;

/**
 * Web application lifecycle listener.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class DatabaseBootstrapListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			DatabaseBootstrap bootstrapper = new DatabaseBootstrap(
					sce.getServletContext(), new DatabaseAccessServiceImpl());
			
			bootstrapper.bootstrap();
		}
		catch (IOException ex) {
			throw new RuntimeException("Critical failure encountered during database bootstrap", ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// we don't much care
	}
}
