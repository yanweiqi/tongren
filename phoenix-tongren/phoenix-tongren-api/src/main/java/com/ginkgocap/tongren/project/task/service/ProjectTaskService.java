package com.ginkgocap.tongren.project.task.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.model.TaskVO;

public interface ProjectTaskService   extends CommonService<Task>{

	/**
	 * 增加一个任务
	 * @param taks
	 * @return
	 */
	public String addTask(Task task);
	
	/**
	 * 增加一个主任务
	 * @param task
	 * @return
	 */
	public String addPrimaryTask(Task task);
	
	/**
	 * 根据项目id获取主任务
	 * @param projectId
	 * @return
	 */
	public Task getPrimaryTaskByProjectId(long projectId);
	
	/**
	 * 根据项目名称获取该项目的所有任务
	 * @param id
	 * @return
	 */
	public List<Task> getAllTasksByProjectId(long id);
	
	/**
	 * 更新一个任务
	 * @param task
	 * @return
	 */
	public String updateTask(Task task) throws Exception;
	
	/**
	 * 更新任务状态
	 * @param task
	 * @return
	 */
	public String updateStatus(long tid,int status,long userId);
	
	
	/**
	 * 根据taskid删除任务
	 * @param taskId
	 * @return
	 */
	public String delete(long taskId,long userId);
	
	/**
	 *
	 * 任务的详细信息 返回改任务下的一级子任务
	 * @author hanxifa
	 * @param type -1  所有的 0 准备中 1 已开始 2 已完成 
	 */
	public Task getTaskDetail(long taskId,int type) ;
	
	/**
	 * 批量更新任务状态 定时任务调用
	 * 1 当前时间大于开始时间的 任务状态修改为已开始
	 * 2 当前时间大于结束时间的 修改为已过期 ？
	 */
	public void batchUpdateStatus();
	
	
	/**
	 * 查询该用户是否在某个任务下创建了子任务
	 * @param taskId
	 * @param userid
	 * @return
	 */
	public List<Task> getSubTaskByCreateId(long taskId,long userId);
	
	/**
	 * 返回小于指定时间的数据
	 * @param time
	 * @param pageSize
	 * @param params
	 * userId 用户id
	 * type 任务类型 0 全部  1 我发起的任务 2 组织任务 3项目任务  4 被退回的任务 5已完成任务
	 * @return 
	 */
	public List<TaskVO> getMyTaskList(long time,int pageSize,Map<String, String> params);
	
	/**
	 * 获取组织任务列表
	 * @param time
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public List<Task> getOrgTaskList(long time,int pageSize,Map<String, String> params);
	
	/**
	 * 获取组织任务数量
	 * @param orgId
	 * @return
	 */
	public int getOrgTaskCount(long orgId);
	
	/**
	 * 退回任务
	 * @param taskId 任务id
	 * @param userId 操作人id
	 * @param text 退回原因
	 * 
	 */
	public boolean reject(long taskId,long userId,String text) throws Exception; 
	/**
	 * 创建组织任务
	 * task 任务对象
	 * userId 执行者id
	 * return 1：成功   2:任务分配返回为空   3：任务保存返回为空  4:异常 操作错误
	 */
	public String createOrganizationTask(Task task,long userId)throws Exception;

	/**
	 * @param projectId 项目id
	 * @param days 延期的天数 
	 * @return
	 */
	public String delayDays(long projectId,int days);
	
	/**
	 * 个人承接的项目变为组织承接
	 * @param projectId
	 * @param orgId
	 * @return
	 */
	public String convertToOrgTask(long projectId,long orgId);
	
	/**
	 * 获取我在某个组织下完成的任务 包含组织任务 项目任务
	 * @param 
	 * 
	 */
	public List<Task> getMyPerformTask(List<AssignTask> taskList)throws Exception;
	
	/**
	 * 是否允许项目下的任务修改性操作，包括创建子任务，修改 分配等等
	 * @param projectId
	 * @return
	 */
	public boolean isAllowUpdate(long projectId);
	
	/**	
	 * 功能描述 ： 任务转化为事务
	 * @param taskId
	 * 
	 * @return 1 成功   2：任务还未分配  3：任务不存在或任务状态为已完成或过期  4:转化事务失败
	 * */
	 
	public String conversionAffair(long taskId) throws Exception;
	
	/**
	 * 功能描述 修改组织任务状态
	 * @param type  2 完成
	 * @return 1：成功   2：  任务不存在  3:不是任务创建者没有权限修改 
	 * 
	 */
	public String updateOrganizationTaskStatus(long taskId,long userId,int type) throws Exception;
	
	/**
	 * 功能描述 ：任务提醒功能
	 * 
	 */
	public String taskRemind(long taskId)throws Exception;
}
