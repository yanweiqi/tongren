/*
 * 文件名： UndertakenServiceImpl.java
 * 创建日期： 2015年11月11日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.util.CacheStoreManage;
import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.project.manage.Exception.PublishException;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.manage.service.AbortedService;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.DeliveryService;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;
import com.ginkgocap.tongren.project.task.exception.UndertakenException;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;


 /**
 *  承接项目接口实现
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月11日
 */
@Service("undertakenService")
public class UndertakenServiceImpl extends AbstractCommonService<Undertaken> implements UndertakenService {
	
	private static final Logger logger = LoggerFactory.getLogger(UndertakenServiceImpl.class);

	private static int error_object_null = 100; // 对象为空
	
	private static int error_create_obj = 101; // 对象创建失败
	
	private static int error_apply = 102;//申请失败
	
	private static int error_undertaken_manytimes = 103;//项目已经承接
	
	private static int error_list = 104; //集合问题
	
	private static int apply_status = 2;//项目已经承接
	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private ProjectService projectService;//
	@Autowired
	private PublishService publishService;//发布项目接口
	@Autowired
	private ApplyService applyService;//项目申请接口
	@Autowired
	private OperationService operationService;//
	@Autowired
	private AbortedService abortedService;//放弃项目记录接口
	@Autowired
	private DeliveryService deliveryService;//完成项目记录接口
	@Autowired
	private JmsSendService jmsSendService;//MQ消息接口
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;//发送消息接口
	@Autowired
	private MessageService messageService;
	
	
	@Override
	protected Class<Undertaken> getEntity() {
		return Undertaken.class;
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
	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.task.service.UndertakenService#undertakenProject(long, long, long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Undertaken undertakenProject(long projectId, long recipientId,long recipientOrganizationId) throws UndertakenException {
		Publish publish = null;
		Undertaken retrunUndertaken = null;
		Map<String, Object> acceptMap = new HashMap<String, Object>();
		try {
			publish = publishService.getPublishByProjectId(projectId);//查询发布的项目
			if(publish == null){//验证是否项目发布了
				messageService.deleteMessageByProjectId(projectId);
				logger.error("Publish object is empty or null");
				throw new UndertakenException(error_object_null, "The specified Publish_Project by projectId [" + ObjectUtils.toString(projectId, null)
						+ "] does not Publish_Project object");
			}
			publish.setProject(projectService.getEntityById(projectId));
			if(publish.getStatus()!=1){
				messageService.deleteMessageByProjectId(projectId);
				logger.info("project status is not allow undertaken"+publish.getStatus()+","+projectId);
				throw new UndertakenException(201, "project status is not allow undertaken");
			}
			Undertaken undertakenCheck = getUndertakenByProjectId(projectId);
			if(undertakenCheck != null){//验证项目是否被承接了
				logger.error("Project has been carried");
				messageService.deleteMessageByProjectId(projectId);
				throw new UndertakenException(error_undertaken_manytimes,"Project has been carried,projectId is ["+projectId+"]");
			}
			
			acceptMap = applyService.accept(publish.getPublisherId(), projectId, recipientId,recipientOrganizationId);
			if(acceptMap == null){//是否确认申请OK
				logger.error("Confirmation of application for failure");
				throw new UndertakenException(error_apply,"Confirmation of application for failure acceptMap is [" +acceptMap+"]");
			}
			Apply apply = (Apply) acceptMap.get(CommonConstants.ACCEPT_SUCCESS);
			if(apply == null){//是否确认申请返回成功
				logger.error("Confirmation of application for failure");
				throw new UndertakenException(error_apply,"Confirmation of application for failure apply is ["+apply+"]");
			}
			
			Timestamp startTime = new Timestamp(System.currentTimeMillis());
			Timestamp endTime = new Timestamp(DateUtil.addDay(startTime, publish.getProject().getCycle()).getTime());
			
			Undertaken undertaken = new Undertaken();
			undertaken.setProjectId(projectId);
			undertaken.setRecipientId(recipientId);
			undertaken.setRecipientOrganizationId(recipientOrganizationId);
			undertaken.setStartTime(startTime);
			undertaken.setEndTime(endTime);
			undertaken.setPublishId(publish.getPublisherId());
			undertaken.setPublishOrganizationId(publish.getOrganizationId());
			undertaken.setStatus(0);
			retrunUndertaken = save(undertaken);
			if(retrunUndertaken == null){
				logger.error("create undertaken project is failure");
				throw new UndertakenException(error_create_obj,"undertaken Object creation failed , projectId ["+ObjectUtils.toString(projectId, null)+"]");
			}
			//承接项目后插入主任务
			Task task =new Task();
			task.setCreateId(recipientId);//创建人
			task.setStartTime(startTime);
			task.setOrganizationId(recipientOrganizationId);//组织id
			task.setProjectUndertakenId(projectId);//项目id
			task.setTaskDescription(publish.getProject().getIntroduction());
			task.setTitle(publish.getProject().getName());
			task.setTaskStatus(1);//任务状态，由于页面没有设置开始时间，主任务创建时视为开始
//			int projectT = (int) (((DateUtil.trunck(endTime).getTime())-(DateUtil.trunck(startTime).getTime()))/(CommonConstants.CALCULATION_DAYS));
			task.setCycle(publish.getProject().getCycle());//项目周期 单位: 天
			String statusTask=projectTaskService.addPrimaryTask(task);
			//1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 不存在对应的主任务 5缺少信息
			
			logger.info("statusTask is "+statusTask);
			
			if(!tongRenSendMessageService.sendIsAgreeInvitationProjectMes(publish.getPublisherId(), recipientId, recipientOrganizationId, projectId, 1))
				logger.warn("sendIsAgreeInvitationProjectMes is error");
			CacheStoreManage.cleanByKey("bean_page_publish");
			return retrunUndertaken;
		} catch (UndertakenException e) {
			logger.error("UndertakenException ",e);
			throw e;
		}catch (Exception e) {
			 logger.error("undertaken failed!"+projectId,e);
			throw new UndertakenException(e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.task.service.UndertakenService#create(long, long, long, java.sql.Timestamp, java.sql.Timestamp, long, long)
	 */
	@Override
	public Undertaken create(long projectId, long recipientId,
			long recipientOrganizationId, Timestamp startTime,
			Timestamp endTime, long publishId, long publishOrganizationId,int status)
			throws Exception {
		Undertaken retrunUndertaken = null;
		try {
			Project project = projectService.getEntityById(projectId);//查询是否有这个项目
			if(project == null)
				return null;
			Undertaken undertaken = new Undertaken();
			undertaken.setProjectId(projectId);
			undertaken.setRecipientId(recipientId);
			undertaken.setRecipientOrganizationId(recipientOrganizationId);
			undertaken.setStartTime(startTime);
			undertaken.setEndTime(endTime);
			undertaken.setPublishId(publishId);
			undertaken.setPublishOrganizationId(publishOrganizationId);
			undertaken.setStatus(0);
			retrunUndertaken = save(undertaken);
			if(retrunUndertaken == null)
				return null;
			//承接项目后插入主任务
			Task task =new Task();
			task.setCreateId(recipientId);//创建人
			task.setStartTime(startTime);
			task.setOrganizationId(recipientOrganizationId);//组织id
			task.setProjectUndertakenId(projectId);//项目id
			task.setTaskDescription(project.getIntroduction());
			task.setTitle(project.getName());
			task.setTaskStatus(1);//任务状态，由于页面没有设置开始时间，主任务创建时视为开始
			int projectT = (int) (((DateUtil.trunck(endTime).getTime())-(DateUtil.trunck(startTime).getTime()))/(CommonConstants.CALCULATION_DAYS));
			task.setCycle(projectT);//项目周期 单位: 天
			String statusTask=projectTaskService.addPrimaryTask(task);
			//1_id 增加成功 2已经存在主任务 3与主任务不是一个组织 4 不存在对应的主任务 5缺少信息
			logger.info("statusTask is "+statusTask);
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return retrunUndertaken;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.task.service.UndertakenService#getUndertakenList(long)
	 */
	@Override
	public List<Undertaken> getUndertakenList(long recipientId,int status) throws Exception {
		List<Undertaken> undertakens = null;
		List<Undertaken> retrunLis = new ArrayList<Undertaken>();
		try {
			if(status != -1){//-1全部、0项目进行中、1完成、2、放弃、3已过期 
				List<Long> ids = getKeysByParams("Undertaken_List_By_RecipientIdAndStatus", new Object[]{recipientId,status});
				if (null != ids) {
					undertakens = getEntityByIds(ids);
				}
			}else{
				List<Long> ids = getKeysByParams("Undertaken_List_By_RecipientId", new Object[]{recipientId});
				if (null != ids) {
					undertakens = getEntityByIds(ids);
				}
			}
			if(undertakens != null && undertakens.size() !=0){
				for (Undertaken undertaken : undertakens) {
					Project project = projectService.getProjectDetail(undertaken.getProjectId());
//					Publish publish = publishService.getPublishByStatusAndProject(ProjectStatus.Project_Publish_Success.getKey(), undertaken.getProjectId());//查询发布的项目
					if(project != null){
						Publish publish = new Publish();
						publish.setProject(project);
						undertaken.setPublish(publish);
					}
					retrunLis.add(undertaken);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw e;
		}
		return retrunLis;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.task.service.UndertakenService#getUndertakenByProjectId(long)
	 */
	@Override
	public Undertaken getUndertakenByProjectId(long projectId) throws Exception {
		Long id = 0l;
		try {
			id = getMappingByParams("Undertaken_By_ProjectId", new Object[]{projectId});
			if(id==null){
				return null;
			}else{
				return getEntityById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.task.service.UndertakenService#delUndertakenByProjectId(long)
	 */
	@Override
	public boolean delUndertakenByProjectId(long projectId) throws Exception {
		long id = getMappingByParams("Undertaken_By_ProjectId", new Object[]{projectId});
		return deleteEntityById(id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.task.service.UndertakenService#extensionProject(long, int)
	 */
	@Override
	public boolean extensionProject(long projectId, int cycle)
			throws Exception {
		Undertaken undertaken = getUndertakenByProjectId(projectId);
		if(undertaken == null){
			return false;
		}
		Timestamp endTime = new Timestamp(DateUtil.addDay(undertaken.getEndTime(), cycle).getTime());
		undertaken.setEndTime(endTime);
		String taskStatus = "";
		try {
//			Publish p_publish = publishService.updatePublish(projectId, ProjectStatus.Project_Publish_Delay,null);
//			if(p_publish == null){
//				logger.error("p_publish is null,projectId is 【"+projectId+"】");
//				return false;
//			}
			 taskStatus = projectTaskService.delayDays(projectId, cycle);
			 if(taskStatus == "2"){
				 logger.error("ProjectId for the 【"+projectId+"】 of the main task does not exist");
			 }
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace(System.err);
			}
			throw new Exception(e);
		}
		return update(undertaken);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.UndertakenService#giveupProject(long, long)
	 */
	@Override
	public boolean projectOperation(long projectId, long userId,int type) throws Exception {
		Undertaken undertaken = getUndertakenByProjectId(projectId);
		if(undertaken == null){
			logger.error("According to the project ID no query to undertake the project.project id is 【"+projectId+"】");
			return false;
		}
		
		if(type == 1){//结束项目
			Publish p_publish = publishService.updatePublish(projectId, ProjectStatus.Project_Publish_Complect,null);
			if(p_publish == null){
				return false;
			}
			undertaken.setStatus(1);
			try {//插入项目成功记录
				deliveryService.create(undertaken.getRecipientId(),
						undertaken.getRecipientOrganizationId(),
						undertaken.getId(),
						new Timestamp(System.currentTimeMillis()), 0,
						undertaken.getPublishId(),
						undertaken.getRecipientOrganizationId());
			} catch (Exception e) {
				logger.error("Failed to insert the delivery project");
			}
		}
		if(type == 2){//放弃项目
			Publish p_publish = publishService.updatePublish(projectId, ProjectStatus.Project_Publish_Abort,null);
			if(p_publish == null){
				return false;
			}
			undertaken.setStatus(2);
			try {//插入项目失败记录
				abortedService.create(projectId, undertaken.getId(), null, undertaken.getRecipientId(), 0, userId, undertaken.getPublishOrganizationId());
			} catch (Exception e) {
				logger.error("Failed to insert the aborted project");
			}
		}
		if(type == 3){//已过期项目
			Publish p_publish = publishService.updatePublish(projectId, ProjectStatus.Project_Publish_Expired_Unfinished,null);
			if(p_publish == null){
				return false;
			}
			undertaken.setStatus(3);
		}
		boolean result = update(undertaken);
		CacheStoreManage.cleanByKey("bean_page_publish");
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.UndertakenService#batchUpdateStatus()
	 */
	@Override
	public void batchUpdateStatus() {
		List<Long> ids=getKeysByParams("Undertaken_list_status",0);
		if(null == ids || ids.size() == 0){
			logger.info("承接的项目状态为进行中的为空");
		}else{
			//0项目进行中、1完成、2、放弃、3已过期
			for(Long id:ids){
				Undertaken undertaken=getEntityById(id);
				if(undertaken.getStatus()==0){
					if(undertaken.getEndTime()!=null&&undertaken.getEndTime().getTime()<System.currentTimeMillis()){
						logger.info("承接更新为已过期 ,undertaken "+undertaken.getId());
						undertaken.setStatus(3);
						Publish p_publish;
						try {
							p_publish = publishService.updatePublish(undertaken.getProjectId(), ProjectStatus.Project_Publish_Expired_Unfinished,null);
							if(p_publish != null){
								update(undertaken);
							}else{
								logger.info("承接项目的p_publish更新失败，项目ID为：【"+undertaken.getProjectId()+"】");
							}
						} catch (PublishException e) {
							e.printStackTrace();
							logger.info("承接项目的p_publish更新失败error，项目ID为：【"+undertaken.getProjectId()+"】");
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("承接项目的p_publish更新失败error1，项目ID为：【"+undertaken.getProjectId()+"】");
						}
						
					}
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.UndertakenService#updateOrgProject(long, long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean updateOrgProject(long projectId, long organizationId)
			throws UndertakenException {
	
		Undertaken undertaken = null;
		try {
			undertaken = getUndertakenByProjectId(projectId);
			if(undertaken == null){
				logger.error("undertaken object is empty or null");
				throw new UndertakenException(error_object_null, "The specified Undertaken by projectId [" + ObjectUtils.toString(projectId, null)
						+ "] does not undertaken object");
			}
			undertaken.setRecipientOrganizationId(organizationId);
			List<Apply> apps = applyService.isUndertakeByProjectId(projectId, apply_status);
			if(apps == null || apps.size() == 0 || apps.size() > 1){
				logger.error("apps object is empty or null");
				throw new UndertakenException(error_list, "The specified Apply by projectId [" + ObjectUtils.toString(projectId, null)
						+ "] Object is not under the conditions to undertake the next");
			}
			Apply apply = apps.get(0);
			if(apply == null){
				logger.error("apply object is empty or null");
				throw new UndertakenException(error_object_null, "No application records to be updated");
			}
			apply.setOrganizationId(organizationId);
			if(!applyService.update(apply)){
				logger.error("update apply status");
				throw new UndertakenException(error_object_null, "The specified Apply by projectId [" + ObjectUtils.toString(projectId, null)
						+ "] Object is not under the conditions to undertake the next");
			}
			projectTaskService.convertToOrgTask(projectId, organizationId);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace(System.err);
			}
			throw new UndertakenException(e);
		}
		return update(undertaken);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.UndertakenService#getUndertakenByOrgAndStatus(long)
	 */
	@Override
	public boolean getUndertakenByOrganizationId(long organizationId)
			throws UndertakenException {
		int status = 0;//项目进行中的状态
		try {
			List<Long> ids = getKeysByParams("Undertaken_list_status_organizationId", new Object[]{status,organizationId});
			if(ids == null || ids.size() == 0)
				return false;
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace(System.err);
			}
			throw new UndertakenException(e);
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.UndertakenService#getUndertakenByOrganizationIdByStatus(long, int)
	 */
	@Override
	public List<Undertaken> getUndertakenByOrganizationIdByStatus(long organizationId,int status) throws UndertakenException {
		
		List<Long> ids = new ArrayList<Long>();
		List<Undertaken> list = new ArrayList<Undertaken>();
		try {
			ids = getKeysByParams("Undertaken_list_status_organizationId", new Object[]{status,organizationId});
			if(ids != null && ids.size() != 0){
				list = getEntityByIds(ids);
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace(System.err);
			}
			throw new UndertakenException(e);
		}
		return list;
	}
	
}
