package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 组织部门成员
 * @author yanweiqi
 */
@Entity
@Table(name="tb_organization_department_member")
public class OrganizationDepMember implements Serializable{
	
	private static final long serialVersionUID = 2209748151579526684L;
	private long id;
	private long organizationMemberId; //组织成员ID
	private long organizationId;
	private long depId;
	private Timestamp createTime;//组织创建时间
	private Timestamp updateTime;//组织创建时间
	private long createrId;    //创建者ID
	private long userId;
	private String userName;
	private String userPic;
		
	@Id
	@GeneratedValue(generator = "OrganizationDepMemberId")
	@GenericGenerator(name = "OrganizationDepMemberId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationDepMemberId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	@Column(name="department_id")
	public long getDepId() {
		return depId;
	}
	public void setDepId(long depId) {
		this.depId = depId;
	}
	
	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name="creater_id")
	public long getCreaterId() {
		return createrId;
	}
	
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
	@Column(name="organization_member_id")
	public long getOrganizationMemberId() {
		return organizationMemberId;
	}
	public void setOrganizationMemberId(long organizationMemberId) {
		this.organizationMemberId = organizationMemberId;
	}
	
	@Transient
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	@Transient
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Transient
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
