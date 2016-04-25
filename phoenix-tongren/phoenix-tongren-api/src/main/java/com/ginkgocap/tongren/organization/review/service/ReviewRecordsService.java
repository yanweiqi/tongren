package com.ginkgocap.tongren.organization.review.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.review.model.ReviewRecords;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;
import com.ginkgocap.tongren.organization.review.vo.ReviewRecordsVO;

public interface ReviewRecordsService extends CommonService<ReviewRecords>{

	 
	 /**
	  * 
	  * 我的审批  (同意、驳回)
	  * applicationId :审核申请id 
	  * userId : 审核人id
	  * type 2： 审核通过  3:驳回
	  * 
	  */
	 public boolean signOff(long applicationId,long userId,int type)throws Exception;
	 
	 /**
	  * 查询审批记录
	  * @param id 申请流程Id
	  * @param status 记录状态 
	  * 
	  */
	 public List<ReviewRecords> getRecordsByProcessId(long id,int status) throws Exception;
	 
	 /**
	  * 功能描述 ： 根据申请Id查询审批记录
	  * @param  applicationId 申请id
	  * 
	  */
	 public List<ReviewRecords> getRecordsListByApplicationId(long applicationId);
	 
	 /**
	  * 功能描述 ： 根据申请id查询审核记录详细信息
	  * @param  applicationId 申请id
	 * @throws Exception 
	  * 
	  */
	 public List<ReviewRecordsVO> getRecordsDetailByApplicationId(long applicationId) throws Exception;
	 
		/***
		 * 查询我的审核列表
		 * @author hanxifa
		 * @param page
		 * 查询条件包含  orgId 组织id，userId 用户id，type 查询类型(0所有的 1 未审核的 2 已经审核)
		 */
	 public Page<ReviewApplicationListVO> getReviewListPage(Page<ReviewApplicationListVO> page);
	 
	 
	 /**
	  * 我未审核的流程列表
	  */
	 public List<ReviewRecords> getMyReviewRecordsList(long userId,long organizationId)throws Exception;
	 

	 
}
