package com.ginkgocap.tongren.organization.review.vo;

import java.io.Serializable;

/**
 * 审批人视图类
 * @author Administrator
 *
 */
public class ReviewObjectVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7388557330111737091L;

	private long userId;
	
	private String name;
	
	private int status;		
	
	private String startTime; //审批开始时间
	
	private String passTime;//审核通过时间

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getPassTime() {
		return passTime;
	}

	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}
}
