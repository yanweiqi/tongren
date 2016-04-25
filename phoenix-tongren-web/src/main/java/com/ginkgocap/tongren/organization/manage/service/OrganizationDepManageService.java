package com.ginkgocap.tongren.organization.manage.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.organization.authority.annotation.UserAccessPermission;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.system.code.FailureCodes;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

/**
 * 
 * @author yanweiqi
 *
 */
@Service
public class OrganizationDepManageService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleManageService.class);
	
	@Autowired
	private OrganizationDepService organizationDepService;
	
	/**
	 * 添加部门
	 * @param createrId
	 * @param depName
	 * @param description
	 * @param organizationId
	 * @param pid
	 * @return OrganizationDep
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_ADD)
	public OrganizationDep addDep(long createrId,long organizationId,String depName, String description,long pid) throws NoPermissionException{
		OrganizationDep organizationDep = null;
		try {
			 organizationDep = organizationDepService.add(createrId, depName, description, organizationId, pid);
		} 
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return organizationDep;
	}
	
	/**
	 * 修改部门
	 * @param createrId
	 * @param depName
	 * @param description
	 * @param organizationId
	 * @param pid
	 * @return OrganizationDep
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_EDIT)
	public OrganizationDep updateDepById(long createrId,long organizationId,String depName, String description,long depId) throws NoPermissionException{
		OrganizationDep organizationDep = null;
		try {
			 organizationDep = organizationDepService.updateDepById(depName, description, depId);
		} 
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return organizationDep;
	}
	
	/**
	 * 删除部门
	 * @param createrId   
	 * @param organizationId
	 * @param depId
	 * @return OrganizationDep
	 * @throws NoPermissionException
	 */
	@UserAccessPermission(role = OrganizationRoles.ADMIN,authority = OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_DEL)
	public boolean delDepById(long createrId,long organizationId,long depId) throws NoPermissionException{
		boolean status = false;
		try {
			 Map<String, OrganizationDep> depMap = organizationDepService.delDepById(depId, organizationId);
			 status = depMap.isEmpty();
			 logger.info("Delete DepId:"+depId+",status:" +status);
		} 
		catch (Exception e) {
		   logger.error(e.getMessage(),e);
		   throw new NoPermissionException(FailureCodes.Unauthorized.getCode(),FailureCodes.Unauthorized.getMessage());
		}
		return status;
	}

}
