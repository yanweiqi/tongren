package com.ginkgocap.tongren.organization.resources.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.resources.exception.ResourceException;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelatedDetail;

/**
 * 关联的详细信息
 * @author hanxifa
 *
 */
public interface ResourcesRelatedDetailService extends CommonService<ResourcesRelatedDetail> {
	
	/**
	 * 根据资源id获取关联的信息
	 * @param relatedId
	 * @return
	 * @throws ResourceException
	 */
	public List<ResourcesRelatedDetail> getResourcesRelatedsByRelatedId(long relatedId) throws ResourceException;
	
	 /**
	  * 根据关联主表的id删除关联的详细信息
	  * @param resourcesRelatedId
	  * @throws ResourceException
	  */
	public void delResourcesRelatedDetail(long resourcesRelatedId )throws ResourceException;;

}
