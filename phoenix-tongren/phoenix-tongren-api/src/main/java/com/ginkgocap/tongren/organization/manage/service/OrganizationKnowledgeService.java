package com.ginkgocap.tongren.organization.manage.service;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.manage.model.OrganizationKd;

/**
 * 组织知识service
 * @author Administrator
 *
 */
public interface OrganizationKnowledgeService extends CommonService<OrganizationKd> {
	
	/**
	 * 功能描述 ：创建组织知识
	 * @param knowledgeId 知识Id 
	 *
	 */
	OrganizationKd createOrganizationKnowledge(OrganizationKd kd) throws Exception;
	
	/**
	 * 功能描述 ：根据组织id 知识id查询组织资源
	 * @param knowledgeId 知识Id 
	 * @param org 组织id
	 */
	OrganizationKd getKdByKnowledgeIdAndOrgId(long knowledgeId,long orgId)throws Exception;
	
	/**
	 * 功能描述：根据组织知识Id查询详情
	 * 
	 */
	OrganizationKd getKdById(long orgKdId)throws Exception;
	
	/**
	 * 功能描述 ：根据组织id查询组织下的知识 分页
	 * 
	 */
	Page<OrganizationKd> getOrganizationKdPage(Page<OrganizationKd> page) throws Exception;
	
	/**
	 * 功能描述 ：根据组织知识id 删除组织知识
	 * 
	 */
	boolean delectOrganizationKnowledge(long id)throws Exception;
}
