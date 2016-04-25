package com.ginkgocap.tongren.project.task.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.tree.Node;
import com.ginkgocap.tongren.common.tree.NodeProvider;
import com.ginkgocap.tongren.common.util.DaoSortType;
import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;
import com.ginkgocap.tongren.project.system.code.TaskCode;
import com.ginkgocap.tongren.project.task.model.AssignTask;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.model.TaskVO;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.tongren.resources.service.ResourcesService;
import com.ginkgocap.ywxt.affair.service.AffairService;
import com.ginkgocap.ywxt.affair.vo.AffairDetailVO;
import com.ginkgocap.ywxt.affair.vo.AffairMemberVO;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 
 * @author hanxifa
 */
@Service("projectTaskService")
public class ProjectTaskServiceImpl  extends AbstractCommonService<Task> implements  ProjectTaskService,NodeProvider{

	@Autowired
	private OperationService operationService;
	
	@Autowired
	private AssignTaskService  assignTaskService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UndertakenService undertakenService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired
	private ResourcesService resourcesService;
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;
	@Resource
	private AffairService affairService;
	
	
	protected static final Logger logger = LoggerFactory.getLogger(ProjectTaskServiceImpl.class);
	
	private static long MIN_DATE_PARTITION=1448899200000l;
	/**
	 * @return 1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 项目不存在主任务 5 此任务对应的项目处于不可编辑的状态
	 */
	@Override
	public String addTask(Task task) {
		logger.info("add task "+task.toString());
		long pid=task.getTaskPid();
		if(pid==0){//增加主任务的情况
			Task ptask=getPrimaryTaskByProjectId(task.getProjectUndertakenId());
			if(isAllowUpdate(task.getProjectUndertakenId())==false){
				return "5";
			}
			if(ptask==null){
				Task t=save(task);
				return "1_"+t.getId();
			}else{//已经存在主任务
				return "2";
			}
		}else{//增加子任务情况
			Task ptask=getEntityById(task.getTaskPid());
			if(ptask!=null){
				if(isAllowUpdate(ptask.getProjectUndertakenId())==false){
					return "5";
				}
				task.setTaskPid(ptask.getId());//设置父id
				task.setProjectUndertakenId(ptask.getProjectUndertakenId());
				if(task.getOrganizationId()!=ptask.getOrganizationId()){
					return "3";
				}
				Task t=save(task);
				asynAddOperation(TaskCode.CHILD,t.getCreateId(),t.getProjectUndertakenId(),t.getId()+"");
				return "1_"+t.getId();
			}else{
				return "4";
			}
		}
	}
	@Override
	public String addPrimaryTask(Task task) {
		logger.info("addPrimaryTask "+task.toString());
		if(task.getProjectUndertakenId()==0){
			logger.info("项目 id为空");
			return "5";
		}
		if(task.getTitle()==null){
			logger.info("项目名称为空");
			return "5";
		}
		if(task.getCycle()<=0){
			logger.info("任务周期必须大于0 ，不能是"+task.getCycle());
			return "5";
		}
		if(task.getCreateId()==0){
			logger.info("创建人为空");
			return "5";
		}
		task.setCreateTime(new Timestamp(System.currentTimeMillis()));
		if(task.getStartTime()==null){
			task.setStartTime(task.getCreateTime());
		}
		task.setStartTime(new Timestamp(DateUtil.trunck(task.getStartTime()).getTime()));
		task.setEndTime(new Timestamp(DateUtil.addDay(task.getStartTime(), task.getCycle()).getTime()));
		task.setTaskPid(0);
		return addTask(task);
	}
	/**
	 * 根据项目id获取主任务
	 * @param projectId
	 * @return
	 */
	public Task getPrimaryTaskByProjectId(long projectId){
		logger.info("主任务："+projectId);
		List<Task> list=getAllTasksByProjectId(projectId);
		if(list!=null){
			for(Task t:list){
				if(t.getPid()==0){
					return getTaskDetail(t.getId(), -1);
				}
			}
		}
		return null;
	}
	/**
	 * 1 更新成功 2 不存在的任务 3 修改人与创建人不一致 4 任务的开始时间不在项目周期内 5 任务的结束时间不在项目周期内 6 任务已经开始不能修改 7 设置任务时间之前必须先分配任务执行人 8 此任务对应的项目处于不可编辑的状态
	 * @throws Exception 
	 */
	@Override
	public String updateTask(Task task) throws Exception {
		logger.info("update Task "+task.toString());
		Task t=getEntityById(task.getId());
		if(t!=null){
			if(isAllowUpdate(t.getProjectUndertakenId())==false){
				return "8";
			}
			if(t.getTaskStatus()!=0){
				return "6";
			}
			if(t.getCreateId()!=task.getCreateId()){
				return "3";
			}
			Task ptask=getPrimaryTaskByProjectId(t.getProjectUndertakenId());
			if(task.getTitle()!=null)
			{
				t.setTitle(task.getTitle());
			}
			
			if(task.getTaskDescription()!=null)
			{
				t.setTaskDescription(task.getTaskDescription());
			}
			
			t.setTaskStatus(task.getTaskStatus());
			
			if(task.getStartTime()!=null)
			{
				//设置任务时间之前必须先分配任务执行人
				List<AssignTask> list=assignTaskService.selectAssignTaskByTaskId(task.getId());
				if(list==null||list.size()==0){
					return "7";
				}
				if(task.getStartTime().compareTo(ptask.getStartTime())<0||task.getStartTime().compareTo(ptask.getEndTime())>0){
					return "4";
				}
				t.setStartTime(task.getStartTime());
			}
			
			if(task.getEndTime()!=null)
			{
				if(task.getEndTime().compareTo(ptask.getStartTime())<0||task.getEndTime().compareTo(ptask.getEndTime())>0){
					return "5";
				}
				t.setEndTime(task.getEndTime());
			}
			if(task.getProgress()>0){
				t.setProgress(task.getProgress());
			}
			update(t);
			return "1";
		}else{
			return "2";
		}
	}

