/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

import org.jasig.cas.TestUtils;
import org.jasig.cas.util.DSAPrivateKeyFactoryBean;
import org.jasig.cas.util.DSAPublicKeyFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 *
 */
public class GoogleAccountsServiceTests extends TestCase {

    private GoogleAccountsService googleAccountsService;

    @Override
    protected void setUp() throws Exception {
        final DSAPublicKeyFactoryBean pubKeyFactoryBean = new DSAPublicKeyFactoryBean();
        final DSAPrivateKeyFactoryBean privKeyFactoryBean = new DSAPrivateKeyFactoryBean();
        
        final ClassPathResource pubKeyResource = new ClassPathResource("DSAPublicKey01.key");
        final ClassPathResource privKeyResource = new ClassPathResource("DSAPrivateKey01.key");
        
        pubKeyFactoryBean.setLocation(pubKeyResource);
        privKeyFactoryBean.setLocation(privKeyResource);
        pubKeyFactoryBean.afterPropertiesSet();
        privKeyFactoryBean.afterPropertiesSet();
        
        final DSAPrivateKey privateKey = (DSAPrivateKey) privKeyFactoryBean.getObject();
        final DSAPublicKey publicKey = (DSAPublicKey) pubKeyFactoryBean.getObject();
        
        final MockHttpServletRequest request = new MockHttpServletRequest();
        
        final String SAMLRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><samlp:AuthnRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"5545454455\" Version=\"2.0\" IssueInstant=\"Value\" ProtocolBinding=\"urn:oasis:names.tc:SAML:2.0:bindings:HTTP-Redirect\" ProviderName=\"https://localhost:8443/myRutgers\" AssertionConsumerServiceURL=\"https://localhost:8443/myRutgers\"/>";
        request.setParameter("SAMLRequest", SAMLRequest);
        
        this.googleAccountsService = GoogleAccountsService.createServiceFrom(request, privateKey, publicKey);
        this.googleAccountsService.setPrincipal(TestUtils.getPrincipal());
    }
    
    public void testResponse() {
        final Response response = this.googleAccountsService.getResponse("ticketId");
        
    }
    
    
}
