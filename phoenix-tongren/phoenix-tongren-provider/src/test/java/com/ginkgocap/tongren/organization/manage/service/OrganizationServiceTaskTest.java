package com.ginkgocap.tongren.organization.manage.service;

import java.sql.Timestamp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;

public class OrganizationServiceTaskTest  extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(OrganizationRoleServiceTest.class);
	
	@Autowired
	private ProjectTaskService projectTaskService;
	@Test
	public void testGetOrganizationById() throws Exception{
		
		Task task = new Task();
		task.setTitle("测试组织任务1214_2");
		task.setStartTime(new Timestamp(System.currentTimeMillis()));
		task.setEndTime(new Timestamp(System.currentTimeMillis()+60*1000*2));
		task.setTaskDescription("测试组织任务1214_2");
		task.setOrganizationId(3910172804382729L);
		String so  = projectTaskService.createOrganizationTask(task, 11);
		System.out.println("*****"+so);
		so = so.substring(2);
		System.out.println("*****"+so);
	}

}
