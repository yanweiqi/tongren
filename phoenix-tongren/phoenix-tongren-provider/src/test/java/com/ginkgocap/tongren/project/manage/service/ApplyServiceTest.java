package com.ginkgocap.tongren.project.manage.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;

public class ApplyServiceTest extends SpringContextTestCase{
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ApplyService applyService;
	
	@Test
	public void testAccept() throws Exception {
		long projectId = 3908370281267205L;
		long reviewerId = 128036L;
		long proposerId = 128036L;
		long organizationId = 0L;
		Map<String, Object> acceptMap = applyService.accept(reviewerId, projectId, proposerId,organizationId);
		Apply apply = (Apply) acceptMap.get(CommonConstants.ACCEPT_SUCCESS);
		assertThat(ProjectStatus.Project_Apply_Success.getKey(),equalTo(apply.getStatus()));
		JSONArray jsonArray = JSONArray.fromObject(acceptMap);
	    logger.info(jsonArray.toArray());
	}

	@Test
	public void testRefuse() {
		
	}

	@Test
	public void testGetMyApplysByProjectId() throws Exception {
		long projectId = 3908028076392453L;
		long reviewerId = 128036L;
		List<Apply> applies = applyService.getMyApplysByProjectId(reviewerId, projectId, ProjectStatus.Project_Apply_CheckPending.getKey());
		for (Apply apply : applies) {
			 JSONObject jsonObject = JSONObject.fromObject(apply);
			 logger.info(jsonObject);
		}
	}

	@Test
	public void testGetMyApplyByProjectIdAndProposerId() {
	}

	/**
	 * 申请项目测试
	 * @throws Exception
	 */
	@Test
	public void testCreate() throws Exception {
		long proposerId = 13594L;
		long organizationId = 0L;
		Timestamp applyTime = new Timestamp(System.currentTimeMillis());
		long projectId = 3915558315884574L;
		long reviewerId = 13594;
		Apply apply = applyService.create(proposerId, organizationId, applyTime, projectId, reviewerId);
		JSONObject jsonObject = JSONObject.fromObject(apply);
		logger.info(jsonObject.toString());
	}
	
	/**
	 * 获取申请者申请的项目列表
	 * @throws Exception 
	 */
	@Test
	public void testGetAppliesByProposerIdAndStatus() throws Exception{
		long proposerId = 128036L;
		Integer status = 1;
		List<Apply> applies = applyService.getAppliesByProposerIdAndStatus(proposerId, status);
		JSONArray jsonArray = JSONArray.fromObject(applies);
		logger.info(jsonArray.toString());
	}
	
}
