/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.event;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;

/**
 * Abstract implementation of the Event interface that defines
 * the method getPublishedDate so that implementing classes
 * do not need to.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public abstract class AbstractEvent extends ApplicationEvent {
    
    private final Date publishedDate;
    
    public AbstractEvent(Object o) {
        super(o);
        this.publishedDate = new Date();
    }

    /**
     * Method to retrieve the date this event was published.
     * @return the Date this event was published.
     */
    public final Date getPublishedDate() {
        return this.publishedDate;
    }
    
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
