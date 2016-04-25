package com.ginkgocap.tongren.common.web;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class FunctionControllerTest extends SpringContextTestCase<FunctionController> {

	private final static String sessionId = "d2ViNjUwODE0NDY1MjA1MTQ0NTk=";
	
	@Test
	public void testGetAllRelations() {
		
		String URI = "/function/getAllRelations";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("pageno", "1");
		jsonRequest.put("size", "8");
		requestController(sessionId, URI, jsonRequest);
	}

}
