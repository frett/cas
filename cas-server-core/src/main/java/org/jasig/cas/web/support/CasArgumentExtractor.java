/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.web.support;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.springframework.util.StringUtils;
import org.springframework.webflow.execution.RequestContext;

/**
 * Implements the traditional CAS2 protocol.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class CasArgumentExtractor extends AbstractArgumentExtractor {

    /** Parameter to retrieve the service. */
    private static final String PARAM_SERVICE = "service";

    /** Parameter to retrivve the ticket. */
    private static final String PARAM_TICKET = "ticket";

    public String getArtifactParameterName() {
        return PARAM_TICKET;
    }

    public String getServiceParameterName() {
        return PARAM_SERVICE;
    }

    public final Service extractService(final HttpServletRequest request) {
        return WebApplicationService.createServiceFrom(request);
    }

    public String constructUrlForRedirect(final RequestContext context) {
        final String service = context.getRequestParameters().get(
            getServiceParameterName());
        final String serviceTicket = WebUtils
            .getServiceTicketFromRequestScope(context);
        final String artifactParameterName = getArtifactParameterName();

        if (service == null) {
            return null;
        }

        final StringBuilder buffer = new StringBuilder(service.length()
            + serviceTicket.length() + artifactParameterName.length() + 2);

        buffer.append(service);
        buffer.append(service.contains("?") ? "&" : "?");

        if (StringUtils.hasText(serviceTicket)) {
            buffer.append(artifactParameterName);
            buffer.append("=");
            buffer.append(serviceTicket);
        }

        return buffer.toString();
    }
}
