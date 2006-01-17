/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
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

    public void testProperId() {
        assertEquals("Ids are not equal.", TestUtils.CONST_USERNAME, TestUtils
            .getService().getId());
    }

    public void testHashCode() {
        assertEquals("HashCodes are not equal.", HashCodeBuilder
            .reflectionHashCode(TestUtils.getService()), TestUtils.getService()
            .hashCode());
    }

    public void testToString() {
        Service service = TestUtils.getService();
        assertEquals("ToStrings are not equal.", ToStringBuilder
            .reflectionToString(service), service.toString());
    }

    public void testEqualsWithNull() {
        assertFalse("Service matches null.", TestUtils.getService()
            .equals(null));
    }

    public void testEqualsWithBadClass() {
        assertFalse("Services matches String class.", TestUtils.getService()
            .equals(new Object()));
    }

    public void testEquals() {
        assertTrue("Services are not equal.", TestUtils.getService().equals(
            TestUtils.getService()));
    }
}