package org.jasig.cas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class UrlUtils {

	public static String getResponseBodyFromUrl(URL url) {
		URLConnection connection = null;
		BufferedReader bufferedReader = null;
		StringBuffer buf = new StringBuffer();
		try {
			connection = url.openConnection();
			connection.setRequestProperty("Connection", "close");
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				buf.append(line);
				buf.append("\n");
			}
		} catch (Exception e) {
			// can't do anything about this
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				// ignore, nothing we can do about it
			}
		}
		return buf.toString();
	}
}
