/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import java.net.MalformedURLException;
import java.net.URL;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.DefaultCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id: DefaultCredentialsToPrincipalResolverTests.java,v 1.2
 * 2005/02/27 05:49:26 sbattaglia Exp $
 */
public class DefaultCredentialsToPrincipalResolverTests extends TestCase {

    private CredentialsToPrincipalResolver resolver = new DefaultCredentialsToPrincipalResolver();

    public void testValidSupportsCredentials() {
        assertTrue(this.resolver.supports(new UsernamePasswordCredentials()));
    }

    public void testNullSupportsCredentials() {
        assertFalse(this.resolver.supports(null));
    }

    public void testInvalidSupportsCredentials() {
        try {
            assertFalse(this.resolver.supports(new HttpBasedServiceCredentials(
                new URL("http://www.rutgers.edu"))));
        } catch (MalformedURLException e) {
            fail("Invalid URL supplied.");
        }
    }

    public void testValidCredentials() {
        UsernamePasswordCredentials request = new UsernamePasswordCredentials();
        request.setUsername("test");
        Principal p = this.resolver.resolvePrincipal(request);

        assertEquals(p.getId(), request.getUsername());
    }

    public void testInvalidCredentials() {
        UsernamePasswordCredentials request = new UsernamePasswordCredentials();
        request.setUsername(null);
        try {
            this.resolver.resolvePrincipal(request);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("IllegalArgumentException expected.");
    }

    public void testNullCredentials() {
        try {
            this.resolver.resolvePrincipal(null);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("IllegalArgumentException expected.");
    }
}