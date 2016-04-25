package com.ginkgocap.tongren.organization.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class OrganizationMemberControllerTest extends SpringContextTestCase<OrganizationDepMemberController> {

	private static final String sessionId = "d2ViNDU4NDE0NDc3NjkwNzE4NTg=";
	
	private static final String organizationId = "3905158115491845";
	
	public void testInvite() {
		//{"oid|R","gintong","friend","search","content"};
		
		String URI = "/organizationMember/invite.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("oid", organizationId);
		jsonRequest.put("gintong", "11-12");
		jsonRequest.put("friend", "13-14");
		jsonRequest.put("search", "15-16");
		jsonRequest.put("content", "test欢迎加入闫伟旗的组织");
		requestController(sessionId, URI, jsonRequest);
	}

	public void testDel() {
		//{"oid","userId"}
		String userId = "12";
		String URI = "/organizationMember/delMember.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("oid", organizationId);
		jsonRequest.put("userId", userId);
		requestController(sessionId, URI, jsonRequest);
	}

	public void testDelAll() {

	}
	
	@Test
	public void testGetApplicationMember(){
		String sessionIdTest = "d2ViNTAwNzE0NDk4MjUwMjg2NDk=";
		String URI = "/organizationMember/getApplicationMember.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", "3915989003796485");
		jsonRequest.put("type", 2);//2申请加入  3.申请退出
		requestController(sessionIdTest, URI, jsonRequest);
	}
	@Test
	public void testDelMember(){
		String sessionIdTest = "d2ViMTgyNzE0NDc2Mzk2MDM2NDg=";
		String userId = "13721";
		String oid = "3908705372602414";
		String URI = "/organizationMember/getApplicationMember.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("oid", oid);
		jsonRequest.put("userId", userId);//2申请加入  3.申请退出
		requestController(sessionIdTest, URI, jsonRequest);
	}
	
	/**
	 * 获取用户组织中的详细信息
	 */
	@Test
	public void testGetMyOrganizationMemberDetail(){
		String URI = "/organizationMember/getMemberDetail.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		requestController(sessionId, URI, jsonRequest);
	}
	
	
}
