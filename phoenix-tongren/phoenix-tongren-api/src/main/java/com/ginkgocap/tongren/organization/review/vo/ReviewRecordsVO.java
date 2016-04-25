package com.ginkgocap.tongren.organization.review.vo;

import com.ginkgocap.tongren.organization.review.model.ReviewRecords;

public class ReviewRecordsVO extends ReviewRecords{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;//审核用户名
	private int level;//审核级别 ，较小的先审批
	private String userPicPath;//用户头像的路径

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUserPicPath() {
		return userPicPath;
	}

	public void setUserPicPath(String userPicPath) {
		this.userPicPath = userPicPath;
	}
	
	
}
