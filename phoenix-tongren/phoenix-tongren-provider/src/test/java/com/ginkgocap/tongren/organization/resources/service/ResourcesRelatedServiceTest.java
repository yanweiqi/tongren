package com.ginkgocap.tongren.organization.resources.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.resources.exception.ResourceException;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelated;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelatedDetail;
import com.ginkgocap.tongren.organization.resources.service.ResourcesRelatedService;

public class ResourcesRelatedServiceTest  extends SpringContextTestCase  {

	@Autowired
	private ResourcesRelatedService resourcesRelatedService;
	
	@Test
	public void testSaveResourcesRelated(){
		ResourcesRelated resourcesRelated=new ResourcesRelated();
		try {
			resourcesRelated.setCreateTime(new Timestamp(System.currentTimeMillis()));
			resourcesRelated.setRelatedType(1);
			resourcesRelated.setResourceId(3906224919937034l);
			resourcesRelated.setRelateName("朋友");
			resourcesRelated.setResourceType(1);
			resourcesRelated.setUserId(13594);
			
			ResourcesRelatedDetail detail=new ResourcesRelatedDetail();
			detail.setRelatedId(142375665021900013l);
			detail.setRelatedType(1);
			detail.setSubType(1);//1金桐脑推荐的，我的
			
			ResourcesRelatedDetail detail2=new ResourcesRelatedDetail();
			detail2.setRelatedId(21352l);
			detail2.setRelatedType(1);
			detail2.setSubType(2);//1金桐脑推荐的，我的
			
			List<ResourcesRelatedDetail> dlist=new ArrayList<ResourcesRelatedDetail>();
			dlist.add(detail);
			dlist.add(detail2);
			resourcesRelated.setDetail(dlist);
			resourcesRelatedService.saveResourcesRelatedInfo(resourcesRelated);
		} catch (ResourceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetResourcesRelatedByResourceId(){
		try {
			List<ResourcesRelated> list=resourcesRelatedService.getResourcesRelatedByResourceId(3906224919937034l,1,true);
			logger.info("json str is: ");
			logger.info(JSON.toJSONString(list));
		} catch (ResourceException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDelResourcesRelated(){
		try {
			resourcesRelatedService.delResourcesRelated(3947903567790084l);
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
