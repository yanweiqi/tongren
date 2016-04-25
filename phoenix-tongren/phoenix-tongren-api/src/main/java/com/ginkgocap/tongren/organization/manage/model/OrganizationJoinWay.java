package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 组织类型
 * @author yanweiqi
 */
public class OrganizationJoinWay implements Serializable {

	private static final long serialVersionUID = -6001773639496145479L;
	private long id;
	private String joinWay;
	private Timestamp createTime;
	private Timestamp updateTime;
	private long createrId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getJoinWay() {
		return joinWay;
	}
	public void setJoinWay(String joinWay) {
		this.joinWay = joinWay;
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

}
