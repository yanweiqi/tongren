package com.ginkgocap.tongren.organization.manage.service;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;

public class OrganizationMemberRoleServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationMemberRoleServiceTest.class);
	
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;

	@Test
	public void testAddMemberRole() throws Exception {
		long createrId = 3898350760099850L;
		long memberId = 3898322612125701L;
		OrganizationMember om = organizationMemberService.getEntityById(memberId);
		OrganizationMemberRole omr = organizationMemberRoleService.addMemberRole(om, createrId);
		JSONObject jsonObject = JSONObject.fromObject(omr);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testAddMemberRole2() throws Exception{
		long createrId = 13594L;
		long organizationId = 3915313980899348l;
		long roleId= 3915313980899413l;
        long organizationMemberId = 3915313985093757l;
		OrganizationMemberRole omr = organizationMemberRoleService.addMemberRole(createrId, organizationId, roleId, organizationMemberId);
		JSONObject jsonObject = JSONObject.fromObject(omr);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testdeleteAdminRole(){
		try {
			organizationMemberRoleService.deleteAdminRole(3915313980899348l, 3915313985093757l);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
