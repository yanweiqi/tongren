package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.manage.exception.DepMemberException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;

/**
 * @author yanweiqi
 *
 */
public interface OrganizationDepMemberService extends CommonService<OrganizationDepMember> {
	
	/**
	 * 添加部门成员
	 * @param OrganizationDepMember
	 * @param OrganizationDep
	 */
	public OrganizationDepMember add(OrganizationMember om,OrganizationDep od) throws Exception;
	
	/**
	 * 添加部门成员
	 * @param createrId            创建者ID
	 * @param organizationId       组织ID
	 * @param depId                部门ID 
	 * @param organizationMemberId 成员ID
	 * @return OrganizationDepMember
	 * @throws Exception
	 */
	public OrganizationDepMember add(long createrId,long organizationId,long depId,long organizationMemberId) throws Exception;
	
	/**
	 * 根据成员ID、组织ID、部门ID删除部门成员
	 * @param organizationId       组织ID
	 * @param depId                部门ID
	 * @param organizationMemberId 成员ID
	 * @return boolean             
	 * @throws Exception
	 */
	public boolean delDepMemberByMemberId(long organizationId,long depId,long organizationMemberId) throws Exception;
	
	/**
	 * 根据成员ID、组织ID、部门ID删除部门成员
	 * @param organizationId       组织ID
	 * @param depId                部门ID
	 * @return boolean             
	 * @throws Exception
	 */
	public boolean delDepMemberByDepId(long organizationId,long depId) throws Exception;
	
	/**
	 * 根据组织ID、部门ID获取部门成员
	 * @param organizationId 组织ID
	 * @param depId          部门ID
	 * @return List<OrganizationDepMember>
	 * @throws Exception
	 */
	public List<OrganizationDepMember> getDepMemberByDepId(long organizationId,long depId) throws Exception;
	
	/**
	 * 根据组织ID、部门ID获取部门成员
	 * @param organizationId 组织ID
	 * @param depId          部门ID
	 * @return List<OrganizationDepMember>
	 * @throws Exception
	 */
	public List<OrganizationDepMember> getDepMemberByOrganizationIdAndDepId(long organizationId,long depId) throws Exception;
	
	/**
	 * 根据组织ID、部门ID、成员ID获取部门成员
	 * @param organizationId
	 * @param depId
	 * @param organizationMemberId
	 * @return OrganizationDepMember
	 * @throws Exception
	 */
	public OrganizationDepMember getDepMemberByOrganizationIdAndDepIdAndMemberId(long organizationId,long depId,long organizationMemberId) throws DepMemberException, Exception;

	/**
	 * 根据组织ID、成员ID获取部门
	 * @param organizationId
	 * @param organizationMemberId
	 * @return OrganizationDepMember
	 * @throws Exception
	 */
	public OrganizationDepMember getDepMemberByOrganizationIdAndMemberId(long organizationId,long organizationMemberId) throws Exception;
}
