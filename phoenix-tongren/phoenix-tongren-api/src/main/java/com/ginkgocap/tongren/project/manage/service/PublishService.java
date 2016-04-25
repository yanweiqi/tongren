package com.ginkgocap.tongren.project.manage.service;


import java.sql.Timestamp;
import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.project.manage.Exception.PublishException;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;

/**
 * 项目创建service
 *  
 * @author yanweiqi
 * @version 
 * @since 2015年11月9日
 */
public interface PublishService extends CommonService<Publish>{
	
	/**
	 * 根据项目Id获取项目发布
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public Publish getPublishByProjectId(long projectId) throws Exception;
	
	 /**
	  * 发布项目
	  * @param publisherId  发布者ID
	  * @param status       发布状态
	  * @param startDate    发布时间
	  * @param endDate      结束时间
	  * @param project      项目对象
	  * @return Publish
	  * @exception Exception
	  */
	 public Publish create(long publisherId,int status,Timestamp startDate,Timestamp endDate, Project project) throws Exception;
	 
	 /**
	  * 更新项目发布
	  * @param projectId 项目ID
	  * @param status    项目状态
	  * @param endDate   发布结束日期
	  * @return Publish  
	  * @throws Exception
	  * @throws PublishException
	  */
	 public Publish updatePublish(long projectId, ProjectStatus ps,Timestamp endDate) throws PublishException, Exception;
	 
	 /**
	  * 获取在有效期未被承接的项目列表
	  * @param status    项目状态
	  * @return Page<Publish>
	  * @throws Exception
	  */
	 public Page<Publish> getPagePublishByStatus(int status, Page<Publish> page) throws Exception;
	 
	 /**
	  * 获取在有效期未被承接的项目列表
	  * @return List<Publish>
	  * @throws Exception
	  */
	 public List<Publish> getAllPublishs() throws Exception ;
	 
	 /**
	  * 获取在有效期未被承接的项目
	  * @param status    项目状态
	  * @param projectId 项目ID
	  * @return Publish  
	  * @throws Exception
	  */
	 public Publish getPublishByStatusAndProject(int status,long projectId) throws Exception;
	 
	 /**
	  * 检测发布的项目是否到期
	  * @return List<Publish>
	  * @throws Exception
	  */
	 public List<Publish> checkProjectIsExpire() throws Exception;
	 
	 /**
	  * 根据发布人ID、状态获取发布信息
	  * @param publisherId    发布人ID
	  * @param publishStatus  发布状态
	  * @return List<Publish>
	  * @throws Exception
	  */
	 public List<Publish> getPublishByPublisherIdAndStatus(long publisherId, Integer publishStatus) throws Exception ;
	 
	 /**
	  * 根据发布人ID获取发布信息
	  * @param publisherId      发布人ID
	  * @return List<Publish>
	  * @throws Exception
	  */
	 public List<Publish> getPublishByPublisherId(long publisherId) throws Exception ;
	 
	 /**
	  * 根据审核人ID和审核状态获取发布列表
	  * @param reviewerId
	  * @param applyStatus
	  * @return List<Publish>
	  * @throws Exception
	  */
	 public List<Publish> getPublishByReviewerIdAndApplyStatus(long reviewerId,int applyStatus) throws Exception;


	 /**
	  * 重新提交项目发布
	  * @param projectId 项目ID
	  * @param delay     发布延期天数
	  * @throws Exception
	  */
	 public Publish resubmit(long projectId, int delay) throws Exception;
	 
	 /**
	  * 重新提交项目发布
	  * @param projectId 项目ID
	  * @param ps    修改发布项目状态枚举
	  * @param startDate 项目发布开始时间
	  * @param endDate 项目发布结束时间
	  * @param cycle 项目周期
	  * @throws Exception
	  */
	 public Publish resubmit_v3(long projectId, ProjectStatus ps,String startDate,String endDate,int cycle) throws Exception;
	 
}
