/*
 * 文件名： ResourcesServiceTest.java
 * 创建日期： 2015年10月30日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.resources.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.resources.service.ResourcesService;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月30日
 */
public class ResourcesServiceTest extends SpringContextTestCase {

	private static Logger logger = LoggerFactory.getLogger(ResourcesServiceTest.class);
	@Autowired
	private ResourcesService resourcesService;
	public void addR(){
		logger.info("exit doSaveLocalObject method");
		LocalObject lo = new LocalObject();
		
		lo.setCreateId(1111);
		lo.setCreateTime(new Timestamp(System.currentTimeMillis()));
		lo.setId(111);
		lo.setOrganizationId(2222);
		lo.setProjectId(3333);
		lo.setTaskId("1111111111111111111111111");
		try {
			resourcesService.addResourcesLocalAndOrg(lo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getResourcesByOrgPro(){
		try {
			List<OrganizationObject> orgLis = resourcesService.getOrgObject(3903677022863450L, 3896043024678917L);//组织资源
			System.out.println(orgLis.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void delResources(){
		int type = 2;
		List<Long> ids = new ArrayList<Long>();
		ids.add(3905095683276890L);
		ids.add(3905096782184543L);
		boolean isSucess = false;
		try {
			isSucess = resourcesService.deleteResources(ids, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(isSucess);
	}
}
