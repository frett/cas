/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication.principal;

/**
 * Generic concept of an authenticated thing.  Examples include a person
 * or a service.
 * 
 * 
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public interface Principal {
	String getId();
}
