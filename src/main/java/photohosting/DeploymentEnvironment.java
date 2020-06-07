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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This enumeration is used for the runtime deployment environment of the 
 * webapp.
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public enum DeploymentEnvironment {
	
	DEVELOPMENT("development", "dev"),
	TESTING("testing", "test"),
	STAGING("staging", "stage", "staged"),
	PRODUCTION("prod", "production", "live");
	
	/**
	 * Each environment can have multiple ways of being declared.
	 */
	private final List<String> declarations;
	
	DeploymentEnvironment(String ... declarations) {
		this.declarations = Arrays.asList(declarations);
	}
	
	/**
	 * Finds a <code>DeploymentEnvironment</code> based on a string declaration.
	 * 
	 * @param declaration the declaration to use in searching.
	 * @return the matching enumeration value, or null (if it's not found).
	 */
	public static DeploymentEnvironment byDeclaration(String declaration) {
		for (DeploymentEnvironment environment : values()) {
			if (environment.declarations.contains(declaration)) {
				return environment;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a list of all possible valid declarations.
	 * 
	 * @return list of strings
	 */
	public static List<String> getAllDeclarations() {
		List<String> list = new ArrayList<>();
		
		for (DeploymentEnvironment environment : values()) {
			list.addAll(environment.declarations);
		}
		
		return list;
	}
	
}
