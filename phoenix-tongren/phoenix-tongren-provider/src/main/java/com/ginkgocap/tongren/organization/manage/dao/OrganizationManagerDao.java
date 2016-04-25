package com.ginkgocap.tongren.organization.manage.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.organization.manage.model.OrganizationKd;
import com.ginkgocap.tongren.organization.manage.model.OrganizationPs;

public interface OrganizationManagerDao {
	
	public List<OrganizationKd> getOrganizationKdByOrgidAndName(Map<String, Object> map);
	
	public int getOrganizationKdByOrgidAndNameCount(Map<String, Object> map);
	
	public List<OrganizationPs> getOrganizationPsByOrgidAndName(Map<String, Object> map);
	
	public int getOrganizationPsByOrgidAndNameCount(Map<String, Object> map);
	
	public List<Long> getUserCreateRoleOrAdminRole(long userId);
	
	public int getUserCreateOrganizationSize(long userId);
	
	public int getUserJoinOrganizationSize(long userId);
	
	public int getUserCreateProjectSize(long userId);
	
	public int getUserUndertakenProjectSize(long user);
}
