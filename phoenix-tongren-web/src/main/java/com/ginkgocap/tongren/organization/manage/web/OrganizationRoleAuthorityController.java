package com.ginkgocap.tongren.organization.manage.web;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.model.OrganizationRoleAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationRoleAuthorityService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleAuthorityManageService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;
import com.ginkgocap.ywxt.user.model.User;
import com.google.common.collect.Maps;

/**
 * 
 * @author yanweiqi
 *
 */
@Controller
@RequestMapping("/manage/auth/")
public class OrganizationRoleAuthorityController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationRoleAuthorityController.class);
	
	@Autowired
	private OrganizationRoleAuthorityManageService organizationRoleAuthorityManageService;	
	
	@Autowired
    private OrganizationRoleAuthorityService organizationRoleAuthorityService;
	
	@RequestMapping(value="/getMyAuthoritys.json")
	public void getMyAuthoritys(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();	
		String[] paramsKey = new String[]{"organizationId|R","roleId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			long roleId = Long.valueOf(params.getParam("roleId"));
			Map<String, OrganizationAuthority> roleAuthorityMap = organizationRoleAuthorityManageService.getMyAuthoritys(roleId, organizationId);
			if(null != roleAuthorityMap){
			   List<OrganizationAuthority> authoritys = new LinkedList<OrganizationAuthority>();
			   for (Map.Entry<String, OrganizationAuthority> entry: roleAuthorityMap.entrySet()) {
				   authoritys.add(entry.getValue());
			   }
			   responseData.put("authoritys", authoritys);
			   renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));		
			}
			else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.UserIsNotExist.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.UserIsNotExist.getMessage());
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
	
	@RequestMapping(value="/setRoleAuthoritys.json")
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_AUTHORITY_ADD)
	public void setMyAuthoritys(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"organizationId|R","roleId|R","authorityNames|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			long roleId = Long.valueOf(params.getParam("roleId"));
			long createrId = user.getId();
			String[] authorityNames = params.getParam("authorityNames").split(",");
			List<OrganizationRoleAuthority> organizationRoleAuthorities = organizationRoleAuthorityService.addRoleAuthorities(createrId, roleId, organizationId, Arrays.asList(authorityNames));
			if(null != organizationRoleAuthorities && organizationRoleAuthorities.size() > 0){
				responseData.put("organizationRoleAuthorities", organizationRoleAuthorities);
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
