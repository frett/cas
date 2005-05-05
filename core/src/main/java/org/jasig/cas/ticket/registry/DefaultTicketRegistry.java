/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.ticket.Ticket;

/**
 * Generic registry that holds all tickets of any kind in a hash map.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class DefaultTicketRegistry implements TicketRegistry {

    /** The Commons Logging instance. */
    private final Log log = LogFactory.getLog(getClass());

    /**
     * A HashMap to contain the tickets. 
     * 
     * <p>Because the HashMap is not itself thread-safe, it can only
     * be access from methods of this class that are declared to be
     * synchronized. We could synchronize only the actually Map accesses,
     * but there is not much more to this code.</p>
     */
    private final Map cache = new HashMap();

    /**
     * 
     * @see org.jasig.cas.ticket.registry.TicketRegistry#addTicket(org.jasig.cas.ticket.Ticket)
     * @throws IllegalArgumentException if the Ticket is null.
     */
    public synchronized void addTicket(final Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket cannot be null");
        }

        log.debug("Added ticket [" + ticket.getId() + "] to registry.");
        this.cache.put(ticket.getId(), ticket);
    }

    /**
     * 
     * @see org.jasig.cas.ticket.registry.TicketRegistry#getTicket(java.lang.String, java.lang.Class)
     * @throws IllegalArgumentException if class is null.
     * @throws ClassCastException if class does not match requested ticket class.
     */
    public Ticket getTicket(final String ticketId, final Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }

        final Ticket ticket = this.getTicket(ticketId);

        if (ticket == null) {
            return null;
        }

        if (!clazz.isAssignableFrom(ticket.getClass())) {
            throw new ClassCastException("Ticket [" + ticket.getId()
                + " is of type " + ticket.getClass()
                + " when we were expecting " + clazz);
        }

        return ticket;
    }

    public synchronized Ticket getTicket(final String ticketId) {
        log.debug("Attempting to retrieve ticket [" + ticketId + "]");
        final Ticket ticket = (Ticket) this.cache.get(ticketId);

        if (ticket != null) {
            log.debug("Ticket [" + ticketId + "] found in registry.");
        }

        return ticket;
    }

    public synchronized boolean deleteTicket(final String ticketId) {
        log.debug("Removing ticket [" + ticketId + "] from registry");
        return (this.cache.remove(ticketId) != null);
    }

    public synchronized Collection getTickets() {
        return Collections.unmodifiableCollection(this.cache.values());
    }
}
