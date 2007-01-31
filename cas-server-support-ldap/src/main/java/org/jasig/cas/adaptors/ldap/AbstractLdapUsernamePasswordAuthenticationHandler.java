/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.adaptors.ldap;

import org.jasig.cas.adaptors.ldap.util.AuthenticatedLdapContextSource;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.ldap.LdapTemplate;
import org.springframework.util.Assert;

/**
 * Abstract class to handle common LDAP functionality.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.3
 */
public abstract class AbstractLdapUsernamePasswordAuthenticationHandler extends
    AbstractUsernamePasswordAuthenticationHandler {

    /** LdapTemplate to execute ldap queries. */
    private LdapTemplate ldapTemplate;
    
    /** Instance of ContextSource */
    private AuthenticatedLdapContextSource contextSource;

    /** The filter path to the uid of the user. */
    private String filter;
    
    /** Whether the LdapTemplate should ignore partial results. */
    private boolean ignorePartialResultException = false;

    /**
     * Method to set the datasource and generate a JdbcTemplate.
     * 
     * @param dataSource the datasource to use.
     */
    public final void setContextSource(final AuthenticatedLdapContextSource contextSource) {
        this.contextSource = contextSource;
        this.ldapTemplate = new LdapTemplate(contextSource);
    }
    
    public final void setIgnorePartialResultException(final boolean ignorePartialResultException) {
        this.ignorePartialResultException = ignorePartialResultException;
    }

    /**
     * Method to return the jdbcTemplate
     * 
     * @return a fully created JdbcTemplate.
     */
    protected final LdapTemplate getLdapTemplate() {
        return this.ldapTemplate;
    }

    protected final AuthenticatedLdapContextSource getContextSource() {
        return this.contextSource;
    }

    protected final String getFilter() {
        return this.filter;
    }

    protected final void afterPropertiesSetInternal() throws Exception {
        Assert.notNull(this.ldapTemplate, "ldapTemplate cannot be null");
        Assert.hasText(this.filter, "filter cannot be null");
        Assert.isTrue(this.filter.indexOf("%u") != -1, "filter must contain %u");
        this.ldapTemplate.setIgnorePartialResultException(this.ignorePartialResultException);
        initDao();
    }

    /**
     * @param filter The filter to set.
     */
    public void setFilter(final String filter) {
        this.filter = filter;
    }

    /**
     * Template method to do additional set up in the dao implementations.
     * 
     * @throws Exception if there is a problem during set up.
     */
    protected void initDao() throws Exception {
        // nothing to do here...stub
    }
}
