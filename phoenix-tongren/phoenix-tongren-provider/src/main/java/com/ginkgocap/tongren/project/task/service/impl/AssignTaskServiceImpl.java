package com.ginkgocap.tongren.project.task.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.system.code.TaskCode;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 
 * @author hanxifa
 * 
 */
@Service("assignTaskService")
public class AssignTaskServiceImpl extends AbstractCommonService<AssignTask> implements AssignTaskService {

	private static Logger logger = LoggerFactory.getLogger(AssignTaskServiceImpl.class);

	@Autowired
	private OperationService operationService;
	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private UserService userSerivce;
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;

	/**
	 * @return 0任务创建成功 1任务已经开始,发起者不能再分配 2转发过来的任务只能分配给一个人 3该任务由执行人进行了再此分配，不能分配
	 *         4该任务由执行人创建了子任务不能删除 5转发过来的任务只能在开始状态下才可以分配; 6操作人无权分配 7不存在的任务 8执行失败
	 *         9已存在的执行人
	 */
	@Override
	public String assignTask(AssignTask assignTask, long[] userIds)
			throws Exception {
		try {

			Task task = projectTaskService.getEntityById(assignTask
					.getProjectTaskId());
			if (task != null) {
				// 判断该任务是自己创建的还是别人分配过来的任务，自己创建的情况下，只要任务没有开始即可分配，别人分配过来的任务开始后才能分配，并且只能分配一个人
				if (task.getCreateId() == assignTask.getAssignerId()) {// 分配自己创建的任务
					if (task.getTaskStatus() == 0) {
						saveAssignTaskWithDelExits(
								assignTask.getProjectTaskId(),
								assignTask.getAssignerId(),
								assignTask.getOrganizationId(), userIds);
						return "0";
					} else {
						logger.info("任务已经开始,发起者不能再分配:" + task.getId()
								+ ",status:" + task.getTaskStatus());
						return "1";
					}
				} else {// 分配别人转发过来的任务
					AssignTask at = getAssignTaskByPerformerId(
							assignTask.getProjectTaskId(),
							assignTask.getAssignerId());
					if (at != null) {
						if (task.getTaskStatus() == 1) {// 分配别入转发过来的任务时必须是任务开始状态
							if (userIds.length != 1) {
								logger.info("转发过来的任务只能分配给一个人,taskid:"
										+ task.getId() + ",status:"
										+ task.getTaskStatus() + " uidcount:"
										+ userIds.length);
								return "2";
							} else {
								AssignTask assignTask2 = getAssignTaskByPerformerId(
										assignTask.getProjectTaskId(),
										userIds[0]);
								if (assignTask2 != null) {
									logger.info("执行人已存在：" + assignTask2);
									return "9";
								}
								List<AssignTask> assigned = getAssignTaskByAssignId(
										assignTask.getProjectTaskId(),
										assignTask.getAssignerId());
								if (assigned != null && assigned.size() > 0) {// 如果该任务已经分配给了其他人，需要先删除后分配
									long perid = assigned.get(0)
											.getPerformerId();// 任务分配后的执行人id
									List<AssignTask> assignedSubList = getAssignTaskByAssignId(
											assignTask.getProjectTaskId(),
											perid);
									if (assignedSubList != null
											&& assignedSubList.size() > 0) {// 任务已经进行了转发分配
										logger.info("该任务由执行人进行了再此分配，不能修改执行人"
												+ assignedSubList.get(0));
										return "3";
									}
									// 查询该任务是否有下级创建了子任务
									List<Task> list = projectTaskService
											.getSubTaskByCreateId(assignTask
													.getProjectTaskId(), perid);
									if (list.size() > 0) {
										logger.info("该任务由执行人创建了子任务不能删除 subtask: "
												+ list);
										return "4";
									}
								}
								saveAssignTaskWithDelExits(
										assignTask.getProjectTaskId(),
										assignTask.getAssignerId(),
										assignTask.getOrganizationId(), userIds);
								return "0";
							}
						} else {
							logger.info("转发过来的任务只能在开始状态下才可以分配,taskid:"
									+ task.getId() + ",status:"
									+ task.getTaskStatus());
							return "5";
						}
					} else {
						logger.info("无权分配  taskid:" + task.getId() + ",status:"
								+ task.getTaskStatus() + "assignerid:"
								+ assignTask.getAssignerId());
						return "6";
					}
				}

			} else {
				logger.info("not found task " + assignTask.getProjectTaskId());
				return "7";
			}

		} catch (Exception e) {
			logger.error("createAssignTask error", e);
			return "8";
		}
	}

