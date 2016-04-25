package com.ginkgocap.tongren.project.manage.web;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.Exception.ApplyException;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.tongren.project.manage.vo.ApplyVo;
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
public class ApplyController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(ApplyController.class);
	
	@Autowired
	private PublishService publishService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ApplyService applyService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	/**
	 * 申请项目
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/apply.json",method=RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"publisherId|R","organizationId","projectId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long projectId = Long.valueOf( params.getParam("projectId") );
		    long proposerId = user.getId();
		    long publisherId = Long.valueOf(params.getParam("publisherId"));
		    long organizationId = StringUtils.isNotEmpty(params.getParam("organizationId"))? Long.valueOf(params.getParam("organizationId")):0;
		    Project p = projectService.getEntityById(projectId);
		    if(null != p){
		    	Timestamp t = new Timestamp(System.currentTimeMillis());
		    	Apply apply = applyService.create(proposerId, organizationId, t, projectId, publisherId);
		    	responseData.put("apply", apply);
		    	renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
			}
		    else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.ProjectIsNotExist.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.ProjectIsNotExist.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		    }
		}catch(ApplyException ae){
			notification.put(WebConstants.NOTIFCODE,ae.getErrorCode());
			notification.put(WebConstants.NOTIFINFO, ae.getErrorMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 拒绝项目申请
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/refuse.json",method=RequestMethod.POST)
	public void refuse(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"proposerId|R","projectId|R","organizationId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long projectId = Long.valueOf( params.getParam("projectId") );
		    long reviewerId = user.getId();
		    long proposerId = Long.valueOf(params.getParam("proposerId"));
		    long organizationId = Long.valueOf(params.getParam("organizationId"));
		    Project p = projectService.getEntityById(projectId);
		    if(null != p){
		    	Apply apply = applyService.refuse(reviewerId, projectId, proposerId,organizationId);
				responseData.put("apply", apply);
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

	//接受项目申请
	@RequestMapping(value="/accept.json",method=RequestMethod.POST)
	public void accept(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"proposerId|R","projectId|R","organizationId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long projectId = Long.valueOf( params.getParam("projectId") );
		    long reviewerId = user.getId();
		    long proposerId = Long.valueOf(params.getParam("proposerId"));
		    long organizationId = Long.valueOf(params.getParam("organizationId"));
		    Project p = projectService.getEntityById(projectId);
		    if(null != p){
		    	Map<String, Object> apply = applyService.accept(reviewerId, projectId, proposerId,organizationId);
				responseData.put("apply", apply);
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
	 * 获取我申请的项目列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMyApplies.json",method=RequestMethod.POST)
	public void getMyApplies(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"applyStatus"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			Integer applyStatus = StringUtils.isNotEmpty(params.getParam("applyStatus"))?  Integer.valueOf( params.getParam("applyStatus") ):null;
	    	List<Apply> applies = applyService.getAppliesByProposerIdAndStatus(user.getId(), applyStatus);
			responseData.put("applies", applies);
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
	 * 根据项目ID查询项目申请人列表
	 */
	@RequestMapping(value="/getMyProjectApply.json",method=RequestMethod.POST)
	public void getMyProjectApply(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"projectId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			Long projectId = Long.valueOf( params.getParam("projectId") );
	    	List<Apply> applies = applyService.getApplyByProjectId(projectId);
	    	List<ApplyVo> applyVos = new ArrayList<ApplyVo>();
	    	for (Apply apply : applies) {
				ApplyVo vo = new ApplyVo();
				if(apply.getOrganizationId() != 0){
					long organizationId = apply.getOrganizationId();
					Organization organization = tongRenOrganizationService.getOrganizationById(organizationId);
					User user = userService.selectByPrimaryKey(organization.getCreaterId());
					vo.setCreaterId(organization.getCreaterId());
					vo.setCreaterName(user.getName());
					vo.setOrganizationId(organizationId);
					vo.setOrganizationLogo(getPathByTaskId(organization.getLogo()));
					vo.setOrganizationName(organization.getName());
					vo.setIsOrganization(true);
					vo.setProposerId(apply.getProposerId());
					List<OrganizationMember> organizationMembers = organizationMemberService.getNormalMember(organizationId);
					vo.setOrganizationMemberSize(organizationMembers.size());
				}
				else{
					User user = userService.selectByPrimaryKey(apply.getProposerId());
					vo.setIsOrganization(false);
					vo.setProposerfigureurl(getUserPicURL(user));
					vo.setProposerId(apply.getProposerId());
					vo.setProposerName(user.getName());
				}
				applyVos.add(vo);
			}
			responseData.put("applies", applyVos);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
}
