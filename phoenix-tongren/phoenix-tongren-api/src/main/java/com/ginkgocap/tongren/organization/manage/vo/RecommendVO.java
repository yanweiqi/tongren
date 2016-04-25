package com.ginkgocap.tongren.organization.manage.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class RecommendVO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long id;
	
	public String name;
	
	public String taskId;
	
	public Timestamp  createTime;
	
	public String createName;
	
	public int type;//0：组织  1:好友
	
	public long orgCreateId;//组织创建人id
	
	public long friendId;//好友id
	
	public long orgId;

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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public long getOrgCreateId() {
		return orgCreateId;
	}

	public void setOrgCreateId(long orgCreateId) {
		this.orgCreateId = orgCreateId;
	}

	public long getFriendId() {
		if(type==0){
			return getOrgCreateId();
		}else if(type==1){
			return getId();
		}
		return -1;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
}
