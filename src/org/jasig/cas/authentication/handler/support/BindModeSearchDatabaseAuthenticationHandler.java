/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler.support;

import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.jasig.cas.authentication.AuthenticationRequest;
import org.jasig.cas.authentication.UsernamePasswordAuthenticationRequest;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


/**
 * This class attempts to authenticate the user by opening a connection to the database with the provided
 * username and password.
 * 
 * Servers are provided as a Properties class with the key being the URL and the property being the type
 * of database driver needed.
 * 
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class BindModeSearchDatabaseAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	private Properties servers;
	
	/**
	 * 
	 * @see org.jasig.cas.authentication.handler.AuthenticationHandler#authenticate(org.jasig.cas.authentication.AuthenticationRequest)
	 */
	public boolean authenticate(final AuthenticationRequest request) {
		final UsernamePasswordAuthenticationRequest uRequest = (UsernamePasswordAuthenticationRequest) request;
		final String username = uRequest.getUserName();
		final String password = uRequest.getPassword();
		
		for (Iterator iter = servers.keySet().iterator(); iter.hasNext();) {
			final DataSource dataSource;
			final String url    = (String) iter.next();
			final String driver = servers.getProperty(url);
			try {
				dataSource = new DriverManagerDataSource(driver, url, username, password);
				return true;
			} catch (CannotGetJdbcConnectionException e) {
				// log ....user could not connect to db or was 
			}
		}
		return false;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (servers == null) {
			throw new IllegalStateException("The drivers and urls must be set on " + this.getClass().getName());
		}
	}

	/**
	 * @param servers The servers to set.
	 */
	public void setServers(final Properties servers) {
		this.servers = servers;
	}
}
