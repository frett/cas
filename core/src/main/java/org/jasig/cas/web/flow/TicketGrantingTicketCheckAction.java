/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.SimpleService;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.flow.util.ContextUtils;
import org.jasig.cas.web.support.WebConstants;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.flow.RequestContext;

/**
 * Action to check to see if a TicketGrantingTicket exists and if we can grant a
 * ServiceTicket using that TicketGrantingTicket.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class TicketGrantingTicketCheckAction extends AbstractCasAction {

    /** The CORE of CAS which we will use to obtain tickets. */
    private CentralAuthenticationService centralAuthenticationService;

    protected ModelAndEvent doExecuteInternal(final RequestContext context, final Map attributes)
        throws Exception {
        final HttpServletRequest request = ContextUtils
            .getHttpServletRequest(context);
        final String ticketGrantingTicketId = WebUtils.getCookieValue(request,
            WebConstants.COOKIE_TGC_ID);
        final String service = request.getParameter(WebConstants.SERVICE);
        final boolean gateway = StringUtils.hasText(request
            .getParameter(WebConstants.GATEWAY));
        final boolean renew = StringUtils.hasText(request
            .getParameter(WebConstants.RENEW))
            && !"false".equals(request.getParameter(WebConstants.RENEW));

        if (!StringUtils.hasText(service) || renew
            || ticketGrantingTicketId == null) {
            return new ModelAndEvent(error());
        }

        try {
            final String serviceTicketId = this.centralAuthenticationService
                .grantServiceTicket(ticketGrantingTicketId, new SimpleService(
                    service));
            Map model = new HashMap();
            model.put(WebConstants.SERVICE, service);
            model.put(WebConstants.TICKET, serviceTicketId);
            return new ModelAndEvent(success(), model);
        } catch (TicketException e) {
            // if we are being used as a gateway just bounce!
            if (gateway) {
                return new ModelAndEvent(result("gateway"), WebConstants.SERVICE, service);
            }
            return new ModelAndEvent(error());
        }
    }

    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        Assert.notNull(this.centralAuthenticationService,
            "centralAuthenticationService cannot be null on "
                + this.getClass().getName());
    }
}
