/*
 * 文件名： MessageProcessingControllerTest.java
 * 创建日期： 2015年11月17日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.message.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月17日
 */
public class MessageProcessingControllerTest extends SpringContextTestCase<MessageProcessingController> {

	private final static String sessionId = "d2ViOTQ4MDE0NTA2NzgyNTU4NzY=";
	
	@Test
	public void testHandle() {
		String URI = "/message/processing/handle.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("status", "1");//1:通过 2:忽悠     
		jsonRequest.put("messageReceiveId", "3922490556678214");
	
		requestController(sessionId, URI, jsonRequest);
	}
}
