/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.remoteuser.web.flow;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.adaptors.remoteuser.authentication.principal.PrincipalBearingCredentials;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.web.flow.AbstractNonInteractiveCredentialsAction;
import org.jasig.cas.web.flow.util.ContextUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.RequestContext;

/**
 * Implementation of the NonInteractiveCredentialsAction that looks for a remote
 * user that is set in the <code>HttpServletRequest</code> and attempts to
 * construct a Principal (and thus a PrincipalBearingCredentials). If it doesn't
 * find one, this class returns and error event which tells the web flow it
 * could not find any credentials.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.5
 */
public final class PrincipalFromRequestRemoteUserNonInteractiveCredentialsAction extends
    AbstractNonInteractiveCredentialsAction {

    protected Credentials constructCredentialsFromRequest(
        final RequestContext context) {
        final HttpServletRequest request = ContextUtils
            .getHttpServletRequest(context);
        final String remoteUser = request.getRemoteUser();

        if (StringUtils.hasText(remoteUser)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Remote  User [" + remoteUser + "] found in HttpServletRequest");
            }
            return new PrincipalBearingCredentials(new SimplePrincipal(
                remoteUser));
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Remote User not found in HttpServletRequest.");
        }

        return null;
    }
}
