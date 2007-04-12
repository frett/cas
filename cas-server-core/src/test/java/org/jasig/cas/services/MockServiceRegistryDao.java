/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services;

import java.util.ArrayList;
import java.util.List;

public class MockServiceRegistryDao implements ServiceRegistryDao {

    public boolean delete(RegisteredService registeredService) {
        return false;
    }

    public RegisteredService findServiceById(long id) {
        return new RegisteredServiceImpl();
    }

    public List<RegisteredService> load() {
        return new ArrayList<RegisteredService>();
    }

    public RegisteredService save(RegisteredService registeredService) {
        return registeredService;
    }
}
