/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.validation;

import java.util.ArrayList;
import java.util.List;

import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.AssertionImpl;
import org.jasig.cas.validation.Cas10ProtocolValidationSpecification;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id$
 */
public class Cas10ProtocolValidationSpecificationTests extends TestCase {

    public void testRenewGettersAndSettersFalse() {
        Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification();

        s.setRenew(false);

        assertFalse(s.isRenew());
    }

    public void testRenewGettersAndSettersTrue() {
        Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification();

        s.setRenew(true);

        assertTrue(s.isRenew());
    }

    public void testRenewAsTrueAsConstructor() {
        Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification(
            true);

        assertTrue(s.isRenew());
    }

    public void testRenewAsFalseAsConstructor() {
        Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification(
            false);

        assertFalse(s.isRenew());
    }

    public void testSatisfiesSpecOfTrue() {
        final List list = new ArrayList();
        final Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification(
            true);
        list.add(new SimplePrincipal("test"));
        final Assertion assertion = new AssertionImpl(list, true);
        assertTrue(s.isSatisfiedBy(assertion));
    }

    public void testNotSatisfiesSpecOfTrue() {
        final List list = new ArrayList();
        final Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification(
            true);
        list.add(new SimplePrincipal("test"));
        final Assertion assertion = new AssertionImpl(list, false);
        assertFalse(s.isSatisfiedBy(assertion));
    }

    public void testSatisfiesSpecOfFalse() {
        final List list = new ArrayList();
        final Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification(
            false);
        list.add(new SimplePrincipal("test"));
        final Assertion assertion = new AssertionImpl(list, true);
        assertTrue(s.isSatisfiedBy(assertion));
    }

    public void testSatisfiesSpecOfFalse2() {
        final List list = new ArrayList();
        final Cas10ProtocolValidationSpecification s = new Cas10ProtocolValidationSpecification(
            false);
        list.add(new SimplePrincipal("test"));
        final Assertion assertion = new AssertionImpl(list, false);
        assertTrue(s.isSatisfiedBy(assertion));
    }

}