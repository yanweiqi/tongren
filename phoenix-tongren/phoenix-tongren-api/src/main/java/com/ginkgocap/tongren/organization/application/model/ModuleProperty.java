/**
 * 
 */
package com.ginkgocap.tongren.organization.application.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * 组织模块属性
 * @author liny
 *
 */
public class ModuleProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;//属性名称
	
	private long createId;//创建人ID
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updatTime;//更新时间
	
	private int status;//属性状态
	
	private String description;//属性描述
	
	private long moduleId;//模块ID
	
	private String url;//属性URL

	/**
	 * @return the id
	 */
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
	 * @return the createId
	 */
	public long getCreateId() {
		return createId;
	}

	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(long createId) {
		this.createId = createId;
	}

	/**
	 * @return the createTime
	 */
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
	 * @return the updatTime
	 */
	public Timestamp getUpdatTime() {
		return updatTime;
	}

	/**
	 * @param updatTime the updatTime to set
	 */
	public void setUpdatTime(Timestamp updatTime) {
		this.updatTime = updatTime;
	}

	/**
	 * @return the status
	 */
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
	 * @return the description
	 */
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
	 * @return the moduleId
	 */
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
