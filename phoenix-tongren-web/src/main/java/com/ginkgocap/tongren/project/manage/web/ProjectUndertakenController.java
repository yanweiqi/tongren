package com.ginkgocap.tongren.project.manage.web;

import java.sql.Timestamp;
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

import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;
import com.ginkgocap.tongren.project.manage.vo.UndertakenVO;
import com.ginkgocap.tongren.project.task.exception.UndertakenException;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.google.common.collect.Maps;

/**
 * 项目承接
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月20日
 */
@Controller
@RequestMapping("/project/undertaken")
public class ProjectUndertakenController extends BaseController{

	private final Logger logger  = LoggerFactory.getLogger(ProjectUndertakenController.class);//日志记录对象
	
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;//发送消息接口
	@Autowired
	private UndertakenService undertakenService;//承接任务接口
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;//组织接口
	@Autowired
	private ProjectService projectService;//项目接口
	@Autowired
	private Cache cache;//缓存对象
	@Autowired
	private MessageService messageService;//消息接口
	@Autowired
	private UserService userService;//用户接口
	
	/**
	 * 功能描述：确认合作项目         
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月20日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value="/undertakeProject.json",method=RequestMethod.POST)
	public void undertakeProject(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"organizationId|R","projectId|R","recipientId|R"};
		Undertaken undertaken = null;
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			Long organizationId = Long.parseLong(params.getParam("organizationId"));
			Long projectId = Long.parseLong(params.getParam("projectId"));
			Long recipientId = Long.parseLong(params.getParam("recipientId"));
			undertaken = undertakenService.undertakenProject(projectId, recipientId, organizationId);
			if(undertaken == null){
				notification.put("notifyCode", SysCode.UNDERTAKE_PROJECT_ERR.getCode());
				notification.put("notifyMessage", SysCode.UNDERTAKE_PROJECT_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.UNDERTAKE_PROJECT_LIST_NULL, genResponseData(null, notification)));
				return;
			}
			responseData.put("undertaken", undertaken);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			return;
		} catch (Exception e) {
			if (e instanceof UndertakenException) {
				UndertakenException un = (UndertakenException) e;
				if(un.getErrorCode() == 100){
					logger.error("Error encoding for 【"+un.getErrorCode()+"】");
					logger.error(e.getMessage());
					warpMsg(SysCode.PROJECT_OBJ_NULL,SysCode.PROJECT_OBJ_NULL.getMessage(),params,response);
					return;
				}
				if(un.getErrorCode() == 103){
					logger.error("Error encoding for 【"+un.getErrorCode()+"】");
					logger.error(e.getMessage());
					warpMsg(SysCode.PROJECT_OBJ_UNDERTAKEN,SysCode.PROJECT_OBJ_UNDERTAKEN.getMessage(),params,response);
				}
				if(un.getErrorCode() == 201){
					logger.error("Error encoding for 【"+un.getErrorCode()+"】");
					logger.error(e.getMessage());
					warpMsg(SysCode.PROJECT_STATUS_NOTONE,SysCode.PROJECT_STATUS_NOTONE.getMessage(),params,response);
				}
				else{
					logger.error("Error encoding for 【"+un.getErrorCode()+"】");
					logger.error(e.getMessage(),e);
					warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
					return;
				}
			}else{
				logger.error("undertakeProject failed! param:"+request.getParameter("requestJson"),e);
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				return;
			}
		}
	}
	/**
	 * 功能描述：我的承接项目列表         
	 *                                                       
	 * @param request
	 * @param response 
	 * @param status  0项目进行中、1完成、2、放弃、3已过期                                                                                  
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月20日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value="/undertakenList.json",method=RequestMethod.POST)
	public void undertakenList(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"status|R"};
		List<Undertaken> list = null;
		List<UndertakenVO> returnList = new ArrayList<UndertakenVO>();
		UndertakenVO vo = null;
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			int status = Integer.parseInt(params.getParam("status"));
			User u = ri.getUser();
			list = undertakenService.getUndertakenList(u.getId(), status);
			if(list == null || list.size() == 0){
				notification.put("notifyCode", SysCode.UNDERTAKE_PROJECT_LIST_NULL.getCode());
				notification.put("notifyMessage", SysCode.UNDERTAKE_PROJECT_LIST_NULL.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.UNDERTAKE_PROJECT_LIST_NULL, genResponseData(null, notification)));
				return;
			}
			for (Undertaken entity : list) {
				vo = new UndertakenVO();
				if(entity.getPublish() != null){
					vo.setArea(entity.getPublish().getProject().getArea());
					vo.setIndustry(entity.getPublish().getProject().getIndustry());
					vo.setIntroduction(entity.getPublish().getProject().getIntroduction());
					vo.setName(entity.getPublish().getProject().getName());
					vo.setRemuneration(entity.getPublish().getProject().getRemuneration());
					vo.setCreateProjectId(entity.getPublish().getProject().getCreaterId());
					User userCreate = userService.selectByPrimaryKey(entity.getPublish().getProject().getCreaterId());
					if(userCreate != null){
						vo.setCreateProjectName(userCreate.getName());
						vo.setCreateProjectPicUrl(getUserPicURL(userCreate));
					}
				}
				vo.setRecipientId(entity.getRecipientId());
				User userRecipient= userService.selectByPrimaryKey(entity.getRecipientId());
				if(userRecipient != null){
					vo.setRecipientName(userRecipient.getName());
					vo.setRecipientPicUrl(getUserPicURL(userRecipient));
				}
				
				vo.setStartTime(entity.getStartTime());
				vo.setEndTime(entity.getEndTime());
				int cycle = (int) (((DateUtil.trunck(entity.getEndTime()).getTime())-(DateUtil.trunck(entity.getStartTime()).getTime()))/(CommonConstants.CALCULATION_DAYS));//算项目周期
				vo.setCycle(cycle);
				int lDays = (int) (((DateUtil.trunck(entity.getEndTime()).getTime())-(DateUtil.trunck(new Timestamp(System.currentTimeMillis())).getTime()))/(CommonConstants.CALCULATION_DAYS));//计算项目剩余天数
				vo.setlDays(lDays);
				
				vo.setOrganizationId(entity.getRecipientOrganizationId());
				try {
					Organization organization = tongRenOrganizationService.getOrganizationById(entity.getRecipientOrganizationId());//是否组织项目
					if(organization != null){
						vo.setOrganizationName(organization.getName());
						vo.setProjectType(CommonConstants.ORGANIZATION_PROJECT);
					}else{
						vo.setProjectType(CommonConstants.PERSONAL_PROJECT);
					}
				} catch (Exception e) {
					logger.error("get organization obj by organizationId failed! organizationId:"+entity.getRecipientOrganizationId(),e);
				}
				vo.setProjectId(entity.getProjectId());
				vo.setStatus(entity.getStatus());//0项目进行中、1完成、2、放弃、3已过期
				if(entity.getStatus() == 0){
					vo.setStatusStr("剩余："+lDays+"天");
				}
				if(entity.getStatus() == 1){
					vo.setStatusStr("已完成");
				}
				if(entity.getStatus() == 2){
					vo.setStatusStr("已放弃");
				}
				if(entity.getStatus() == 3){
					vo.setStatusStr("已过期");
				}
				returnList.add(vo);
			}
			responseData.put("list", returnList);
			responseData.put("size", returnList.size());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			return;
		} catch (Exception e) {
			logger.error("undertakenList failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：把个人项目变成组织项目         
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月24日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value="/updateUndertakenProject.json",method=RequestMethod.POST)
	public void updateUndertakenProject(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"projectId|R","organizationId|R"};
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			long projectId = Long.parseLong(params.getParam("projectId"));
			long organizationId = Long.parseLong(params.getParam("organizationId"));
			boolean isOk = undertakenService.updateOrgProject(projectId, organizationId);
			if(isOk){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				return;
			}else{
				notification.put("notifyCode", SysCode.SYS_ERR.getCode());
				notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
				return;
			}
		} catch (Exception e) {
			logger.error("updateUndertakenProject failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述： 定时器调用 批量更新项目已过期状态    
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月20日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/batchUpdateStatus.do")
	public void batchUpdateStatus(HttpServletRequest request, HttpServletResponse response) {
		boolean isAllow=isAllowUpdate();
		try{
			if(isAllow){
				logger.info("begin 【batch update to undertake the project has expired】 ");
				undertakenService.batchUpdateStatus();
				logger.info("end 【batch update to undertake the project has expired】 ");
			}else{
				logger.info("Batch update execution within the time of failure");
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
		String cacheCey = RedisKeyUtils.getSessionIdKey("last_batch_update_time_undertaken");
		Object val=cache.getByRedis(cacheCey);
		int mins=5;//默认5分钟
//		try{
//			if(System.getProperty("undertaken.batchupdate.interval")!=null){
//				mins=Integer.parseInt(System.getProperty("undertaken.batchupdate.interval"));
//			}
//		}catch(Exception e){
//			
//		}
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
	/**
	 * 项目延期申请
	 * @author liweichao
	 * @param organizationId  组织id
	 * @param projectId 项目id
	 * @param cycle   延期天数
	 * @param content 延期申请说明
	 * 
	 */
	
