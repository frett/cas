/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.jasig.cas.authentication.principal.Principal;

/**
 * Interface representing the results of a valid request for Authentication.
 * 
 * @author Dmitriy Kopylenko
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public interface Authentication extends Serializable {

    /**
     * Retrieve the principal that was authenticated.
     * 
     * @return the principal that was authenticated.
     */
    Principal getPrincipal();

    /**
     * Retrieve that date/time that the authentication occurred.
     * 
     * @return the date/time the authentication occurred.
     */
    Date getAuthenticatedDate();

    /**
     * Retrieve any additional authentication information.
     * 
     * @return any additional authentication information.
     */
    Map getAttributes();
}
