package com.ginkgocap.tongren.organization.application.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class ModuleApplicationServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(ModuleApplicationServiceTest.class);
	
	@Autowired
	private OrganizationModuleApplicationService moduleApplicationService;
	
	@Test
	public void testCreateDefault() throws Exception {
		moduleApplicationService.createDefault(1111, 2222);
	}

}
