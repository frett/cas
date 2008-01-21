/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.support;

import org.jasig.cas.util.HttpClient;

/**
 * Abstract class for handling the enabling and disabling of Single Sign Out features.
 * 
 * @author scottbattaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1.2
 *
 */
public abstract class AbstractSingleSignOutEnabledArgumentExtractor implements
    ArgumentExtractor {

    /** Whether single sign out is disabled or not. */
    private boolean disableSingleSignOut = false;
    
    /** Default instance of HttpClient. */
    private HttpClient httpClient = new HttpClient();
    
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    protected HttpClient getHttpClientIfSingleSignOutEnabled() {
        return this.disableSingleSignOut ? this.httpClient : null; 
    }
}
