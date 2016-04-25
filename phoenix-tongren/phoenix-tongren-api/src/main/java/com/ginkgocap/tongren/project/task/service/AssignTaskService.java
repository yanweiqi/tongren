package com.ginkgocap.tongren.project.task.service;

import java.util.List;
import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.task.model.AssignTask;
/**
 * 项目任务分配Service
 * @author liweichao
 *
 */
public interface AssignTaskService extends CommonService<AssignTask>{
	
	 /**
	  * 功能描述 ：保存分配任务
	  * 
	  */
	public String assignTask(AssignTask assignTask, long[] userIds)throws Exception;
	 /**
	  * 功能描述 ：分配组织任务
	  * @param assignTask 分配任务参数类
	  * @param startTime 任务开始时间  endTime 任务结束时间
	  * @return 1_id 增加成功  2:已经分配过任务并且任务状态不是已驳回 3:保存任务分配为空  4：组织任务为空
	  */
	public String createOrganizationAssignTask(AssignTask assignTask,String startTime,String endTime,String attachId)throws Exception;
	
	
	/**
	 * 功能描述 ：根据项目任务id查询分配集合
	 * @author liweichao
	 * @param taskId 项目任务id
	 * @return 任务分配集合
	 */
	public List<AssignTask> selectAssignTaskByTaskId(long taskId)throws Exception;
	
	
	/**
	 * 功能描述  ：根据任务id查询操作对象名称
	 * 
	 */
	public StringBuilder getPerformerName(long taskId)throws Exception;
	
	/**
	 * 根据任务id 和 分配人id 查询分配数据
	 * @param taskId
	 * @param performerId
	 * @return
	 */
	public List<AssignTask> getAssignTaskByAssignId(long taskId,long assignerId);
	
	/**
	 * 根据任务id和执行id查询分配数据
	 * @param taskId
	 * @param performerId
	 * @return
	 */
	public AssignTask getAssignTaskByPerformerId(long taskId,long performerId);
	
	/**
	 * 获取组织任务的退回原因
	 * @param taskId
	 * @return
	 */
	public String getRejectReasonByTaskId(long taskId);
	
	/**
	 * 获取我在某个组织下分配给我的任务（有效) 项目任务
	 * @param organizationId 组织Id
	 * @param performerId  执行人Id
	 */
	public List<AssignTask> getAssignOrgnazationTask(long organizationId,long performerId)throws Exception;
	
}
