/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.util.support;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Id$
 */
public class DesPasswordTranslatorTests extends TestCase {

    private static final String KEY = "12345678";

    private static final String MESSAGE_TO_ENCODE = "rasmuslerdorf";

    // Is this what we want? Base64 Response????
    private static final String ENCODED_MESSAGE = "WS0WSKNr+IX/xpcQnKpC2g==";

    private DesPasswordTranslator passwordTranslator = new DesPasswordTranslator();

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        this.passwordTranslator.setKey(KEY);
        this.passwordTranslator.afterPropertiesSet();
    }

    public void testEncryptedMessage() {
        assertEquals(ENCODED_MESSAGE, this.passwordTranslator
            .translate(MESSAGE_TO_ENCODE));
    }

    public void testNullMessage() {
        assertEquals(null, this.passwordTranslator.translate(null));
    }

    public void testAfterPropertiesSetBad() {
        this.passwordTranslator.setKey(null);

        try {
            this.passwordTranslator.afterPropertiesSet();
            fail("Exception expected.");
        }
        catch (Exception e) {
            // ok
        }
    }

    public void testAfterPropertiesSetGood() {
        this.passwordTranslator.setKey("12345678");

        try {
            this.passwordTranslator.afterPropertiesSet();
        }
        catch (Exception e) {
            fail("Unexpected expected.");
        }
    }
}
