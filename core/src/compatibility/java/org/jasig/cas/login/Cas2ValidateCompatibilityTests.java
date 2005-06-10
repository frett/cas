/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Common testcases for /serviceValidate and /proxyValidate.
 * @version $Revision$ $Date$
 * @since 3.0
 */
public abstract class Cas2ValidateCompatibilityTests extends AbstractCompatibilityTests {

	public final String PROXY_RECEPTOR_URL_PROPERTY = "pgtreceptor.url";
	
    public Cas2ValidateCompatibilityTests() throws IOException {
        super();
    }

    public Cas2ValidateCompatibilityTests(String name) throws IOException {
        super(name);
    }
    
    /**
     * Returns /serviceValidate in the case of /serviceValidate, 
     * and /proxyValidate in the case of /proxyValidate.
     * Concrete subclasses implement this method to configure the common
     * tests defined here.
     * @return
     */
    protected abstract String getValidationPath();
    
    protected final String getProxyCallbackUrl() {
    	return getProperties().getProperty(PROXY_RECEPTOR_URL_PROPERTY);
    }
    
    public void testNoParameters() {
        beginAt(getValidationPath());
        assertTextPresent("cas:authenticationFailure");
        
        // TODO: actually test the validation response XML.
    }
    
    public void testBadServiceTicket() throws UnsupportedEncodingException {
        final String service = getServiceUrl();
        beginAt(getValidationPath() + "?service=" + URLEncoder.encode(service, "UTF-8") + "&ticket=test");
        
        assertTextPresent("cas:authenticationFailure");
        
        // TODO: do more to test that the response is actually XML, etc. etc.
    }
    
    /**
     * Test validation of a valid service ticket and that service tickets are
     * not multiply validatable.
     * @throws IOException
     */
    public void testProperCredentialsAndServiceTicket() throws IOException {
        final String service = getServiceUrl();
        String encodedService = URLEncoder.encode(service, "UTF-8");
        beginAt("/login?service=" + encodedService);
        setFormElement("username", getUsername());
        setFormElement("password", getGoodPassword());
        submit();
        
        // read the service ticket
        
        String serviceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // great, now we have a ticket
        
        // let's validate it
        
        beginAt(getValidationPath() + "?service=" + encodedService + "&" + "ticket=" + serviceTicket);
        
        assertTextPresent("cas:authenticationSuccess");
        
        // this assertion may be too strict.  How does whitespace work here?
        assertTextPresent("<cas:user>" + getUsername() + "</cas:user>");
        
        // TODO: do more to test that the response is actually XML, etc. etc.
        
        // let's validate it again and ensure that we cannot again validate
        // the ticket
        
        beginAt(getValidationPath() + "?service=" + encodedService + "&" + "ticket=" + serviceTicket);
        assertTextPresent("cas:authenticationFailure");
        
        // TODO: do more to test that the response is actually XML, etc. etc.
        
    }
    
    /**
     * Test that renew=true, when specified both at login and ticket validation, 
     * validation succeeds.
     * @throws IOException
     */
    public void testRenew() throws IOException {
        final String service = getServiceUrl();
        String encodedService = URLEncoder.encode(service, "UTF-8");
        beginAt("/login?renew=true&service=" + encodedService);
        setFormElement("username", getUsername());
        setFormElement("password", getGoodPassword());
        submit();
        
        // read the service ticket
        
        String serviceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // great, now we have a ticket
        
        // let's validate it
        
        beginAt(getValidationPath() + "?renew=true&service=" + encodedService + "&" + "ticket=" + serviceTicket);
        
        assertTextPresent("cas:authenticationSuccess");
    }
    
    /**
     * Test that renew=true, when specified only at ticket validation, 
     * validation succeeds if username, password were presented at login even
     * though renew wasn't set then.
     * @throws IOException
     */
    public void testAccidentalRenew() throws IOException {
        final String service = getServiceUrl();
        String encodedService = URLEncoder.encode(service, "UTF-8");
        beginAt("/login?service=" + encodedService);
        setFormElement("username", getUsername());
        setFormElement("password", getGoodPassword());
        submit();
        
        // read the service ticket
        
        String serviceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // great, now we have a ticket
        
        // let's validate it
        
        beginAt(getValidationPath() + "?renew=true&service=" + encodedService + "&" + "ticket=" + serviceTicket);
        
        assertTextPresent("<cas:authenticationSuccess>");
        assertTextPresent("<cas:user>" + getUsername() + "</cas:user>");
    }
    
