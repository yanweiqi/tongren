/**
 * 
 */
package com.ginkgocap.tongren.organization.attendance.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 组织考勤记录
 * @author liny
 *
 */
@Entity 
@Table(name="tb_organization_attendance_records")
public class AttendanceRecords extends BasicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long attendanceSystemId;//考勤制度ID
	
	private long userId;//用户ID
	
	private Timestamp date;//打卡日期
	
	private Timestamp startWorkTime;//上班时间
	
	private Timestamp workTimeOut;//下班时间
	
	private long organizationId;//组织ID
	
	private Timestamp createTime;//记录创建时间
	
	private Timestamp updateTime;//记录修改时间
	
	private String ipAddrStart;//打卡时ip地址（web端打卡）签到
	
	private String ipAddrEnd;//打卡时ip地址（web端打卡）签退
	
	private String  lonlatStart;//app打卡的经纬度（签到）
	
	private String  lonlatEnd;//app打卡的经纬度（签退）

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "AttendanceRecordsId")
	@GenericGenerator(name = "AttendanceRecordsId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "AttendanceRecordsId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the attendanceSystemId
	 */
	@Column(name = "attendance_system_id")
	public long getAttendanceSystemId() {
		return attendanceSystemId;
	}

	/**
	 * @param attendanceSystemId the attendanceSystemId to set
	 */
	public void setAttendanceSystemId(long attendanceSystemId) {
		this.attendanceSystemId = attendanceSystemId;
	}

	/**
	 * @return the userId
	 */
	@Column(name = "user_id")
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the date
	 */
	@Column(name = "date")
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the startWorkTime
	 */
	@Column(name = "start_work_time")
	public Timestamp getStartWorkTime() {
		return startWorkTime;
	}

	/**
	 * @param startWorkTime the startWorkTime to set
	 */
	public void setStartWorkTime(Timestamp startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	/**
	 * @return the workTimeOut
	 */
	@Column(name = "work_time_out")
	public Timestamp getWorkTimeOut() {
		return workTimeOut;
	}

	/**
	 * @param workTimeOut the workTimeOut to set
	 */
	public void setWorkTimeOut(Timestamp workTimeOut) {
		this.workTimeOut = workTimeOut;
	}

	/**
	 * @return the organizationId
	 */
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "ip_addr_start")
	public String getIpAddrStart() {
		return ipAddrStart;
	}

	public void setIpAddrStart(String ipAddrStart) {
		this.ipAddrStart = ipAddrStart;
	}

	@Column(name = "ip_addr_end")
	public String getIpAddrEnd() {
		return ipAddrEnd;
	}

	public void setIpAddrEnd(String ipAddrEnd) {
		this.ipAddrEnd = ipAddrEnd;
	}

	@Column(name = "lonlat_start")
	public String getLonlatStart() {
		return lonlatStart;
	}

	public void setLonlatStart(String lonlatStart) {
		this.lonlatStart = lonlatStart;
	}

	@Column(name = "lonlat_end")
	public String getLonlatEnd() {
		return lonlatEnd;
	}

	public void setLonlatEnd(String lonlatEnd) {
		this.lonlatEnd = lonlatEnd;
	}

	 
}
