package com.ginkgocap.tongren.organization.system.model;


/**
 * 组织模块集
 * @author yanweiqi
 */
public enum OrganizationModules{

	ORGANIZATION_MANAGE("0","组织管理",1),
	
	ORGANIZATION_MEMBER("1","组织成员",1),
	
	ORGANIZATION_RGROUP("2","组织成员分组",1),
	
	ORGANIZATION_RESOURCE("3","组织资源",1),
	
	ORGANIZATION_ROLE("4","组织角色",1),
	
	ORGANIZATION_APPLICATION("5","组织应用",1),
	
	ORGANIZATION_PURSE("6","组织钱包",1),
	
	ORGANIZATION_CERTIFIED("7","组织认证",1),
	
	ORGANIZATION_MEMBE_DEPARTMENT("8","组织成员分部门",0),
	
	PROJECT_MANAGE("9","项目管理",0),
	
	PROJECT_MANAGE_TASK("10","项目任务管理",0);
	
	private String key;
	private String value;
	private int status;
	
	private OrganizationModules(String key, String value,int status) {
		this.key = key;
		this.value = value;
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}