package com.ginkgocap.tongren.organization.authority.service;

import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.authority.model.OrganizationRoleAuthority;

public class OrganizationRoleAuthorityServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationRoleAuthorityServiceTest.class);
	
	@Autowired
	private OrganizationRoleAuthorityService organizationRoleAuthorityService;
	
	@Test
	public void testCreateDefault() throws Exception {
		organizationRoleAuthorityService.createDefault(null);
	}

	@Test
	public void getAuthorityByRoleId() throws Exception{
		long roleId = 3898596542119941L;
		long organizationId = 3898596537925637L;
		List<Long> list = organizationRoleAuthorityService.getAuthorityByRoleId(roleId, organizationId);
		logger.info(list.toString());
	}
	
	@Test
	public void addRoleAuthorities() throws Exception{
		long createrId = 128036L;
		long roleId = 3912653647118340L;
		long organizationId = 3905158115491845L;
		String[] authorityNames = new String[]{"ORGANIZATION_MANAGE_LIST_SHOW","ORGANIZATION_MANAGE_SHOW"};
		List<OrganizationRoleAuthority> oras = organizationRoleAuthorityService.addRoleAuthorities(createrId, roleId, organizationId, Arrays.asList(authorityNames));
		JSONArray jsonArray = JSONArray.fromObject(oras);
		logger.info(jsonArray.toString());  
	}
	
	
}
