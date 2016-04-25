package com.ginkgocap.tongren.organization.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.system.code.FailureCodes;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

/**
 * 
 * @author yanweiqi
 *
 */
@Service
public class OrganizationMemberManageService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationMemberManageService.class);
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	/**
	 * 删除角色
	 * @param createrId
	 * @param organizationId
	 * @param roleId
	 * @return boolean
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_DEL)
	public boolean delMember(long createrId,long organizationId,long userId) throws NoPermissionException{
		boolean status = false;
		try {
			checkMember(organizationId,userId);
			status = organizationMemberService.delMember(organizationId, userId);
		} catch (Exception e) {
			 throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return status;
	}
	


	/**
	 * 检测用户是否可删除
	 * @param roleId
	 * @throws NoPermissionException
	 */
	public void checkMember(long organizationId,long userId) throws Exception {
		List<OrganizationRole> roles = new ArrayList<OrganizationRole>();
		try {
			roles = organizationRoleService.getMyOrganizationRole(userId, organizationId);
			for (OrganizationRole role : roles) {
				if(role.getRoleName().equals(OrganizationRoles.CREATER.getValue())) 
					throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
				}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

}
