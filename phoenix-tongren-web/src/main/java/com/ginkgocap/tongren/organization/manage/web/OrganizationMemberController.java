package com.ginkgocap.tongren.organization.manage.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.common.utils.LoadPropertiesUtil;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberManageService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.manage.vo.ApplicationMenberVO;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationMemberVO;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.review.model.ReviewRecords;
import com.ginkgocap.tongren.organization.review.service.ReviewRecordsService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 
 * 组织成员控制层
 * @author liweichao
 *
 */
@Controller
@RequestMapping("/organizationMember") 
public class OrganizationMemberController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationMemberController.class);
	String path = LoadPropertiesUtil.getPropertiesValue("conf/application.properties","nginx.root");
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	@Autowired
	private MessageService messageService;//桐人消息接口	
	@Autowired
	private UserService userService;//金桐网的用户接口
	@Autowired 
	private OrganizationMemberManageService organizationMemberManageService;
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private ReviewRecordsService reviewRecordsService;
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	
	@RequestMapping(value="/getMemberDetail.json",method =RequestMethod.POST)
	public void getMyOrganizationMemberDetail(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String paramsKey[] = {"organizationId|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request,response,paramsKey);
			if(ri==null) return;
			params = ri.getParams();
			long organizationId = Long.valueOf(params.getParam("organizationId"))  ;
			long userId = ri.getUser().getId();
			OrganizationMember om = organizationMemberService.getMyOrganizationMemberDetail(organizationId, userId);
            if(null != om){
            	om.setUser(ri.getUser());
				responseData.put("organizationMember", om);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}
			else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	/**
	 * 
	 * 功能描述 ： 邀请成员
	 * 
	 */
	@RequestMapping(value="/invite.json",method =RequestMethod.POST)
	public void invite(HttpServletRequest request, HttpServletResponse response){
		
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			String paramsKey[] = {"oid|R","gintong","friend","search","content"};
			ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			String oid = params.getParam("oid");
			String gintong = params.getParam("gintong");
			String friend = params.getParam("friend");
			String search = params.getParam("search");
			String content = params.getParam("content");
			Map<Integer,String[]> map = new HashMap<Integer,String[]>();
			
			if(StringUtils.isNotBlank(friend)){
			    String [] flst = friend.split("-");//我的好友
			    map.put(1, flst);
			}
			if(StringUtils.isNotBlank(search)){
				String [] slst = search.split("-");//搜索
				  map.put(2, slst);
			}
			if(StringUtils.isNotBlank(gintong)){
				String [] glst = gintong.split("-");
				map.put(3, glst);  //系统(金桐)推荐
			}
	
			if(!map.isEmpty()){
				responseData = organizationMemberService.invite(Long.valueOf(oid), map, content);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				notification.put("error", "邀请组织成员集合为空");
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, notification)));
			}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
		}
		catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	
	/**
	 * 
	 * 功能描述 ：退出组织
	 * 
	 */
	@RequestMapping(value="/exit.json",method =RequestMethod.POST)
	public void exit(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"oid|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			String oid = params.getParam("oid");
			
			List<AssignTask> list = assignTaskService.getAssignOrgnazationTask(Long.valueOf(oid), user.getId());
			if(list !=null && CollectionUtils.isNotEmpty(list)){
				warpMsg(SysCode.UNFINISHED_TASK_ERROR,"您还有未完成的项目任务 不能申请退出组织..",params,response);
				return;
			}
			String  result = organizationMemberService.exit(Long.valueOf(oid), user.getId());
			if(result.equals("2")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				String[] resStrArr={"已提交退出组织申请","系统错误","用户不存在该组织下"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(result)-1],params,response,responseData);
			}
		}
		catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
		}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	
	/**
	 * 组织成员列表接口
	 * @param status 1:包含申请退出组织成员    3:不包含
	 */
	@RequestMapping(value="/getOrganizationMemberInfo.json",method =RequestMethod.POST)
	public void getOrganizationMemberInfo(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = {"oid|R","status|R"};
		ParamInfo params = null;
		try{

			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			
			String oid = params.getParam("oid");
			String status = params.getParam("status");
			List<OrganizationMemberVO> list = organizationMemberService.getOrganizationAllMemberInfo(Long.valueOf(oid),Integer.valueOf(status));
			
			if(!list.isEmpty()){
				responseData.put("result", list);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
		}
		catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);
		}
		catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}

	
	

	/**
	 * 删除组织成员
	 *  
	 */
	@RequestMapping(value="/delMember.json",method =RequestMethod.POST)
	public void delMember(HttpServletRequest request, HttpServletResponse response){
		ParamInfo params = null;
		String paramsKey[] = {"oid|R","userId|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			User user=ri.getUser();
			params=ri.getParams();
			long oid = Long.valueOf(params.getParam("oid"));
			long userId = Long.valueOf(params.getParam("userId"));
			Long uid = Long.valueOf(userId);
			if(user.getId() == uid){
				notification.put("error", "组织管理者不能删除自己");
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
				return;
			}
			//如果删除的成员 在该组织下是流程审核人的话 不能删除
			List<ReviewRecords> list = reviewRecordsService.getMyReviewRecordsList(userId,oid);
			if(list != null && CollectionUtils.isNotEmpty(list)){
				warpMsg(SysCode.UNFINISHED_REVIEW_ERROR,"您删除的成员是审批人,还不能删除",params,response);
				return;
			}
			boolean  result = organizationMemberManageService.delMember(user.getId(),oid,userId);
			if(result){
				responseData.put("result", result);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				notification.put("result", result);
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
			}
		}
		catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);
		}
		catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	/**
	 * 功能描述：组织成员的最新申请和退出申请列表        
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value="/getApplicationMember.json",method =RequestMethod.POST)
	public void getApplicationMember(HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String[] paramsKey = {"type|R","organizationId|R"};
		ParamInfo params=null;
		User user=null;
		List<MessageSend> mesList = null;
		List<ApplicationMenberVO> returnList = new ArrayList<ApplicationMenberVO>();
		ApplicationMenberVO vo = null;
		try {
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			user  =ri.getUser();
			int type = Integer.parseInt(params.getParam("type"));
			Long organizationId = Long.parseLong(params.getParam("organizationId"));
			logger.info("The value of the application type is:"+type);
			mesList = messageService.getMessageByOrgUidStatus(user.getId(), organizationId, type);
			if(mesList == null|| mesList.size() == 0){
				notification.put("notifyCode", SysCode.APPLICATION_MEMBER_LIST_NULL.getCode());
				notification.put("notifyMessage", SysCode.APPLICATION_MEMBER_LIST_NULL.getMessage());
	            renderResponseJson(response, params.getResponse(SysCode.APPLICATION_MEMBER_LIST_NULL, genResponseData(null, notification)));
	            logger.debug("ApplicationMessage list is null",SysCode.MESSAGE_LIST_NULL);
				return;
			}
			for (MessageSend messageSend : mesList) {
				vo = new ApplicationMenberVO();
				vo.setApplicationId(messageSend.getMessageReceive().getId());
				vo.setApplicationTime(messageSend.getMessageReceive().getReceiveTime());
				user = userService.selectByPrimaryKey(messageSend.getUserId());
				vo.setName(user.getName() == null ? "" : user.getName());
				vo.setuPicPath(user.getPicPath() == null ? "" : FileInstance.FTP_WEB.trim()+FileInstance.FTP_URL.trim()+user.getPicPath());
				returnList.add(vo);
			}
			responseData.put("returnList", returnList);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("getApplicationMember failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述 ：申请加入组织
	 * 
	 */
	@RequestMapping(value = "/applyOrganization.json", method = RequestMethod.POST)
	public void applyOrganization(HttpServletRequest request,HttpServletResponse response){
		
		String[] paramKeys = {"organizationId|R"};
		ParamInfo params = new ParamInfo();
		OrganizationMember member = null;
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			RequestInfo ri=validate(request,response,paramKeys);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			long organizationId = Long.valueOf(params.getParam("organizationId"));
			Organization o = tongRenOrganizationService.getEntityById(organizationId);
			if(o == null || o.getStatus() == 1){
				notification.put("error", "组织不存在...");
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
			}
			member = organizationMemberService.addMember(user.getId(), organizationId, o.getCreaterId(), 0, MessageType.APPLICATION.getType());
			if(member != null){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				notification.put("error", "申请加入组织失败");
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
			}
		}catch(Exception e){
			logger.info(e.getMessage(),e);
		}
	}
	
	@RequestMapping(value = "/getOrganizationMemberAll.json",method = RequestMethod.POST)
	public void getOrganizationMemberAll(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = {"oid|R"};
		ParamInfo params = null;
		try{

			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			
			String oid = params.getParam("oid");
			List<OrganizationMemberVO> list = organizationMemberService.getOrganizationMemberAll(Long.valueOf(oid));
			
			if(!list.isEmpty()){
				responseData.put("result", list);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
		}
		catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);
		}
		catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
}
