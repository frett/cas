/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.CentralAuthenticationServiceImpl;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationMetaDataPopulator;
import org.jasig.cas.authentication.AuthenticationManagerImpl;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.handler.support.SimpleTestUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.HttpBasedServiceCredentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.flow.util.ContextUtils;
import org.jasig.cas.web.support.WebConstants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.flow.MockRequestContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.flow.execution.servlet.ServletEvent;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 */
public class LoginFormActionTests extends TestCase {

    private LoginFormAction logonFormAction;

    private CentralAuthenticationServiceImpl centralAuthenticationService;

    protected void setUp() throws Exception {
        this.logonFormAction = new LoginFormAction();

        this.centralAuthenticationService = new CentralAuthenticationServiceImpl();
        this.centralAuthenticationService
            .setTicketRegistry(new DefaultTicketRegistry());

        AuthenticationManagerImpl manager = new AuthenticationManagerImpl();
        manager
            .setAuthenticationMetaDataPopulators(new AuthenticationMetaDataPopulator[] {new AuthenticationMetaDataPopulator(){

                public Authentication populateAttributes(
                    Authentication authentication, Credentials credentials) {
                    return authentication;
                }
            }});
        manager
            .setAuthenticationHandlers(new AuthenticationHandler[] {new SimpleTestUsernamePasswordAuthenticationHandler()});
        manager
            .setCredentialsToPrincipalResolvers(new CredentialsToPrincipalResolver[] {new UsernamePasswordCredentialsToPrincipalResolver()});

        this.centralAuthenticationService.setAuthenticationManager(manager);
        manager.afterPropertiesSet();
        this.centralAuthenticationService
            .setServiceTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        this.centralAuthenticationService
            .setTicketGrantingTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        this.centralAuthenticationService
            .setTicketGrantingTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        this.logonFormAction
            .setCentralAuthenticationService(this.centralAuthenticationService);
        this.centralAuthenticationService
            .setServiceTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        this.logonFormAction.afterPropertiesSet();
    }

