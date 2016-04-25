package com.ginkgocap.tongren.organization.attendance.service;

import java.util.Date;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceRecords;
import com.ginkgocap.tongren.organization.attendance.model.MonthRecodInfo;

/**
 * 考勤记录服务接口
 * @author hanxifa
 *
 */
public interface AttendanceRecordsService  extends CommonService<AttendanceRecords>{

	/**
	 * 上班打卡
	 * @param attendanceRecords
	 * @return 1 增加成功 2 增加失败
	 */
	public String start(AttendanceRecords attendanceRecords);
	
	
	/**
	 * 下班
	 * @param attendanceRecords
	 * @return 1 增加成功 2 增加失败
	 */
	public String end(AttendanceRecords attendanceRecords);
	
	
	/**
	 * 获取某个月份的出勤信息汇总
	 * @param userId
	 * @return
	 */
	public MonthRecodInfo getMonthInfo(long userId,String month,long orgId);
	
	/**
	 * 获取某个用户在某个组织下的打卡记录
	 * @param userid
	 * @param orgId
	 * @param date
	 * @return
	 */
	public AttendanceRecords getAttendanceRecordsOfDate(long userid,long orgId,Date date);
	 
	 
}
