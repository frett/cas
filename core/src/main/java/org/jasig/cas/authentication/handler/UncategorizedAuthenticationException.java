/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler;

/**
 * Generic abstract exception to extend when you don't know what the heck is
 * going on.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public abstract class UncategorizedAuthenticationException extends
    AuthenticationException {

    public UncategorizedAuthenticationException(final String code) {
        super(code);
    }

    public UncategorizedAuthenticationException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
