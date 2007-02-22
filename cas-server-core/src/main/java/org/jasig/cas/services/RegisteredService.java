/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services;

import java.util.List;

import org.jasig.cas.authentication.principal.Service;

/**
 * Interface for a service that can be registered by the Services Management
 * interface.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public interface RegisteredService {

    /**
     * Is this application currently allowed to use CAS?
     * 
     * @return true if it can use CAS, false otherwise.
     */
    boolean isEnabled();

    /**
     * Returns the list of allowed attributes. Users should always check
     * {@link RegisteredService#isAllowedToSeeAllAttributes()} to determine
     * global access.
     * 
     * @return the list of attributes
     */
    List<String> getAllowedAttributes();

    /**
     * Is this application allowed to see all current and future attributes?
     * 
     * @return true if it can, false otherwise.
     */
    boolean isAllowedToSeeAllAttributes();

    /**
     * Is this application allowed to take part in the proxying capabilities of
     * CAS?
     * 
     * @return true if it can, false otherwise.
     */
    boolean isAllowedToProxy();

    /**
     * The unique identifier for this service.
     * 
     * @return the unique identifier for this service.
     */
    String getId();

    /**
     * Returns the name of the service.
     * 
     * @return the name of the service.
     */
    String getName();

    /**
     * Returns a short theme name. Services do not need to have unique theme
     * names.
     * 
     * @return the theme name associated with this service.
     */
    String getTheme();

    /**
     * Does this application participate in the SSO session?
     * 
     * @return true if it does, false otherwise.
     */
    boolean isSsoEnabled();

    /**
     * Returns the description of the service.
     * 
     * @return the description of the service.
     */
    String getDescription();

    /**
     * Returns whether the service matches the registered service.
     * 
     * @param service the service to match.
     * @return true if they match, false otherwise.
     */
    boolean matches(final Service service);
}
