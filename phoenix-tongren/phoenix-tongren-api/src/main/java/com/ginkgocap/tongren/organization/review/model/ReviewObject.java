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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 组织审核对象
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_review_object")
public class ReviewObject implements Serializable{

	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;

	private long id;
	
	private long organizationId;//组织ID
	
	private int reviewLevel;//检核级别
	
	private long reviewRoleId;//签核角色ID
	
	private long reviewUserId;//签核人ID
	
	private Timestamp createTime;//创建日期
	
	private Timestamp upateTime;//更新时间
	
	private long createId;//创建人ID
	
	private long backupReviewUserId;//签核备份人ID
	
	private long reviewProcess;//审核流程ID
	
	private long roleId;//审核人对应的成员Id
	@Id
	@GeneratedValue(generator = "ReviewObjectId")
	@GenericGenerator(name = "ReviewObjectId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ReviewObjectId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	@Column(name = "review_level")
	public int getReviewLevel() {
		return reviewLevel;
	}

	public void setReviewLevel(int reviewLevel) {
		this.reviewLevel = reviewLevel;
	}
	@Column(name = "review_role_id")
	public long getReviewRoleId() {
		return reviewRoleId;
	}

	public void setReviewRoleId(long reviewRoleId) {
		this.reviewRoleId = reviewRoleId;
	}
	@Column(name = "review_user_id")
	public long getReviewUserId() {
		return reviewUserId;
	}

	public void setReviewUserId(long reviewUserId) {
		this.reviewUserId = reviewUserId;
	}
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Column(name = "upate_time")
	public Timestamp getUpateTime() {
		return upateTime;
	}

	public void setUpateTime(Timestamp upateTime) {
		this.upateTime = upateTime;
	}
	@Column(name = "create_id")
	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}
	@Column(name = "backup_review_user_id")
	public long getBackupReviewUserId() {
		return backupReviewUserId;
	}

	public void setBackupReviewUserId(long backupReviewUserId) {
		this.backupReviewUserId = backupReviewUserId;
	}
	@Column(name = "review_process")
	public long getReviewProcess() {
		return reviewProcess;
	}

	public void setReviewProcess(long reviewProcess) {
		this.reviewProcess = reviewProcess;
	}
	@Transient
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
}
