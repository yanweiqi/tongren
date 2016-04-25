/*
 * 文件名： ResourceVO.java
 * 创建日期： 2015年11月3日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.resource.vo;

import java.io.Serializable;
import java.sql.Timestamp;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月3日
 */
public class ResourceVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private long createId;//创建者ID
	
	private long organizationId;//组织者ID
	
	private long projectId;//项目ID
	
	private String taskId;//图片标识ID---跟file库中taskID对应
	
	private Timestamp createTime;//创建时间
	
	private String titleName;//文件名称
	
	private String path;//路径
	
	private String createName;//上传人姓名
	
	/**
	 * @return 返回 createName。
	 */
	public String getCreateName() {
		return createName;
	}

	/**
	 * ---@param createName 要设置的 createName。
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * @return 返回 id。
	 */
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
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * ---@param createTime 要设置的 createTime。
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return 返回 titleName。
	 */
	public String getTitleName() {
		return titleName;
	}

	/**
	 * ---@param titleName 要设置的 titleName。
	 */
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	/**
	 * @return 返回 path。
	 */
	public String getPath() {
		return path;
	}

	/**
	 * ---@param path 要设置的 path。
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
}