	/**
	 * 写入任务分配表数据 如果存在改分配人的任务，先删除
	 * 
	 * @param assignId
	 *            分配id
	 * @param taskId
	 *            任务id
	 * @param orgId
	 *            组织id
	 * @param performerIds
	 *            执行人id集合
	 */
	private void saveAssignTaskWithDelExits(long taskId, long assignId,
			long orgId, long[] performerIds) {
		Task task = projectTaskService.getEntityById(taskId);
		if (task == null) {
			logger.warn("没有发现此id对应的任务 " + taskId);
			return;
		}
		List<AssignTask> assignedList = getAssignTaskByAssignId(task.getId(),
				assignId);
		if (assignedList != null && assignedList.size() > 0) {
			for (AssignTask ast : assignedList) {
				logger.info("delete AssignTask:" + ast);
				deleteEntityById(ast.getId());
			}
		}
		if (performerIds == null || performerIds.length == 0) {
			logger.info("执行人id为空");
			return;
		}
		for (Long performId : performerIds) {
			AssignTask at = new AssignTask();
			at.setAssignerId(assignId);
			at.setAssignTime(new Timestamp(System.currentTimeMillis()));
			at.setOrganizationId(orgId);
			at.setProjectTaskId(task.getId());
			at.setPerformerId(performId);
			save(at);

			try {
				boolean sendStatus = tongRenSendMessageService.sendOrganizationalTasksMes(assignId, performId, orgId, 10, task.getTitle());
				logger.info("send assgin task message is " + sendStatus + ",taskid:" + taskId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("send assgin task message failed,taskid:" + taskId, e);
			}
		}

		// 删除之前的分配任务操作
		try {
			List<Operation> listOpertions = operationService
					.getOperactionByTaskId(task.getId());
			for (Operation operation : listOpertions) {
				if (operation.getOperationCode().equals(
						TaskCode.ALLOT.getCode())) {
					operationService.deleteEntityById(operation.getId());
				}
			}
		} catch (Exception e) {
			logger.error(" getOperactionByTaskId  failed! " + task.getId(), e);
		}
		// 保存操作记录
		Operation o = new Operation();
		o.setProjectId(task.getProjectUndertakenId()); // 项目id
		o.setOperationCode(TaskCode.ALLOT.getCode());// 操作类型
		o.setRemark(String.valueOf(task.getId()));// 主任务id
		o.setOperationUid(assignId);
		o.setOperactionTime(new Timestamp(System.currentTimeMillis()));
		try {
			operationService.createOperation(o);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save Operation failed! " + task, e);
		}
	}

	/**
	 * 根据任务id和执行id查询分配数据
	 * 
	 * @param taskId
	 * @param performerId
	 * @return
	 */
	public AssignTask getAssignTaskByPerformerId(long taskId, long performerId) {
		String sql = "assignTask_list_projectTaskId_performerId";
		List<Long> ids = getKeysByParams(sql, taskId, performerId);
		if (ids != null && ids.size() > 0) {
			return getEntityById(ids.get(0));
		} else {
			return null;
		}
	}

	/**
	 * 根据任务id 和 分配任id 查询分配数据
	 * 
	 * @param taskId
	 * @param performerId
	 * @return
	 */
	public List<AssignTask> getAssignTaskByAssignId(long taskId, long assignerId) {
		if(assignerId==0){
			return selectAssignTaskByTaskId(taskId);
		}
		String sql = "assignTask_list_projectTaskId_assignerId";
		List<Long> ids = getKeysByParams(sql, taskId, assignerId);
		//id倒序排序=时间倒叙排序
		Collections.sort(ids, new Comparator<Long>(){
			@Override
			public int compare(Long o1, Long o2) {
				return o2.compareTo(o1);
			}});
		if (ids != null && ids.size() > 0) {
			List<AssignTask> rslist = new ArrayList<AssignTask>(ids.size());
			for (Long id : ids) {
				AssignTask assignTask=getEntityById(id);
				if(assignTask.getStatus()==0){//有效地任务
					rslist.add(assignTask);
				}
			}
			Collections.sort(rslist,new Comparator<AssignTask>(){
				@Override
				public int compare(AssignTask o1, AssignTask o2) {
					return o2.getAssignTime().compareTo(o1.getAssignTime());
				}});
			return rslist;
		} else {
			return null;
		}
	}

	/**
	 * 分配或者重新分配组织任务 多次分配后分配表中保存多条记录
	 */
	@Override
	public String createOrganizationAssignTask(AssignTask assignTask,String startTime,String endTime,String attachId)throws Exception {

				AssignTask at = null;
				// 根据任务id查询任务
				Task task = projectTaskService.getEntityById(assignTask.getProjectTaskId());
				if (task != null) {
					// 如果状态不是被退回 返回任务已经被分配 不能操作
						if (task.getTaskStatus() != 6) {
							return  "2";
						}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");	
					if(StringUtils.isNotBlank(startTime)){
						task.setEndTime(new Timestamp(df.parse(endTime).getTime()));
					}	
					if(StringUtils.isNotBlank(endTime)){
						task.setStartTime(new Timestamp(df.parse(startTime).getTime()));
					}
					if(StringUtils.isNotBlank(attachId)){
						task.setAttachId(attachId);
					}
					task.setTaskStatus(1);
					boolean status = projectTaskService.update(task);
					if (status) {
						// 保存任务分配
						at = save(assignTask);
						if (at != null) {
							// 保存操作记录
							Operation o = new Operation();
							o.setOperationCode(TaskCode.RETRY.getCode());// 操作类型
							o.setOrganizationTaskId(assignTask.getProjectTaskId());// 组织任务id
							o.setOperationUid(assignTask.getAssignerId());// 分配人id
							o.setOperactionTime(new Timestamp(System.currentTimeMillis()));
							operationService.createOperation(o);
						}else{
							return "3";
						}
					} 
				}else{
					return "4";
				}
				return "1_"+at.getProjectTaskId();
	}

	@Override
	public List<AssignTask> selectAssignTaskByTaskId(long taskId) {

		List<AssignTask> list = new ArrayList<AssignTask>();
		List<Long> ids = getKeysByParams("assignTask_list_projectTaskId",taskId);
		if (ids != null && !ids.isEmpty()) {
			list = getEntityByIds(ids);
		}
		Collections.sort(list,new Comparator<AssignTask>(){
			@Override
			public int compare(AssignTask a1, AssignTask a2) {
				return a2.getAssignTime().compareTo(a1.getAssignTime());
			}});
		return list;
	}

	@Override
	protected Class<AssignTask> getEntity() {
		return AssignTask.class;
	}

	@Override
	public Map<String, Object> doWork() {
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		return null;
	}

	@Override
	public String doError() {
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		return null;
	}

	@Override
	public StringBuilder getPerformerName(long taskId) {

		StringBuilder sb = new StringBuilder();
		List<AssignTask> atList = selectAssignTaskByTaskId(taskId);
		if (!atList.isEmpty()) {
			for (AssignTask at : atList) {
				User user = userSerivce.selectByPrimaryKey(at.getPerformerId());
				if (user != null) {
					sb.append(user.getName()).append(" ");
				}
			}
		}
		return sb;
	}

	@Override
	public String getRejectReasonByTaskId(long taskId) {
		List<AssignTask> list=selectAssignTaskByTaskId(taskId);
		if(list!=null&&list.size()>0){
			return list.get(0).getRemark();
		}else{
			return null;
		}
	}

	@Override
	public List<AssignTask> getAssignOrgnazationTask(long organizationId, long performerId)throws Exception {
		
		List<Long> ids  = null;
		List<AssignTask> assignList = new ArrayList<AssignTask>();
		logger.info("getAssignOrgnazationTask begin organizationId-->"+organizationId,"performerId-->"+performerId);
	try{	
		
		ids = getKeysByParams("assignTask_list_organizationId_performerId", organizationId,performerId);
		if(ids != null){
			for(long id :ids){
				AssignTask at = getEntityById(id);
				if(at == null){
					logger.info("not found assignTask by id-->"+id);
					continue;
				}
				//根据taskId返回任务实体
				Task  task = projectTaskService.getEntityById(at.getProjectTaskId());
				if(task != null){
					if(task.getTaskType() == 1){  //过滤掉组织任务
						continue;
					}
				}
				assignList.add(at);
			}
		}
	}catch(Exception e){
		e.printStackTrace();
		logger.info("getAssignOrgnazationTask Exception",e.getMessage());
	}
		return assignList;
	}
}
