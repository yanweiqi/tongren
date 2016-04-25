package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;

public class OrganizationDepServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationDepServiceTest.class);
	
	@Autowired
	private OrganizationDepService organizationDepService;
	
	@Test
	public void testCreateDefault() throws Exception {
		organizationDepService.createDefault(111111L, 3897422401241093L);
	}
	
	@Test
	public void testAdd() throws Exception{
		long pid = 3905158115491860L; //技术开发Id
		storeDep(pid);
	}

	public  void delDep(long id) {
		logger.info("==================清楚部门开始=====================");
		organizationDepService.deleteEntityById(id);
		logger.info("==================清楚部门结束=====================");
	}

	private OrganizationDep storeDep(long pid) throws Exception {
		logger.info("==================保存部门开始=====================");
		long createrId = 128036L;
		Random r = new Random();
		String depName = "闫伟旗测试部门"+r.nextInt(1000);
		String discription = "闫伟旗测试部门"+r.nextInt(2000);
		long organizationId = 3905158115491845L;
		OrganizationDep dep = organizationDepService.add(createrId, depName, discription, organizationId, pid);
		logger.info("==================保存部门结束=====================");
		return dep;
	}
	
	@Test
	public void testGetTreeDepByOrganizationId() throws Exception{
		long organizationId = 3905158115491845L;
		List<TreeNode<OrganizationDep>> treeNodes = organizationDepService.getTreeDepByOrganizationId(organizationId);
		JSONArray jsonArray = JSONArray.fromObject(treeNodes);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testGetSubTreeDepById() throws Exception{
		long organizationId = 3905158115491845L;
		long id = 3906511520923653L;
		TreeNode<OrganizationDep> treeNode = organizationDepService.getSubTreeDepById(id, organizationId);
		JSONArray jsonArray = JSONArray.fromObject(treeNode);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testDelDepById() throws Exception{
		long organizationId = 3905158115491845L;
		long id = 3906511520923653L;
		Map<String, OrganizationDep>  nodeMap = organizationDepService.delDepById(id, organizationId);
		JSONArray jsonArray = JSONArray.fromObject(nodeMap);
		logger.info(jsonArray.toString());
	}

	@Test
	public void testGetDepsByOrganizationId() throws Exception{
		long organizationId = 3921003302617093L;
		List<OrganizationDep> list = organizationDepService.getDepsByOrganizationId(organizationId);
		JSONArray jsonArray = JSONArray.fromObject(list);
		logger.info(jsonArray.toString());
	}
}
