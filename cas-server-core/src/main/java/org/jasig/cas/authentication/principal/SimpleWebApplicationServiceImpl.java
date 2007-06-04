/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * Represents a service which wishes to use the CAS protocol.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.3 $ $Date: 2007/04/24 18:19:22 $
 * @since 3.1
 */
public final class SimpleWebApplicationServiceImpl extends
    AbstractWebApplicationService {

    private static final String CONST_PARAM_SERVICE = "service";

    private static final String CONST_PARAM_TICKET = "ticket";

    /**
     * Unique Id for Serialization
     */
    private static final long serialVersionUID = 8334068957483758042L;

    public SimpleWebApplicationServiceImpl(final String id) {
        super(id, id, null);
    }
    
    private SimpleWebApplicationServiceImpl(final String id,
        final String originalUrl, final String artifactId) {
        super(id, originalUrl, artifactId);
    }

    public static SimpleWebApplicationServiceImpl createServiceFrom(
        final HttpServletRequest request) {
        final String service = request.getParameter(CONST_PARAM_SERVICE);

        if (!StringUtils.hasText(service)) {
            return null;
        }

        final String id = cleanupUrl(service);
        final String artifactId = request.getParameter(CONST_PARAM_TICKET);

        return new SimpleWebApplicationServiceImpl(id, service, artifactId);
    }

    public Response getResponse(final String ticketId) {
        final Map<String, String> parameters = new HashMap<String, String>();

        if (StringUtils.hasText(ticketId)) {
            parameters.put(CONST_PARAM_TICKET, ticketId);
        }

        return Response.getRedirectResponse(getOriginalUrl(), parameters);
    }

    public boolean logOutOfService(final String sessionIdentifier) {
        return false;
    }
}
