/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler.support;

import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.jasig.cas.authentication.AuthenticationRequest;
import org.jasig.cas.authentication.UsernamePasswordAuthenticationRequest;
import org.jasig.cas.util.PasswordTranslator;
import org.jasig.cas.util.support.PlainTextPasswordTranslator;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * Class that given a table, username field and password field will query a database table with the provided
 * encryption technique to see if the user exists.
 * 
 * This class provides a failover.  If provided multiple datasources, on a DataAccessResourceFailureException
 * the next datasource in the list will be tried.
 * 
 * This class defaults to a PasswordTranslator of PlainTextPasswordTranslator.
 * 
 * @author Scott Battaglia
 * @version $Id$
 *
 */

// TODO: is this efficient enough??
public class SearchModeSearchDatabaseAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	private static final String SQL_PREFIX = "Select count(*) from ";
	private static final PasswordTranslator DEFAULT_PASSWORD_TRANSLATOR = new PlainTextPasswordTranslator();
	
	private List dataSources;
	private String fieldUser;
	private String fieldPassword;
	private String tableUsers;
	private PasswordTranslator passwordTranslator = DEFAULT_PASSWORD_TRANSLATOR;

	/**
	 * 
	 * @see org.jasig.cas.authentication.handler.AuthenticationHandler#authenticate(org.jasig.cas.authentication.AuthenticationRequest)
	 */
	public boolean authenticate(final AuthenticationRequest request) {
		final UsernamePasswordAuthenticationRequest uRequest = (UsernamePasswordAuthenticationRequest) request;
		final String SQL = SQL_PREFIX + tableUsers + " Where " + fieldUser + " = ? And " + fieldPassword + " = ?";
		
		for (Iterator iter = dataSources.iterator(); iter.hasNext();) {
			
			try {
				final DataSource dataSource = (DataSource) iter.next();
				final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				final String encyptedPassword = passwordTranslator.translate(uRequest.getPassword());
				int count = jdbcTemplate.queryForInt(SQL, new Object[] {uRequest.getUserName(), encyptedPassword});
				
				if (count > 0)
					return true;
			} catch (DataAccessResourceFailureException e) {
				// this means the server failed!!!
			}
			catch (DataAccessException dae) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (passwordTranslator == null || dataSources == null || fieldPassword == null || fieldUser == null || tableUsers == null) {
			throw new IllegalStateException("passwordTranslator, dataSources, fieldPassword, fieldUser and tableUsers must be set on " + this.getClass().getName());
		}
	}

	/**
	 * @param dataSources The dataSources to set.
	 */
	public void setDataSources(final List dataSources) {
		this.dataSources = dataSources;
	}
	/**
	 * @param fieldPassword The fieldPassword to set.
	 */
	public void setFieldPassword(final String fieldPassword) {
		this.fieldPassword = fieldPassword;
	}
	/**
	 * @param fieldUser The fieldUser to set.
	 */
	public void setFieldUser(final String fieldUser) {
		this.fieldUser = fieldUser;
	}
	/**
	 * @param passwordTranslator The passwordTranslator to set.
	 */
	public void setPasswordTranslator(final PasswordTranslator passwordTranslator) {
		this.passwordTranslator = passwordTranslator;
	}
	/**
	 * @param tableUsers The tableUsers to set.
	 */
	public void setTableUsers(final String tableUsers) {
		this.tableUsers = tableUsers;
	}
}
