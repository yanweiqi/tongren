/*
 * 文件名： MessageControllerTest.java
 * 创建日期： 2015年11月7日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.message.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.ginkgocap.tongren.base.SpringContextTestCase;
	

 /**
 *  消息controller控制器测试
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月7日
 */
public class MessageControllerTest extends SpringContextTestCase<MessageController>{

	private final static String sessionId = "d2ViMzI5MTE0NDkxMDgyMzUxNzY=";
	
	public void testOperateApplicationMessage() {
		String URI = "/operateApplicationMessage.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("status", "1");//1:通过 2:忽悠     
		jsonRequest.put("messageReceiveId", "1");
	
		requestController(sessionId, URI, jsonRequest);
	}
	
	public void testSendInvitationProject(){
		String URI = "/message/sendInvitationProject.json";
		JSONObject  jsonRequest = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(13594);
		jsonRequest.put("uids", jsonArray);
		jsonRequest.put("content", "邀请你承接这个项目");
		jsonRequest.put("projectId", 3912660471250956L);
		jsonRequest.put("organizationId", 0);
		
		requestController(sessionId, URI, jsonRequest);
	}
	@Test
	public void testUserMessage(){
		String URL = "/message/userMessage.json";
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("type", 1);
		
		requestController(sessionId, URL, jsonRequest);
	}
	@Test
	public void userMessageByOrgTest(){
		String URL = "/message/userMessageByOrg.json";
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", "3915551995068476");
		
		requestController(sessionId, URL, jsonRequest);
	}
}
