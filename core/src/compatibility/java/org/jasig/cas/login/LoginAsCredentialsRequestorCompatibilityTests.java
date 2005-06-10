/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jasig.cas.web.support.WebConstants;

/**
 * 
 * @author Scott Battaglia
 * @author Drew Mazurek
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public class LoginAsCredentialsRequestorCompatibilityTests extends AbstractLoginCompatibilityTests {
    
    
    public LoginAsCredentialsRequestorCompatibilityTests() throws IOException {
        super();
    }
    
    public LoginAsCredentialsRequestorCompatibilityTests(final String name) throws IOException {
        super(name);
    }

    public void testLoginWithNoParams() {
    	final String URL = "/login";
    	beginAt(URL);
    	assertFormElementPresent(WebConstants.LOGIN_TOKEN);
    }
    
    public void testGatewayWithServiceWithNoTgt() throws UnsupportedEncodingException {
        final String GATEWAY = "true";
        final String SERVICE = URLEncoder.encode("http://www.cnn.com", "UTF-8");
        final String URL = "/login?service=" + SERVICE + "&gateway=" + GATEWAY;
         
        beginAt(URL);
        assertTextPresent("cnn.com");
    }
    
    public void testGatewayFalseEqualsGatewayTrueWithServiceWithNoTgt() throws UnsupportedEncodingException {
        final String GATEWAY = "false";
        final String SERVICE = URLEncoder.encode("http://www.cnn.com", "UTF-8");
        final String URL = "/login?service=" + SERVICE + "&gateway=" + GATEWAY;
         
        beginAt(URL);
        assertTextPresent("cnn.com");
    }
    
    public void testServiceWithSingleSignOn() {
        setFormElement(FORM_USERNAME, getUsername());
        setFormElement(FORM_PASSWORD, getGoodPassword());
        final String URL = "/login";
        submit();
        assertCookiePresent(WebConstants.COOKIE_TGC_ID);
        beginAt(URL);
        assertFormNotPresent(FORM_USERNAME);
    }
    
    public void testGatewayWithNoService() {
        final String GATEWAY = "notNull";
        final String URL = "/login?gateway=" + GATEWAY;
        
        beginAt(URL);
        assertFormElementPresent(WebConstants.LOGIN_TOKEN);
    }
    
    public void testGatewayWithServiceWithTgt() {
    	final String GATEWAY = "notNull";
    	final String SERVICE = "http://www.yale.edu";
        final String URLNOGW = "/login?service=" + SERVICE;
        final String URLGW = "/login?service=" + SERVICE + "&gateway=" + GATEWAY;
        setFormElement(FORM_USERNAME, getUsername());
        setFormElement(FORM_PASSWORD, getGoodPassword());
        submit();
        assertCookiePresent(WebConstants.COOKIE_TGC_ID);
        beginAt(URLGW);
        assertTextPresent(SERVICE);
        assertTextPresent("ticket=ST-");
        assertFormNotPresent();
    }
    
    /**
     * Test that when renew=true, an existing TGT still causes CAS to render
     * the login UI.
     * @throws UnsupportedEncodingException
     */
    public void testExistingTgtRenewEqualsTrue() throws UnsupportedEncodingException {
    	final String service = "http://www.yale.edu";
    	final String encodedService = URLEncoder.encode(service, "UTF-8");
        final String renewUrl = "/login?service=" + encodedService + "&renew=true";
        
        
        setFormElement(FORM_USERNAME, getUsername());
        setFormElement(FORM_PASSWORD, getGoodPassword());
        submit();
        assertCookiePresent(WebConstants.COOKIE_TGC_ID);
        beginAt(renewUrl);
        
        // test that we're at the login screen (no ST was issued).
        assertFormPresent();
        assertFormElementPresent(WebConstants.LOGIN_TOKEN);
    }
    
    public void testTrustHandling() {
        // TODO test trust handling
    }
    
    public void testInitialFormParameters() {
        assertFormElementPresent(FORM_USERNAME);
        assertFormElementPresent(FORM_PASSWORD);
        assertFormElementPresent(WebConstants.LOGIN_TOKEN);
        assertTextPresent("LT-");
    }
}
