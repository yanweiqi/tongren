/**
 * 
 */
package com.ginkgocap.tongren.organization.authority.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 组织角色权限
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_role_authority")
public class OrganizationRoleAuthority implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	
	private long roleId;//组织角色ID
	
	private long authorityId;//权限
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updateTime;//更新时间
	
	private long createrId;//创建者ID
	
	private long organizationId;//组织者ID

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "organizationRoleAuthorityId")
	@GenericGenerator(name = "organizationRoleAuthorityId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "organizationRoleAuthorityId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the roleId
	 */
	@Column(name="role_id")
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the createTime
	 */
	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the organizationId
	 */
	@Column(name="organization_id")	
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name="authority_id")	
	public long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(long authorityId) {
		this.authorityId = authorityId;
	}
	
	@Column(name="creater_id")
	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
	
}
