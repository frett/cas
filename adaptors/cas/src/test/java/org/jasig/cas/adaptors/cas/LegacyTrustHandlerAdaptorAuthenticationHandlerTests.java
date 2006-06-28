/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */

package org.jasig.cas.adaptors.cas;

import javax.servlet.ServletRequest;

import org.jasig.cas.adaptors.cas.mock.MockTrustHandler;
import org.springframework.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;

/**
 * Testcase for LegacyTrustAdaptorAuthenticationHandler.
 * 
 * @version $Revision$ $Date$
 */
public class LegacyTrustHandlerAdaptorAuthenticationHandlerTests extends
    TestCase {

    private LegacyTrustHandlerAdaptorAuthenticationHandler legacyTrustAdaptor;

    protected void setUp() throws Exception {
        super.setUp();
        this.legacyTrustAdaptor = new LegacyTrustHandlerAdaptorAuthenticationHandler();
        this.legacyTrustAdaptor.setTrustHandler(new MockTrustHandler());
        this.legacyTrustAdaptor.afterPropertiesSet();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Currently, tests that the adaptor does not support null credentials,
     * supports an instance of LegacyCasTrustedCredentials, and does not support
     * an instance of LegacyCasCredentials.
     */
    public void testSupports() {

        assertFalse(this.legacyTrustAdaptor.supports(null));

        LegacyCasTrustedCredentials goodCred = new LegacyCasTrustedCredentials();

        assertTrue(this.legacyTrustAdaptor.supports(goodCred));

        LegacyCasCredentials badCred = new LegacyCasCredentials();

        assertFalse(this.legacyTrustAdaptor.supports(badCred));

    }

    /**
     * Test demonstrating that throws NPE when no TrustHandler injected. We
     * could guarantee that our TrustHandler dependency is never null by making
     * using constructor dependency injection instead of setter dependency
     * injection. Issue is mitigated by use of afterPropertiesSet() to assert
     * dependecy was set.
     * 
     * @throws AuthenticationException
     */
    public void testNoTrustHandler() {
        LegacyCasTrustedCredentials trustedCredentials = new LegacyCasTrustedCredentials();
        this.legacyTrustAdaptor.setTrustHandler(null);

        try {
            this.legacyTrustAdaptor.authenticate(trustedCredentials);
        } catch (NullPointerException npe) {
            return;
        }

        fail();
    }

    /**
     * Test a successful authentication.
     * 
     * @throws AuthenticationException as one failure modality
     */
    public void testAuthenticate() {
        LegacyCasTrustedCredentials trustedCredentials = new LegacyCasTrustedCredentials();

        ServletRequest request = new MockHttpServletRequest();

        trustedCredentials.setServletRequest(request);

        MockTrustHandler mockTrustHandler = new MockTrustHandler();

        mockTrustHandler.setUserName("testUser");

        this.legacyTrustAdaptor.setTrustHandler(mockTrustHandler);

        assertTrue(this.legacyTrustAdaptor.authenticate(trustedCredentials));

        assertSame(request, mockTrustHandler.getRequest());

    }

    /**
     * Test an unsuccessful authentication.
     * 
     * @throws AuthenticationException - as one failure modality
     */
    public void testAuthenticateFails() {
        LegacyCasTrustedCredentials trustedCredentials = new LegacyCasTrustedCredentials();

        ServletRequest request = new MockHttpServletRequest();

        trustedCredentials.setServletRequest(request);

        MockTrustHandler mockTrustHandler = new MockTrustHandler();

        mockTrustHandler.setUserName(null);

        this.legacyTrustAdaptor.setTrustHandler(mockTrustHandler);

        assertFalse(this.legacyTrustAdaptor.authenticate(trustedCredentials));

        assertSame(request, mockTrustHandler.getRequest());

    }

    /**
     * Test that throws UnsupportedCredentialsException for an unsupported
     * credential.
     * 
     * @throws AuthenticationException
     */
    public void testAuthenticateUnsupported() {
        LegacyCasCredentials badCredentials = new LegacyCasCredentials();
        assertFalse(this.legacyTrustAdaptor.supports(badCredentials));
    }
}
