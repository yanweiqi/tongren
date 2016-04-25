package com.ginkgocap.tongren.organization.attendance.service;

import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceSystem;

public class AttendanceSystemServiceTest extends SpringContextTestCase {

	@Autowired
	AttendanceSystemService attendanceSystemService;

	@Test
	public void testAdd() {
		AttendanceSystem attendanceSystem = new AttendanceSystem();
		attendanceSystem.setCreateId(111111);
		attendanceSystem
				.setCreateTime(new Timestamp(System.currentTimeMillis()));
		attendanceSystem.setDescription("默认考勤");
		attendanceSystem.setElasticityMinutes(30);// 弹性时间默认为分钟
		attendanceSystem.setName("默认考勤");
		attendanceSystem.setStartWorkTime("09:00");//设置上班时间
		attendanceSystem.setWorkTimeOut("18:00");//设置下班时间
		attendanceSystem.setOrganizationId(3900797213736965l);
		attendanceSystem.setUpdateTime(attendanceSystem.getCreateTime());
		String status=attendanceSystemService.add(attendanceSystem);
		System.out.println(status);
	}
	
//	@Test
//	public void testModify() {
//		AttendanceSystem attendanceSystem = new AttendanceSystem();
//		attendanceSystem.setCreateId(111111);
//		attendanceSystem
//				.setCreateTime(new Timestamp(System.currentTimeMillis()));
//		attendanceSystem.setDescription("修改考勤配置");
//		attendanceSystem.setElasticityMinutes(30);// 弹性时间默认为分钟
//		attendanceSystem.setName("修改考勤配置");
//		attendanceSystem.setStartWorkTime(9);//设置上班时间
//		attendanceSystem.setWorkTimeOut(18);//设置下班时间
//		attendanceSystem.setOrganizationId(3900797213736965l);
//		attendanceSystem.setUpdateTime(attendanceSystem.getCreateTime());
//		attendanceSystemService.modify(attendanceSystem);
//		 
//	}
	
	@Test
	public void testGetCurrentAttendance(){
		
		AttendanceSystem a=attendanceSystemService.getCurretAttendanceSystem(3900797213736965l);
		System.out.println("getDescription--> "+a.getDescription());

		 a=attendanceSystemService.getCurretAttendanceSystem(3900797213736965l);
		System.out.println("getDescription--> "+a.getDescription());
		
		 a=attendanceSystemService.getCurretAttendanceSystem(3900797213736965l);
		System.out.println("getDescription--> "+a.getDescription());
	}
}
