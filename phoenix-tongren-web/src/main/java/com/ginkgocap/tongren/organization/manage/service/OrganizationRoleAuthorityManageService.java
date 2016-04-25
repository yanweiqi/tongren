package com.ginkgocap.tongren.organization.manage.service;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.authority.service.OrganizationAuthorityService;
import com.ginkgocap.tongren.organization.authority.service.OrganizationRoleAuthorityService;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.system.code.FailureCodes;

@Service
public class OrganizationRoleAuthorityManageService {

	private static Logger logger = LoggerFactory.getLogger(OrganizationRoleAuthorityManageService.class);

	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Autowired
	private OrganizationRoleAuthorityService organizationRoleAuthorityService;
	
	@Autowired
	private OrganizationAuthorityService organizationAuthorityService;
	
	public boolean isUserAccessPermission(long createrId,long organizationId,UserAccessPermission userAccessPermission)throws NoPermissionException {
		boolean isAccessPermission = false;
    	String authorityName = userAccessPermission.authority().name();
        //存储用户的权限集合
        Map<String, OrganizationAuthority> roleAuthorityMap = new LinkedHashMap<String, OrganizationAuthority>();
        try{
           //获取当前用户角色
           List<OrganizationRole> roles = organizationRoleService.getMyOrganizationRole(createrId, organizationId);
    	   if(null != roles && roles.size() > 0){
               for (OrganizationRole role : roles) {
            	   logger.info("CreateId:"+createrId+",OrganizationId:"+organizationId+",RoleName:"+role.getRoleName()+",RoleDescription:"+role.getDescription());
                   if (userAccessPermission.role().equals(role.getRoleName()) ){
                      isAccessPermission = true;
                      break;
                   }
                   //查询当前角色拥有的权限
                   List<Long> authorityIds = organizationRoleAuthorityService.getAuthorityByRoleId(role.getId(), organizationId);
                   for (long authorityId : authorityIds) {
                	   Map<String, OrganizationAuthority> tmp_map = organizationAuthorityService.getOrganizationAuthorityMapByAuthorityId(authorityId);
                	   roleAuthorityMap.putAll(tmp_map);
       			   }
               }
               if (!isAccessPermission){
                   for (Map.Entry<String, OrganizationAuthority> entry : roleAuthorityMap.entrySet()) {
                  	   if(authorityName.equals(entry.getKey())) {
                  		  isAccessPermission = true;
                  		  logger.info("CreateId:"+createrId+",OrganizationId:"+organizationId+",CheckAuthority:SUCCESS");
                  		 break;
                  	   }
          		   }
               }
    	   }
    	   else{
    		   logger.info(FailureCodes.Unauthorized.getMessage()+","+FailureCodes.Unauthorized.getDescription());
    		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
    	   }
        }
        catch(Exception e){
    	     logger.info(FailureCodes.Unauthorized.getMessage()+","+FailureCodes.Unauthorized.getDescription(),e);
    	     throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
        }
        return isAccessPermission;
	}
	
	public Map<String, OrganizationAuthority> getMyAuthoritys(long roleId,long organizationId) throws Exception{
        //存储用户的权限集合
		Map<String, OrganizationAuthority> roleAuthorityMap = new LinkedHashMap<String, OrganizationAuthority>();
		try {
	        //查询当前角色拥有的权限
	        List<Long> authorityIds = organizationRoleAuthorityService.getAuthorityByRoleId(roleId, organizationId);
	        for (long authorityId : authorityIds) {
	           //TreeNode<OrganizationAuthority> treeNode = organizationAuthorityService.getChildrenTreeNodeById(authorityId);
	           //treeNode.convertTreeNodeToMap(treeNode, roleAuthorityMap);
	           Map<String, OrganizationAuthority> tmp_Map = organizationAuthorityService.getOrganizationAuthorityMapByAuthorityId(authorityId);
	           roleAuthorityMap.putAll(tmp_Map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return roleAuthorityMap;		
	}

}
