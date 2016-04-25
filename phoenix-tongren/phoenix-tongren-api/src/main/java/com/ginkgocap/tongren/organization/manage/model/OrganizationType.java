package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;

/**
 * 组织类型 视图类
 * @author Administrator
 *
 */

public class OrganizationType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String name;
	
	private int pid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public OrganizationType(int id, String name,int pid) {
		super();
		this.id = id;
		this.name = name;
		this.pid =pid;
	}
}
