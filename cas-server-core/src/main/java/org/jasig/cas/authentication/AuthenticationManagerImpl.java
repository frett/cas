/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication;

import java.util.ArrayList;
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
import org.springframework.util.Assert;

/**
 * <p>
 * Default implementation of the AuthenticationManager. The
 * AuthenticationManager follows the following algorithm. The manager loops
 * through the array of AuthenticationHandlers searching for one that can
 * attempt to determine the validity of the credentials. If it finds one, it
 * tries that one. If that handler returns true, it continues on. If it returns
 * false, it looks for another handler. If it throws an exception, it aborts the
 * whole process and rethrows the exception. Next, it looks for a
 * CredentialsToPrincipalResolver that can handle the credentials in order to
 * create a Principal. Finally, it attempts to populate the Authentication
 * object's attributes map using AuthenticationAttributesPopulators
 * <p>
 * Behavior is determined by external beans attached through three configuration
 * properties. The Credentials are opaque to the manager. They are passed to the
 * external beans to see if any can process the actual type represented by the
 * Credentials marker.
 * <p>
 * AuthenticationManagerImpl requires the following properties to be set:
 * </p>
 * <ul>
 * <li> <code>authenticationHandlers</code> - The array of
 * AuthenticationHandlers that know how to process the credentials provided.
 * <li> <code>credentialsToPrincipalResolvers</code> - The array of
 * CredentialsToPrincipal resolvers that know how to process the credentials
 * provided.
 * </ul>
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 * @see org.jasig.cas.authentication.handler.AuthenticationHandler
 * @see org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver
 * @see org.jasig.cas.authentication.AuthenticationMetaDataPopulator
 */

public final class AuthenticationManagerImpl implements AuthenticationManager,
    InitializingBean {

    /** Log instance for logging events, errors, warnigs, etc. */
    private final Log log = LogFactory.getLog(AuthenticationManagerImpl.class);

    /** An array of authentication handlers. */
    private List<AuthenticationHandler> authenticationHandlers;

    /** An array of CredentialsToPrincipalResolvers. */
    private List<CredentialsToPrincipalResolver> credentialsToPrincipalResolvers;

    /** An array of AuthenticationAttributesPopulators. */
    private List<AuthenticationMetaDataPopulator> authenticationMetaDataPopulators;

    public Authentication authenticate(final Credentials credentials)
        throws AuthenticationException {
        boolean foundSupported = false;
        boolean authenticated = false;

        for (final AuthenticationHandler authenticationHandler : this.authenticationHandlers) {
            if (authenticationHandler.supports(credentials)) {
                foundSupported = true;
                if (!authenticationHandler.authenticate(credentials)) {
                    if (log.isInfoEnabled()) {
                        log
                            .info("AuthenticationHandler: "
                                + authenticationHandler.getClass().getName()
                                + " failed to authenticate the user which provided the following credentials: "
                                + credentials.toString());
                    }
                } else {
                    if (log.isInfoEnabled()) {
                        log
                            .info("AuthenticationHandler: "
                                + authenticationHandler.getClass().getName()
                                + " successfully authenticated the user which provided the following credentials: "
                                + credentials.toString());
                    }
                    authenticated = true;
                    break;
                }
            }
        }

        if (!authenticated) {
            if (foundSupported) {
                throw BadCredentialsAuthenticationException.ERROR;
            }

            throw UnsupportedCredentialsException.ERROR;
        }

        Authentication authentication = null;
        foundSupported = false;

        for (final CredentialsToPrincipalResolver credentialsToPrincipalResolver : this.credentialsToPrincipalResolvers) {
            if (credentialsToPrincipalResolver.supports(credentials)) {
                final Principal principal = credentialsToPrincipalResolver
                    .resolvePrincipal(credentials);
                foundSupported = true;
                if (principal != null) {
                    authentication = new MutableAuthentication(principal);
                    break;
                }
            }
        }

        if (authentication == null) {
            if (foundSupported) {
                if (log.isDebugEnabled()) {
                    log
                        .debug("CredentialsToPrincipalResolver found but no principal returned.");
                }

                throw BadCredentialsAuthenticationException.ERROR;
            }

            log.error("CredentialsToPrincipalResolver not found for "
                + credentials.getClass().getName());
            throw UnsupportedCredentialsException.ERROR;
        }

        for (final AuthenticationMetaDataPopulator authenticationMetaDataPopulator : this.authenticationMetaDataPopulators) {
            authentication = authenticationMetaDataPopulator
                .populateAttributes(authentication, credentials);
        }

        return new ImmutableAuthentication(authentication.getPrincipal(),
            authentication.getAttributes());
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(this.authenticationHandlers,
            "authenticationHandlers is a required property.");
        Assert.notEmpty(this.credentialsToPrincipalResolvers,
            "credentialsToPrincipalResolvers is a required property.");

        if (this.authenticationMetaDataPopulators == null) {
            this.authenticationMetaDataPopulators = new ArrayList<AuthenticationMetaDataPopulator>();
        }
    }

    /**
     * @param authenticationHandlers The authenticationHandlers to set.
     */
    public void setAuthenticationHandlers(
        final List<AuthenticationHandler> authenticationHandlers) {
        this.authenticationHandlers = authenticationHandlers;
    }

    /**
     * @param credentialsToPrincipalResolvers The
     * credentialsToPrincipalResolvers to set.
     */
    public void setCredentialsToPrincipalResolvers(
        final List<CredentialsToPrincipalResolver> credentialsToPrincipalResolvers) {
        this.credentialsToPrincipalResolvers = credentialsToPrincipalResolvers;
    }

    /**
     * @param authenticationMetaDataPopulators the
     * authenticationMetaDataPopulators to set.
     */
    public void setAuthenticationMetaDataPopulators(
        final List<AuthenticationMetaDataPopulator> authenticationMetaDataPopulators) {
        this.authenticationMetaDataPopulators = authenticationMetaDataPopulators;
    }
}