	/**
	 * 根据项目id获取该项目下所有的任务
	 */
	@Override
	public List<Task> getAllTasksByProjectId(long projectId) {
		logger.info("获取该项目下所有的任务 "+projectId);
		List<Long> ids= this.getKeysByParams("task_list_projectUndertakenId",DaoSortType.DESC, projectId);
		if(ids!=null&&ids.size()>=0){
			 List<Task> tasks=new ArrayList<Task>();
			 for(Long id:ids){
				 tasks.add(getEntityById(id));
			 }
			 logger.info("获取该项目下所有的任务 "+projectId+",任务数量:"+tasks.size());
			 return tasks;
		}
		return null;
	}
	
	/**
	 *
	 * 任务的详细信息 返回改任务下的一级子任务
	 * @author hanxifa
	 * @param type -1  所有的 0 准备中 1 已开始 2 已完成 
	 */
	public Task getTaskDetail(long taskId,int type) {
		logger.info("getTaskDetail "+taskId+",type:"+type);
		Task pt=getEntityById(taskId);
		if(pt!=null){
			 List<Node> subTasks= getChildNodesById(pt.getId()); 
			 pt.clearChildren();
			 if(subTasks!=null){
				 for(Node node:subTasks){
					 Task st=(Task) node;
					 updateTaskStatus(st);//弥补定是更新任务状态不及时的缺陷，当查询时更新任务状态
					 if(type==-1){
						 pt.addChild(node);
					 }else if(type==0||type==1||type==2||type==3){
						 if(st.getTaskStatus()==type){
							 pt.addChild(node);
						 }
					 }else{
						 logger.warn("type is invalied! "+type);
					 }
					
				 }
			 }
			 return pt;
		}else{
			return null;
		}
		
	}

