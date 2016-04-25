package com.ginkgocap.tongren.project.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 项目发布
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_project_publish")
public class Publish implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	private Timestamp startDate; //发布起始日
	
	private Timestamp endDate; //发布结束日
	
	private long publisherId; //发布人id
	
	private long projectId; //项目id
	
	private int status;     //0发布失败、1发布成功、2屏蔽发布、3已过期,4项目完成,5项目放弃,6项目延期,7项目过期未完成,8项目进行中
	
	private long organizationId; //组织id
	
	private Timestamp createTime;  //创建时间
	
	private Timestamp updateTime;  //更新时间
	
	private Project project;       //项目
	
	private Set<Apply> applySet;   //申请
	
	private String publisherUserName;//发布人

	@Id
	@GeneratedValue(generator = "PublishId")
	@GenericGenerator(name = "PublishId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "PublishId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="publisher_id")
	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	@Column(name="project_Id")
	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name="start_date")
	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	@Column(name="end_date")
	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Transient
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Transient
	public Set<Apply> getApplySet() {
		return applySet;
	}

	public void setApplySet(Set<Apply> applySet) {
		this.applySet = applySet;
	}

	@Transient
	public String getPublisherUserName() {
		return publisherUserName;
	}

	public void setPublisherUserName(String publisherUserName) {
		this.publisherUserName = publisherUserName;
	}
	
	
}
