package com.ginkgocap.tongren.organization.manage.vo;

import com.ginkgocap.tongren.project.task.model.Task;

public class OrgTaskVo extends Task{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orgName;//组织名称
	private String attachUrl;//组织任务的附件url
	private String rejectReason;//退回原因
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAttachUrl() {
		return attachUrl;
	}
	public void setAttachUrl(String attachUrl) {
		this.attachUrl = attachUrl;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	

}
