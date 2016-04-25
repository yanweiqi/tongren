package com.ginkgocap.tongren.organization.manage.web;

import java.util.Random;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class OrganizationDepControllerTest extends SpringContextTestCase<OrganizationController> {

	private static final String sessionId = "d2ViNTg2NjE0NTA4NjA4NzIxNTY=";
	
	private static final String organizationId = "3915315348242467";
	
	@Test
	public void testGetOrganizationDeps() {
		String URI = "/manage/dep/getDeps.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		requestController(sessionId, URI, jsonRequest);
	}

	@Test
	public void testAddDep(){
		String dep_pid = "3906194381209630";
		String URI = "/manage/dep/addDep.json";
		JSONObject  jsonRequest = new JSONObject();
		Random r = new Random();
		jsonRequest.put("depName", "闫伟旗测试"+r.nextInt(100));
		jsonRequest.put("description", "闫伟旗测试"+r.nextInt(100));
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("pid", dep_pid);
		requestController(sessionId, URI, jsonRequest);
	}
	
	@Test
	public void testUpdateDep(){
		//String[] keys = new String[]{"depName","description","organizationId","depId"};
		String depId = "3906228694810639";
		String URI = "/manage/dep/editDep.json";
		JSONObject  jsonRequest = new JSONObject();
		Random r = new Random();
		jsonRequest.put("depName", "update闫伟旗测试"+r.nextInt(100));
		jsonRequest.put("description", "update闫伟旗测试"+r.nextInt(100));
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("depId", depId);
		requestController(sessionId, URI, jsonRequest);
	}
	
	@Test
	public void testDelDep(){
		String depId = "3906194381209630";
		String URI = "/manage/dep/delDep.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("depId", depId);
		requestController(sessionId, URI, jsonRequest);
	}
	
	@Test
	public void testGetOrganizationDepTree() {
		String URI = "/manage/dep/getDepTree.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", "3905158115491845");
		requestController(sessionId, URI, jsonRequest);
	}
}
