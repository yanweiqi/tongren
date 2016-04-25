package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;

/**
 * 
 * @author yanweiqi
 *
 */
public interface OrganizationRoleService extends CommonService<OrganizationRole> {
	
	/**
	 * 创建默认角色
	 * @param roles
	 */
	public List<OrganizationRole> createDefault(long createId,long organizationId) throws Exception;
	
	/**
	 * 添加角色
	 * @param createrId 创建者ID
	 * @param organizationId 组织ID
	 * @param roleName 角色名称
	 * @return OrganizationRole：组织角色对象
	 * @throws NoPermissionException
	 */
	public OrganizationRole addRole(long createrId,long organizationId,String roleName,String description) throws Exception;
	
	/**
	 * 修改角色
	 * @param roleName
	 * @param description
	 * @param createrId
	 * @return OrganizationRole
	 * @throws NoPermissionException
	 */
	public OrganizationRole updateRole(String roleName,String description,long roleId) throws NoPermissionException;
	
	/**
	 * 根据角色名称获取组织角色
	 * @param organizationId 组织ID
	 * @param roleName 角色名称
	 * @return OrganizationRole：组织角色对象
	 */
	public OrganizationRole getOrganizationRoleByOrganizationIdAndRoleName(long organizationId,String roleName) throws Exception;
	
	/**
	 * 获取我在组织中的角色
	 * @param userId
	 * @param organizationId
	 * @return List<OrganizationRole> :组织角色List
	 */
	public List<OrganizationRole> getMyOrganizationRole(long userId,long organizationId) throws Exception;
	
	
	/**
	 * 根据OrganizationId获取组织中所有的角色
	 * @param organizationId
	 * @return List<OrganizationRole> :组织角色List
	 */
	public List<OrganizationRole> getOrganizationRoles(long organizationId) throws Exception;
	
	/**
	 * 获取角色权重，权限越高权重越大
	 * @param roleName
	 * @return
	 */
	public int getRoleWeihtByName(String roleName);
	

}
