/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services.web;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServiceRegistryManager;
import org.springframework.util.StringUtils;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class EditRegisteredServiceSimpleFormController extends
    AbstractRegisteredServiceSimpleFormController {

    /**
     * Public constructor that takes a {@link ServiceRegistryManager}
     * 
     * @param serviceRegistryManager the ServiceRegistryManager
     */
    public EditRegisteredServiceSimpleFormController(
        final ServiceRegistryManager serviceRegistryManager) {
        super(serviceRegistryManager);
    }

    protected void onSubmitInternal(final RegisteredService registeredService) {
        getServiceRegistryManager().updateService(registeredService);
    }

    protected Object formBackingObject(final HttpServletRequest request)
        throws Exception {
        final String id = request.getParameter("id");

        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("id could not be found.");
        }

        final long serviceId = Long.parseLong(id);

        final RegisteredService service = getServiceRegistryManager()
            .findServiceBy(serviceId);

        if (service == null) {
            throw new IllegalArgumentException(
                "Service could not be found for provided id.");
        }

        return service;
    }
}
