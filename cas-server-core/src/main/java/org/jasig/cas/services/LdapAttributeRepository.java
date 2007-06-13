/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Retrieves all of the LDAP attributes (MUST and MAY) from a specified entry in
 * the LDAP server.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class LdapAttributeRepository implements AttributeRepository {
    
    private Log log = LogFactory.getLog(getClass());

    private final Map<String, Attribute> attributesMap = new HashMap<String, Attribute>();

    public LdapAttributeRepository(final String schemaLookupDn, final String url) {
        this(schemaLookupDn, url, "com.sun.jndi.ldap.LdapCtxFactory");
    }

    public LdapAttributeRepository(final String schemaLookupDn,
        final String url, final String factory) {
        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
        env.put(Context.PROVIDER_URL, url);

        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            final DirContext tedClasses = ctx
                .getSchemaClassDefinition(schemaLookupDn);

            final NamingEnumeration<SearchResult> ne = tedClasses
                .search("", null);
            while (ne.hasMore()) {
                final SearchResult result = ne.next();
                final Attributes attrs = result.getAttributes();

                populateListWithAttributes(attrs.get("MUST"));
                populateListWithAttributes(attrs.get("MAY"));
            }
        } catch (final Exception e) {
            log.error(e,e);
            throw new RuntimeException(e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (final Exception e) {
                    // do nothing
                }
            }
        }
    }

    public Attribute getAttribute(String id) {
        return this.attributesMap.get(id);
    }

    public Collection<Attribute> getAttributes() {
        return this.attributesMap.values();
    }

    private void populateListWithAttributes(
        final javax.naming.directory.Attribute attribute)
        throws NamingException {
        if (attribute == null) {
            return;
        }

        final NamingEnumeration a = attribute
            .getAll();

        while (a.hasMore()) {
            final String s = (String) a.next();
            final Attribute current = new Attribute(s, s);
            this.attributesMap.put(s, current);
        }
    }
}
