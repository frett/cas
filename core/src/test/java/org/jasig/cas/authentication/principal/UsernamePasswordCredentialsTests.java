/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import org.apache.commons.lang.builder.ToStringBuilder;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class UsernamePasswordCredentialsTests extends TestCase {

    public void testSetGetUsername() {
        final UsernamePasswordCredentials c = new UsernamePasswordCredentials();
        final String userName = "test";

        c.setUsername(userName);

        assertEquals(userName, c.getUsername());
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

        c.setUsername(userName);

        assertEquals(new ToStringBuilder(c).append("userName", c.getUsername())
            .toString(), c.toString());
    }
}