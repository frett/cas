/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler.support;

import java.util.Collection;

import org.jasig.cas.authentication.AuthenticationRequest;
import org.jasig.cas.authentication.UsernamePasswordAuthenticationRequest;


/**
 * Class to provide a list of users to automatically reject.
 * 
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class RejectUsersAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	private Collection users;

	/**
	 * 
	 * @see org.jasig.cas.authentication.handler.AuthenticationHandler#authenticate(org.jasig.cas.authentication.AuthenticationRequest)
	 */
	public boolean authenticate(final AuthenticationRequest request) {
		final UsernamePasswordAuthenticationRequest uRequest = (UsernamePasswordAuthenticationRequest) request;
		
		return !users.contains(uRequest.getUserName());
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (users == null) {
			throw new IllegalStateException("You must provide a list of users that are not allowed to use the system.");
		}
	}
	/**
	 * @param users The users to set.
	 */
	public void setUsers(final Collection users) {
		this.users = users;
	}
}
