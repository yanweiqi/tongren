package com.ginkgocap.tongren.organization.attendance;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;

public class AttendanceRecordsControllerTest {
	
	
	private  String cookie="sessionID=\"d2ViNTE4MjE0NTkzMTkzODc5OTM=\"";
	private  String baseUrl="http://localhost/";
	/**
	 * 增加打卡记录
	 * @throws Exception 
	 */
	@Test
	public void addBegin() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		//params.put("startWorkTime", "2015-12-10 08:36:21");
		params.put("organizationId", "3899738856620077");
		params.put("ipAddrStart", "web");
		//params.put("lonlatStart", "北京市朝阳区大屯街道慧忠里第一社区");
		pt.testURI("attendanceRecords/addBegin.json", params);
	}
	
	@Test
	public void addEnd() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("organizationId", "3899738856620077");
		params.put("lonlatEnd", "北京市朝阳区大屯街道慧忠里第一社区");
		pt.testURIWithBody("attendanceRecords/addEnd.json", params);
	}
	@Test
	public void testGetAttendanceRecordsOfDate() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("organizationId","3899738856620077");
		params.put("date", "2015-12-11 08:36:26");
		pt.testURIWithBody("attendanceRecords/getAttendanceRecordsOfDate.json", params);
	}
	@Test
	public void export() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("month","2015-12");
		params.put("userId", "13583");
		params.put("organizationId", "3921051885240325");
		pt.testURI("attendanceRecords/exportMonth.json", params);
	}
	@Test
	public void getMonthInfo() throws Exception{
		PostUtil pt=new PostUtil(cookie, baseUrl);
		Map<String, String> params=new HashMap<String, String>();
		params.put("month","2016-03");
		params.put("userId", "13583");
		params.put("organizationId", "3921032742437684");
		pt.testURI("attendanceRecords/getMonthInfo.json", params);
	}
}