    /**
     * Test that renew at ticket validation blocks validation of a ticket
     * vended via SSO.
     * @throws IOException
     */
    public void testRenewBlocksSsoValidation() throws IOException {
    	
    	// initial authentication
        final String firstService = getServiceUrl();
        final String encodedFirstService = URLEncoder.encode(firstService, "UTF-8");
        beginAt("/login?service=" + encodedFirstService);
        setFormElement("username", getUsername());
        setFormElement("password", getGoodPassword());
        submit();
        
        String firstServiceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // that established SSO.  Now let's get another ticket via SSO
        
        final String secondService= "http://www.uportal.org/";
        final String encodedSecondService = URLEncoder.encode(secondService, "UTF-8");
        
        beginAt("/login?service=" + encodedSecondService);
        
        // read the service ticket
        
        String secondServiceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // let's validate the second (non-renew) ticket.
        
        beginAt(getValidationPath() + "?renew=true&service=" + encodedSecondService + "&ticket=" + secondServiceTicket);
        
        assertTextPresent("cas:authenticationFailure");
        
        // TODO: test the authentication failure response in more detail
        
        assertTextNotPresent("<cas:user>");
        
        // however, we can validate the first ticket with renew=true.
        
        beginAt(getValidationPath() + "?renew=true&service=" + encodedFirstService + "&ticket=" + firstServiceTicket);
        
        assertTextPresent("cas:authenticationSuccess");
        assertTextPresent("<cas:user>" + getUsername() + "</cas:user>");
        // TODO: assert more about the response
        
    }
    
    /**
     * Test best-effort ticket validation when a specified proxy callback handler
     * doesn't really exist.
     * @throws IOException
     */
    public void testBrokenProxyCallbackUrl() throws IOException {
    	
        final String service = getServiceUrl();
        String encodedService = URLEncoder.encode(service, "UTF-8");
        beginAt("/login?service=" + encodedService);
        setFormElement("username", getUsername());
        setFormElement("password", getGoodPassword());
        submit();
        
        // read the service ticket
        
        String serviceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // great, now we have a ticket
        
        // let's validate it, specifying a bogus pgt callback
        
        String encodedProxyCallbackUrl = URLEncoder.encode("https://secure.its.yale.edu/cas/noexist", "UTF-8");
        
        beginAt(getValidationPath() + "?renew=true&service=" + encodedService + "&" + "ticket=" + serviceTicket + "&pgtUrl=" + encodedProxyCallbackUrl);
        
        assertTextPresent("<cas:authenticationSuccess>");
        assertTextPresent("<cas:user>" + getUsername() + "</cas:user>");
        
        // no pgtiou because failure in sending pgt to specified receptor URL.
        assertTextNotPresent("<cas:pgtiou>");
    	
    }
    
    public void testPgtAcquisition() throws IOException {
    	
        final String service = getServiceUrl();
        String encodedService = URLEncoder.encode(service, "UTF-8");
        beginAt("/login?service=" + encodedService);
        setFormElement("username", getUsername());
        setFormElement("password", getGoodPassword());
        submit();
        
        // read the service ticket
        
        String serviceTicket = LoginHelper.serviceTicketFromResponse(getDialog().getResponse());
        
        // great, now we have a ticket
        
        // let's validate it, specifying a bogus pgt callback
        
        String encodedProxyCallbackUrl = URLEncoder.encode(getProxyCallbackUrl(), "UTF-8");
        
        beginAt(getValidationPath() + "?renew=true&service=" + encodedService + "&" + "ticket=" + serviceTicket + "&pgtUrl=" + encodedProxyCallbackUrl);
        
        assertTextPresent("<cas:authenticationSuccess>");
        assertTextPresent("<cas:user>" + getUsername() + "</cas:user>");
        // pgtiou because success in sending pgt
        assertTextPresent("<cas:proxyGrantingTicket>");
    	
    }
    
}
