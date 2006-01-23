/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

import java.security.cert.X509Certificate;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 *
 */
public final class X509CertificateCredentials implements Credentials {

    /** Unique Id for serialization. */
    private static final long serialVersionUID = 7579713688326827121L;

    /** The collection of certificates sent with the request. */
    private X509Certificate[] certificates;
    
    /** The certificate that we actually use. */
    private X509Certificate certificate;

    public X509CertificateCredentials(final X509Certificate[] certificates) {
        this.certificates = certificates;
    }

    public X509Certificate[] getCertificates() {
        return this.certificates;
    }
    
    public void setCertificate(final X509Certificate certificate) {
        this.certificate = certificate;
    }
    
    public X509Certificate getCertificate() {
        return this.certificate;
    }
}
