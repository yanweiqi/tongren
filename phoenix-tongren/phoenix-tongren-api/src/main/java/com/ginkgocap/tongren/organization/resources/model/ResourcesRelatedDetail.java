package com.ginkgocap.tongren.organization.resources.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 
 * @author hanxifa
 *
 */

@Entity
@Table(name = "tb_resources_related_detail")
public class ResourcesRelatedDetail extends BasicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long relatedId;//关联id 对应基础表的id，比如人脉，对应人脉表的id
	
	private long resourcesRelatedId;//对应 tb_resources_related表的id
	
	private int subType;//目前分两种   1 金桐脑推荐，2 我的
	
	private int relatedType;//关联1人脉，2 组织 3 知识 4 事件

	/**
	 * @return 返回 id。
	 */
	@Id
	@GeneratedValue(generator = "ResourcesRelatedDetailID")
	@GenericGenerator(name = "ResourcesRelatedDetailID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ResourcesRelatedDetailID") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "related_id")
	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	@Column(name = "resource_related_id")
	public long getResourcesRelatedId() {
		return resourcesRelatedId;
	}

	public void setResourcesRelatedId(long resourcesRelatedId) {
		this.resourcesRelatedId = resourcesRelatedId;
	}

	@Column(name = "sub_type")
	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	@Column(name = "related_type")
	public int getRelatedType() {
		return relatedType;
	}

	public void setRelatedType(int relatedType) {
		this.relatedType = relatedType;
	}

	 
	
	
}
