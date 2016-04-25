package com.ginkgocap.tongren.organization.attendance.web;

import java.sql.Timestamp;
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

import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceSystem;
import com.ginkgocap.tongren.organization.attendance.service.AttendanceSystemService;
import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 考勤模板
 * @author hanxifa
 *
 */
@Controller
@RequestMapping("/attendanceSystem") 
public class AttendanceSystemController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(AttendanceSystemController.class);
	
	@Autowired
	AttendanceSystemService attendanceSystemService;
	
	/***
	 * 增加考勤设置
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/add.json",method = RequestMethod.POST)
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_ATTENDANCE_ADD)
	public void add(HttpServletRequest request, HttpServletResponse response){
		saveOrUpdate(request,response,"add");
		
	}
	
	/***
	 * 增加考勤设置
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/modify.json",method = RequestMethod.POST)
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_ATTENDANCE_ADD)
	public void modify(HttpServletRequest request, HttpServletResponse response){
		saveOrUpdate(request,response,"modify");
	}
	private void saveOrUpdate(HttpServletRequest request, HttpServletResponse response,String opType){
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"name|R|L20","startWorkTime|R","workTimeOut|R","organizationId|R","elasticityMinutes|R","description|R|L50"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			logger.info("begin add attendancesystem  param: "+params.getParams());
		    AttendanceSystem attendanceSystem=new AttendanceSystem();
		    attendanceSystem.setCreateId(user.getId());
			attendanceSystem
					.setCreateTime(new Timestamp(System.currentTimeMillis()));
			attendanceSystem.setDescription(params.getParam("description"));
			attendanceSystem.setElasticityMinutes(Integer.parseInt(params.getParam("elasticityMinutes")));// 弹性时间默认为分钟
			attendanceSystem.setName(params.getParam("name"));
			attendanceSystem.setStartWorkTime(params.getParam("startWorkTime"));//设置上班时间
			attendanceSystem.setWorkTimeOut(params.getParam("workTimeOut"));//设置下班时间
			attendanceSystem.setOrganizationId(Long.valueOf(params.getParam("organizationId")));
			attendanceSystem.setUpdateTime(attendanceSystem.getCreateTime());
			
			//1 增加成功 2 考勤设置存在 3 参数错误
			if(opType.equals("add")){
				String status= attendanceSystemService.add(attendanceSystem);
				responseData.put("status", status);
				if(status.equals("1")){
					 renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
					 logger.info("add attendancesystem success! param: "+params.getParams());
				}else{
					String msg= status.equals("2")?"考勤设置存在":"参数错误";
					notification.put("notifyCode", SysCode.PARAM_IS_ERROR.getCode());
					notification.put("notifyMessage", msg);
					renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(responseData, notification)));
					logger.info("add attendancesystem failed status  "+status+",param:"+params.getParams());
				}
			}else if(opType.equals("modify")){
				 String status=attendanceSystemService.modify(attendanceSystem);
				 if(status.equals("1")){
					 renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
					 logger.info("update attendancesystem success! param: "+params.getParams());
				 }else{
					 String msgStr="";
					if(status.equals("2")){
						 msgStr="考勤配置不存在，无法更新";
					}else if(status.equals("3")){
						msgStr="更新失败！";
					}
					notification.put("notifyCode", SysCode.ERROR_CODE.getCode());
					notification.put("notifyMessage", msgStr);
					renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(responseData, notification)));
					logger.info("update attendancesystem failed status  "+status+",param:"+params.getParams());
				 }
				 
			}
		   
		}catch(Exception e){
			
			logger.error("add attendancesystem failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	/***
	 * 获取当前考勤
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/get.json",method = RequestMethod.POST)
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_ATTENDANCE_SHOW)
	public void get(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"organizationId|R"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			//User user=ri.getUser();
			params=ri.getParams();
			logger.info("begin get attendancesystem  param: "+params.getParams());
			List<AttendanceSystem> list=attendanceSystemService.getByOrgId(Long.parseLong(params.getParam("organizationId")));
			if(list!=null&&list.size()>0){
				logger.info("found  attendanceSystem "+list.size());
				responseData.put("success", list.get(0));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				logger.info("not found any attendanceSystem");
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null,notification)));
			}
		}catch(Exception e){
			logger.error("get attendancesystem failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}

}
