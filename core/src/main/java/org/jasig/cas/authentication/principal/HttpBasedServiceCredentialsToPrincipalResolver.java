/*
 * Copyright 2005 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.SimpleService;

/**
 * Resolver to resolve the credentials presented for an HTTP-based service to a
 * principal.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class HttpBasedServiceCredentialsToPrincipalResolver implements
    CredentialsToPrincipalResolver {

    public Principal resolvePrincipal(Credentials credentials) {
        HttpBasedServiceCredentials serviceCredentials = (HttpBasedServiceCredentials) credentials;

        if (credentials == null) {
            throw new IllegalArgumentException("credentials cannot be null.");
        }

        return new SimpleService(serviceCredentials.getCallbackUrl()
            .toExternalForm());
    }

    public boolean supports(Credentials credentials) {
        return credentials != null
            && HttpBasedServiceCredentials.class.isAssignableFrom(credentials
                .getClass());
    }
}
