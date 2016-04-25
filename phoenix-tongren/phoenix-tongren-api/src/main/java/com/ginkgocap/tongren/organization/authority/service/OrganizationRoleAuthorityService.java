package com.ginkgocap.tongren.organization.authority.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.authority.model.OrganizationRoleAuthority;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;

/**
 * @author yanweiqi
 */
public interface OrganizationRoleAuthorityService extends CommonService<OrganizationRoleAuthority> {

	/**
	 * 创建组织默认角色权限
	 * @param OrganizationRole
	 * @return List<OrganizationRoleAuthority>
	 * @exception Exception
	 */
	public List<OrganizationRoleAuthority> createDefault(OrganizationRole or) throws Exception;
	
	/**
	 * 获取角色权限
	 * @param roleId
	 * @param organizationId
	 * @return List：roleIds
	 * @exception Exception
	 */
	public List<Long> getAuthorityByRoleId(long roleId,long organizationId) throws Exception;
	
	/**
	 * 角色添加权限
	 * @param createrId
	 * @param roleId
	 * @param organizationId
	 * @param authorityNames
	 * @return List<OrganizationRoleAuthority>
	 * @throws Exception
	 */
	public List<OrganizationRoleAuthority> addRoleAuthorities(long createrId,long roleId,long organizationId,List<String> authorityNames) throws Exception;
	
}
