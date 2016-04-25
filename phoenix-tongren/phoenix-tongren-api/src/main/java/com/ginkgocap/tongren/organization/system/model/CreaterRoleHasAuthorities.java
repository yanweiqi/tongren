package com.ginkgocap.tongren.organization.system.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 创建者权限
 * @author yanweiqi
 *
 */
public class CreaterRoleHasAuthorities {

	private Map<OrganizationRoles, Set<OrganizationAuthorities>> roleMap = new HashMap<OrganizationRoles, Set<OrganizationAuthorities>>();
	private Set<OrganizationAuthorities> authorities = new HashSet<OrganizationAuthorities>();
	private static CreaterRoleHasAuthorities instance;
	
	private CreaterRoleHasAuthorities(){
		for(OrganizationAuthorities oas: OrganizationAuthorities.values()){
			int length = oas.getKey().length();
			if (length == 1) {
				authorities.add(oas);
			}
		}
		roleMap.put(OrganizationRoles.CREATER, authorities);
	}
	
	public static CreaterRoleHasAuthorities getInstance(){
		if(null == instance){
			instance = new CreaterRoleHasAuthorities();
		}
		return instance;
	}

	public Map<OrganizationRoles,  Set<OrganizationAuthorities>> getRoleMap() {
		return roleMap;
	}

	public Set<OrganizationAuthorities> getAuthorities(OrganizationRoles key) {
		return roleMap.get(key);
	}
	
	
	
}
