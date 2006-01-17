/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import org.jasig.cas.web.flow.util.ContextUtils;
import org.jasig.cas.web.support.WebConstants;
import org.jasig.cas.web.util.WebUtils;
import org.springframework.webflow.RequestContext;
import org.springframework.webflow.ViewDescriptor;
import org.springframework.webflow.ViewDescriptorCreator;

/**
 * Custom View Descriptor that allows the Web Flow to have an end state that
 * allows for redirects based on a URL provided rather than just configured
 * views.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class RedirectViewDescriptorCreator implements
    ViewDescriptorCreator {

    /**
     * @return ViewDescriptor constructed from a ServiceUrl stored in the
     * RequestScope as WebConstants.SERVICE and the model consisting of the
     * ticket id stored in the request scope.
     */
    public ViewDescriptor createViewDescriptor(final RequestContext context) {
        final String service = WebUtils.getRequestParameterAsString(
            ContextUtils.getHttpServletRequest(context), WebConstants.SERVICE);
        final String ticket = (String) ContextUtils.getAttribute(context,
            WebConstants.TICKET);

        if (ticket != null) {
            final ViewDescriptor descriptor = new ViewDescriptor(service,
                WebConstants.TICKET, ticket);
            descriptor.setRedirect(true);
            return descriptor;
        }
        final ViewDescriptor viewDescriptor = new ViewDescriptor(service);
        viewDescriptor.setRedirect(true);
        return viewDescriptor;
    }
}
