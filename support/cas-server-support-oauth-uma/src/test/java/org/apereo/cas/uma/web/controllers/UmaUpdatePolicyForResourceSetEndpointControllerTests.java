package org.apereo.cas.uma.web.controllers;

import org.apereo.cas.uma.ticket.resource.ResourceSet;
import org.apereo.cas.uma.ticket.resource.ResourceSetPolicy;
import org.apereo.cas.uma.ticket.resource.ResourceSetPolicyPermission;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.util.Pac4jUtils;

import lombok.val;
import org.junit.Test;
import org.pac4j.core.profile.CommonProfile;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This is {@link UmaUpdatePolicyForResourceSetEndpointControllerTests}.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
public class UmaUpdatePolicyForResourceSetEndpointControllerTests extends BaseUmaEndpointControllerTests {
    @Test
    public void verifyOperation() throws Exception {
        val results = authenticateUmaRequest();
        var map = new LinkedHashMap<String, Object>();
        map.put("name", "my-resource");
        map.put("resource_scopes", CollectionUtils.wrapList("read", "write"));
        var response = umaCreateResourceSetRegistrationEndpointController.registerResourceSet(MAPPER.writeValueAsString(map), results.getLeft(), results.getMiddle());
        var model = (Map) response.getBody();
        val resourceId = (long) model.get("resourceId");

        var policy = new ResourceSetPolicy();
        var perm = new ResourceSetPolicyPermission();
        perm.setScopes(CollectionUtils.wrapHashSet("read", "write"));
        final CommonProfile profile = (CommonProfile) Pac4jUtils.getPac4jProfileManager(results.getLeft(), results.getMiddle()).get(true).get();
        perm.setSubject(profile.getId());
        policy.setPermissions(CollectionUtils.wrapHashSet(perm));
        val body = MAPPER.writeValueAsString(policy);

        response = umaCreatePolicyForResourceSetEndpointController.createPolicyForResourceSet(resourceId, body, results.getLeft(), results.getMiddle());
        model = (Map) response.getBody();
        val policyId = ((ResourceSet) model.get("entity")).getPolicies().iterator().next().getId();

        policy = new ResourceSetPolicy();
        perm = new ResourceSetPolicyPermission();
        perm.setScopes(CollectionUtils.wrapHashSet("read"));
        perm.setSubject(profile.getId());
        policy.setPermissions(CollectionUtils.wrapHashSet(perm));
        val bodyNew = MAPPER.writeValueAsString(policy);

        response = umaUpdatePolicyForResourceSetEndpointController.updatePoliciesForResourceSet(resourceId, policyId, bodyNew, results.getLeft(), results.getMiddle());
        model = (Map) response.getBody();
        assertTrue(model.containsKey("code"));
        assertTrue(model.containsKey("entity"));
    }
}