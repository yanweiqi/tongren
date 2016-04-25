package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;

public interface OrganizationDepService extends CommonService<OrganizationDep> {
	
	/**
	 * 默认部门的创建
	 * @param createId
	 * @param organizationId
	 */
	public List<OrganizationDep> createDefault(long createrId,long organizationId) throws Exception;
	
	/**
	 * 根据组织Id获取组织部门
	 * @param createrId
	 * @param organizationId
	 * @return
	 */
	public List<OrganizationDep> getDepsByOrganizationId(long organizationId) throws Exception;
	
	/**
	 * 创建部门
	 * @param createrId      创建者ID
	 * @param depName        部门名称
	 * @param discription    部门描述
	 * @param organizationId 组织ID
	 * @param pid            部门父ID
	 * @return OrganizationDep
	 * @throws Exception
	 */
	public OrganizationDep add(long createrId,String depName,String discription,long organizationId,long pid) throws  Exception;
	
	/**
	 * 根据部门名称获取部门
	 * @param organizationId  组织ID
	 * @param depName         部门名称
	 * @param pid			    部门父ID
	 * @return OrganizationDep 
	 * @throws Exception
	 */
	public OrganizationDep getDepByOrganizationIdAndDepNameAndPid(long organizationId,String depName,long pid) throws Exception;

	/**
	 * 根据父ID获取兄弟部门
	 * @param  pid 父ID
	 * @return List<OrganizationDep> 
	 * @throws Exception
	 */
	public List<OrganizationDep> getDepByPid(long pid) throws Exception;
	
	/**
	 * 根据ID修改部门
	 * @param depName          部门名称
	 * @param description      部门描述
	 * @param id               部门ID
	 * @return OrganizationDep 
	 * @throws Exception
	 */
	public OrganizationDep updateDepById(String depName,String description,long id) throws Exception; 
	
	/**
	 * 根据部门ID删除部门
	 * @param id             部门ID
	 * @param organizationId 组织ID
	 * @return TreeNode<OrganizationDep>
	 * @throws Exception
	 */
	public TreeNode<OrganizationDep> getSubTreeDepById(long id,long organizationId) throws Exception; 
	
	/**
	 * 根据部门ID获取部门树
	 * @param organizationId 组织ID
	 * @return List
	 */
	public List<TreeNode<OrganizationDep>> getTreeDepByOrganizationId(long organizationId) throws Exception;
	
	/**
	 * 根据部门ID删除部门
	 * @param id             部门ID   
	 * @param organizationId 组织ID
	 * @return Map<String, OrganizationDep>
	 * @throws Exception
	 */
	public Map<String, OrganizationDep> delDepById(long id,long organizationId) throws Exception;
}
