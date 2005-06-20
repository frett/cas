/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.registry.support;

import org.jasig.cas.ticket.registry.AbstractRegistryCleanerTests;
import org.jasig.cas.ticket.registry.DefaultTicketRegistry;
import org.jasig.cas.ticket.registry.RegistryCleaner;
import org.jasig.cas.ticket.registry.TicketRegistry;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class DefaultTicketRegistryCleanerTests extends
    AbstractRegistryCleanerTests {

    public RegistryCleaner getNewRegistryCleaner(
        final TicketRegistry ticketRegistry) {
        DefaultTicketRegistryCleaner cleaner = new DefaultTicketRegistryCleaner();
        cleaner.setTicketRegistry(ticketRegistry);
        try {
            cleaner.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return cleaner;
    }

    public TicketRegistry getNewTicketRegistry() {
        return new DefaultTicketRegistry();
    }
}