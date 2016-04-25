package com.ginkgocap.tongren.organization.resources.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.resources.exception.ResourceException;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelated;

/**
 * 关联接口
 * @author hanxifa
 *
 */
public interface ResourcesRelatedService extends CommonService<ResourcesRelated>{
	/**
	 * 保存关联信息
	 * @param resourcesRelated
	 * @throws ResourceException
	 */
	public void saveResourcesRelatedInfo(ResourcesRelated resourcesRelated) throws ResourceException ;
	
	/**
	 * 根据关联id获取关联的详细信息
	 * @param resourceId
	 * type 1 我的资源 2组织资源
	 * @return
	 * @throws ResourceException
	 */
	public List<ResourcesRelated> getResourcesRelatedByResourceId(long resourceId,int type,boolean isDetail)throws ResourceException ;
	
	/**
	 * 删除一个关联
	 * @param rrid
	 * @throws ResourceException
	 */
	public void delResourcesRelated(long rrid)throws ResourceException ;
	
}
