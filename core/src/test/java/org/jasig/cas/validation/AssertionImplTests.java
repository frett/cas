/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.validation.ImmutableAssertionImpl;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id: AssertionImplTests.java,v 1.4 2005/02/27 05:49:26 sbattaglia
 * Exp $
 */
public class AssertionImplTests extends TestCase {

    public void testNullParameters() {
        try {
            new ImmutableAssertionImpl(null, false);
        }
        catch (IllegalArgumentException e) {
            return;
        }

        fail("IllegalArgumentException expected.");
    }

    public void testEmptyParameters() {
        try {
            new ImmutableAssertionImpl(new ArrayList(), false);
        }
        catch (IllegalArgumentException e) {
            return;
        }

        fail("IllegalArgumentException expected.");
    }

    public void testGettersForChainedPrincipals() {
        final List list = new ArrayList();

        list.add(new SimplePrincipal("test"));
        list.add(new SimplePrincipal("test1"));
        list.add(new SimplePrincipal("test2"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, true);

        assertEquals(list, assertion.getChainedPrincipals());
    }

    public void testGetterFalseForNewLogin() {
        final List list = new ArrayList();

        list.add(new SimplePrincipal("test"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, false);

        assertFalse(assertion.isFromNewLogin());
    }

    public void testGetterTrueForNewLogin() {
        final List list = new ArrayList();

        list.add(new SimplePrincipal("test"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, true);

        assertTrue(assertion.isFromNewLogin());
    }

    public void testEqualsWithNull() {
        final List list = new ArrayList();
        list.add(new SimplePrincipal("test"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, true);

        assertFalse(assertion.equals(null));
    }

    public void testEqualsWithInvalidObject() {
        final List list = new ArrayList();
        list.add(new SimplePrincipal("test"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, true);

        assertFalse(assertion.equals("test"));
    }

    public void testEqualsWithValidObject() {
        final List list = new ArrayList();
        final List list1 = new ArrayList();
        list.add(new SimplePrincipal("test"));
        list1.add(new SimplePrincipal("test"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, true);
        final ImmutableAssertionImpl assertion1 = new ImmutableAssertionImpl(list1, true);

        assertTrue(assertion.equals(assertion1));
    }

    public void testToString() {
        final List list = new ArrayList();

        list.add(new SimplePrincipal("test"));

        final ImmutableAssertionImpl assertion = new ImmutableAssertionImpl(list, true);
        assertEquals(ToStringBuilder.reflectionToString(assertion), assertion
            .toString());
    }
}