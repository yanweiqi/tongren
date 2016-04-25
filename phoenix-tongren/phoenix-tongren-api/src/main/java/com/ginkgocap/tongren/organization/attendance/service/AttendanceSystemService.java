package com.ginkgocap.tongren.organization.attendance.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceSystem;

/***
 * 	组织考勤业务处理接口
 * @author hanxifa
 *
 */
public interface AttendanceSystemService extends CommonService<AttendanceSystem>{

	/**
	 * 增加组织考勤制度
	 * @param attendanceSystem
	 */
	 public String add(AttendanceSystem attendanceSystem);
	 
	 
	 /**
	 * 修改考勤记录
	 * @param attendanceSystem
	 */
	 public String modify(AttendanceSystem attendanceSystem);
	 
	 /**
	  * 根据组织id获取该组织的考勤设置
	  * @param orgId
	  * @return
	  */
	 public List<AttendanceSystem> getByOrgId(long orgId);
	
	 /**
	  * 根据id获取考勤设置
	  * @param id
	  * @return
	  */
	 public AttendanceSystem getById(long id);
	 
	 /**
	  * 根据组织id获取当前组织的考勤模板
	  * 考勤表中有多条记录，设置靠前模板后第二天生效，因此取前一天之前的创建时间最近的记录，如果所有记录创建时间都大于等于当天，则去默认考勤设置，即创建时间最小的记录
	  * @param orgId
	  * @return
	  */
	 public AttendanceSystem getCurretAttendanceSystem(long orgId);
	 
}
