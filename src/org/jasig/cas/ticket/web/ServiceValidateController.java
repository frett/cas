/* Copyright 2004 The JA-SIG Collaborative.  All rights reserved.
 * See license distributed with this file and
 * available online at http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.constants.WebConstants;
import org.jasig.cas.ticket.CasAttributes;
import org.jasig.cas.ticket.ProxyGrantingTicket;
import org.jasig.cas.ticket.ProxyTicket;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.TicketManager;
import org.jasig.cas.ticket.validation.ValidationRequest;
import org.springframework.web.bind.BindUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * 
 * Controller to validate ServiceTickets and ProxyGrantingTickets.
 * 
 * @author Scott Battaglia
 * @version $Id$
 *
 */
public class ServiceValidateController extends AbstractController
{
	protected final Log logger = LogFactory.getLog(getClass());
	private TicketManager ticketManager;
	private String casValidationFailure;
	private String casValidationSuccess;

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ValidationRequest validationRequest = new ValidationRequest();
		CasAttributes casAttributes = new CasAttributes();
		Map model = new HashMap();
		ServiceTicket serviceTicket;
		BindUtils.bind(request, validationRequest, "validationRequest");

		logger.info("Attempting to retrieve a valid ServiceTicket for [" + validationRequest.getTicket() + "]");
		serviceTicket = ticketManager.validateServiceTicket(validationRequest);
		
		if (serviceTicket == null)
		{
			logger.info("ServiceTicket [" + validationRequest.getTicket() + "was invalid.");
			model.put(WebConstants.CONST_MODEL_CODE, "INVALID_TICKET");
			model.put(WebConstants.CONST_MODEL_DESC, "ticket '" + validationRequest.getTicket() + "' not recognized.");
			return new ModelAndView(casValidationFailure, model);
		}
		else
		{
			logger.info("ServiceTicket [" + validationRequest.getTicket() + "was valid.");
			if (validationRequest.getPgtUrl() != null)
			{
				logger.info("Creating ProxyGranting Ticket for ServiceTicket [" + validationRequest.getTicket() + ".");
				ProxyGrantingTicket proxyGrantingTicket = ticketManager.createProxyGrantingTicket(serviceTicket.getPrincipal(), casAttributes, serviceTicket);
				model.put(WebConstants.CONST_MODEL_PGTIOU, proxyGrantingTicket.getProxyIou());
			}
		}
		
		if (serviceTicket instanceof ProxyTicket) {
			ProxyTicket p = (ProxyTicket)  serviceTicket;
			model.put(WebConstants.CONST_MODEL_PROXIES, p.getProxies());
		}
		
		model.put(WebConstants.CONST_MODEL_PRINCIPAL, serviceTicket.getPrincipal());
		
		return new ModelAndView(casValidationSuccess, model);
	}
	
	/**
	 * @param casValidationFailure The casValidationFailure to set.
	 */
	public void setCasValidationFailure(String casValidationFailure)
	{
		this.casValidationFailure = casValidationFailure;
	}
	/**
	 * @param casValidationSuccess The casValidationSuccess to set.
	 */
	public void setCasValidationSuccess(String casValidationSuccess)
	{
		this.casValidationSuccess = casValidationSuccess;
	}
	/**
	 * @param ticketManager The ticketManager to set.
	 */
	public void setTicketManager(TicketManager ticketManager)
	{
		this.ticketManager = ticketManager;
	}
}
