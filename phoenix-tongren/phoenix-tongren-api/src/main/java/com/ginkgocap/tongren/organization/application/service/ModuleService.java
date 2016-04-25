package com.ginkgocap.tongren.organization.application.service;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.application.model.Module;

/**
 * @author yanweiqi
 */
public interface ModuleService extends CommonService<Module> {
	
	/**
	 * 创建缺省的组织模块<br>
	 * 0代表缺省创建者<br>
	 * 0代表缺省组织<br>
	 * 桐人初始化的调用Junit，不需要单独创建。
	 * @param 
	 */
	public void createDefault(long createId,long organizationId);
	
	public void testOrgName();

}
