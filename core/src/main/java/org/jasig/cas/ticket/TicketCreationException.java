/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

/**
 * Exception thrown if there is an error creating a ticket.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class TicketCreationException extends TicketException {

    /** Serializable ID for unique id. */
    private static final long serialVersionUID = 5501212207531289993L;

    /** Code description. */
    private static final String CODE = "CREATION_ERROR";

    public TicketCreationException() {
        super(CODE);
    }

    public TicketCreationException(final Throwable throwable) {
        super(CODE, throwable);
    }
}
