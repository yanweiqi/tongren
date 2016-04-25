/*
 * 文件名： ProjectEnclosure.java
 * 创建日期： 2015年10月29日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
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

import com.ginkgocap.ywxt.file.model.FileIndex;


 /**
 *  项目附件表实体
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月29日
 */
@Entity
@Table(name="tb_project_resources_attachment")
public class ResourceAttachment implements Serializable {
	
	private static final long serialVersionUID = -3041606323398289477L;

	private long id;
	
	private long createrId;//创建者ID
	
	private long projectId;//项目ID
	
	private String taskId;//图片标识ID---跟file库中taskID对应
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updateTime;
	
	private FileIndex fileIndex;

	/**
	 * @return 返回 id。
	 */
	@Id
	@GeneratedValue(generator = "ResourcesAttachmentId")
	@GenericGenerator(name = "ResourcesAttachmentId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ProjectEnclosureId") })
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
	@Column(name = "creater_id")
	public long getCreaterId() {
		return createrId;
	}

	/**
	 * ---@param createId 要设置的 createId。
	 */
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
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

	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return 返回 fileIndex。
	 */
	@Transient
	public FileIndex getFileIndex() {
		return fileIndex;
	}

	/**
	 * ---@param fileIndex 要设置的 fileIndex。
	 */
	public void setFileIndex(FileIndex fileIndex) {
		this.fileIndex = fileIndex;
	}
	
}
