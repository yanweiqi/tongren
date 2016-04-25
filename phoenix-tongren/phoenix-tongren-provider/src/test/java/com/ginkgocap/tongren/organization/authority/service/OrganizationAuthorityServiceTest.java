package com.ginkgocap.tongren.organization.authority.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;

public class OrganizationAuthorityServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationAuthorityServiceTest.class);

	@Autowired
	private OrganizationAuthorityService organizationAuthorityService;
	
	/**
	 * 桐人初始化权限
	 * @throws Exception 
	 */
	@Test
	public void testCreateDefault() throws Exception {
		long createId = 0l; //桐人默认创建者ID
		long organizationId = 0l;//桐人默认组织ID
		organizationAuthorityService.createDefault(createId, organizationId);
	}

	@Test
	public void testGetOrganizationAuthority(){
	   long id = organizationAuthorityService.getMappingByParams("organizationAuthority_map_authorityName", OrganizationAuthorities.ORGANIZATION_MANAGE.name());
	   logger.info(String.valueOf(id));
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testGetDefaultAuthoritysByAuthorityNo() throws Exception{
		List<OrganizationAuthority> list = organizationAuthorityService.getDefaultAuthoritys();
		for (OrganizationAuthority organizationAuthority : list) {
			logger.info(organizationAuthority.getAuthorityName()+","+organizationAuthority.getAuthorityNo());
		}
	}
	
	@Test
	public void testGetAuthorityTreeNodes() throws Exception{
		List<TreeNode<OrganizationAuthority>> treeNodes = organizationAuthorityService.getAuthorityTreeNodes();
		JSONArray array = JSONArray.fromObject(treeNodes); 
		logger.info(array.toString());
	}
	
	@Test
	public void testGetChildrenTreeNodeById() throws Exception{
		long authorityId = 3898227065880636L;
		TreeNode<OrganizationAuthority> treeNode = organizationAuthorityService.getChildrenTreeNodeById(authorityId);
		JSONObject jsonObject = JSONObject.fromObject(treeNode);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testGetOrganizationAuthorityById() throws Exception{
		long authorityId = 3898227065880636L;
		Map<String,OrganizationAuthority> map = organizationAuthorityService.getOrganizationAuthorityMapByAuthorityId(authorityId);
		JSONArray jsonObject = JSONArray.fromObject(map);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testGetChildrenTreeNodeByName() throws Exception{
		String authorityName = "ORGANIZATION_MANAGE_RESOURCE";
		TreeNode<OrganizationAuthority> treeNode = organizationAuthorityService.getChildrenTreeNodeByName(authorityName);
		JSONObject jsonObject = JSONObject.fromObject(treeNode);
		logger.info(jsonObject.toString());
		Map<String, OrganizationAuthority> map =  new LinkedHashMap<String, OrganizationAuthority>();
	    treeNode.convertTreeNodeToMap(treeNode,map);
		logger.info((JSONObject.fromObject(map)).toString());
	}
}