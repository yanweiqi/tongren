package com.ginkgocap.tongren.project.service.task;

import java.sql.Timestamp;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;

public class AssignTaskServiceTest extends SpringContextTestCase{
	
	@Autowired
	private AssignTaskService assignTaskService;
	
	


	
	@Test
	public void testSave(){
		
		long[] userIds = {1l,2l,3l};
		
		AssignTask  at = new AssignTask();
		at.setProjectTaskId(3908038784450565l);
		at.setAssignerId(200822);
		at.setOrganizationId(3903677022863450l);
		try {
			String status=assignTaskService.assignTask(at, userIds);
			System.out.println("status is "+status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testupdate() throws Exception{
		
		long[] userIds = {1l,2l,13l};
		AssignTask  at = new AssignTask();
		at.setProjectTaskId(3908038784450565l);
		at.setAssignerId(200822);
		at.setOrganizationId(3903677022863450l);
		String status=assignTaskService.assignTask(at, userIds);
		System.out.println("status is "+status);
		
	}
	
	@Test
	public void testAssignSub(){
		
		long[] userIds = {5l};
		
		AssignTask  at = new AssignTask();
		at.setProjectTaskId(3908038784450565l);
		at.setAssignerId(1l);
		at.setOrganizationId(3903677022863450l);
		try {
			String status=assignTaskService.assignTask(at, userIds);
			System.out.println("status is "+status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test 
	public void testSelect() throws Exception{
		List<AssignTask>  lst = assignTaskService.selectAssignTaskByTaskId(111111);
		for(AssignTask st:lst){
			System.out.println("****"+st.getId());
		}
	}
	@Test
	public void testGetName()throws Exception{
		
		StringBuilder sb = assignTaskService.getPerformerName(1111);
		System.out.println("|||||||"+sb);
	}
	@Test
	public void testCreateOrganizationTask() throws Exception{
		
		AssignTask at = new AssignTask();
		at.setAssignerId(13583);
		at.setOrganizationId(3908035437395978L);
		at.setPerformerId(Long.valueOf(11));
		at.setProjectTaskId(3908307773554693L);
		at.setAssignTime(new Timestamp(System.currentTimeMillis()));
		
//		String st = assignTaskService.createOrganizationAssignTask(at);
//		System.out.println("***"+st);
	}
	
	
}
