/*
 * 文件名： MessageController.java
 * 创建日期： 2015年10月16日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.message.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.message.vo.MessageVO;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;


 /**
 *  消息控制器
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月16日
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {
	
	private final Logger logger  = LoggerFactory.getLogger(getClass());//日志记录对象
	
	@Autowired
	private MessageService messageService;//桐人消息接口
	@Autowired
	private UserService userService;//金桐网的用户接口
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;//桐人组织接口
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;//桐人发送接口
	
	/**
	 * 发送消息
	 * @author liny
	 * @param request
	 * @param response
	 * @throws ValiaDateRequestParameterException 
	 * 
	 */
	@RequestMapping(value = "/sendMessage.json", method = RequestMethod.POST)
	public void sendMessage(HttpServletRequest request,HttpServletResponse response){
		logger.info("begin sendMessage method");
		String[] paramKey = {"content","sendOrganizationId","receiveUserId"};
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		ParamInfo params = null;
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			String content = params.getParam("content");
			String sendOrganizationId = params.getParam("sendOrganizationId");
			String receiveUserIds = params.getParam("receiveUserId");
			
			List<Long> receiveUserIdsLong = JSON.parseArray(receiveUserIds, Long.class);
			if(StringUtils.isEmpty(content)){
				notification.put("notifyCode", SysCode.MESSAGE_CONTENT_ERR.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_CONTENT_ERR.getMessage());
	            renderResponseJson(response, params.getResponse(SysCode.MESSAGE_CONTENT_ERR, genResponseData(null, notification)));
				return;
			}
//			User userTemp = userService.selectByPrimaryKey(Long.parseLong(receiveUserId));//根据用户ID查询
			Organization organization = tongRenOrganizationService.getOrganizationById(Long.parseLong(sendOrganizationId));
			StringBuffer title = new StringBuffer();
			title.append("“"+organization.getName()+"”组织的成员“"+user.getName()+"”给您发送了消息");

			if(receiveUserIdsLong == null || receiveUserIdsLong.size() == 0){
				notification.put("notifyCode", SysCode.MESSAGE_SEND_USER_NULL.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_SEND_USER_NULL.getMessage());
	            renderResponseJson(response, params.getResponse(SysCode.MESSAGE_SEND_USER_NULL, genResponseData(null, notification)));
				return;
			}
			for (Long long1 : receiveUserIdsLong) {
				try {
					messageService.sendMessage(title.toString(), content, user.getId(), Long.parseLong(sendOrganizationId), 0, long1, Long.parseLong(sendOrganizationId), 0, MessageType.GENERAL_MESSAGE.getType(), 0);
				} catch (Exception e) {
					logger.error("sendMessage is error, sendUserid is 【"+user.getId()+"】 and resceiveUserid is 【"+long1+"】");
				}
				
			}
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, responseData)));
			return;
			
		} catch (Exception e) {
			logger.error("sendMessage is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
		}
	}
	/**
	 * 功能描述：    根据消息查询类型过滤消息list   
	 *                                                       
	 * @param type 0：全部 1：组织 2：项目
	 * @param messageList 消息集合
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<MessageVO> getMessageListByType(String type,List<MessageVO> messageList){
		List<MessageVO> retrunMessages = new ArrayList<MessageVO>();
		if(messageList == null || messageList.size() == 0)
			return null;
		if(type == "0" || type.equals("0"))
			return messageList;
		if(type == "1" || type.equals("1")){//组织类消息
			for (MessageVO messageVO : messageList) {
				if(messageVO.getMessageType() == MessageType.INVITATION.getType()){
					messageVO.setMessageTypeRes(MessageType.INVITATION.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.APPLICATION.getType()){
					messageVO.setMessageTypeRes(MessageType.APPLICATION.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.SIGNOUT.getType()){
					messageVO.setMessageTypeRes(MessageType.SIGNOUT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.AGREEJIONIN.getType()){
					messageVO.setMessageTypeRes(MessageType.AGREEJIONIN.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.REFUSEAGREEJOININ.getType()){
					messageVO.setMessageTypeRes(MessageType.REFUSEAGREEJOININ.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.AGREESIGNOUT.getType()){
					messageVO.setMessageTypeRes(MessageType.AGREESIGNOUT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.REFUSEAGREESIGNOUT.getType()){
					messageVO.setMessageTypeRes(MessageType.REFUSEAGREESIGNOUT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.DISSOLUTION.getType()){
					messageVO.setMessageTypeRes(MessageType.DISSOLUTION.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.KICKED.getType()){
					messageVO.setMessageTypeRes(MessageType.KICKED.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.ASSIGNMENT_TASK.getType()){
					messageVO.setMessageTypeRes(MessageType.ASSIGNMENT_TASK.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.RETURN_TASK.getType()){
					messageVO.setMessageTypeRes(MessageType.RETURN_TASK.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.REPEAT_TASK.getType()){
					messageVO.setMessageTypeRes(MessageType.REPEAT_TASK.getMessage());
					retrunMessages.add(messageVO);
				}
			}
			return retrunMessages;
		}
		if(type == "2" || type.equals("2")){//项目消息
			for (MessageVO messageVO : messageList) {
				if(messageVO.getMessageType() == MessageType.GIVE_UP_PROJECT.getType()){
					messageVO.setMessageTypeRes(MessageType.GIVE_UP_PROJECT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.END_PROJECT.getType()){
					messageVO.setMessageTypeRes(MessageType.END_PROJECT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.ORGANIZATION_TO_UNDERTAKE_PROJECTS.getType()){
					messageVO.setMessageTypeRes(MessageType.ORGANIZATION_TO_UNDERTAKE_PROJECTS.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.PROJECT_DOCUMENT.getType()){
					messageVO.setMessageTypeRes(MessageType.PROJECT_DOCUMENT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.ASSIGNMENT_SUB_TASK.getType()){
					messageVO.setMessageTypeRes(MessageType.ASSIGNMENT_SUB_TASK.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.INVITATION_PROJECT.getType()){
					messageVO.setMessageTypeRes(MessageType.INVITATION_PROJECT.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.AGREE_TO_UNDERTAKE_PROJECTS.getType()){
					messageVO.setMessageTypeRes(MessageType.AGREE_TO_UNDERTAKE_PROJECTS.getMessage());
					retrunMessages.add(messageVO);
				}
				if(messageVO.getMessageType() == MessageType.REFUSEAGREE_TO_UNDERTAKE_PROJECTS.getType()){
					messageVO.setMessageTypeRes(MessageType.REFUSEAGREE_TO_UNDERTAKE_PROJECTS.getMessage());
					retrunMessages.add(messageVO);
				}
			}
			return retrunMessages;
		}
		return null;
	}
	/**
	 * 功能描述：  查询我的消息列表--进入桐人首页的消息，可以看到任何组织给我发的消息
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @throws ValiaDateRequestParameterException 
	 * @since 2015年10月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/userMessage.json")
	public void userMessage(HttpServletRequest request,HttpServletResponse response){
		String[] paramKeys = {"type"};//0：全部 1：组织 2：项目
		ParamInfo params = new ParamInfo();
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
//		List<MessageCreate> returnMessageList1 = new ArrayList<MessageCreate>();
		List<MessageVO> returnMessageList = new  ArrayList<MessageVO>();
		List<MessageSend> messageSendList = new ArrayList<MessageSend>();
		MessageVO messageVO = null;
		
		try {
			RequestInfo ri=validate(request,response,paramKeys);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			String type = params.getParam("type");
			messageSendList = messageService.getMessageListByOrgUid(user.getId(), 0);
			
			if(messageSendList == null || messageSendList.size() == 0){
				notification.put("notifyCode", SysCode.MESSAGE_LIST_NULL.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_LIST_NULL);
	            renderResponseJson(response, params.getResponse(SysCode.MESSAGE_LIST_NULL, genResponseData(null, notification)));
	            logger.debug("message list is null",SysCode.MESSAGE_LIST_NULL);
				return;
			}
			for (MessageSend messageSend : messageSendList) {
				messageVO = new MessageVO();
				messageVO.setContent(messageSend.getMessageCreate().getContent());
				messageVO.setTitle(messageSend.getMessageCreate().getTitle());
				messageVO.setSendTime(messageSend.getSendTime());
				messageVO.setSendUserID(messageSend.getUserId());
				messageVO.setOrganizationId(messageSend.getOrganizationId());
				messageVO.setMessageStatus(messageSend.getMessageReceive().getStatus());
				User userSend = userService.selectByPrimaryKey(messageSend.getUserId());
				if(userSend != null){
					messageVO.setSendUserName(userSend.getName());
					messageVO.setSendUserPic(getUserPicURL(userSend));
				}
				messageVO.setMessageReceiveID(messageSend.getMessageReceive().getId());
				messageVO.setMessageType(messageSend.getMessageCreate().getMessageType());	
				messageVO.setMessageTypeRes(messageTypeZhuanHuan(messageSend.getMessageCreate().getMessageType()));
				returnMessageList.add(messageVO);
			}
			
			responseData.put("messageList", getMessageListByType(type, returnMessageList));
			responseData.put("imgUrlAddr", FileInstance.FTP_URL.trim());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("userMessage is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	/**
	 * 功能描述： 消息转换功能--用户消息类型转换成字符串，反馈前端信息查看
	 *                                                       
	 * @param messageType 消息类型 0普通消息、1项目消息、2邀请消息、3申请消息、4文档提交消息
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月26日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	protected String messageTypeZhuanHuan(int messageType){
		String str = "";
		if(messageType == 1)
			str = "项目消息";
		else if(messageType == 2)
			str = "邀请消息";
		else if(messageType == 3)
			str = "申请消息";
		else if(messageType == 4)
			str = "文档提交消息";
		else
			str = "普通消息";
			
		return str;
	}
	/**
	 * 功能描述： 清空我的消息列表---只清空我的组织下的消息
	 * @param request
	 * @param response
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @throws ValiaDateRequestParameterException 
	 * @since 2015年10月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/emptyMessage.json")
	public void emptyMessage(HttpServletRequest request,
			HttpServletResponse response){
		String[] paramKeys = {"organizationId|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		
		boolean delTF = false;
		
		try {
			RequestInfo ri=validate(request,response,paramKeys);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			String organizationId = params.getParam("organizationId");
			delTF = messageService.deleteMessage(user.getId(),Long.parseLong(organizationId));
			if(delTF){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				return;
			}
			else{
				notification.put("notifyCode", SysCode.MESSAGE_LIST_EMPTY_REEOR.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_LIST_EMPTY_REEOR);
				renderResponseJson(response, params.getResponse(SysCode.MESSAGE_LIST_EMPTY_REEOR, genResponseData(null, notification)));
				return;
			}
		} catch (Exception e) {
			logger.error("emptyMessage is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：  查询我的组织下的我的消息
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @throws ValiaDateRequestParameterException 
	 * @since 2015年10月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/userMessageByOrg.json")
	public void userMessageByOrg(HttpServletRequest request,
			HttpServletResponse response){
		String[] paramKeys = {"organizationId"};
		ParamInfo params = new ParamInfo();
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		List<MessageVO> returnMessageList = new  ArrayList<MessageVO>();
		List<MessageSend> messageSendList = new ArrayList<MessageSend>();
		MessageVO messageVO = null;
		try {
			RequestInfo ri=validate(request,response,paramKeys);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			String organizationId = params.getParam("organizationId");
			messageSendList = messageService.getMessageListByOrgUid(user.getId(), Long.parseLong(organizationId));
			if(messageSendList == null || messageSendList.size() == 0){
				notification.put("notifyCode", SysCode.MESSAGE_LIST_NULL.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_LIST_NULL);
	            renderResponseJson(response, params.getResponse(SysCode.MESSAGE_LIST_NULL, genResponseData(null, notification)));
	            logger.debug("message list is null",SysCode.MESSAGE_LIST_NULL);
				return;
			}
			for (MessageSend messageSend : messageSendList) {
				messageVO = new MessageVO();
				messageVO.setContent(messageSend.getMessageCreate().getContent());
				messageVO.setTitle(messageSend.getMessageCreate().getTitle());
				messageVO.setSendTime(messageSend.getSendTime());
				messageVO.setSendUserID(messageSend.getUserId());
				User userSend = userService.selectByPrimaryKey(messageSend.getUserId());
				if(userSend != null){
					messageVO.setSendUserName(userSend.getName());
					messageVO.setSendUserPic(getUserPicURL(userSend));
				}
				messageVO.setMessageType(messageSend.getMessageCreate().getMessageType());	
				messageVO.setMessageTypeRes(messageTypeZhuanHuan(messageSend.getMessageCreate().getMessageType()));
				returnMessageList.add(messageVO);
			}
			responseData.put("messageList", returnMessageList);
			responseData.put("imgUrlAddr", FileInstance.FTP_URL.trim());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("userMessageOrg is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.MESSAGE_LIST_NULL.getCode());
			notification.put("notifyMessage", SysCode.MESSAGE_LIST_NULL.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述： 根据关键字搜索我的组织下的消息         
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @throws ValiaDateRequestParameterException 
	 * @since 2015年10月20日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/searchMessage.json")
	public void searchMessage(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("begin searchMessage method");
		String[] paramKeys = {"keyword","organizationId"};
		ParamInfo params = new ParamInfo();
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		List<MessageSend> returnMessageList = new ArrayList<MessageSend>();
		try {
			RequestInfo ri=validate(request,response,paramKeys);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			String keyword = params.getParam("keyword");
			String organizationId = params.getParam("organizationId");
			returnMessageList = messageService.searchMessageByKeywordAndUserID(user.getId(), keyword,Long.parseLong(organizationId));
			
			if(returnMessageList == null || returnMessageList.size() == 0){
				logger.debug("message list is null",SysCode.MESSAGE_LIST_SEARCH_EMPTY);
				notification.put("code", SysCode.MESSAGE_LIST_SEARCH_EMPTY.getCode());
				notification.put("message", SysCode.MESSAGE_LIST_SEARCH_EMPTY.getMessage());
	            renderResponseJson(response, params.getResponse(SysCode.MESSAGE_LIST_SEARCH_EMPTY, genResponseData(null, notification)));
				return;
			}
			responseData.put("messageList", returnMessageList);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("searchMessage is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述： 删除消息(带删除操作的消息类型，可进行删除，传输我的消息messageReceiveId)        
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/delMessage.json")
	public void delMessage(HttpServletRequest request,
			HttpServletResponse response){
		String[] paramsKey = {"messageReceiveId|R"};
		ParamInfo params=null;
		Map<String, Object> notification = new HashMap<String, Object>();
		boolean isOk = false;
		try {
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri == null) return;
			params=ri.getParams();
			String messageReceiveId = params.getParam("messageReceiveId");
			isOk = messageService.deleteReceiveMessage(Long.parseLong(messageReceiveId));
			if(isOk){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(null, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.MESSAGE_LIST_NULL,genResponseData(null, null)));
			}
		} catch (Exception e) {
			logger.error("delMessage is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：创建项目后给人发送邀请承接项目         
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/sendInvitationProject.json", method = RequestMethod.POST)
	public void sendInvitationProject(HttpServletRequest request,
			HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		String[] paramKey = {"uids|R","content","projectId|R","organizationIds|R"};
		boolean isOk = false;
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			String uids = params.getParam("uids");
			String organizationIds = params.getParam("organizationIds");
			Long projectId = Long.parseLong(params.getParam("projectId"));
			String content = params.getParam("content");
			List<Long> userIds = JSON.parseArray(uids, Long.class);
			List<Long> orgs = JSON.parseArray(organizationIds, Long.class);
			if(userIds == null || userIds.size() == 0){
				notification.put("notifyCode", SysCode.MESSAGE_SEND_USER_ERR.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_SEND_USER_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.MESSAGE_SEND_USER_ERR, genResponseData(null, notification)));
				return;
			}
			isOk = tongRenSendMessageService.sendInvitationProjectMes(user.getId(), userIds, orgs, projectId, content);
			if(isOk){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(null, null)));
				return;
			}else{
				notification.put("notifyCode", SysCode.MESSAGE_ERROR.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_ERROR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.MESSAGE_ERROR, genResponseData(null, notification)));
				return;
			}	
		} catch (Exception e) {
			logger.error("get sendInvitationProject failed! param:"+request.getParameter("requestJson"),e);
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 功能描述：  分页查询我的消息列表--进入桐人首页的消息，可以看到任何组织给我发的消息
	 * @param request
	 * @param response                                                                                                 
	 * @throws ValiaDateRequestParameterException 
	 * @since 2016年1月29日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/userMessagePage.json")
	public void userMessagePage(HttpServletRequest request,HttpServletResponse response){
		String[] paramKeys = {"time|R","pageSize|R"};//
		ParamInfo params = new ParamInfo();
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		List<MessageVO> returnMessageList = new  ArrayList<MessageVO>();
		List<MessageSend> messageSendList = new ArrayList<MessageSend>();
		MessageVO messageVO = null;
		try {
			RequestInfo ri=validate(request,response,paramKeys);
			if(ri == null) return;
			params=ri.getParams();
			User user  =ri.getUser();
			long time = Long.valueOf(params.getParam("time"));
			int pageSize = Integer.valueOf(params.getParam("pageSize"));
			messageSendList = messageService.getMyMessages(user.getId(),time,pageSize);
			
			if(messageSendList == null || messageSendList.size() == 0){
				notification.put("notifyCode", SysCode.MESSAGE_LIST_NULL.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_LIST_NULL);
	            renderResponseJson(response, params.getResponse(SysCode.MESSAGE_LIST_NULL, genResponseData(null, notification)));
	            logger.debug("message list is null",SysCode.MESSAGE_LIST_NULL);
				return;
			}
			for (MessageSend messageSend : messageSendList) {
				messageVO = new MessageVO();
				messageVO.setContent(messageSend.getMessageCreate().getContent());
				messageVO.setTitle(messageSend.getMessageCreate().getTitle());
				messageVO.setSendTime(messageSend.getSendTime());
				messageVO.setSendUserID(messageSend.getUserId());
				messageVO.setOrganizationId(messageSend.getOrganizationId());
				User userSend = userService.selectByPrimaryKey(messageSend.getUserId());
				if(userSend != null){
					messageVO.setSendUserName(userSend.getName());
					messageVO.setSendUserPic(getUserPicURL(userSend));
				}
				messageVO.setMessageReceiveID(messageSend.getMessageReceive().getId());
				messageVO.setMessageType(messageSend.getMessageCreate().getMessageType());	
				messageVO.setMessageTypeRes(messageTypeZhuanHuan(messageSend.getMessageCreate().getMessageType()));
				returnMessageList.add(messageVO);
			}
			
			responseData.put("messageList", returnMessageList);
			responseData.put("imgUrlAddr", FileInstance.FTP_URL.trim());
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (Exception e) {
			logger.error("userMessagePage is error");
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	/**
	 * 查看消息 修改消息状态
	 * 
	 */
	@RequestMapping(value = "/updateMessageStatus.json", method = RequestMethod.POST)
	public void updateMessageStatus(HttpServletRequest request,HttpServletResponse response){
		
			String[] paramKey = {"messageReceiveId|R"};
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			ParamInfo params = new ParamInfo();
			try{
				RequestInfo ri=validate(request,response,paramKey);
				if(ri == null) return;
				params=ri.getParams();
				long messageReceiveId = Long.parseLong(params.getParam("messageReceiveId"));
		
				boolean status = messageService.updateMessageStatus(messageReceiveId);
				if(status){
					responseData.put("status", status);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
					return;
				}else{
					notification.put("notifyCode", "100411");
					notification.put("notifyMessage", "消息不存在或状态为已读");
					renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
	}
}
