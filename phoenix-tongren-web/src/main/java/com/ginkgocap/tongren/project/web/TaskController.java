package com.ginkgocap.tongren.project.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.bigdata.vo.UserVO;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.tree.Node;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.tongren.common.utils.StrUtils;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.model.TaskVO;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
/**
 * 项目任务的控制器
 * @author hanxifa
 *
 */
@Controller
@RequestMapping("/projectTask")
public class TaskController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private ProjectTaskService projectTaskService;
	
	@Autowired
	private Cache cache;
	
	@Autowired
	private AssignTaskService assignTaskService;
	
	@Autowired
	private UserService userService;
	
	
	/***
	 * 增加子任务状态
	 * 1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 项目不存在主任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addSubTask.json", method = RequestMethod.POST)
	public void addSubTask(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "title|R","organizationId|R","taskPid|R","description"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			User user = ri.getUser();
			params = ri.getParams();
			Task task=new Task();
			task.setCreateTime(new Timestamp(System.currentTimeMillis()));
			task.setOrganizationId(Long.parseLong(params.getParam("organizationId")));
			//task.setProjectUndertakenId(Long.parseLong(params.getParam("projectId")));
			task.setTitle(params.getParam("title"));
			task.setTaskDescription(params.getParam("description"));
			task.setTaskStatus(0);
			task.setCreateId(user.getId());
			task.setTaskType(0);//项目任务编码为0
			task.setTaskPid(Long.valueOf(params.getParam("taskPid")));//主任务id
			//1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 项目不存在主任务
			String status=projectTaskService.addTask(task);
			if(status.startsWith("1")){
				notification.put("status", "1");
				responseData.put("id", status.substring(2));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				responseData.put("status", status);
				String[] resStrArr={"已经存在主任务","与主任务不是一个组织"," 项目不存在主任务","此任务对应的项目处于不可编辑的状态"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("增加子任务失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/***
	 * 增加子任务，用于app端业务，任务名称，分配人，等一并提交
	 * 1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 项目不存在主任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addSubTaskWithMobile.json", method = RequestMethod.POST)
	public void addSubTaskWithMobile(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "title|R","organizationId|R","taskPid|R","users|R","startDate|R","endDate|R","description"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> responseData = new HashMap<String, Object>();
			params = ri.getParams();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Task task=new Task();
			task.setCreateTime(new Timestamp(System.currentTimeMillis()));
			task.setOrganizationId(Long.parseLong(params.getParam("organizationId")));
			
			task.setTitle(params.getParam("title"));
			task.setTaskDescription(params.getParam("description"));
			task.setTaskStatus(0);
			task.setCreateId(ri.getUser().getId());
			task.setTaskType(0);//项目任务编码为0
			task.setTaskPid(Long.valueOf(params.getParam("taskPid")));//主任务id
			task.setStartTime(new Timestamp(sf.parse(params.getParam("startDate")).getTime()));
			task.setEndTime(new Timestamp(sf.parse(params.getParam("endDate")).getTime()));
			//1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 项目不存在主任务
			String status=projectTaskService.addTask(task);
			String[] userArray=params.getParam("users").split(",");
			long[] performUids=new long[userArray.length];
			int index=0;
			for(String uid:userArray){
				performUids[index++]=Long.parseLong(uid);
			}
			if(status.startsWith("1")&&performUids.length>0){
				AssignTask at = new AssignTask();
				at.setAssignerId(task.getCreateId());
				at.setOrganizationId(Long.parseLong(params.getParam("organizationId")));
				at.setProjectTaskId(Long.parseLong(status.substring(2)));
				String astatus=assignTaskService.assignTask(at, performUids);
				logger.info("assignTask status is "+astatus);
				Map<String, Object> notification = new HashMap<String, Object>();
				notification.put("status", "1");
				responseData.put("id", status.substring(2));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				responseData.put("status", status);
				String[] resStrArr={"已经存在主任务","与主任务不是一个组织"," 项目不存在主任务","此任务对应的项目处于不可编辑的状态"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("增加子任务失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 *更新子任务状态
	 * * 1 更新成功 2 不存在的任务 3 修改人与创建人不一致 4 任务的开始时间不在项目周期内 5 任务的结束时间不在项目周期内 6 任务已经开始不能修改 7设置任务时间之前必须先分配任务执行人
	 */
	@RequestMapping(value = "/updateSubTask.json", method = RequestMethod.POST)
	public void updateSubTask(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String paramsKey[] = { "taskId|R","title","startDate","endDate","organizationId|R","users"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			logger.info("begin updateSubTask param:"+request.getParameter("requestJson"));
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			User user = ri.getUser();
			params = ri.getParams();
			Task task=new Task();
			task.setId(Long.parseLong(params.getParam("taskId")));
			task.setTitle(params.getParam("title"));
			String startDate=params.getParam("startDate");
			String endDate=params.getParam("endDate");
			
			int tmpFlag=0;
			if(StringUtils.hasText(startDate)){
				tmpFlag++;
			}
			if(StringUtils.hasText(endDate)){
				tmpFlag++;
			}
			if(tmpFlag==1){
				warpMsg(SysCode.ERROR_CODE,"任务的开始时间和结束时间必须成对提交，或者都不提交",params,response,responseData);
				return;
			}
			if(tmpFlag==2){
				task.setStartTime(new Timestamp(sf.parse(startDate).getTime()));
				task.setEndTime(new Timestamp(sf.parse(endDate).getTime()));
				if(task.getStartTime().compareTo(task.getEndTime())>0){
					warpMsg(SysCode.ERROR_CODE,"任务的开始时间不能大于结束时间",params,response,responseData);
					return;
				}
			}
			if(tmpFlag==0&&StringUtils.hasText(params.getParam("title"))==false){
				warpMsg(SysCode.ERROR_CODE,"名称或时间至少输入一项",params,response,responseData);
				return;
			}
			task.setCreateId(user.getId());
			String users=params.getParam("users");
			if(users!=null&&users.length()>0){//修改分配人
				logger.info("users is "+users);
				String[] userArray=params.getParam("users").split(",");
				long[] performUids=new long[userArray.length];
				int index=0;
				for(String uid:userArray){
					performUids[index++]=Long.parseLong(uid);
				}
				AssignTask at = new AssignTask();
				at.setAssignerId(user.getId());
				at.setOrganizationId(Long.parseLong(params.getParam("organizationId")));
				at.setProjectTaskId(task.getId());
				String astatus=assignTaskService.assignTask(at, performUids);
				logger.info("assignTask status is "+astatus);
				if("0".equals(astatus)==false){
					String resStrArr[]={"任务已经开始,发起者不能再分配","转发过来的任务只能分配给一个人","该任务由执行人进行了再次分配，不能分配","该任务由执行人创建了子任务不能删除","转发过来的任务只能在开始状态下才可以分配;","操作人无权分配","不存在的任务","执行失败","已存在的执行人"};
					warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(astatus)-1],params,response,responseData);
					return ;
				}
			}
			// 1 更新成功 2 不存在的任务 3 修改人与创建人不一致 4 任务的开始时间不在项目周期内 5 任务的结束时间不在项目周期内 6 任务已经开始不能修改
			String status=projectTaskService.updateTask(task);
			notification.put("status", "1");
			if(status.startsWith("1")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				String[] resStrArr={"不存在的任务","修改人与创建人不一致 ","任务的开始时间不在项目周期内","任务的结束时间不在项目周期内","任务已经开始不能修改","设置任务时间之前必须先分配任务执行人","此任务对应的项目处于不可编辑的状态"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
			logger.info("end updateSubTask param:"+request.getParameter("requestJson"));
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改任务失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 完成任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/completeTask.json", method = RequestMethod.POST)
	public void completeTask(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "taskId|R","organizationId|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			User user = ri.getUser();
			params = ri.getParams();
			//1 更新成功 2 不存在任务 3 状态码不正确  4没有权限
			String status=projectTaskService.updateStatus(Long.parseLong(params.getParam("taskId")), 2,user.getId());
			if(status.startsWith("1")){
				notification.put("status", "1");
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				notification.put("status", status);
				String[] resStrArr={" 不存在任务 ","状态码不正确"," 没有权限","此任务对应的项目处于不可编辑的状态"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("完成任务失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 删除任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/removeTask.json", method = RequestMethod.POST)
	public void removeTask(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "taskId|R","organizationId|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			User user = ri.getUser();
			params = ri.getParams();
			//1 1 删除成功 2 任务不存在 3 任务已经开始不能删除
			String status=projectTaskService.delete(Long.parseLong(params.getParam("taskId")),user.getId());
			if(status.startsWith("1")){
				notification.put("status", "1");
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				notification.put("status", status);
				String[] resStrArr={"任务不存在","任务已经开始不能删除","没有权限"," 此任务对应的项目处于不可编辑的状态"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除任务失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 获取主任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getPrimaryTask.json", method = RequestMethod.POST)
	public void getPrimaryTask(HttpServletRequest request, HttpServletResponse response)
	{
		ParamInfo params = null;
		String paramsKey[] = { "projectId|R","organizationId|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String projectId=params.getParam("projectId");
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			Task task=projectTaskService.getPrimaryTaskByProjectId(Long.parseLong(projectId));
			if(params.isLoginWithMobile()){
				List<Node> subTasks=task.getChildren();
				if(subTasks!=null&&subTasks.size()>0){
					for(Node st:subTasks){
						st.addExtend("assignInfo", getAssginUserInfo(st.getId(),ri.getUser().getId()));
					}
				}
			}
			responseData.put("task", task);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("获取 主任务失败！："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 任务的详细信息 返回改任务下的一级子任务
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getTaskDetail.json", method = RequestMethod.POST)
	public void getTaskList(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "taskId|R","type|R","organizationId|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			//User user = ri.getUser();
			params = ri.getParams();
			String type=params.getParam("type");
			//-1  所有的 0 准备中 1 已开始 2 已完成 
			if(StrUtils.isIn(type, "-1,0,1,2,3")==false){
				warpMsg(SysCode.PARAM_IS_ERROR,"type is not in -1,0,1,2,3",params,response,responseData);
				return;
			}
			Task task=projectTaskService.getTaskDetail(Long.parseLong(params.getParam("taskId")), Integer.parseInt(type));
			responseData.put("tasklist", task);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除任务失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 我的任务列表
	 */
	@RequestMapping(value = "/getMyTaskList.json")
	public void getMyTaskList(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "time|R", "type|R","pageSize","organizationId|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			int pageSize=20;
			try{
				pageSize=Integer.parseInt(params.getParam("pageSize"));
			}catch(Exception e){}
			 //* userId 用户id
			 //* type 任务类型 0 全部  1 我发起的任务 2 组织任务 3项目任务  4 被退回的任务 5已完成任务
			Map<String, String> qp=new HashMap<String, String>();
			qp.put("userId", ri.getUser().getId()+"");
			qp.put("type", params.getParam("type"));
			qp.put("organizationId", params.getParam("organizationId"));
			if(StrUtils.isIn(params.getParam("type"), "0,1,2,3,4,5")==false){
				warpMsg(SysCode.PARAM_IS_ERROR,"parameter type is not in (0,1,2,3,4,5)",params,response);
				return;
			}
			long time=Long.parseLong(params.getParam("time"));
			if(time<=0){
				time=System.currentTimeMillis();
			}else if(time>System.currentTimeMillis()){
				time=System.currentTimeMillis();
			}
			List<TaskVO> list=projectTaskService.getMyTaskList(time, pageSize, qp);
			long t1=System.currentTimeMillis();
			for(TaskVO task:list){
				task.setAttachUrl(getPathByTaskId(task.getTask().getAttachId()));
			}
			logger.info("convert to url from id cost:"+(System.currentTimeMillis()-t1));
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			responseData.put("tasklist", list);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询我的任务列表失败！参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}

	}
	
	/**
	 * 定时器调用 批量更新任务状态
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/batchUpdateStatus.do")
	public void batchUpdateStatus(HttpServletRequest request, HttpServletResponse response) {
		boolean isAllow=isAllowUpdate();
		try{
			if(isAllow){
				logger.info("begin batch update project task status ");
				projectTaskService.batchUpdateStatus();
				logger.info("end batch update project task status ");
			}else{
				logger.info("has cancel batch update project task status ");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("batchUpdateStatus failed! ",e);
		}
	}
	
	/**
	 * 是否允许批量更新项目任务状态
	 * @return
	 */
	private boolean isAllowUpdate(){
		String cacheCey = RedisKeyUtils.getSessionIdKey("last_batch_update_time");
		Object val=cache.getByRedis(cacheCey);
		int mins=5;//默认5分钟
		try{
			if(System.getProperty("project.batchupdate.interval")!=null){
				mins=Integer.parseInt(System.getProperty("project.batchupdate.interval"));
			}
		}catch(Exception e){
			
		}
		if(val!=null){
			Long preTime=(Long)val;
			if(System.currentTimeMillis()-preTime>=mins*60*1000){
				cache.setByRedis(cacheCey, new Long(System.currentTimeMillis()), 24*60*60);
				return true;
			}else{
				return false;
			}
		}else{
			cache.setByRedis(cacheCey, new Long(System.currentTimeMillis()), 24*60*60);
			return true;
		}
	}
	
	public List<UserVO> getAssginUserInfo(long taskId,long userId){
		List<AssignTask>  lst = assignTaskService.getAssignTaskByAssignId(Long.valueOf(taskId),userId);
		List<UserVO> result = new ArrayList<UserVO>();
		if(lst != null && !lst.isEmpty()){
			String urlPre=FileInstance.FTP_WEB+FileInstance.FTP_URL;
			for(AssignTask at :lst){
				User u  = userService.selectByPrimaryKey(at.getPerformerId());
				if(u!=null){
					UserVO vo = new UserVO();
					vo.setName(u.getName());
					vo.setPath(urlPre+u.getPicPath());
					vo.setId(u.getId());
					result.add(vo);
				}else{
					logger.info("not found user userinfo "+at.getPerformerId());
				}
			}
		}
		return result;
	}
	/**
	 * 功能描述 ：任务转化为事务
	 * 
	 */
	@RequestMapping(value = "/conversionAffair.json")
	public void conversionAffair(HttpServletRequest request, HttpServletResponse response) {
		
		ParamInfo params = null;
		String paramsKey[] = { "taskId|R"};
		try{
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			params = ri.getParams();
			long taskId = Long.parseLong(params.getParam("taskId"));
			
			String status = projectTaskService.conversionAffair(taskId);
			if(status.equals("1")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				String[] resStrArr={"任务还未分配或被执行人退回","任务不存在或任务状态为已完成或过期"," 转化事务失败"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("batchUpdateStatus failed! ",e);
		}
	}
	/**
	 * 任务提醒功能 ：
	 * 
	 */
	@RequestMapping(value = "/sendTaskRemind.json")
	public void sendTaskRemind(HttpServletRequest request, HttpServletResponse response){
		
		ParamInfo params = null;
		String paramsKey[] = { "taskId|R"};
		try{
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			params = ri.getParams();
			long taskId = Long.parseLong(params.getParam("taskId"));
			
			String status = projectTaskService.taskRemind(taskId);
			if(status.equals("1")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				String[] resStrArr={"任务不存在","任务未能找到分配人","发送提醒失败","距离上一次任务提醒不超过8个小时"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("sendTaskRemind failed! ",e);
		}
	}
}
