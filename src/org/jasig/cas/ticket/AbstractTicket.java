/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Abstract implementation of a ticket that handles all ticket state for policies. Also incorporates properties common among all tickets.
 * 
 * @author Scott Battaglia
 * @version $Id$
 */
public abstract class AbstractTicket implements InternalTicket {

	final private ExpirationPolicy expirationPolicy;

    private long lastTimeUsed;

    private int countOfUses;

    final private String id;

    public AbstractTicket(final String id, final ExpirationPolicy expirationPolicy) {
        if (expirationPolicy == null || id == null)
            throw new IllegalArgumentException("id and expirationPolicy are required parameters.");

        this.id = id;
        this.lastTimeUsed = System.currentTimeMillis();

        this.expirationPolicy = expirationPolicy;
    }

    /**
     * @see org.jasig.cas.ticket.Ticket#getId()
     */
    public String getId() {
        return this.id;
    }

    public int getCountOfUses() {
        return this.countOfUses;
    }

    public long getLastTimeUsed() {
        return this.lastTimeUsed;
    }

    public void incrementCount() {
        this.countOfUses++;
    }

    public void updateLastUse() {
        this.lastTimeUsed = System.currentTimeMillis();
    }

    /**
     * @see org.jasig.cas.ticket.Ticket#isExpired()
     */
    public boolean isExpired() {
        return this.expirationPolicy.isExpired(this);
    }

    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}