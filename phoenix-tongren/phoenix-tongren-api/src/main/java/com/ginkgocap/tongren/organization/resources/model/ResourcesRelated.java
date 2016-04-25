package com.ginkgocap.tongren.organization.resources.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 资源关联，包括关联的人脉，组织，知识，事件
 * 
 * @author hanxifa
 * @version
 * @since 2016年02月26日
 */
@Entity
@Table(name = "tb_resources_related")
public class ResourcesRelated extends BasicBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	
	private long resourceId;// 资源id
	
	private int resourceType;// 资源类型 1 我的资源，2 组织资源
	
	private int relatedType;// 关联类型   1 人脉，2 组织 3 知识 4事件
	
	private String relateName;// 关联名称
	
	private long userId;//用户id
	
	private Timestamp createTime;// 创建时间
	
	private List<ResourcesRelatedDetail> detail;
	
	@Id
	@GeneratedValue(generator = "ResourcesRelatedID")
	@GenericGenerator(name = "ResourcesRelatedID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ResourcesRelatedID") })
	@Column(name = "id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "resource_id")
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	
	@Column(name = "resource_source")
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	
	@Column(name = "related_type")
	public int getRelatedType() {
		return relatedType;
	}
	public void setRelatedType(int relatedType) {
		this.relatedType = relatedType;
	}
	
	@Column(name = "related_name")
	public String getRelateName() {
		return relateName;
	}
	public void setRelateName(String relateName) {
		this.relateName = relateName;
	}
	
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Transient
	public List<ResourcesRelatedDetail> getDetail() {
		return detail;
	}
	@Transient
	public void setDetail(List<ResourcesRelatedDetail> detail) {
		this.detail = detail;
	}
	@Column(name = "user_id")
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	 

	
}
