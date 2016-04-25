package com.ginkgocap.tongren.resource.web;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.ginkgocap.tongren.base.PostUtil;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.resource.web.ResourceController;

public class ResourceControllerTest {

	
	private String cookie = "sessionID=\"d2ViNzc3NzE0NTkyMjE2NTgyMTg=\"";
	private String baseUrl = "http://localhost/";
	//private String baseUrl = "http://192.168.101.131:6789/";

	@Test
	public void saveFileObjs() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("taskIds", "[\"TVRRMU1qY3pOamszTVRFNU1qQXdNREV5TWpnMk9Ea3g=\",\"TVRRMU1qY3pOamsyT0RFM01UQXdNREV3TkRJeU5UazE=\"]");
		params.put("projectId", "3918815159386117");
		params.put("organizationId", "3915313980899348");
		params.put("organizationTaskId", "0");
		params.put("catalogId", "3931134950113310");
		params.put("tagId", "");
		pt.testURI("file/saveFileObjs.json", params);
	}
	
	@Test
	public void saveFileObjs_v3() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("taskIds", "[\"TVRRMU9URTFOamc1TmpVek1EQXdNREE0TkRrNU9UZ3k=\"]");
		params.put("projectId", "0");
		params.put("organizationId", "3923591674724362");
		params.put("organizationTaskId", "0");
		params.put("catalogId", "");
		params.put("tagId", "");
		params.put("title", "桐人测试文档");
		params.put("relatedInfo", "[{\"relateName\":\"朋友\",\"relatedType\":1,\"detail\":[{\"relatedId\":21,\"relatedType\":1,\"subType\":1},{\"relatedId\":21352,\"relatedType\":1,\"subType\":2}]}]");
		pt.testURI("file/saveFileObjs_v3.json", params);
	}
	
	@Test
	public void updateRelatedInfo() throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("resourceId", "3958067695976453");
		params.put("relatedInfo", "[{\"relateName\":\"同学\",\"relatedType\":1,\"detail\":[{\"relatedId\":21,\"relatedType\":1,\"subType\":1},{\"relatedId\":21352,\"relatedType\":1,\"subType\":2}]}]");
		pt.testURI("file/updateRelatedInfo_v3.json", params);
	}
	@Test
	public void getResourceRelatedV3() throws Exception{
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("resourceId", "3958067695976453");
		pt.testURI("resource/getResourceRelated_v3.json", params);
	}
}
