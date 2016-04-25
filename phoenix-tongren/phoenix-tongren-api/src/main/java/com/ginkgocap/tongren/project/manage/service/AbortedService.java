package com.ginkgocap.tongren.project.manage.service;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.model.Aborted;

/**
 * 项目失败service
 * @author Administrator
 *
 */
public interface AbortedService extends CommonService<Aborted>{

	/**
	 * 功能描述：创建失败项目记录         
	 *                                                       
	 * @param projectId 项目Id
	 * @param projectUndertakenId 项目承接Id
	 * @param reason 失败原因
	 * @param recipientId 承接人ID
	 * @param undertakenOrganizationId 承接组织ID
	 * @param operationId 操作人ID
	 * @param createOrganizationId 项目创建组织ID
	 * @return 失败项目记录对象
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public Aborted create(long projectId, long projectUndertakenId,
			String reason, long recipientId, long undertakenOrganizationId,
			long operationId, long createOrganizationId) throws Exception;
}
