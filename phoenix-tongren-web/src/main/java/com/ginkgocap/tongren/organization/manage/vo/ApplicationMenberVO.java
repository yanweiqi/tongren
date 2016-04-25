/*
 * 文件名： ApplicationMenberVO.java
 * 创建日期： 2015年11月16日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.manage.vo;

import java.sql.Timestamp;


 /**
 *  申请成员和退出申请VO对象
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月16日
 */
public class ApplicationMenberVO {

	private long applicationId;//申请ID（对象消息中的接收消息ID）
	
	private String name;//姓名
	
	private Timestamp applicationTime;//申请时间
	
	private String uPicPath;//头像路径
	
	/**
	 * @return 返回 applicationId。
	 */
	public long getApplicationId() {
		return applicationId;
	}

	/**
	 * ---@param applicationId 要设置的 applicationId。
	 */
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * ---@param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 applicationTime。
	 */
	public Timestamp getApplicationTime() {
		return applicationTime;
	}

	/**
	 * ---@param applicationTime 要设置的 applicationTime。
	 */
	public void setApplicationTime(Timestamp applicationTime) {
		this.applicationTime = applicationTime;
	}

	/**
	 * @return 返回 uPicPath。
	 */
	public String getuPicPath() {
		return uPicPath;
	}

	/**
	 * ---@param uPicPath 要设置的 uPicPath。
	 */
	public void setuPicPath(String uPicPath) {
		this.uPicPath = uPicPath;
	}
	
}
