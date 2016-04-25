package com.ginkgocap.tongren.organization.manage.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;

/**
 * 
 * @author yanweiqi
 *
 */
public class OrganizationRoleServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationRoleServiceTest.class);
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;

	//CreateId:128036,organizationId:3921107266830361,roleName:test-CEOdescription:test-CEO
	@Test
	public void testAddRole() {
		long createrId = 128036L;
		long organizationId = 3921107266830361L;
		String roleName = "test-CEO";
		String description = "test-CEO";
		try {
	    	 OrganizationRole or = organizationRoleService.addRole(createrId, organizationId, roleName, description); //添加
	    	 JSONObject jsonObject = JSONObject.fromObject(or);
	    	 logger.info(jsonObject.toString());
	    	 //organizationRoleService.deleteEntityById(or.getId());//清除数据
		} catch (Exception e) {
			 logger.info(e.getMessage());
			 logger.info("联系管理员");
		}
	}
	
	@Test
	public void testUpdateRole(){
		OrganizationRole or = bulidOrganizationRoleRole();
		try {
			logger.info("===================测试添加======================");
			OrganizationRole p_or = organizationRoleService.addRole(or.getCreaterId(), or.getOrganizationId(), or.getRoleName(), or.getDescription());
			assertThat(p_or.getRoleName(),equalTo(or.getRoleName()));
			logger.info("===================测试更新======================");
			String roleName = "测试角色2";
			String description = "测试角色2";
			OrganizationRole p_or_2 = organizationRoleService.updateRole(roleName, description, p_or.getId());
			assertThat(p_or_2.getRoleName(),equalTo(roleName));
			
			logger.info("==================数据清理=======================");
			organizationRoleService.deleteEntityById(p_or.getId());
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	@Test
	public void testCreateDefault() throws Exception {
		organizationRoleService.createDefault(0,0);
	}
	
	@Test
	public void testGetRoles() throws Exception{
		long organizationId = 3903677022863450L;
        List<OrganizationRole>	 roles = organizationRoleService.getOrganizationRoles(organizationId);
        Map<String, List<OrganizationRole>> map = new HashMap<String, List<OrganizationRole>>();
        map.put("responseData", roles);
        JSONArray jsonArray = JSONArray.fromObject(map);
        logger.info(jsonArray.toString());
	}

	private OrganizationRole bulidOrganizationRoleRole(){
		OrganizationRole or = new OrganizationRole();
		or.setCreaterId(128036L);
		or.setRoleName("测试闫伟旗-人力资源经理");
		long organizationId = 3900797213736965L;
		or.setOrganizationId(organizationId);
		return or;
	}
}
