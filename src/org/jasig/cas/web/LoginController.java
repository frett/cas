/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.BasicCredentialsValidator;
import org.jasig.cas.authentication.SimpleService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.util.DefaultUniqueTokenIdGenerator;
import org.jasig.cas.util.UniqueTokenIdGenerator;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.bind.support.DefaultSpringBindCredentialsBinder;
import org.jasig.cas.web.support.ViewNames;
import org.jasig.cas.web.support.WebConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * @author Scott Battaglia
 * @version $Id$
 */
public class LoginController extends SimpleFormController implements InitializingBean {

    /** LOGGING * */
    protected final Log log = LogFactory.getLog(this.getClass());

    /** INSTANCE VARIABLES * */
    private CentralAuthenticationService centralAuthenticationService;

    private UniqueTokenIdGenerator uniqueTokenIdGenerator = null;

    private Map loginTokens;

    private CredentialsBinder credentialsBinder;

    public LoginController() {
        this.setCacheSeconds(0);
        this.setValidator(new BasicCredentialsValidator());
        this.setFormView(ViewNames.CONST_LOGON);
        this.setSuccessView(ViewNames.CONST_LOGON_SUCCESS);
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        if (this.loginTokens == null || this.centralAuthenticationService == null) {
            throw new IllegalStateException("You must set loginTokens and centralAuthenticationService on " + this.getClass());
        }

        if (this.uniqueTokenIdGenerator == null) {
            this.uniqueTokenIdGenerator = new DefaultUniqueTokenIdGenerator();
            log.info("UniqueIdGenerator not set, using default UniqueIdGenerator of class: " + this.uniqueTokenIdGenerator.getClass());
        }

        if (this.getCommandClass() == null) {
            this.setCommandName("credentials");
            this.setCommandClass(UsernamePasswordCredentials.class);
            log.info("CommandClass not set, using default CommandClass of " + this.getCommandClass().getName() + " and name of "
                + this.getCommandName());
        }

        if (this.credentialsBinder == null) {
            this.credentialsBinder = new DefaultSpringBindCredentialsBinder();
            log.info("CredentialsBinder not set.  Using default CredentialsBinder of " + this.credentialsBinder.getClass().getName());

            if (!this.credentialsBinder.supports(this.getCommandClass())) {
                throw new ServletException("CredentialsBinder does not support supplied Command Class: " + this.getCommandClass());
            }
        }
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(final HttpServletRequest request) throws Exception {
        final Map referenceData = new HashMap();

        referenceData.put(WebConstants.LOGIN_TOKEN, this.getLoginToken()); // a unique token to solve browser back issues

        return referenceData;
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#showForm(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
     */
    protected ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response, final BindException errors)
        throws Exception {
        final String ticketGrantingTicketId = this.getCookieValue(request, WebConstants.COOKIE_TGC_ID);
        final boolean warn = this.convertValueToBoolean(this.getCookieValue(request, WebConstants.COOKIE_PRIVACY));
        final boolean gateway = StringUtils.hasText(request.getParameter(WebConstants.GATEWAY));
        final String service = request.getParameter(WebConstants.SERVICE);
        final boolean renew = StringUtils.hasText(request.getParameter(WebConstants.RENEW));

        System.out.println("COOKIE VALUE: " + this.getCookieValue(request,WebConstants.COOKIE_PRIVACY));
        System.out.println("COOKIE VALUE: " + warn);
        
        
        // if we managed to find an existing ticketGrantingTicketId
        if (StringUtils.hasText(ticketGrantingTicketId) && StringUtils.hasText(service) && !renew) {
            // we have a service and no request for renew
            final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, new SimpleService(service));

            if (serviceTicketId != null) {

                if (warn) {
                    final Map model = new HashMap();

                    model.put(WebConstants.TICKET, serviceTicketId);
                    model.put(WebConstants.SERVICE, service);
                    return new ModelAndView(ViewNames.CONST_LOGON_CONFIRM, model);
                }

                return new ModelAndView(new RedirectView(service), WebConstants.TICKET, serviceTicketId); //assume first = false?
            }
        }

        // if we are being used as a gateway just bounce!
        if (gateway && StringUtils.hasText(service))
            return new ModelAndView(new RedirectView(service));

        // otherwise display the logon form
        return super.showForm(request, response, errors);
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView processFormSubmission(final HttpServletRequest request, final HttpServletResponse response, final Object object,
        final BindException errors) throws Exception {
        final Credentials credentials = (Credentials)object;

        this.credentialsBinder.bind(request, credentials);

        final String ticketGrantingTicketId = this.centralAuthenticationService.createTicketGrantingTicket(credentials);
        final String service = request.getParameter(WebConstants.SERVICE);
        final boolean warn = StringUtils.hasText(request.getParameter(WebConstants.WARN));

        // the ticket was not created because invalid Credentials
        if (ticketGrantingTicketId == null) {
            errors.reject("bad.credentials", null);
            return super.processFormSubmission(request, response, object, errors);
        }

        final String oldTicketGrantingTicketId = getCookieValue(request, WebConstants.COOKIE_TGC_ID);

        if (oldTicketGrantingTicketId != null)
            this.centralAuthenticationService.destroyTicketGrantingTicket(oldTicketGrantingTicketId);

        this.createCookie(WebConstants.COOKIE_TGC_ID, ticketGrantingTicketId, request, response);

        if (warn)
            this.createCookie(WebConstants.COOKIE_PRIVACY, WebConstants.COOKIE_DEFAULT_FILLED_VALUE, request, response);
        else
            this.createCookie(WebConstants.COOKIE_PRIVACY, WebConstants.COOKIE_DEFAULT_EMPTY_VALUE, request, response);

        if (StringUtils.hasText(service)) {
            final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, new SimpleService(service));

            if (warn) {
                final Map model = new HashMap();

                model.put(WebConstants.TICKET, serviceTicketId);
                model.put(WebConstants.SERVICE, service);
                //                model.put(WebConstants.FIRST, "true");
                return new ModelAndView(ViewNames.CONST_LOGON_CONFIRM, model);
            }

            final Map model = new HashMap();
            model.put(WebConstants.TICKET, serviceTicketId);
            //            model.put(WebConstants.FIRST, "true");

            return new ModelAndView(new RedirectView(service), model);
        }

        return super.processFormSubmission(request, response, object, errors);
    }

