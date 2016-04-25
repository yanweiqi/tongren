package com.ginkgocap.tongren.organization.purse.model;

import java.io.Serializable;

/**
 * 第三方钱包
 * @author yanweiqi
 */
public class OrganizationThirdPurse implements Serializable{

	private static final long serialVersionUID = -8007218201048537273L;

	private long id;
	private String name;
	private String account;
	private long organizationCapitalId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public long getOrganizationCapitalId() {
		return organizationCapitalId;
	}
	public void setOrganizationCapitalId(long organizationCapitalId) {
		this.organizationCapitalId = organizationCapitalId;
	}
	
}
