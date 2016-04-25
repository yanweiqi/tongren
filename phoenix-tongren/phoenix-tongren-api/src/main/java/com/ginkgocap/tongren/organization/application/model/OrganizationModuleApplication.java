/**
 * 
 */
package com.ginkgocap.tongren.organization.application.model;

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
 * 组织模块应用
 * @author liny
 *
 */
@Entity 
@Table(name="tb_organization_module_application")
public class OrganizationModuleApplication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long organizationId;//组织id
	
	private long moduleId;//模块id
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updateTime;//更新时间
	
	private long createrId;//创建人id

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "ModuleApplicationId")
	@GenericGenerator(name = "ModuleApplicationId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ModuleApplicationId") })
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

	/**
	 * @return the moduleId
	 */
	@Column(name="module_id")
	public long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
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
	 * @return the createId
	 */
	@Column(name="creater_id")
	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
}
