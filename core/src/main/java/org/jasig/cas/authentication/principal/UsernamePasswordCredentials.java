/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Domain object to bind the request for an authentication to a command object.
 * This will bind all of the CAS 2.0 parameters.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class UsernamePasswordCredentials implements Credentials {

    /** Unique ID for serialization. */
    private static final long serialVersionUID = -8343864967200862794L;

    /** The username. */
    private String username;

    /** The password. */
    private String password;

    /**
     * @return Returns the password.
     */
    public final String getPassword() {
        return this.password;
    }

    /**
     * @param password The password to set.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @return Returns the userName.
     */
    public final String getUsername() {
        return this.username;
    }

    /**
     * @param userName The userName to set.
     */
    public final void setUsername(final String userName) {
        this.username = userName;
    }

    public final String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        return toStringBuilder.append("userName", this.username).toString();
    }
}
