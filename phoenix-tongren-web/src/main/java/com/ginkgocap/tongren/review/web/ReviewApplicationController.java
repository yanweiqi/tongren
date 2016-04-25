package com.ginkgocap.tongren.review.web;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.web.OrganizationController;
import com.ginkgocap.tongren.organization.review.model.ReviewApplication;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.service.ReviewApplicationService;
import com.ginkgocap.tongren.organization.review.service.ReviewObjectService;
import com.ginkgocap.tongren.organization.review.service.ReviewProcessService;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;

/**
 *  审批流程记录控制层
 * @author liweichao
 *
 */

@Controller
@RequestMapping(value="/reviewApplication") 
public class ReviewApplicationController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private ReviewApplicationService reviewApplicationService;
	@Autowired
	private ReviewProcessService reviewProcessService;
	@Autowired
	private ReviewObjectService reviewObjectService;
	
	
	/**
	 * 创建申请(流程申请)
	 */
	@RequestMapping(value="/createApplication.json",method =RequestMethod.POST)
	public void createApplication(HttpServletRequest request, HttpServletResponse response){
		
				User user = getUser(response, request);
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				ParamInfo params = null;
				String paramsKey[] = {"organizationId|R","reviewProcessId|R","reviewGenreId|R","applyRereason|R|L50|F","startTime|R","endTime|R"};
		try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				long organizationId = Long.valueOf(params.getParam("organizationId"));
				long reviewProcessId = Long.valueOf(params.getParam("reviewProcessId"));
				long reviewGenreId = Long.valueOf(params.getParam("reviewGenreId"));
				String applyRereason = params.getParam("applyRereason");
				String startTime = params.getParam("startTime");
				String endTime = params.getParam("endTime");
				
				ReviewApplication r = new ReviewApplication();
				
				validateDate(startTime, endTime, params, response);
				
				r.setReviewGenreId(reviewGenreId);
				r.setReviewProcessId(reviewProcessId);
				String applicationNo =  reviewProcessService.getNomber()+"U";
				packaging(r, user.getId(), organizationId, applyRereason, null, 0, applicationNo, new Timestamp(System.currentTimeMillis()), startTime, endTime);
				ReviewApplication  result = reviewApplicationService.create(r);
				if(result != null){
					responseData.put("result", result);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					notification.put("notifyCode", SysCode.SYS_ERR.getCode());
					notification.put("notifyMessage", SysCode.SYS_ERR);
		            renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
					logger.debug("createRecords Object is null",SysCode.SYS_ERR);
				}
			}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
		 	}catch(Exception e){
				logger.error("createRecords is error");
				e.printStackTrace();
				notification.put("notifyCode", SysCode.NO_ERR.getCode());
				notification.put("notifyMessage", SysCode.NO_ERR);
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
		}
	}
	/**
	 * 创建申请(模板申请)
	 * 
	 * 
	 */
	@RequestMapping(value="/createTemplateApplication.json",method =RequestMethod.POST)
	public void createTemplateApplication(HttpServletRequest request, HttpServletResponse response){
		
			User user = getUser(response, request);
			Map<String, Object> responseData = new HashMap<String, Object>();
			ParamInfo params = null;
			String paramsKey[] = {"organizationId|R","applyRereason|R|L50|F","startTime","endTime","templateJson|R","templateType|R","objectList|R"};
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			String endTime = params.getParam("endTime");
			String startTime = params.getParam("startTime");
			String objectList = params.getParam("objectList");
			String applyRereason = params.getParam("applyRereason");
			long organizationId = Long.valueOf(params.getParam("organizationId"));
			String templateJson = params.getParam("templateJson");
			Integer templateType = Integer.valueOf(params.getParam("templateType"));
			List<ReviewObject> olst = JSON.parseArray(objectList, ReviewObject.class);//审批人集合
			
			if(templateType <=0 || templateType >7){
				warpMsg(SysCode.PARAM_IS_ERROR,"模板类型参数错误",params,response);
			}
			checkRecordsObject(olst,params, response);
			
			if(org.apache.commons.lang3.StringUtils.isNotBlank(startTime) &&org.apache.commons.lang3.StringUtils.isNotBlank(endTime) ){
				validateDate(startTime, endTime, params, response);
			}
			ReviewApplication r = new ReviewApplication();
			String applicationNo = reviewProcessService.getNomber()+"T";
			packaging(r, user.getId(), organizationId, applyRereason, templateJson, templateType, applicationNo, new Timestamp(System.currentTimeMillis()), startTime, endTime);
			
			ReviewApplication result = reviewApplicationService.createTemplate(r, olst);
			if(result != null){
				responseData.put("result", result);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
		}catch(Exception e){
			logger.info(e.getMessage(),e);		
		}
	}
	/**
	 * 重新提交 
	 * 
	 */
	@RequestMapping(value="/afreshSubmit.json",method =RequestMethod.POST)
	public void afreshSubmit(HttpServletRequest request, HttpServletResponse response){
			
				User user = getUser(response, request);
				List<ReviewObject> olst = new ArrayList<ReviewObject>();
				Map<String, Object> responseData = new HashMap<String, Object>();
				ParamInfo params = null;
				String paramsKey[] = {"oldId|R","organizationId|R","applyRereason|R|L50|F","startTime|R","endTime|R","templateJson","templateType","objectList"};
		try {
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}	
				params=ri.getParams();
				long oldId = Long.parseLong(params.getParam("oldId"));
				String endTime = params.getParam("endTime");
				String startTime = params.getParam("startTime");
				String objectList = params.getParam("objectList");
				String applyRereason = params.getParam("applyRereason");
				long organizationId = Long.valueOf(params.getParam("organizationId"));
				String templateJson = params.getParam("templateJson");
				Integer templateType = Integer.valueOf(params.getParam("templateType"));
			
				String applicationNo = reviewProcessService.getNomber();
				ReviewApplication newApplication = new ReviewApplication();
				ReviewApplication oldApplication = reviewApplicationService.getEntityById(oldId);
			
			if(oldApplication == null || oldApplication.getApplyStatus() == 0){
				logger.info("afreshSubmit oldId is not found --->" + oldId);
				warpMsg(SysCode.PARAM_IS_ERROR,"申请无效",params,response);
				return;
			}
			
			if(org.apache.commons.lang3.StringUtils.isNotBlank(startTime) &&org.apache.commons.lang3.StringUtils.isNotBlank(endTime) ){
				validateDate(startTime, endTime, params, response);
			}
			if(templateType == 0){
				 applicationNo += "U";
				 newApplication.setReviewGenreId(oldApplication.getReviewGenreId());
				 newApplication.setReviewProcessId(oldApplication.getReviewProcessId());
				 olst = reviewObjectService.getReviewList(oldApplication.getReviewProcessId());
			}else{
				
				applicationNo +="T";
				 olst = JSON.parseArray(objectList, ReviewObject.class);//审批人集合
				 checkRecordsObject(olst,params, response);
			}
				packaging(newApplication,user.getId(), organizationId, applyRereason, templateJson, templateType, applicationNo, new Timestamp(System.currentTimeMillis()), startTime, endTime);
				
				ReviewApplication result = reviewApplicationService.afreshSubmit(oldId, newApplication, olst);
				if(result != null){
					responseData.put("result", result);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				}
			
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	/**
	 * 查看我的申请
	 */
	@RequestMapping("/getMyApplyFor.json")
	public void getMyApplyFor(HttpServletRequest request, HttpServletResponse response) {
		ParamInfo params = null;
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = { "orgId|R", "type|R", "index|R", "size" };
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			
			Page<ReviewApplicationListVO> page = new Page<ReviewApplicationListVO>();
			page.addParam("orgId", Long.valueOf(params.getParam("orgId")));
			page.addParam("userId", Long.valueOf(user.getId()));
			page.addParam("type", Integer.valueOf(params.getParam("type")));
			page.setIndex(Integer.valueOf(params.getParam("index")));
			if(StringUtils.hasText(params.getParam("size"))){
				page.setSize(Integer.valueOf(params.getParam("size")));
			}
			Page<ReviewApplicationListVO> pageResult = reviewApplicationService.getMyApplyForV3(page);
			if (pageResult.getResult()!=null&&pageResult.getResult().isEmpty()==false) {
				responseData.put("success", pageResult);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			} else {
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			notification.put("notifyCode", SysCode.PARAM_IS_ERROR.getCode());
			e.printStackTrace();
			notification.put("notifyMessage", SysCode.PARAM_IS_ERROR);
			renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(null, notification)));
			logger.debug("Permission authentication failed", SysCode.PARAM_IS_ERROR);
			return;
		}
	}
	
	/**
	 * 功能描述 ：撤回申请
	 * @author 
	 */
	@RequestMapping(value="/recallRecords.json",method =RequestMethod.POST)
	 public void recall(HttpServletRequest request, HttpServletResponse response){
		 
					String paramsKey[] = {"applicationId"};
					Map<String, Object> responseData = new HashMap<String, Object>();
					Map<String, Object> notification = new HashMap<String, Object>();
					ParamInfo params = null ;

			try{
					RequestInfo ri=validate(request,response,paramsKey);
					if(ri==null){
						return;
					}
					params=ri.getParams();
					params = parseRequest(request,response,"getRecordsProgress",paramsKey);
					String applicationId = params.getParam("applicationId");
				boolean result = reviewApplicationService.recallRecords(Long.valueOf(applicationId));
				if(result){
					responseData.put("result", result);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					notification.put("notifyCode", SysCode.ERROR_CODE.getCode());
					notification.put("notifyMessage", SysCode.ERROR_CODE);
		            renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
					logger.debug("recall update error",SysCode.ERROR_CODE);
					}
			}catch(ValiaDateRequestParameterException e){
					logger.info(e.getMessage(),e);	
			}catch(Exception e){
					notification.put("notifyCode", SysCode.PARAM_IS_ERROR.getCode());
					notification.put("notifyMessage", SysCode.PARAM_IS_ERROR);
					renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(null, notification)));
					logger.debug("Permission authentication failed",SysCode.PARAM_IS_ERROR);
					return;
		}
	 }
	
	private void validateDate(String startTime,String endTime,ParamInfo params,HttpServletResponse response){
		int  flag = startTime.compareTo(endTime);
		if(flag>0 ||flag == 0){
			warpMsg(SysCode.PARAM_IS_ERROR,"申请开始时间不能大于或等于结束时间",params,response);
			return;
		}
	}
	private void checkRecordsObject(List<ReviewObject> olst,ParamInfo params, HttpServletResponse response){
		Set<Long> userSet =  new HashSet<Long>();
		if(olst != null && CollectionUtils.isNotEmpty(olst)){
			for(ReviewObject r :olst){userSet.add(r.getReviewUserId());}
			if(userSet.size()!= olst.size()){
				warpMsg(SysCode.REVIEWUSER_DOUBLE__ERROR,SysCode.REVIEWUSER_DOUBLE__ERROR.getMessage(),params,response);
				return;
			}
		}
	}
	private void packaging(ReviewApplication r,long applyId,long organizationId,String applyRereason,String templateJson,
			int templateType,String applicationNo,Timestamp applyDate,String startTime,String endTime){
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd mm:hh");
		try{
			r.setApplyId(applyId);
			r.setOrganizationId(organizationId);
			r.setApplyRereason(applyRereason);
			r.setTemplateJson(templateJson);
			r.setTemplateType(templateType);
			r.setApplicationNo(applicationNo);
			r.setApplyDate(new Timestamp(System.currentTimeMillis()));
			if(org.apache.commons.lang3.StringUtils.isNotBlank(endTime)){
				r.setEndTime(new Timestamp(format.parse(endTime).getTime()));
			}
			if(org.apache.commons.lang3.StringUtils.isNotBlank(startTime)){
				r.setStartTime(new Timestamp(format.parse(startTime).getTime()));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