	@RequestMapping(value="/postponeProject.json",method=RequestMethod.POST)
	public void postponeProject(HttpServletRequest request, HttpServletResponse response) {
		
			ParamInfo params = null;
			Map<String, Object> responseData = Maps.newHashMap();
			Map<String, Object> notification = Maps.newHashMap();
			String[] paramsKey = new String[]{"organizationId","projectId|R","cycle|R","content"};
			try{
				RequestInfo requestInfo = validate(request, response, paramsKey);
				if(null == requestInfo) return;
				User user = requestInfo.getUser();
				params = requestInfo.getParams();
				String orgStr = params.getParam("organizationId");
				if(orgStr == null || orgStr.equals("")){
					orgStr="0";
				}
				Long organizationId = Long.valueOf(params.getParam("organizationId"));
				Long projectId = Long.valueOf(params.getParam("projectId"));
				Integer cycle = Integer.valueOf(params.getParam("cycle"));
				String content = params.getParam("content").trim();
				
				List<Long> ids = messageService.getMessageCreateListId(projectId, user.getId(), MessageType.APPLICATION_EXTENSION_PROJECT);//判断是否有发过邀请
				if(ids != null && ids.size() != 0){
					notification.put("notifyCode", SysCode.EXTENSION_PROJECT_ERROR_SUBMIT.getCode());
					notification.put("notifyMessage", SysCode.EXTENSION_PROJECT_ERROR_SUBMIT.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.EXTENSION_PROJECT_ERROR_SUBMIT, genResponseData(null, notification)));
					return;
				}
				
				boolean status = tongRenSendMessageService.sendExtensioProjectMes(user.getId(), organizationId, projectId, cycle, content);
				if(status){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
					return;
				}else{
					notification.put("notifyCode", SysCode.EXTENSION_PROJECT_ERROR.getCode());
					notification.put("notifyMessage", SysCode.EXTENSION_PROJECT_ERROR.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.EXTENSION_PROJECT_ERROR, genResponseData(null, notification)));
					return;
				}
			}catch(Exception e){
				logger.error("postponeProject failed! param:"+request.getParameter("requestJson"),e);
				e.printStackTrace();
				notification.put("notifyCode", SysCode.SYS_ERR.getCode());
				notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
				return;
			}
		}
	
	
	/**
	 * 功能描述： 放弃项目和结束项目
	 * @author liweichao
	 * @param projectId 项目ID
	 * @param userId 放弃项目的人ID
	 * @param type 1: 结束项目 2：放弃项目 
	 * 
	 */
	
	@RequestMapping(value="/projectOperation.json",method=RequestMethod.POST)
	public void projectOperation(HttpServletRequest request, HttpServletResponse response) {
		
			ParamInfo params = null;
			Map<String, Object> responseData = Maps.newHashMap();
			Map<String, Object> notification = Maps.newHashMap();
			String[] paramsKey = new String[]{"projectId|R","type|R"};
			try{
				RequestInfo requestInfo = validate(request, response, paramsKey);
				if(null == requestInfo) return;
				User user = requestInfo.getUser();
				params = requestInfo.getParams();
				
				Long projectId = Long.valueOf(params.getParam("projectId"));
				Integer type = Integer.valueOf(params.getParam("type"));
				
				boolean status = undertakenService.projectOperation(projectId,user.getId(), type);
				if(status){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
					return;
				}else{
					notification.put("notifyCode", SysCode.PROJECT_OPERATION_ERROR.getCode());
					notification.put("notifyMessage", SysCode.PROJECT_OPERATION_ERROR.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.PROJECT_OPERATION_ERROR, genResponseData(null, notification)));
					return;
				}
			}catch(Exception e){
				logger.error("projectOperation failed! param:"+request.getParameter("requestJson"),e);
				e.printStackTrace();
				notification.put("notifyCode", SysCode.SYS_ERR.getCode());
				notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
				return;
			}
		}
}
