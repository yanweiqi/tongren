package com.ginkgocap.tongren.organization.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class OrganizationRoleAuthorityControllerTest extends SpringContextTestCase<OrganizationController> {
	
	private static final String sessionID = "d2ViNjk1MjE0NDk1NDE0MDI2MDY=";

	@Test
	public void testGetMyRole() {
		String organizationId = "3916041902358538";
		String roleId = "3917824598999060";
		String URI = "/manage/auth/getMyAuthoritys.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("roleId", roleId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	//setRoleAuthoritys.json
	//createrId, roleId, organizationId, Arrays.asList(authorityNames)
	@Test
	public void testSetRoleAuthoritys() {
		String organizationId = "3905158115491845";
		String roleId = "3905158115491845";
		String authorityNames = "ORGANIZATION_MANAGE_SHOW,ORGANIZATION_MANAGE_DEPARTMENT_SHOW";
		String URI = "/manage/auth/setRoleAuthoritys.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("roleId", roleId);
		jsonRequest.put("authorityNames", authorityNames);
		requestController(sessionID, URI, jsonRequest);
	}
}

