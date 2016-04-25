package com.ginkgocap.tongren.organization.manage.model;

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
 * 组织知识表
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_organization_knowledge")
public class OrganizationKd extends BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long userId;//共享知识userId
	
	private String title;
	
	private long releaseId;//发布人id
	
	private String releaseName;//发布人名称
	
	private Timestamp releaseTime;//发布时间
	
	private String context;//内容
	
	private String taskId;
	
	private long organizationId;
	
	private Timestamp createTime;
	
	private long knowledgeId; //知识Id

	@Id
	@GeneratedValue(generator = "OrganizationKdId")
	@GenericGenerator(name = "OrganizationKdId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationKdId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "user_id")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "release_id")
	public long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(long releaseId) {
		this.releaseId = releaseId;
	}
	@Column(name = "release_name")
	public String getReleaseName() {
		return releaseName;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	@Column(name = "release_time")
	public Timestamp getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
	}
	@Column(name = "content")
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	@Column(name = "task_id")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

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

	@Column(name = "knowledge_id")
	public long getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
}
