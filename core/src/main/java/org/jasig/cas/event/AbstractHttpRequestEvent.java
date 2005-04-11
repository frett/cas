/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.event;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract implementation of HttpRequestEvent that defines the
 * getRequest method so that implementing classes do not need to.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public class AbstractHttpRequestEvent extends AbstractEvent implements
    HttpRequestEvent {
    
    private final HttpServletRequest request;
    
    public AbstractHttpRequestEvent(final HttpServletRequest request) {
        this.request = request;
    }

    public final HttpServletRequest getRequest() {
        return this.request;
    }
}
