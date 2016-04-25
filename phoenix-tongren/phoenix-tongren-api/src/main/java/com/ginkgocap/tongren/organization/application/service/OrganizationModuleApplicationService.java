package com.ginkgocap.tongren.organization.application.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.application.model.OrganizationModuleApplication;

/**
 * @author yanweiqi
 */
public interface OrganizationModuleApplicationService extends CommonService<OrganizationModuleApplication> {
	
	/**
	 * 创建缺省的应用模块
	 * @param createrId
	 * @param organizationId
	 */
	public List<OrganizationModuleApplication> createDefault(long createrId,long organizationId) throws Exception;

}
