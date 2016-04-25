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

/**
 * 
 * 组织考勤制度
 * @author liny
 *
 */
@Entity 
@Table(name="tb_organization_attendance_system")
public class AttendanceSystem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;//考勤名称
	
	private String startWorkTime;//上班时间 格式为 09:00
	
	private String workTimeOut;//下班时间  格式为 18:00
	
	private int weekWorkingDays;//周工作制
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updateTime;//更新时间
	
	private long createId;//创建人ID
	
	private long organizationId;//组织ID
	
	private int workingHours;//日工作制
	
	private int elasticityMinutes;//弹性时间
	
	private String description;//描述

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "AttendanceSystemId")
	@GenericGenerator(name = "AttendanceSystemId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "AttendanceSystemId") })
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
	 * @return the name
	 */
	@Column(name="name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startWorkTime
	 */
	@Column(name="start_work_time")
	public String getStartWorkTime() {
		return startWorkTime;
	}

	/**
	 * @param startWorkTime the startWorkTime to set
	 */
	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	/**
	 * @return the workTimeOut
	 */
	@Column(name="work_time_out")
	public String getWorkTimeOut() {
		return workTimeOut;
	}

	/**
	 * @param workTimeOut the workTimeOut to set
	 */
	public void setWorkTimeOut(String workTimeOut) {
		this.workTimeOut = workTimeOut;
	}

	/**
	 * @return the weekWorkingDays
	 */
	@Column(name="week_working_days")
	public int getWeekWorkingDays() {
		return weekWorkingDays;
	}

	/**
	 * @param weekWorkingDays the weekWorkingDays to set
	 */
	public void setWeekWorkingDays(int weekWorkingDays) {
		this.weekWorkingDays = weekWorkingDays;
	}

	/**
	 * @return the createTime
	 */
	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the createId
	 */
	@Column(name="create_id")
	public long getCreateId() {
		return createId;
	}

	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(long createId) {
		this.createId = createId;
	}

	/**
	 * @return the organizationId
	 */
	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the workingHours
	 */
	@Column(name="working_hours")
	public int getWorkingHours() {
		return workingHours;
	}

	/**
	 * @param workingHours the workingHours to set
	 */
	public void setWorkingHours(int workingHours) {
		this.workingHours = workingHours;
	}

	/**
	 * @return the elasticityMinutes
	 */
	@Column(name="elasticity_minutes")
	public int getElasticityMinutes() {
		return elasticityMinutes;
	}

	/**
	 * @param elasticityMinutes the elasticityMinutes to set
	 */
	public void setElasticityMinutes(int elasticityMinutes) {
		this.elasticityMinutes = elasticityMinutes;
	}

	/**
	 * @return the description
	 */
	@Column(name="description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
