/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.remoteuser.authentication.principal;

import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;


/**
 * Extracts the Principal out of PrincipalBearingCredentials.
 * It is very simple to resolve PrincipalBearingCredentials to a Principal
 * since the credentials already bear the ready-to-go Principal.
 * 
 * @author Andrew Petro
 * @version $Revision$ $Date$
 * @since 3.0.5
 */
public final class PrincipalBearingCredentialsToPrincipalResolver 
    implements CredentialsToPrincipalResolver {

    public Principal resolvePrincipal(final Credentials credentials) {
        return ((PrincipalBearingCredentials) credentials).getPrincipal();
    }

    public boolean supports(final Credentials credentials) {
        return credentials.getClass().equals(PrincipalBearingCredentials.class);
    }
}