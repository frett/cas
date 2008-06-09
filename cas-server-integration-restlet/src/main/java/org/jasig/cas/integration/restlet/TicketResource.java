/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.integration.restlet;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketException;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles the creation of Ticket Granting Tickets.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.2.2
 * 
 */
@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE)
public class TicketResource extends Resource {
    
    @Autowired
    private CentralAuthenticationService centralAuthenticationService;
    
    public TicketResource(final Context context, final Request request, final Response response) {
        super(context, request, response);
    }

    public final boolean allowGet() {
        return false;
    }

    public final boolean allowPost() {
        return true;
    }

    @Override
    public final void acceptRepresentation(final Representation entity)
        throws ResourceException {
        super.acceptRepresentation(entity);
        final Credentials c = obtainCredentials();
        try {
            final String ticketGrantingTicketId = this.centralAuthenticationService.createTicketGrantingTicket(c);
            getResponse().setStatus(Status.SUCCESS_CREATED);
            getResponse().setLocationRef(ticketGrantingTicketId);
        } catch (final TicketException e) {
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        }
    }
    
    protected Credentials obtainCredentials() {
        final UsernamePasswordCredentials c = new UsernamePasswordCredentials();
        final WebRequestDataBinder binder = new WebRequestDataBinder(c);
        
        binder.bind(new RestletWebRequest(getRequest()));
        
        return c;
    }
    
    protected class RestletWebRequest implements WebRequest {
        
        private final Request request;
        
        public RestletWebRequest(final Request request) {
            this.request = request;
        }

        public boolean checkNotModified(long lastModifiedTimestamp) {
            return false;
        }

        public String getContextPath() {
            return this.request.getResourceRef().getPath();
        }

        public String getDescription(boolean includeClientInfo) {
            return null;
        }

        public Locale getLocale() {
            return LocaleContextHolder.getLocale();
        }

        public String getParameter(String paramName) {
            return (String) this.request.getAttributes().get(paramName);
        }

        public Map getParameterMap() {
            return this.request.getAttributes();
        }

        public String[] getParameterValues(String paramName) {
            final Object o = this.request.getAttributes().get(paramName);
            
            if (o instanceof String) {
                return new String[] {(String) o};
            }
            
            if (o instanceof String[]) {
                return (String[]) o;
            }
            
            return null;
        }

        public String getRemoteUser() {
            return null;
        }

        public Principal getUserPrincipal() {
            return null;
        }

        public boolean isSecure() {
            return this.request.isConfidential();
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public Object getAttribute(String name, int scope) {
            return null;
        }

        public String[] getAttributeNames(int scope) {
            return null;
        }

        public String getSessionId() {
            return null;
        }

        public Object getSessionMutex() {
            return null;
        }

        public void registerDestructionCallback(String name, Runnable callback,
            int scope) {
            // nothing to do
        }

        public void removeAttribute(String name, int scope) {
            // nothing to do
        }

        public void setAttribute(String name, Object value, int scope) {
            // nothing to do
        }
        
    }
}
