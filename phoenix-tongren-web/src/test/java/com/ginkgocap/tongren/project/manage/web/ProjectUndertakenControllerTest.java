package com.ginkgocap.tongren.project.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class ProjectUndertakenControllerTest extends SpringContextTestCase<ProjectUndertakenController> {
	
	private static final String sessionID = "d2ViNzk2NTE0NTExMDE1NjY0NjU=";
	
	@Test
	public void testPostponeProject() {
		String projectId = "3921061519556673";
		String content = "1";
		String URI = "/project/undertaken/postponeProject.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organiztionId", 0);
		jsonRequest.put("projectId", projectId);
		jsonRequest.put("cycle", 1);
		jsonRequest.put("content", content);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void testUndertakenList(){
		String status = "-1";
		String URI = "/project/undertaken/undertakenList.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("status", status);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void getUndertakenProjectInfo(){
		long projectId = 3908370281267205L;
		String URI = "/project/undertaken/getUndertakenProjectInfo.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void testUndertakeProject(){
		String projectId = "3922489701040163";
		String organizationId = "0"; 
		String recipientId = "13594";
		String URI = "/project/undertaken/undertakeProject.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("projectId", projectId);
		jsonRequest.put("recipientId", recipientId);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void projectOperationTest(){
		String URI = "/project/undertaken/projectOperation.json";
		String projectId = "3923562754998277";
		String type = "1";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		jsonRequest.put("type", type);
		requestController(sessionID, URI, jsonRequest);
	}
	
	
}
