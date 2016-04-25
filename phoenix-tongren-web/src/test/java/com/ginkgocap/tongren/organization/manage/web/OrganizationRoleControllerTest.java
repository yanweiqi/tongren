package com.ginkgocap.tongren.organization.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class OrganizationRoleControllerTest extends SpringContextTestCase<OrganizationRoleController> {
	
	private static final String sessionID = "d2ViNjk3MTQ1MDM0ODI3OTk3MQ==";
	
	private static final String organizationId = "3921107266830361"; //"3905158115491845";

	@Test
	public void testGetMyRole() {
		String userId = "128036";
		String URI = "/manage/role/getMyRole.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("userId", userId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testAdd(){
		String roleName = "test-CEO";
		String description = "test-CEO";
		String URI = "/manage/role/add.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("roleName", roleName);
		jsonRequest.put("description", description);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testEditRole(){
		String roleName = "test前台角色";
		String description = "teset前台---update";
		String roleId = "3908737723269445";
		String URI = "/manage/role/editRole.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("roleName", roleName);
		jsonRequest.put("description", description);
		jsonRequest.put("roleId", roleId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testDelRole(){
		String roleId = "3908365445234873";
		String URI = "/manage/role/delRole.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("roleId", roleId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testGetOrganizationRoles(){
		String URI = "/manage/role/getOrganizationRoles.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		requestController(sessionID, URI, jsonRequest);
	}
	
}

