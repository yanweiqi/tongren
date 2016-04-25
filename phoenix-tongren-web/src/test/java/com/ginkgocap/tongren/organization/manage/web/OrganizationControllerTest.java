package com.ginkgocap.tongren.organization.manage.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.manage.model.Organization;

public class OrganizationControllerTest  extends SpringContextTestCase<OrganizationRoleController> {
	
	private static final String sessionID = "d2ViNTIwMjE0NTMyMTkyMjY3MTQ=";
	

	@Test
	public void testCreateOrganization() {
		getUser(sessionID);
		String  URL = "/organization/create";
		Organization vo = new Organization();
		
		vo.setName("嘻嘻嘻嘻嘻嘻");
		vo.setIntroduction("啊啊啊啊啊啊a");
		vo.setClassification(8);
		vo.setIndustry(33);
		vo.setLogo("800002");
		vo.setArea(22);

		requestJson.put("name", vo.getName());
		requestJson.put("introduction", vo.getIntroduction());
		requestJson.put("classification", vo.getClassification());
		requestJson.put("industry", vo.getIndustry());
		requestJson.put("taskId", vo.getLogo());
		requestJson.put("area", vo.getArea());
		
		try {
			logger.info(requestJson.toString());
			Cookie[] cookies = new  Cookie[]{new Cookie("sessionID", sessionID)};
			MvcResult result = mockMvc.perform(post(URL).cookie(cookies)
					.param(SessionID, sessionID)
					.param(RequestJson, requestJson.toString())
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
				    .andDo(print())
				    .andReturn();
			String content = result.getResponse().getContentAsString();
			JSONObject jsonObject = JSONObject.fromObject(content);
			String code = jsonObject.get("code").toString();
			logger.info(code);
            if(code.equals("000000")){
            	JSONObject responseData = JSONObject.fromObject(jsonObject.get("responseData"));
            	logger.info(responseData.toString());
            }
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Test
	public void testGetOrganizationSumup(){
		String organizationId = "3910550153330689";
		String URI = "/organization/getOrganizationSumup.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("oid", organizationId);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void testQueryOrganizationPage(){
		int status = 0;
		int index = 0;
		int size = 4;
		String URI = "/organization/queryOrganizationPage.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("status", status);
		jsonRequest.put("index", index);
		jsonRequest.put("size", size);
		requestController(sessionID, URI, jsonRequest);
	}
	@Test
	public void countOrganizationSumup(){
		String URI = "/organization/countOrganizationSumup.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("organizationId", 3915313980899348L);
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void getIndexContent(){
		String URI = "/organization/getIndexContent.json";
		JSONObject  jsonRequest = new JSONObject();
		requestController(sessionID, URI, jsonRequest);
	}
	
	@Test
	public void testSearch(){
		String URI = "/organization/testSearch.json";
		JSONObject  jsonRequest = new JSONObject();
		jsonRequest.put("name", "吴");
		requestController(sessionID, URI, jsonRequest);
	}
}

