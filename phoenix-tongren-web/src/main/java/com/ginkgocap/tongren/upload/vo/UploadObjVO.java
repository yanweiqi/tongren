/*
 * 文件名： UploadObjVO.java
 * 创建日期： 2015年10月30日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.upload.vo;





 /**
 *  项目文件上传VO
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月30日
 */
public class UploadObjVO{
	
	private long fileSize;//文件大小
	
	private String taskId;//文件标识ID
	
	private String imgFullpath;//完整路径
	
	private String fileName;//文件名称
	
	/**
	 * @return 返回 fileName。
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * ---@param fileName 要设置的 fileName。
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return 返回 fileSize。
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * ---@param fileSize 要设置的 fileSize。
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
	
	
	
	
}
