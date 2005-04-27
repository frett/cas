/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.validation;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Default implementation of the Assertion interface which returns the minimum
 * number of attributes required to conform to the CAS 2 protocol.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class ImmutableAssertionImpl implements Assertion {

    /** The list of principals. */
    private final List principals;

    /** Was this the result of a new login. */
    private final boolean fromNewLogin;

    /**
     * 
     * @param principals the chain of principals
     * @param fromNewLogin was the service ticket from a new login.
     * @throws IllegalArgumentException if there are no principals.
     */
    public ImmutableAssertionImpl(final List principals,
        final boolean fromNewLogin) {
        if (principals == null || principals.isEmpty()) {
            throw new IllegalArgumentException(
                "principals cannot be null or empty.");
        }

        this.principals = Collections.unmodifiableList(principals);
        this.fromNewLogin = fromNewLogin;
    }

    public List getChainedPrincipals() {
        return this.principals;
    }

    public boolean isFromNewLogin() {
        return this.fromNewLogin;
    }

    public boolean equals(final Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