    private String getLoginToken() {
        final String loginToken = this.uniqueTokenIdGenerator.getNewTokenId();
        this.loginTokens.put(loginToken, new Date());

        return loginToken;
    }

    private String getCookieValue(final HttpServletRequest request, final String cookieId) {
        Cookie cookie = WebUtils.getCookie(request, cookieId);

        return (cookie == null) ? null : cookie.getValue();
    }

    private void createCookie(final String id, final String value, final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie cookie = new Cookie(id, value);
        cookie.setSecure(true);
        cookie.setMaxAge(-1);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }

    private boolean convertValueToBoolean(final String value) {
        return Boolean.getBoolean(value);
    }

    /**
     * @param centralAuthenticationService The centralAuthenticationService to set.
     */
    public void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    /**
     * @param loginTokens The loginTokens to set.
     */
    public void setLoginTokens(final Map loginTokens) {
        this.loginTokens = loginTokens;
    }

    /**
     * @param uniqueTokenIdGenerator The uniqueTokenIdGenerator to set.
     */
    public void setUniqueTokenIdGenerator(final UniqueTokenIdGenerator uniqueTokenIdGenerator) {
        this.uniqueTokenIdGenerator = uniqueTokenIdGenerator;
    }

    /**
     * @param credentialsBinder The credentialsBinder to set.
     */
    public void setCredentialsBinder(CredentialsBinder credentialsBinder) {
        this.credentialsBinder = credentialsBinder;
    }
}