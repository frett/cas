/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.webservice;

import org.jasig.cas.domain.UsernamePasswordAuthenticationRequest;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

/**
 * Actual implementation for JAX-RPC that delegates to the actual CasService.
 * 
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class JaxRpcCasService extends ServletEndpointSupport implements CasService, RemoteCasService {
	private static final String CONST_CAS_SERVICE_NAME = "casService";
	private CasService casService;

    protected void onInit() {
        this.casService = (CasService) getWebApplicationContext().getBean(CONST_CAS_SERVICE_NAME);
    }
	/**
	 * @see org.jasig.cas.webservice.RemoteCasService#getServiceTicket(org.jasig.cas.domain.AuthenticationRequest)
	 */
	public String getServiceTicket(UsernamePasswordAuthenticationRequest request, String serviceUrl) {
		return casService.getServiceTicket(request, serviceUrl);
	}
}