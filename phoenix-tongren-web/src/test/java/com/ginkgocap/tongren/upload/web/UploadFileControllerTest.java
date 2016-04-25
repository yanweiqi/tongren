package com.ginkgocap.tongren.upload.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.json.JSONObject;

import com.ginkgocap.tongren.base.PostUtil;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.resource.web.ResourceController;

public class UploadFileControllerTest {

	private String cookie = "sessionID=\"d2ViODAwOTE0NTkzOTQxNDcyNDc=\"";
	private String baseUrl = "http://localhost/";
	//private String baseUrl = "http://192.168.101.131:6789/";

	@Test
	public void getMyOrgResourcePage() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("index", "1");
		params.put("lstr", "产品");
		params.put("organizationId", "3908705372602414");
		pt.testURI("resource/getMyOrgResourcePage.json", params);
	}
	
	@Test
	public void getOrgResourcePage() throws Exception {
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("index", "1");
		params.put("lstr", "加班");
		params.put("organizationId", "3923591674724362");
		pt.testURI("resource/getOrgResourcePage.json", params);
	}
	
	@Test
	public void getFileInfo() throws Exception{
		String url="file/getFileInfo.json";
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "1");
		params.put("resourceId", "3958067695976453");
		pt.testURI("file/getFileInfo.json", params);
	}
}
