package com.ginkgocap.tongren.organization.system.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 管理员权限
 * @author yanweiqi
 *
 */
public class AdminRoleHasAuthorities {
	private Map<OrganizationRoles, Set<OrganizationAuthorities>> roleMap = new HashMap<OrganizationRoles, Set<OrganizationAuthorities>>();
	private Set<OrganizationAuthorities> authorities = new HashSet<OrganizationAuthorities>();
	private static AdminRoleHasAuthorities instance;
	
	private AdminRoleHasAuthorities(){
		Map<OrganizationRoles, Set<OrganizationAuthorities>> map = CreaterRoleHasAuthorities.getInstance().getRoleMap();

		authorities = map.get(OrganizationRoles.CREATER);
		
		for (OrganizationAuthorities or : authorities) {
			if(or.name().equals(OrganizationAuthorities.ORGANIZATION_MANAGE_ADMIN_ADD.name())){				
			   authorities.remove(or);
			   break;
			}
		}
		roleMap.put(OrganizationRoles.ADMIN, authorities);
	}
	
	public static AdminRoleHasAuthorities getInstance(){
		if(null == instance){
			instance = new AdminRoleHasAuthorities();
		}
		return instance;
	}

	
	public Map<OrganizationRoles, Set<OrganizationAuthorities>> getRoleMap() {
		return roleMap;
	}

	public Set<OrganizationAuthorities> getAuthorities(OrganizationRoles key) {
		return roleMap.get(key);
	}
	
	
}
