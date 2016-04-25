package com.ginkgocap.tongren.organization.manage.web;


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
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.exception.DepMemberException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepMemberManageService;
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
@RequestMapping("/manage/depMember")
public class OrganizationDepMemberController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationDepMemberController.class);
	
	@Autowired
	private OrganizationDepMemberManageService organizationDepMemberManageService;	
	
	@RequestMapping(value="/add.json")
	public void add(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] paramsKey = new String[]{"organizationId|R","depId|R","organizationMemberId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);	
		    long createrId = user.getId();
		    long organizationId = Long.valueOf(params.getParam("organizationId")) ;
		    long depId = Long.valueOf(params.getParam("depId"));
		    long organizationMemberId = Long.valueOf(params.getParam("organizationMemberId"));
		    OrganizationDepMember odm = organizationDepMemberManageService.addMember(createrId, organizationId, depId, organizationMemberId);
		    responseData.put("organizationDepMember", odm);
		    renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch(DepMemberException e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.DepMemberIsExist.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.DepMemberIsExist.getMessage());
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
	}
	
	@RequestMapping(value="/del.json")
	public void del(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] paramsKey = new String[]{"organizationId|R","depId|R","organizationMemberId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
		    long createrId = user.getId();
		    long organizationId = Long.valueOf(params.getParam("organizationId")) ;
		    long depId = Long.valueOf(params.getParam("depId"));
		    long organizationMemberId = Long.valueOf(params.getParam("organizationMemberId"));
		    boolean status = organizationDepMemberManageService.delDepMemberByMemberId(createrId, organizationId, depId, organizationMemberId);
		    responseData.put("status", status);
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
	}	
	
	@RequestMapping(value="/delAll.json")
	public void delAll(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] paramsKey = new String[]{"organizationId|R","depId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
		    long createrId = user.getId();
		    long organizationId = Long.valueOf(params.getParam("organizationId")) ;
		    long depId = Long.valueOf(params.getParam("depId"));
		    boolean status = organizationDepMemberManageService.delDepMemberByDepId(createrId, organizationId, depId);
		    responseData.put("status", status);
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
	}	

}
