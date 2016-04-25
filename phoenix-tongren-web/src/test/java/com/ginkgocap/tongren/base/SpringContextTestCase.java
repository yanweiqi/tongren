package com.ginkgocap.tongren.base;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.ginkgocap.tongren.organization.user.model.GinTongUser;


/**
 * 
 * @author yanweiqi
 * @param <T>
 * @since 2015-10-28 
 * @version 桐人项目V1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"/applicationContext.xml","/spring-mvc.xml"})
public abstract class SpringContextTestCase<T>  extends AbstractJUnit4SpringContextTests{
	
	protected static final Logger logger = LoggerFactory.getLogger(SpringContextTestCase.class);
	
	protected static final String RequestJson = "requestJson";
	protected static final String SessionID   = "sessionId";
	protected static MockHttpServletRequest  request;
	protected static MockHttpServletResponse response;
	
	protected static JSONObject requestJson = new JSONObject();
	
    @Autowired
    protected WebApplicationContext wac;
    
	@Autowired
	protected RequestMappingHandlerAdapter handlerAdapter;
 
    protected MockMvc mockMvc;
	 
    @Autowired
	protected T t ;
	
	@Before
	public void init(){
        request  = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setCharacterEncoding("UTF-8");
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	
	protected GinTongUser getUser(String sessionID){
		GinTongUser user = null;
		String  URL = "/account/user/getUserInfo.json";
		requestJson.put("sessionID", sessionID);
		try {
			//request.setParameter(RequestJson, requestJson.toString());
			//t.getUserBySessionId(request, response);
			MvcResult result = mockMvc.perform(post(URL)
					.param(SessionID, sessionID)
					.param(RequestJson, requestJson.toString())
					.content(requestJson.toString())
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					/*
			        .andExpect(jsonPath("$.exception", nullValue()))
			        .andExpect(jsonPath("$.errors", hasSize(0)))
				    .andExpect((jsonPath("$.ssoToken", notNullValue())))
				    */
				    .andDo(print())
				    .andReturn();
			String content = result.getResponse().getContentAsString();
			JSONObject jsonObject = JSONObject.fromObject(content);
			String code = jsonObject.get("code").toString();
			logger.info(code);
            if(code.equals("000000")){
            	JSONObject responseData = JSONObject.fromObject(jsonObject.get("responseData"));
            	JSONObject jsonUser = JSONObject.fromObject(responseData).getJSONObject("user");
            	user = (GinTongUser) JSONObject.toBean(jsonUser, GinTongUser.class);
            	logger.info(user.getName());
            }
			logger.info(content);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return user;
	}
	
	/**
	 * 请求controller的底层方法
	 * @param sessionId 用户登录sessionId
	 * @param URI 请求的controller的URI
	 * @param requestJson 请求POST封装的参数
	 */
	protected void requestController(String sessionId,String URI,JSONObject requestJson) {
		//getUser(sessionId);		
		try {
			logger.info(requestJson.toString());
			Cookie[] cookies = new  Cookie[]{new Cookie("sessionID", sessionId)};
			MvcResult result = mockMvc.perform(post(URI).cookie(cookies)
					.param(SessionID, sessionId)
					.param(RequestJson, requestJson.toString())
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
				    .andDo(print())
				    .andReturn();
			String content = result.getResponse().getContentAsString();
			JSONObject jsonObject = JSONObject.fromObject(content);
			String code = jsonObject.get("code").toString();
			logger.info(code);
			assertThat("000000",equalTo(code));
            if(code.equals("000000")){
            	JSONObject responseData = JSONObject.fromObject(jsonObject.get("responseData"));
            	logger.info(responseData.toString());
            }
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	} 

}
