package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="tb_organization_member_role")
public class OrganizationMemberRole implements Serializable {


	private static final long serialVersionUID = 6455498475785764962L;
	
	private long id;
	private long organizationId;
	private long roleId;
	private long organizationMemberId;
	private Timestamp createTime;
	private Timestamp updateTime;
	private long createrId;
	
	@Id
	@GeneratedValue(generator = "OrganizationMemberRoleGroupId")
	@GenericGenerator(name = "OrganizationMemberRoleGroupId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationRoleGroupId") })
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
	
	@Column(name="role_id")
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
	
}
