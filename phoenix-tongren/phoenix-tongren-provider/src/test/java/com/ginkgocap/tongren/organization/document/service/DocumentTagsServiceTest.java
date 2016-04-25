package com.ginkgocap.tongren.organization.document.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.document.model.DocumentTags;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;

public class DocumentTagsServiceTest extends SpringContextTestCase{
	
	@Autowired
	private DocumentTagsService documentTagsService;
	/**
	 * 组织id下增加一个资源
	 * @param tags
	 * @param orgId
	 * @return
	 */
	private long userId=ConfigService.ORG_DEF_USER_ID;
	private long orgId=3916007035109386l;
	
	@Test
	public void testAddTags(){
		DocumentTags tags=new DocumentTags();
		tags.setName("饭店");
		tags.setUserId(userId);
		String code=documentTagsService.addTags(tags, orgId,1);
		logger.info("code is "+code);
		
		tags=new DocumentTags();
		tags.setName("理发");
		tags.setUserId(userId);
		code=documentTagsService.addTags(tags, orgId,1);
		logger.info("code is "+code);
		
		tags=new DocumentTags();
		tags.setName("理财产品");
		tags.setUserId(userId);
		code=documentTagsService.addTags(tags, orgId,1);
		logger.info("code is "+code);
	}
	
	@Test
	public void testgetAllTags(){
		List<DocumentTags> list=documentTagsService.getAllTags(userId, orgId);
		for(DocumentTags dt:list){
			logger.info("dt is "+dt.getName());
		}
	}
	
	@Test
	public void testAddSourceToTags(){
		String[] str="3916043944984581,3916044125339658,3916044318277647,3916048680353812,3916055277994009,3916271314010142".split(",");
		for(int i=0;i<str.length;i++){
			String code=documentTagsService.addSourceToTags(userId, 3930189939867673l, Long.parseLong(str[i]),orgId);
			logger.info("code is "+code);
		}
	}
	
	@Test
	public void addSourceToRecTags(){
		String[] str="3916043944984581,3916044125339658,3916044318277647,3916048680353812,3916055277994009,3916271314010142".split(",");
		String code=documentTagsService.addSourceToRecTags(userId, "fiscal policy", 3916043944984581l,orgId);
	}
	@Test
	public void testDelTagsById(){
		documentTagsService.delTagsById(userId, 3946079200739333l);
	}
	
	@Test
	public void testDelSourceFromTags(){
		documentTagsService.delSourceFromTags(userId, 3930162907578418l);
	}
	
	@Test
	public void testGetAllSoucesFromTags(){
		Page<BasicBean> page =new Page<BasicBean>();
		page.addParam("tagsId", 3930189939867668l);
		page.addParam("userId", 13583l);
		page=documentTagsService.getAllSoucesFromTags(page);
		String str=JSON.toJSONString(page);
		logger.info(str);
	}
	
	@Test
	public void testGetAssociateTagsBySourceId(){
		List<DocumentTags> list=documentTagsService.getAssociateTagsBySourceId(userId, 3916044318277647l, orgId);
		logger.info("doc tags is "+JSON.toJSONString(list));
	}
}
