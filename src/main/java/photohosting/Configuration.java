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

import java.util.stream.Collectors;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Nathan Crause <nathan@crause.name>
 */
public class Configuration {
	
	public static final String DEPLOYMENT_ENVIRONMENT_LOOKUP_NAME = "configuration/PhotoHosting/Environment".intern();
	
	private static Configuration instance;
	
	/**
	 * Retrieves the single instance.
	 * 
	 * @return 
	 * @throws IllegalStateException if this instance hasn't been initialized,
	 * either via <code>initialize()</code> or <code>test()</code>.
	 */
	public static Configuration get() {
		if (instance == null) {
			throw new IllegalStateException("You haven't initialized this object.");
		}
		
		return instance;
	}
	
	/**
	 * Initializes the singleton configuration instance. Typically invoked only
	 * once during the lifecycle started (via <code>ConfigurationListener</code>)
	 * 
	 * @throws NamingException if there was some issue gathering information
	 * from the JNDI service
	 * @throws IllegalStateException if there was already an active instance.
	 */
	public static void initialize() throws NamingException {
		if (instance != null) {
			throw new IllegalStateException("You have already initialized this object.");
		}
		
		instance = new Configuration();
		
		instance.build();
	}
	
	/**
	 * Invoke this when performing unit testing.
	 */
	public static void test() {
		instance = new Configuration();
		
		instance.buildTest();
		
		System.out.println(StringUtils.repeat("*", 80));
		System.out.println("** ENTERING TESTING CONFIGURATION");
		System.out.println(StringUtils.repeat("*", 80));
	}
	
	private Configuration() {}
	
	private Context jndiEnvironment;
	
	@Getter
	private DeploymentEnvironment deploymentEnvironment;
	
	private void build() throws NamingException {
		buildJndiEnvironmentContext();
		
		String deploymentEnvironmentString = getJndiEnvironmentString(DEPLOYMENT_ENVIRONMENT_LOOKUP_NAME, false);
		
		if (deploymentEnvironmentString == null) {
			deploymentEnvironment = DeploymentEnvironment.DEVELOPMENT;
		}
		else {
			deploymentEnvironment = DeploymentEnvironment
					.byDeclaration(deploymentEnvironmentString);

			if (deploymentEnvironment == null) {
				throw new NamingException(String.format(
						"Cannot determine deployment environment: received '%s', expected one of '%s'",
						deploymentEnvironmentString,
						DeploymentEnvironment.getAllDeclarations().stream()
								.collect(Collectors.joining(", "))));
			}
		}
	}
	
	private void buildTest() {
		deploymentEnvironment = DeploymentEnvironment.TESTING;
	}
	
	private void buildJndiEnvironmentContext() throws NamingException {
		Context initialContext = new InitialContext();
		jndiEnvironment = (Context) initialContext.lookup("java:comp/env");
	}
	
	private String getJndiEnvironmentString(String name, 
			boolean throwExceptionOrReturnNull) {
		try {
			return (String) jndiEnvironment.lookup(name);
		}
		catch (NamingException ex) {
			if (throwExceptionOrReturnNull) {
				throw new RuntimeException(
						"Failure while trying to lookup environment variable " + name, ex);
			}
			else {
				return null;
			}
		}
	}
	
	private String getJndiEnvironmentString(String name) {
		return getJndiEnvironmentString(name, true);
	}
	
	/**
	 * Used to determine if you are currently running in a testing
	 * deployment environment.
	 * 
	 * @return <code>true</code> if being run within a testing deployment
	 * environment.
	 */
	public boolean isTesting() {
		return deploymentEnvironment.equals(DeploymentEnvironment.TESTING);
	}
	
	/**
	 * Used to determine if you are currently running in a development
	 * deployment environment.
	 * 
	 * @return <code>true</code> if being run within a development deployment
	 * environment.
	 */
	public boolean isDevelopment() {
		return deploymentEnvironment.equals(DeploymentEnvironment.DEVELOPMENT);
	}
	
	/**
	 * Used to determine if you are currently running in a staging
	 * deployment environment.
	 * 
	 * @return <code>true</code> if being run within a staging deployment
	 * environment.
	 */
	public boolean isStaging() {
		return deploymentEnvironment.equals(DeploymentEnvironment.STAGING);
	}
	
	/**
	 * Used to determine if you are currently running in a live production
	 * deployment environment.
	 * 
	 * @return <code>true</code> if being run within a live production 
	 * deployment environment.
	 */
	public boolean isProduction() {
		return deploymentEnvironment.equals(DeploymentEnvironment.PRODUCTION);
	}
	
	/**
	 * Tests if the current deployment environment is a "local" one, as would
	 * conceivably be on a workstation.
	 * 
	 * @return <code>true</code> if you are in either development or testing
	 * deployment environments.
	 */
	public boolean isLocal() {
		return isTesting() || isDevelopment();
	}
	
	/**
	 * Invoked to clean up resources. On a running server, this will be
	 * automatically invoked by <code>ConfigurationListener</code>, but needs
	 * to be invoked manually in unit tests (typically in a 
	 * <code>@BeforeClass</code> block).
	 */
	public void shutdown() {
	}
	
}
