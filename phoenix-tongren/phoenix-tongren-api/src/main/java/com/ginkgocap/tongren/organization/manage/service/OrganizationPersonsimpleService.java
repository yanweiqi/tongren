package com.ginkgocap.tongren.organization.manage.service;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.manage.model.OrganizationPs;

/**
 * 组织人脉Service
 * @author Administrator
 *
 */
public interface OrganizationPersonsimpleService extends CommonService<OrganizationPs>{
	
	/**
	 * 功能描述 ：创建组织人脉
	 * @param personId 人脉Id
	 * @param user  分享人id
	 * @param org 组织id
	 * @return OrganizationPs 人脉对象
	 */
	OrganizationPs createOrganizationPs(long personId,long userId,long orgId,String fromType) throws Exception;
	
	/**
	 * 功能描述 ： 根据组织id和人脉id  
	 * 
	 * 
	 */
	OrganizationPs getPsByPsIdAndOrganizationId(long personId,long orgId)throws Exception;
	
	/**
	 * 
	 * 功能描述 ：根据组织人脉id 查询
	 * 
	 */
	OrganizationPs getOrgPsById(long organizationPsId)throws Exception;
	
	/**
	 * 功能描述  ：根据组织id查询组织下的人脉
	 * 
	 */
	Page<OrganizationPs> getOrganizationPsPage(Page<OrganizationPs> page)throws Exception;
	
	/**
	 * 
	 * 功能描述 ：根据人脉id 删除人脉
	 * 
	 */
	 public boolean delectPersonsimple(long personId)throws Exception;

}
