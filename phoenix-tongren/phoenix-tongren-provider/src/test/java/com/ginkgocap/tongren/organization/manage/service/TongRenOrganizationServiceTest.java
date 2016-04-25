package com.ginkgocap.tongren.organization.manage.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationVO;
import com.ginkgocap.tongren.organization.manage.vo.RecommendVO;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;

public class TongRenOrganizationServiceTest  extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationRoleServiceTest.class);
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Autowired
	private FriendsRelationService friendsRelationService;
	
	@Test
	public void testCreate() throws Exception {
		/***/
		Map<Integer,String[]> maps = new HashMap<Integer,String[]>();
		String[] a  ={"10","12","13"};
		maps.put(3, a);//好友推荐
		
		Organization o = new Organization();
		o.setName("仇伟刚测试1217_01");
		o.setIntroduction("仇伟刚测试1217_01");
		o.setClassification(43);
		o.setIndustry(441);
		o.setArea(1881);
		o.setCreaterId(111111);
		o.setCreateTime(new Timestamp(System.currentTimeMillis()));
		o.setLogo("TVRRME56SXhNRGcyTXpBMU1EQXdNRFV3TmpNNE9UY3g=");
		Organization s  = tongRenOrganizationService.create(o);
		organizationMemberService.invite(3899738856620077L, maps, "欢迎来到金桐大家庭");
//		List<User> lst= friendsRelationService.findAllFriendsByNameAndChar(1,null,null,-1,17,8);
//		for(User u:lst){
//			System.out.println("****"+u.getName());
//		}
//		List<User> lst= friendsRelationService.findAllFriendsByNameAndChar(200820,null,null,-1,Integer.valueOf(1),Integer.valueOf(8));
//		System.out.println(lst);
		
	}
	
	@Test
	public void testGetMyJoinOrganization() throws Exception {
		long userId = 13594 ;
		List<OrganizationVO> lst = tongRenOrganizationService.getMyJoinOrganization(userId);
		for(OrganizationVO vo:lst){
			System.out.println("success project ---->"+vo.getPerformSize());
		}
	}

	@Test
	public void testGetMyCreateOrganizationIds() throws Exception {
		long createrId = 13594;
		int status = 0;
		long t1 = System.currentTimeMillis();
		List<OrganizationVO> organizations = tongRenOrganizationService.getMyCreateOrganizations(createrId, status,true);
		long t2= System.currentTimeMillis() -t1;
		System.out.println("getMyCreateOrganizations..."+t2);
		
//		t1 = System.currentTimeMillis();
//		organizations = tongRenOrganizationService.getMyCreateOrganizations(createrId, status);
//		t2= System.currentTimeMillis() -t1;
//		System.out.println("getMyCreateOrganizations2..."+t2);
//		
	    logger.info(organizations.toString());
	}
	@Test
	public void testGetOrganizationById() throws Exception{
		long id = 3905029845286917L ;
		Organization o = tongRenOrganizationService.getOrganizationById(id);
		System.out.println(o);
	}
	@Test
	public void testQueryOrganizationPage() throws Exception{
		
		Page<RecommendVO> page=new Page<RecommendVO>();
		page.setIndex(0);
		page.setSize(8);
		page.addParam("status", "0");
		Page<RecommendVO> result = tongRenOrganizationService.queryOrganizationPage(page);
		List<RecommendVO> list = result.getResult();
		System.out.println("****"+list.size());
	}
	@Test
	public void testGetOrganizationByName()throws Exception {
		
		String name = "仇伟刚测试";
		List<RecommendVO> list =  tongRenOrganizationService.getOrganizationByName(name);
		System.out.println(list);
	}
	@Test
	public void testGetOrganizationListByType() throws Exception{
		
		Page<OrganizationVO> page = new Page<OrganizationVO>();
		page.addParam("type", 1);
		page.setIndex(1);
		page.setSize(10);
		Page<OrganizationVO> result = tongRenOrganizationService.getOrganizationListByType(page);
		for(OrganizationVO vo :result.getResult()){
			System.out.println("******"+vo.getId());
		}
	}
	
	
	@Test
	public void testUpdateAllNameSpell() throws Exception{
		
		tongRenOrganizationService.updateAllNameSpell();
	}
	
	@Test
	public void testQueryOranizationByNameOrSpell() throws Exception{
		
		 List<Long> ids=tongRenOrganizationService.queryOranizationByNameOrSpell("吴");
		 for(long id:ids){
			 Organization org= tongRenOrganizationService.getEntityById(id);
			 logger.info("id: "+id+" -->"+org.getNameSpelling()+","+org.getName());
		 }
	}
	@Test
	public void testCC()throws Exception{
		long organizationId = 3920264773763082L;
		long receiveId = 13594;
		
		String result = tongRenOrganizationService.creatorTurnOver(organizationId, receiveId);
		System.out.println(result);
		
	}
	@Test
	public void testSSD()throws Exception{
		long userId = 13594l;
		Map<String,Object>responseData =  new HashMap<String,Object>();
		tongRenOrganizationService.getMenuSize(userId, responseData);
		System.out.println(responseData);
	}
}
