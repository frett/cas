/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.UnsupportedCredentialsException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.springframework.beans.factory.InitializingBean;

/**
 * Default implementation of AuthenticationManager. Default implementation
 * accepts a list of handlers. It will iterate through the list of handlers and
 * return the principal for the first one that can validate the request.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */

public final class AuthenticationManagerImpl implements AuthenticationManager,
    InitializingBean {

    /** Log instance for logging events, errors, warnigs, etc. */
    private final Log log = LogFactory.getLog(getClass());

    /** A list of authentication handlers. */
    private List authenticationHandlers;

    /** A list of CredentialsToPrincipalResolvers. */
    private List credentialsToPrincipalResolvers;

    public Authentication authenticateAndResolveCredentials(
        final Credentials credentials) throws AuthenticationException {
        boolean authenticated = false;

        for (Iterator iter = this.authenticationHandlers.iterator(); iter
            .hasNext();) {
            final AuthenticationHandler handler = (AuthenticationHandler) iter
                .next();

            try {
                if (!handler.authenticate(credentials)) {
                    log.info("AuthenticationHandler: "
                        + handler.getClass().getName()
                        + " failed to authenticate the user.");
                    throw new BadCredentialsAuthenticationException();
                }
                log.info("AuthenticationHandler: "
                    + handler.getClass().getName()
                    + " successfully authenticated the user.");
                authenticated = true;
                break;
            } catch (UnsupportedCredentialsException e) {
                continue;
            }
        }

        if (!authenticated) {
            throw new UnsupportedCredentialsException();
        }

        for (final Iterator resolvers = this.credentialsToPrincipalResolvers
            .iterator(); resolvers.hasNext();) {
            final CredentialsToPrincipalResolver resolver = (CredentialsToPrincipalResolver) resolvers
                .next();

            if (resolver.supports(credentials)) {
                final Principal principal = resolver
                    .resolvePrincipal(credentials);

                if (principal == null) {
                    throw new UnsupportedCredentialsException();
                }

                return new ImmutableAuthentication(principal, null);
            }
        }

        log.error("CredentialsToPrincipalResolver not found for "
            + credentials.getClass().getName());
        throw new UnsupportedCredentialsException();
    }

    public void afterPropertiesSet() throws Exception {
        if (this.authenticationHandlers == null
            || this.authenticationHandlers.isEmpty()
            || this.credentialsToPrincipalResolvers == null
            || this.credentialsToPrincipalResolvers.isEmpty()) {
            throw new IllegalStateException(
                "You must provide authenticationHandlers and credentialsToPrincipalResolvers for "
                    + this.getClass().getName());
        }
    }

    /**
     * @param authenticationHandlers The authenticationHandlers to set.
     */
    public void setAuthenticationHandlers(final List authenticationHandlers) {
        this.authenticationHandlers = authenticationHandlers;
    }

    /**
     * @param credentialsToPrincipalResolvers The
     * credentialsToPrincipalResolvers to set.
     */
    public void setCredentialsToPrincipalResolvers(
        final List credentialsToPrincipalResolvers) {
        this.credentialsToPrincipalResolvers = credentialsToPrincipalResolvers;
    }
}
