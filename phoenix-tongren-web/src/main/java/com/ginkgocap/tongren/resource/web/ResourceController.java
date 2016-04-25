/*
 * 文件名： ResourceController.java
 * 创建日期： 2015年11月3日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.resource.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.util.ChineseToEnglish;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.PostUtil;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.LocalCacheBeanWrap;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelated;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelatedDetail;
import com.ginkgocap.tongren.organization.resources.service.ResourcesRelatedService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.resource.vo.ResourceVO;
import com.ginkgocap.tongren.resources.service.ResourcesService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.user.model.User;


 /**
 * 文件类资源控制层
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月3日
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
	@Autowired
	private ResourcesService resourcesService;//资源接口
	@Autowired
	private FileIndexService fileIndexService;//文件接口
 
	@Autowired
	private ResourcesRelatedService resourcesRelatedService;//关联
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * 功能描述：   获得某一个项目中的提交文档资源      
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getResourceProject.json", method = RequestMethod.POST)
	public void getResourceProject(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"organizationId|R","projectId|R"};
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			List<OrganizationObject>  orgProObjs = resourcesService.getOrgObject(Long.valueOf(params.getParam("organizationId")), Long.valueOf(params.getParam("projectId")));
			if(orgProObjs == null || orgProObjs.size() == 0){
				notification.put("notifyCode", SysCode.RESOURCE_LIST_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_LIST_EMPTY);
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_LIST_EMPTY, genResponseData(null, notification)));
				return;
			}
			List <ResourceVO> returnList = new ArrayList<ResourceVO>();
			for (OrganizationObject organizationObject : orgProObjs) {
				ResourceVO vo = new ResourceVO();
				BeanUtils.copyProperties(vo, organizationObject);
				vo.setCreateName(getUserNameById(organizationObject.getCreateId()));
				returnList.add(getUrlByTaskId(vo, organizationObject.getTaskId()));
			}
			responseData.put("resourceList", returnList);
			responseData.put("total", returnList.size());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("get resourceProject failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：获得组织管理下的组织资源列表         
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getOrgResource.json", method = RequestMethod.POST)
	public void getOrgResource(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"organizationId|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			List<OrganizationObject> orgProObjs = resourcesService.getOrgObject(Long.valueOf(params.getParam("organizationId")), 0);
			if(orgProObjs == null || orgProObjs.size() == 0){
				notification.put("notifyCode", SysCode.RESOURCE_LIST_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_LIST_EMPTY);
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_LIST_EMPTY, genResponseData(null, notification)));
				return;
			}
			List <ResourceVO> returnList = new ArrayList<ResourceVO>();
			for (OrganizationObject organizationObject : orgProObjs) {
				ResourceVO vo = new ResourceVO();
				BeanUtils.copyProperties(vo, organizationObject);
				vo.setCreateName(getUserNameById(organizationObject.getCreateId()));
				returnList.add(getUrlByTaskId(vo, organizationObject.getTaskId()));
			}
			responseData.put("resourceList", returnList);
			responseData.put("total", returnList.size());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("get OrgResource failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	/**
	 * 功能描述：分页获得组织管理下的组织资源列表         
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getOrgResourcePage.json", method = RequestMethod.POST)
	public void getOrgResourcePage(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"organizationId|R","index|R","lstr"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			List<OrganizationObject> orgProObjs = resourcesService.getOrgObject(Long.valueOf(params.getParam("organizationId")), 0);
			if(orgProObjs == null || orgProObjs.size() == 0){
				notification.put("notifyCode", SysCode.RESOURCE_LIST_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_LIST_EMPTY);
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_LIST_EMPTY, genResponseData(null, notification)));
				return;
			}
			Page<ResourceVO> page=new Page<ResourceVO>();
			page.setIndex(Integer.parseInt(params.getParam("index")));
			page.setSize(10);//每页显示20条数据
			String lstr=params.getParam("lstr");
			List <ResourceVO> returnList = new ArrayList<ResourceVO>();
			for (OrganizationObject organizationObject : orgProObjs) {
				ResourceVO vo = new ResourceVO();
				BeanUtils.copyProperties(vo, organizationObject);
				vo.setCreateName(getUserNameById(organizationObject.getCreateId()));
				ResourceVO rvo=getUrlByTaskId(vo, organizationObject.getTaskId());
				if(lstr==null||lstr.trim().length()==0){
					returnList.add(rvo);
				}else{
					if(rvo.getTitleName()!=null){
						if(rvo.getTitleName().contains(lstr)){
							String title=rvo.getTitleName();
							title=title.replaceAll(lstr, "<font class=\"highlight\">"+lstr+"</font>");
							rvo.setTitleName(title);
							returnList.add(rvo);
						}else if(ChineseToEnglish.getSpellByWord(rvo.getTitleName().charAt(0)).toLowerCase().equals(lstr.toLowerCase())){
							char firstChar=rvo.getTitleName().charAt(0);
							rvo.setTitleName("<font class=\"highlight\">"+firstChar+"</font>"+rvo.getTitleName().substring(1));
							returnList.add(rvo);
						}
						
					}
				}
			}
			int tc=returnList.size();
			page.setTotalCount(tc);
			if(page.getStart()<tc){
				int end=page.getEnd()>=tc?tc:page.getEnd();
				page.setResult(returnList.subList(page.getStart(),end));
			}
			responseData.put("page", page);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("get OrgResource failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：    删除组织资源文件 
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/delOrgResource.json")
	public void delOrgResource(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"ids|R","organizationId|R"};
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			String idsStr = params.getParam("ids");
			List<Long> ids = JSON.parseArray(idsStr, Long.class);
			if(resourcesService.deleteResources(ids, 1)){//删除成功
				List<OrganizationObject> orgProObjs = resourcesService.getOrgObject(Long.valueOf(params.getParam("organizationId")), 0);
				List <ResourceVO> returnList = new ArrayList<ResourceVO>();
				if(orgProObjs != null){
					for (OrganizationObject organizationObject : orgProObjs) {
						ResourceVO vo = new ResourceVO();
						BeanUtils.copyProperties(vo, organizationObject);
						vo.setCreateName(getUserNameById(organizationObject.getCreateId()));
						returnList.add(getUrlByTaskId(vo, organizationObject.getTaskId()));
					}
				}
				responseData.put("resourceList", returnList);
				responseData.put("total", returnList.size());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				notification.put("notifyCode", SysCode.RESOURCE_DELETE_ERR.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_DELETE_ERR);
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_DELETE_ERR, genResponseData(null, notification)));
				return;
			}
		} catch (Exception e) {
			logger.error("get delOrgResource failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：  获得我的某个组织下的本地资源列表--我的资源       
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getMyOrgResourcePage.json", method = RequestMethod.POST)
	public void getMyOrgResourcePage(HttpServletRequest request,HttpServletResponse response){
		//name查询条件
		String[] paramKey = {"organizationId|R","index|R","lstr"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			
			Page<ResourceVO> page=new Page<ResourceVO>();
			page.setIndex(Integer.parseInt(params.getParam("index")));
			page.setSize(10);//每页显示20条数据
			String lstr=params.getParam("lstr");
			List<LocalObject> localObjS = resourcesService.getLocalObj(Long.valueOf(params.getParam("organizationId")), user.getId());
			if(localObjS != null && localObjS.size()>0){
				List <ResourceVO> returnList = new ArrayList<ResourceVO>();
				for (LocalObject localObject : localObjS) {
					ResourceVO vo = new ResourceVO();
					BeanUtils.copyProperties(vo, localObject);
					vo.setCreateName(getUserNameById(localObject.getCreateId()));
					ResourceVO rvo=getUrlByTaskId(vo, localObject.getTaskId());
					if(lstr==null||lstr.trim().length()==0){
						returnList.add(rvo);
					}else{
						if(rvo.getTitleName()!=null){
							if(rvo.getTitleName().contains(lstr)){
								String title=rvo.getTitleName();
								title=title.replaceAll(lstr, "<font class=\"highlight\">"+lstr+"</font>");
								rvo.setTitleName(title);
								returnList.add(rvo);
							}else if(ChineseToEnglish.getSpellByWord(rvo.getTitleName().charAt(0)).toLowerCase().equals(lstr.toLowerCase())){
								char firstChar=rvo.getTitleName().charAt(0);
								rvo.setTitleName("<font class=\"highlight\">"+firstChar+"</font>"+rvo.getTitleName().substring(1));
								returnList.add(rvo);
							}
							
						}
					}
					
				}
				int tc=returnList.size();
				page.setTotalCount(tc);
				if(page.getStart()<tc){
					int end=page.getEnd()>=tc?tc:page.getEnd();
					page.setResult(returnList.subList(page.getStart(),end));
				}
				
			}
			responseData.put("page", page);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("get resourceProject failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	/**
	 * 功能描述：  获得我的某个组织下的本地资源列表--我的资源       
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getMyOrgResource.json", method = RequestMethod.POST)
	public void getMyOrgResource(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"organizationId|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			List<LocalObject> localObjS = resourcesService.getLocalObj(Long.valueOf(params.getParam("organizationId")), user.getId());
			if(localObjS == null || localObjS.size() == 0){
				notification.put("notifyCode", SysCode.RESOURCE_LIST_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_LIST_EMPTY.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_LIST_EMPTY, genResponseData(null, notification)));
				return;
			}
			List <ResourceVO> returnList = new ArrayList<ResourceVO>();
			for (LocalObject localObject : localObjS) {
				ResourceVO vo = new ResourceVO();
				BeanUtils.copyProperties(vo, localObject);
				vo.setCreateName(getUserNameById(localObject.getCreateId()));
				returnList.add(getUrlByTaskId(vo, localObject.getTaskId()));
			}
			responseData.put("resourceList", returnList);
			responseData.put("total", returnList.size());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("get resourceProject failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：   获得我的资源列表    
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getMyResource.json", method = RequestMethod.GET)
	public void getMyResource(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,null);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			List<LocalObject> localObjS = resourcesService.getLocalObj(0, user.getId());
			if(localObjS == null || localObjS.size() == 0){
				notification.put("notifyCode", SysCode.RESOURCE_LIST_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_LIST_EMPTY);
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_LIST_EMPTY, genResponseData(null, notification)));
				return;
			}
			List <ResourceVO> returnList = new ArrayList<ResourceVO>();
			for (LocalObject localObject : localObjS) {
				ResourceVO vo = new ResourceVO();
				BeanUtils.copyProperties(vo, localObject);
				vo.setCreateName(getUserNameById(localObject.getCreateId()));
				returnList.add(getUrlByTaskId(vo, localObject.getTaskId()));
			}
			responseData.put("resourceList", returnList);
			responseData.put("total", returnList.size());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("get resourceProject failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：    删除我的资源文件     
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/delMyResource.json")
	public void delMyResource(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"ids|R"};
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			String idsStr = params.getParam("ids");
			List<Long> ids = JSON.parseArray(idsStr, Long.class);
			if(resourcesService.deleteResources(ids, 2)){//删除成功
				List<LocalObject> localObjS = resourcesService.getLocalObj(0, user.getId());
				List <ResourceVO> returnList = new ArrayList<ResourceVO>();
				if(localObjS != null){
					for (LocalObject localObject : localObjS) {
						ResourceVO vo = new ResourceVO();
						BeanUtils.copyProperties(vo, localObject);
						vo.setCreateName(getUserNameById(localObject.getCreateId()));
						returnList.add(getUrlByTaskId(vo, localObject.getTaskId()));
					}
				}
				responseData.put("resourceList", returnList);
				responseData.put("total", returnList.size());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				notification.put("notifyCode", SysCode.RESOURCE_DELETE_ERR.getCode());
				notification.put("notifyMessage", SysCode.RESOURCE_DELETE_ERR);
				renderResponseJson(response, params.getResponse(SysCode.RESOURCE_DELETE_ERR, genResponseData(null, notification)));
				return;
			}
		} catch (Exception e) {
			logger.error("get delMyResource failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	/**
	 * 获取资源的关联信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getResourceRelated_v3.json")
	public void getResourceRelatedv3(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"resourceId|R"};
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			//User user  =ri.getUser();
			List<ResourcesRelated> rlist=resourcesRelatedService.getResourcesRelatedByResourceId(Long.parseLong(params.getParam("resourceId")),1, true);
			for(ResourcesRelated related:rlist){
				String url=FileInstance.getValue("tongren.related.url", "http://www.gintong.com/cross");
				logger.info("url is --> "+url);
				addExtendDetail(url,(String)request.getSession().getAttribute("sessionId"),related.getRelatedType(),related.getDetail());
			}
			responseData.put("success", rlist);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			 
		} catch (Exception e) {
			logger.error("getResourceRelated failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	private void addExtendDetail(String uri,String sessonId,int type,List<ResourcesRelatedDetail> list) throws Exception{
		if(list==null){
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		PostUtil pt = new PostUtil("sessionID=\""+sessonId+"\"", "");
		Map<String, JSONObject> rsMap=new HashMap<String, JSONObject>();
		if(type==1){//人脉
			params.put("type", "2");
			String str=pt.testURIWithBody(uri+"/knowledge/getKnowledgeRelatedResources.json", params);
			rsMap=fetchToMap(str,"listPlatformPeople","listUserPeople");//2
		}else if(type==2){//组织
			params.put("type", "3");
			String str=pt.testURIWithBody(uri+"/knowledge/getKnowledgeRelatedResources.json", params);
			rsMap=fetchToMap(str,"listPlatformOrganization","listUserOrganization");
		}else if(type==3){//知识
			params.put("type", "4");
			String str=pt.testURIWithBody(uri+"/knowledge/getKnowledgeRelatedResources.json", params);
			rsMap=fetchToMap(str,"listUserKnowledge","listPlatformKnowledge");
		}else if(type==4){//事件
			params.put("type", "1");
			String str=pt.testURIWithBody(uri+"/knowledge/getKnowledgeRelatedResources.json", params);
			rsMap=fetchToMap(str,"listPlatformAffair","listUserAffair");
		}
		for(ResourcesRelatedDetail rrd:list){
			rrd.addExtend("info", rsMap.get(rrd.getRelatedId()+""));
		}
	}
	@SuppressWarnings("unchecked")
	private Map<String, JSONObject> fetchToMap(String jsonStr,String key1,String key2){
		JSONObject jsonobj=JSON.parseObject(jsonStr);
		JSONObject responseData=jsonobj.getJSONObject("responseData");
		Map<String, JSONObject> map=new HashMap<String, JSONObject>();
		if(responseData==null){
			return map;
		}
		List<Object> list=(List<Object>) responseData.get(key1);
		if(list!=null){
			for(Object obj:list){
				JSONObject people=(JSONObject) obj;
				map.put(people.getString("id"), people);
			}
		}
		
		list=(List<Object>) responseData.get(key2);
		if(list!=null){
			for(Object obj:list){
				JSONObject people=(JSONObject) obj;
				map.put(people.getString("id"), people);
			}
		}
		return map;
	}
	
	/**
	 * 根据taskId获取url信息
	 * @param taskId
	 * @return
	 */
	private static Map<String, LocalCacheBeanWrap<FileIndex>> fileCache=new ConcurrentHashMap<String, LocalCacheBeanWrap<FileIndex>>();
	private ResourceVO getUrlByTaskId(ResourceVO vo,String taskId) throws Exception{
		LocalCacheBeanWrap<FileIndex> lcb=fileCache.get(taskId);
		if(lcb==null||lcb.isValid()==false){
			List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
			FileIndex fi=null;
			if(list.size()>0){
				fi=list.get(0);
			}
			lcb=new LocalCacheBeanWrap<FileIndex>(fi, 86400000);//缓存一天
			fileCache.put(taskId, lcb);
		}
		FileIndex findex=lcb.getBean();
		if(findex!=null){
			vo.setTitleName(findex.getFileTitle() == null ? null:findex.getFileTitle());
			vo.setPath(findex.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + findex.getFilePath()+ "/"+findex.getFileTitle());
		}
		return vo;
	}
	
}
