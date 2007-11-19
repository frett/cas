/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.event.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jasig.cas.event.TicketEvent;
import org.jasig.cas.event.TicketEvent.TicketEventType;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.annotation.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public class CentralAuthenticationServiceMethodInterceptor implements
    MethodInterceptor, ApplicationEventPublisherAware {

    /** The TicketRegistry which holds ticket information. */
    @NotNull
    private TicketRegistry ticketRegistry;

    /** The publisher to publish events. */
    private ApplicationEventPublisher applicationEventPublisher;

    public Object invoke(MethodInvocation method) throws Throwable {
        Ticket ticket = null;
        TicketEvent ticketEvent = null;

        if (method.getMethod().getName().equals("validateServiceTicket")) {
            ticket = this.ticketRegistry.getTicket((String) method
                .getArguments()[0]);
        }

        final Object returnValue = method.proceed();

        if (!method.getMethod().getName().equals("validateServiceTicket")
            && !method.getMethod().getName().equals(
                "destroyTicketGrantingTicket")) {
            ticket = this.ticketRegistry.getTicket((String) returnValue);
        }

        final String methodName = method.getMethod().getName();

        if (methodName.equals("createTicketGrantingTicket")) {
            ticketEvent = new TicketEvent(ticket,
                TicketEventType.CREATE_TICKET_GRANTING_TICKET);
        } else if (methodName.equals("delegateTicketGrantingTicket")) {
            ticketEvent = new TicketEvent(ticket,
                TicketEventType.CREATE_TICKET_GRANTING_TICKET);
        } else if (methodName.equals("grantServiceTicket")) {
            ticketEvent = new TicketEvent(ticket,
                TicketEventType.CREATE_SERVICE_TICKET);
        } else if (methodName.equals("destroyTicketGrantingTicket")) {
            ticketEvent = new TicketEvent(
                TicketEventType.DESTROY_TICKET_GRANTING_TICKET, (String) method
                    .getArguments()[0]);
        } else if (methodName.equals("validateServiceTicket")) {
            ticketEvent = new TicketEvent(ticket,
                TicketEventType.VALIDATE_SERVICE_TICKET);
        }

        if (ticketEvent != null) {
            this.applicationEventPublisher.publishEvent(ticketEvent);
        }

        return returnValue;
    }

    public void setApplicationEventPublisher(
        final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * The TicketRegistry to use to look up tickets.
     * 
     * @param ticketRegistry the TicketRegistry
     */
    public void setTicketRegistry(final TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }

}
