/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.Ticket;

/**
 * Expiration policy that is based on a certain time period for a ticket to
 * exist.
 * 
 * @author Scott Battaglia
 * @version $Id: TimeoutExpirationPolicy.java,v 1.4 2005/03/01 05:04:58
 * sbattaglia Exp $
 */
public class TimeoutExpirationPolicy implements ExpirationPolicy {

    private static final long serialVersionUID = 3545511790222979383L;

    final private long timeToKillInMilliSeconds;

    public TimeoutExpirationPolicy(final long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    public boolean isExpired(final Ticket ticket) {
        return (ticket == null)
            || (System.currentTimeMillis() - ticket.getLastTimeUsed() >= this.timeToKillInMilliSeconds);
    }
}