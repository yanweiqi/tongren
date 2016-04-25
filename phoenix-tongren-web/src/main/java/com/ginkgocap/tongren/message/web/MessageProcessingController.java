/*
 * 文件名： MessageProcessingController.java
 * 创建日期： 2015年11月17日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.message.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService;
import com.ginkgocap.tongren.organization.system.code.SysCode;

 /**
 * 消息处理控制层 
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月17日
 */
@Controller
@RequestMapping("/message/processing")
public class MessageProcessingController extends BaseController {

	private final Logger logger  = LoggerFactory.getLogger(MessageProcessingController.class);//日志记录对象
	
	@Autowired
	private TongRenMessageProcessingService tongRenMessageProcessingService;//消息处理操作接口 
	
	/**
	 * 功能描述：申请类，退出申请，邀请类（同意、忽悠操作）         
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月17日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/handle.json", method = RequestMethod.POST)
	public void handle(HttpServletRequest request,HttpServletResponse response){
		String[] paramKey = {"status|R","messageReceiveId|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null) return;
			params=ri.getParams();
			/*
			 * messageType 消息类型(详细查看com.ginkgocap.tongren.common.model.MessageType)
			 */
			int status = Integer.parseInt(params.getParam("status"));
			long messageReceiveId = Long.parseLong(params.getParam("messageReceiveId"));
			int messageType = tongRenMessageProcessingService.getMessageTypeByMessageReceiveId(messageReceiveId);
			if(messageType == MessageType.INVITATION.getType()){//邀请组织消息
				if(tongRenMessageProcessingService.messageOrganizationInvitation(status, messageReceiveId)){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, null)));
					return;
				}else{
					notification.put("notifyCode", SysCode.MESSAGE_PROCESSING_ERR.getCode());
					notification.put("notifyMessage", SysCode.MESSAGE_PROCESSING_ERR.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.MESSAGE_PROCESSING_ERR, genResponseData(null, notification)));
					return;
				}
			}else if(messageType == MessageType.APPLICATION.getType()){//申请组织消息
				if(tongRenMessageProcessingService.messageOrganizationApplication(status, messageReceiveId)){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, null)));
					return;
				}else{
					notification.put("notifyCode", SysCode.MESSAGE_PROCESSING_ERR.getCode());
					notification.put("notifyMessage", SysCode.MESSAGE_PROCESSING_ERR.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.MESSAGE_PROCESSING_ERR, genResponseData(null, notification)));
					return;
				}
			}else if(messageType == MessageType.SIGNOUT.getType()){//申请退出组织消息
				if(tongRenMessageProcessingService.messageOrganizationSignOut(status, messageReceiveId)){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, null)));
					return;
				}else{
					notification.put("notifyCode", SysCode.MESSAGE_PROCESSING_ERR.getCode());
					notification.put("notifyMessage", SysCode.MESSAGE_PROCESSING_ERR.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.MESSAGE_PROCESSING_ERR, genResponseData(null, notification)));
					return;
				}
			}else if(messageType == MessageType.APPLICATION_EXTENSION_PROJECT.getType()){//延期项目
				if(tongRenMessageProcessingService.messageExtensioProject(status, messageReceiveId)){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, null)));
					return;
				}else{
					notification.put("notifyCode", SysCode.MESSAGE_PROCESSING_ERR.getCode());
					notification.put("notifyMessage", SysCode.MESSAGE_PROCESSING_ERR.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.MESSAGE_PROCESSING_ERR, genResponseData(null, notification)));
					return;
				}
			}else if(messageType == MessageType.INVITATION_PROJECT.getType()){//项目邀请组织承接
				
				
				String result = tongRenMessageProcessingService.messageInvitationProjectProcessing(status, messageReceiveId);
				if(result.equals("5")){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, null)));
					return;
				}
				else{
					String[] resStrArr={"操作消息出错","项目已被承接或已过期","承接项目出错"};
					warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(result)-2],params,response,responseData);
					return;
				}
//				
//				if(tongRenMessageProcessingService.messageInvitationProjectProcessing(status, messageReceiveId)){
//					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(null, null)));
//					return;
//				}else{
//					notification.put("notifyCode", SysCode.MESSAGE_PROCESSING_ERR.getCode());
//					notification.put("notifyMessage", SysCode.MESSAGE_PROCESSING_ERR.getMessage());
//					renderResponseJson(response, params.getResponse(SysCode.MESSAGE_PROCESSING_ERR, genResponseData(null, notification)));
//					return;
//				}
			}
			else{
				notification.put("notifyCode", SysCode.MESSAGE_PROCESSING_ERR_TYPE.getCode());
				notification.put("notifyMessage", SysCode.MESSAGE_PROCESSING_ERR_TYPE.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.MESSAGE_PROCESSING_ERR_TYPE, genResponseData(null, notification)));
				return;
			}
		} catch (Exception e) {
			logger.error("handle failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
		
	}
}
