package com.ginkgocap.tongren.project.manage.service;

import java.sql.Timestamp;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;

public class PublishServiceTest extends SpringContextTestCase{
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PublishService publishService;

	@Test
	public void testCreate() throws Exception {
		long projectId = 3915320469487636L;
		Project project = projectService.getEntityById(projectId);
		Timestamp t = new Timestamp(System.currentTimeMillis());
		long publisherId = project.getCreaterId();
		int status = 1;
		publishService.create(publisherId, status, project.getValidityStartTime(), project.getValidityEndTime(), project);
	}
	
	@Test
	public void testGetPagePublishByStatus() throws Exception{

		Page<Publish> page = new Page<Publish>();
		page.setIndex(1);
		page.setSize(9);
		Page<Publish> publishs =  publishService.getPagePublishByStatus(ProjectStatus.Project_Publish_Success.getKey(),page);
		for (Publish publish : publishs.getResult()) {
			JSONObject  jsonObject = JSONObject.fromObject(publish);
			logger.info(jsonObject.toString());
		}
	}
	
	@Test
	public void testGetPublishByPublishIdAndStatus() throws Exception{
		long publishId = 128036L;
		List<Publish> publishs = publishService.getPublishByPublisherIdAndStatus(publishId,ProjectStatus.Project_Publish_Success.getKey());
		JSONArray jsonArray = JSONArray.fromObject(publishs);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testgetPublishByReviewerIdAndApplyStatus() throws Exception{
		long reviewerId = 128036L;
		int applyStatus = ProjectStatus.Project_Apply_CheckPending.getKey();
		List<Publish> publishs = publishService.getPublishByReviewerIdAndApplyStatus(reviewerId, applyStatus);
		JSONArray jsonArray = JSONArray.fromObject(publishs);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testGetPublishByProjectId() throws Exception{
		long projectId = 3908370281267205L;
		Publish publish =  publishService.getPublishByProjectId(projectId);
		JSONObject jsonObject = JSONObject.fromObject(publish);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testCheckProjectIsExpire() throws Exception{
		List<Publish> publishs = publishService.checkProjectIsExpire();
		JSONArray jsonArray = JSONArray.fromObject(publishs);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testGetPublishByStatusAndProject() throws Exception{
		int status = ProjectStatus.Project_Publish_Success.getKey();
		long projectId = 3908370281267205L;
		Publish publish = publishService.getPublishByStatusAndProject(status, projectId);
		JSONObject jsonObject = JSONObject.fromObject(publish);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testUpdate() throws Exception{
		long projectId = 3908370281267205L;
		Publish publish = publishService.updatePublish(projectId, ProjectStatus.Project_Publish_Success,null);
		JSONObject jsonObject = JSONObject.fromObject(publish);
		logger.info(jsonObject.toString());
	}
	
	@Test
	public void testResubmit() throws Exception{
		long projectId = 3918083937009684L;
		String startDate = "2016-03-01 13:13:13";
		String endDate = "2016-05-01 13:13:13";
		int cycle = 15;
		Publish publish = publishService.resubmit_v3(projectId,ProjectStatus.Project_Publish_Resumit,startDate,endDate,cycle);
		JSONObject jsonObject = JSONObject.fromObject(publish);
		logger.info(jsonObject.toString());
	}

}
