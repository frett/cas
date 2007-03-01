/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServiceRegistryManager;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;


public class AddRegisteredServiceSimpleFormController extends
    SimpleFormController {
    
    private final ServiceRegistryManager serviceRegistryManager;
    
    public AddRegisteredServiceSimpleFormController(final ServiceRegistryManager serviceRegistryManager) {
        Assert.notNull(serviceRegistryManager);
        this.serviceRegistryManager = serviceRegistryManager;
    }
    
    protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response, final Object command, final BindException errors) throws Exception {
        final RegisteredService service = (RegisteredService) command;
        this.serviceRegistryManager.addService(service);
        
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("successMessage", "The service has been successfully saved in the database.");
        
        return showForm(request, response, errors, model);
    }

    protected void initBinder(final HttpServletRequest request,
        final ServletRequestDataBinder binder) throws Exception {
        binder.setRequiredFields(new String[] {"description", "id", "name", "allowedToProxy", "enabled", "ssoEnabled"});
    }

    protected Map referenceData(final HttpServletRequest request) throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();
        
        final List<String> attributes = new ArrayList<String>();
        
        // TODO HACK
        attributes.add("test");
        attributes.add("test1");
        attributes.add("test2");
        attributes.add("test3");
        
        model.put("availableAttributes", attributes);
        return model;
    }
}
