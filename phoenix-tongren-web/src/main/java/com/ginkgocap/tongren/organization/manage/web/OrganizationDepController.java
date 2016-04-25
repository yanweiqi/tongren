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
import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepManageService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;
import com.google.common.collect.Maps;

/**
 * @author yanweiqi
 *
 */
@Controller
@RequestMapping("/manage/dep")
public class OrganizationDepController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationDepController.class);
	
	@Autowired
	private OrganizationDepService organizationDepService;
	
	@Autowired
	private OrganizationDepManageService organizationDepManageService;
	
	@Autowired
	private OrganizationDepMemberService organizationDepMemberService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@RequestMapping(value="/getDeps.json",method=RequestMethod.POST)
	public void getOrganizationDeps(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
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
			logger.info("user id "+ createrId +" get organization " + organizationId + " dep.....");
			List<OrganizationDep> deps =  organizationDepService.getDepsByOrganizationId(organizationId);
			if(null != deps){
				for (OrganizationDep dep : deps) {
					List<OrganizationDepMember> depMembers = organizationDepMemberService.getDepMemberByOrganizationIdAndDepId(dep.getOrganizationId(),dep.getId());
					if(null != depMembers){
					   dep.setDepMembers(depMembers);
					   dep.addExtend("memberCount", depMembers.size());
					}else{
						  dep.addExtend("memberCount", new Integer(0));
					}
					
				}
				responseData.put("organizationDep", deps);
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
	
	@RequestMapping(value="/addDep.json",method=RequestMethod.POST)
	public void addDep(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String paramsKey[] = new String[]{"depName|R","description","organizationId|R","pid|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createrId = user.getId();
			String depName = params.getParam("depName");
			String description = params.getParam("description");
			long organizationId = Long.valueOf(params.getParam("organizationId"));
			long pid = Long.valueOf(params.getParam("pid"));
			logger.info("user id "+ createrId +" get organization " + organizationId + " dep.....");
			OrganizationDep dep = organizationDepManageService.addDep(createrId,organizationId, depName, description, pid);
			responseData.put("organizationDep", dep);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				
		
		}
		catch (NoPermissionException e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode());
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
	
	@RequestMapping(value="/editDep.json",method=RequestMethod.POST)
	public void updateDep(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] paramsKey = new String[]{"depName|R","description","organizationId|R","depId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createrId = user.getId();
			String depName = params.getParam("depName");
			String description = params.getParam("description");
			long organizationId = Long.valueOf(params.getParam("organizationId"));
			long depId = Long.valueOf(params.getParam("depId"));
			logger.info("user id "+ createrId +" get organization " + organizationId + " dep.....");
			OrganizationDep dep = organizationDepManageService.updateDepById(createrId, organizationId, depName, description, depId);
			responseData.put("organizationDep", dep);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				
		}
		catch (NoPermissionException e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode());
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
	
	@RequestMapping(value="/delDep.json",method=RequestMethod.POST)
	public void delDep(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
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
			long organizationId = Long.valueOf(params.getParam("organizationId"));
			long depId = Long.valueOf(params.getParam("depId"));
			logger.info("user id "+ createrId +" get organization " + organizationId + " dep.....");
			boolean status = organizationDepManageService.delDepById(createrId, organizationId,depId);
			responseData.put("status", status);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));				
	
		}
		catch (NoPermissionException e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode());
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
	
	@RequestMapping(value="/getDepTree.json",method=RequestMethod.POST)
	public void getOrganizationDepTree(HttpServletRequest request, HttpServletResponse response) throws ValiaDateRequestParameterException{
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] paramsKey = new String[]{"organizationId|R"};
		ParamInfo params = parseRequest(request,response,"getOrganizationRoles",paramsKey);
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			User user = getUser(response, request);
			long createrId = user.getId();
			long organizationId = Long.valueOf(params.getParam("organizationId")) ;
			logger.info("user id "+ createrId +" get organization " + organizationId + " dep.....");
			List<TreeNode<OrganizationDep>> deps =  organizationDepService.getTreeDepByOrganizationId(organizationId);
			responseData.put("organizationDep", deps);
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
