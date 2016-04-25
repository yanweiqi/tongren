package com.ginkgocap.tongren.organization.document.web;

import java.io.Serializable;
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

import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;
import com.ginkgocap.tongren.organization.document.service.DocumentCatalogueService;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.system.code.SysCode;
/**
 * 目录服务
 * @author hanxifa
 *
 */
@Controller
@RequestMapping("/document/catalog")
public class CatalogController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

	@Autowired
	private DocumentCatalogueService documentCatalogueService;
	
	
	/***
	 * 增加一个目录
	 * 101_id成功 102创建失败 103 此组织下的文档根目录已经存在 104指定的父id不存在
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addCatalog.json", method = RequestMethod.POST)
	public void addCatalog(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		//1 组织资源，2 我的资源 3 组织人脉 4 组织知识
		String paramsKey[] = { "organizationId|R","name|R","pid|R","type|R"};
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params=ri.getParams();
			long userId=getUserIdByType(ri);
			DocumentCatalogue catalogue=new DocumentCatalogue();
			catalogue.setName(params.getParam("name"));
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			catalogue.setOrganizationId(orgId);
			catalogue.setUserId(userId);
			catalogue.setPid(Long.parseLong(params.getParam("pid")));
			String code=documentCatalogueService.addCatalog(catalogue);
			if(code.startsWith("101_")){//成功的状态码已101开始
				responseData.put("id", code.substring(4));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				warpMsg(SysCode.ERROR_CODE,code,params,response,responseData);
			}
			 
		} catch (Exception e) {
			logger.error("创建目录失败, 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	private long getUserIdByType(RequestInfo ri){
		ParamInfo params=ri.getParams();
		//type 1组织资源，2我的资源   3 组织人脉 4 组织知识
		 long userId=ConfigService.ORG_DEF_USER_ID;
		if("2".equals(params.getParam("type"))){//type 1组织资源，2我的资源
			userId=ri.getUser().getId();
		}
		return userId;
	}
	/**
	 * 
	 * @param ri
	 * @return
	 */
	private long getOrgIdByType(RequestInfo ri,long orgId){
		ParamInfo params=ri.getParams();
		//type 1组织资源，2我的资源  3 组织人脉 4 组织知识
		String type=params.getParam("type");
		if("1".equals(type)||"2".equals(type)){//type 1组织资源，2我的资源
			return orgId;
		}else if("3".equals(type)){
			return Long.parseLong("30"+orgId);
		}else if("4".equals(type)){
			return Long.parseLong("40"+orgId);
		}else{
			return orgId;
		}
	}
	/**
	 * 获取根目录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getRootCatalogs.json", method = RequestMethod.POST)
	public void getRootCatalogs(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "organizationId|R","type|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			long userId=getUserIdByType(ri);
			params=ri.getParams();
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			DocumentCatalogue dc=documentCatalogueService.getRootCatalogs(userId, orgId);
			responseData.put("rootCatalog", dc);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			logger.error("获取根目录失败, 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 获取一个目录下的所有资源，包含目录和文件
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getResourceOfCatalog.json", method = RequestMethod.POST)
	public void getCatalogsById(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "catalogId|R","index|R","type|R"};//index为第几页
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params=ri.getParams();
			Page<Serializable> page=new Page<Serializable>();
			page.setIndex(Integer.parseInt(params.getParam("index")));
			page.setSize(10);
			long userId=getUserIdByType(ri);
			List<DocumentCatalogue> clist=documentCatalogueService.getCatalogsById(userId, Long.parseLong(params.getParam("catalogId")));
			List<BasicBean> rlist=documentCatalogueService.getDocumentsById(userId, Long.parseLong(params.getParam("catalogId")));
			
			page.setTotalCount(clist.size()+rlist.size());
			
			List<Serializable> resultList=new ArrayList<Serializable>();
			page.setResult(resultList);
			int start=page.getStart();
			for(;start<page.getEnd();start++){
				if(start<clist.size()){
					DocumentCatalogue dc=clist.get(start);
					dc.addExtend("type", "c");
					resultList.add(dc);
				}else{
					break;
				}
			}
		
			for(;start<page.getEnd();start++){
				int index=start-clist.size();
				if(index<rlist.size()){
					BasicBean lo=rlist.get(index);
					lo.addExtend("type", "d");
					resultList.add(lo);
				}else{
					break;
				}
			}
			responseData.put("page", page);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			logger.error("获取一个目录下的所有资源失败, 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	
	/**
	 * 获取一个目录下的所有子目录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getSubCatalogs.json", method = RequestMethod.POST)
	public void getSubCatalogs(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "catalogId|R","type|R"};//index为第几页
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params=ri.getParams();
			long userId=getUserIdByType(ri);
			List<DocumentCatalogue> clist=documentCatalogueService.getCatalogsById(userId, Long.parseLong(params.getParam("catalogId")));
			responseData.put("catalogs", clist);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			logger.error("获取一个目录下的所有子目录失败, 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 更新目录名称
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateCatalogueName.json", method = RequestMethod.POST)
	public void updateCatalogueName(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "catalogId|R","name|R","type|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params=ri.getParams();
			long userId=getUserIdByType(ri);
			String code=documentCatalogueService.updateCatalogueName(userId, Long.parseLong(params.getParam("catalogId")), params.getParam("name"));
			logger.info("code is "+code);
			if("401".equals(code)){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else if("404".equals(code)){
				warpMsg(SysCode.DOCUMENT_ALREADY_EXISTS,SysCode.DOCUMENT_ALREADY_EXISTS.getMessage(),params,response,responseData);
			}else{
				warpMsg(SysCode.ERROR_CODE,code,params,response,responseData);
			}
			
		} catch (Exception e) {
			logger.error("更新目录名称失败, 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 设置一个资源所属目录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/setDocumentToDirs.json", method = RequestMethod.POST)
	public void setDocumentToCatalogs(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "organizationId|R", "resourceId|R", "catalogIds|R" ,"type|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String dirs[] = params.getParam("catalogIds").split(",");
			long[] cataIds = new long[dirs.length];
			int index = 0;
			for (String dir : dirs) {
				cataIds[index++] = Long.parseLong(dir);
			}
			long userId=getUserIdByType(ri);
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			String code = documentCatalogueService.setDocumentToDirs(userId, orgId,Long.parseLong(params.getParam("resourceId")), cataIds);
			logger.info("code is " + code);
			if ("601".equals(code)) {
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			} else {
				warpMsg(SysCode.ERROR_CODE, code, params, response, responseData);
			}

		} catch (Exception e) {
			logger.error("设置一个资源所属目录失败, 参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}
	
	/**
	 * 获取文档目录树，并标识某一个文档在那个目录下
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getCatalogueTree.json", method = RequestMethod.POST)
	public void getCatalogueTree(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "resourceId|R", "organizationId|R","type|R" };
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			long userId=getUserIdByType(ri);
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			DocumentCatalogue dc=documentCatalogueService.getCatalogueTree(userId,orgId, Long.parseLong(params.getParam("resourceId")));
			responseData.put("dtree", dc);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		
		} catch (Exception e) {
			logger.error("获取文档目录树失败, 参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}
	
	/**
	 * 删除目录下的一个资源
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/deleteResourceOfCatalogue.json", method = RequestMethod.POST)
	public void deleteResourceOfCatalogue(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "catalogResourceId|R" ,"type|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			long userId=getUserIdByType(ri);
			String code=documentCatalogueService.deleteResourceOfCatalogue(userId, Long.parseLong(params.getParam("catalogResourceId")));
			logger.info("code is "+code);
			if(code.equals("501")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else {
				warpMsg(SysCode.ERROR_CODE, code, params, response, responseData);
			}
		} catch (Exception e) {
			logger.error("删除资源失败, 参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}
	
	/**
	 * 删除目录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/deleteCatalogue.json", method = RequestMethod.POST)
	public void deleteCatalogue(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "catalogId|R","type|R" };
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			long userId=getUserIdByType(ri);
			String code=documentCatalogueService.deleteCatalogue(userId, Long.parseLong(params.getParam("catalogId")));
			logger.info("code is "+code);
			if(code.equals("501")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else {
				warpMsg(SysCode.ERROR_CODE, code, params, response, responseData);
			}
		} catch (Exception e) {
			logger.error("删除目录失败, 参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}
	
	
}
