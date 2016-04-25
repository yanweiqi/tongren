package com.ginkgocap.tongren.organization.manage.service;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.manage.exception.DepMemberException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;


public class OrganizationDepMemberServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationDepMemberServiceTest.class);
	
	@Autowired
	private OrganizationDepMemberService organizationDepMemberService;
	
	@Autowired
	private OrganizationDepService organizationDepService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;

	@Test
	public void testAdd() throws Exception {
	    /**
		OrganizationDep od = organizationDepService.getEntityById(3897527644717076L);
		OrganizationMember om = organizationMemberService.getEntityById(3897948194996229L);
		organizationDepMemberService.add(om, od);
		*/
		//organizationDepMemberService.add(createrId, organizationId, depId, organizationMemberId);
	}
	
	@Test
	public void testGetDepsByOrganizationId() throws Exception{
		long organizationId = 3903677022863450L;
	    List<OrganizationDep> organizationDeps = organizationDepService.getDepsByOrganizationId(organizationId);
	    assertThat(0,lessThan(organizationDeps.size()));
	    for (OrganizationDep dep : organizationDeps) {
			logger.info(dep.toString());
		}
	}
	
	@Test
	public void testDelDepMemberByDepId() throws Exception{
		long  organizationId = 3905158115491845L;
		long  depId = 3905158115491860L;
		logger.info("=========================删除部门成员开始==================================");
		boolean status = organizationDepMemberService.delDepMemberByDepId(organizationId, depId);
		logger.info("delete status:"+String.valueOf(status));
		logger.info("=========================删除部门成员开始==================================");
	}
	
	@Test
	public void testGetDepMemberByDepId() throws Exception{
		long  organizationId = 3915314375163929L;
		long  depId = 3915314375163999L;
		/**
		logger.info("=========================添加部门成员开始==================================");
		long  organizationId = 3905158115491845L;
		long  depId = 3905158115491860L;
		long  organizationMemberId= 9999999L;
		long  createrId = 128036L;
		OrganizationDepMember depMember = organizationDepMemberService.add(createrId, organizationId, depId, organizationMemberId);
		logger.info("=========================添加部门成员结束==================================");	
		logger.info(depMember.toString()+","+ String.valueOf(null != depMember));
		*/
		
		List<OrganizationDepMember> depMembers = organizationDepMemberService.getDepMemberByOrganizationIdAndDepId(organizationId, depId);
		for (OrganizationDepMember depMember_2 : depMembers) {
			logger.info(depMember_2.toString());
		}
	}
	
	@Test
	public void testGetDepMemberByOrganizationIdAndDepIdAndMemberId() throws DepMemberException, Exception{
		long organizationId = 3913739569201162L;
		long depId = 3913739569201182L;
		long organizationMemberId = 3913739573395486L;
		OrganizationDepMember odm = organizationDepMemberService.getDepMemberByOrganizationIdAndDepIdAndMemberId(organizationId, depId, organizationMemberId);
		JSONObject jsonObject = JSONObject.fromObject(odm);
		logger.info(jsonObject.toString());
	}

}
