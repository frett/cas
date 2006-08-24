/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.ticket.TicketException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The ProxyController is involved with returning a Proxy Ticket (in CAS 2
 * terms) to the calling application. In CAS 3, a Proxy Ticket is just a Service
 * Ticket granted to a service.
 * <p>
 * The ProxyController requires the following property to be set:
 * </p>
 * <ul>
 * <li> centralAuthenticationService - the service layer</li>
 * <li> casArgumentExtractor - the assistant for extracting parameters</li>
 * </ul>
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class ProxyController extends AbstractController implements
    InitializingBean {

    /** View for if the creation of a "Proxy" Ticket Fails. */
    private static final String CONST_PROXY_FAILURE = "casProxyFailureView";

    /** View for if the creation of a "Proxy" Ticket Succeeds. */
    private static final String CONST_PROXY_SUCCESS = "casProxySuccessView";

    /** Key to use in model for service tickets. */
    private static final String MODEL_SERVICE_TICKET = "ticket";

    /** CORE to delegate all non-web tier functionality to. */
    private CentralAuthenticationService centralAuthenticationService;

    /** Instance of helper for retrieving parameters. */
    private CasArgumentExtractor casArgumentExtractor;

    public ProxyController() {
        setCacheSeconds(0);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.centralAuthenticationService,
            "centralAuthenticationService cannot be null on "
                + this.getClass().getName());
        Assert.notNull(this.casArgumentExtractor,
            "casArgumentExtractor cannot be null.");
    }

    /**
     * @return ModelAndView containing a view name of either
     * <code>casProxyFailureView</code> or <code>casProxySuccessView</code>
     */
    protected ModelAndView handleRequestInternal(
        final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        final String ticket = this.casArgumentExtractor
            .extractProxyGrantingTicket(request);

        if (!StringUtils.hasText(ticket)
            || !this.casArgumentExtractor.isTargetServicePresent(request)) {
            return generateErrorView("INVALID_REQUEST",
                "INVALID_REQUEST_PROXY", null);
        }

        try {
            return new ModelAndView(CONST_PROXY_SUCCESS, MODEL_SERVICE_TICKET,
                this.centralAuthenticationService.grantServiceTicket(ticket,
                    this.casArgumentExtractor.extractTargetService(request)));
        } catch (TicketException e) {
            return generateErrorView(e.getCode(), e.getCode(),
                new Object[] {ticket});
        }
    }

    private ModelAndView generateErrorView(final String code,
        final String description, final Object[] args) {
        final ModelAndView modelAndView = new ModelAndView(CONST_PROXY_FAILURE);
        modelAndView.addObject("code", code);
        modelAndView.addObject("description", getMessageSourceAccessor()
            .getMessage(description, args, description));

        return modelAndView;
    }

    /**
     * @param centralAuthenticationService The centralAuthenticationService to
     * set.
     */
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void setCasArgumentExtractor(
        final CasArgumentExtractor casArgumentExtractor) {
        this.casArgumentExtractor = casArgumentExtractor;
    }
}
