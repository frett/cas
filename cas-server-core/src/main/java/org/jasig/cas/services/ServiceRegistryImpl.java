/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jasig.cas.authentication.principal.Service;
import org.springframework.beans.factory.InitializingBean;

/**
 * Implementation of the ServiceRegistry and ServiceRegistryManager interfaces.
 * TODO: javadoc
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
// JdbcDaoSupport
public final class ServiceRegistryImpl implements
    ServiceRegistry, ServiceRegistryManager, InitializingBean {

    private List<RegisteredService> services = new CopyOnWriteArrayList<RegisteredService>();
    
    private static final RegisteredService EMPTY_REGISTERED_SERVICE = new RegisteredServiceImpl();

    private boolean enabled = true;

    /**
     * Will only return a RegisteredService if the RegisteredService is enabled.
     * @see org.jasig.cas.services.ServiceRegistry#findServiceBy(org.jasig.cas.authentication.principal.Service)
     */
    public RegisteredService findServiceBy(final Service service) {
        if (!this.enabled) {
            return EMPTY_REGISTERED_SERVICE;
        }
        
        for (final RegisteredService registeredService : this.services) {
            if (registeredService.matches(service) && registeredService.isEnabled()) {
                return registeredService;
            }
        }

        return null;
    }

    public List<RegisteredService> getAllServices() {
        return Collections.unmodifiableList(this.services);
    }

    public boolean matchesExistingService(final Service service) {
        return !this.enabled || findServiceBy(service) != null;
    }

    public void afterPropertiesSet() throws Exception {
        if (!this.enabled) {
            return;
        }
        // TODO load from database
    }

    public synchronized void addService(final RegisteredService service) {
        if (this.services.contains(service)) {
            this.services.remove(service);
        }

        this.services.add(service);

        // TODO database persistance
    }

    public boolean deleteService(final RegisteredService service) {
        return this.services.remove(service);

        // TODO database persistance

    }

    public synchronized void updateService(final RegisteredService service) {
        addService(service);

        // TODO database persistance
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setBootstrapService(final String serviceId) {
        
        // TODO
    }
}
