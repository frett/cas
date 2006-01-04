/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.springframework.util.Assert;

/**
 * Domain object representing a Service Ticket. A service ticket grants specific
 * access to a particular service. It will only work for a particular service.
 * Generally, it is a one time use Ticket, but the specific expiration policy
 * can be anything.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class ServiceTicketImpl extends AbstractTicket implements
    ServiceTicket {

    /** Unique ID for serializing. */
    private static final long serialVersionUID = 1296808733190507408L;

    /** The service this ticket is valid for. */
    private final Service service;

    /** Is this service ticket the result of a new login. */
    private boolean fromNewLogin;

    /**
     * Constructs a new ServiceTicket with a Unique Id, a TicketGrantingTicket,
     * a Service, Expiration Policy and a flag to determine if the ticket
     * creation was from a new Login or not.
     * 
     * @param id the unique identifier for the ticket.
     * @param ticket the TicketGrantingTicket parent.
     * @param service the service this ticket is for.
     * @param fromNewLogin is it from a new login.
     * @param policy the expiration policy for the Ticket.
     * @throws IllegalArgumentException if the TicketGrantingTicket or the
     * Service are null.
     */
    protected ServiceTicketImpl(final String id,
        final TicketGrantingTicket ticket, final Service service,
        final boolean fromNewLogin, final ExpirationPolicy policy) {
        super(id, ticket, policy);
        
        Assert.notNull(ticket);
        Assert.notNull(service);

        this.service = service;
        this.fromNewLogin = fromNewLogin;
    }

    public boolean isFromNewLogin() {
        return this.fromNewLogin;
    }

    public void setFromNewLogin(final boolean fromNewLogin) {
        this.fromNewLogin = fromNewLogin;
    }

    public Service getService() {
        return this.service;
    }

    public boolean isExpiredInternal() {
        return this.getGrantingTicket().isExpired();
    }

    public TicketGrantingTicket grantTicketGrantingTicket(final String id,
        final Authentication authentication,
        final ExpirationPolicy expirationPolicy) {
        return new TicketGrantingTicketImpl(id, this.getGrantingTicket(),
            authentication, expirationPolicy);
    }

}
