package com.ginkgocap.tongren.organization.attendance.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ginkgocap.tongren.common.model.BasicBean;



/**
 * 记录某一天考勤信息
 * @author hanxifa
 *
 */
public class DayRecordInfo extends BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long attendanceSystemId;
	private Date workDay;//考勤时间
	private int status;//考勤状态 1 正常 2异常  3 缺卡(上下班均未打卡) 4 加班 （本来不应该打卡，但是存在打卡记录）
	private int startStatus;//上班考勤状态 1 正常 2 迟到 3 上班未打卡
	private int endStatus;//下班考勤状态 1 正常 2 早退3 下班未打卡
	private boolean isWarkDay;//当前日期是否需要打卡，目前规定周六日不需要打卡 
	private Date start;//开始打卡时间
	private Date end;//结束打卡时间
	public long getAttendanceSystemId() {
		return attendanceSystemId;
	}
	public void setAttendanceSystemId(long attendanceSystemId) {
		this.attendanceSystemId = attendanceSystemId;
	}
	public Date getWorkDay() {
		return workDay;
	}
	public void setWorkDay(Date workDay) {
		this.workDay = workDay;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean getIsWarkDay() {
		return isWarkDay;
	}
	public void setIsWarkDay(boolean isWarkDay) {
		this.isWarkDay = isWarkDay;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public int getStartStatus() {
		return startStatus;
	}
	public void setStartStatus(int startStatus) {
		this.startStatus = startStatus;
	}
	public int getEndStatus() {
		return endStatus;
	}
	public void setEndStatus(int endStatus) {
		this.endStatus = endStatus;
	}
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2=new SimpleDateFormat("HH:mm");
		sb.append(sf.format(workDay)+",是否需要打卡:"+isWarkDay);
		String str="";
		if(status==1){
			if(isWarkDay){
				str="正常("+sf2.format(start)+"-"+sf2.format(end)+")";
			}else{
				str="正常(节假日)";
			}
		}else if(status==2){
			if(startStatus==1){
				str=sf2.format(start);
			}else if(startStatus==2){
				str="迟到"+sf2.format(start);
			}else if(startStatus==3){
				str="上班缺卡";
			}
			str+="-";
			if(endStatus==1){
				str+=sf2.format(end);
			}else if(endStatus==2){
				str+="早退"+sf2.format(end);
			}else if(endStatus==3){
				str+="下班缺卡";
			}
		}else if(status==3){
			str="全天缺卡";
		}else if(status==4){
			str="加班("+(start==null?"缺卡":sf2.format(start))+"-"+(end==null?"缺卡":sf2.format(end))+")";
		}else{
			str="状态非法("+status+")";
		}
		sb.append(",状态:"+str);
		return sb.toString();
	}
	
}
