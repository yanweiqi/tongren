package com.ginkgocap.tongren.project.manage.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.vo.ProjectVO;

public class ProjectServiceTest extends SpringContextTestCase{
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private PublishService publishService;

	@Test
	public void testCreate() throws Exception {
		
		//long createrId = 128036L;
		//long organizationId = 3905158115491845L;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 2);
		//"name":"mizehau","introduction":"dkelwfjw","validityStartTime":"2015-12-08 15:06:21","validityEndTime":"2015-12-10 

		//15:06:24","cycle":"12","industry":"1202","area":"2897","remuneration":"1200","taskId":"TVRRME9UVTFPRE0zT0RBeU1EQXdNRFkyTVRnMU9EQTU="
		Project a = new Project();
		Date endDate = calendar.getTime();
		String name = "mizehau";
		String introduction = "dkelwfjw";
		String industry = "1202";
		String area = "test闫伟旗创建项目";
		//String document = "test闫伟旗创建项目";
		int cycle = 12;
		String taskId = "TVRRME9UVTFPRE0zT0RBeU1EQXdNRFkyTVRnMU9EQTU=";
		double remuneration = 1200;
		Timestamp validityStartTime = Timestamp.valueOf(sdf.format(startDate));
		Timestamp validityEndTime = Timestamp.valueOf(sdf.format(endDate));
		a.setArea(area);
		a.setName(name);
		a.setIntroduction(introduction);
		a.setValidityStartTime(validityStartTime);
		a.setValidityEndTime(validityEndTime);
		a.setCycle(cycle);
		a.setIndustry(industry);
		a.setRemuneration(remuneration);
		a.setDocument(taskId);
		Project p = projectService.create(a);
		System.out.println(p);
	}

	@Test
	public void testGetProjectsByStatus() throws Exception {
		int stauts = ProjectStatus.Project_Create_Official.getKey();
		List<Project> projects =  projectService.getProjectsByStatus(stauts);
		JSONArray jsonArray = JSONArray.fromObject(projects);
		logger.info(jsonArray.toString());
	}

	@Test
	public void testGetMyProjectByCreateId() throws Exception {
		long createrId = 128036L;
		List<Project> projects =  projectService.getMyProjectByCreateId(createrId);
		JSONArray jsonArray = JSONArray.fromObject(projects);
		logger.info(jsonArray.toString());
	}
	
	@Test
	public void testGetMyProjectsByCreateIdAndStatus() throws Exception{
		int stauts = ProjectStatus.Project_Create_Official.getKey();
		long createrId = 128036L;
		List<Project> projects =  projectService.getMyProjectsByCreateIdAndStatus(createrId, stauts);
		JSONArray jsonArray = JSONArray.fromObject(projects);
		logger.info(jsonArray.toString());
	}
	@Test
	public void testGetProjectDetail() throws Exception{
		long projectId = 3915320469487636L;
		projectService.getProjectDetail(projectId);
	}
	@Test
	public void testGetProjectListByParameters() throws Exception{
		
		int pageSize = 10;
		long time = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String,String>();
		params.put("areaId", "1");
		params.put("industryId", "372");
		//params.put("areaId", value);
		
		List<Publish> voList = projectService.getProjectListByParameters(time,pageSize, params);
		System.out.println("***"+voList.size());
	}
	public static void main(String[] args) {
		
		System.out.println(System.currentTimeMillis());
	}
}
