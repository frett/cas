/*
 * Copyright 2005 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.services.support;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.services.AuthenticatedService;
import org.jasig.cas.services.SingleSignoutCallback;
import org.jasig.cas.util.UrlUtils;

/**
 * Single sign out callback class to allow single signout to a Campus Crusade
 * for Christ modified CAS client.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 * 
 */
public class CCCISingleSignoutCallback implements SingleSignoutCallback {

    protected final Log log = LogFactory.getLog(getClass());

    public boolean sendSingleSignoutRequest(AuthenticatedService service,
        String serviceTicketId) {
        try {
            URL callbackUrl = new URL(service.getId());

            callbackUrl = new URL(
                ((callbackUrl.getQuery() != null) ? "&" : "?") + "ticket=~"
                    + serviceTicketId);

            UrlUtils.getResponseBodyFromUrl(callbackUrl);
        }
        catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

}
