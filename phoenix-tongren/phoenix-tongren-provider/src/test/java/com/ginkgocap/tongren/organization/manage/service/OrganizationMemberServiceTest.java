package com.ginkgocap.tongren.organization.manage.service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationMemberVO;
import com.ginkgocap.tongren.organization.message.service.MessageReceiveService;

public class OrganizationMemberServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationMemberServiceTest.class);
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	@Autowired
	private MessageReceiveService messageReceiveService;
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMyOrganizationMembers(){
		long userId = 128036L;
		List<OrganizationMember> members = organizationMemberService.getMyOrganizationMembers(userId);
		if(null != members){
			JSONArray jsonArray = JSONArray.fromObject(members);
			logger.info(jsonArray.toString());
		}
	}
	
	@Test
	public void testOrganizationInfo(){
		long organizationId = 3923591674724362L;
		int status = 1;
		List<OrganizationMemberVO> s = organizationMemberService.getOrganizationAllMemberInfo(organizationId,status);
		JSONArray jsonArray = JSONArray.fromObject(s);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testExit() throws Exception{
		String s =  organizationMemberService.exit(3899738856620077L, 10);
		System.out.println(s);
	}
	@Test
	public void testSignOff() throws Exception{
		boolean  flag = organizationMemberService.delMember(3905029794955769L, 13594);
		System.out.println("************"+flag);
	}
	
	@Test
	public void testInvitor() throws Exception{
		long organizationId = 3905158115491845L;
		Map<Integer,String[]> maps = new HashMap<Integer,String[]>();
		String[] a  ={"10","12","13"};
		maps.put(3, a);
		organizationMemberService.invite(organizationId, maps, "test闫伟旗欢迎来到金桐大家庭");
	}
	
	@Test
	public void testDel() throws Exception{
		long organizationId = 3905158115491845L;
		long userId = 10L;
		organizationMemberService.delMember(organizationId, userId);
	}
	
	@Test
	public void testGetMyOrganizationMemberDetail() throws Exception{
		long organizationId = 3905158115491845L;
		long userId = 128036L;
		OrganizationMember om = organizationMemberService.getMyOrganizationMemberDetail(organizationId, userId);
		JSONObject jsonObject = JSONObject.fromObject(om);
		logger.info(jsonObject.toString());
	}
	@Test
	public void testGK() throws Exception{
		long organizationId = 3910172804382729L;
		List<OrganizationMember> list = organizationMemberService.getNormalMember(organizationId);
		System.out.println(list.size());
	}
	@Test
	public void testUpdateTime ()throws Exception{
		messageReceiveService.updateReceiveTime(13583, 3915313980899348L, 3);
	}
	@Test
	public void testAdd()throws Exception{
		OrganizationMember o = organizationMemberService.addMember(13363, 3915553429520449L, 13583, 0, 2);
		System.out.println("|||"+o.getId());
	}
	@Test
	public void testAll()throws Exception {
		
		List<OrganizationMemberVO>  lst = organizationMemberService.getOrganizationMemberAll(3915553429520449L);
		System.out.println("size ==" +lst.size());
		
		
	}
	
}
