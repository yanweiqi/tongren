package com.ginkgocap.tongren.organization.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationAuthorityService;
import com.ginkgocap.tongren.organization.authority.service.OrganizationRoleAuthorityService;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.user.service.UserRoleAuthorityService;

/**
 * @author yanweiqi
 */
@Service
public class UserRoleAuthorityServiceImpl implements UserRoleAuthorityService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserOrganizationServiceImpl.class);

	@Autowired
	private OrganizationAuthorityService organizationAuthorityService;
	
	@Autowired
	private OrganizationRoleAuthorityService organizationRoleAuthorityService;
	
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleyService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Override
	public List<Long> getMyRoleIds(long userId, long organizationId) throws Exception{
		try {
			OrganizationMember  member = organizationMemberService.getMemberByOrganizationIdAndUserId(organizationId,userId);
			return organizationMemberRoleyService.getMyRoleIds(member.getId(), organizationId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
	}

	@Override
	public Map<String, List<OrganizationAuthority>> getMyOrganizationAuthorities(long userId, long organizationId) throws Exception {
		Map<String, List<OrganizationAuthority>> map = new HashMap<String, List<OrganizationAuthority>>();
		try {
			List<Long> roleIds =  getMyRoleIds(userId, organizationId);
			for (Long roleId : roleIds) {
				List<Long> authorityIds = organizationRoleAuthorityService.getAuthorityByRoleId(roleId, organizationId);
				List<OrganizationAuthority> OrganizationAuthorities = organizationAuthorityService.getEntityByIds(authorityIds);
				OrganizationRole or = organizationRoleService.getEntityById(roleId);
				map.put(or.getRoleName(), OrganizationAuthorities);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return map;
	}

}
