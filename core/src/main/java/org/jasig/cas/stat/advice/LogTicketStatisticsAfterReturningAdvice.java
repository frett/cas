/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.stat.advice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.stat.TicketStats;
import org.springframework.aop.AfterReturningAdvice;


/**
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class LogTicketStatisticsAfterReturningAdvice implements AfterReturningAdvice {
    private Map statsStateMutators = new HashMap();
    
    
    private static final String PROXY_GRANTING_TICKET_METHOD = "delegateTicketGrantingTicket";
    private static final String SERVICE_TICKET_METHOD = "grantServiceTicket";
    private static final String TICKET_GRANTING_TICKET_METHOD = "createTicketGrantingTicket";
    private static final String PROXY_TICKET_METHOD = "";
    
    private TicketStats ticketStats;
    /**
     * @see org.springframework.aop.AfterReturningAdvice#afterReturning(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        //Scott, now using Map to map method name on the target to "stats muteator methods"
        //and reflectively invoke them!
      
        if (returnValue == null || this.statsStateMutators.isEmpty()) {
            return;
        }
        
        String statsStateMutatorMethodName = (String)this.statsStateMutators.get(method.getName());
        if(statsStateMutatorMethodName == null){
            return;
        }
        Method statsStateMutatorMethod = this.ticketStats.getClass().getMethod(statsStateMutatorMethodName, null);
        statsStateMutatorMethod.invoke(this.ticketStats, null);
        
        
        /*if (PROXY_GRANTING_TICKET_METHOD.equals(method.getName())) {
            this.ticketStats.incrementNumberOfProxyGrantingTicketsVended();
        } else if (SERVICE_TICKET_METHOD.equals(method.getName())) {
            this.ticketStats.incrementNumberOfServiceTicketsVended();
        } else if (TICKET_GRANTING_TICKET_METHOD.equals(method.getName())) {
            this.ticketStats.incrementNumberOfTicketGrantingTicketsVended();
        } else if (PROXY_TICKET_METHOD.equals(method.getName())) {
            this.ticketStats.incrementNumberOfProxyTicketsVended();
        }*/
    }
    /**
     * @param ticketStats The ticketStats to set.
     */
    public void setTicketStats(TicketStats ticketStats) {
        this.ticketStats = ticketStats;
    }
    
    /**
     * @param statsStateMutators The statsStateMutators to set.
     */
    public void setStatsStateMutators(Map statsStateMutators) {
        this.statsStateMutators = statsStateMutators;
    }
}
