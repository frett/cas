/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.SimpleService;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.ViewNames;
import org.jasig.cas.web.support.WebConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller to return a valid proxy ticket upon request.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class ProxyController extends AbstractController implements
    InitializingBean {

    /** Log instance. */
    private final Log log = LogFactory.getLog(getClass());

    /** CORE to delegate all non-web tier functionality to. */
    private CentralAuthenticationService centralAuthenticationService;

    public ProxyController() {
        setCacheSeconds(0);
    }

    public void afterPropertiesSet() throws Exception {
        if (this.centralAuthenticationService == null) {
            throw new IllegalStateException(
                "centralAuthenticationService cannot be null on "
                    + this.getClass().getName());
        }
    }

    protected ModelAndView handleRequestInternal(
        final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        final String ticket = request
            .getParameter(WebConstants.PROXY_GRANTING_TICKET);
        final Service service = new SimpleService(request
            .getParameter(WebConstants.TARGET_SERVICE));

        try {
            final String serviceTicket = this.centralAuthenticationService
                .grantServiceTicket(ticket, service);

            if (serviceTicket == null) {
                final Map model = new HashMap();
                model.put(WebConstants.CODE, "BAD_PGT");
                model.put(WebConstants.DESC, "unrecognized pgt: " + ticket);
                return new ModelAndView(ViewNames.CONST_PROXY_FAILURE, model);
            }

            return new ModelAndView(ViewNames.CONST_PROXY_SUCCESS,
                WebConstants.TICKET, serviceTicket);
        } catch (TicketException e) {
            final Map model = new HashMap();
            model.put(WebConstants.CODE, e.getCode());
            model.put(WebConstants.DESC, e.getDescription());
            return new ModelAndView(ViewNames.CONST_PROXY_FAILURE, model);
        }
    }

    /**
     * @param centralAuthenticationService The centralAuthenticationService to
     * set.
     */
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
}