    public void testSubmitBadCredentials() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test2");

        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context,
            "org.springframework.validation.BindException.credentials",
            new BindException(credentials, "credentials"));

        assertEquals("error", this.logonFormAction.submit(context).getId());
    }

    public void testSubmitProperCredentialsWithService() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));

        request.addParameter("service", "test");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");

        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context,
            "org.springframework.validation.BindException.credentials",
            new BindException(credentials, "credentials"));

        assertEquals("warn", this.logonFormAction.submit(context).getId());
    }

    public void testSubmitProperCredentialsWithNoService() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");

        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context,
            "org.springframework.validation.BindException.credentials",
            new BindException(credentials, "credentials"));

        assertEquals("noService", this.logonFormAction.submit(context).getId());
    }

    public void testWarn() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));

        request.addParameter("warn", "on");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");

        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context,
            "org.springframework.validation.BindException.credentials",
            new BindException(credentials, "credentials"));

        assertEquals("noService", this.logonFormAction.submit(context).getId());
        MockHttpServletResponse response = (MockHttpServletResponse) ContextUtils
            .getHttpServletResponse(context);
        assertNotNull(response.getCookie(WebConstants.COOKIE_PRIVACY));
        assertEquals(WebConstants.COOKIE_DEFAULT_FILLED_VALUE, response
            .getCookie(WebConstants.COOKIE_PRIVACY).getValue());
    }

    public void testRenewIsTrue() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));

        request.addParameter("service", "true");
        request.addParameter("renew", "true");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");

        final String ticketGrantingTicket = this.centralAuthenticationService
            .createTicketGrantingTicket(credentials);
        request.setCookies(new Cookie[] {new Cookie(WebConstants.COOKIE_TGC_ID,
            ticketGrantingTicket)});

        ContextUtils.addAttribute(context, "credentials", credentials);
        ContextUtils.addAttribute(context,
            "org.springframework.validation.BindException.credentials",
            new BindException(credentials, "credentials"));

        assertEquals("warn", this.logonFormAction.submit(context).getId());
    }

    public void testRenewIsTrueWithDifferentCredentials() throws Exception {
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));

        request.addParameter("service", "true");
        request.addParameter("renew", "true");
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();
        credentials.setUsername("test");
        credentials.setPassword("test");

        final UsernamePasswordCredentials credentials2 = new UsernamePasswordCredentials();
        credentials2.setUsername("test2");
        credentials2.setPassword("test2");

        final String ticketGrantingTicket = this.centralAuthenticationService
            .createTicketGrantingTicket(credentials);
        request.setCookies(new Cookie[] {new Cookie(WebConstants.COOKIE_TGC_ID,
            ticketGrantingTicket)});

        ContextUtils.addAttribute(context, "credentials", credentials2);
        ContextUtils.addAttribute(context,
            "org.springframework.validation.BindException.credentials",
            new BindException(credentials, "credentials"));

        assertEquals("warn", this.logonFormAction.submit(context).getId());
    }

    public void testAfterPropertiesSetCas() {
        try {
            this.logonFormAction.setCentralAuthenticationService(null);
            this.logonFormAction.afterPropertiesSet();
            fail("Exception expected.");
        } catch (Exception e) {
            return;
        }
    }

    public void testAfterPropertiesSetBadCredentials() {
        try {
            this.logonFormAction.setFormObjectClass(Object.class);
            this.logonFormAction.afterPropertiesSet();
            fail("Exception expected.");
        } catch (Exception e) {
            return;
        }
    }

    public void testAfterPropertiesSetDifferentCredentials() {
        try {
            this.logonFormAction
                .setFormObjectClass(HttpBasedServiceCredentials.class);
            this.logonFormAction.setValidator(new Validator(){

                public boolean supports(Class arg0) {
                    return true;
                }

                public void validate(Object arg0, Errors arg1) {
                    // do nothing
                }
            });
            this.logonFormAction.setCredentialsBinder(new CredentialsBinder(){

                public void bind(HttpServletRequest request,
                    Credentials credentials) {
                    // do nothing
                }

                public boolean supports(Class clazz) {
                    return false;
                }
            });
            this.logonFormAction.afterPropertiesSet();
            fail("Exception expected.");
        } catch (Exception e) {
            return;
        }
    }

    public void testOnBindNoBinding() throws IllegalAccessException,
        InvocationTargetException {
        this.logonFormAction
            .setFormObjectClass(UsernamePasswordCredentials.class);
        this.logonFormAction.setCredentialsBinder(null);
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));
        Method[] methods = this.logonFormAction.getClass().getDeclaredMethods();
        UsernamePasswordCredentials c = new UsernamePasswordCredentials();

        Method method = null;

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("onBind")
                && methods[i].getParameterTypes().length == 3) {
                method = methods[i];
                break;
            }
        }

        method.invoke(this.logonFormAction, new Object[] {context, c,
            new BindException(c, "credentials")});
    }

    public void testBinding() throws IllegalAccessException,
        InvocationTargetException {
        this.logonFormAction
            .setFormObjectClass(UsernamePasswordCredentials.class);
        this.logonFormAction.setCredentialsBinder(new CredentialsBinder(){

            public void bind(HttpServletRequest request, Credentials credentials) {
                UsernamePasswordCredentials c = (UsernamePasswordCredentials) credentials;
                c.setUsername("test");
            }

            public boolean supports(Class clazz) {
                return true;
            }
        });
        MockRequestContext context = new MockRequestContext();
        MockHttpServletRequest request = new MockHttpServletRequest();
        context.setSourceEvent(new ServletEvent(request,
            new MockHttpServletResponse()));
        Method[] methods = this.logonFormAction.getClass().getDeclaredMethods();
        UsernamePasswordCredentials c = new UsernamePasswordCredentials();

        Method method = null;

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("onBind")
                && methods[i].getParameterTypes().length == 3) {
                method = methods[i];
                break;
            }
        }

        method.invoke(this.logonFormAction, new Object[] {context, c,
            new BindException(c, "credentials")});
        assertEquals("test", c.getUsername());
    }
}
