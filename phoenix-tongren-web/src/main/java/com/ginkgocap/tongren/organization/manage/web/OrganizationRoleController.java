package com.ginkgocap.tongren.organization.manage.web;


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

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.exception.MemberRoleException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleManageService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;
import com.google.common.collect.Maps;

/**
 * 
 * @author yanweiqi
 *
 */
@Controller
@RequestMapping("/manage/role")
public class OrganizationRoleController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationRoleController.class);
	
	@Autowired
	private OrganizationRoleManageService organizationRoleManageService;	
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@RequestMapping(value="/add.json",method = RequestMethod.POST)
	public void add(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();		
		String[] paramsKey = new String[]{"organizationId|R","roleName|R","description"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createId = user.getId();
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			String roleName     = params.getParam("roleName");
			String description  = params.getParam("description");
			OrganizationRole or =  organizationRoleManageService.addRole(createId, organizationId, roleName, description);
			responseData.put("organizationRole", or);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				

		}
		catch(NoPermissionException e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode() );
			notification.put(WebConstants.NOTIFINFO,e.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Throwable e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	@RequestMapping(value="/editRole.json",method=RequestMethod.POST)
	public void editRole(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();		
		String[] paramsKey = new String[]{"organizationId|R","roleName|R","description","roleId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createrId = user.getId();
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			long roleId         = Long.valueOf(params.getParam("roleId"));
			String roleName     = params.getParam("roleName");
			organizationRoleManageService.checkRoleName(roleId,null);
			String description  = params.getParam("description");
			OrganizationRole or =  organizationRoleManageService.updateRole(createrId, organizationId, roleName, description, roleId);
			responseData.put("organizationRole", or);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				
		}
		catch(NoPermissionException e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode() );
			notification.put(WebConstants.NOTIFINFO,e.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Throwable e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	@RequestMapping(value="/delRole.json",method=RequestMethod.POST)
	public void delRole(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();	
		String[] paramsKey = new String[]{"roleId|R","organizationId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createrId = user.getId();
			long roleId         = Long.valueOf(params.getParam("roleId"));
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			organizationRoleManageService.checkRoleName(roleId,organizationId);
			boolean status =  organizationRoleManageService.delRole(createrId, organizationId, roleId);
			responseData.put("organizationRole", status);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				
		}
		catch(MemberRoleException e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrorCode() );
			notification.put(WebConstants.NOTIFINFO,e.getMessage()+","+e.getErrorMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch(NoPermissionException e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode() );
			notification.put(WebConstants.NOTIFINFO,e.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Throwable e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	@RequestMapping(value="/getOrganizationRoles.json",method=RequestMethod.POST)
	public void getOrganizationRoles(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] paramsKey = new String[]{"organizationId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createrId = user.getId();
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			List<OrganizationRole> roles =  organizationRoleManageService.getRolesByOrgazationId(createrId, organizationId);
			responseData.put("organizationRole", roles);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				
		}
		catch(NoPermissionException e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode() );
			notification.put(WebConstants.NOTIFINFO,e.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Throwable e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}	
	
	@RequestMapping(value="/getMyRole.json",method=RequestMethod.POST)
	public void getMyRole(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();		
		String[] paramsKey = new String[]{"organizationId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = ri.getUser();
			long organizationId = Long.valueOf(params.getParam("organizationId"));
			List<OrganizationRole> roles =  organizationRoleService.getMyOrganizationRole(user.getId(), organizationId);;
			responseData.put("organizationRole", roles);
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
