/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;

/**
 * Domain object representing a Service Ticket. A service ticket grants specific
 * access to a particular service. It will only work for a particular service.
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

    protected ServiceTicketImpl(final String id,
        final TicketGrantingTicket ticket, final Service service,
        final boolean fromNewLogin, final ExpirationPolicy policy) {
        super(id, ticket, policy);

        if (ticket == null || service == null) {
            throw new IllegalArgumentException(
                "ticket and service are required parameters");
        }

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
