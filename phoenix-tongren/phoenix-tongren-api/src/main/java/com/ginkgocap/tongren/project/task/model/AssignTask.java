package com.ginkgocap.tongren.project.task.model;

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
 * 项目任务分配表
 * @author Administrator
 *
 */

@Entity
@Table(name="tb_project_assign_task")
public class AssignTask extends BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private long id;//主键
	
	private long projectTaskId;//项目任务id
	
	private long assignerId;//分配人id
	
	private Timestamp assignTime;//分配时间
	
	private long performerId;//执行者id
	
	private long organizationId;//此任务所属组织的id
	
	private int status;//0 有效 1 无效
	
	private String remark;//备注
	
	@Id
	@GeneratedValue(generator = "AssignTaskID")
	@GenericGenerator(name = "AssignTaskID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "AssignTaskID") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="project_task_id")
	public long getProjectTaskId() {
		return projectTaskId;
	}

	public void setProjectTaskId(long projectTaskId) {
		this.projectTaskId = projectTaskId;
	}
	
	@Column(name="assigner_id")
	public long getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(long assignerId) {
		this.assignerId = assignerId;
	}

	@Column(name="assign_time")
	public Timestamp getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(Timestamp assignTime) {
		this.assignTime = assignTime;
	}

	@Column(name="performer_id")
	public long getPerformerId() {
		return performerId;
	}

	public void setPerformerId(long performerId) {
		this.performerId = performerId;
	}

	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "AssignTask [id=" + id + ", projectTaskId=" + projectTaskId + ", assignerId=" + assignerId + ", assignTime=" + assignTime
				+ ", performerId=" + performerId + ", organizationId=" + organizationId + "]";
	}

}
