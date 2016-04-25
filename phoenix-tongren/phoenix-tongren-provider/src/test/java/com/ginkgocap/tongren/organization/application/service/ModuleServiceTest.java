package com.ginkgocap.tongren.organization.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationKd;
import com.ginkgocap.tongren.organization.manage.model.OrganizationPs;
import com.ginkgocap.tongren.organization.manage.service.OrganizationKnowledgeService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationPersonsimpleService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.AppKnowledgeNewsService;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;
import com.ginkgocap.ywxt.person.model.PersonSimple;
import com.ginkgocap.ywxt.person.service.PersonSimpleService;

public class ModuleServiceTest extends SpringContextTestCase {
	
	
	@Autowired
	private PersonSimpleService personSimpleService;
	
	@Autowired
	private AppKnowledgeNewsService appKnowledgeNewsService;
	
	@Autowired
	private OrganizationKnowledgeService organizationKnowledgeService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private OrganizationPersonsimpleService  organizationPersonsimpleService;
	
	@Autowired
	private CodeRegionService codeRegionService;
	
	@Autowired 
	private CodeService codeService;
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	
	

	@Test
	public void testCreateDefault() {
		long createId = 0l;
		long organizationId = 0l;
		moduleService.createDefault(createId, organizationId);
	
	}
	
	@Test
	public void testOrg(){
		moduleService.testOrgName();
	}

	@Test
	public void testGetPerson() throws Exception{
		
		List<Long> personIds = new ArrayList<Long>();
		personIds.add(13594L);
		personIds.add(13583L);
		personIds.add(13626L);
		List<PersonSimple> result = personSimpleService.findByPeIdsAndPeTypeAndCtime(personIds, 2, null);
	//	List<PersonSimple> result = personSimpleService.findByCategoryIdAndTid(13594L, null, null, null);
		for(PersonSimple p :result){
			System.out.println("name :"+p.getName1()+p.getName2()+
					"|gender :"+p.getGender() +" company :"+p.getCompany()
					+ "userPic :"+p.getPicpath());
		}
	}
	@Test
	public void testGetKnowledge() throws Exception{
		
		
		KnowledgeNews result = appKnowledgeNewsService.selectKnowledgeByKnowledgeId(13423L);
		System.out.println("title :" +result.getTitle());
		System.out.println("createTime :" +result.getCreatetime());
		System.out.println("uname :" +result.getUname());
		System.out.println("taskId :" +result.getTaskid());
		System.out.println("content :" +result.getContent());
		
	}
	@Test
	public void testSavePs() throws Exception{
		
		OrganizationPs os =  organizationPersonsimpleService.createOrganizationPs(200697L, 13594L, 3961210966114344L,null);
		System.out.println(os);
	}
	@Test
	public void testGet()throws Exception{
		OrganizationPs ps = organizationPersonsimpleService.getPsByPsIdAndOrganizationId(13583L, 3915306552786959L);
		System.out.println(ps);
	}
	@Test
	public void testGetKg()throws Exception{
		organizationKnowledgeService.getKdByKnowledgeIdAndOrgId(13423L, 12345678L);
	}
	@Test
	public void testPage()throws Exception{
		Page<OrganizationKd> page = new Page<OrganizationKd>();
		//page.addParam("paramStr", );
		page.addParam("organizationId", 12345678);
		organizationKnowledgeService.getOrganizationKdPage(page);
	}
	@Test
	public void testPage1()throws Exception{
		Page<OrganizationPs> page = new Page<OrganizationPs>();
		//page.addParam("paramStr", );
		page.addParam("organizationId", 12345678);
		page.setIndex(3);
		page.setSize(5);
		Page<OrganizationPs>  os = organizationPersonsimpleService.getOrganizationPsPage(page);
		System.out.println(com.alibaba.fastjson.JSON.toJSONString(os));
	}
	

	private void getAllSubCodeIds(List<Long> subCodeIds,long id){
		List<CodeRegion> list = codeRegionService.selectByParentId(id);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				CodeRegion cr=list.get(i);
				subCodeIds.add(cr.getId());
				getAllSubCodeIds(subCodeIds,cr.getId());
			}
		}
	}
	@Test
	public void selectAll()throws Exception{
		List<Long> subCodeIds=new ArrayList<Long>();
		subCodeIds.add(10l);
		getAllSubCodeIds(subCodeIds,10);
		Collections.sort(subCodeIds);
	//	Collections.binarySearch(subCodeIds, key);
//		for(Long id:subCodeIds){
//			System.out.println("id-->"+JSON.toJSONString(codeRegionService.selectByPrimaryKey(id)));
//		}
	}
	private void getAllSubIn(List<Long> subCodeIds,long id){
		List<Code> levelList = codeService.selectChildNodeById(id);
		if(CollectionUtils.isNotEmpty(levelList)){
			for(int i = 0;i<levelList.size();i++){
				Code code = levelList.get(i);
				subCodeIds.add(code.getId());
				getAllSubIn(subCodeIds, code.getId());
			}
		}
	}
	@Test
	public void selectAll1()throws Exception{
		List<Long> subCodeIds=new ArrayList<Long>();
		subCodeIds.add(444l);
		getAllSubIn(subCodeIds,444);
	//	Collections.sort(subCodeIds);
	//	Collections.binarySearch(subCodeIds, key);
		for(Long id:subCodeIds){
			System.out.println("id-->"+JSON.toJSONString(codeService.selectByPrimarKey(id)));
	}
	}
	
	@Test
	public void testDel()throws Exception{
		boolean  result = organizationKnowledgeService.delectOrganizationKnowledge(3954020981145605L);
		System.out.println(result);
	}
	@Test
	public void testDel1()throws Exception{
		boolean  result = organizationPersonsimpleService.delectPersonsimple(3954018665889797L);
		System.out.println(result);
	}
	@Test
	public void test2() throws Exception{
		Organization o = tongRenOrganizationService.getOrganizationById(3915306552786959L);
		JSON.toJSONString("||"+o);
	}
}
