/*
 * Copyright 2005 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication;

import org.jasig.cas.authentication.principal.Credentials;

/**
 * An extension point to the Authentication process that allows CAS to provide
 * additional attributes related to the overall Authentication (such as
 * authentication type) that are specific to the Authentication request versus
 * the Principal itself. AuthenticationAttributePopulators are a new feature in
 * CAS3. In order for an installation to be CAS2 compliant, they do not need a
 * AuthenticationAttributesPopulator or they may use a dummy
 * AuthenticationAttributesPopulator.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 */
public interface AuthenticationAttributesPopulator {

    /**
     * Provided with an Authentication object and the original credentials
     * presented, provide any additional attributes to the Authentication
     * object. Implementations have the option of returning the same
     * Authentication object, or a new one.
     * 
     * @param authentication The Authentication to potentially augment with
     * additional attributes.
     * @return the original Authentication object or a new Authentication
     * object.
     */
    Authentication populateAttributes(Authentication authentication,
        Credentials credentials);
}
