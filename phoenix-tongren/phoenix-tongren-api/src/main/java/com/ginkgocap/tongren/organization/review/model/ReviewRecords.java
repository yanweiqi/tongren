/**
 * 
 */
package com.ginkgocap.tongren.organization.review.model;

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
 * 组织审核记录表
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_review_records")
public class ReviewRecords implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private long reviewUserId;// 签核人Id
	
	private int reviewStatus;//0审核中、1、撤回申请 2审核通过、3审核拒绝
	
	private Timestamp reviewDate;//签核日期
	
	private String reviewMessage;//签核消息
	
	private long applicationId;//审核申请Id
	
	private long organizationId;//组织Id
	
	private int  isReview;//当前是否需要审核：1需要审核 0不需要审核
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "ReviewRecordsId")
	@GenericGenerator(name = "ReviewRecordsId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ReviewRecordsId") })
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
	@Column(name = "review_user_id")
	public long getReviewUserId() {
		return reviewUserId;
	}

	public void setReviewUserId(long reviewUserId) {
		this.reviewUserId = reviewUserId;
	}
	@Column(name = "review_date")
	public Timestamp getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Timestamp reviewDate) {
		this.reviewDate = reviewDate;
	}
	@Column(name = "review_message")
	public String getReviewMessage() {
		return reviewMessage;
	}

	public void setReviewMessage(String reviewMessage) {
		this.reviewMessage = reviewMessage;
	}
	
	@Column(name = "review_status")
	public int getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	@Column(name = "application_id")
	public long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	
	@Column(name = "organization_id")
	public long getOrganizationId() {
	return organizationId;
	}

	public void setOrganizationId(long organizationId) {
	this.organizationId = organizationId;
	}
	
	@Column(name = "is_review")
	public int getIsReview() {
		return isReview;
	}

	public void setIsReview(int isReview) {
		this.isReview = isReview;
	}
	
}
