package com.ginkgocap.tongren.organization.attendance;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;

public class AttendanceSystemController {

	private  String cookie="sessionID=\"d2ViNTE4MjE0NTkzMTkzODc5OTM=\"";
	private  String baseUrl="http://localhost/";
	
	/**
	 * 增加组织考勤
	 * @throws Exception 
	 */
	@Test
	public void add() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("name", "秘泽华上班时间秘泽华上班时间秘泽华上班时ss");
		params.put("startWorkTime", "09:00");
		params.put("workTimeOut", "18:00");
		params.put("organizationId", "3921032742437684");
		params.put("elasticityMinutes", "20");
		params.put("description", "秘泽华上班时间");
		pt.testURI("attendanceSystem/add.json", params);
	}
	
	/**
	 * 增加组织考勤
	 * @throws Exception 
	 */
	@Test
	public void modify() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("name", "秘泽华上班时间秘泽华上班时间秘泽华上班时ss");
		params.put("startWorkTime", "09:31");
		params.put("workTimeOut", "18:30");
		params.put("organizationId", "3921032742437684");
		params.put("elasticityMinutes", "31");
		params.put("description", "桐人考勤，测试");
		pt.testURI("attendanceSystem/modify.json", params);
	}
	@Test
	public void get() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("organizationId", "3918889289515018");
		pt.testURIWithBody("attendanceSystem/get.json", params);
	} 
}
