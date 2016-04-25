/*
 * 文件名： UndertakenVO.java
 * 创建日期： 2015年11月20日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.vo;

import java.sql.Timestamp;


 /**
 * 承接项目VO对象
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月20日
 */
public class UndertakenVO {

	private Long projectId;//项目ID
	
	private String name;//项目名称
	
	private String introduction;//项目简介
	
	private int cycle;//项目周期
	
	private double remuneration;//项目酬劳
	
	private long organizationId;//承接的组织ID
	
	private String organizationName;//承接的组织名称
	
	private Timestamp startTime; //承接项目开始时间
	
	private Timestamp endTime; //承接项目结束时间
	
	private int status; //0项目进行中、1完成、2、放弃、3已过期
	
	private String area;//地区
	
	private String industry;//行业
	
	private int lDays;//剩余天数
	
	private String statusStr;//状态描述
	
	private int projectType;//1：组织项目  2：个人项目
	
	private long recipientId;//承接人ID
	
	private String recipientName;//承接人姓名
	
	private String recipientPicUrl;//承接人头像
	
	private long createProjectId;//项目创建人ID
	
	private String createProjectName;//项目创建人姓名
	
	private String createProjectPicUrl;//项目创建人头像
	
	/**
	 * @return 返回 recipientId。
	 */
	public long getRecipientId() {
		return recipientId;
	}

	/**
	 * ---@param recipientId 要设置的 recipientId。
	 */
	public void setRecipientId(long recipientId) {
		this.recipientId = recipientId;
	}

	/**
	 * @return 返回 recipientName。
	 */
	public String getRecipientName() {
		return recipientName;
	}

	/**
	 * ---@param recipientName 要设置的 recipientName。
	 */
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	/**
	 * @return 返回 recipientPicUrl。
	 */
	public String getRecipientPicUrl() {
		return recipientPicUrl;
	}

	/**
	 * ---@param recipientPicUrl 要设置的 recipientPicUrl。
	 */
	public void setRecipientPicUrl(String recipientPicUrl) {
		this.recipientPicUrl = recipientPicUrl;
	}

	/**
	 * @return 返回 createProjectId。
	 */
	public long getCreateProjectId() {
		return createProjectId;
	}

	/**
	 * ---@param createProjectId 要设置的 createProjectId。
	 */
	public void setCreateProjectId(long createProjectId) {
		this.createProjectId = createProjectId;
	}

	/**
	 * @return 返回 createProjectName。
	 */
	public String getCreateProjectName() {
		return createProjectName;
	}

	/**
	 * ---@param createProjectName 要设置的 createProjectName。
	 */
	public void setCreateProjectName(String createProjectName) {
		this.createProjectName = createProjectName;
	}

	/**
	 * @return 返回 createProjectPicUrl。
	 */
	public String getCreateProjectPicUrl() {
		return createProjectPicUrl;
	}

	/**
	 * ---@param createProjectPicUrl 要设置的 createProjectPicUrl。
	 */
	public void setCreateProjectPicUrl(String createProjectPicUrl) {
		this.createProjectPicUrl = createProjectPicUrl;
	}

	/**
	 * @return 返回 projectId。
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * ---@param projectId 要设置的 projectId。
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	 * @return 返回 introduction。
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * ---@param introduction 要设置的 introduction。
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return 返回 cycle。
	 */
	public int getCycle() {
		return cycle;
	}

	/**
	 * ---@param cycle 要设置的 cycle。
	 */
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	/**
	 * @return 返回 remuneration。
	 */
	public double getRemuneration() {
		return remuneration;
	}

	/**
	 * ---@param remuneration 要设置的 remuneration。
	 */
	public void setRemuneration(double remuneration) {
		this.remuneration = remuneration;
	}

	/**
	 * @return 返回 organizationName。
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * ---@param organizationName 要设置的 organizationName。
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	/**
	 * @return 返回 startTime。
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * ---@param startTime 要设置的 startTime。
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return 返回 endTime。
	 */
	public Timestamp getEndTime() {
		return endTime;
	}

	/**
	 * ---@param endTime 要设置的 endTime。
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return 返回 status。
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * ---@param status 要设置的 status。
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return 返回 area。
	 */
	public String getArea() {
		return area;
	}

	/**
	 * ---@param area 要设置的 area。
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return 返回 industry。
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * ---@param industry 要设置的 industry。
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * @return 返回 lDays。
	 */
	public int getlDays() {
		return lDays;
	}

	/**
	 * ---@param lDays 要设置的 lDays。
	 */
	public void setlDays(int lDays) {
		this.lDays = lDays;
	}

	/**
	 * @return 返回 statusStr。
	 */
	public String getStatusStr() {
		return statusStr;
	}

	/**
	 * ---@param statusStr 要设置的 statusStr。
	 */
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	/**
	 * @return 返回 projectType。
	 */
	public int getProjectType() {
		return projectType;
	}

	/**
	 * ---@param projectType 要设置的 projectType。
	 */
	public void setProjectType(int projectType) {
		this.projectType = projectType;
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
	
}
