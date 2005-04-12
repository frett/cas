/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public final class MockAuthentication implements Authentication {

    private static final long serialVersionUID = 3689065132152010039L;

    private Principal principal = new SimplePrincipal("test");

    private Map attributes = new HashMap();

    private Date authenticatedDate = new Date();

    public MockAuthentication(Principal p) {
        this.principal = p;
    }

    public MockAuthentication() {
        // nothing to do;
    }

    public Principal getPrincipal() {
        return this.principal;
    }

    public Date getAuthenticatedDate() {
        return this.authenticatedDate;
    }

    public Map getAttributes() {
        return this.attributes;
    }
}
