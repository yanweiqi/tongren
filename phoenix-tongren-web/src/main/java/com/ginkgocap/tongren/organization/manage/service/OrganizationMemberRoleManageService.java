package com.ginkgocap.tongren.organization.manage.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.system.code.FailureCodes;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

@Service
public class OrganizationMemberRoleManageService {

	private static Logger logger = LoggerFactory.getLogger(OrganizationMemberRoleManageService.class);

	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
	
	@UserAccessPermission(role = OrganizationRoles.CREATER,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ADMIN_ADD)
	@Deprecated
	public OrganizationMemberRole addAminRole(long createrId,long organizationId,long roleId,long organizationMemberId) throws NoPermissionException{
		OrganizationMemberRole organizationMemberRole = null;
		try {
			organizationMemberRole = organizationMemberRoleService.addMemberRole(createrId, organizationId, roleId, organizationMemberId);
		} catch (NoPermissionException e) {
			throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return organizationMemberRole;
	}
	

}
