/**
 * 
 */
package com.ginkgocap.tongren.organization.certified.model;

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
 * 组织认证
 * @author liny
 *
 */
@Entity 
@Table(name="tb_organization_certified")
public class Certified implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//
	
	private long organizationId;//组织ID
	
	private String fullName;//组织全称
	
	private String introduction;//组织简介
	
	private int organizationType;//组织类型
	
	private String legalPerson;//法人
	
	private String legalPersonMobile;//法人手机号
	
	private String logo;//企业LOGO
	
	private String businessLicense;//营业执照复印件
	
	private String identityCard;//法人身份证复印件
	
	private String status;//认证状态 1 待认证 2 认证通过 3 认证不通过
	
//	private String businessLicenseUrl;//营业执照复印件url
//	
//	private String identityCardUrl;//法人身份证复印件 url
//	
//	private String logoUrl; //企业logoUrl
	
	private Timestamp createTime;//创建人id
	
	private Timestamp updateTime;//修改时间
	
	private Long operUserId;//操作人id

	/**
	 * @return the id
	 */
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "CertifiedId")
	@GenericGenerator(name = "CertifiedId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "CertifiedId") })
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
	 * @return the fullName
	 */
	@Column(name = "full_name")
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the introduction
	 */
	@Column(name = "introduction")
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * @param introduction the introduction to set
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * @return the organizationType
	 */
	@Column(name = "organization_type")
	public int getOrganizationType() {
		return organizationType;
	}

	/**
	 * @param organizationType the organizationType to set
	 */
	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}

	/**
	 * @return the legalPerson
	 */
	@Column(name = "legal_person")
	public String getLegalPerson() {
		return legalPerson;
	}

	/**
	 * @param legalPerson the legalPerson to set
	 */
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	/**
	 * @return the legalPersonMobile
	 */
	@Column(name = "legal_person_mobile")
	public String getLegalPersonMobile() {
		return legalPersonMobile;
	}

	/**
	 * @param legalPersonMobile the legalPersonMobile to set
	 */
	public void setLegalPersonMobile(String legalPersonMobile) {
		this.legalPersonMobile = legalPersonMobile;
	}

	/**
	 * @return the logo
	 */
	@Column(name = "logo")
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the businessLicense
	 */
	@Column(name = "business_license")
	public String getBusinessLicense() {
		return businessLicense;
	}

	/**
	 * @param businessLicense the businessLicense to set
	 */
	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	/**
	 * @return the identityCard
	 */
	@Column(name = "identity_card")
	public String getIdentityCard() {
		return identityCard;
	}

	/**
	 * @param identityCard the identityCard to set
	 */
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "oper_user_id")
	public Long getOperUserId() {
		return operUserId;
	}

	public void setOperUserId(Long operUserId) {
		this.operUserId = operUserId;
	}
	
	
}
