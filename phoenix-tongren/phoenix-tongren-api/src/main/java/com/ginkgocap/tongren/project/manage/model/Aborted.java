package com.ginkgocap.tongren.project.manage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *  项目失败表
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_project_aborted")
public class Aborted implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long id; //主键
	
	private long projectId; //项目Id
	
	private long projectUndertakenId; //项目承接Id
	
	private String reason; //失败原因
	
	private long recipientId; //承接人ID
	
	private long undertakenOrganizationId;//承接组织ID
	
	private long operationId; //操作人ID
	
	private long  createOrganizationId;//项目创建组织ID
	@Id
	@GeneratedValue(generator = "AbortedId")
	@GenericGenerator(name = "AbortedId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "AbortedId") })
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
	@Column(name = "project_undertaken_id")
	public long getProjectUndertakenId() {
		return projectUndertakenId;
	}

	public void setProjectUndertakenId(long projectUndertakenId) {
		this.projectUndertakenId = projectUndertakenId;
	}
	@Column(name = "reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	@Column(name = "recipient_id")
	public long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(long recipientId) {
		this.recipientId = recipientId;
	}
	@Column(name = "undertaken_organization_id")
	public long getUndertakenOrganizationId() {
		return undertakenOrganizationId;
	}

	public void setUndertakenOrganizationId(long undertakenOrganizationId) {
		this.undertakenOrganizationId = undertakenOrganizationId;
	}
	@Column(name = "operation_id")
	public long getOperationId() {
		return operationId;
	}

	public void setOperationId(long operationId) {
		this.operationId = operationId;
	}
	@Column(name = "create_organization_id")
	public long getCreateOrganizationId() {
		return createOrganizationId;
	}

	public void setCreateOrganizationId(long createOrganizationId) {
		this.createOrganizationId = createOrganizationId;
	}
	
}
