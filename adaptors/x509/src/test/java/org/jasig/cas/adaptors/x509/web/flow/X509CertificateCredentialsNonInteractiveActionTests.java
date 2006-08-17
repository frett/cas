/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.x509.web.flow;

import java.security.cert.X509Certificate;

import org.jasig.cas.CentralAuthenticationServiceImpl;
import org.jasig.cas.adaptors.x509.authentication.handler.support.X509CredentialsAuthenticationHandler;
import org.jasig.cas.adaptors.x509.authentication.principal.AbstractX509CertificateTests;
import org.jasig.cas.adaptors.x509.authentication.principal.X509CertificateCredentialsToSerialNumberPrincipalResolver;
import org.jasig.cas.adaptors.x509.web.flow.X509CertificateCredentialsNonInteractiveAction;
import org.jasig.cas.authentication.AuthenticationManagerImpl;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.test.MockRequestContext;


public class X509CertificateCredentialsNonInteractiveActionTests extends
    AbstractX509CertificateTests {
    
    private X509CertificateCredentialsNonInteractiveAction action;

    protected void setUp() throws Exception {
        this.action = new X509CertificateCredentialsNonInteractiveAction();
        final CentralAuthenticationServiceImpl centralAuthenticationService = new CentralAuthenticationServiceImpl();
        centralAuthenticationService.setTicketRegistry(new DefaultTicketRegistry());

        final AuthenticationManagerImpl authenticationManager = new AuthenticationManagerImpl();

        final X509CredentialsAuthenticationHandler a = new X509CredentialsAuthenticationHandler();
        a.setTrustedIssuerDnPattern("JA-SIG");
        a.afterPropertiesSet();
        
        authenticationManager.setAuthenticationHandlers(new AuthenticationHandler[] {a});
        authenticationManager.setCredentialsToPrincipalResolvers(new CredentialsToPrincipalResolver[] {new X509CertificateCredentialsToSerialNumberPrincipalResolver()});
        authenticationManager.afterPropertiesSet();
        
        centralAuthenticationService.setTicketGrantingTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        centralAuthenticationService.setServiceTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        centralAuthenticationService.setServiceTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        centralAuthenticationService.setTicketGrantingTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        centralAuthenticationService.setAuthenticationManager(authenticationManager);
        centralAuthenticationService.afterPropertiesSet();
        
        this.action.setTicketGrantingTicketCookieGenerator(new CookieGenerator());
        this.action.setWarnCookieGenerator(new CookieGenerator());
        this.action.setCentralAuthenticationService(centralAuthenticationService);
    }
    
    public void testNoCredentialsResultsInError() throws Exception {
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(), new MockHttpServletResponse()));
        assertEquals("error", this.action.execute(context).getId());
//        assertEquals("error", this.action.doExecuteInternal(context, "test", "test", false, false, false).getId());
    }
    
    public void testCredentialsResultsInSuccess() throws Exception {
        final MockRequestContext context = new MockRequestContext();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("javax.servlet.request.X509Certificate", new X509Certificate[] {VALID_CERTIFICATE});
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
        assertEquals("success", this.action.execute(context).getId());
//        assertEquals("success", this.action.doExecuteInternal(context, "test", "test", false, false, false).getId());
        
    }
    
    

}
