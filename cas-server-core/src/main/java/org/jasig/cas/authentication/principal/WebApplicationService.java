/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

/**
 * Represents a service using CAS that comes from the web.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public interface WebApplicationService extends Service {

    /**
     * Constructs the url to redirect the service back to.
     * 
     * @param ticketId the service ticket to provide to the service.
     * @return the redirect url.
     */
    String getRedirectUrl(String ticketId);
}
