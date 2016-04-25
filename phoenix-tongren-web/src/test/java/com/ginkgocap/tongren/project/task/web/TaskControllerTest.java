package com.ginkgocap.tongren.project.task.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;

public class TaskControllerTest {

	private String cookie = "sessionID=\"ODY0NjAxMDI4MzY3MDI3Njg3MDE0NTMzMDM1Mzc2MDM=\"";
	private String baseUrl = "http://localhost/";
	//private String baseUrl = "http://192.168.101.131:6789/";

	@Test
	public void testGetPrimaryTask() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("projectId", "3915660568821805");
		params.put("organizationId", "3915551995068476");
		pt.testURIWithBody("projectTask/getPrimaryTask.json", params);
	}

	// projectTask/addSubTask.json?requestJson={"title":"考勤管理设计","organizationId":"3903677022863450","taskPid":"3908413574873093",
	//"description":"考勤管理设计"}
	/**
	 * 增加子任务
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddSubTask() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "项目模块开发");
		params.put("organizationId", "3903677022863450");
		params.put("taskPid", "3908413574873093");
		params.put("description", "项目模块开发");
		pt.testURI("projectTask/addSubTask.json", params);
	}
	@Test
	public void testAddSubTaskWithMobile() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "阿斯顿发生发");
		params.put("organizationId", "3915551995068476");
		params.put("taskPid", "3916391963164677");
		params.put("description", "项目模块开发");
		params.put("users", "13583,12767,13594");
		params.put("startDate", "2015-12-09 09:00");
		params.put("endDate", "2015-12-15 18:00");
		pt.testURI("projectTask/addSubTaskWithMobile.json", params);
	}
	@Test
	public void testUpdateSubTask() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "阿斯顿发生发");
		params.put("taskId", "3917743971893253");
		params.put("organizationId", "3915551995068476");
		params.put("users", "12884");
		params.put("startDate", "2015-12-12 00:00");
		params.put("endDate", "2015-12-13 00:00");
		pt.testURI("projectTask/updateSubTask.json", params);
	}
	@Test
	public void testgetMyTaskList() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", "1450235109686");
		params.put("organizationId", "3919937949401093");
		params.put("type", "0");
		params.put("pageSize", "20");
		pt.testURIWithBody("projectTask/getMyTaskList.json", params);
	}
	
	@Test
	public void testgetResourceProject() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", System.currentTimeMillis()+"");
		params.put("organizationId", "3913739569201162");
		params.put("type", "1");
		pt.testURI("/resource/getResourceProject.json", params);
	}
}
