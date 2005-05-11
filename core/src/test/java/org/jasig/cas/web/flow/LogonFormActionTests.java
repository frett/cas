/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationServiceImpl;
import org.jasig.cas.authentication.AuthenticationAttributesPopulator;
import org.jasig.cas.authentication.AuthenticationManagerImpl;
import org.jasig.cas.authentication.DefaultAuthenticationAttributesPopulator;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.handler.support.SimpleTestUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.DefaultCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.HttpBasedServiceCredentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.util.DefaultUniqueTokenIdGenerator;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.flow.util.ContextUtils;
import org.jasig.cas.web.support.WebConstants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.flow.MockRequestContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.flow.execution.servlet.HttpServletRequestEvent;

import junit.framework.TestCase;


public class LogonFormActionTests extends TestCase {

    private LogonFormAction logonFormAction;
    
    private CentralAuthenticationServiceImpl centralAuthenticationService;

    protected void setUp() throws Exception {
        this.logonFormAction = new LogonFormAction();
        
        this.centralAuthenticationService = new CentralAuthenticationServiceImpl();
        this.centralAuthenticationService.setTicketRegistry(new DefaultTicketRegistry());
                
        AuthenticationManagerImpl manager = new AuthenticationManagerImpl();
        manager.setAuthenticationAttributesPopulators(new AuthenticationAttributesPopulator[] {new DefaultAuthenticationAttributesPopulator()});
        manager.setAuthenticationHandlers(new AuthenticationHandler[] {new SimpleTestUsernamePasswordAuthenticationHandler()});
        manager.setCredentialsToPrincipalResolvers(new CredentialsToPrincipalResolver[] {new DefaultCredentialsToPrincipalResolver()});
        
        this.centralAuthenticationService.setAuthenticationManager(manager);
        this.centralAuthenticationService.setServiceTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        this.centralAuthenticationService.setTicketGrantingTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        this.centralAuthenticationService.setUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        this.logonFormAction.setCentralAuthenticationService(this.centralAuthenticationService);
        this.logonFormAction.setLoginTokens(new HashMap());
        this.logonFormAction.afterPropertiesSet();
    }

    public void testGetLoginToken() throws Exception {
        MockRequestContext context = new MockRequestContext();
        context.setOriginatingEvent(new HttpServletRequestEvent(new MockHttpServletRequest(), new MockHttpServletResponse()));
        
        assertEquals("success", this.logonFormAction.setupReferenceData(context).getId());
        assertNotNull(ContextUtils.getAttribute(context, "loginToken"));
    }
    
    public void testSubmitNoLoginToken() throws Exception {
        MockRequestContext context = new MockRequestContext();
        context.setOriginatingEvent(new HttpServletRequestEvent(new MockHttpServletRequest(), new MockHttpServletResponse()));
        
        assertEquals("error", this.logonFormAction.submit(context).getId());
    }
    
    public void testSubmitBadCredentials() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setOriginatingEvent(new HttpServletRequestEvent(request, new MockHttpServletResponse()));
        
        this.logonFormAction.setupReferenceData(context);
        final String loginToken = (String) ContextUtils.getAttribute(context, "loginToken");
        
        request.addParameter("lt", loginToken);
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test2");
        
        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context, "org.springframework.validation.BindException.credentials", new BindException(credentials, "credentials"));

        assertEquals("error", this.logonFormAction.submit(context).getId());
    }
    
    public void testSubmitProperCredentialsWithService() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setOriginatingEvent(new HttpServletRequestEvent(request, new MockHttpServletResponse()));
        
        this.logonFormAction.setupReferenceData(context);
        final String loginToken = (String) ContextUtils.getAttribute(context, "loginToken");
        
        request.addParameter("lt", loginToken);
        request.addParameter("service", "test");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");
        
        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context, "org.springframework.validation.BindException.credentials", new BindException(credentials, "credentials"));

        assertEquals("success", this.logonFormAction.submit(context).getId());
    }
    
    public void testSubmitProperCredentialsWithNoService() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setOriginatingEvent(new HttpServletRequestEvent(request, new MockHttpServletResponse()));
        
        this.logonFormAction.setupReferenceData(context);
        final String loginToken = (String) ContextUtils.getAttribute(context, "loginToken");
        
        request.addParameter("lt", loginToken);
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");
        
        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context, "org.springframework.validation.BindException.credentials", new BindException(credentials, "credentials"));

        assertEquals("noService", this.logonFormAction.submit(context).getId());
    }
    
    public void testWarn() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setOriginatingEvent(new HttpServletRequestEvent(request, new MockHttpServletResponse()));
        
        this.logonFormAction.setupReferenceData(context);
        final String loginToken = (String) ContextUtils.getAttribute(context, "loginToken");
        
        request.addParameter("lt", loginToken);
        request.addParameter("warn", "on");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");
        
        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context, "org.springframework.validation.BindException.credentials", new BindException(credentials, "credentials"));

        assertEquals("noService", this.logonFormAction.submit(context).getId());
        MockHttpServletResponse response = (MockHttpServletResponse) ContextUtils.getHttpServletResponse(context);
        assertNotNull(response.getCookie(WebConstants.COOKIE_PRIVACY));
        assertEquals(WebConstants.COOKIE_DEFAULT_FILLED_VALUE, response.getCookie(WebConstants.COOKIE_PRIVACY).getValue());
    }
    
    public void testRenewIsTrue() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setOriginatingEvent(new HttpServletRequestEvent(request, new MockHttpServletResponse()));
        
        this.logonFormAction.setupReferenceData(context);
        final String loginToken = (String) ContextUtils.getAttribute(context, "loginToken");
        
        request.addParameter("service", "true");
        request.addParameter("lt", loginToken);
        request.addParameter("renew", "true");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");

        final String ticketGrantingTicket = this.centralAuthenticationService.createTicketGrantingTicket(credentials);
        request.setCookies(new Cookie[] {new Cookie(WebConstants.COOKIE_TGC_ID, ticketGrantingTicket)});
        
        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context, "org.springframework.validation.BindException.credentials", new BindException(credentials, "credentials"));

        assertEquals("success", this.logonFormAction.submit(context).getId());
    }
    
    public void testAfterPropertiesSetNoLoginToken() {
        try {
            this.logonFormAction.setLoginTokens(null);
            this.logonFormAction.afterPropertiesSet();
            fail("Exception expected.");
        } catch (Exception e) {
            return;
        }
    }
    
    public void testAfterPropertiesSetNonDefaultTokenGenerator() throws Exception {
        this.logonFormAction.setUniqueTokenIdGenerator(new DefaultUniqueTokenIdGenerator());
        this.logonFormAction.afterPropertiesSet();
    }
    
    public void testAfterPropertiesSetDifferentCredentials() {
        try {
            this.logonFormAction.setFormObjectClass(HttpBasedServiceCredentials.class);
            this.logonFormAction.setValidator(new Validator() {

                public boolean supports(Class arg0) {
                    return true;
                }

                public void validate(Object arg0, Errors arg1) {
                }});
            this.logonFormAction.setCredentialsBinder(new CredentialsBinder() {

                public void bind(HttpServletRequest request, Credentials credentials) {
                }

                public boolean supports(Class clazz) {
                    return false;
                }});
            this.logonFormAction.afterPropertiesSet();
            fail("Exception expected.");
        } catch (Exception e) {
            return;
        }
    }
}
