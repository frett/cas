/*
 * Copyright 2005 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.handler.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.HttpBasedServiceCredentials;
import org.jasig.cas.util.UrlUtils;

/**
 * Class to validate the credentials presented by communicating with the web
 * server and checking the certificate that is returned against the hostname,
 * etc.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class HttpBasedServiceCredentialsAuthenticationHandler extends
    AbstractAuthenticationHandler {

    protected final Log log = LogFactory.getLog(getClass());

    private static final String PROTOCOL_HTTPS = "https";
	
	private boolean allowNullResponses = false;

    public boolean authenticateInternal(Credentials credentials) {
        final HttpBasedServiceCredentials serviceCredentials = (HttpBasedServiceCredentials)credentials;
        String response = null;
        if (!serviceCredentials.getCallbackUrl().getProtocol().equals(
            PROTOCOL_HTTPS)) {
            return false;
        }
        log
            .debug("Attempting to resolve credentials for "
                + serviceCredentials);
        try {
            response = UrlUtils.getResponseBodyFromUrl(serviceCredentials
                .getCallbackUrl());
			log.debug(response);
        }
        catch (Exception e) {
            log.error(e);
			return false;
        }
		
		return this.allowNullResponses ? true : response != null;
    }

    protected boolean supports(Credentials credentials) {
        return HttpBasedServiceCredentials.class.isAssignableFrom(credentials
            .getClass());
    }
	
	public void setAllowNullResponses(boolean allowNullResponses) {
		this.allowNullResponses = allowNullResponses;
	}
}