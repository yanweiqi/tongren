/**
 * 
 */
package com.ginkgocap.tongren.organization.review.model;

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

/**
 * 组织审核流程
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_review_process")
public class ReviewProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String reviewName;//审核名称
	
	private long createId;//创建人ID
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updateTime;//修改时间
	
	private String reviewNo;//审核编号
	
	private String description;//说明
	
	private long organizationId;//组织ID
	
	private List<ReviewGenre> genreList;//审批类型
	
	private List<ReviewObject> objectList;//审批类型
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "ReviewProcessId")
	@GenericGenerator(name = "ReviewProcessId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ReviewProcessId") })
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
	 * @return the reviewName
	 */
	@Column(name = "review_name")
	public String getReviewName() {
		return reviewName;
	}

	/**
	 * @param reviewName the reviewName to set
	 */
	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	/**
	 * @return the createId
	 */
	@Column(name = "create_id")
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
	@Column(name = "create_time")
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
	@Column(name = "update_time")
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
	 * @return the reviewNo
	 */
	@Column(name = "review_no")
	public String getReviewNo() {
		return reviewNo;
	}

	/**
	 * @param reviewNo the reviewNo to set
	 */
	public void setReviewNo(String reviewNo) {
		this.reviewNo = reviewNo;
	}

	/**
	 * @return the description
	 */
	@Column(name = "description")
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
	 * @return the organizationId
	 */
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	@Transient
	public List<ReviewGenre> getGenreList() {
		return genreList;
	}

	public void setGenreList(List<ReviewGenre> genreList) {
		this.genreList = genreList;
	}
	@Transient
	public List<ReviewObject> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<ReviewObject> objectList) {
		this.objectList = objectList;
	}
}
