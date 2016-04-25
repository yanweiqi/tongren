package com.ginkgocap.tongren.organization.authority.web;

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

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationAuthorityService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.google.common.collect.Maps;

/**
*  组织控制层
* @author  李伟超
* @version 
* @since 2015年10月15日
*/
@Controller
@RequestMapping("/organization/author") 
public class OrganizationAuthorityController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationAuthorityController.class);
	
	@Autowired
	private OrganizationAuthorityService organizationAuthorityService;
	
	@RequestMapping(value ="/init.json",method = RequestMethod.GET)
	public void getIndexContent(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = new ParamInfo();
		try{
			List<OrganizationAuthority> oas = organizationAuthorityService.createDefault(0L, 0L);
		    responseData.put("oas", oas);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
}
