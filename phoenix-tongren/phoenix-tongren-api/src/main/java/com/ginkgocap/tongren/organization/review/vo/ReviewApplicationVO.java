package com.ginkgocap.tongren.organization.review.vo;

import java.io.Serializable;


public class ReviewApplicationVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5527364396460888351L;

	private long recordsId; //记录Id
	
	private long processId;//审批流程Id
	
	private String description;//流程说明
	
	private String applyRereason;//申请原因
	
	private String processName; //审批流程名称
	
	private String genreName;//审批流程子类型名称
	
	private String createTime;//流程创建时间
	
	private String reviewNo;//审核编号
	
	private String startTime;//记录提交开始时间
	
	private String endTime;//记录结束时间
	
	private int recordsStatus;//记录状态

	public long getRecordsId() {
		return recordsId;
	}

	public void setRecordsId(long recordsId) {
		this.recordsId = recordsId;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getReviewNo() {
		return reviewNo;
	}

	public void setReviewNo(String reviewNo) {
		this.reviewNo = reviewNo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getApplyRereason() {
		return applyRereason;
	}

	public void setApplyRereason(String applyRereason) {
		this.applyRereason = applyRereason;
	}

	public int getRecordsStatus() {
		return recordsStatus;
	}

	public void setRecordsStatus(int recordsStatus) {
		this.recordsStatus = recordsStatus;
	}
}
