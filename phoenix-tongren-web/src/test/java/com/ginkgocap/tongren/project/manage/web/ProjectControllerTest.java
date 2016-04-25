package com.ginkgocap.tongren.project.manage.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class ProjectControllerTest extends SpringContextTestCase<ProjectController> {
	

	private static final String sessionID = "d2ViOTU1MTE0NTA2NjkxMzM2ODc=";
	
	@Test
	public void testCreate() {	
		/**
		 * requestJson:{
		 * "name":"测试闫伟旗",
		 * "introduction":"测试闫伟旗测试闫伟旗测试闫伟旗测试闫伟旗测试闫伟旗",
		 * "validityStartTime":"2015-12-09 18:09:32",
		 * "validityEndTime":"2015-12-31 18:09:34",
		 * "cycle":"23",
		 * "industry":"407",
		 * "area":"500",
		 * "remuneration":"23000",
		 * "taskId":""}
		 */
		String name = "测试闫伟旗法轮功";
		String introduction = "测试闫伟旗测试闫伟旗测试闫伟旗测试闫伟旗测试闫伟旗";
		String area = "500";
		String industry = "407";
		//String taskId = "TVRRME56Z3pPRGd5TmpjME9UQXdNREU0TnpZeU16STI=";
		//String organizationId = "3905158115491845";
		String remuneration = "45y";
		String validityStartTime = "2015-12-09 18:09:32";
		String validityEndTime   = "2015-12-31 18:09:34";
		String cycle = "23";
		String URI = "/project/manage/create.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("name", name);
		jsonRequest.put("introduction", introduction);
		jsonRequest.put("area", area);
		jsonRequest.put("industry", industry);
		//jsonRequest.put("taskId", taskId);
		//jsonRequest.put("organizationId", organizationId);
		jsonRequest.put("industry", industry);
		jsonRequest.put("remuneration", remuneration);
		jsonRequest.put("validityStartTime", validityStartTime);
		jsonRequest.put("validityEndTime", validityEndTime);
		jsonRequest.put("cycle", cycle);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testGetMyAllProjects() throws Exception{
		String URI = "/project/manage/getMyAllProjects.json";
		JSONObject  jsonRequest = new JSONObject();
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testGetMyProjectsUnPublish() throws Exception{
		String URI = "/project/manage/getMyProjectsUnPublish.json";
		JSONObject  jsonRequest = new JSONObject();
		requestController(sessionID, URI, jsonRequest);
	}

}
