/*
 * 文件名： UploadItem.java
 * 创建日期： 2015年10月15日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.upload.vo;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月15日
 */
public class UploadItem implements java.io.Serializable{
	
	/**
	 * ---serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String filename;//文件名称
	
	private String filepath;//图片路径
	
	private String imgUrl;//图片地址
	
	private String imgType;//0.临时文件 1:图片 2：音频 3.组织头像 4.文件
	
	private String imgFullpath;//完整路径
	
	private String fileSize;//文件大小
	
	private String taskId;//文件标识ID
	
	private long projectId;//项目ID
	
	private long organizationId;//承接项目的组织ID
	
	private List<CommonsMultipartFile> fileData;
	
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
	 * @return 返回 fileSize。
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * ---@param fileSize 要设置的 fileSize。
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return 返回 imgFullpath。
	 */
	public String getImgFullpath() {
		return imgFullpath;
	}

	/**
	 * ---@param imgFullpath 要设置的 imgFullpath。
	 */
	public void setImgFullpath(String imgFullpath) {
		this.imgFullpath = imgFullpath;
	}

	/**
	 * @return 返回 imgType。
	 */
	public String getImgType() {
		return imgType;
	}

	/**
	 * ---@param imgType 要设置的 imgType。
	 */
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	/**
	 * @return 返回 filepath。
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * ---@param filepath 要设置的 filepath。
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	/**
	 * @return 返回 imgUrl。
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * ---@param imgUrl 要设置的 imgUrl。
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @return 返回 filename。
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * ---@param filename 要设置的 filename。
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return 返回 fileData。
	 */
	public List<CommonsMultipartFile> getFileData() {
		return fileData;
	}

	/**
	 * ---@param fileData 要设置的 fileData。
	 */
	public void setFileData(List<CommonsMultipartFile> fileData) {
		this.fileData = fileData;
	}
	
}
