package com.ginkgocap.tongren.project.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * 项目承接表
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_project_undertaken")
public class Undertaken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	private long projectId; //承接项目ID
	
	private long recipientId; //承接人
	
	private long recipientOrganizationId; //

	private Timestamp startTime; //承接项目开始时间
	
	private Timestamp endTime; //承接项目结束时间
	
	private int status; //0项目进行中、1完成、2、放弃、3已过期
	
	private long publishId; //发布人ID
	
	private long publishOrganizationId; //发布人所属组织ID
	
	private Publish publish;//项目相关发布和创建

	@Id
	@GeneratedValue(generator = "UndertakenId")
	@GenericGenerator(name = "UndertakenId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "UndertakenId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name="project_id")
	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	@Column(name="recipient_id")
	public long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(long recipientId) {
		this.recipientId = recipientId;
	}
	@Column(name="recipient_organization_id")
	public long getRecipientOrganizationId() {
		return recipientOrganizationId;
	}

	public void setRecipientOrganizationId(long recipientOrganizationId) {
		this.recipientOrganizationId = recipientOrganizationId;
	}
	@Column(name="start_time")
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	@Column(name="end_time")
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="publish_id")
	public long getPublishId() {
		return publishId;
	}

	public void setPublishId(long publishId) {
		this.publishId = publishId;
	}
	@Column(name="publish_organization_id")
	public long getPublishOrganizationId() {
		return publishOrganizationId;
	}

	public void setPublishOrganizationId(long publishOrganizationId) {
		this.publishOrganizationId = publishOrganizationId;
	}
	/**
	 * @return 返回 publish。
	 */
	@Transient
	public Publish getPublish() {
		return publish;
	}

	/**
	 * ---@param publish 要设置的 publish。
	 */
	public void setPublish(Publish publish) {
		this.publish = publish;
	}

	
}
