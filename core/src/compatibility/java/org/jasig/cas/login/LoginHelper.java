/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */

package org.jasig.cas.login;

import java.io.IOException;

import com.meterware.httpunit.WebResponse;

/**
 * Helper class for accomplishing CAS login, a common task of compatibility tests.
 * 
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class LoginHelper {

	public static String serviceTicketFromResponse(WebResponse webResponse) throws IOException {
		
		String serviceTicket;
		
		 // now we need to extract the service ticket.
        
        // in a baseline CAS 2.x distribution return to the service is accomplished by 
        // JavaScript redirect
        // 
        // CAS 3 accomplishes this differently such that our client has already
        // followed the redirect to the service, so we'll find the service ticket
        // on the response URL.
        
        String queryString = webResponse.getURL().getQuery();
        
        int ticketIndex = queryString.indexOf("ticket=");
        
        if (ticketIndex == -1) {

        	// the ticket wasn't in the response URL.
            // we're testing for CAS 2.x style JavaScript for redirection, as 
        	// recommended in appendix B of the CAS 2 protocol specification
        	
        	// parse the ticket out of the JavaScript
        	
        	String response = webResponse.getText();
            
            int declarationStartsAt = response.indexOf("window.location.href");
            // cut off the front of the response up to the beginning of the service URL
            String responseAfterWindowLocHref = response.substring(declarationStartsAt + "window.location.href12".length());
            
            // The URL might be single or double quoted
            final int endDoubleQuoteIndex = responseAfterWindowLocHref.indexOf("\"");
            final int endSingleQuoteIndex = responseAfterWindowLocHref.indexOf("\'");
            
            // we will set this variable to be the index of the first ' or " character
            int endQuoteIndex = 0;
            if (endDoubleQuoteIndex == -1 && endSingleQuoteIndex == -1) {
            	throw new RuntimeException("Failed parsing a service ticket from the response:" + response);
            } else if ( (endDoubleQuoteIndex > -1) && 
            		(endDoubleQuoteIndex < endSingleQuoteIndex || endSingleQuoteIndex == -1)) {
            	endQuoteIndex = endDoubleQuoteIndex;
            } else {
            	endQuoteIndex = endSingleQuoteIndex;
            }
            
            int ticketEqualsIndex = responseAfterWindowLocHref.indexOf("ticket=");
            
            serviceTicket = responseAfterWindowLocHref.substring(ticketEqualsIndex + "ticket=".length(), endQuoteIndex);
            
        	
        } else {
        	// service ticket was found on query String, parse it from there
        	
        	// TODO Is this type of redirection compatible?  
        	// Does it address all the issues that CAS2 JavaScript redirection
        	// was intended to address?
        	
        	serviceTicket = queryString.substring(ticketIndex + "ticket=".length(), queryString.length());
        	
        }
        
        return serviceTicket;
	}
	
}
