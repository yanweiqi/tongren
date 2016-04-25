package com.ginkgocap.tongren.project.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class PublishControllerTest extends SpringContextTestCase<PublishController> {
	
	private static final String sessionID = "d2ViMzY4NDE0NTAwNTk2NTEwMjQ=";
	
	@Test
	public void testPublish() {
		String projectId = "3912660471250956";
		String URI = "/project/manage/publish.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testGetPagePublishValidity() {
		String URI = "/project/manage/getPagePublishValidity.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("pageNumber", "2");
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testGetAllPublishValidity() {
		
		String URI = "/project/manage/getAllPublishValidity.json";
		JSONObject  jsonRequest = new JSONObject();
		String applyStatus = ""; //1,2
		jsonRequest.put("applyStatus", applyStatus);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void testProjectOperation() {
		String URI = "/project/manage/projectOperation.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", "3908656357965830");
		jsonRequest.put("type", "1");
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testDiscoverPublishProject(){
		String URI = "/project/manage/discoverPublishProject.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("pageNumber", "1");
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testResubmit(){
		String URI = "/project/manage/resubmit.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", "3918811258683487");
		jsonRequest.put("delay", 15);
		requestController(sessionID, URI, jsonRequest);
	}
}
