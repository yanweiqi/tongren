package com.ginkgocap.tongren.project.manage.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class ApplyControllerTest extends SpringContextTestCase<PublishController> {
	
	private static final String sessionID = "d2ViNDExNzE0NDk1NDYyNDEyNDA=";
	private static final String projectId = "3908371191431178";
	
	/**
	 * 创建申请
	 */
	@Test
	public void testCreate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String applyTime  = sdf.format(date).toString();
		String publisherId = "128036";
		String URI = "/project/manage/apply.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		jsonRequest.put("publisherId", publisherId);
		jsonRequest.put("applyTime", applyTime);	
		requestController(sessionID, URI, jsonRequest);
	}

	/**
	 * 接受申请
	 */
	@Test
	public void testAccep() {
		String proposerId = "128036";
		String URI = "/project/manage/accept.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		jsonRequest.put("proposerId", proposerId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	/**
	 * 拒绝申请
	 */
	@Test
	public void testRefuse() {
		String proposerId = "128036";
		String URI = "/project/manage/refuse.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		jsonRequest.put("proposerId", proposerId);
		requestController(sessionID, URI, jsonRequest);
	}
	
	/**
	 * 我申请的项目列表
	 */
	@Test
	public void testGetMyApplies(){
		String applyStatus = "1";
		String URI = "/project/manage/getMyApplies.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("applyStatus", applyStatus);
		requestController(sessionID, URI, jsonRequest);
	}
	
	//getMyProjectApply.json
	/**
	 * 根据项目ID查询申请列表
	 */
	@Test
	public void getMyProjectApplys(){
		String projectId = "3915660568821805";
		String URI = "/project/manage/getMyProjectApply.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("projectId", projectId);
		requestController(sessionID, URI, jsonRequest);
	}
}
