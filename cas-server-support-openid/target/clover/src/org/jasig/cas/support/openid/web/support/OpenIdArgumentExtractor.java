/* $$ This file has been instrumented by Clover 1.3.12#20060208202937157 $$ *//*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.support.openid.web.support;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.support.openid.authentication.principal.OpenIdService;
import org.jasig.cas.web.support.ArgumentExtractor;

/**
 * Constructs an OpenId Service.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 *
 */
public class OpenIdArgumentExtractor implements ArgumentExtractor {public static class __CLOVER_8_0{public static com_cenqua_clover.g cloverRec;static{try{if(20060208202937157L!=com_cenqua_clover.CloverVersionInfo.getBuildMagic()){java.lang.System.err.println("[CLOVER] WARNING: The Clover version used in instrumentation does not match the runtime version. You need to run instrumented classes against the same version of Clover that you instrumented with.");java.lang.System.err.println("[CLOVER] WARNING: Instr=1.3.12#20060208202937157,Runtime="+com_cenqua_clover.CloverVersionInfo.getReleaseNum() + "#"+com_cenqua_clover.CloverVersionInfo.getBuildMagic());}cloverRec = com_cenqua_clover.f.getRecorder(new char[] {67,58,92,119,111,114,107,115,112,97,99,101,92,99,97,115,51,92,99,97,115,45,115,101,114,118,101,114,45,115,117,112,112,111,114,116,45,111,112,101,110,105,100,92,116,97,114,103,101,116,47,99,108,111,118,101,114,47,99,108,111,118,101,114,46,100,98},1181745647584L,500, true);}catch (Throwable t) {java.lang.System.err.println("[CLOVER] FATAL ERROR: Clover could not be initialised. Are you sure you have Clover in the runtime classpath? ("+t.getClass()+":"+ t.getMessage()+")");}}}

    public WebApplicationService extractService(final HttpServletRequest request) {try {__CLOVER_8_0.cloverRec.M[125]++;
        __CLOVER_8_0.cloverRec.S[467]++;return OpenIdService.createServiceFrom(request);
    }finally{__CLOVER_8_0.cloverRec.D = true;}}
}
