/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication;

import java.util.Date;

import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Principal;

/**
 * Default immutable implementation of Authentication.
 * 
 * @author Dmitriy Kopylenko
 * @author Scott Battaglia
 */
public final class ImmutableAuthentication implements Authentication {

    private final Credentials credentials;

    private final Principal principal;

    private final Date authenticatedDate;

    private final Object attributes;

    public ImmutableAuthentication(final Credentials credentials, final Principal principal, final Object attributes) {
        this.credentials = credentials;
        this.principal = principal;
        this.attributes = attributes;
        this.authenticatedDate = new Date();
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public Principal getPrincipal() {
        return this.principal;
    }

    public Date getAuthenticatedDate() {
        return this.authenticatedDate;
    }

    public Object getAttributes() {
        return this.attributes;
    }

}