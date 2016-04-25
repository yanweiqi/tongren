package com.ginkgocap.tongren.organization.system.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MemberRoleHasAuthorities {

	private Map<OrganizationRoles, Set<OrganizationAuthorities>> roleMap = new HashMap<OrganizationRoles, Set<OrganizationAuthorities>>();
	private Set<OrganizationAuthorities> authorities = new HashSet<OrganizationAuthorities>();
	private static MemberRoleHasAuthorities instance;
	
	private MemberRoleHasAuthorities() {
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_ADD);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_MEMBER_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_GROUP_MEMBER_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_GROUP_MEMBER_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_MEMBER_LIST_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_DEPARTMENT_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_RESOURCE_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_RESOURCE_DOWNLOAD);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_RESOURCE_UPLOAD);
		
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_ROLE_AUTHORITY_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_ATTENDANCE_SELF_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_ATTENDANCE_SELF_EXPORT);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_WORKFLOW_SHOW);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_WORKFLOW_USE);
		authorities.add(OrganizationAuthorities.ORGANIZATION_MANAGE_APPLICATION_MESSAGE);
		
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_ADD);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_DEL);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_EDIT);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_SHOW);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_PUBLISH);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_APPLY);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_REVIEW);
		
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_ADD);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_EDIT);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_DEL);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_SHOW);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_ASSIGN);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_FORWARD);
		authorities.add(OrganizationAuthorities.PROJECT_MANAGE_TASK_REPLY);
		
		roleMap.put(OrganizationRoles.MEMBER, authorities);
	}

	public static MemberRoleHasAuthorities getInstance(){
		if(null == instance){
			instance = new MemberRoleHasAuthorities();
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
