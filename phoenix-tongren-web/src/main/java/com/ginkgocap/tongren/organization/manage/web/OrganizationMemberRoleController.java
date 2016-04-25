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

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.manage.exception.MemberRoleException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
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
@RequestMapping("/manage/role")
public class OrganizationMemberRoleController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationMemberRoleController.class);
	
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@RequestMapping(value="/setAdminRole.json")
	@UserAccessPermission(role = OrganizationRoles.CREATER,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ADMIN_ADD)
	public void setAdminRole(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"organizationId|R","roleId|R","organizationMemberId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long roleId = Long.valueOf(params.getParam("roleId"));
			OrganizationRole role = organizationRoleService.getEntityById(roleId);
			if(null != role && OrganizationRoles.ADMIN.name().equals(role.getRoleName())){
				OrganizationMemberRole omr = setMemberRole(params, user);
				if(null != omr){
				   responseData.put("organizationRole", omr);
				   renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));		
				}
				else{
					notification.put(WebConstants.NOTIFCODE,ApiCodes.UserIsNotExist.getCode());
					notification.put(WebConstants.NOTIFINFO,ApiCodes.UserIsNotExist.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
				}
			}
			else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.RoleIdIsNotAdmin.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.RoleIdIsNotAdmin.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
			}
		}
		catch(MemberRoleException e){
			notification.put(WebConstants.NOTIFCODE,e.getErrorCode());
			notification.put(WebConstants.NOTIFINFO,e.getErrorMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		} 
	}

	@RequestMapping(value="/setUserRole.json")
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_MEMBER_ROLE_EDIT)
	public void setUserRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[] { "organizationId|R", "roleId|R", "organizationMemberId|R" };
		try {
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if (null == requestInfo)
				return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			
			// 不能改变自己角色
//			OrganizationMember organizationMember = organizationMemberService.getMemberByOrganizationIdAndUserId(
//					Long.valueOf(params.getParam("organizationId")), user.getId());
//			if (organizationMember.getId() == Long.valueOf(params.getParam("organizationMemberId"))) {
//				notification.put(WebConstants.NOTIFCODE, ApiCodes.NoEnoughAuthority.getCode());
//				notification.put(WebConstants.NOTIFINFO, ApiCodes.NoEnoughAuthority.getMessage());
//				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
//				return;
//			} 
			
			//设置管理员移动到单独接口
			OrganizationRole organizationRole = organizationRoleService.getEntityById(Long.parseLong(params.getParam("roleId")));
			if(organizationRole==null||organizationRole.getRoleName().equals(OrganizationRoles.ADMIN.name())){
				notification.put(WebConstants.NOTIFINFO, "不能设置管理员角色");
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
				return;
			}
			
			OrganizationMemberRole omr = setMemberRole(params, user);
			if (null != omr) {
				responseData.put("organizationRole", omr);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			} else {
				notification.put(WebConstants.NOTIFCODE, ApiCodes.UserIsNotExist.getCode());
				notification.put(WebConstants.NOTIFINFO, ApiCodes.UserIsNotExist.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
			}
		} catch (MemberRoleException e) {
			notification.put(WebConstants.NOTIFCODE, e.getErrorCode());
			notification.put(WebConstants.NOTIFINFO, e.getErrorMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			notification.put(WebConstants.NOTIFCODE, ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO, ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}

	/**
	 * 设置组织成员的角色
	 * @param params
	 * @param user
	 * @return
	 * @throws MemberRoleException
	 * @throws Exception
	 */
	private OrganizationMemberRole setMemberRole(ParamInfo params, User user) throws MemberRoleException, Exception {
		long createrId = user.getId();
		long organizationId = Long.valueOf(params.getParam("organizationId")) ;
		long roleId = Long.valueOf(params.getParam("roleId"));
		long organizationMemberId = Long.valueOf(params.getParam("organizationMemberId"));
		OrganizationMemberRole omr = organizationMemberRoleService.addMemberRole(createrId, organizationId, roleId, organizationMemberId);
		return omr;
	}
	
	/**
	 * 设置组织的某个成员为管理员角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/setManagerRole.json")
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_MEMBER_ROLE_EDIT)
	public void setManagerRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[] { "organizationId|R",  "organizationMemberId|R","type"}; //type 2 取消管理员, 非2 设置管理员
		try {
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if (null == requestInfo)
				return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long orgId=Long.valueOf(params.getParam("organizationId"));
			long omid=Long.valueOf(params.getParam("organizationMemberId"));
			// 不能改变自己角色
			OrganizationMember organizationMember = organizationMemberService.getMemberByOrganizationIdAndUserId(orgId, user.getId());
			if (organizationMember.getId() ==omid) {
				notification.put(WebConstants.NOTIFCODE, ApiCodes.NoEnoughAuthority.getCode());
				notification.put(WebConstants.NOTIFINFO, ApiCodes.NoEnoughAuthority.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
				return;
			} 
			if("2".equals(params.getParam("type"))){//取消管理员角色
				organizationMemberRoleService.deleteAdminRole(orgId,omid);
				List<OrganizationMemberRole> list=organizationMemberRoleService.getAllAdminRole(orgId);
				responseData.put("adminCount", list==null?0:list.size());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				return;
			}else{//设置管理员角色
				//查询该组织的管理员角色
				List<OrganizationMemberRole> list=organizationMemberRoleService.getAllAdminRole(orgId);
				if(list!=null&&list.size()>=3){//最多可以设置3个管理员
					notification.put(WebConstants.NOTIFINFO,"最多只能设置3个管理员");
					renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
					return;
				}
				OrganizationRole adminRole= organizationRoleService.getOrganizationRoleByOrganizationIdAndRoleName(orgId, "ADMIN");
				OrganizationMemberRole omr = organizationMemberRoleService.addMemberRole(user.getId(), orgId, adminRole.getId(), omid);
				if (null != omr) {
					responseData.put("organizationRole", omr);
					list=organizationMemberRoleService.getAllAdminRole(orgId);
					responseData.put("adminCount", list==null?0:list.size());
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				} else {
					notification.put(WebConstants.NOTIFCODE, ApiCodes.UserIsNotExist.getCode());
					notification.put(WebConstants.NOTIFINFO, ApiCodes.UserIsNotExist.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
				}
			}
		} catch (MemberRoleException e) {
			notification.put(WebConstants.NOTIFCODE, e.getErrorCode());
			notification.put(WebConstants.NOTIFINFO, e.getErrorMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			notification.put(WebConstants.NOTIFCODE, ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO, ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 获取某个组织下的所有管理员
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/getAllAdminRole.json")
	public void getAllAdminRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[] { "organizationId|R"}; //type 2 取消管理员, 非2 设置管理员
		try {
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if (null == requestInfo)
				return;
			//User user = requestInfo.getUser();
			params = requestInfo.getParams();
			long orgId=Long.valueOf(params.getParam("organizationId"));
			List<OrganizationMemberRole> list=organizationMemberRoleService.getAllAdminRole(orgId);
			responseData.put("success", list);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
					 
		}  catch (Exception e) {
			logger.error(e.getMessage(), e);
			notification.put(WebConstants.NOTIFCODE, ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO, ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}

}
