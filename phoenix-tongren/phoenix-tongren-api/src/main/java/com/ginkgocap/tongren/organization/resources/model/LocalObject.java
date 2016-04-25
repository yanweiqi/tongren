/*
 * 文件名： LocalObject.java
 * 创建日期： 2015年10月29日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.resources.model;

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

import com.ginkgocap.tongren.common.model.BasicBean;


 /**
 *  我的本地资源表
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月29日
 */
@Entity
@Table(name="tb_resources_local_object")
public class LocalObject extends BasicBean implements Serializable {

	/**
	 * ---serialVersionUID
	 */
	private static final long serialVersionUID = -1886849625517486538L;

	private long id;
	
	private long createId;//创建者ID
	
	private long organizationId;//组织者ID
	
	private long projectId;//项目ID
	
	private String taskId;//图片标识ID---跟file库中taskID对应
	
	private Timestamp createTime;//创建时间
	
	private String title;//文档标题

	/**
	 * @return 返回 id。
	 */
	@Id
	@GeneratedValue(generator = "ResourcesLocalObjectID")
	@GenericGenerator(name = "ResourcesLocalObjectID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ResourcesLocalObjectID") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	/**
	 * ---@param id 要设置的 id。
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return 返回 createId。
	 */
	@Column(name = "create_id")
	public long getCreateId() {
		return createId;
	}

	/**
	 * ---@param createId 要设置的 createId。
	 */
	public void setCreateId(long createId) {
		this.createId = createId;
	}

	/**
	 * @return 返回 organizationId。
	 */
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * ---@param organizationId 要设置的 organizationId。
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return 返回 projectId。
	 */
	@Column(name = "project_id")
	public long getProjectId() {
		return projectId;
	}

	/**
	 * ---@param projectId 要设置的 projectId。
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return 返回 taskId。
	 */
	@Column(name = "task_id")
	public String getTaskId() {
		return taskId;
	}

	/**
	 * ---@param taskId 要设置的 taskId。
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return 返回 createTime。
	 */
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * ---@param createTime 要设置的 createTime。
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Transient//三期才加入这个字段@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
