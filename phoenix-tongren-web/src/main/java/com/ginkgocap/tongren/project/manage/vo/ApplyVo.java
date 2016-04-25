package com.ginkgocap.tongren.project.manage.vo;

public class ApplyVo {
	
	private long   organizationId;	        //组织ID
    private String organizationName;        //组织名称
	private String createrName;             //组织创建者名称
	private long   createrId;         	    //组织创建者ID
  	private int    organizationMemberSize;  //组织成员数量
  	private String organizationLogo;        //组织Logo
  
  	private long   proposerId;              //申请者ID
  	private String proposerName;            //申请者名称
  	private String proposerfigureurl;       //申请者头像URI
  
  	private boolean isOrganization;         //是否为组织承接

	public long getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	public String getCreaterName() {
		return createrName;
	}
	
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	
	public long getCreaterId() {
		return createrId;
	}
	
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
	public int getOrganizationMemberSize() {
		return organizationMemberSize;
	}
	
	public void setOrganizationMemberSize(int organizationMemberSize) {
		this.organizationMemberSize = organizationMemberSize;
	}
	
	public String getOrganizationLogo() {
		return organizationLogo;
	}
	
	public void setOrganizationLogo(String organizationLogo) {
		this.organizationLogo = organizationLogo;
	}
	
	public long getProposerId() {
		return proposerId;
	}
	
	public void setProposerId(long proposerId) {
		this.proposerId = proposerId;
	}
	
	public String getProposerName() {
		return proposerName;
	}
	
	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}
	
	public String getProposerfigureurl() {
		return proposerfigureurl;
	}
	
	public void setProposerfigureurl(String proposerfigureurl) {
		this.proposerfigureurl = proposerfigureurl;
	}
	
	public boolean getIsOrganization() {
		return isOrganization;
	}
	
	public void setIsOrganization(boolean isOrganization) {
		this.isOrganization = isOrganization;
	}

}
