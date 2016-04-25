package com.ginkgocap.tongren.organization.task.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.user.web.UserController;

/**
 * 
 * @author liweichao
 *
 */
public class OrganizationTaskControllerTest extends SpringContextTestCase<UserController> {

	private static final String sessionID = "d2ViNTYxMzE0NTM2NTE5NTkyMTE=";  
	
//	private String cookie = "sessionID=\"d2ViODExMTQ1MDA2NDYxNjc1Mw==\"";
	//private String baseUrl = "http://localhost/";
	private String baseUrl = "http://192.168.101.131:6789/";
	
	/**
	 * 创建组织任务
	 */
	@Test
	public void testCreateApplication() {
		String  URI = "/organizationTask/create.json";
		requestJson.put("title", "李伟超测试组织任务");
		requestJson.put("startTime", "2015-12-02");
		requestJson.put("endTime", "2016-01-01");
		requestJson.put("taskDescription", "请勿删除");
		requestJson.put("attachId", "TVRRME56YzBNVEUyTVRNMU9UQXdNREUwT1RZek1USTE=");
		requestJson.put("performerId", "11");
		requestJson.put("organizationId", "3910172804382729");
		requestController(sessionID, URI, requestJson);
	}
	/**
	 * 退回组织任务
	 */
	@Test
	public void testReject(){
		
		String  URI = "/organizationTask/reject.json";
		requestJson.put("organizationTaskId", "3910564246192130");
		requestJson.put("text", "这个需求有问题 做不了");
		requestController(sessionID, URI, requestJson);
	}
	
	/**
	 *重新分配组织任务 
	 *
	 */
	
	@Test
	public void testAssignOrganizationTask(){
		
		String  URI = "/organizationTask/assignOrganizationTask.json";
		requestJson.put("organizationTaskId", "3910564246192130");
		requestJson.put("performerId", "123");
		requestJson.put("organizationId", "3910172804382729");
		requestController(sessionID, URI, requestJson);
	}
	/**
	 *查看组织任务操作 
	 *
	 */
	
	@Test
	public void testGetOrganizationTaskOperation(){
		String  URI = "/organizationTask/getOrganizationTaskOperation.json";
		requestJson.put("organizationTaskId", "3910564246192130");
		requestController(sessionID, URI, requestJson);
	}
	/**
	 *删除组织任务操作 
	 *
	 */
	
	@Test
	public void testDelOrganizationTaskOperation(){
		String  URI = "/projectTask/removeTask.json";
		requestJson.put("taskId", "3910564246192130");
		requestController(sessionID, URI, requestJson);
	}
	
	@Test
	public void testGetOrgTaskList(){
		String  URI = "organization/getOrgTaskList.json";
		PostUtil pt = new PostUtil(sessionID, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", System.currentTimeMillis()+"");
		params.put("orgId", "3918850202796037");
		params.put("type", "-1");
		try {
			pt.testURI(URI,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
