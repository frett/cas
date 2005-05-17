/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services.advice;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServiceRegistry;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Advice to check if a Service is allowed to proxy.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0 //TODO this should be using proxyUrl???
 */
public final class ServiceAllowedToProxyMethodBeforeAdvice implements
    MethodBeforeAdvice, InitializingBean {

    /** Log instance. */
    private Log log = LogFactory.getLog(this.getClass());

    /** The TicketRegistry that stores the tickets. */
    private TicketRegistry ticketRegistry;

    /** The registry that stores services. */
    private ServiceRegistry serviceRegistry;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.ticketRegistry,
            "ticketRegistry is a required property.");
        Assert.notNull(this.serviceRegistry,
            "serviceRegistry is a required property.");

    }

    public void before(final Method method, final Object[] args,
        final Object target) throws UnauthorizedServiceException {
        String serviceTicketId = (String) args[0];
        ServiceTicket serviceTicket = (ServiceTicket) this.ticketRegistry
            .getTicket(serviceTicketId);
        RegisteredService authenticatedService = null;

        for (Iterator iter = this.serviceRegistry.getServices().iterator(); iter
            .hasNext();) {
            final RegisteredService service = (RegisteredService) iter.next();
            if ((authenticatedService.getProxyUrl().toExternalForm()
                .equals(serviceTicket.getService().getId()))
                || (authenticatedService.getId().equals(serviceTicket
                    .getService().getId()))) {
                authenticatedService = service;
                break;
            }

        }

        if ((authenticatedService == null)
            || (!authenticatedService.isAllowedToProxy())) {
            throw new UnauthorizedServiceException(
                "Service is not allowed to proxy.");
        }
    }

    /**
     * @param serviceRegistry The serviceRegistry to set.
     */
    public void setServiceRegistry(final ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * @param ticketRegistry The ticketRegistry to set.
     */
    public void setTicketRegistry(final TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }
}
