package org.jasig.cas.web.bind.support;

import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.web.bind.CredentialsBinder;


/**
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class DefaultSpringBindCredentialsBinder implements CredentialsBinder {

	/**
	 * @see org.jasig.cas.web.bind.CredentialsBinder#bind(javax.servlet.http.HttpServletRequest, org.jasig.cas.authentication.principal.Credentials)
	 */
	public void bind(HttpServletRequest request, Credentials credentials) {
		// don't do anything, Spring binding handles everything already due to the controller
	}
}
