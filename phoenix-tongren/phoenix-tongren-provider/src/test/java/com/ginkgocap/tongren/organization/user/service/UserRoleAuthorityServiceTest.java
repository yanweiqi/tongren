package com.ginkgocap.tongren.organization.user.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;

public class UserRoleAuthorityServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(UserRoleAuthorityServiceTest.class);
	
	@Autowired
	private UserRoleAuthorityService userRoleAuthorityService;
	
	@Test
	public void testGetMyOrganizationAuthorities() throws Exception {
		long userId = 111111L ;
		long organizationId = 3898638619377669L;
		Map<String,List<OrganizationAuthority>> map = userRoleAuthorityService.getMyOrganizationAuthorities(userId, organizationId);
		logger.info(map.toString());
		logger.info("+++++++++++++++++++++++++++++++++++++++");
	}

}
