/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

import java.io.Serializable;

/**
 * Strategy that determines if the ticket is expired. Implementations of the
 * Expiration Policy define their own rules on what they consider an expired
 * Ticket to be.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 * <p>This is a published and supported CAS Server 3 API.</p>
 * @see org.jasig.cas.ticket.Ticket
 */
public interface ExpirationPolicy extends Serializable {

    /**
     * Method to determine if a Ticket has expired or not, based on the policy.
     * 
     * @param ticket The ticket to check.
     * @return true if the ticket is expired, false otherwise.
     */
    boolean isExpired(Ticket ticket);
}
