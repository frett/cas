/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.registry.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.ticket.InvalidTicketClassException;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;

/**
 * Ticket registry backed by EHCache caching subsystem.
 * <p>
 * Note: assumes that <code>Cache</code> instances will be injected via setter method, typically by some kind of IoC container.
 * 
 * @author Dmitriy Kopylenko
 * @version $Id$
 */
public class EhCacheTicketRegistry implements TicketRegistry {

    protected final Log log = LogFactory.getLog(getClass());

    private Cache cache;

    /**
     * Set backed cache.
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#addTicket(org.jasig.cas.ticket.Ticket)
     */
    public void addTicket(final Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Cannot add null Ticket to the registry.");
        }

        log.debug("Added ticket [" + ticket.getId() + "] to registry.");
        this.cache.put(new Element(ticket.getId(), ticket));
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#getTicket(java.lang.String, java.lang.Class)
     */
    public Ticket getTicket(final String ticketId, final Class clazz) throws InvalidTicketClassException {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz argument must not be null.");
        }
    	
    	final Ticket ticket = this.getTicket(ticketId);
    	
    	if (ticket == null)
    		return null;

        if (!clazz.isAssignableFrom(ticket.getClass()))
            throw new InvalidTicketClassException("Ticket [" + ticket.getId() + "] is of type "
                + ticket.getClass() + " when we were expecting " + clazz);

        return ticket;
    }

	/**
	 * @see org.jasig.cas.ticket.registry.TicketRegistry#getTicket(java.lang.String)
	 */
	public Ticket getTicket(String ticketId){
        log.debug("Attempting to retrieve ticket [" + ticketId + "]");
		if (ticketId == null) {
            return null;
        }

        try {
            Element element = this.cache.get(ticketId);
            if (element == null) {
                return null;
            }

            Ticket ticket = (Ticket)element.getValue();
            log.debug("Ticket [" + ticketId + "] found in registry.");
            return ticket;
        }
        catch (CacheException ex) {
            throw new IllegalStateException("Ticket registry threw an exception: " + ex.getMessage());
        }
	}
    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#deleteTicket(java.lang.String)
     */
    public boolean deleteTicket(final String ticketId) {
        log.debug("Removing ticket [" + ticketId + "] from registry");
        return this.cache.remove(ticketId);
    }

    /**
     * @see org.jasig.cas.ticket.registry.TicketRegistry#getTickets()
     */
    public Collection getTickets() {
        try {
            List keys = this.cache.getKeys();
            Collection items = new ArrayList();

            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                Serializable key = (Serializable) iter.next();
                Element element = this.cache.get(key);
                items.add(element.getValue());
            }
            return Collections.unmodifiableCollection(items);
        } catch (CacheException e) {
            throw new RuntimeException(e);
        }
    }
}