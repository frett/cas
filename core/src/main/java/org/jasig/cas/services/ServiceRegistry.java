/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services;

import java.util.Collection;

/**
 * Registry that contains a list of valid services that service tickets must use
 * for validation. If a service ticket does not exist in the registry (other
 * than the registry being empty), a service ticket should not be granted.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public interface ServiceRegistry {

    /**
     * Method to determine if the service exists in the registry. Default
     * behavior of returning true if there are no services registered in the
     * system.
     * 
     * @param serviceId the service to check
     * @return true if the service exists. False otherwise.
     */
    boolean serviceExists(String serviceId);

    /**
     * Retrieve a service from the registry.
     * 
     * @param serviceId the id of the service to retrieve.
     * @return the service if found, null otherwise.
     */
    RegisteredService getService(String serviceId);

    /**
     * Method to return the list of services in the registry.
     * 
     * @return A list of services
     */
    Collection getServices();
}
