/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.registry.support;

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.ticket.InvalidTicketException;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.registry.TicketRegistry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

/**
 * Ticket registry backed by EHCache caching subsystem.
 * <p>
 * Note: assumes that <code>Cache</code> instances will be injected via setter method, typically by some kind of IoC
 * container.
 * 
 * @author Dmitriy Kopylenko
 * @version $Id$
 */
public class EhCacheTicketRegistry implements TicketRegistry {
	protected final Log logger = LogFactory.getLog(getClass());
    private Cache cache;

    /**
     * Set backed cache.
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#addTicket(org.jasig.cas.domain.Ticket)
     */
    public void addTicket(final Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Cannot add null Ticket to the registry.");
        }
        
        logger.debug("Added ticket [" + ticket.getId() + "] to registry.");
        this.cache.put(new Element(ticket.getId(), ticket));
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#getTicket(java.lang.String, java.lang.Class)
     */
    public Ticket getTicket(final String ticketId, final Class clazz) throws InvalidTicketException {
    	logger.debug("Attempting to retrieve ticket [" + ticketId + "]");
   
        if (ticketId == null) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz argument must not be null.");
        }
        try {
            Element element = this.cache.get(ticketId);
            if (element == null) {
                return null;
            }
            else {
                Ticket ticket = (Ticket)element.getValue();
                if (!ticket.getClass().isAssignableFrom(clazz))
                    throw new InvalidTicketException("Ticket [" + ticket.getId() + "] for user [" + ticket.getPrincipal() + "] is of type "
                        + ticket.getClass() + " when we were expecting " + clazz);

                logger.debug("Ticket [" + ticketId + "] found in registry.");
                return ticket;
            }
        }
        catch (CacheException ex) {
            throw new TicketException("Ticket registry threw an exception", ex);
        }
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#deleteTicket(java.lang.String)
     */
    public boolean deleteTicket(final String ticketId) {
    	logger.debug("Removing ticket [" + ticketId + "] from registry");
        return this.cache.remove(ticketId);
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#getTickets()
     */
    public Collection getTickets() {
        // TODO Implement
        return null;
    }
}