/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.stat.advice;

import java.util.Properties;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.CentralAuthenticationServiceImpl;
import org.jasig.cas.TestUtils;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.stat.support.TicketStatisticsImpl;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class LogTicketStatisticsAfterReturningAdviceTests extends TestCase {

    public void testNullReturnsOkay() {
        LogTicketStatisticsAfterReturningAdvice advice = new LogTicketStatisticsAfterReturningAdvice();
        try {
            advice.afterReturning(null, null, null, null);
        } catch (Throwable e) {
            fail("Throwable not expected.");
        }
    }

    public void testMethodNotFound() {
        LogTicketStatisticsAfterReturningAdvice advice = new LogTicketStatisticsAfterReturningAdvice();
        Properties properties = new Properties();
        properties.put("test", "test");

        advice.setStatsStateMutators(properties);
        try {

            advice.afterReturning("test", advice.getClass()
                .getDeclaredMethods()[0], null, null);
        } catch (Throwable e) {
            fail("Throwable not expected.");
        }
    }

    public void testMethodFound() {
        TicketStatisticsImpl t = new TicketStatisticsImpl();
        LogTicketStatisticsAfterReturningAdvice advice = new LogTicketStatisticsAfterReturningAdvice();
        advice.setTicketRegistry(new DefaultTicketRegistry());
        advice.setTicketStatsManager(t);
        Properties p = new Properties();
        p.put("createTicketGrantingTicket",
            "incrementNumberOfTicketGrantingTicketsVended");
        advice.setStatsStateMutators(p);

        try {

            advice.afterReturning("tgt", CentralAuthenticationService.class
                .getMethod("createTicketGrantingTicket",
                    new Class<?>[] {Credentials.class}), null,
                new CentralAuthenticationServiceImpl());
            assertEquals(1, t.getNumberOfTicketGrantingTicketsVended());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Throwable not expected.");
        }
    }

    public void testProxyTicket() {
        TicketStatisticsImpl ts = new TicketStatisticsImpl();
        TicketGrantingTicketImpl tgtParent = new TicketGrantingTicketImpl(
            "parentTicket", TestUtils.getAuthentication(),
            new NeverExpiresExpirationPolicy());
        ServiceTicket stChild = tgtParent.grantServiceTicket("childTicket",
            TestUtils.getService(), new NeverExpiresExpirationPolicy(), false);
        TicketGrantingTicket tgtPgt = stChild.grantTicketGrantingTicket(
            "pgtId", TestUtils.getAuthenticationWithService(),
            new NeverExpiresExpirationPolicy());
        ServiceTicket stProxy = tgtPgt.grantServiceTicket("proxyId",
            TestUtils.getService(), new NeverExpiresExpirationPolicy(), false);

        TicketRegistry t = new DefaultTicketRegistry();
        t.addTicket(tgtParent);
        t.addTicket(stChild);
        t.addTicket(tgtPgt);
        t.addTicket(stProxy);

        LogTicketStatisticsAfterReturningAdvice advice = new LogTicketStatisticsAfterReturningAdvice();
        advice.setTicketRegistry(t);
        advice.setTicketStatsManager(ts);
        Properties p = new Properties();
        p.put("grantServiceTicket", "incrementNumberOfServiceTicketsVended");
        advice.setStatsStateMutators(p);

        try {
            advice.afterReturning(stProxy.getId(),
                CentralAuthenticationService.class.getMethod(
                    "grantServiceTicket", new Class<?>[] {String.class,
                        Service.class}), null,
                new CentralAuthenticationServiceImpl());
            assertEquals(1, ts.getNumberOfProxyTicketsVended());
        } catch (Throwable e) {
            fail("Throwable not expected.");
        }

        try {
            advice.afterReturning(stChild.getId(),
                CentralAuthenticationService.class.getMethod(
                    "grantServiceTicket", new Class<?>[] {String.class,
                        Service.class}), null,
                new CentralAuthenticationServiceImpl());
            assertEquals(1, ts.getNumberOfProxyTicketsVended());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Throwable not expected.");
        }
    }
}
