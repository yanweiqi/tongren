/*
 * 文件名： OrganizationObjectServiceTest.java
 * 创建日期： 2015年10月30日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.resources.service;

import java.sql.Timestamp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月30日
 */
public class OrganizationObjectServiceTest extends SpringContextTestCase{

private static Logger logger = LoggerFactory.getLogger(LocalObjectServiceTest.class);
	
	@Autowired
	private OrganizationObjectService organizationObjectService;
	
	@Test
	public void doSaveOrganizationObject(){
		logger.info("exit doSaveLocalObject method");
		OrganizationObject lo = new OrganizationObject();
		
		lo.setCreateId(1111);
		lo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		lo.setOrganizationId(2222);
		lo.setProjectId(3333);
		lo.setTaskId("1111111111111111111111111");
		
		organizationObjectService.save(lo);
		logger.info("doSaveLocalObject method end");
		
	}
}
