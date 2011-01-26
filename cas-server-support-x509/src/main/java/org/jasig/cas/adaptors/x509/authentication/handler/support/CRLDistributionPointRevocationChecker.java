/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.adaptors.x509.authentication.handler.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.adaptors.x509.util.CertUtils;
import org.springframework.core.io.UrlResource;

import edu.vt.middleware.crypt.x509.ExtensionReader;
import edu.vt.middleware.crypt.x509.types.DistributionPoint;
import edu.vt.middleware.crypt.x509.types.DistributionPointList;
import edu.vt.middleware.crypt.x509.types.GeneralName;
import edu.vt.middleware.crypt.x509.types.GeneralNameList;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


/**
 * Performs CRL-based revocation checking by consulting resources defined in
 * the CRLDistributionPoints extension field on the certificate.  Although RFC
 * 2459 allows the distribution point name to have arbitrary meaning, this class
 * expects the name to define an absolute URL, which is the most common
 * implementation.  This implementation caches CRL resources fetched from remote
 * URLs to improve performance by avoiding CRL fetching on every revocation
 * check.
 *
 * @author Marvin S. Addison
 * @version $Revision$
 * @since 3.4.7
 *
 */
public class CRLDistributionPointRevocationChecker extends AbstractCRLRevocationChecker {
    
    /** Logger instance. */
    private final Log logger = LogFactory.getLog(getClass());
   
    /** CRL cache. */
    private Cache crlCache;
    

    /**
     * Creates a new instance that uses the given cache instance for CRL caching.
     *
     * @param crlCache Cache for CRL data.
     */
    public CRLDistributionPointRevocationChecker(final Cache crlCache) {
        if (crlCache == null) {
            throw new IllegalArgumentException("Cache cannot be null.");
        }
       this.crlCache = crlCache; 
    }

    /**
     * @see AbstractCRLRevocationChecker#getCRL(X509Certificate)
     */
    protected X509CRL getCRL(final X509Certificate cert) {
        final URL[] urls = getDistributionPoints(cert);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(
                String.format("Distribution points for %s: %s.", CertUtils.toString(cert), urls));
        }
        
        Object value;
        for (URL url : urls) {
            value = this.crlCache.get(url);
            if (value != null) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Found CRL in cache for " + CertUtils.toString(cert));
                }
                return (X509CRL) value;
            }
        }

        // Try all distribution points and stop at first fetch that succeeds
        X509CRL crl = null;
        for (int i = 0; i < urls.length && crl == null; i++) {
           this.logger.info("Attempting to fetch CRL at " + urls[i]);
           try {
               crl = CertUtils.fetchCRL(new UrlResource(urls[i]));
               this.logger.info("Success. Caching fetched CRL.");
               this.crlCache.put(new Element(urls[i], crl));
           } catch (Exception e) {
               this.logger.error("Error fetching CRL at " + urls[i], e);
           }
        }
        
        return crl;
    }

    private URL[] getDistributionPoints(final X509Certificate cert) {
        final DistributionPointList points;
        try {
            points = new ExtensionReader(cert).readCRLDistributionPoints();
        } catch (Exception e) {
            this.logger.error(
                "Error reading CRLDistributionPoints extension field on " + CertUtils.toString(cert), e);
            return new URL[0];
        }

        final List<URL> urls = new ArrayList<URL>();
        for (DistributionPoint point : points.getItems()) {
            final Object location = point.getDistributionPoint();
            if (location instanceof String) {
                addURL(urls, (String) location);
            } else if (location instanceof GeneralNameList) {
                for (GeneralName gn : ((GeneralNameList) location).getItems()) {
                    addURL(urls, gn.getName());
                }
            } else {
                this.logger.warn(location + " not supported.  String or GeneralNameList expected.");
            }
        }
        
        return urls.toArray(new URL[urls.size()]);
    }
    
    private void addURL(final List<URL> list, final String url) {
        try {
            list.add(new URL(url));
        } catch (MalformedURLException e) {
            this.logger.warn(url + " is not a valid distribution point URL.");
        }
    }
}
