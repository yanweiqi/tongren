package com.ginkgocap.tongren.project.task.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.geronimo.mail.util.Base64Encoder;
import org.jboss.netty.handler.codec.base64.Base64Decoder;
import org.junit.Test;
import sun.misc.BASE64Encoder; 
import com.ginkgocap.tongren.base.PostUtil;

public class CommonTestController {

	private String cookie = "sessionID=\"d2ViMTI3OTE0NjEyMDM4NjM4Nzk=\"";
	//private String baseUrl = "http://localhost/";
	//private String baseUrl = "http://192.168.101.131:6789/";
	private String baseUrl = "http://test.online.gintong.com/";
	//private String baseUrl = "http://192.168.101.53:4448/";
	
	@Test
	public void testgetFileInfo(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("resourceId", "3967069586718725");
		params.put("type", "1");
		//params.put("cycle", "11");
		//params.put("content", "test_11");
		try {
			pt.testURI("/file/getFileInfo.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testshareKnowledge(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", "3961210966114344");
		params.put("knowledgeId", "2971411");
		params.put("knowledgeType", "1");
		//params.put("cycle", "11");
		//params.put("content", "test_11");
		try {
			pt.testURI("crossgtadmin/organizationResource/shareKnowledge.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testgetUndertakenProjectInfo(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("projectId", "3916723397066757");
		try {
			pt.testURI("project/undertaken/getUndertakenProjectInfo.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testDelProject(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("projectId", "3915320469487636");
		try {
			pt.testURI("/project/manage/del.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAllpublish(){
		
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("applyStatus", "");
		try {
			long t1=System.currentTimeMillis();
			pt.testURI("project/manage/getAllPublishValidity.json", params);
			System.out.println("time is "+(System.currentTimeMillis()-t1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testLoginWithMobile(){
		String url="http://test.online.gintong.com/cross/";//仿真环境
		//String url="http://192.168.120.234:3322/";//dev环境
		PostUtil pt = new PostUtil(cookie, url);
		Map<String, String> params = new HashMap<String, String>();
		BASE64Encoder base=new BASE64Encoder();
		params.put("platform", "");
		params.put("clientPassword", "");
		params.put("model", "");
		params.put("loginString", "18664900944");
		params.put("systemVersion", "19");
		params.put("channelID", "");
		params.put("systemName", "android");
		params.put("imei", "864601028367027");
		params.put("clientID", "");
		params.put("password", base.encode("121212".getBytes()));
		params.put("resolution", "");
		params.put("version", "0.93.02beta");
		try {
			pt.testURIWithBody("login/loginConfiguration.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testAddBegin(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", "3916041902358538");
		params.put("startWorkTime", "2015-12-09 17:07:40");
		params.put("date", "2015-12-09 09:00:00");
		try {
			pt.testURI("/attendanceRecords/addBegin.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//organization/getOrgTaskList.json
	@Test
	public void testGetOrgTaskList(){
		String  URI = "organization/getOrgTaskList.json";
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", System.currentTimeMillis()+"");
		params.put("orgId", "3921340050702376");
		params.put("type", "-1");
		try {
			pt.testURIWithBody(URI,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testgetAllPublishValidity(){
		String  URI = "project/manage/getAllPublishValidity.json";
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("applyStatus", "");
		try {
			pt.testURI(URI,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testGetMyApplyList(){
		String uri="reviewApplication/getMyApplyFor.json";
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", "3915313980899348");
		params.put("type", "1");
		params.put("index", "1");
		params.put("size", "20");
		try {
			pt.testURI(uri, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testQueryOrganzationOrFriendPage(){
		String  URI = "organization/queryOrganzationOrFriendPage.json";
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "c");
		params.put("index", "1");
		try {
			pt.testURI(URI,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testsetUserRole(){
		String  URI = "manage/role/setUserRole.json";
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("organizationId", "3915551995068476");
		params.put("roleId", "3915551995068706");
		params.put("organizationMemberId", "3915551995068691");
		try {
			pt.testURI(URI,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	public static void main(String[] args) {
		System.out.println(new BASE64Encoder().encode("837669han".getBytes()));
	}
}
