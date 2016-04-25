package com.ginkgocap.tongren.organization.authority.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;

public interface OrganizationAuthorityService extends CommonService<OrganizationAuthority> {

	/**
	 * 桐人初始化的权限
	 * @param createId
	 * @param organizationId
	 */
	public List<OrganizationAuthority> createDefault(long createId,long organizationId) throws Exception;
	
	/**
	 * 获取桐人默认权限
	 * @return List<OrganizationAuthority> : 权限集合
	 */
	public List<OrganizationAuthority> getDefaultAuthoritys() throws Exception;
	
	/**
	 * 根据AuthorityName和organizationId获取自定义权限
	 * @param authorityName
	 * @param organizationId
	 * @return List<OrganizationAuthority> : 权限集合
	 */
	public List<OrganizationAuthority> getDefinedAuthoritysByAuthorityNo(String authorityName,long organizationId) throws Exception;
	
	/**
	 * 根据权限名称获取桐人默认权限
	 * @param authorityName
	 * @return OrganizationAuthority
	 * @throws Exception
	 */
	public OrganizationAuthority getDefaultAuthoritysByName(String authorityName) throws Exception;
	
	/**
	 * 返回树形权限集合
	 * @return List<TreeNode> : 树形权限集合
	 */
	public List<TreeNode<OrganizationAuthority>> getAuthorityTreeNodes() throws Exception;
	
	/**
	 * 根据权限Id查询子权限
	 * @param authorityId
	 * @return TreeNode 树形权限
	 */
    public TreeNode<OrganizationAuthority> getChildrenTreeNodeById(long authorityId) throws Exception;
    
    /**
     * 根据权限ID获取权限对象
     * @return OrganizationAuthority
     * @throws Exception
     */
    public OrganizationAuthority getOrganizationAuthorityById(long authorityId) throws Exception;
    
	/**
	 * 按权限名称查找
	 * @param authorityName
	 * @return TreeNode
	 */
    public TreeNode<OrganizationAuthority> getChildrenTreeNodeByName(String authorityName) throws Exception;
    
    /**
     * 根据权限ID返回权限Map对象
     * @param authorityId
     * @return
     * @throws Exception
     */
    public Map<String, OrganizationAuthority> getOrganizationAuthorityMapByAuthorityId(long authorityId) throws Exception;
}
