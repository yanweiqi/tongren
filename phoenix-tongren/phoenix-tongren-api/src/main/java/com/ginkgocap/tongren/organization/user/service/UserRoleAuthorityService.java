package com.ginkgocap.tongren.organization.user.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;

/**
 * 
 * @author yanweiqi
 *
 */
public interface UserRoleAuthorityService {
	
	/**
	 * 我的组织角色ids
	 * @param userId
	 * @return List:ids
	 */
	public List<Long> getMyRoleIds(long userId,long organizationId) throws Exception;
	
	/**
	 * 我的组织权限
	 * @param userId
	 * @param organizationId
	 * @return Map<角色名称，权限List>
	 */
	public Map<String,List<OrganizationAuthority>> getMyOrganizationAuthorities(long userId,long organizationId) throws Exception;
	
	
}
