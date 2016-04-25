package com.ginkgocap.tongren.organization.manage.service;



import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.manage.exception.MemberRoleException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;

/**
 * 
 * @author yanweiqi
 *
 */
public interface OrganizationMemberRoleService extends CommonService<OrganizationMemberRole>{

	/**
	 * 添加成员角色,创建组织时候调用
	 * @param OrganizationMember  组织成员对象    
	 * @param roleId              角色ID
	 * @return OrganizationMemberRole
	 */
	public OrganizationMemberRole addMemberRole(OrganizationMember om,long roleId) throws Exception;
	
	/**
	 * 添加成员角色
	 * @param createrId               创建者ID
	 * @param organizationId          组织ID
	 * @param roleId                  角色ID
	 * @param organizationMemberId 	    成员ID
	 * @return OrganizationMemberRole 
	 * @throws Exception
	 * @throws MemberRoleException
	 */
	public OrganizationMemberRole addMemberRole(long createrId,long organizationId,long roleId,long organizationMemberId) throws MemberRoleException, Exception;
	
    /**
     * 获取我的角色Ids
     * @param organizationMemberId   成员ID  
     * @param organizationId         组织ID
     * @return List：roleIds
     */
	public List<Long> getMyRoleIds(long organizationMemberId,long organizationId) throws Exception;
	
	/**
	 * 获取角色对象List
     * @param organizationMemberId   成员ID  
     * @param organizationId         组织ID
	 * @return List<OrganizationMemberRole>
	 * @throws Exception
	 */
	//public List<OrganizationMemberRole> getMemberRolesByMemberIdAndOrganizationId(long organizationMemberId,long organizationId) throws Exception;
	
	/**
	 * 获取成员角色ID
	 * @param organizationId 组织ID
	 * @param roleId         角色ID
	 * @param memberId       成员ID
	 * @return Long 
	 */
	public Long getOrganizationMemberRoleIdByOrganizationIdAndRoleIdAndMemberId(long organizationId,long roleId,long memberId) throws Exception;
	
	/**
	 * 获取成员角色对象
	 * @param organizationId 组织ID
	 * @param roleId         角色ID
	 * @param memberId       成员ID
	 * @return Long 
	 */
	public OrganizationMemberRole getMemberRoleIdByOrganizationIdAndRoleIdAndMemberId(long organizationId,long roleId,long memberId) throws Exception;
	
	/**
	 * 根据组织ID、成员ID获取成员角色对象
	 * @param organizationId 组织ID
	 * @param memberId       成员ID
	 * @return Long 
	 */
	public OrganizationMemberRole getMemberRoleByOrganizationIdAndMemerId(long organizationId,long memberId) throws Exception;
	
	/**
	 * 根据组织ID，角色ID成员角色对象
	 * @param organizationId
	 * @param roleId
	 * @return List<OrganizationMemberRole>
	 * @throws Exception
	 */
	public List<OrganizationMemberRole> getMemberRoleByOrganizationIdAndRoleId(long organizationId,long roleId) throws Exception;
	
	/**
	 * 取消成员的管理员角色
	 * @param organizationId
	 * @param memberId
	 * @throws Exception
	 */
	public void deleteAdminRole(long organizationId,long memberId)throws Exception;
	
	/**
	 * 获取一个组织下所有管理员角色的组织成员
	 * @param organizationId
	 * @return
	 * @throws Exception 
	 */
	public List<OrganizationMemberRole> getAllAdminRole(long organizationId) throws Exception;
	
	/**
	 * 获取组织角色 
	 * type 1 创建者 2 管理员 3 普通成员
	 */
	public OrganizationMemberRole getMemberRoleByOrganizationIdAndMemerId(long organizationId,long memberId,int type)  throws Exception;
	
}
