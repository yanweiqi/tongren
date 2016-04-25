package com.ginkgocap.tongren.project.manage.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishManageService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.google.common.collect.Maps;

/**
 * 
 * @author yanweiqi
 *
 */
@Controller
@RequestMapping("/project/manage/")
public class PublishController extends BaseController{

	private final Logger logger = LoggerFactory.getLogger(PublishController.class);
	
	@Autowired
	private PublishService publishService;
	
	@Autowired
	private ApplyService applyService; 
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PublishManageService publishManageService;
	
	@RequestMapping(value="/publish.json",method=RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"projectId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long projectId = Long.valueOf( params.getParam("projectId") );
		    long publisherId = user.getId();
		    Project p = projectService.getEntityById(projectId);
		    if(null != p){
				Publish publish =  publishService.create(publisherId, ProjectStatus.Project_Publish_Success.getKey(), p.getValidityStartTime(), p.getValidityEndTime(), p);
				responseData.put("project", p);
				responseData.put("publish", publish);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
			}
		    else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.ProjectIsNotExist.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.ProjectIsNotExist.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		    }
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
		
	/**
	 * 获取有效期内未被承接的项目Page列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getPagePublishValidity.json",method=RequestMethod.POST)
	public void getPagePublishValidity(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"pageNumber|R","size|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			int status = ProjectStatus.Project_Apply_CheckPending.getKey();
			int pageNumber = Integer.valueOf(params.getParam("pageNumber"));
			int size = Integer.valueOf(params.getParam("size"));
			Page<Publish> pagePublish = new Page<Publish>();
			pagePublish.setSize(size);
		    pagePublish.setIndex(pageNumber);
			pagePublish = publishService.getPagePublishByStatus(status, pagePublish);
			responseData.put("pagePublish", pagePublish);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 发现有效期内未被承接的项目Page列表 (时间戳分页)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/discoverPublishProject.json",method=RequestMethod.POST)
	public void discoverPublishProject(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"time|R","pageSize|R","area","industry"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			long time = Long.parseLong(params.getParam("time"));
			int pageSize = Integer.valueOf(params.getParam("pageSize"));
			String area = params.getParam("area");
			String industry = params.getParam("industry");
			if(StringUtils.isBlank(area) && StringUtils.isBlank(industry)){
				warpMsg(SysCode.PARAM_IS_ERROR,"area-"+area+",industry-"+industry,params,response);
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("areaId", area);
			map.put("industryId", industry);
			List<Publish> resultList = projectService.getProjectListByParameters(time, pageSize, map);
			responseData.put("resultList", resultList);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}	
	
	/**
	 * 我发布的项目、已承接、未承接接口 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getAllPublishValidity.json",method=RequestMethod.POST)
	public void getPublishValidity(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"applyStatus"};//1 未承接、2已承接、3、已过期、非必填字段 ，空值 查询全部项目列表
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			User user = requestInfo.getUser();
            Integer applyStatus = StringUtils.isNotBlank(params.getParam("applyStatus"))? Integer.valueOf(params.getParam("applyStatus")):null; 
            
            List<Publish> publishs =null;
            long t1=System.currentTimeMillis();
            if(applyStatus==null ){//查询全部项目列表
            	publishs=publishService.getPublishByPublisherId(user.getId());
            }else{
            	publishs=publishService.getPublishByReviewerIdAndApplyStatus(user.getId(), applyStatus);
            }
            logger.info("getPublishByPublisherId--> "+(System.currentTimeMillis()-t1));
            t1=System.currentTimeMillis();
            CopyOnWriteArrayList<Publish> copyPublishs = new CopyOnWriteArrayList<Publish>(publishs);
            long tt2=0;
            for (Publish publish : copyPublishs) {
            	if(publish.getStatus() == ProjectStatus.Project_Publish_Complect.getKey() || 
            	   publish.getStatus() == ProjectStatus.Project_Publish_Abort.getKey()){
            	   copyPublishs.remove(publish);
            	   continue;
            	}
            	Set<Apply>  applySet = publish.getApplySet();
            	if(null != applySet && applySet.size() > 0){
            		for (Apply apply : applySet) {
            			long t2=System.currentTimeMillis();
            			OrganizationMember om = organizationMemberService.getMyOrganizationMemberDetail(apply.getOrganizationId(), apply.getProposerId());
            			//OrganizationMember om = organizationMemberService.getMemberByOrganizationIdAndUserId(apply.getOrganizationId(), apply.getProposerId());
            			tt2=tt2+(System.currentTimeMillis()-t2);
            			logger.info("getMyOrganizationMemberDetail-->"+tt2);
            			if(null != om){
            			   User u = getUserById(apply.getProposerId());	
            			   u.setPicPath(getUserPicURL(u));
            			   om.setUser(u);
            			   apply.setOrganizationMember(om);
            			}
            			else{
            			    User u = getUserById(apply.getProposerId());
            			    u.setPicPath(getUserPicURL(u));
            			    apply.setUser(u);
            			}
					}
            	}
            	User pu = getUserById(publish.getPublisherId());
            	if(pu!=null){
            		pu.setPicPath(getUserPicURL(pu));
            		publish.setPublisherUserName(pu.getUserName());
            	}else{
            		logger.warn ("not found user info by id "+publish.getPublisherId());
            	}
            	
			}
            logger.info("detail --> "+(System.currentTimeMillis()-t1));
            t1=System.currentTimeMillis();
			responseData.put("publishs", copyPublishs);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 检测项目是否到期
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/checkProjectIsExpire.do")
	public void checkProjectIsExpire(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = new ParamInfo();
		try{
			List<Publish> publishs =  publishService.checkProjectIsExpire();
			responseData.put("publishs", publishs);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 删除项目发布
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/del.json")
	public void del(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"projectId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			User user = requestInfo.getUser();
			long projectId = Long.valueOf(params.getParam("projectId"));
			List<Apply> applies = applyService.getMyApplysByProjectId(user.getId(), projectId, ProjectStatus.Project_Apply_Success.getKey());
			if(null != applies && applies.size() > 0){
				notification.put(WebConstants.NOTIFCODE,ApiCodes.ProjectHasBeenToUndertake.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.ProjectHasBeenToUndertake.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, notification)));	
			}
			else {
				Publish publish = publishService.updatePublish(projectId, ProjectStatus.Project_Publish_Discussed,null);
				boolean del_status = null != publish;
				responseData.put("status", del_status);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, notification)));	
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 根据项目ID、重新提交发布
	 * projectId  项目id
	 * startTime 开始时间
	 * endTime 结束时间
	 * cycle 项目周期
	 * 
	 */
	@RequestMapping(value="/resubmit.json",method=RequestMethod.POST)
	public void resubmit(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"projectId|R","delay"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			Long projectId = Long.valueOf( params.getParam("projectId") );
			Integer delay  = null != params.getParam("delay") ? Integer.valueOf(params.getParam("delay")):3;
			Publish publish = publishService.resubmit(projectId,delay);
	    	if(null != publish){
				responseData.put("publish", publish);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
	    	}
	    	else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
	    	}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	/**
	 * 发现有效期内未被承接的项目Page列表 (传统分页)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/discoverPublishProjectPage.json",method=RequestMethod.POST)
	public void discoverPublishProjects(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"pageNumber|R","area","industry"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			int pageNumber  = Integer.valueOf(params.getParam("pageNumber"));
			String area     =  null != params.getParam("area")? params.getParam("area"):null;
			String industry =  null != params.getParam("industry")? params.getParam("industry"):null;
			Page<Project> pageProject = new Page<Project>();
			pageProject = publishManageService.discoverPagePublishProject(pageNumber, area, industry);
			responseData.put("pageProject", pageProject);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}	
	/**
	 * 根据项目ID、重新提交发布
	 * projectId  项目id
	 * startTime 开始时间
	 * endTime 结束时间
	 * cycle 项目周期
	 * 
	 */
	@RequestMapping(value="/resubmit_v3.json",method=RequestMethod.POST)
	public void resubmit_v3(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"projectId|R","startDate","endDate|R","cycle"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			Integer cycle = Integer.valueOf(params.getParam("cycle"));
			String startDate = params.getParam("srartDate");
			String endDate = params.getParam("endDate");
			Long projectId = Long.valueOf( params.getParam("projectId") );
			
			Publish publish = publishService.resubmit_v3(projectId,ProjectStatus.Project_Publish_Resumit,startDate,endDate,cycle);
	    	if(null != publish){
				responseData.put("publish", publish);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
	    	}
	    	else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
	    	}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
}
