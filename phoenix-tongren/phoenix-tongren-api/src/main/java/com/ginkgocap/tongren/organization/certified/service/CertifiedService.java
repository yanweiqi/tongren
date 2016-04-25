package com.ginkgocap.tongren.organization.certified.service;

import com.ginkgocap.tongren.organization.certified.model.Certified;

/**
 * 组织认证接口
 * @author hanxifa
 *
 */
public interface CertifiedService {

	/**
	 * 增加组织认证
	 * @param certified
	 * @return 1_id 增加成功  2数据库中已经存在该组织id对应的认证信息  3 更新失败
	 */
	public String add(Certified certified);
	
	/**
	 * 根据id获取组织认证信息
	 * @param id
	 * @return
	 */
	public Certified getById(long id);
	
	
	/**
	 * 根据组织id获取组织认证信息
	 * @param id
	 * @return 查询不到则返回null
	 */
	public Certified getByOrgId(long id);
	
	/**
	 * 修改组织认证信息
	 * @param certified
	 * @return 1 更新成功 2 数据库中不存在记录 3 状态更新流程不合法  4 状态值只能为 1 2 3 
	 */
	public String modify(Certified certified);
	
	
	/***
	 * 修改组织认证状态
	 * @param id 认证组织id
	 * @param status 更新状态  只能为 1 2 3 
	 * @param userId 操作的用户id 
	 * @return 1 更新成功 2 数据库中不存在记录 3 当前状态不能更新  4 状态值只能为 1 2 3 
	 */
	public String updateStatus(long id,int status,long userId);
	
	
}
