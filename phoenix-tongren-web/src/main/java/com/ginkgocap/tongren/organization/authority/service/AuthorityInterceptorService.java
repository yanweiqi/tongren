package com.ginkgocap.tongren.organization.authority.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleAuthorityManageService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;

/**
 * @author yanweiqi
 */
@Aspect
public class AuthorityInterceptorService {
	
	private static Logger logger = LoggerFactory.getLogger(AuthorityInterceptorService.class);
	
	@Autowired
    private OrganizationRoleAuthorityManageService organizationRoleAuthorityManageService;
	
	@Pointcut("@annotation(com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission)")
	public void controllerAspect(){}
	
	@Around("controllerAspect() && @annotation(userAccessPermission)")
	public Object doAccessCheck(ProceedingJoinPoint joinPoint, UserAccessPermission userAccessPermission) throws Throwable { 		
		logger.info("=====进入权限控制 begin=====");  
		Object[] parameters = joinPoint.getArgs();
		if(parameters[0] instanceof HttpServletRequest){
			logger.info("拦截 web controller ");
			BaseController baseController= (BaseController) joinPoint.getTarget();
			HttpServletRequest req=(HttpServletRequest) parameters[0];
			HttpServletResponse res= (HttpServletResponse) parameters[1];
			User user=baseController.validateUserIsLogin(req,res);
			if(user==null){
				baseController.warpMsg(SysCode.SECURITY_ERR,SysCode.SECURITY_ERR.getMessage(), new ParamInfo(), res);
				return null;
			}
			String[] keys={"orgId","oid","organizationId"};
			try {
				ParamInfo pi=baseController.parseRequest(req, res, "validateParams", keys);
				String orgId=null;
				for(String k:keys){
					if(pi.getParam(k)!=null){
						orgId=pi.getParam(k);
					}
				}
				if(orgId!=null){
					try{
						organizationRoleAuthorityManageService.isUserAccessPermission(user.getId(),Long.parseLong(orgId),userAccessPermission);
					} catch(NoPermissionException e){
						logger.info("权限认证失败",e);
						Map<String, Object> notification=new HashMap<String, Object>();
						notification.put(WebConstants.NOTIFCODE,e.getErrCode());
						notification.put(WebConstants.NOTIFINFO,e.getMessage());
						ParamInfo params=new ParamInfo();
						baseController.renderResponseJson(res, params.getResponse(SysCode.ERROR_CODE, baseController.genResponseData(null, notification)));
						return null;
			        }
				}else{
					baseController.warpMsg(SysCode.PARAM_IS_ERROR,"组织id为空", new ParamInfo(), res);
					logger.info("组织id为空： "+req.getParameter("requestJson"));
					return null;
				}
			} catch (Exception e) {
				baseController.warpMsg(SysCode.PARAM_IS_ERROR, SysCode.PARAM_IS_ERROR.getMessage(), new ParamInfo(), res);
				logger.info("参数错误 "+req.getParameter("requestJson"));
				return null;
			}
			return joinPoint.proceed();
		}
		else{
			logger.info("拦截 service ");
	        logger.info("Before running loggingAdvice on method="+joinPoint.toString());
	        logger.info("Agruments Passed=" + parameters);
	        logger.info("需要的权限"+userAccessPermission.role());
	        try{
	        	long createrId = Long.valueOf(parameters[0].toString()) ;
	        	long organizationId = Long.valueOf(parameters[1].toString());
	        	organizationRoleAuthorityManageService.isUserAccessPermission(createrId,organizationId,userAccessPermission);
	        	logger.info("=====进入权限控制 end=====");
	        	return joinPoint.proceed();
	        }
	        catch(NoPermissionException e){
				throw e;
	        }
		}
    }
	
	public String getClassName(JoinPoint joinPoint){
		String clazzName = joinPoint.getTarget().getClass().getName();
		return clazzName;
	}
	
	public String getMethodName(JoinPoint joinPoint){
		String methodName = joinPoint.getSignature().getName(); 
		return methodName;
	}
	
	public void getParameters(JoinPoint joinPoint){
		Object[] objs = joinPoint.getArgs();
		for (Object obj : objs) {
			logger.info(obj.toString());
		}
	}
}
