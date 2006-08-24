/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import org.jasig.cas.web.CasArgumentExtractor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.test.MockRequestContext;

import junit.framework.TestCase;


public class GatewayRequestCheckActionTests extends TestCase {
    private GatewayRequestCheckAction action = new GatewayRequestCheckAction();
    
    protected void setUp() throws Exception {
        final CasArgumentExtractor casArgumentExtractor = new CasArgumentExtractor(new CookieGenerator(), new CookieGenerator());
        this.action.setCasArgumentExtractor(casArgumentExtractor);
        this.action.afterPropertiesSet();
    }
    
    public void testGatewayIsTrue() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
        request.addParameter("gateway", "true");
        request.addParameter("service", "test");
        
        assertEquals("success", this.action.execute(context).getId());
    }

    public void testGatewayIsFalse() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
        request.addParameter("service", "test");
        
        assertEquals("error", this.action.execute(context).getId());
    }
    
    public void testGatewayIsTrueNoService() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
        
        assertEquals("error", this.action.execute(context).getId());
    }    
    
}
