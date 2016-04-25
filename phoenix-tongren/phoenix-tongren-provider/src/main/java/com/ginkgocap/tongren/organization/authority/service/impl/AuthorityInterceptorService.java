package com.ginkgocap.tongren.organization.authority.service.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NoPermissionException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationAuthorityService;
import com.ginkgocap.tongren.organization.authority.service.OrganizationRoleAuthorityService;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;

/**
 * @author yanweiqi
 */
@Aspect
public class AuthorityInterceptorService {
	
	private static Logger logger = LoggerFactory.getLogger(AuthorityInterceptorService.class);
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Autowired
	private OrganizationRoleAuthorityService organizationRoleAuthorityService;
	
	@Autowired
	private OrganizationAuthorityService organizationAuthorityService;
	

	//@Pointcut("execution(* com.ginkgocap.tongren.*.manage.service.*.*(..))")
	@Pointcut("@annotation(com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission)")
	public void authorityPoint(){}
	
    @Before("authorityPoint() && @annotation(userAccessPermission)")
	public void doAccessCheck(JoinPoint joinPoint, UserAccessPermission userAccessPermission) throws NoPermissionException,Exception{  
        System.out.println("====================================================================");  
		System.out.println("Before running loggingAdvice on method="+joinPoint.toString());
		System.out.println("Agruments Passed=" + Arrays.toString(joinPoint.getArgs()));
        System.out.println("权限控制,"+userAccessPermission.role());  
        
        //临时变量，方便测试
        long userId = 111111L;
        long organizationId = 3900797213736965L;
        boolean isAccessPermission = false;
        String authorityName = userAccessPermission.authority().name();
        //
        Map<String, OrganizationAuthority> roleAuthorityMap = new LinkedHashMap<String, OrganizationAuthority>();
        //获取当前用户角色
        List<OrganizationRole> roles = organizationRoleService.getMyOrganizationRole(userId, organizationId);
        for (OrganizationRole role : roles) {
            if(userAccessPermission.role().equals(role.getRoleName()) ){
               isAccessPermission = true;
               break;
            }
            //查询当前角色拥有的权限
            List<Long> authorityIds = organizationRoleAuthorityService.getAuthorityByRoleId(role.getId(), organizationId);
            for (long authorityId : authorityIds) {
               //TreeNode<OrganizationAuthority>	treeNode = organizationAuthorityService.getChildrenTreeNodeById(authorityId);
               //treeNode.convertTreeNodeToMap(treeNode, roleAuthorityMap);
            	Map<String, OrganizationAuthority> tmp_map = organizationAuthorityService.getOrganizationAuthorityMapByAuthorityId(authorityId);	
                roleAuthorityMap.putAll(tmp_map);  
			}
        }
        if(!isAccessPermission){
            for (Map.Entry<String, OrganizationAuthority> entry : roleAuthorityMap.entrySet()) {
            	if(authorityName.equals(entry.getKey())) {
            		isAccessPermission = true;
            		break;
            	}
    		}
        }
        if(!isAccessPermission){
        	logger.info("权限不够");
        	throw new NoPermissionException("权限不够！");
        }
    }  
    
    @Before("args(createrId)")
    public void doAccessCheckArguments(long createrId){
    	 System.out.println(String.valueOf(createrId)); 
    }
}
