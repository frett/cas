/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import java.net.URL;

/**
 * @author Scott Battaglia
 * @version $Id$
 */
public class HttpBasedServiceCredentials implements Credentials {

    private static final long serialVersionUID = 3904681574350991665L;

    final private URL callbackUrl;

    public HttpBasedServiceCredentials(URL callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * @return Returns the callbackUrl.
     */
    public URL getCallbackUrl() {
        return this.callbackUrl;
    }
}
