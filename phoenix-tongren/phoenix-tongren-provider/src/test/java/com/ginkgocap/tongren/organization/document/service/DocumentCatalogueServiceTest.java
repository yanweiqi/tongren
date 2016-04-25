package com.ginkgocap.tongren.organization.document.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;

public class DocumentCatalogueServiceTest  extends SpringContextTestCase{
	private static final Logger logger = LoggerFactory.getLogger(DocumentCatalogueServiceTest.class);
	
	@Autowired
	private DocumentCatalogueService documentCatalogueService;
	
	/**
	 *  测试增加根节点 
	 */
	private long userId=13594l;
	private long orgId=3916041902358538l;
 
	/**
	 * 获取根节点，不存在，则增加
	 */
	@Test
	public void testGetRootCatalogs(){
		DocumentCatalogue dc=documentCatalogueService.getRootCatalogs(userId, orgId);
		logger.info("dc -->"+JSON.toJSONString(dc));
	}
	
	/**
	 * 测试增加叶子结点
	 * 3929026049867791
	 */
	@Test
	public void testAddSubCatalog(){
		DocumentCatalogue catalogue=new DocumentCatalogue();
		catalogue.setName("项目文档");
		catalogue.setOrganizationId(orgId);
		catalogue.setUserId(userId);
		catalogue.setPid(3929036481101884l);
		String code=documentCatalogueService.addCatalog(catalogue);
		logger.info("code is "+code);
	}
	
	/**
	 * 获取所有子目录
	 */
	@Test
	public void testgetCatalogsById(){
		List<DocumentCatalogue> list=documentCatalogueService.getCatalogsById(userId, 3946073676840965l);
		for(DocumentCatalogue dc:list){
			logger.info("sub dir is:"+dc.getName());
		}
	}
	

	/**
	 * 获取一个目录下的所有资源
	 */
	@Test
	public void testGetDocumentsById() {
		List<BasicBean> list=documentCatalogueService.getDocumentsById(userId, 3946073676840970l);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				try {
					logger.info(BeanUtils.getProperty(list.get(i), "taskId")+"--->"+list.get(i).getExtend());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Test
	public void testUpdateCatalogueName(){
		String code=documentCatalogueService.updateCatalogueName(userId, 3961316821958661l, "未分组");
		logger.info("code is "+code);
	}
	
	@Test
	public void testSetDocumentToDirs(){
		String code=documentCatalogueService.setDocumentToDirs(userId, orgId, 3918573496172549l, new long[]{3946074041745423l});
		logger.info("code is "+code);
	}
	
	@Test
	public void testGetCatalogueTree(){
		DocumentCatalogue dc=documentCatalogueService.getCatalogueTree(userId, orgId, 3918573496172549l);
		logger.info("dc is "+	JSON.toJSONString(dc));
	}
	
	@Test
	public void testDeleteResourceOfCatalogue(){
		String code=documentCatalogueService.deleteResourceOfCatalogue(userId, 3946077191667732l);
		logger.info("code is "+code);
	}
	
	//3929037072498758
	
	@Test
	public void testDeleteCatalogue(){
		String code =documentCatalogueService.deleteCatalogue(userId, 3946074041745423l);
		logger.info("code-->"+code);
	}
	
	@Test
	public void testDelResourceOfCatalog(){
		documentCatalogueService.delResourceOfCatalog(3918573496172549l,orgId,userId);
	}
}
