/**
 * 
 */
package com.ginkgocap.tongren.organization.review.web;

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
 * 组织审核申请表
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_review_application")
public class ReviewApplication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private long reviewProcessId;//审核流程ID
	
	private long reviewGenreId;//审核流程类型ID
	
	private long organizationId;//组织ID
	
	private long applyId;//申请者ID
	
	private String applyDate;//申请日期
	
	private String applyRereason;//申请原因
	
	private String startTime; //申请开始时间
	
	private String endTime;//申请结束时间
	
	private String applicationNo;//申请编号
	

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "ReviewApplicationId")
	@GenericGenerator(name = "ReviewApplicationId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ReviewApplicationId") })
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
	 * @return the reviewProcessId
	 */
	@Column(name = "review_process_id")
	public long getReviewProcessId() {
		return reviewProcessId;
	}

	/**
	 * @param reviewProcessId the reviewProcessId to set
	 */
	public void setReviewProcessId(long reviewProcessId) {
		this.reviewProcessId = reviewProcessId;
	}
	@Column(name = "review_genre_id")
	public long getReviewGenreId() {
		return reviewGenreId;
	}

	public void setReviewGenreId(long reviewGenreId) {
		this.reviewGenreId = reviewGenreId;
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

	/**
	 * @return the applyId
	 */
	@Column(name = "apply_id")
	public long getApplyId() {
		return applyId;
	}

	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(long applyId) {
		this.applyId = applyId;
	}

	/**
	 * @return the applyDate
	 */
	@Column(name = "apply_date")
	public String getApplyDate() {
		return applyDate;
	}

	/**
	 * @param applyDate the applyDate to set
	 */
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	/**
	 * @return the applyRereason
	 */
	@Column(name = "apply_rereason")
	public String getApplyRereason() {
		return applyRereason;
	}

	/**
	 * @param applyRereason the applyRereason to set
	 */
	public void setApplyRereason(String applyRereason) {
		this.applyRereason = applyRereason;
	}

	@Column(name = "start_time")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	@Column(name = "end_time")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "application_no")
	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

}
