package com.ginkgocap.tongren.review.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.model.ReviewProcess;
import com.ginkgocap.tongren.organization.review.service.ReviewProcessService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;

/**
*  审批流程控制层
* @author  李伟超
* @version 
* @since 2015年10月18日
*/
@Controller
@RequestMapping("/reviewProcess") 
public class ReviewProcessController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(ReviewProcessController.class);
	
	@Autowired
	private ReviewProcessService reviewProcessService;
	
	/**
	 * 
	 * 生成流程编号
	 * 
	 */
	@RequestMapping(value="/getNomber.json",method =RequestMethod.POST)
	private void getNomber(HttpServletRequest request, HttpServletResponse response){
		
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				ParamInfo params = null;
		try{
			
				params = parseRequest(request,response,"getNomber",null);
				String str = reviewProcessService.getNomber();
			if(StringUtils.isNotBlank(str)){
				responseData.put("reviewNo", str);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null,notification)));
			}
			
		}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
		}catch(Exception e){
				logger.error("getNomber is error");
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	/**
	 * 校验审批流程是否存在
	 * 
	 */
	@RequestMapping(value="/checkProcessName.json",method =RequestMethod.POST)
	public void checkProcessName(HttpServletRequest request, HttpServletResponse response){
		
				Map<String, Object> responseData = new HashMap<String, Object>();
				String paramsKey[] = {"oid|R","name|R"};
				ParamInfo params = null;
		try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				String oid = params.getParam("oid");
				String name = params.getParam("name");
	
			boolean result = reviewProcessService.checkProcessName(Long.valueOf(oid), name);
			if(result){
				responseData.put(Boolean.toString(result), "名称可以使用");
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData,null)));
			}else{
	            warpMsg(SysCode.REVIEW_FALSE,name+"已存在,请尝试用新的名称命名",params,response);
				logger.debug("checkProcessName is false",SysCode.REVIEW_FALSE);
			}
		}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
		}catch(Exception e){
				logger.error("checkProcessName is error");
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
		/**
		 * 创建流程审批
		 * @param reviewProcess 申请流程对象
		 * @param genreList  审批类型集合
		 * @param objectList  审核人集合
		 * 
		 */
	@RequestMapping(value="/create.json",method =RequestMethod.POST )
	public void create(HttpServletRequest request, HttpServletResponse response){
			
				Map<String, Object> responseData = new HashMap<String, Object>();
				String paramsKey[] = {"reviewProcess","genreList","objectList"};
				ParamInfo params = null;

		try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					warpMsg(SysCode.PARAM_IS_ERROR,SysCode.PARAM_IS_ERROR.getMessage(),params,response);
					return;
				}
				User user=ri.getUser();
				params=ri.getParams();
				params = parseRequest(request,response,"create",paramsKey);
				String reviewProcess = params.getParam("reviewProcess");	
				String genreList = params.getParam("genreList");
				String objectList = params.getParam("objectList");

				ReviewProcess p = JSON.parseObject(reviewProcess, ReviewProcess.class);
				List<ReviewGenre> glst = JSON.parseArray(genreList, ReviewGenre.class);
				List<ReviewObject> olst = JSON.parseArray(objectList, ReviewObject.class);
				Set<Long> userSet =  new HashSet<Long>();
				Set<String> typeSet = new HashSet<String>();
				//判断提交重复审核类型
				if(glst != null && CollectionUtils.isNotEmpty(glst)){
					for(ReviewGenre rg:glst){
						typeSet.add(rg.getName());
					}
					if(typeSet.size() != glst.size()){
						warpMsg(SysCode.REVIEWGENRE_DOUBLE__ERROR,SysCode.REVIEWGENRE_DOUBLE__ERROR.getMessage(),params,response);
						return;
					}
				}
				//判断提交重复审核人
				if(olst != null && CollectionUtils.isNotEmpty(olst)){
					for(ReviewObject r :olst){
						userSet.add(r.getReviewUserId());
					}
					if(userSet.size()!= olst.size()){
						warpMsg(SysCode.REVIEWUSER_DOUBLE__ERROR,SysCode.REVIEWUSER_DOUBLE__ERROR.getMessage(),params,response);
						return;
					}
					
				}
				if(glst.isEmpty() || olst.isEmpty()){
					warpMsg(SysCode.PARAM_IS_ERROR,SysCode.PARAM_IS_ERROR.getMessage(),params,response);
					return;
				}
				if(p.getReviewName().length()>30){
					warpMsg(SysCode.PARAM_IS_ERROR,"流程审批名称过长,请重新输入",params,response);
					return;
				}
				if(p.getDescription().length()>200){
					warpMsg(SysCode.PARAM_IS_ERROR,"流程审批说明内容过长,请重新输入",params,response);
					return;
				}
				p.setCreateId(user.getId());
				p.setCreateTime(new Timestamp(System.currentTimeMillis()));
				Long id = reviewProcessService.create(p, glst, olst);
			if(id != null){
				responseData.put("organizationId", p.getOrganizationId());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData,null)));
		}else{
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		 }
					
		}catch(Exception e){
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				e.printStackTrace();
		}
	}
	/**
	 * 
	 * 查询组织下的审批流程
	 * @param oid 组织id
	 * @return 审批集合
	 * 
	 */
	@RequestMapping(value="/getProcessByOrgId.json",method =RequestMethod.POST)
	public void getProcessByOrgId(HttpServletRequest request, HttpServletResponse response){
		
				Map<String, Object> responseData = new HashMap<String, Object>();
				String paramsKey[] = {"oid"};
				ParamInfo params = null;
		try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				params = parseRequest(request,response,"getProcessByOrgId",paramsKey);
				String oid = params.getParam("oid");
				logger.info("oid--->"+oid);
				List<ReviewProcess> list = reviewProcessService.getReviewByOrgId(Long.valueOf(oid));
			if(list.isEmpty()){
	            warpMsg(SysCode.BIGDATA_EMPTY,SysCode.BIGDATA_EMPTY.getMessage(),params,response);
				logger.debug("getProcessById is null",SysCode.BIGDATA_EMPTY);
			}else{
				responseData.put(oid, list);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}
		}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
		}catch(Exception e){
				logger.error("getProcess is error");
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	/**
	 * 查询审批流程
	 * @param id 流程id
	 * @return reviewProcess 流程实体
	 * 
	 */
	@RequestMapping(value="/getProcessById.json",method =RequestMethod.POST)
	public void getProcessById(HttpServletRequest request,HttpServletResponse response){
		
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				String paramsKey[] = {"id"};
				ParamInfo params = null;
			try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				String id = params.getParam("id");
				logger.info("getProcessById--->"+id);
				ReviewProcess reviewProcess = reviewProcessService.getReviewById(Long.valueOf(id));
			if(reviewProcess != null){
				responseData.put(id, reviewProcess);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				notification.put("notifyCode", SysCode.BIGDATA_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.BIGDATA_EMPTY);
		        renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
				logger.debug("getProcess is null",SysCode.BIGDATA_EMPTY);
				}
			}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
			}catch(Exception e){
				logger.error("getProcess is error");
				e.printStackTrace();
				notification.put("notifyCode", SysCode.SYS_ERR.getCode());
				notification.put("notifyMessage", SysCode.SYS_ERR);
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			}
	}
	/**
	 * 修改审批流程
	 * 
	 */
	@RequestMapping(value="/update.json",method =RequestMethod.POST)
	public void update(HttpServletRequest request,HttpServletResponse response){
		
				Map<String, Object> responseData = new HashMap<String, Object>();
				String paramsKey[] = {"reviewProcess"};
				ParamInfo params = null;
			try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				String reviewProcess = params.getParam("reviewProcess");	


				ReviewProcess p = JSON.parseObject(reviewProcess, ReviewProcess.class);
				p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				
				boolean  flag = reviewProcessService.updateReviewProcess(p);
			if(flag){
				responseData.put("organizationId", p.getOrganizationId());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				}
			}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
			}catch(Exception e){
				logger.error("update is error");
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
	}
	/**
	 * 
	 * 功能描述：删除审批流程
	 * 
	 */
	@RequestMapping(value="/delect.json",method =RequestMethod.POST)
	public void delect(HttpServletRequest request,HttpServletResponse response){
		
				Map<String, Object> responseData = new HashMap<String, Object>();
				String paramsKey[] = {"reviewProcessId|R"};
				ParamInfo params = null;
			try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				String reviewProcessId = params.getParam("reviewProcessId");	
				
				boolean  flag = reviewProcessService.delect(Long.valueOf(reviewProcessId));
			if(flag){
				responseData.put("result", flag);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				warpMsg(SysCode.SYS_ERR,"改审批流程已被人使用 不能删除",params,response);
				}
			}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
			}catch(Exception e){
				logger.error("delect is error");
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			}
	}
}