	/**
	 * 查询该用户是否在某个任务下创建了子任务
	 * @param taskId
	 * @param userid
	 * @return
	 */
	public List<Task> getSubTaskByCreateId(long taskId,long userId){
		Task taskdetial=getTaskDetail(taskId,-1);
		List<Task> list=new ArrayList<Task>();
		if(taskdetial.getChildren().size()>0){
			for(Node subt:taskdetial.getChildren()){
				if(userId==((Task)subt).getCreateId()){
					list.add((Task)subt);
				}
			}
		}
		return list;
	}
	/**
	 * @param tid 任务的id
	 * @param status '0 准备中 1 已开始 2 已完成 3 已过期',
	 * @return 1 更新成功 2 不存在任务 3 状态码不正确  4没有权限 5此任务对应的项目处于不可编辑的状态
	 */
	@Override
	public String updateStatus(long tid,int status,long userId) {
		logger.info("更新任务状态  taskid:"+tid+",userid:"+userId+",status:"+status);
		if(status==1||status==2||status==3){
			Task t=getEntityById(tid);
			if(t!=null){
				if(isAllowUpdate(t.getProjectUndertakenId())==false){
					return "5";
				}
				if(t.getCreateId()!=userId){
					return "4";
				}
				t.setTaskStatus(status);
				update(t);
				if(status==2){//完成操作需要记录操作表
					asynAddOperation(TaskCode.FINISH,t.getCreateId(),t.getProjectUndertakenId(),t.getId()+"");
				}
				return "1";
			}else{
				return "2";
			}
		}else{
			return "3";
		}
	}

	/**
	 * 根据id删除任务
	 * 1 删除成功 2 任务不存在 3 任务已经开始不能删除 4没有权限 5 此任务对应的项目处于不可编辑的状态
	 */
	@Override
	public String delete(long taskId,long userId) {
		logger.info("删除任务 taskid:"+taskId+",userid:"+userId);
		List<Operation> list = new ArrayList<Operation>();
		List<AssignTask> atList = new ArrayList<AssignTask>();
		Task t=getEntityById(taskId);
		if(t!=null){
			if(isAllowUpdate(t.getProjectUndertakenId())==false){
				return "5";
			}
			if(t.getCreateId()!=userId){
				return "4";
			}
			if(t.getTaskStatus()==0){
				try {
					 atList = assignTaskService.selectAssignTaskByTaskId(taskId);
					if(t.getTaskType() == 0){
						 list = operationService.getOperactionByTaskId(taskId);
					}else{
						 list = operationService.getOpertionByOrgTaskId(taskId);
					}
					//删除操作分配人
					if(!atList.isEmpty()){
						for(AssignTask at :atList){
							assignTaskService.deleteEntityById(at.getId());
						}
					}
					//删除任务操作记录
					if(!list.isEmpty()){
						for(Operation o:list){
							operationService.deleteEntityById(o.getId());
						}
					}
//					t.setTaskStatus(9);//9 已删除
//					this.update(t);
					this.deleteEntityById(taskId);
				} catch (Exception e) {
					logger.error("delete AssigTask failed! "+taskId,e);
				}
				return "1";
			}else{
				return "3";
			}
		}else{
			return "2";
		}
	}
	
	/**
	 * 批量更新任务状态 定时任务调用
	 * 1 当前时间大于开始时间的 任务状态修改为已开始
	 * 2 当前时间大于结束时间的 修改为已过期 ？
	 */
	@Override
	public void batchUpdateStatus() {
		batchUpdateStatusByType(0);//更新 项目任务
		batchUpdateStatusByType(1);//批量更新组织任务
	}
	
