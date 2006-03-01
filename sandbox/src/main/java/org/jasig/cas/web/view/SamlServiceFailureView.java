/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.SAMLException;
import org.opensaml.SAMLResponse;
import org.springframework.web.servlet.View;

/**
 * This Spring View object is connected to an instance of 
 * org.jasig.cas.web.ServiceValidateController. It will be passed
 * control after an unsuccessful ST validation request. It will be
 * passed a Model containing error information. It job is to create
 * a SAML bad-status Response, which is really an empty SAML 
 * Assertion in the SAML 1.1 spec.
 * 
 * This is an extension to the CAS 2.0 protocol standard.
 * 
 * @author Howard Gilbert
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public class SamlServiceFailureView implements View {

	public String getContentType() {
        return "text/xml";
    }

	public void render(final Map model, final HttpServletRequest request,
			final HttpServletResponse response) {
		
		// In OpenSAML, the way to get a bad status response is to
		// create a SAMLException object with the status info.
//		SAMLException samlException = new SAMLException("Invalid Ticket");
		
		// Then pass it to the Response constructor
		SAMLResponse samlResponse;
		try {
			// samlResponse = new SAMLResponse(null,null,null,samlException);
            samlResponse = new SAMLResponse(null,null,null,null);
		} catch (SAMLException e) {
			// Should not occur. Something serious wrong in OpenSAML
			response.setStatus(500);
			return;
		}
		response.setContentType("text/xml");
		try {
			response.getWriter().print(samlResponse.toString());
		} catch (IOException e1) {
			// The Service probably went away before getting its
			// response. Not much can be done.
		}
	}
}
