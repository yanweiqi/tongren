package com.ginkgocap.tongren.organization.review.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.model.ReviewProcess;

public interface ReviewProcessService extends CommonService<ReviewProcess>{

	/**
	 *  生成申请流程编号
	 *  @author liweichao 
	 *  生成规则 RV-UUID
	 *  @return String
	 */
	public String getNomber()throws Exception;
	
	
	/**
	 * 
	 * 校验流程名称
	 * @author liweichao
	 * @param orgId 组织Id
	 * @param name 流程名称
	 * @return true:不重复可以使用  false:该组织已经存在 不可创建
	 * 
	 */
	public boolean checkProcessName(long orgId,String name)throws Exception;
	
	/**
	 * 查询组织下的审批流程
	 * @param orgId  组织Id
	 * 
	 */
	public List<ReviewProcess> getReviewByOrgId(long orgId)throws Exception; 
	
	
	/**
	 * 查询审批流程
	 * @param reviewId 审批流程Id
	 * @return ReviewProcess 审批流程对象
	 *
	 */
	public ReviewProcess getReviewById(long reviewId)throws Exception;
	
	
	/**
	 * 新增审批流程
	 * @author liweichao
	 * @param ReviewProcess 组织流程对象 
	 * @param genreList 流程类型集合
	 * @param objList 审核对象集合
	 * @return 组织Id
	 */
	
	public long create(ReviewProcess p,List<ReviewGenre> genreList,List<ReviewObject> objList)throws Exception;
	
	
	
	/**
	 * 
	 * 修改审批流程
	 * @author liweichao
	 * @param orgId 审核流程 Id
	 * @return 
	 * 
	 */
	public boolean updateReviewProcess(ReviewProcess p)throws Exception;
	
	
	/**
	 * 
	 * 删除流程审批
	 * @author liweichao
	 * @param reviewProcessId 审核流程 Id
	 * @return 
	 * 
	 */
	public boolean delect(long reviewProcessId)throws Exception;
	
	
}
