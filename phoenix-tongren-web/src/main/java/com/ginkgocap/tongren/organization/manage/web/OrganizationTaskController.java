package com.ginkgocap.tongren.organization.manage.web;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.manage.vo.OperationVO;
import com.ginkgocap.tongren.project.system.code.TaskCode;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 组织任务控制层
 * 
 * @author liweichao
 *
 */
@Controller
@RequestMapping("/organizationTask") 
public class OrganizationTaskController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationTaskController.class);
	
	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private UserService userService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private FileIndexService fileIndexService;

	/**
	 * 功能描述 : 创建组织任务
	 * @author liweichao
	 * @param title  任务名称
	 * @param startTime  开始时间 时分秒
	 * @param endTime  结束时间   时分秒
	 * @param taskDescription 任务描述
	 * @param attachId //taskId
	 * @param userId 执行人id
	 * @param organizationId 组织id
	 * 1：成功   2:任务分配返回为空   3：任务保存返回为空  4:异常 操作错误
	 * 
	 */
	@RequestMapping("/create.json")
	public void create(HttpServletRequest request,HttpServletResponse response){
		
		ParamInfo params = null;
		String paramsKey[] = { "title|R","startTime|R","endTime|R","taskDescription","attachId","performerId|R","organizationId|R"};
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			String title = params.getParam("title").trim();
			String userId = params.getParam("performerId").trim();
			String endTime = params.getParam("endTime").trim();
			String attachId = params.getParam("attachId");
			String startTime = params.getParam("startTime").trim();
			String taskDescription = params.getParam("taskDescription");
			String organizationId = params.getParam("organizationId").trim();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			Task  tk = new Task();
			tk.setTitle(title);
			tk.setEndTime(new Timestamp(df.parse(endTime).getTime()));
			if(StringUtils.isNotBlank(attachId)){
				tk.setAttachId(attachId.trim());
			}
			if(StringUtils.isNotBlank(taskDescription)){
				tk.setTaskDescription(taskDescription.trim());
			}
			tk.setStartTime(new Timestamp(df.parse(startTime).getTime()));
			tk.setCreateId(user.getId());
			tk.setTaskStatus(0);//组织任务 0 准备中 1 已开始 2 已完成 3 已过期
			tk.setTaskType(1);//组织任务编码为1
			tk.setTaskPid(0);//设置为-1 会按照项目id自动 查找主任务id
			tk.setProjectUndertakenId(0);
			tk.setOrganizationId(Long.valueOf(organizationId));
			tk.setCreateTime(new Timestamp(System.currentTimeMillis()));
			
			String status = projectTaskService.createOrganizationTask(tk, Long.valueOf(userId));
			if(status.startsWith("1")){
				notification.put("status", "1");
				responseData.put("id", status.substring(2));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				responseData.put("status", status);
				String[] resStrArr={"任务分配返回为空","任务保存返回为空","异常 操作错误"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
			}
		}catch(Exception e){
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				return;
		}
	}
		/**
		 * 功能描述 :重新分配组织任务
		 * @author liweichao 
		 * @param organizationTaskId  组织任务id
		 * @param performerId  操作人id
		 * @param organizationId 组织id
		 * @param startTime 任务开始时间  
		 * @param endTime 任务结束时间
		 * @return status- 1_id 增加成功  2:已经分配过任务并且任务状态不是已驳回 3:保存任务分配为空  4：组织任务为空
		 */
		@RequestMapping("/assignOrganizationTask.json")
		public void assignOrganizationTask(HttpServletRequest request,HttpServletResponse response){
			
			ParamInfo params = null;
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			String paramsKey[] = { "organizationTaskId|R","performerId|R","organizationId|R","startTime","endTime","attachId"};
			try {
				RequestInfo ri = validate(request, response, paramsKey);
				if (ri == null) {
					return;
				}
					User user = ri.getUser();
					params = ri.getParams();
					String organizationTaskId = params.getParam("organizationTaskId").trim();
					String performerId = params.getParam("performerId").trim();
					String organizationId = params.getParam("organizationId").trim();
					String startTime = params.getParam("startTime").trim();
					String endTime = params.getParam("endTime").trim();
					String attachId = params.getParam("attachId");
					AssignTask assignTask = new AssignTask();
					assignTask.setAssignerId(user.getId());
					assignTask.setPerformerId(Long.valueOf(performerId));
					assignTask.setProjectTaskId(Long.valueOf(organizationTaskId));
					assignTask.setOrganizationId(Long.valueOf(organizationId));
					assignTask.setAssignTime(new Timestamp(System.currentTimeMillis()));
					
					String status = assignTaskService.createOrganizationAssignTask(assignTask,startTime,endTime,attachId);
				if(status.startsWith("1")){
					notification.put("status", "1");
					responseData.put("id", status.substring(2));
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				}else{
					responseData.put("status", status);
					String[] resStrArr={"任务已经分配过并且接受人没有驳回,不能从新分配","保存任务分配为空","组织任务为空"};
					warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
				}
			}catch(Exception e){
					e.printStackTrace();
					warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
					return;
			}
		}
		/**
		 * 退回组织任务
		 * @param organizationTaskId 组织任务id
		 * 
		 */
		@RequestMapping("/reject.json")
		public void reject(HttpServletRequest request,HttpServletResponse response){
			
			ParamInfo params = null;
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			String paramsKey[] = { "organizationTaskId|R","text"};
			try {
				RequestInfo ri = validate(request, response, paramsKey);
				if (ri == null) {
					return;
				}
				User user = ri.getUser();
				params = ri.getParams();
				String organizationTaskId = params.getParam("organizationTaskId").trim();
				String text = params.getParam("text").trim();
				boolean status = projectTaskService.reject(Long.valueOf(organizationTaskId), user.getId(),text);
				if(status){
					responseData.put("status", status);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				}else{
					responseData.put("status", status);
					warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				}
			}catch(Exception e){
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				return;
			}
		}
		
		/**
		 * 查询组织分配任务
		 * @param organizationTaskId 组织任务id
		 * 
		 */
		@RequestMapping(value="/getOrganizationTaskOperation.json",method =RequestMethod.POST)
		public void getOperactionByOrgId(HttpServletRequest request, HttpServletResponse response){
			
					ParamInfo params = null;
					String paramsKey[] = {"organizationTaskId|R"};
					List<OperationVO> resultList = new ArrayList<OperationVO>(); 
					Map<String, Object> responseData = new HashMap<String, Object>();
					Map<String, Object> notification = new HashMap<String, Object>();
				try{
					RequestInfo ri=validate(request,response,paramsKey);
					if(ri==null){
						return;
					}
						params=ri.getParams();
						String organizationTaskId = params.getParam("organizationTaskId");
						logger.info("select organization operation --->"+organizationTaskId);
					
					List<Operation>  lst = operationService.getOpertionByOrgTaskId(Long.valueOf(organizationTaskId));
					if(lst != null && !lst.isEmpty()){
						
							for(Operation o :lst){
								OperationVO vo = new OperationVO();
								BeanUtils.copyProperties(vo, o);
								String str = "";
								User u = userService.selectByPrimaryKey(o.getOperationUid());
								Task  task = projectTaskService.getEntityById(Long.valueOf(o.getOrganizationTaskId()));//获取主任务
								StringBuilder sb = assignTaskService.getPerformerName(Long.valueOf(o.getOrganizationTaskId())); //获取被分配人名称
								if(task != null){
									if(o.getOperationCode().equals(TaskCode.ALLOT.getCode())){
										str = u.getName()+"给 "+sb+"分配了"+ task.getTitle();
									}
									if(o.getOperationCode().equals(TaskCode.RETRY.getCode())){
										str = u.getName()+"给 "+sb+"重发了"+ task.getTitle();
									}
									if(o.getOperationCode().equals(TaskCode.EXIT_TASK.getCode())){
										str = u.getName()+"退回了 "+ task.getTitle();
									}
									if(o.getOperationCode().equals(TaskCode.FINISH.getCode())){
										str = u.getName()+"完成了"+ task.getTitle();
									}
									if(o.getOperationCode().equals(TaskCode.SUBDOC.getCode())){
										List<String> file = getUrlByTaskId(o.getRemark());
										if(file!=null && !file.isEmpty()){
											str = u.getName()+" 上传了 "+file.get(0);
											vo.setDocPath(file.get(1));
										}
									}
								}
									vo.setName(str);
									resultList.add(vo);
							}
						responseData.put("organizationTaskId",organizationTaskId);
						responseData.put("result", resultList);
						renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
					}else{
						notification.put(SysCode.BIGDATA_EMPTY.getCode(), SysCode.BIGDATA_EMPTY.getMessage());
						renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
					}
				}catch(Exception e){
					e.printStackTrace();
					warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
					return;
				}
		}
		/**
		 * 删除组织任务 
		 * @param organizationTaskId 组织任务id
		 * 1 删除成功 2 任务不存在 3 任务已经开始不能删除 4没有权限
		 * 
		 */
		@RequestMapping(value="/delete.json",method =RequestMethod.POST)
		public void delete(HttpServletRequest request, HttpServletResponse response){
			
					ParamInfo params = null;
					String paramsKey[] = {"organizationTaskId|R"};
					Map<String, Object> responseData = new HashMap<String, Object>();
					Map<String, Object> notification = new HashMap<String, Object>();
			try{
				RequestInfo ri=validate(request,response,paramsKey);
					if(ri==null){
						return;
					}
					User user = ri.getUser();
					params=ri.getParams();
					String organizationTaskId = params.getParam("organizationTaskId");
					logger.info("delect organizationTask --->"+organizationTaskId);
					
					String status = projectTaskService.delete(Long.valueOf(organizationTaskId), user.getId());
					if(status.startsWith("1")){
						notification.put("status", "1");
						renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
					}else{
						notification.put("status", status);
						String[] resStrArr={"任务不存在","任务已经开始不能删除","没有权限"};
						warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
					}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		/**
		 * 根据taskId获取url信息
		 * @param taskId
		 * @return
		 */
		private List<String> getUrlByTaskId(String taskId) throws Exception{
			List<String> result = new ArrayList<String>();
			List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
			String path ="";
			if(list!=null&&list.size()>0){
				FileIndex s = (FileIndex) list.get(0);
				path = s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath()+"/"+s.getFileTitle();
				result.add(s.getFileTitle());
				result.add(path);
			}
			return result;
		}
	/**
	 * 完成组织任务 
	 * @param organizationTaskId 组织任务id
	 * @param type 操作类型   2：完成
	 * 	
	 */
		@RequestMapping(value="/finishOrganizationTask.json",method =RequestMethod.POST)
		public void finishOrganizationTask(HttpServletRequest request, HttpServletResponse response){
			
					ParamInfo params = null;
					String paramsKey[] = {"organizationTaskId|R","type|R"};
					Map<String, Object> responseData = new HashMap<String, Object>();
					Map<String, Object> notification = new HashMap<String, Object>();
			try{
				RequestInfo ri=validate(request,response,paramsKey);
					if(ri==null){
						return;
					}
					User user = ri.getUser();
					params=ri.getParams();
					Integer type = Integer.valueOf(params.getParam("type"));
					String organizationTaskId = params.getParam("organizationTaskId");
					logger.info("finishOrganizationTask organizationTask --->"+organizationTaskId+",type-->" + type);
					
					String status = projectTaskService.updateOrganizationTaskStatus(Long.valueOf(organizationTaskId), user.getId(),type);
					if(status.startsWith("1")){
						notification.put("status", "1");
						renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
					}else{
						notification.put("status", status);
						String[] resStrArr={"任务不存在","没有权限"};
						warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-2],params,response,responseData);
					}
		}catch(Exception e){
			logger.info("finishOrganizationTask is error...");
			e.printStackTrace();
		}
	}
}
