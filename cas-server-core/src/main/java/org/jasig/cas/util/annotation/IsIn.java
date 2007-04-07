/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.util.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * States that the field value must be in the array of values.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
@Target( {ElementType.FIELD})
public @interface IsIn {

    int[] value();
}
