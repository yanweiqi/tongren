package com.ginkgocap.tongren.organization.system.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GroupLeaderRoleHasAuthorities {

	private Map<OrganizationRoles, Set<OrganizationAuthorities>> roleMap = new HashMap<OrganizationRoles, Set<OrganizationAuthorities>>();
	private Set<OrganizationAuthorities> authorities = new HashSet<OrganizationAuthorities>();
	private static GroupLeaderRoleHasAuthorities instance;
	
	private GroupLeaderRoleHasAuthorities(){
		authorities.addAll(MemberRoleHasAuthorities.getInstance().getAuthorities(OrganizationRoles.MEMBER));
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_GROUP);
		roleMap.put(OrganizationRoles.GROUP_LEADER, authorities);
	}
	
	public static GroupLeaderRoleHasAuthorities getInstance(){
		if(null == instance){
			instance = new GroupLeaderRoleHasAuthorities();
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
