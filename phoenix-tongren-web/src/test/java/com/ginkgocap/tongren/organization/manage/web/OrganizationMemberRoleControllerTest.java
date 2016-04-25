package com.ginkgocap.tongren.organization.manage.web;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;
import com.ginkgocap.tongren.base.SpringContextTestCase;

public class OrganizationMemberRoleControllerTest{

	private String cookie = "sessionID=\"d2ViNDQ1NDE0NjA5NDc0NTAzMDE=\"";
	private String baseUrl = "http://localhost/";
	
	/**
	 * 设置用户角色
	 */
	@Test
	public void testSetUserRole(){
//		String roleId = "3912366140162066";
//		String organizationMemberId = "3913008762060805";
//		String URI = "/manage/role/setUserRole.json";
//		JSONObject  jsonRequest = new JSONObject();
//		jsonRequest.put("organizationId", organizationId);
//		jsonRequest.put("roleId", roleId);
//		jsonRequest.put("organizationMemberId", organizationMemberId);
		
	}
	
	/**
	 * 设置管理员角色
	 */
	@Test
	public void testSetAdminRole(){
//		String roleId = "3905158115491850";
//		String organizationMemberId = "3905158123880453";
//		String URI = "/manage/role/setAdminRole.json";
//		JSONObject  jsonRequest = new JSONObject();
//		jsonRequest.put("organizationId", organizationId);
//		jsonRequest.put("roleId", roleId);
//		jsonRequest.put("organizationMemberId", organizationMemberId);
		 
	}
	
	
	@Test
	public void testsetManagerRole(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", "3915313980899348");
		params.put("organizationMemberId", "3922492020490270");
		params.put("type", "1");
		try {
			pt.testURI("manage/role/setManagerRole.json", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testgetAllAdminRole(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", "3915313980899348");
		try {
			pt.testURI("manage/role/getAllAdminRole.json", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
