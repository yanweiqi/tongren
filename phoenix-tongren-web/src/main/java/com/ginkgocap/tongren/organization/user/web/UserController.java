package com.ginkgocap.tongren.organization.user.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.organization.user.model.GinTongUser;
import com.ginkgocap.tongren.organization.user.service.AccountService;
import com.ginkgocap.ywxt.user.model.User;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/account/user")
public class UserController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private AccountService accountService;

    @RequestMapping(value="getUserInfo.json")
	public void getUserBySessionId(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		String[] keys = new String[]{"sessionID"};
		ParamInfo params = new ParamInfo();
		try {
			params = parseRequest(request,response,"getUser",keys);
			String sessionID = params.getParam("sessionID");
			User user = accountService.getUserByName(sessionID);
			if(null != user){
				GinTongUser ginTongUser = new GinTongUser();
				BeanUtils.copyProperties( user, ginTongUser);
				responseData.put("user", ginTongUser);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));		
			}
			else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.UserIsNotExist.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.UserIsNotExist.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));	
			}
		} catch (ValiaDateRequestParameterException e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,e.getErrCode());
			notification.put(WebConstants.NOTIFINFO,e.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));	
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));	
		}
	}
	
}