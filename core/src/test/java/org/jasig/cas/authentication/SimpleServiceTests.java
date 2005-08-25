/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jasig.cas.TestUtils;
import org.jasig.cas.authentication.principal.Service;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class SimpleServiceTests extends TestCase {

    private final static String CONST_ID = "test";

    public void testNullId() {
        try {
            TestUtils.getService(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    public void testProperId() {
        assertEquals(CONST_ID, TestUtils.getService(CONST_ID).getId());
    }

    public void testHashCode() {
        assertEquals(
            HashCodeBuilder.reflectionHashCode(TestUtils.getService()),
            TestUtils.getService().hashCode());
    }

    public void testToString() {
        Service service = TestUtils.getService();
        assertEquals(ToStringBuilder.reflectionToString(service), service
            .toString());
    }

    public void testEqualsWithNull() {
        assertFalse(TestUtils.getService().equals(null));
    }

    public void testEqualsWithBadClass() {
        assertFalse(TestUtils.getService().equals("test"));
    }

    public void testEquals() {
        assertTrue(TestUtils.getService().equals(TestUtils.getService()));
    }
}