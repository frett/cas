/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler.support;

import java.util.Collection;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * Class to provide a list of users to automatically reject.
 * 
 * @author Scott Battaglia
 * @version $Id: RejectUsersAuthenticationHandler.java,v 1.3 2005/03/07 21:00:05
 * sbattaglia Exp $
 */
public class RejectUsersAuthenticationHandler extends
    AbstractUsernamePasswordAuthenticationHandler {

    private Collection users;

    public boolean authenticateInternal(final Credentials credentials) {
        final UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials)credentials;

        if (usernamePasswordCredentials.getUserName() == null)
            return false;
        return !this.users.contains(usernamePasswordCredentials.getUserName());
    }

    public void afterPropertiesSet() throws Exception {
        if (this.users == null) {
            throw new IllegalStateException(
                "You must provide a list of users that are not allowed to use the system.");
        }
    }

    /**
     * @param users The users to set.
     */
    public void setUsers(final Collection users) {
        this.users = users;
    }
}