package com.ginkgocap.tongren.resource.web;

import java.sql.Timestamp;
import java.util.HashMap;
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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.PostUtil;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.OrganizationKd;
import com.ginkgocap.tongren.organization.manage.model.OrganizationPs;
import com.ginkgocap.tongren.organization.manage.service.OrganizationKnowledgeService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationPersonsimpleService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;

/**
* 组织 人脉 知识控制层
* @author liweichO@gintong.com
* @version 
* @since 2016年3月17日
*/
@Controller
@RequestMapping("/organizationResource")
public class OrganizationResourceController extends BaseController {
	
	private final Logger logger  = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrganizationKnowledgeService organizationKnowledgeService;
	
	@Autowired
	private OrganizationPersonsimpleService  organizationPersonsimpleService;
	
	/**
	 * 共享人脉
	 * 
	 */
	@RequestMapping(value = "/sharePersonsimple.json", method = RequestMethod.POST)
	public void sharePersonsimple(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"personId|R","organizationId|R","fromType"};
		
		Long personId =null;
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null)
				return;
			
			params=ri.getParams();
			User user  =ri.getUser();
			personId = Long.valueOf(params.getParam("personId"));
			Long orgId = Long.valueOf(params.getParam("organizationId"));
			String fromType = params.getParam("fromType");
			
			OrganizationPs p = organizationPersonsimpleService.getPsByPsIdAndOrganizationId(personId, orgId);
			if( p != null){
				warpMsg(SysCode.SYS_ERR,"该人脉已经在该组织下分享",params,response);
				return ;
			}
			OrganizationPs ps = organizationPersonsimpleService.createOrganizationPs(personId, user.getId(), orgId,fromType);
			
			if(ps != null){
				responseData.put(String.valueOf(ps.getId()), ps);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null,notification)));
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("sharePersonsimple is error personId :" + personId);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}
	
	/**
	 * 分享知识
	 * 
	 */
	@RequestMapping(value = "/shareKnowledge.json", method = RequestMethod.POST)
	public void shareKnowledge(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"knowledgeId|R","organizationId|R","knowledgeType|R"};
		
		
				String knowledgeId = null;
				String knowledgeType = null;
				Map<String, String> paramap = new HashMap<String, String>();
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				
				ParamInfo params = new ParamInfo();
		try {
				RequestInfo ri=validate(request,response,paramKey);
				if(ri == null)
					return;
				params=ri.getParams();
				User user  =ri.getUser();
				knowledgeId = params.getParam("knowledgeId");
				knowledgeType = params.getParam("knowledgeType");
				Long orgId = Long.valueOf(params.getParam("organizationId"));
				
				OrganizationKd k = organizationKnowledgeService.getKdByKnowledgeIdAndOrgId(Long.parseLong(knowledgeId), orgId);
				if(k != null){
					warpMsg(SysCode.SYS_ERR,"该知识已经在该组织下分享",params,response);
					return ;
				}
				
				String url = FileInstance.getValue("tongren.related.url", "http://www.gintong.com/cross");
				String sessonId = (String)request.getSession().getAttribute("sessionId");
				PostUtil pt = new PostUtil("sessionID=\""+sessonId+"\"", "");
				
				paramap.put("knowledgeType", knowledgeType);
				paramap.put("knowledgeId", knowledgeId);
				String jsonStr = pt.testURIWithBody(url+"/knowledge/getKnowledgeDetails.json", paramap);
				//打印返回数据
				logger.info("getKnowledgeDetails begin:"+jsonStr+":end");
				
				OrganizationKd kd = fetchToMap(jsonStr, "title","uid", "cname", "content", "createtime", "taskId");
				
				if(kd == null){
					logger.info("shareKnowledge is not fount...knowledgeId = "+knowledgeId+",knowledgeType ="+knowledgeType );
					warpMsg(SysCode.SYS_ERR,"获取知识详情为空",params,response);
					return ;
				}
				
				kd.setUserId(user.getId());
				kd.setOrganizationId(orgId);
				kd.setKnowledgeId(Long.parseLong(knowledgeId));
				
				OrganizationKd result = organizationKnowledgeService.createOrganizationKnowledge(kd);
				if(result != null){
					responseData.put(String.valueOf(result.getId()), result);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null,notification)));
				}
		}catch(Exception e){
				e.printStackTrace();
				logger.info("shareKnowledge is error knowledgeId :" + knowledgeId);
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}
	/**
	 * 根据组织知识id查看详情
	 * 
	 */
	@RequestMapping(value = "/getOrgKdDetail.json", method = RequestMethod.POST)
	public void getOrgKdDetail(HttpServletRequest request,HttpServletResponse response){
		
				Long orgKnowledgeId = null;
				String[] paramKey = {"orgKnowledgeId|R"};
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				
				ParamInfo params = new ParamInfo();
		try {
				RequestInfo ri=validate(request,response,paramKey);
				if(ri == null)
					return;
				params=ri.getParams();
				orgKnowledgeId = Long.valueOf(params.getParam("orgKnowledgeId"));
				
				OrganizationKd k = organizationKnowledgeService.getKdById(orgKnowledgeId);
				if(k != null){
					responseData.put(orgKnowledgeId.toString(), k);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null,notification)));
				}
		}catch(Exception e){
				e.printStackTrace();
				logger.info("getOrgKdDetail is error orgKnowledgeId :" + orgKnowledgeId);
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}
	/**
	 * 根据组织人脉id查看详情
	 * 
	 */
	@RequestMapping(value = "/getOrgPsDetail.json", method = RequestMethod.POST)
	public void getOrgPsDetail(HttpServletRequest request,HttpServletResponse response){
		
				String[] paramKey = {"orgPsId|R"};
				Long orgPsId = null;
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				
				ParamInfo params = new ParamInfo();
		try {
				RequestInfo ri=validate(request,response,paramKey);
				if(ri == null)
					return;
				params=ri.getParams();
				orgPsId = Long.valueOf(params.getParam("orgPsId"));
				
				OrganizationPs ps = organizationPersonsimpleService.getOrgPsById(orgPsId);
				if(ps != null){
					responseData.put(String.valueOf(orgPsId), ps);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null,notification)));
				}
		}catch(Exception e){
				e.printStackTrace();
				logger.info("getOrgPsDetail is error orgPsId :" + orgPsId);
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}
	/**
	 * 功能描述 ：根据组织id 分页查询组织知识
	 * 
	 * 
	 */
	@RequestMapping(value = "/getOrgKnowledgePage.json", method = RequestMethod.POST)
	public void getOrgKnowledgePage(HttpServletRequest request,HttpServletResponse response){
		
				String organizationId = null;
				ParamInfo params = new ParamInfo();
				Page<OrganizationKd> page = new Page<OrganizationKd>();
				String[] paramKey = {"organizationId|R","paramStr","index|R","size|R"};
				
				Map<String, Object> responseData = new HashMap<String, Object>();
		try{
				RequestInfo ri=validate(request,response,paramKey);
				if(ri == null)
					return;
				params=ri.getParams();
				Integer index = Integer.valueOf(params.getParam("index"));
				Integer size = Integer.valueOf(params.getParam("size"));
				String paramStr = params.getParam("paramStr");
				organizationId = params.getParam("organizationId");
				
				page.setIndex(index);
				page.setSize(size);
				page.addParam("paramStr",paramStr);
				page.addParam("organizationId",organizationId);
				
				page = organizationKnowledgeService.getOrganizationKdPage(page);
				responseData.put("page", page);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
			
		}catch(Exception e){
				e.printStackTrace();
				logger.info("getOrgKdPage is error organizationId :" + organizationId);
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 功能描述 ：根据组织id 分页查询组织人脉
	 * 
	 * 
	 */
	@RequestMapping(value = "/getOrgpersonsimplePage.json", method = RequestMethod.POST)
	public void getOrgpersonsimplePage(HttpServletRequest request,HttpServletResponse response){
				
				String organizationId = null;
				ParamInfo params = new ParamInfo();
				Page<OrganizationPs> page = new Page<OrganizationPs>();
				String[] paramKey = {"organizationId|R","paramStr","index|R","size|R"};
				Map<String, Object> responseData = new HashMap<String, Object>();
		try{
				RequestInfo ri=validate(request,response,paramKey);
				if(ri == null)
					return;
				params=ri.getParams();
				Integer index = Integer.valueOf(params.getParam("index"));
				Integer size = Integer.valueOf(params.getParam("size"));
				String paramStr = params.getParam("paramStr");
				organizationId = params.getParam("organizationId");
				
				page.setIndex(index);
				page.setSize(size);
				page.addParam("paramStr",paramStr);
				page.addParam("organizationId",organizationId);
				
				page = organizationPersonsimpleService.getOrganizationPsPage(page);
				
				responseData.put("page", page);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
		
		}catch(Exception e){
			e.printStackTrace();
			logger.info("getOrgKdPage is error getOrgpersonsimplePage :" + organizationId);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 功能描述:删除人脉
	 * 
	 */
	@RequestMapping(value = "/delPersonsimple.json", method = RequestMethod.POST)
	public void delPersonsimple(HttpServletRequest request,HttpServletResponse response){
		
			Long personId = null;
			ParamInfo params = new ParamInfo();
			String[] paramKey = {"personId|R"};
			
		try{
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null)
				return;
			params=ri.getParams();
		    personId = Long.parseLong(params.getParam("personId"));
		    boolean  result = organizationPersonsimpleService.delectPersonsimple(personId);
			if(result ){
				warpMsg(SysCode.SUCCESS,SysCode.SUCCESS.getMessage(),params,response);
			}else{
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}catch(Exception e){
			logger.info("delPersonsimple is error personId -- >" +personId );
			e.printStackTrace();
		}
	}
	/**
	 * 功能描述:删除知识
	 * 
	 */
	@RequestMapping(value = "/delKnowledge.json", method = RequestMethod.POST)
	public void delKnowledge(HttpServletRequest request,HttpServletResponse response){
		
			Long knowledgeId = null;
			ParamInfo params = new ParamInfo();
			String[] paramKey = {"knowledgeId|R"};
			
		try{
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null)
				return;
			params=ri.getParams();
			knowledgeId = Long.parseLong(params.getParam("knowledgeId"));
		    boolean  result = organizationKnowledgeService.delectOrganizationKnowledge(knowledgeId);
			if(result ){
				warpMsg(SysCode.SUCCESS,SysCode.SUCCESS.getMessage(),params,response);
			}else{
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}catch(Exception e){
			logger.info("delKnowledge is error knowledgeId -- >" +knowledgeId);
			e.printStackTrace();
		}
	}
	
	private  OrganizationKd fetchToMap(String jsonStr,String key1,String key2,String key3,String key4,String key5,String key6){
		
		OrganizationKd kd  = null;
		
		if(StringUtils.isBlank(jsonStr)){
			return kd;
		}
		JSONObject jsonobj=JSON.parseObject(jsonStr);
		JSONObject responseData=jsonobj.getJSONObject("responseData");
		JSONObject responseData1=responseData.getJSONObject("knowledge2");
		if(responseData1==null){
			return kd;
		}
		String title = (String) responseData1.get(key1);
		long uid =  Long.parseLong(responseData1.get(key2).toString());
		String cname = (String) responseData1.get(key3);
		String content = (String) responseData1.get(key4);
		String createtime = (String) responseData1.get(key5);
		String taskId = (String) responseData1.get(key6);
		
		kd = new OrganizationKd();
		kd.setTitle(title);
		kd.setContext(content);
		kd.setCreateTime(new Timestamp(System.currentTimeMillis()));
		kd.setReleaseId(uid);
		kd.setReleaseName(cname);
		kd.setReleaseTime(new Timestamp(Long.parseLong(createtime)));
		kd.setTaskId(taskId);
		
		return kd;
	}
}
