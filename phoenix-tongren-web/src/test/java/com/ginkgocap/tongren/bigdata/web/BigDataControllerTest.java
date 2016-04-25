/*
 * 文件名： BigDataControllerTest.java
 * 创建日期： 2015年12月7日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.bigdata.web;

import net.sf.json.JSONObject;

import org.junit.Test;



import com.ginkgocap.tongren.base.SpringContextTestCase;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年12月7日
 */
public class BigDataControllerTest  extends SpringContextTestCase<BigDataController>{

	private final static String sessionId = "d2ViOTg1MTQ0OTQ3ODc3NzAyNg==";
	
	@Test
	public void testOperateApplicationMessage() {
		String URI = "/data/getRecommendedUser.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("pageNo", "1");
		jsonRequest.put("pageSize", "8");
		requestController(sessionId, URI, jsonRequest);
	}
	
}
