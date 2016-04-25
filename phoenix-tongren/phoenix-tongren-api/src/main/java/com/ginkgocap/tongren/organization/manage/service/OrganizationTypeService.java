package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;

import com.ginkgocap.tongren.organization.manage.model.OrganizationType;

public interface OrganizationTypeService {
	
	
	/**
	 * 返回行业基础数据
	 */
	public List<OrganizationType> getOrganizationData(int id);
	
	/**
	 * 根据 id 查询对象名称
	 *  
	 */
	public String getOrganizationTypeName(String id);
	
	
	
}
