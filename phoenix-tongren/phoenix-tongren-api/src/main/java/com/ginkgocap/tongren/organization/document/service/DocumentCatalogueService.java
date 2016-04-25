package com.ginkgocap.tongren.organization.document.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;

/**
 * 文档目录service接口
 * @author hanxifa
 *
 */
public interface DocumentCatalogueService extends CommonService<DocumentCatalogue> {

	/**
	 * 增加一个文档目录 
	 */
	public String addCatalog(DocumentCatalogue catalogue);
	
	/**
	 * 根据用户id和组织id获取根目录列表
	 * 返回的数据包含所有一级子节点
	 * @param userId
	 * @param organizationId
	 * @return
	 */
	public DocumentCatalogue getRootCatalogs(long userId,long organizationId);
	
	/**
	 * 获取目录下的目录
	 * @param catalogId
	 * @return
	 */
	public List<DocumentCatalogue> getCatalogsById(long userId,long catalogId);
	
	/**
	 * 某个目录下的资源
	 * @param catalogId
	 * @return
	 */
	public List<BasicBean> getDocumentsById(long userId,long catalogId); 
	
	/**
	 * 更新某个目录的名字
	 * @param catalogue
	 * @return
	 */
	public String updateCatalogueName(long userId,long catalogId,String name);
	
	/**
	 * 删除目录
	 * 删除目录后，如果该目录下的资源不属于任何目录，则移动到未分组目录下
	 * @param catalogId
	 * @return
	 */
	public String deleteCatalogue(long userId,long catalogId);
	
	/**
	 * 删除某个目录下的资源，被删除的只能是资源，不能是目录
	 * @param catalogId
	 * @param diresourceId 对应 DirectorySource.id
	 * @return
	 */
	public String deleteResourceOfCatalogue(long userId,long diresourceId);
	/**
	 * 获取用户在某个组是下的文档树
	 * @param userId
	 * @param organizationId
	 * @return
	 */
	public DocumentCatalogue getCatalogueTree(long userId,long organizationId,long sourceId);
	
	/**
	 * 设置某个文档到指定目录
	 * 如果toDirIds参数空，则不做任何改变
	 * @param userId
	 * @param organizationId
	 * @param sourceId
	 * @param toDirIds
	 * @return
	 */
	public String setDocumentToDirs(long userId,long organizationId,long sourceId,long[] toDirIds);
	
	/**
	 * 根据用户ie和组织id获取默认的目录
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public DocumentCatalogue getDefaultCatalog(long userId,long orgId);
	
	
	/**
	 * 删除一个资源和所有目录关联关系，删除我的资源情况下调用
	 * @param orgId
	 * @param resourceId
	 */
	public void delResourceOfCatalog(long resourceId,long orgId,long userId);
	
	/**
	 * 如果资源不在指定的目录下，则是设置目录，否则直接返回
	 * 防止如果重复设置默认目录，会把之前已经设置的目录覆盖掉
	 * @param userId
	 * @param organizationId
	 * @param sourceId
	 * @param toDirIds
	 * @return
	 */
	public String setDocumentToDirsIfAbsent(long userId, long organizationId, long sourceId, long[] toDirIds);

}
