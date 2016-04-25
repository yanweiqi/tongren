package com.ginkgocap.tongren.organization.system.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DepartmentLeaderRoleHasAuthorities {
	
	private Map<OrganizationRoles, Set<OrganizationAuthorities>> roleMap = new HashMap<OrganizationRoles, Set<OrganizationAuthorities>>();
	private Set<OrganizationAuthorities> authorities = new HashSet<OrganizationAuthorities>();
	private static DepartmentLeaderRoleHasAuthorities instance;
	
	private DepartmentLeaderRoleHasAuthorities(){
		Map<OrganizationRoles, Set<OrganizationAuthorities>> map = MemberRoleHasAuthorities.getInstance().getRoleMap();
		authorities = map.get(OrganizationRoles.MEMBER);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT);
		roleMap.put(OrganizationRoles.DEPARTMENT_LEADER, authorities);
	}
	
	public static DepartmentLeaderRoleHasAuthorities getInstance(){
		if(null == instance){
			instance = new DepartmentLeaderRoleHasAuthorities();
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
