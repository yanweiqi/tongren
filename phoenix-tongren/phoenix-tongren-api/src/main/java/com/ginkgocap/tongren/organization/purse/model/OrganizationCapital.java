package com.ginkgocap.tongren.organization.purse.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 组织资金、钱包
 * @author yanweiqi
 *
 */
public class OrganizationCapital implements Serializable {

	private static final long serialVersionUID = -2428665860567116857L;
    private long id;
    private long organizationId;
    private double amonut;
    private Timestamp createTime;
    private Timestamp updateTime;
    private long createrId;
    private int status;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public double getAmonut() {
		return amonut;
	}
	public void setAmonut(double amonut) {
		this.amonut = amonut;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public long getCreaterId() {
		return createrId;
	}
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    
	
}
