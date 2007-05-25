/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.springframework.util.Assert;

/**
 * Concrete implementation of a TicketGrantingTicket. A TicketGrantingTicket is
 * the global identifier of a principal into the system. It grants the Principal
 * single-sign on access to any service that opts into single-sign on.
 * Expiration of a TicketGrantingTicket is controlled by the ExpirationPolicy
 * specified as object creation.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.3 $ $Date: 2007/02/20 14:41:04 $
 * @since 3.0
 */
public final class TicketGrantingTicketImpl extends AbstractTicket implements
    TicketGrantingTicket {

    /** The Unique ID for serializing. */
    private static final long serialVersionUID = -8673232562725683059L;

    /** The authenticated object for which this ticket was generated for. */
    private final Authentication authentication;

    /** Flag to enforce manual expiration. */
    private boolean expired = false;
    
    private final Map<String,Service> services = new HashMap<String, Service>();

    /**
     * Constructs a new TicketGrantingTicket.
     * 
     * @param id the id of the Ticket
     * @param ticketGrantingTicket the parent ticket
     * @param authentication the Authentication request for this ticket
     * @param policy the expiration policy for this ticket.
     * @throws IllegalArgumentException if the Authentication object is null
     */
    public TicketGrantingTicketImpl(final String id,
        final TicketGrantingTicket ticketGrantingTicket,
        final Authentication authentication, final ExpirationPolicy policy) {
        super(id, ticketGrantingTicket, policy);

        Assert.notNull(authentication, "authentication cannot be null");

        this.authentication = authentication;
    }

    /**
     * Constructs a new TicketGrantingTicket without a parent
     * TicketGrantingTicket.
     * 
     * @param id the id of the Ticket
     * @param authentication the Authentication request for this ticket
     * @param policy the expiration policy for this ticket.
     */
    public TicketGrantingTicketImpl(final String id,
        final Authentication authentication, final ExpirationPolicy policy) {
        this(id, null, authentication, policy);
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public synchronized ServiceTicket grantServiceTicket(final String id,
        final Service service, final ExpirationPolicy expirationPolicy,
        final boolean credentialsProvided) {
        final ServiceTicket serviceTicket = new ServiceTicketImpl(id, this,
            service, this.getCountOfUses() == 0 || credentialsProvided,
            expirationPolicy);

        updateState();
        
        // XXX is this okay, or will this fail with proxy?
        service.setPrincipal(getAuthentication().getPrincipal());
        
        this.services.put(id, service);

        return serviceTicket;
    }
    
    protected synchronized void logOutOfServices() {
        for (final Entry<String, Service> entry : this.services.entrySet()) {
            final Service service = entry.getValue();
            
            if (service instanceof WebApplicationService) {
                ((WebApplicationService) service).logOutOfService(entry.getKey());
            }
        }
    }

    public boolean isRoot() {
        return this.getGrantingTicket() == null;
    }

    public synchronized void expire() {
        this.expired = true;
        
        logOutOfServices();
    }

    public boolean isExpiredInternal() {
        return this.expired
            || (this.getGrantingTicket() != null && this.getGrantingTicket()
                .isExpired());
    }

    public List<Authentication> getChainedAuthentications() {
        final List<Authentication> list = new ArrayList<Authentication>();

        if (this.getGrantingTicket() == null) {
            list.add(this.getAuthentication());
            return Collections.unmodifiableList(list);
        }

        list.add(this.getAuthentication());
        list.addAll(this.getGrantingTicket().getChainedAuthentications());

        return Collections.unmodifiableList(list);
    }
}
