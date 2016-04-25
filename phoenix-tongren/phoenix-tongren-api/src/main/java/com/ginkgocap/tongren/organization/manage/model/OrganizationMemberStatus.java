package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 组织成员状态
 * @author yanweiqi
 */
public class OrganizationMemberStatus implements Serializable {

	private static final long serialVersionUID = -6001773639496145479L;
	private long id;
	private String value; //1申请加入、2审核中、3审核通过、4申请退出、5退出审核中、6退出审核通过、7退出组织
	private Timestamp createTime;
	private Timestamp updateTime;
	private long createrId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
