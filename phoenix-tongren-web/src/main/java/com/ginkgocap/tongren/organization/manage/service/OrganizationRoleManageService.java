package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.exception.MemberRoleException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.FailureCodes;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

/**
 * 
 * @author yanweiqi
 *
 */
@Service
public class OrganizationRoleManageService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleManageService.class);
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
	
	/**
	 * 修改角色
	 * @param createrId
	 * @param organizationId
	 * @param roleName
	 * @param description
	 * @param roleId
	 * @return OrganizationRole
	 * @throws NoPermissionException 
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_EDIT)
	public OrganizationRole updateRole(long createrId,long organizationId,String roleName, String description, long roleId) throws NoPermissionException {
		OrganizationRole organizationRole = null;
		try {
			 organizationRole = organizationRoleService.updateRole(roleName, description,roleId);
		} catch (Exception e) {
			 throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return organizationRole;
	}
	
	/**
	 * 添加组织角色
	 * @param createId
	 * @param organizationId
	 * @param roleName
	 * @param description
	 * @return OrganizationRole
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_ADD)
	public OrganizationRole addRole(long createId, long organizationId, String roleName, String description) throws NoPermissionException{
		OrganizationRole organizationRole = null;
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			logger.info("Aready add Role CreateId:"+createId+",organizationId:"+organizationId+",roleName:"+roleName+"description:"+description);
			organizationRole = organizationRoleService.addRole(createId, organizationId, roleName, description);
			stopWatch.stop();
			logger.info("Add Role use time "+stopWatch.getTime()+"ms");
		} catch (Exception e) {
			 throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return organizationRole;
	}
	
	/**
	 * 删除角色
	 * @param createrId
	 * @param organizationId
	 * @param roleId
	 * @return boolean
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_DEL)
	public boolean delRole(long createrId,long organizationId,long roleId) throws NoPermissionException{
		boolean status = false;
		try {
			status = organizationRoleService.deleteEntityById(roleId);
		} catch (Exception e) {
			 throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return status;
		
	}
	
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_LIST_SHOW)
	public List<OrganizationRole> getRolesByOrgazationId(long createrId,long organizationId) throws NoPermissionException{
		List<OrganizationRole> roles = null;
	    try {
			roles =  organizationRoleService.getOrganizationRoles(organizationId);
		} catch (Exception e) {
			throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
	    return roles;
	}

	/**
	 * 检测用户是否可删除
	 * @param roleId
	 * @throws NoPermissionException
	 */
	public void checkRoleName(long roleId,Long organizationId) throws NoPermissionException,MemberRoleException{
		OrganizationRole organizationRole =  organizationRoleService.getEntityById(roleId);
		String roleName = organizationRole.getRoleName();
		if(roleName.equals(OrganizationRoles.CREATER.getValue())) throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		if(roleName.equals(OrganizationRoles.ADMIN.getValue())) throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		if(roleName.equals(OrganizationRoles.DEPARTMENT_LEADER.getValue())) throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		if(roleName.equals(OrganizationRoles.GROUP_LEADER.getValue())) throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		
		if(null != organizationId){
			try {
				List<OrganizationMemberRole> memberRoles = organizationMemberRoleService.getMemberRoleByOrganizationIdAndRoleId(organizationId, roleId);
				if(null != memberRoles && memberRoles.size() > 0){
					throw new MemberRoleException(ApiCodes.OrganizationRoleDoNotDel.getCode(),ApiCodes.OrganizationRoleDoNotDel.getMessage()+","+ApiCodes.OrganizationRoleDoNotDel.getDescription());
				}
			} catch (Exception e) {
			    logger.error(e.getMessage(),e);	
			}
		}

        
	}
	

}
