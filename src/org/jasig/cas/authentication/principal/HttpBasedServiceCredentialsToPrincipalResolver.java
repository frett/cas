package org.jasig.cas.authentication.principal;

import org.jasig.cas.authentication.SimpleService;


/**
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class HttpBasedServiceCredentialsToPrincipalResolver implements CredentialsToPrincipalResolver {

	/**
	 * @see org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver#resolvePrincipal(org.jasig.cas.authentication.principal.Credentials)
	 */
	public Principal resolvePrincipal(Credentials credentials) {
		HttpBasedServiceCredentials serviceCredentials = (HttpBasedServiceCredentials) credentials;
		return new SimpleService(serviceCredentials.getCallbackUrl().toExternalForm());
	}

	/**
	 * @see org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver#supports(org.jasig.cas.authentication.principal.Credentials)
	 */
	public boolean supports(Credentials credentials) {
        return HttpBasedServiceCredentials.class.isAssignableFrom(credentials.getClass());
	}
}
