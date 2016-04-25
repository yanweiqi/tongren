package com.ginkgocap.tongren.project.service.task;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.organization.document.service.DocumentCatalogueService;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.model.TaskVO;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;

public class ProjectTaskServiceTest extends SpringContextTestCase{

	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private FileIndexService fileIndexService;
	
	@Autowired
	private DocumentCatalogueService documentCatalogueService;
	
	
	@Test
	public void testAddPrimaryTask(){
		Task task =new Task();
		task.setCreateId(200822l);//创建人
		
		task.setStartTime(new Timestamp(System.currentTimeMillis()));
		task.setCycle(60);//项目周期 单位: 天
		task.setOrganizationId(3903677022863450l);//组织id
		task.setProjectUndertakenId(123232332l);//项目id
		task.setTaskDescription("测试组织任务1214_2");
		task.setTitle("测试组织任务1214_2");
		task.setTaskStatus(1);//任务状态，由于页面没有设置开始时间，主任务创建时视为开始
		String status=projectTaskService.addPrimaryTask(task);
		//1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 不存在对应的主任务 5缺少信息
		System.out.println("status is "+status);
	}
	
	/**
	 * 增加子任务
	 */
	@Test
	public void testAddTask(){
		Task task =new Task();
		task.setCreateId(200822l);
		Calendar current=Calendar.getInstance();
		
		task.setCreateTime(new Timestamp(current.getTimeInMillis()));
		//task.setStartTime(new Timestamp(current.getTimeInMillis()));
		//task.setCycle(5);
		//current.add(Calendar.DAY_OF_MONTH, 5);
		//task.setEndTime(new Timestamp(current.getTimeInMillis()));
		task.setOrganizationId(3903677022863450l);
		//task.setProgress(0);
		task.setTaskPid(-1);
		task.setProjectUndertakenId(3908028080586867l);
		task.setTaskDescription("编写接口文档");
		task.setTitle("编写接口文档");
		task.setTaskStatus(0);
		String status=projectTaskService.addTask(task);
		System.out.println("status is "+status);
	}
	
	@Test
	public void testUpdateStartAndEndTime(){
		Task task =new Task();
		task.setCreateId(200822l);
		Calendar current=Calendar.getInstance();
		task.setId(3907933817798661l);
		task.setStartTime(new Timestamp(current.getTimeInMillis()));
		//current.add(Calendar.DAY_OF_MONTH, 5);
		task.setEndTime(new Timestamp(DateUtil.addDay(current.getTime(), 5).getTime()));
		String status;
		try {
			status = projectTaskService.updateTask(task);
			System.out.println("status is "+status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateTitle(){
		Task task =new Task();
		task.setCreateId(200822l);
		task.setId(3908027384332293l);
		task.setTitle("编写接口文档 testupdate");
		String status;
		try {
			status = projectTaskService.updateTask(task);
			System.out.println("status is "+status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testBatchUpdateStatus(){
		projectTaskService.batchUpdateStatus();
	}
	@Test
	public void testGetAllTasksByProjectId(){
		List<Task> list=projectTaskService.getAllTasksByProjectId(123232332l);
		for(Task t:list)
		{
			System.out.println(t.getTitle()+","+t.getTaskStatus()+",pid:"+t.getPid()+",start:"+DateUtil.formatDate(t.getStartTime())+",end:"+DateUtil.formatDate(t.getEndTime()));
		}
	}
	
	@Test
	public void testUpdateStatus() throws Exception{
		String status=projectTaskService.updateOrganizationTaskStatus(3948593656627210L, 13594L,2);
		System.out.println("status1 is "+status);
		
		//status=projectTaskService.updateStatus(3907595144527877l, 1,200822l);
		//System.out.println("status2 is "+status);
		
	}
	
	@Test
	public void testDelete() {
		String status = projectTaskService.delete(3907940260249605l, 200822l);
		System.out.println("status is:" + status);
		status = projectTaskService.delete(3956202660954117L, 13594l);
		System.out.println("status is:" + status);
	}
	@Test
	public void testTital(){
			List<FileIndex> list=fileIndexService.selectByTaskId("MTQzNTAyNzQzOTcyMDAwMDAzMTg2MTEzMzg3ODYwMjE0Mzg=", "1");
			String path ="";
			if(list!=null&&list.size()>0){
				FileIndex s = (FileIndex) list.get(0);path = s.getFilePath() == null ? null :"http://file.online.gintong.com/tongren"
								+ s.getFilePath()+s.getFileTitle();
			}
			System.out.println(path);
	}
	
	@Test
	public void testGetTasks(){
		Map<String, String> params =new HashMap<String, String>();
		params.put("userId", "13583");
		params.put("type", "0");
		params.put("organizationId", "3915313980899348");
		long t1=System.currentTimeMillis();
		List<TaskVO> ts=projectTaskService.getMyTaskList(System.currentTimeMillis(),4, params);
	
		ts=projectTaskService.getMyTaskList(System.currentTimeMillis(),4, params);
		System.out.println("task count is :"+ts.size()+"cost "+(System.currentTimeMillis()-t1));
		for(TaskVO t:ts){
			System.out.println("task detail is :"+t.getCreateUserName()+","+t.getPerformUserName()+","+t.getRtype()+","+t.getTask());
		}
		
	}
	
	@Test
	public void testGetOrgTaskList(){
		Map<String, String> params =new HashMap<String, String>();
		params.put("orgId", "3916041902358538");
		params.put("type", "-1");
		long t1=System.currentTimeMillis();
		List<Task> ts=projectTaskService.getOrgTaskList(System.currentTimeMillis(),20, params);
		System.out.println("task count is :"+ts.size()+"cost "+(System.currentTimeMillis()-t1));
		t1=System.currentTimeMillis();
		ts=projectTaskService.getOrgTaskList(System.currentTimeMillis(),20, params);
		System.out.println("task count is :"+ts.size()+"cost "+(System.currentTimeMillis()-t1));
		for(Task t:ts){
			System.out.println("task detail is :"+t);
		}
		
	}
	@Test
	public void testGetOrgTaskCount(){
		int count=projectTaskService.getOrgTaskCount(3915202903146521l);
		System.out.println("org task count is "+count);
	}
	@Test
	public void testGetPrimaryTaskByProjectId() throws IOException{
		Task task=projectTaskService.getPrimaryTaskByProjectId(123232332l);
		System.out.println("task is "+task.getChildren().size());
		
	}
	@Test
	public void testConversionAffair() throws Exception {
		String result = projectTaskService.conversionAffair(3948593656627210L);
		System.out.println(result);
	}
}
