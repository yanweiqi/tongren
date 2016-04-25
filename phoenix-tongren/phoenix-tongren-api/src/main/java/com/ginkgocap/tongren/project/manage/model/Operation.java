package com.ginkgocap.tongren.project.manage.model;

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
 * 项目操作类
 * @author  o
 *
 */
@Entity
@Table(name="tb_project_operation")
public class Operation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id; 
	
	private long projectId; //项目id
	
	private long operationUid; //操作人id
	
	private Timestamp operactionTime; //操作时间
	
	private String operationCode; //操作类型
	
	private String remark; //备注 如果操作是上传的话存的是 taskId
	
	private long organizationTaskId;//组织任务id
	
	@Id
	@GeneratedValue(generator = "OperationId")
	@GenericGenerator(name = "OperationId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OperationId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "project_id")
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	@Column(name = "operation_uid")
	public long getOperationUid() {
		return operationUid;
	}

	public void setOperationUid(long operationUid) {
		this.operationUid = operationUid;
	}
	@Column(name = "operation_time")
	public Timestamp getOperactionTime() {
		return operactionTime;
	}

	public void setOperactionTime(Timestamp operactionTime) {
		this.operactionTime = operactionTime;
	}
	@Column(name = "operation_code")
	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "organization_task_Id")
	public long getOrganizationTaskId() {
		return organizationTaskId;
	}

	public void setOrganizationTaskId(long organizationTaskId) {
		this.organizationTaskId = organizationTaskId;
	}
}
