/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.support;

import org.jasig.cas.mock.MockAuthentication;
import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id: TimeoutExpirationPolicyTests.java,v 1.4 2005/02/27 05:49:26
 * sbattaglia Exp $
 */
public class TimeoutExpirationPolicyTests extends TestCase {

    private static final long TIMEOUT = 5000;

    private ExpirationPolicy expirationPolicy;
    
    private Ticket ticket;

    protected void setUp() throws Exception {
        this.expirationPolicy = new TimeoutExpirationPolicy(TIMEOUT);
        
        this.ticket = new TicketGrantingTicketImpl("test",
            new MockAuthentication(),
            this.expirationPolicy);

        super.setUp();
    }

    public void testTicketIsNull() {
        assertTrue(this.expirationPolicy.isExpired(null));
    }

    public void testTicketIsNotExpired() {
        assertFalse(this.ticket.isExpired());
    }

    public void testTicketIsExpired() {
        try {
            Thread.sleep(TIMEOUT + 10); // this failed when it was only +1...not
            // accurate??
            assertTrue(this.ticket.isExpired());
        }
        catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}
