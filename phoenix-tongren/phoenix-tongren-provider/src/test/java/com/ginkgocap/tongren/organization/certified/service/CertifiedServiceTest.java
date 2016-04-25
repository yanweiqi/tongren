package com.ginkgocap.tongren.organization.certified.service;

import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.certified.model.Certified;

public class CertifiedServiceTest  extends SpringContextTestCase{

	@Autowired
	private CertifiedService certifiedService;
	
	@Test
	public void testAdd(){
		Certified certified=new Certified();;
		certified.setBusinessLicense("BusinessLicense_id");
		certified.setCreateTime(new Timestamp(System.currentTimeMillis()));
		certified.setFullName("组织全称");
		certified.setIdentityCard("IdentityCard");
		certified.setIntroduction("组织简介");
		certified.setLegalPerson("法人姓名");
		certified.setLegalPersonMobile("15811189392");
		certified.setLogo("logo");
		certified.setOrganizationId(3903677022863450l);
		certified.setOrganizationType(123);
		certified.setStatus("1");
		certified.setUpdateTime(certified.getCreateTime());
		certified.setOperUserId(126685l);
		String status=certifiedService.add(certified);
		System.out.println("status is "+status);
		Assert.assertSame(status.charAt(0)+"", "1");
	}
	@Test
	public void testModifyStatus(){
		String status=certifiedService.updateStatus(3904693948645381l, 2, 126685);
		System.out.println("status is "+status);
	}
	
	@Test
	public void testModify(){
		Certified certified=new Certified();;
		certified.setBusinessLicense("BusinessLicense_id");
		certified.setCreateTime(new Timestamp(System.currentTimeMillis()));
		certified.setFullName("组织全称 update");
		certified.setIdentityCard("IdentityCard");
		certified.setIntroduction("组织简介update");
		certified.setLegalPerson("法人姓名");
		certified.setLegalPersonMobile("15811189392");
		certified.setLogo("logo");
		certified.setOrganizationId(3903677022863450l);
		certified.setOrganizationType(123);
		certified.setStatus("1");
		certified.setUpdateTime(certified.getCreateTime());
		certified.setOperUserId(126685l);
		String status=certifiedService.modify(certified);
		System.out.println("status is "+status);
	}
}
