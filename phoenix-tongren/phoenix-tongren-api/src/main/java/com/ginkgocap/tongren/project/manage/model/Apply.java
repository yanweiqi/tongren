package com.ginkgocap.tongren.project.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.ywxt.user.model.User;


/**
 * 项目申请
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_project_apply")
public class Apply implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	private long proposerId; //申请人 user_id
	
	private long organizationId; //申请人所属组织ID
	
	private Timestamp  applyTime; //申请时间
	
	private long projectId; //申请项目ID
	
	private int status; //0申请失败、1审核中、2申请成功
	
	private Timestamp  reviewTime; //审核日期
	
	private long reviewerId; //审核人Id
	
	private Timestamp  completedTime; //完成时间
	
	private OrganizationMember  organizationMember;
	
	private User user;
	
	private String organizationName;

	@Id
	@GeneratedValue(generator = "ApplyId")
	@GenericGenerator(name = "ApplyId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "ApplyId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
    
	@Column(name="proposer_id")
	public long getProposerId() {
		return proposerId;
	}

	public void setProposerId(long proposerId) {
		this.proposerId = proposerId;
	}

	@Column(name="apply_time")
	public Timestamp getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name="review_time")
	public Timestamp getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Timestamp reviewTime) {
		this.reviewTime = reviewTime;
	}

	@Column(name="organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name="project_id")
	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	@Column(name="reviewer_Id")
	public long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(long reviewerId) {
		this.reviewerId = reviewerId;
	}

	@Transient
	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(int days,Timestamp reviewTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate = new Date(reviewTime.getTime());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		Date endDate = calendar.getTime();
		this.completedTime = Timestamp.valueOf(sdf.format(endDate));
	}

	@Transient
	public OrganizationMember getOrganizationMember() {
		return organizationMember;
	}

	public void setOrganizationMember(OrganizationMember organizationMember) {
		this.organizationMember = organizationMember;
	}

	@Transient
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
}
