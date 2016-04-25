package com.ginkgocap.tongren.resources.service;

import java.util.List;

import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;


/**
 * 资源接口
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月30日
 */
public interface ResourcesService {

	/**
	 * 功能描述： 添加本地资源和组织资源信息        
	 *                                                       
	 * @param localObject
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月30日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public long[] addResourcesLocalAndOrg(LocalObject localObject) throws Exception;
	
	/**
	 * 功能描述：  获得组织内的文档，如果      projectId为0 说明查出组织内所有文档 
	 * @param organizationId 组织ID
	 * @param projectId 项目ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<OrganizationObject> getOrgObject(long organizationId,long projectId)throws Exception;
	/**
	 * 功能描述：我的本地资源，根据组织ID和创建者ID，如果组织ID为0查询的是我的资源，否则查询的是我的组织内的本地资源         
	 *                                                       
	 * @param organizationId  组织ID
	 * @param createId 项目ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<LocalObject> getLocalObj(long organizationId,long createId) throws Exception;
	/**
	* 功能描述：  根据资源ID集合删除组织资源       
	 *                                                       
	 * @param ids资源IDs集合
	 * @param type 1:删除组织内容资源 2:删除我的资源文件
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月3日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean deleteResources(List<Long> ids,int type) throws Exception;
	/**
	 * 功能描述：保存项目附件资源        
	 *                                                       
	 * @param projectEnclosure
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月9日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public ResourceAttachment saveProjectEnclosure(ResourceAttachment projectEnclosure) throws Exception;
	
	/**
	 * 根据id获取资源信息
	 * @param lid
	 * @return
	 */
	public LocalObject getLocalObjectById(long lid,boolean isWithDetail);
	
	/**
	 * 获取组织资源对象，
	 * @param lid
	 * @param isWithDetail 是否包含详细信息
	 * @return
	 */
	public OrganizationObject getOrgObjectById(long lid,boolean isWithDetail);
}
