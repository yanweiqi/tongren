package com.ginkgocap.tongren.organization.review.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.review.model.ReviewApplication;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;

public interface ReviewApplicationService extends CommonService<ReviewApplication>{

	/**
	 * 保存审核记录(我的申请)
	 * @param ReviewApplication r
	 * @param r  记录实体
	 */
	 public ReviewApplication create(ReviewApplication r)throws Exception;
	 
	 /**
	  * 保存申请记录(模板申请)
	  * 
	  */
	 public ReviewApplication createTemplate(ReviewApplication r,List<ReviewObject> objectIds)throws Exception;
	 
	 /**
	  * 查看我的申请
	  * @param userId 用户Id
	  * @param oid 组织id
	  * @param type type 0 全部   1:已提申请  2:已完成申请
	  * 
	  */
	 public Page<ReviewApplicationListVO> getMyApplyFor(Page<ReviewApplicationListVO> page);
	 
	 /**
	  * 撤回申请
	  * @param applicationId 申请Id
	  * 
	  */
	 public boolean recallRecords(long applicationId)throws Exception;
	 /**
	  * 
	  *  查询申请
	  *  @param organizationId 组织Id
	  *  @param userId 用户Id
	  *  
	  */
	 public List<ReviewApplication> getUserReviewListByOrgId(long organizationId,long userId);
	
 
	/**
	 * 根据申请id查询详情，包含各个级别审核情况
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public ReviewApplicationListVO getReviewApplicationDetail(long id) throws Exception;
	
	/**
	 * v3版本的申请列表
	 * @param page
	 * @param type 0全部 1 审批中 2 已完成 3 已驳回 4 已撤回
	 * @return
	 */
	public Page<ReviewApplicationListVO> getMyApplyForV3(Page<ReviewApplicationListVO> page);
	
	/**
	 * v3重新提交
	 * 
	 */
	public ReviewApplication afreshSubmit(long oldId,ReviewApplication r,List<ReviewObject> objectIds) throws Exception;
	 
}
