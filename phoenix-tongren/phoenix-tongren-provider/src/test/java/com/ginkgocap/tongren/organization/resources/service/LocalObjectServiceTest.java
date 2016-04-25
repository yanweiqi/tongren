/*
 * 文件名： LocalObjectServiceTest.java
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
import com.ginkgocap.tongren.organization.resources.model.LocalObject;


 /**
 *  本地资源接口测试 
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月30日
 */
public class LocalObjectServiceTest extends SpringContextTestCase{
	
	private static Logger logger = LoggerFactory.getLogger(LocalObjectServiceTest.class);
	
	@Autowired
	private LocalObjectService localObjectService;
	
	@Test
	public void doSaveLocalObject(){
		logger.info("exit doSaveLocalObject method");
		LocalObject lo = new LocalObject();
		
		lo.setCreateId(1111);
		lo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		lo.setId(111);
		lo.setOrganizationId(2222);
		lo.setProjectId(3333);
		lo.setTaskId("1111111111111111111111111");
		
		localObjectService.save(lo);
		logger.info("doSaveLocalObject method end");
		
	}
}
