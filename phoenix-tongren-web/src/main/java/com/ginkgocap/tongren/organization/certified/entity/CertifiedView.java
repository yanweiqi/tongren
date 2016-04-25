package com.ginkgocap.tongren.organization.certified.entity;

import com.ginkgocap.tongren.organization.certified.model.Certified;

/**
 * 组织认证信息 
 * 增加了附件的url
 * @author hanxifa
 *
 */
public class CertifiedView extends Certified{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String businessLicenseUrl;//营业执照复印件url
	
	private String identityCardUrl;//法人身份证复印件 url
	
	private String logoUrl; //企业logoUrl

	public String getBusinessLicenseUrl() {
		return businessLicenseUrl;
	}

	public void setBusinessLicenseUrl(String businessLicenseUrl) {
		this.businessLicenseUrl = businessLicenseUrl;
	}

	public String getIdentityCardUrl() {
		return identityCardUrl;
	}

	public void setIdentityCardUrl(String identityCardUrl) {
		this.identityCardUrl = identityCardUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

}
