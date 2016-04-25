package com.ginkgocap.tongren.project.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 项目创建
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_project")
public class Project implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	private String name;//项目名称

	private String introduction;//项目介绍
	
	private Timestamp validityStartTime;//有效开始时间
	
	private Timestamp validityEndTime;//有效结束时间     
	
	private int cycle;//项目周期,单位天
	
	private String area;//地区
	
	private String industry;//行业
	
	private String document; //文档
	
	private long createrId;//创建人id
	
	private Timestamp createTime;//创建日期
	
	private Timestamp updateTime;//更新时间
	
	private long organizationId; //创建者所属组织
	
	private double remuneration;//项目酬劳
	
	private int status; //状态 0草稿、1正式
	
	private List<ResourceAttachment> resourceAttachments;
	
	@Id
	@GeneratedValue(generator = "ProjectId")
	@GenericGenerator(name = "ProjectId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ProjectId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="introduction")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Column(name="cycle")
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	@Column(name="area")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	@Column(name="industry")
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
	
	@Transient
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="create_id")
	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	@Column(name="remuneration")
	public double getRemuneration() {
		return remuneration;
	}

	public void setRemuneration(double remuneration) {
		this.remuneration = remuneration;
	}
	
	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name="validity_start_time")
	public Timestamp getValidityStartTime() {
		return validityStartTime;
	}

	public void setValidityStartTime(Timestamp validityStartTime) {
		this.validityStartTime = validityStartTime;
	}
	
	@Column(name="validity_end_time")
	public Timestamp getValidityEndTime() {
		return validityEndTime;
	}

	public void setValidityEndTime(Timestamp validityEndTime) {
		this.validityEndTime = validityEndTime;
	}

	@Transient
	public List<ResourceAttachment> getResourceAttachments() {
		return resourceAttachments;
	}

	public void setResourceAttachments(List<ResourceAttachment> resourceAttachments) {
		this.resourceAttachments = resourceAttachments;
	}

}
