/*
 * Copyright 2005 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.services.AuthenticatedService;
import org.jasig.cas.services.ServiceRegistry;
import org.springframework.web.servlet.theme.AbstractThemeResolver;

/**
 * ThemeResolver to determine the theme for CAS based on the service provided.
 * If the Service is not found, the ThemeResolver will return the default theme.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class ServiceThemeResolver extends AbstractThemeResolver {

    public static final String SERVICE_THEME_KEY = "service";

    private ServiceRegistry serviceRegistry;

    public String resolveThemeName(HttpServletRequest request) {
        if (this.serviceRegistry == null) {
            return getDefaultThemeName();
        }

        String serviceId = request.getParameter(SERVICE_THEME_KEY);
        AuthenticatedService service = this.serviceRegistry
            .getService(serviceId);

        return service != null && service.getTheme() != null ? service
            .getTheme() : getDefaultThemeName();
    }

    public void setThemeName(HttpServletRequest request,
        HttpServletResponse response, String themeName) {
        // nothing to do here
    }
}
