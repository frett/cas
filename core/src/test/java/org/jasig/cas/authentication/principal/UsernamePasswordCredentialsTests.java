/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id$
 */
public class UsernamePasswordCredentialsTests extends TestCase {

    public void testSetGetUsername() {
        final UsernamePasswordCredentials c = new UsernamePasswordCredentials();
        final String userName = "test";

        c.setUserName(userName);

        assertEquals(userName, c.getUserName());
    }

    public void testSetGetPassword() {
        final UsernamePasswordCredentials c = new UsernamePasswordCredentials();
        final String password = "test";

        c.setPassword(password);

        assertEquals(password, c.getPassword());
    }
    
    public void testToString() {
        final UsernamePasswordCredentials c = new UsernamePasswordCredentials();
        final String userName = "test";

        c.setUserName(userName);
        
        assertEquals(c.getClass().getName()+"@"+ Integer.toHexString(c.hashCode())+"[userName="+userName+"]", c.toString());
    }

}