package com.ginkgocap.tongren.organization.manage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.exception.DepMemberException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;
import com.ginkgocap.tongren.organization.system.code.FailureCodes;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

/**
 * 
 * @author yanweiqi
 *
 */
@Service
public class OrganizationDepMemberManageService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleManageService.class);
	
	@Autowired
	private OrganizationDepMemberService organizationDepMemberService;
	
	/**
	 * 添加部门成员
	 * @param createrId
	 * @param organizationId
	 * @param depId
	 * @param organizationMemberId
	 * @return OrganizationDepMember
	 * @throws NoPermissionException
	 * @throws DepMemberException 
	 */
	@UserAccessPermission(role = OrganizationRoles.DEPARTMENT_LEADER,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_MEMBER_ADD)
	public OrganizationDepMember addMember(long createrId,long organizationId,long depId,long organizationMemberId) throws NoPermissionException, DepMemberException{
		OrganizationDepMember organizationDepMember = null;
		try {
			organizationDepMember = organizationDepMemberService.add(createrId, organizationId, depId, organizationMemberId); 
		}
		catch(DepMemberException e){
			throw e;
		}
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return organizationDepMember;
	}
	
	/**
	 * 删除部门成员
	 * @param createrId
	 * @param organizationId
	 * @param depId
	 * @param organizationMemberId
	 * @return boolean
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.DEPARTMENT_LEADER,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_MEMBER_DEL)
	public boolean delDepMemberByMemberId(long createrId,long organizationId,long depId,long organizationMemberId) throws NoPermissionException{
		boolean status = false;
		try {
			status = organizationDepMemberService.delDepMemberByMemberId(organizationId, depId, organizationMemberId); 
		} 
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return status;
	}
	
	
	/**
	 * 添加部门所有成员
	 * @param createrId
	 * @param organizationId
	 * @param depId
	 * @param organizationMemberId
	 * @return boolean
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_MEMBER_DEL)
	public boolean delDepMemberByDepId(long createrId,long organizationId,long depId) throws NoPermissionException{
		boolean status = false;
		try {
			status = organizationDepMemberService.delDepMemberByDepId(organizationId, depId); 
		} 
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return status;
	}
	
	
}