	private void batchUpdateStatusByType(int type){
		long t1=System.currentTimeMillis();
		List<Long> ids=getKeysByParams("task_list_taskType", type);
		//0 准备中 1 已开始 2 已完成 3 已过期
		for(Long id:ids){
			Task task=getEntityById(id);
			updateTaskStatus(task);
		}
		long t2=System.currentTimeMillis()-t1;
		logger.info("batchUpdateStatusByType type:"+type+",has cost "+t2+" ms");
	}
	/**
	 * 更新任务状态为已开始或者为过期 
	 * @param task
	 */
	private synchronized void updateTaskStatus(Task task){
		if(task.getTaskStatus()==0&&task.getTaskType()==0){//任务状态更新为已开始
			if(task.getStartTime()!=null&&task.getStartTime().getTime()<=System.currentTimeMillis()){
				logger.info("更新为已完成,taskId "+task.getId());
				task.setTaskStatus(1);
				update(task);
				asynAddOperation(TaskCode.START,task.getCreateId(),task.getProjectUndertakenId(),task.getId()+"");
			}
		}else if(task.getTaskStatus()==1){
			if(task.getEndTime()!=null&&task.getEndTime().getTime()<System.currentTimeMillis()){
				logger.info("更新为已过期 ,taskId "+task.getId());
				task.setTaskStatus(3);
				update(task);
			}
		}
	}
	/**
	 * 返回小于指定时间的数据
	 * @param time
	 * @param pageSize
	 * @param params
	 * userId 用户id
	 * type 任务类型 0 全部  1 我发起的任务 2 组织任务 3项目任务  4 被退回的任务 5已完成的任务
	 * @return 
	 */
	public List<TaskVO> getMyTaskList(long time,int pageSize,Map<String, String> params)
	{
		logger.info("begin getMyTaskList");
		long t1=System.currentTimeMillis();
		if(pageSize<=0||pageSize>100){
			pageSize=100;
		}
		long userId = Long.parseLong(params.get("userId"));
		int type = Integer.parseInt(params.get("type"));
		long orgId=Long.parseLong(params.get("organizationId"));
		List<TaskVO> rsListVO = new ArrayList<TaskVO>();
		Calendar queryDate=Calendar.getInstance();
		queryDate.setTimeInMillis(time);
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMM");
		
		while (rsListVO.size() != pageSize&&queryDate.getTimeInMillis()>=MIN_DATE_PARTITION) {
			List<Long> ids = getKeysByParams("task_list_createTime",DaoSortType.DESC, new Object[] {sf.format(queryDate.getTime())});
			for (Long id : ids) {
				int rtype=-1;//0 我创建的 1我分配的 2 分配给我的 
				Task t = getEntityById(id);
				if(t==null){
					logger.warn("not found task info in cache by id "+id);
					continue;
				}
				if(t.getCreateTime().getTime()>=time||t.getTaskStatus()==9||t.getOrganizationId()!=orgId){//删除状态的或者不是这个组织下的任务 直接 跳过
					continue;
				}
				if(t.getTaskType()==0&&t.getTaskPid()==0){//项目任务，主任务不在我的任务列表显示
					continue;
				}
				Task target = null;
				long assignId=userId;
				if (t.getCreateId() == userId) {
					target = t;
					rtype=0;
				} else {
					// 分配的任务
					List<AssignTask> asList = assignTaskService.getAssignTaskByAssignId(t.getId(), userId);
					if (asList != null && asList.size() > 0) {
						if(t.getTaskType()==0){//分配的项目任务，分配人看不到，被分配的人才能看到
							continue;
						}
						target = t;
						rtype=1;
					} 
					// 被分配的任务 （分配人和被分配人，可能为同一个人）
					AssignTask performer = assignTaskService.getAssignTaskByPerformerId(t.getId(), userId);
					if (performer != null && performer.getStatus() == 0 && (t.getTaskStatus() != 6 && t.getTaskStatus() != 9)) {
						assignId=performer.getAssignerId();
						target = t;
						rtype=2;
					}
					

				}
				
				if (target != null) {// 满足基本的筛选条件，我创建的，我分配的，分配给我的
					if (type == 0) {
						rsListVO.add(convertToVo(target,rtype,assignId));
					} else if (type == 1) {
						if (rtype==0||rtype==1) {//我创建的或者我分配的任务
							logger.info(target.getId()+",rtype:"+rtype);
							rsListVO.add(convertToVo(target,rtype,assignId));
						}

					} else if (type == 2) {
						if (target.getTaskType() == 1) {
							rsListVO.add(convertToVo(target,rtype,assignId));
						}
					} else if (type == 3) {
						if (target.getTaskType() == 0) {
							rsListVO.add(convertToVo(target,rtype,assignId));
						}
					} else if (type == 4) {
						if (target.getTaskType() == 1 && target.getTaskStatus() == 6) {// 只有组织任务，才有被退回状态
							rsListVO.add(convertToVo(target,rtype,assignId));
						}
					}else if (type==5){
						if (target.getTaskStatus() == 2) {// 已完成的任务
							rsListVO.add(convertToVo(target,rtype,assignId));
						}
					}
				}
				if (rsListVO.size() == pageSize) {
					break;
				}
			}
			if(rsListVO.size()!=pageSize){
				queryDate.add(Calendar.MONTH, -1);
			}
		}
		logger.info("查询组织下的任务,time:"+time+",pageSize:"+pageSize+",params:"+params+",resultCount:"+rsListVO.size());
		logger.info("query mytaks list is "+(System.currentTimeMillis()-t1));
		return rsListVO;
	}
	
	
	/**
	 * 
	 * @param task
	 * @param rtype 0 我创建的 1我分配的 2 分配给我的 
	 * @return
	 */
	private TaskVO convertToVo(Task task,int rtype,long userId){
		
		TaskVO tv=new TaskVO();
		tv.setTask(task);
		long tp=System.currentTimeMillis()-task.getLastRemindTime();
		tv.setRequreRemind(tp>8*60*60*1000);
		tv.setRtype(rtype);
		User user= userService.selectByPrimaryKey(userId);
		if(user!=null){
			tv.setCreateUserName(user.getName());
		}
		try {
			Organization org = tongRenOrganizationService.getOrganizationById(task.getOrganizationId());
			if(org!=null){
				tv.setOrgName(org.getName());
			}else{
				logger.info("没有发现这个组织 id:"+task.getOrganizationId());
			}
		} catch (Exception e) {
				logger.error("查询组织信息失败！id："+task.getOrganizationId(),e);
		}
	
		List<AssignTask> asList =assignTaskService.getAssignTaskByAssignId(task.getId(), userId);
		List<Map<String, String>> userInfo=new ArrayList<Map<String,String>>(5);
		if(asList!=null&&asList.size()>0){
			StringBuilder sb=new StringBuilder();
			for(AssignTask at:asList){
				user= userService.selectByPrimaryKey(at.getPerformerId());
				if(user!=null){
					Map<String, String> umap=new HashMap<String, String>(2);
					umap.put("name", user.getName());
					umap.put("id", user.getId()+"");
					sb.append(user.getName()).append(",");
					userInfo.add(umap);
				}
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
				tv.setPerformUserName(sb.toString());
			}
		}
		tv.getTask().addExtend("assignInfo", userInfo);
		if(task.getTaskStatus()==6){
			tv.setRejectStr(assignTaskService.getRejectReasonByTaskId(task.getId()));//拒绝原因
		}
		//项目名称
		if(task.getTaskType()==0){
			Project project= projectService.getEntityById(task.getProjectUndertakenId());
			if(project!=null){
				tv.getTask().addExtend("projectName",project.getName());
			}else{
				logger.warn("not found project info by id "+task.getProjectUndertakenId());
			}
		}
		
		return tv;
		
	}
	
