/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.TestUtils;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.PlainTextPasswordEncoder;
import org.jasig.cas.authentication.handler.support.SimpleTestUsernamePasswordAuthenticationHandler;

import junit.framework.TestCase;

/**
 * Test of the simple username/password handler
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class SimpleTestUsernamePasswordHandlerTests extends TestCase {

    private SimpleTestUsernamePasswordAuthenticationHandler authenticationHandler;

    protected void setUp() throws Exception {
        this.authenticationHandler = new SimpleTestUsernamePasswordAuthenticationHandler();
        this.authenticationHandler
            .setPasswordEncoder(new PlainTextPasswordEncoder());
        this.authenticationHandler.afterPropertiesSet();
    }

    public void testSupportsProperUserCredentials() {
        assertTrue(this.authenticationHandler.supports(TestUtils
            .getCredentialsWithSameUsernameAndPassword()));
    }

    public void testDoesntSupportBadUserCredentials() {
        assertFalse(this.authenticationHandler.supports(TestUtils
            .getHttpBasedServiceCredentials()));
    }

    public void testValidUsernamePassword() throws AuthenticationException {
        assertTrue(this.authenticationHandler.authenticate(TestUtils
            .getCredentialsWithSameUsernameAndPassword()));
    }

    public void testInvalidUsernamePassword() {
        try {
            assertFalse(this.authenticationHandler
                .authenticate(TestUtils
                    .getCredentialsWithDifferentUsernameAndPassword("test",
                        "test1")));
        } catch (AuthenticationException ae) {
            // this is okay
        }
    }

    public void testNullUsernamePassword() {
        try {
            assertFalse(this.authenticationHandler.authenticate(TestUtils
                .getCredentialsWithSameUsernameAndPassword(null)));
        } catch (AuthenticationException ae) {
            // this is okay
        }
    }

    public void testAfterPropertiesSet() throws Exception {
        this.authenticationHandler.afterPropertiesSet();
    }
}