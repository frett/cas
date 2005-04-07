/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.registry;

import org.jasig.cas.mock.MockAuthentication;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id: AbstractRegistryCleanerTests.java,v 1.2 2005/02/27 05:49:26
 * sbattaglia Exp $
 */
public abstract class AbstractRegistryCleanerTests extends TestCase {

    private RegistryCleaner registryCleaner;

    private TicketRegistry ticketRegistry;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.ticketRegistry = this.getNewTicketRegistry();
        this.registryCleaner = this.getNewRegistryCleaner(this.ticketRegistry);
    }

    public abstract RegistryCleaner getNewRegistryCleaner(
        TicketRegistry newTicketRegistry);

    public abstract TicketRegistry getNewTicketRegistry();

    public void testCleanEmptyTicketRegistry() {
        this.registryCleaner.clean();
        assertTrue(this.ticketRegistry.getTickets().isEmpty());
    }

    public void testCleanRegistryOfExpiredTicketsAllExpired() {
        for (int i = 0; i < 10; i++) {
            TicketGrantingTicket ticket = new TicketGrantingTicketImpl("test"
                + i, new MockAuthentication(),
                new NeverExpiresExpirationPolicy());
            ticket.expire();
            this.ticketRegistry.addTicket(ticket);
        }

        this.registryCleaner.clean();

        assertTrue(this.ticketRegistry.getTickets().isEmpty());
    }

    public void testCleanRegistryOneNonExpired() {
        for (int i = 0; i < 10; i++) {
            TicketGrantingTicket ticket = new TicketGrantingTicketImpl("test"
                + i, new MockAuthentication(),
                new NeverExpiresExpirationPolicy());
            ticket.expire();
            this.ticketRegistry.addTicket(ticket);
        }

        TicketGrantingTicket ticket = new TicketGrantingTicketImpl(
            "testNoExpire", new MockAuthentication(),
            new NeverExpiresExpirationPolicy());
        this.ticketRegistry.addTicket(ticket);

        this.registryCleaner.clean();

        assertEquals(this.ticketRegistry.getTickets().size(), 1);
    }
}