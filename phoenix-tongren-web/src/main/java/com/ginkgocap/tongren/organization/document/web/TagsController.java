package com.ginkgocap.tongren.organization.document.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.document.model.DocumentTags;
import com.ginkgocap.tongren.organization.document.service.DocumentTagsService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
/**
 * 标签服务
 * @author hanxifa
 *
 */
@Controller
@RequestMapping("/document/tags")
public class TagsController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TagsController.class);
 
	@Autowired
	private DocumentTagsService documentTagsService;
	
	/**
	 * 增加一个标签
	 */
	@RequestMapping(value = "/addTags.json", method = RequestMethod.POST)
	public void addTags(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "name|R","organizationId|R","type|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			long userId=getUserIdByType(ri);
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			params=ri.getParams();
			DocumentTags tags=new DocumentTags();
			tags.setName(params.getParam("name"));
			tags.setUserId(userId);
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			String code=documentTagsService.addTags(tags, orgId,1);
			logger.info("code is "+code);
			if(code.startsWith("101_")){
				responseData.put("id", code.substring(4));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				warpMsg(SysCode.ERROR_CODE,code,params,response,responseData);
			}
		} catch (Exception e) {
			logger.error("增加标签失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 根据请求参数获取用户id，组织资源时，由于可以为多个用户操作，所以返回一个固定的用户id，我的资源时返回当前用户id
	 * @param ri
	 * @return
	 */
	private long getUserIdByType(RequestInfo ri){
		ParamInfo params=ri.getParams();
		//type 1组织资源，2我的资源   3 组织人脉 4 组织知识
		 long userId=ConfigService.ORG_DEF_USER_ID;
		if("2".equals(params.getParam("type"))){//2我的资源
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
	 * 获取所有标签
	 */
	@RequestMapping(value = "/getAllTags.json", method = RequestMethod.POST)
	public void getAllTags(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "organizationId|R","type|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			params=ri.getParams();
			long userId=getUserIdByType(ri);
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			List<DocumentTags> list=documentTagsService.getAllTags(userId, orgId);
			responseData.put("list", list);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			logger.error("获取所有标签失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 删除标签
	 */
	@RequestMapping(value = "/delTagsById.json", method = RequestMethod.POST)
	public void delTagsById(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "tagId|R","type|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			params=ri.getParams();
			long userId=getUserIdByType(ri);
			String code=documentTagsService.delTagsById(userId, Long.parseLong(params.getParam("tagId")));
			logger.info("code is "+code);
			if(code.equals("101")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				warpMsg(SysCode.ERROR_CODE,code,params,response,responseData);
			}
		} catch (Exception e) {
			logger.error("删除标签失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 增加一个资源到指定的标签下
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addSourceToTags.json", method = RequestMethod.POST)
	public void addSourceToTags(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		// recommendTag金桐脑推荐的标签
		// 格式为:[{"tagid":"1112","tagName":"master"},{"tagid":"1113","tagName":"mobile"}]
		String paramsKey[] = { "tagId", "organizationId|R", "sourceId|R", "type|R", "recommendTag" };
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			params = ri.getParams();
			long orgId = getOrgIdByType(ri, Long.parseLong(params.getParam("organizationId")));
			long userId = getUserIdByType(ri);
			boolean isSuccess = true;
			long sourceId = Long.parseLong(params.getParam("sourceId"));
			boolean isRemoveAll = false;
			if (params.getParam("tagId") != null) {
				String[] array = params.getParam("tagId").split(",");
				if (array.length > 0) {
					documentTagsService.delAllTagsOfSource(sourceId, orgId);
					isRemoveAll = true;
				}
				for (String tid : array) {
					String code = documentTagsService
							.addSourceToTags(userId, Long.parseLong(tid), Long.parseLong(params.getParam("sourceId")), orgId);
					if (!code.equals("201")) {
						isSuccess = false;
					}
					logger.info("code is " + code);
				}
			}
			String recommendTag = params.getParam("recommendTag");
			if (StringUtils.hasText(recommendTag)) {
				logger.info("add recommendTag:" + recommendTag);
				JSONArray recTags = JSON.parseArray(recommendTag);
				if (recTags.size() > 0 && isRemoveAll == false) {
					documentTagsService.delAllTagsOfSource(sourceId, orgId);
				}

				for (int i = 0; i < recTags.size(); i++) {
					JSONObject jb = recTags.getJSONObject(i);
					String code = documentTagsService.addSourceToRecTags(userId, jb.getString("tagName"),
							Long.parseLong(params.getParam("sourceId")), orgId);
					logger.info(jb.getString("tagName") + "-->" + code);
					if (!code.equals("201")) {
						isSuccess = false;
					}
				}
			}
			if (isSuccess) {
				logger.info("succsss add recommendTag:" + recommendTag);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			} else {
				warpMsg(SysCode.ERROR_CODE, "增加标签失败", params, response, responseData);
			}

		} catch (Exception e) {
			logger.error("增加资源失败！ 参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}
	/**
	 * 获取标签下的所有资源
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAllSoucesFromTags.json", method = RequestMethod.POST)
	public void getAllSoucesFromTags(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "tagId|R","index|R","type|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			params=ri.getParams();
			Page<BasicBean> page =new Page<BasicBean>();
			page.addParam("tagsId", Long.parseLong(params.getParam("tagId")));
			int index=Integer.parseInt(params.getParam("index"));
			int pageSize=10;
			page.setIndex(index);
			page.setSize(pageSize);
			long userId=getUserIdByType(ri);
			page.addParam("userId", Long.valueOf(userId));
			page=documentTagsService.getAllSoucesFromTags(page);
			responseData.put("page", page);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			
		} catch (Exception e) {
			logger.error("获取标签下的资源失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 删除标签资源
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delSourceFromTags.json", method = RequestMethod.POST)
	public void delSourceFromTags(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "tagSourceId|R","type|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			params=ri.getParams();
			long userId=getUserIdByType(ri);
			String code=documentTagsService.delSourceFromTags(userId,Long.parseLong(params.getParam("tagSourceId")));
			logger.info("code is "+code);
			if(code.equals("401")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				warpMsg(SysCode.ERROR_CODE,code,params,response,responseData);
			}
		} catch (Exception e) {
			logger.error("删除标签资源失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 获取资源关联的标签
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAssociateTagsBySourceId.json", method = RequestMethod.POST)
	public void getAssociateTagsBySourceId(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		String paramsKey[] = { "sourceId|R","organizationId|R","type|R"};
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params=ri.getParams();
			long orgId=getOrgIdByType(ri,Long.parseLong(params.getParam("organizationId")));
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			long userId=getUserIdByType(ri);
			List<DocumentTags> list=documentTagsService.getAssociateTagsBySourceId(userId,Long.parseLong(params.getParam("sourceId")),orgId);
			responseData.put("list", list);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			logger.error("获取资源关联的标签失败！ 参数："+request.getParameter("requestJson"),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	
}
