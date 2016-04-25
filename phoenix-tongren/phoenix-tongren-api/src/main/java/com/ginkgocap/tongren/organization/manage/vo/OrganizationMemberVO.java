package com.ginkgocap.tongren.organization.manage.vo;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Transient;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 组织成员VO
 * @author liweichao
 *
 */
public class OrganizationMemberVO extends BasicBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7127968090676352472L;
	
	private long memberId;

	private long userId;
	
	private String userName;
	
	private Timestamp createTime;
	
	private Timestamp addTime; //成员加入组织时间
	
	private String roleName;
	
	private String logo;
	
	private long roleId;//成员角色Id
	
	private long depId;//成员部门Id
	
	private int status;
	
	private String depName;//部门名称
	
	private long messageReceiveId; //消息id
	
	private String roleStr;//角色 名称 

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	@Transient
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public long getMemberId() {
		return memberId;
	}

	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	public long getDepId() {
		return depId;
	}

	public void setDepId(long depId) {
		this.depId = depId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public long getMessageReceiveId() {
		return messageReceiveId;
	}

	public void setMessageReceiveId(long messageReceiveId) {
		this.messageReceiveId = messageReceiveId;
	}

	public String getRoleStr() {
		return roleStr;
	}

	public void setRoleStr(String roleStr) {
		this.roleStr = roleStr;
	}
	
	
}
