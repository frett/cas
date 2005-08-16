/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.web.flow;

import org.springframework.webflow.execution.servlet.ServletFlowExecutionManager;

/**
 * Specific subclass of FlowExecutionManager to override default value for
 * FlowExecutionIdParameterName to match the login token from the CAS 2.0
 * protocol.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class CasServletFlowExecutionManager extends ServletFlowExecutionManager {

    /**
     * The string constant representing what CAS identifies as the Flow Id.
     * Matches the Login Token id from CAS2 to eliminate the need to duplicate
     * logic.
     */
    private static final String LOGIN_TOKEN = "lt";

    /**
     * This method is overridden to match our login token id from the CAS 2
     * domain.
     */
    protected final String getFlowExecutionIdParameterName() {
        return LOGIN_TOKEN;
    }
}
