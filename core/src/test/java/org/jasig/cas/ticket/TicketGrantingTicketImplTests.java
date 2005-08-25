/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jasig.cas.TestUtils;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.SimpleService;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.util.UniqueTicketIdGenerator;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class TicketGrantingTicketImplTests extends TestCase {

    private UniqueTicketIdGenerator uniqueTicketIdGenerator = new DefaultUniqueTicketIdGenerator();

    public void testNullAuthentication() {
        try {
            new TicketGrantingTicketImpl("test", null, null,
                new NeverExpiresExpirationPolicy());
            fail("Exception expected.");
        } catch (Exception e) {
            // this is okay
        }
    }

    public void testGetAuthentication() {
        Authentication authentication = TestUtils.getAuthentication();

        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            authentication, new NeverExpiresExpirationPolicy());

        assertEquals(t.getAuthentication(), authentication);
    }

    public void testIsRootTrue() {
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());

        assertTrue(t.isRoot());
    }

    public void testIsRootFalse() {
        TicketGrantingTicket t1 = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", t1,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());

        assertFalse(t.isRoot());
    }

    public void testGetChainedPrincipalsWithOne() {
        Authentication authentication = TestUtils.getAuthentication();
        List principals = new ArrayList();
        principals.add(authentication);

        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            authentication, new NeverExpiresExpirationPolicy());

        assertEquals(principals, t.getChainedAuthentications());
    }

    public void testGetChainedPrincipalsWithTwo() {
        Authentication authentication = TestUtils.getAuthentication();
        Authentication authentication1 = TestUtils.getAuthentication("test1");
        List principals = new ArrayList();
        principals.add(authentication);
        principals.add(authentication1);

        TicketGrantingTicket t1 = new TicketGrantingTicketImpl("test", null,
            authentication1, new NeverExpiresExpirationPolicy());
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", t1,
            authentication, new NeverExpiresExpirationPolicy());

        assertEquals(principals, t.getChainedAuthentications());
    }

    public void testServiceTicketAsFromInitialCredentials() {
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());
        ServiceTicket s = t.grantServiceTicket(this.uniqueTicketIdGenerator
            .getNewTicketId(ServiceTicket.PREFIX), new SimpleService("test"),
            new NeverExpiresExpirationPolicy());

        assertTrue(s.isFromNewLogin());
        assertEquals(t.getCountOfUses(), 1);
    }

    public void testServiceTicketAsFromNotInitialCredentials() {
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());
        ServiceTicket s = t.grantServiceTicket(this.uniqueTicketIdGenerator
            .getNewTicketId(ServiceTicket.PREFIX), new SimpleService("test"),
            new NeverExpiresExpirationPolicy());
        s = t.grantServiceTicket(this.uniqueTicketIdGenerator
            .getNewTicketId(ServiceTicket.PREFIX), new SimpleService("test"),
            new NeverExpiresExpirationPolicy());

        assertFalse(s.isFromNewLogin());
        assertEquals(t.getCountOfUses(), 2);
    }

    public void testHashCode() {
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());

        assertEquals(HashCodeBuilder.reflectionHashCode(t), t.hashCode());
    }

    public void testToString() {
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());

        assertEquals(ToStringBuilder.reflectionToString(t), t.toString());
    }

    public void testIncrementTimeUpdated() {
        TicketGrantingTicket t = new TicketGrantingTicketImpl("test", null,
            TestUtils.getAuthentication(), new NeverExpiresExpirationPolicy());

        t.updateLastTimeUsed();
        assertEquals(t.getLastTimeUsed(), System.currentTimeMillis());
    }

    public void testNoIdOrPolicy() {
        try {
            new TicketGrantingTicketImpl(null, null, TestUtils.getAuthentication(), null);

            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}
