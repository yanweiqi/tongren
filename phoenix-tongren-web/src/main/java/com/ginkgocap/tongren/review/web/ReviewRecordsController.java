package com.ginkgocap.tongren.review.web;

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
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.web.OrganizationController;
import com.ginkgocap.tongren.organization.review.service.ReviewApplicationService;
import com.ginkgocap.tongren.organization.review.service.ReviewRecordsService;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;
import com.ginkgocap.tongren.organization.review.vo.ReviewRecordsVO;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;

/**
 *  审批流程记录控制层
 * @author liweichao
 *
 */

@Controller
@RequestMapping("/reviewRecords") 
public class ReviewRecordsController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private ReviewRecordsService reviewRecordsService;
	@Autowired
	private ReviewApplicationService reviewApplicationService;
	
	
	/**
	 * @author hanxifa
	 * 查看审批进度
	 * 
	 */
		@RequestMapping(value="/getRecordsProgress.json",method =RequestMethod.POST)
	public void getRecordsProgress(HttpServletRequest request, HttpServletResponse response) {
			Map<String, Object> responseData = new HashMap<String, Object>();
		ParamInfo params = null;
		String paramsKey[] = { "applicationId|R" };
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			String applicationId = params.getParam("applicationId");
			ReviewApplicationListVO ral = reviewApplicationService.getReviewApplicationDetail(Long.valueOf(applicationId));
			if(ral!=null){
				//是否 有查看给申请的权限
				boolean hasPrivilege=false;
				if(ral.getApplyId()!=user.getId()){
					List<ReviewRecordsVO> rlist=ral.getReviewDetail();
					for(ReviewRecordsVO rro:rlist){
						if(rro.getReviewUserId()==user.getId()){
							hasPrivilege=true;
						}
					}
				}else{
					hasPrivilege=true;
				}
				if(hasPrivilege){
					responseData.put("success", ral);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					warpMsg(SysCode.SECURITY_ERR, "当前用户无权查看该申请进度"+request.getParameter("requestJson"), params, response);
				}
			}else{
				warpMsg(SysCode.BIGDATA_EMPTY, SysCode.BIGDATA_EMPTY.getMessage(), params, response);
			}
		} catch (Exception e) {
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
			logger.error("getRecordsProgress failed! " + request.getParameter("requestJson"), e);
		}

	}

		
		 /**
		  * 我的审批  (同意、驳回)
		  * applicationId :审核记录Id
		  * type 2： 审核通过  3:驳回
		  */
			@RequestMapping(value="/signOff.json",method =RequestMethod.POST)
			public void signOff(HttpServletRequest request, HttpServletResponse response){
			
					Map<String, Object> responseData = new HashMap<String, Object>();
					Map<String, Object> notification = new HashMap<String, Object>();
					String paramsKey[] = {"applicationId|R","type|R"};
					ParamInfo params = null;

				try{
					RequestInfo ri=validate(request,response,paramsKey);
					if(ri==null){
						return;
					}
					User user=ri.getUser();
					params=ri.getParams();
					String applicationId = params.getParam("applicationId");
					String type = params.getParam("type");
					logger.info("recId-->"+applicationId+","+"type" + type);
					boolean result = reviewRecordsService.signOff(Long.valueOf(applicationId),user.getId(),Integer.valueOf(type));
				if(result){
					responseData.put("result", result);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					notification.put("notifyMessage", "您做过操作了");
		            renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
					logger.debug("signOff update error",SysCode.SYS_ERR);
					}
				}catch(ValiaDateRequestParameterException e){
					logger.info(e.getMessage(),e);	
				}catch(Exception e){
					logger.error("signOff is error");
					e.printStackTrace();
					notification.put("notifyCode", SysCode.SYS_ERR.getCode());
					notification.put("notifyMessage", SysCode.SYS_ERR);
					renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			}
		}
	
			/**
			 * 我的审批列表
			 *  查询条件包含  orgId 组织id，userId 用户id，type 查询类型(0所有的 1 未审核的 2 已经审核)
			 */
		@RequestMapping("/getReviewApplicationList.json")
		public void getReviewApplicationList(HttpServletRequest request, HttpServletResponse response){
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
				String type=params.getParam("type").trim();
				if("0".equals(type)||"1".equals(type)||"2".equals(type)){
					Page<ReviewApplicationListVO> page=new Page<ReviewApplicationListVO>();
					page.setIndex(Integer.parseInt(params.getParam("index")));
					if(params.getParam("size")!=null){
						page.setSize(Integer.valueOf(params.getParam("size")));
					}
					page.addParam("orgId", params.getParam("orgId"));
					page.addParam("userId", user.getId()+"");
					page.addParam("type", type);
					Page<ReviewApplicationListVO> pageResult=reviewRecordsService.getReviewListPage(page);
					if(pageResult!=null&&pageResult.getResult().isEmpty()==false){
						responseData.put("success", pageResult);
						renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
					}else{
						renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
					}
				}else{
					warpMsg(SysCode.PARAM_IS_ERROR,"type must be in 0,1,2",params,response);
				}
				
			} catch (Exception e) {
				warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
				logger.error("getReviewApplicationList failed! "+request.getParameter("requestJson"),e);
			}
		}
		
}
