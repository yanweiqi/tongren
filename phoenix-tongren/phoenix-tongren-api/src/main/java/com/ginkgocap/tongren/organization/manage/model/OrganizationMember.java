package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.ywxt.user.model.User;

/**
 * 组织成员表
 * @author yanweiqi
 */
@Entity
@Table(name="tb_organization_member")
public class OrganizationMember implements Serializable{
	
	private static final long serialVersionUID = 2209748151579526684L;
	private long id;
	private long userId;         
	private long organizationId;  
	private Timestamp createTime;
	private Timestamp updateTime;
	private long createrId;    
	private int joinWay;
	private Timestamp applyTime;
	private int status;      //1邀请加入、2申请加入、3正式成员、4申请退出
	private User user;
	private OrganizationRole organizationRole;
	private OrganizationDep organizationDep;
	private Organization organization;
	
	@Id
	@GeneratedValue(generator = "OrganizationMemberId")
	@GenericGenerator(name = "OrganizationMemberId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationMemberId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "user_id")
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "create_id")
	public long getCreaterId() {
		return createrId;
	}
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	@Column(name = "join_way")
	public int getJoinWay() {
		return joinWay;
	}
	public void setJoinWay(int joinWay) {
		this.joinWay = joinWay;
	}
	@Column(name = "apply_time")
	public Timestamp getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}
	@Column(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Transient
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Transient
	public OrganizationRole getOrganizationRole() {
		return organizationRole;
	}
	public void setOrganizationRole(OrganizationRole organizationRole) {
		this.organizationRole = organizationRole;
	}
	
	@Transient
	public OrganizationDep getOrganizationDep() {
		return organizationDep;
	}
	public void setOrganizationDep(OrganizationDep organizationDep) {
		this.organizationDep = organizationDep;
	}
	
	@Transient
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	
}
