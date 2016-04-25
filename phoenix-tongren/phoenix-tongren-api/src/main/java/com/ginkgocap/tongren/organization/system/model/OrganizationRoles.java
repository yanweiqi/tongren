package com.ginkgocap.tongren.organization.system.model;


/**
 * 组织角色集合
 * @author yanweiqi
 */
public enum OrganizationRoles{
		
	//TONGREN_ADMIN("0","桐人管理员"),
	CREATER("1","创建者"),
	ADMIN("2","管理员"),
	DEPARTMENT_LEADER("3","部门负责人"),
	GROUP_LEADER("4","组负责人"),
	MEMBER("5","普通成员");

	private String key;
	private String value;
	
	private OrganizationRoles(String key, String value) {
		this.key = key;
		this.value = value;
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
	
	

}