/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.nis;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.util.PasswordTranslator;
import org.jasig.cas.util.support.PlainTextPasswordTranslator;
import org.springframework.beans.factory.DisposableBean;

/**
 * Class to authenticate users by connecting to an NIS server. Defaults are: map =
 * paswd.byname contextFactory = com.sun.jndi.nis.NISCtxFactory
 * securityAuthentication = simple passwordTranslator =
 * PlainTextPasswordTranslator protocol = nis://
 * 
 * @author Scott Battaglia
 * @version $Id: NisAuthenticationHandler.java,v 1.4 2005/03/07 21:00:05
 * sbattaglia Exp $
 */
// TODO: can we keep the context open?
public class NisAuthenticationHandler extends
    AbstractUsernamePasswordAuthenticationHandler implements DisposableBean {

    private static final String DEFAULT_MAP = "passwd.byname";

    private static final String DEFAULT_CONTEXT_FACTORY = "com.sun.jndi.nis.NISCtxFactory";

    private static final String DEFAULT_SECURITY_AUTHENTICATION = "simple";

    private static final PasswordTranslator DEFAULT_PASSWORD_TRANSLATOR = new PlainTextPasswordTranslator();

    private static final String DEFAULT_PROTOCOL = "nis://";

    private String domain;

    private String host;

    private String map = DEFAULT_MAP;

    private PasswordTranslator passwordTranslator = DEFAULT_PASSWORD_TRANSLATOR;

    private String contextFactory = DEFAULT_CONTEXT_FACTORY;

    private String securityAuthentication = DEFAULT_SECURITY_AUTHENTICATION;

    private String url;

    private Hashtable config;

    private InitialContext initialContext;

    public boolean authenticateInternal(final Credentials request) {
        final UsernamePasswordCredentials uRequest = (UsernamePasswordCredentials)request;
        try {
            final String nisEntry = this.initialContext.lookup(
                "system/" + this.map + "/" + uRequest.getUserName()).toString();
            final String nisFields[] = nisEntry.split(":");
            String nisEncryptedPassword = nisFields[1];

            return nisEncryptedPassword.matches(this.passwordTranslator
                .translate(uRequest.getPassword()));
        }
        catch (NamingException e) {
            return false;
        }
    }

    /**
     * @param domain The domain to set.
     */
    public void setDomain(final String domain) {
        this.domain = domain;
    }

    /**
     * @param host The host to set.
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * @param map The map to set.
     */
    public void setMap(final String map) {
        this.map = map;
    }

    /**
     * @param passwordTranslator The passwordTranslator to set.
     */
    public void setPasswordTranslator(
        final PasswordTranslator passwordTranslator) {
        this.passwordTranslator = passwordTranslator;
    }

    public void afterPropertiesSet() throws Exception {
        if (this.domain == null || this.host == null || this.map == null
            || this.passwordTranslator == null || this.contextFactory == null
            || this.securityAuthentication == null) {
            throw new IllegalStateException(
                "domain, host, map, contextFactory, securityAuthentication and passwordTranslator cannot be null on "
                    + this.getClass().getName());
        }

        this.url = DEFAULT_PROTOCOL + this.host + "/" + this.domain;
        this.config = new Hashtable(5, 0.75F);

        this.config.put(Context.INITIAL_CONTEXT_FACTORY, this.contextFactory);
        this.config.put(Context.PROVIDER_URL, this.url);
        this.config.put(Context.SECURITY_AUTHENTICATION,
            this.securityAuthentication);

        this.initialContext = new InitialDirContext(this.config);
    }

    public void destroy() throws Exception {
        this.initialContext.close();
    }
}