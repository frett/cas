package org.apereo.cas.web.flow.resolver.impl.mfa.request;

import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.authentication.AuthenticationServiceSelectionPlan;
import org.apereo.cas.authentication.AuthenticationSystemSupport;
import org.apereo.cas.authentication.MultifactorAuthenticationProviderSelector;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.ticket.registry.TicketRegistrySupport;
import org.apereo.cas.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is {@link RequestSessionAttributeMultifactorAuthenticationPolicyEventResolver}
 * that attempts to resolve the next event based on the authentication providers of this service.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
public class RequestSessionAttributeMultifactorAuthenticationPolicyEventResolver extends BaseRequestMultifactorAuthenticationPolicyEventResolver {


    private final String attributeName;

    public RequestSessionAttributeMultifactorAuthenticationPolicyEventResolver(final AuthenticationSystemSupport authenticationSystemSupport,
                                                                               final CentralAuthenticationService centralAuthenticationService,
                                                                               final ServicesManager servicesManager,
                                                                               final TicketRegistrySupport ticketRegistrySupport,
                                                                               final CookieGenerator warnCookieGenerator,
                                                                               final AuthenticationServiceSelectionPlan authenticationStrategies,
                                                                               final MultifactorAuthenticationProviderSelector selector,
                                                                               final CasConfigurationProperties casProperties) {
        super(authenticationSystemSupport, centralAuthenticationService, servicesManager,
            ticketRegistrySupport, warnCookieGenerator, authenticationStrategies, selector);
        attributeName = casProperties.getAuthn().getMfa().getSessionAttribute();
    }

    @Override
    protected List<String> resolveEventFromHttpRequest(final HttpServletRequest request) {
        val session = request.getSession(false);
        var attributeValue = session != null ? session.getAttribute(attributeName) : null;
        if (attributeValue == null) {
            LOGGER.debug("No value could be found for session attribute [{}]. Checking request attributes...", this.attributeName);
            attributeValue = request.getAttribute(attributeName);
        }

        if (attributeValue == null) {
            LOGGER.debug("No value could be found for [{}]", this.attributeName);
            return null;
        }

        val values = CollectionUtils.toCollection(attributeValue);
        LOGGER.debug("Found values [{}] mapped to attribute name [{}]", values, this.attributeName);
        return values.stream().map(Object::toString).collect(Collectors.toList());
    }
}
