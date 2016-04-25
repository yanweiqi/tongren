package com.ginkgocap.tongren.organization.attendance.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceRecords;
import com.ginkgocap.tongren.organization.attendance.model.DayRecordInfo;
import com.ginkgocap.tongren.organization.attendance.model.MonthRecodInfo;

/**
 * 考勤记录测试类
 * @author hanxifa
 *
 */
public class AttendanceRecordsServiceTest extends SpringContextTestCase {

	@Autowired
	 private AttendanceRecordsService attendanceRecordsService;
	
	@Test
	public void testStart(){
		AttendanceRecords attendanceRecords=new AttendanceRecords();
		attendanceRecords.setDate(new Timestamp(System.currentTimeMillis()));
		attendanceRecords.setOrganizationId(3899738856620077l);
		attendanceRecords.setStartWorkTime(new Timestamp(System.currentTimeMillis()));
		attendanceRecords.setUserId(13583);
		//attendanceRecords.setIpAddrStart("192.168.0.2");
		attendanceRecords.setLonlatStart("40.0070190000,116.3874820000");
		String status=attendanceRecordsService.start(attendanceRecords);
		System.out.println("status is --> "+status);
	}
	
	@Test
	public void testGetAttendanceRecordsOfDate(){
		AttendanceRecords a=attendanceRecordsService.getAttendanceRecordsOfDate(13594, 3916041902358538l, new Date());
		System.out.println("AttendanceRecords is --> "+JSONArray.toJSONString(a));
	}
	@Test
	public void testEnd(){
		AttendanceRecords attendanceRecords=new AttendanceRecords();
		attendanceRecords.setId(3918796972883974l);
		attendanceRecords.setDate(new Timestamp(System.currentTimeMillis()));
		attendanceRecords.setOrganizationId(3899738856620077l);
		attendanceRecords.setWorkTimeOut(new Timestamp(System.currentTimeMillis()));
		attendanceRecords.setUserId(13583);
		//attendanceRecords.setIpAddrEnd("192.168.0.2");
		attendanceRecords.setLonlatEnd("40.0070190000,116.3874820000");
		String status=attendanceRecordsService.end(attendanceRecords);
		System.out.println("status is --> "+status);
	}
	@Test 
	public void testStartEnd() throws Throwable{
		
		sn("2015-10-16 09:54:00","2015-10-19 17:53:00");
		 
		
		
	}
	private void sn(String start,String ends) throws Exception{
		AttendanceRecords beginRecord=new AttendanceRecords();
		SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp begin=new Timestamp(sf.parse(start).getTime());
		Timestamp end=new Timestamp(sf.parse(ends).getTime());
		
//		beginRecord.setDate(begin);
//		beginRecord.setOrganizationId(3900797213736965l);
//		beginRecord.setStartWorkTime(begin);
//		beginRecord.setUserId(111111);
//		attendanceRecordsService.start(beginRecord);
		
		AttendanceRecords endRecord=new AttendanceRecords();
		endRecord.setId(beginRecord.getId());
		endRecord.setDate(end);
		endRecord.setOrganizationId(3900797213736965l);
		endRecord.setWorkTimeOut(end);
		endRecord.setUserId(111111);
		attendanceRecordsService.end(endRecord);
		
	}
	@Test
	public void testMonthDetail(){
		MonthRecodInfo mri=attendanceRecordsService.getMonthInfo(13594, "2015-12",3899738856620077l);
		System.out.println("缺席天数："+mri.getAbsenceDays());
		System.out.println("迟到天数："+mri.getComelateDays());
		System.out.println("早退天数："+mri.getLeaveearlierDays());
		System.out.println("实际天数："+mri.getRealWorkDays());
		System.out.println("实际天数："+mri.getUserName());
		List<DayRecordInfo> ll=mri.getRecordDetail();
		for(int i=0;i<ll.size();i++){
			DayRecordInfo dr=ll.get(i);
			String str=dr.toString();
			int si=str.indexOf("状态:")+3;
			System.out.println(str.substring(si));
			
		}
		
		System.out.println("MonthRecodInfo is --sss> "+com.alibaba.fastjson.JSON.toJSONString(mri));
	}
}
