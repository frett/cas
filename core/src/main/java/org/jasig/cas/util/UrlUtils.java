/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities class for generic functions related to URLs
 * 
 * @author Scott Battaglia
 * @version $Id$
 * @since 3.0
 */
public class UrlUtils {

    protected static final Log log = LogFactory.getLog(UrlUtils.class);

    /**
     * Method to retrieve the response from a HTTP request for a specific URL.
     * 
     * @param url The URL to contact.
     * @return the body of the response.
     */
    public static String getResponseBodyFromUrl(URL url) {
        URLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuffer buf = new StringBuffer();
        try {
            connection = url.openConnection();
            connection.setRequestProperty("Connection", "close");
            bufferedReader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
            String line = bufferedReader.readLine();
            while (line != null) {
                buf.append(line);
                buf.append("\n");
                line = bufferedReader.readLine();
            }
        }
        catch (Exception e) {
            log.error(e);
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
            catch (IOException e) {
                log.error(e);
            }
        }
        return buf.toString().length() > 0 ? buf.toString() : null;
    }
}