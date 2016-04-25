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
 * 组织模块
 * @author liny
 *
 */
@Entity 
@Table(name="tb_organization_module")
public class Module implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;//模块名称
	
	private Timestamp createTime;//创建日期
	
	private Timestamp updateTime;//更新时间
	
	private long createrId;//创建人ID
	
	private String description;//模块描述
	
	private int status;//1启用、2关闭
	
	private String url;//模块URL
	
	private long organizationId; //组织id、 0公用组织id、供以后扩展用

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "ModuleId")
	@GenericGenerator(name = "ModuleId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ModuleId") })
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
	 * @return the name
	 */
	
	@Column(name="name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the description
	 */
	@Column(name="description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	@Column(name="status")
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the url
	 */
	@Column(name="url")
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return "Module [id=" + id + ", name=" + name + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", createrId="
				+ createrId + ", description=" + description + ", status="
				+ status + ", url=" + url + ",organizationId=" + organizationId + "]";
	}
	
	
}
