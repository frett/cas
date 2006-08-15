/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.radius;

import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * Interface representing a Radius Server.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.6
 */
public interface RadiusServer {

    /**
     * Method to authenticate a set of credentials.
     * 
     * @param credentials the credentials to authenticate.
     * @return true if authenticated, false otherwise.
     */
    boolean authenticate(UsernamePasswordCredentials credentials);

}
