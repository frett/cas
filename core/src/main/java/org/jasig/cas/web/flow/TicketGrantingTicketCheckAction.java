/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.SimpleService;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.WebConstants;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.flow.Event;
import org.springframework.web.flow.RequestContext;
import org.springframework.web.flow.action.AbstractAction;
import org.springframework.web.flow.execution.servlet.HttpServletRequestEvent;

/**
 * Action to check to see if a TicketGrantingTicket exists and if we can
 * grant a ServiceTicket using that TicketGrantingTicket.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public class TicketGrantingTicketCheckAction extends AbstractAction {
    
    private CentralAuthenticationService centralAuthenticationService;

    protected Event doExecuteAction(final RequestContext context) throws Exception {
        final HttpServletRequest request = ((HttpServletRequestEvent)context.getOriginatingEvent()).getRequest();
        final String ticketGrantingTicketId = WebUtils.getCookieValue(request, WebConstants.COOKIE_TGC_ID);
        final String service = request.getParameter(WebConstants.SERVICE);
        final boolean gateway = StringUtils.hasText(request.getParameter(WebConstants.GATEWAY));
        final boolean renew = StringUtils.hasText(request.getParameter(WebConstants.RENEW)) && !"false".equals(request.getParameter(WebConstants.RENEW));
        
        if (!StringUtils.hasText(service) || renew) {
            return error();
        }
        
        try {
            final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, new SimpleService(service));
            context.getRequestScope().setAttribute(WebConstants.SERVICE, service);
            context.getRequestScope().setAttribute(WebConstants.TICKET, serviceTicketId);
            return success();
        } catch (TicketException e) {
            // if we are being used as a gateway just bounce!
            if (gateway) {
                // TODO how to do redirect.
            }
            return error();
        }
    }

    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        
        if (this.centralAuthenticationService == null) {
            throw new IllegalStateException("centralAuthenticationService cannot be null on " + this.getClass().getName());
        }
    }
    

}
