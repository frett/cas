/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.trusted.web.flow;

import org.jasig.cas.CentralAuthenticationServiceImpl;
import org.jasig.cas.adaptors.trusted.authentication.handler.support.PrincipalBearingCredentialsAuthenticationHandler;
import org.jasig.cas.adaptors.trusted.authentication.principal.PrincipalBearingCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.AuthenticationManagerImpl;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.web.CasArgumentExtractor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.test.MockRequestContext;

import junit.framework.TestCase;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.5
 *
 */
public class PrincipalFromRequestRemoteUserNonInteractiveCredentialsActionTests
    extends TestCase {

    private PrincipalFromRequestRemoteUserNonInteractiveCredentialsAction action;
    
    protected void setUp() throws Exception {
        this.action = new PrincipalFromRequestRemoteUserNonInteractiveCredentialsAction();
        final CasArgumentExtractor casArgumentExtractor = new CasArgumentExtractor(new CookieGenerator(), new CookieGenerator());
        this.action.setCasArgumentExtractor(casArgumentExtractor);
        
        final CentralAuthenticationServiceImpl centralAuthenticationService = new CentralAuthenticationServiceImpl();
        centralAuthenticationService.setTicketRegistry(new DefaultTicketRegistry());

        final AuthenticationManagerImpl authenticationManager = new AuthenticationManagerImpl();
   
        authenticationManager.setAuthenticationHandlers(new AuthenticationHandler[] {new PrincipalBearingCredentialsAuthenticationHandler()});
        authenticationManager.setCredentialsToPrincipalResolvers(new CredentialsToPrincipalResolver[] {new PrincipalBearingCredentialsToPrincipalResolver()});
        authenticationManager.afterPropertiesSet();
        
        centralAuthenticationService.setTicketGrantingTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        centralAuthenticationService.setServiceTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        centralAuthenticationService.setServiceTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        centralAuthenticationService.setTicketGrantingTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        centralAuthenticationService.setAuthenticationManager(authenticationManager);
        centralAuthenticationService.afterPropertiesSet();
        
        this.action.setCentralAuthenticationService(centralAuthenticationService);
    }
    
    public void testRemoteUserExists() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteUser("test");
        
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
        
        assertEquals("success", this.action.execute(context).getId());
    }
    
    public void testRemoteUserDoesntExists() throws Exception {
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(), new MockHttpServletResponse()));
        
        assertEquals("error", this.action.execute(context).getId());
    }
    
}
