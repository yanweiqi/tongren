package com.ginkgocap.tongren.project.web;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.bigdata.vo.UserVO;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
@Controller
@RequestMapping("/assignTask")
public class AssignTaskController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(AssignTaskController.class);
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 保存分配任务
	 * 之前对次任务分配过的 ，则删除之前分配的记录，重新写入本次分配数据
	 * 
	 */
	
	@RequestMapping(value="/assign.json",method =RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "taskId|R", "orgId|R", "users|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			User user = ri.getUser();
			params = ri.getParams();
			String users = params.getParam("users");
			String[] userIds = users.split(",");
			long[] performerUsers = new long[userIds.length];
			int index = 0;
			for (String strUid : userIds) {
				performerUsers[index++] = Long.parseLong(strUid);
			}
			if (performerUsers.length != 0) {
				AssignTask at = new AssignTask();
				at.setAssignerId(user.getId());
				at.setOrganizationId(Long.parseLong(params.getParam("orgId")));
				at.setProjectTaskId(Long.parseLong(params.getParam("taskId")));
				String status = assignTaskService.assignTask(at, performerUsers);
				logger.info("assign status is " + status + ",params" + request.getParameter("requestJson"));
				if("0".equals(status)){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				}else{
					String resStrArr[]={"任务已经开始,发起者不能再分配","转发过来的任务只能分配给一个人","该任务由执行人进行了再次分配，不能分配","该任务由执行人创建了子任务不能删除","转发过来的任务只能在开始状态下才可以分配;","操作人无权分配","不存在的任务","执行失败","已存在的执行人"};
					warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-1],params,response,responseData);
				}
			}else{
				warpMsg(SysCode.PARAM_IS_ERROR,"执行人参数错误！",params,response,responseData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("分配任务失败！ 参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}

	/**
	 * 查询分配任务
	 * 
	 */
	@RequestMapping(value="/selectAssignTaskByTaskId.json",method =RequestMethod.POST)
	public void selectAssignTaskByTaskId(HttpServletRequest request, HttpServletResponse response){
		
				ParamInfo params = null;
				String paramsKey[] = {"taskId|R","organizationId|R"};
				Map<String, Object> responseData = new HashMap<String, Object>();
			try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
					User user=ri.getUser();
					params=ri.getParams();
					String taskId = params.getParam("taskId");
					logger.info("select AssignTask taskId--->"+taskId);
				
				List<AssignTask>  lst = assignTaskService.getAssignTaskByAssignId(Long.valueOf(taskId),0l);
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
				responseData.put("taskId", taskId);
				responseData.put("userList", result);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}catch(Exception e){
				e.printStackTrace();
				logger.error("查询任务执行人失败！ 参数：" + request.getParameter("requestJson"), e);
				warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
			}
	}


}
