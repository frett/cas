/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

/**
 * Exception that is thrown when a ticket retrieval from the registry does not
 * match the expected class.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class InvalidTicketClassException extends TicketException {

    /** Serializable Unique ID. */
    private static final long serialVersionUID = 6833932903118682012L;

    /**
     * 
     */
    public InvalidTicketClassException() {
        super();
    }

    /**
     * @param message
     */
    public InvalidTicketClassException(final String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidTicketClassException(final String message,
        final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public InvalidTicketClassException(final Throwable cause) {
        super(cause);
    }
}
