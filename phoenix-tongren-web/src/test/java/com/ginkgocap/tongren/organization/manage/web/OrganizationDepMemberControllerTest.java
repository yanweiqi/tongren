package com.ginkgocap.tongren.organization.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class OrganizationDepMemberControllerTest extends SpringContextTestCase<OrganizationDepMemberController> {

	private static final String sessionId = "d2ViNTE1NjE0NDY3MjY4NTQyMTI=";
	
	private static final String organizationId = "3905158115491845";
	
	@Test
	public void testAdd() {
		//String[] keys = new String[]{"organizationId","depId","organizationMemberId"};
		String depId = "3905947772911621";
		String organizationMemberId = "22222222";
		String URI = "/manage/depMember/add.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("depId", depId);
		jsonRequest.put("organizationMemberId", organizationMemberId);
		requestController(sessionId, URI, jsonRequest);
	}

	@Test
	public void testDel() {
		String depId = "3905947772911621";
		String organizationMemberId = "22222221";
		String URI = "/manage/depMember/del.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("depId", depId);
		jsonRequest.put("organizationMemberId", organizationMemberId);
		requestController(sessionId, URI, jsonRequest);
	}

	@Test
	public void testDelAll() {
		String depId = "3905947772911621";
		String URI = "/manage/depMember/delAll.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("depId", depId);
		requestController(sessionId, URI, jsonRequest);
	}

}
