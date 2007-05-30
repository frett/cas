/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.util.DefaultUniqueTicketIdGenerator;
import org.jasig.cas.util.SamlUtils;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.springframework.util.StringUtils;

/**
 * Class to represent that this service wants to use SAML. We use this in
 * combination with the CentralAuthenticationServiceImpl to choose the right
 * UniqueTicketIdGenerator.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.6 $ $Date: 2007/02/27 19:31:58 $
 * @since 3.1
 */
public final class SamlService extends AbstractWebApplicationService {

    private static final Log LOG = LogFactory.getLog(SamlService.class);

    /** Constant representing service. */
    private static final String CONST_PARAM_SERVICE = "TARGET";

    /** Constant representing artifact. */
    private static final String CONST_PARAM_TICKET = "SAMLart";

    private static final UniqueTicketIdGenerator GENERATOR = new DefaultUniqueTicketIdGenerator();

    private boolean loggedOutAlready = false;

    /**
     * Unique Id for serialization.
     */
    private static final long serialVersionUID = -6867572626767140223L;

    protected SamlService(final String id) {
        super(id, id, null);
    }

    protected SamlService(final String id, final String originalUrl,
        final String artifactId) {
        super(id, originalUrl, artifactId);
    }

    public static WebApplicationService createServiceFrom(
        final HttpServletRequest request) {
        final String service = request.getParameter(CONST_PARAM_SERVICE);

        if (!StringUtils.hasText(service)) {
            return null;
        }

        final String id = cleanupUrl(service);
        final String artifactId = request.getParameter(CONST_PARAM_TICKET);

        return new SamlService(id, service, artifactId);
    }

    public Response getResponse(final String ticketId) {
        final Map<String, String> parameters = new HashMap<String, String>();

        parameters.put(CONST_PARAM_TICKET, ticketId);
        parameters.put(CONST_PARAM_SERVICE, getOriginalUrl());

        return Response.getRedirectResponse(getOriginalUrl(), parameters);
    }

    public synchronized boolean logOutOfService(final String sessionIdentifier) {
        if (this.loggedOutAlready) {
            return true;
        }

        LOG.debug("Sending logout request for: " + getId());

        final String logoutRequest = "<samlp:LogoutRequest ID=\""
            + GENERATOR.getNewTicketId("LR")
            + "\" Version=\"2.0\" IssueInstant=\"" + SamlUtils.getCurrentDateAndTime()
            + "\"><saml:NameID>" + getPrincipal().getId() + "</saml:NameID><samlp:SessionIndex>"
            + sessionIdentifier + "</samlp:SessionIndex></samlp:LogoutRequest>";

        HttpURLConnection connection = null;
        try {
            final URL logoutUrl = new URL(getOriginalUrl());
            final String output = "logoutRequest=" + logoutRequest;

            connection = (HttpURLConnection) logoutUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Length", ""
                + Integer.toString(output.getBytes().length));
            connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
            final DataOutputStream printout = new DataOutputStream(connection
                .getOutputStream());
            printout.writeBytes(output);
            printout.flush();
            printout.close();

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection
                .getInputStream()));

            while (in.readLine() != null) {
                // nothing to do
            }
            
            return true;
        } catch (final MalformedURLException e) {
            return false;
        } catch (final IOException e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            this.loggedOutAlready = true;
        }
    }
}
