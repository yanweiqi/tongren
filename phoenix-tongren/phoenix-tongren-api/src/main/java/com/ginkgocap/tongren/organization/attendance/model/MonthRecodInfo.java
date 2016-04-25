package com.ginkgocap.tongren.organization.attendance.model;

import java.io.Serializable;
import java.util.List;

/**
 * 月度考勤信息汇总
 * @author hanxifa
 *
 */
public class MonthRecodInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;
	private String userName;
	private int realWorkDays;//实际出勤天数
	private int comelateDays;//迟到天数
	private int leaveearlierDays;//早退天数
	private int absenceDays;//旷工天数
	private int normalWorkDays;//应出勤天数
	private int month;//月份 1-12 
	//月度每天的考勤详细信息
	private List<DayRecordInfo> recordDetail;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getRealWorkDays() {
		return realWorkDays;
	}
	public void setRealWorkDays(int realWorkDays) {
		this.realWorkDays = realWorkDays;
	}
	public int getComelateDays() {
		return comelateDays;
	}
	public void setComelateDays(int comelateDays) {
		this.comelateDays = comelateDays;
	}
	public int getLeaveearlierDays() {
		return leaveearlierDays;
	}
	public void setLeaveearlierDays(int leaveearlierDays) {
		this.leaveearlierDays = leaveearlierDays;
	}
	public int getAbsenceDays() {
		return absenceDays;
	}
	public void setAbsenceDays(int absenceDays) {
		this.absenceDays = absenceDays;
	}
	public int getNormalWorkDays() {
		return normalWorkDays;
	}
	public void setNormalWorkDays(int normalWorkDays) {
		this.normalWorkDays = normalWorkDays;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public List<DayRecordInfo> getRecordDetail() {
		return recordDetail;
	}
	public void setRecordDetail(List<DayRecordInfo> recordDetail) {
		this.recordDetail = recordDetail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
