package com.ginkgocap.tongren.project.manage.service;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.Exception.ApplyException;
import com.ginkgocap.tongren.project.manage.model.Apply;

/**
 * 项目申请service
 *  
 * @author yanweiqi
 * @version 
 * @since 2015年11月9日
 */
public interface ApplyService extends CommonService<Apply>{
	
	/**
	 * 根据项目ID获取申请列表
	 * @param projectId
	 * @return List<Apply>
	 * @throws Exception
	 */
	public List<Apply> getApplyByProjectId(long projectId) throws Exception;
	
	/**
	 * 创建项目申请
	 * @param proposerId     申请人
	 * @param organizationId 申请人所属组织ID，用户如果没有组织，组织ID为0
	 * @param applyTime      申请时间
	 * @param projectId      项目ID
	 * @param reviewerId     审核人ID
	 * @return Apply
	 * @throws Exception
	 */
	public Apply create(long proposerId,
				        long organizationId,
				        Timestamp applyTime,
				        long projectId,
				        long reviewerId)throws Exception;
	/**
	 * 接受申请
	 * @param reviewerId	  审核人ID
	 * @param projectId      项目ID
	 * @param proposerId     申请人
	 * @param organizationId 申请人所属组织ID
	 * @return Map<String, Object>
	 * @throws Exception
	 * @throws ApplyException
	 */
	public Map<String, Object> accept(long reviewerId,long projectId,long proposerId,long organizationId) throws ApplyException, Exception;
	
	/**
	 * 拒绝申请
	 * @param reviewerId	  审核人ID
	 * @param projectId      项目ID
	 * @param proposerId     申请人
	 * @param organizationId 申请人所属组织ID
	 * @return Apply
	 * @throws Exception
	 */
	public Apply refuse(long reviewerId, long projectId,long proposerId,long organizationId) throws Exception;
	
	/**
	 * 根据审核人ID、项目ID、状态获取申请列表
	 * @param reviewerId     审核人ID
	 * @param projectId      项目ID
	 * @param stauts         申请状态
	 * @return List<Apply>
	 * @throws Exception
	 */
	public List<Apply> getMyApplysByProjectId(long reviewerId, long projectId,Integer stauts) throws Exception;
	
	/**
	 * 根据审核人ID、项目ID、状态、申请人ID、申请人所属组织ID，获取申请
	 * @param reviewerId     审核人ID
	 * @param projectId      项目ID
	 * @param stauts         申请状态
	 * @param proposerId     申请人ID
	 * @param organizationId 申请人所属组织ID
	 * @return Apply
	 * @throws Exception
	 */
	public Apply getMyApplyByProjectIdAndProposerId(long reviewerId, long projectId,int stauts,long proposerId,long organizationId) throws Exception;
	
	/**
	 * 根据projectId查看项目是否被承接
	 * @param projectId  项目ID
	 * @param stauts     状态
	 * @return Apply
	 * @throws Exception
	 */
	public List<Apply> isUndertakeByProjectId(long projectId,int stauts) throws Exception;
	
	/**
	 * 根据审核人ID、状态获取申请列表
	 * @param reviewerId  审核人ID
	 * @param status      状态
	 * @return List<Apply>
	 * @throws Exception
	 */
	public List<Apply> getAppliesByReviewerIdAndStatus(long reviewerId,Integer status) throws Exception;
	
	/**
	 * 根据申请人Id获取申请列表
	 * @param  proposerId  申请人ID
	 * @param  status      申请状态
	 * @return List<Apply>
	 * @throws Exception
	 */
	public List<Apply> getAppliesByProposerIdAndStatus(long proposerId, Integer status) throws Exception;
}