	/**
	 * 获取组织下的任务列表
	 *  type 任务类型 -1 全部 ,0 准备中,1已开始  ,2已完成 3 已驳回 
	 */
	@Override
	public List<Task> getOrgTaskList(long time, int pageSize, Map<String, String> params) {
		if (pageSize <= 0 || pageSize > 100) {// 组织任务前台没有分页，设置为200条
			pageSize = 100;
		}
		long orgId = Long.parseLong(params.get("orgId"));
		int type = -1;
		try {
			type = Integer.parseInt(params.get("type"));
		} catch (Exception e) {
		}

		List<Task> rsList = new ArrayList<Task>();
		Calendar queryDate = Calendar.getInstance();
		queryDate.setTimeInMillis(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		while (rsList.size() != pageSize && queryDate.getTimeInMillis() >= MIN_DATE_PARTITION) {
			List<Long> ids = getKeysByParams("task_list_organizationId_createTime", DaoSortType.DESC,
					new Object[] { new Long(orgId), 1, sf.format(queryDate.getTime()) });
			for (Long id : ids) {
				Task task = getEntityById(id);
				if (task == null) {
					logger.warn("not found task by id " + id);
					continue;
				}
				if (task.getCreateTime().getTime() >= time || task.getTaskStatus() == 9) {// 删除状态的直接跳过
					continue;
				}
				if (type == -1) {
					rsList.add(task);
				} else if (type == task.getTaskStatus()) {// 0 准备中,1准备中 ,2已完成
															// 这几种查询条件与状态一致
					rsList.add(task);
				} else if (type == 3) {
					if (task.getTaskStatus() == 6) {
						rsList.add(task);
					}
				}
				if (rsList.size() == pageSize) {
					break;
				}
			}

			if (rsList.size() != pageSize) {
				queryDate.add(Calendar.MONTH, -1);
			}
		}
		// 查询任务分配执行人信息
		long t1 = System.currentTimeMillis();
		for (Task task : rsList) {
			List<Map<String, String>> userInfo = new ArrayList<Map<String, String>>(5);
			try {
				List<AssignTask> assignList = assignTaskService.selectAssignTaskByTaskId(task.getId());
				for (AssignTask at : assignList) {
					Map<String, String> umap = new HashMap<String, String>(2);
					User user = userService.selectByPrimaryKey(at.getPerformerId());
					if (user != null) {
						umap.put("name", user.getName());
						umap.put("id", user.getId() + "");
						userInfo.add(umap);
					}
				}
				task.addExtend("assignInfo", userInfo);
			} catch (Exception e) {
				logger.error("query assign task failed! " + task.getId(), e);
			}
		}
		logger.info("query users info cost: " + (System.currentTimeMillis() - t1) + " ms");
		logger.info("查询组织下的任务,time:" + time + ",pageSize:" + pageSize + ",params:" + params + ",resultCount:" + rsList.size());
		return rsList;
	}
	
	@Override
	public Node getNodeById(long id) {
	
		return getEntityById(id);
	}

	@Override
	public List<Node> getChildNodesById(long id) {
		List<Long> ids=getKeysByParams("task_list_taskPid", DaoSortType.ASC,id);
		if(ids!=null&&ids.size()>0)
		{
			List<Node> list=new ArrayList<Node>();
			for(Long tid:ids){
				list.add(getEntityById(tid));
			}
			return list;
		}
		return null;
	}

	/**
	 * 异步写入操作表
	 * @param tc
	 * @param userId
	 * @param projectId
	 * @param remark
	 */
	private void asynAddOperation(TaskCode tc,long userId,final long projectId,String remark){
		final Operation operation =new Operation();
		operation.setOperactionTime(new Timestamp(System.currentTimeMillis()));
		operation.setOperationCode(tc.getCode());
		operation.setOperationUid(userId);
		operation.setProjectId(projectId);
		operation.setRemark(remark);
		try {
			logger.info("end add operation projectId:"+projectId+",code:"+tc.getCode());
			String status =operationService.createOperation(operation);
			logger.info("end add operation projectId:"+projectId+",code:"+tc.getCode()+",status"+status);
		} catch (Exception e) {
			logger.error("add operation failed"+projectId,e);
		}
		
	}
	@Override
	protected Class<Task> getEntity() {
		return Task.class;
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
	public boolean reject(long taskId,long userId,String text) throws Exception {
			logger.info("reject taskid:"+taskId+",userid:"+userId,"text:"+text);
			boolean flag = false;
		try{
			Task   task =  getEntityById(taskId);
			if(task != null){
				task.setTaskStatus(6);
				flag = update(task);
				AssignTask at = assignTaskService.selectAssignTaskByTaskId(task.getId()).get(0);
				if(at != null){
					at.setStatus(1);
					at.setRemark(text);
					assignTaskService.update(at);
				}
				if(flag){
					Operation o = new Operation();
					o.setOperationCode(TaskCode.EXIT_TASK.getCode());// 操作类型
					o.setOrganizationTaskId(taskId);// 项目任务id
					o.setOperationUid(userId);//操作人id
					o.setOperactionTime(new Timestamp(System.currentTimeMillis()));
					operationService.createOperation(o);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 创建组织任务
	 */
	@Override
	public String createOrganizationTask(Task task,long userId) throws Exception {
			
				Task t = null;
		try{
				t = save(task);
			if(t != null){
				AssignTask assignTask = new AssignTask();
				assignTask.setAssignerId(t.getCreateId());
				assignTask.setPerformerId(userId);//执行者id
				assignTask.setProjectTaskId(t.getId());
				assignTask.setOrganizationId(t.getOrganizationId());
				assignTask.setStatus(0);//0:有效   1:无效
				assignTask.setAssignTime(new Timestamp(System.currentTimeMillis()));
				AssignTask at = assignTaskService.save(assignTask);
				LocalObject localObject=new LocalObject();
				try {
					boolean sendStatus = tongRenSendMessageService.sendOrganizationalTasksMes(t.getCreateId(), userId, t.getOrganizationId(), 10, task.getTitle());
					logger.info("send assgin task message is " + sendStatus + ",taskid:" + t.getId());
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("send assgin task message failed,taskid:" + t.getId(), e);
				}
				
				if(task.getAttachId()!=null&&task.getAttachId().trim().length()>0){
					try{
						localObject.setCreateId(task.getCreateId());
						localObject.setCreateTime(new Timestamp(System.currentTimeMillis()));
						localObject.setOrganizationId(t.getOrganizationId());
						localObject.setTaskId(task.getAttachId());
						resourcesService.addResourcesLocalAndOrg(localObject);
						logger.info("save resource succcess ! "+task);
					}catch(Exception e){
						logger.error("save resource failed! "+task,e);
					}
				}else{
						logger.info("attachId is null ,save has been cancel!");
				}
				
				if(at != null){
					Operation o = new Operation();
					o.setOperationCode(TaskCode.ALLOT.getCode());// 操作类型
					o.setOrganizationTaskId(assignTask.getProjectTaskId());// 组织任务id
					o.setOperationUid(assignTask.getAssignerId());//分配人id
					o.setOperactionTime(new Timestamp(System.currentTimeMillis()));
					operationService.createOperation(o);
					
				}else{
					return "2";
				}
			}else{
				return "3";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "4";
		}
		return "1_"+t.getId();
	}
	
	/**
	 * 项目延期
	 * @return 1 延期成功 2 主任务不存在 
	 */
	@Override
	public String delayDays(long projectId, int days) {
		Task task=getPrimaryTaskByProjectId(projectId);
		if(task!=null){
			task.setCycle(task.getCycle()+days);
			task.setEndTime(new Timestamp(DateUtil.addDay(task.getStartTime(), task.getCycle()).getTime()));
			update(task);
			return "1";
		}else{
			return "2";
		}
	}

	/**
	 * 项目任务转化为组织任务
	 * @return 1 转化成功 2 住任务不存在
	 */
	@Override
	public String convertToOrgTask(long projectId, long orgId) {
		Task task=getPrimaryTaskByProjectId(projectId);
		if(task!=null){
			task.setOrganizationId(orgId);
			update(task);
			return "1";
		}else{
			return "2";
		}
	}
	
	/**
	 * 是否允许项目下的任务修改性操作，包括创建子任务，修改 分配等等
	 * @param projectId
	 * @return
	 */
	public boolean isAllowUpdate(long projectId){
		Undertaken undertaken;
		try {
			undertaken = undertakenService.getUndertakenByProjectId(projectId);
			if(undertaken!=null&&undertaken.getStatus()==0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error("查询项目承接信息失败 "+projectId,e);
			return false;
		}
		
	}

	@Override
	public List<Task> getMyPerformTask(List<AssignTask> taskList)throws Exception {
		
		List<Task> result  = new ArrayList<Task>();
		
		for(AssignTask at :taskList){
			
			Task  task = getEntityById(at.getProjectTaskId());
			
			if(task == null){
				logger.info("getMyPerformTask taks is null");
				continue;
			}
			if(task.getTaskStatus() != 2){
				continue;
			}
			result.add(task);
		}
		return result;
	}

	@Override
	public int getOrgTaskCount(long orgId) {
		if(orgId>0){
			return count("task_list_organizationId_count", new Object[]{new Long(orgId),new Integer(1)});
		}else{
			return 0;
		}
	}
	/**
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String conversionAffair(long taskId) throws Exception {
		
		Task task  = getEntityById(taskId);
		if(task != null && task.getTaskStatus() != 3 || task.getTaskStatus() != 4){
			List<AssignTask>  list = assignTaskService.selectAssignTaskByTaskId(taskId);
			if(list == null || list.size() == 0){
				return "2";
			}
			AssignTask atk = list.get(0);//获取任务分配人 执行用户信息
			if(atk.getStatus() == 0){
				AffairDetailVO ad = new AffairDetailVO();
				String ftpUrl = FileInstance.FTP_WEB + FileInstance.FTP_URL;
				List<AffairMemberVO> memebers = new ArrayList<AffairMemberVO>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				
				User assugnerUser = userService.selectByPrimaryKey(atk.getAssignerId());
				User performerUser = userService.selectByPrimaryKey(atk.getPerformerId());
				
				memebers.add(new AffairMemberVO(0l,atk.getAssignerId(),"0","c",assugnerUser.getName(),ftpUrl+assugnerUser.getPicPath(),null));
				memebers.add(new AffairMemberVO(0l,atk.getAssignerId(),"0","m",performerUser.getName(),ftpUrl+performerUser.getPicPath(),null));
				ad.setExpired("0");
				ad.setTitleType("t");
				ad.setFinished("0");
				ad.setTitle(task.getTitle());
				ad.setMemebers(memebers);
				ad.setDetail(task.getTaskDescription());
				ad.setStartTime(sdf.format(task.getStartTime()));
				ad.setEndTime(sdf.format(task.getEndTime()));
				long id = affairService.createAffair(ad);
				if(id != 0){
					return "1";
				}else{
					return "4";
				}
			}else{
				return "2";
			}
		}else{
			return "3";
		}
	}

	@Override
	public String updateOrganizationTaskStatus(long taskId,long userId,int type) throws Exception {
		
			logger.info("更新组织任务状态  taskid:"+taskId+",userid:"+userId+",type:"+type);
			Task t=getEntityById(taskId);
			if(t!=null){
				if(t.getCreateId()!=userId){
					return "3";
				}
				t.setTaskStatus(type);
				update(t);
				if(type == 2){
					//完成操作需要记录操作表  组织任务
					try{
						Operation o = new Operation();
						o.setOperationCode(TaskCode.FINISH.getCode());// 操作类型
						o.setOrganizationTaskId(t.getId());// 组织任务id
						o.setOperationUid(userId);//操作人id
						o.setOperactionTime(new Timestamp(System.currentTimeMillis()));
						operationService.createOperation(o);
					}catch(Exception e){
						logger.info("update taskStatus add organizationOperation is error :"+t.getId());
					}
				}	
				return "1";
			}else{
				return "2";
			}
	}

	@Override
	public String taskRemind(long taskId) throws Exception {
		
		Task t = getEntityById(taskId);
		if(t == null){ return "2";}
		long time = System.currentTimeMillis() - t.getLastRemindTime();
		if(time < 8*60*60*1000){
			return "5";
		}
		List<AssignTask> list = assignTaskService.selectAssignTaskByTaskId(taskId);
		if(CollectionUtils.isEmpty(list)){return "3";}
		
		for(AssignTask at :list){
			if(at.getStatus() == 0){
				boolean result = tongRenSendMessageService.sendTaskRemind(at.getAssignerId(), at.getPerformerId(),t.getOrganizationId(), t.getProjectUndertakenId(),t.getTitle());
				if(result){
					t.setLastRemindTime(System.currentTimeMillis());
					save(t);
					return "1";
				}else{
					return "4";
				}
			}
		}
		return "3";
	}
}
