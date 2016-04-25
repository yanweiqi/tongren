/*
 * 文件名： UndertakenService.java
 * 创建日期： 2015年11月11日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service;

import java.sql.Timestamp;
import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.task.exception.UndertakenException;


 /**
 *  承接项目业务接口
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月11日
 */
public interface UndertakenService extends CommonService<Undertaken> {

	/**
	 * 功能描述：   创建承接项目      
	 *                                                       
	 * @param projectId 承接项目ID
	 * @param recipientId 承接人ID
	 * @param recipientOrganizationId 承接人组织ID
	 * @param startTime 承接项目开始时间
	 * @param endTime 承接项目结束时间
	 * @param publishId 发布人ID
	 * @param publishOrganizationId 发布人所属组织ID
	 * @param 承接项目状态 (0未启动、1启动、2暂停、3完成、4放弃) 
	 * @return Undertaken 实体
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public Undertaken create(long projectId, long recipientId,
			long recipientOrganizationId, Timestamp startTime,
			Timestamp endTime, long publishId, long publishOrganizationId,int status)
			throws Exception;
	/**
	 * 功能描述：承接项目         
	 *                                                       
	 * @param projectId 项目ID
	 * @param recipientId 承接人ID
	 * @param recipientOrganizationId 承接人组织
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月18日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public Undertaken undertakenProject(long projectId,long recipientId,long recipientOrganizationId)throws UndertakenException;
	/**
	 * 功能描述：根据项目ID查询承接项目对象         
	 *                                                       
	 * @param projectId
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月12日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public Undertaken getUndertakenByProjectId(long projectId) throws Exception;
	/**
	 * 功能描述： 根据项目ID删除某一个承接项目对象        
	 *                                                       
	 * @param projectId
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月12日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean delUndertakenByProjectId(long projectId) throws Exception;
	/**
	 * 功能描述： 根据用户ID查询我创建的项目      
	 *                                                       
	 * @param recipientId 承接人ID
	 * @param status 0项目进行中、1完成、2、放弃、3已过期
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月18日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<Undertaken> getUndertakenList(long recipientId,int status)throws Exception;
	/**
	 * 功能描述：延期项目         
	 *                                                       
	 * @param projectId 项目ID
	 * @param cycle 延期天数
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月18日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean extensionProject(long projectId,int cycle) throws Exception;
	/**
	 * 功能描述： 放弃项目和结束项目和已过期项目       
	 *                                                       
	 * @param projectId 项目ID
	 * @param userId 放弃项目的人ID
	 * @param type 1: 结束项目 2：放弃项目 3:已过期项目
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean projectOperation(long projectId,long userId,int type) throws Exception;
	/**
	 * 功能描述：批量更新状态，如果项目到期项目变成已过期        
	 *                                                                                                                                                        
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月20日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public void batchUpdateStatus();
	/**
	 * 功能描述：把个人项目变成组织项目     
	 *                                                       
	 * @param projectId 项目ID
	 * @param organizationId 组织ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月23日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean updateOrgProject(long projectId,long organizationId) throws UndertakenException; 
	/**
	 * 功能描述：  判断是否组织是否存在进行中的项目
	 *                                                       
	 * @param organizationId 组织ID
	 * @return
	 * @throws UndertakenException                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年12月23日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean getUndertakenByOrganizationId(long organizationId) throws UndertakenException; 
	/**
	 * 功能描述：  根据组织ID和status承接状态     
	 *                                                       
	 * @param organizationId承接组织ID
	 * @param status '0项目进行中、1完成、2、放弃、3已过期'
	 * @return
	 * @throws UndertakenException                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年12月28日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<Undertaken> getUndertakenByOrganizationIdByStatus(long organizationId,int status) throws UndertakenException; 
}
