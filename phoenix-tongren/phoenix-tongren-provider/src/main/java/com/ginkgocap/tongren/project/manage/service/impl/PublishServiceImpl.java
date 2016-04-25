package com.ginkgocap.tongren.project.manage.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.JmsMsgBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;
/**
 * 项目发布service实现类
 *  
 * @author yanweiqi
 * @version 
 * @since 2015年11月9日
 */
@Service("publishService")
public class PublishServiceImpl extends AbstractCommonService<Publish> implements PublishService {
	
	private static final Logger logger = LoggerFactory.getLogger(PublishServiceImpl.class);
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ApplyService applyService;
	
	@Autowired
	private JmsSendService jmsSendService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CodeRegionService codeRegionService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired
	private UndertakenService undertakenService;
	
	
	@Override
	public Publish resubmit(long projectId, int delay) throws Exception {
		Publish new_publish = null ;
		try {
			Publish publish = getPublishByProjectId(projectId);
			Project project = projectService.getEntityById(projectId);
			if(null != publish && null != project){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, delay);
				Date new_endDate = calendar.getTime();
				new_publish = updatePublish(projectId, ProjectStatus.Project_Publish_Resumit,Timestamp.valueOf(sdf.format(new_endDate)));
				List<Apply> applies = applyService.getMyApplysByProjectId(publish.getPublisherId(), projectId, null);
				if(null != applies){
					for (Apply apply : applies) {
						boolean del_action_status = applyService.deleteEntityById(apply.getId()); //删除之前对我项目的申请
						if(del_action_status){
							logger.info("success applyId:"+apply.getId()+",Apply:"+apply.toString());
						}
						else{
							logger.warn("fail applyId:"+apply.getId()+",Apply:"+apply.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
			throw e;
		}
		return new_publish;
	}
	
	@Override
	public Publish getPublishByStatusAndProject(int status, long projectId) throws Exception {
		Publish publish = null;
		try {
			publish = getPublishByProjectId(projectId);
			if(publish != null && publish.getStatus() == status){
				return publish;
			}else{
				logger.info("getPublishByStatusAndProject puvlish is null..status ="+status+",projectId ="+projectId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() ,e );
			throw e;
		}
		return publish;
	}
	
	@Override
	public Publish getPublishByProjectId(long projectId) throws Exception {
		Publish publish = null;
		try {
			Long id = getMappingByParams("publish_map_projectId", new Object[]{projectId});
			if(null != id){
			   publish = getEntityById(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() ,e );
			throw e;
		}
		return publish;
	}
	
	@Override
	public List<Publish> checkProjectIsExpire() throws Exception {
		List<Publish> update_list_publish = new ArrayList<Publish>();
		try {
			//检测项目是否到期
			List<Long> ids = getKeysByParams("publish_list_checkIsExpire", new Object[]{ProjectStatus.Project_Publish_Success.getKey()});
			if(null != ids && !ids.isEmpty()){
				for (long publishId : ids) {
					Publish publish = getEntityById(publishId);
					publish.setStatus(ProjectStatus.Project_Publish_Expired.getKey());
					Publish  p_publish = updatePublish(publish.getProjectId(),ProjectStatus.Project_Publish_Expired,null);
				    update_list_publish.add(p_publish);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return update_list_publish;
	}
	
	@Override
	public Page<Publish> getPagePublishByStatus(int status, Page<Publish> page) throws Exception {	
		Page<Publish> pagePublish = new Page<Publish>();
		BeanUtils.copyProperties(page, pagePublish);
		int begin = page.getSize() * (page.getIndex()-1);
		try {
			List<Long> total = getKeysByParams("publish_list_status", new Object[]{status});
			if(null != total && begin <= total.size()){
				pagePublish.setTotalCount(total.size());
				List<Long> ids = getKeysByParams("publish_list_status", new Object[]{status}, begin, page.getSize());
				if (null != ids && ids.size() > 0) {
					List<Publish> publishs = getEntityByIds(ids);
					for (Publish publish : publishs) {
						Project p = projectService.getProjectDetail(publish.getProjectId());
						publish.setProject(p);
						List<Apply> list_apply = applyService.getApplyByProjectId(p.getId());
						if(null != list_apply && list_apply.size() > 0){
							Set<Apply> applies = new HashSet<Apply>(list_apply);
							publish.setApplySet(applies);
						}
					}
					pagePublish.setResult(publishs);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return pagePublish;
	}
	
	@Override
	public List<Publish> getAllPublishs() throws Exception {
		List<Publish> publishs = new ArrayList<Publish>();
		int status = ProjectStatus.Project_Apply_CheckPending.getKey();//待审核
		try {
			List<Long> ids = getKeysByParams("publish_list_status", new Object[]{status});
			publishs = getEntityByIds(ids);
			if(publishs.size() != 0){
				for (Publish publish : publishs) {
					Project p = projectService.getProjectDetail(publish.getProjectId());
					publish.setProject(p);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return publishs;
	}
	
	@Override
	public List<Publish> getPublishByPublisherIdAndStatus(long publisherId, Integer publishStatus) throws Exception {
		List<Publish> publishs = null;
		try {
			List<Long> ids = getKeysByParams("publish_list_publisherId_status", new Object[]{publisherId,publishStatus.intValue()});
			if (null != ids && ids.size() > 0) {
				publishs = getEntityByIds(ids);
				for (Publish publish : publishs) {
					Project p = projectService.getProjectDetail(publish.getProjectId());
					if(null != p) publish.setProject(p);
					List<Apply> applies = applyService.getApplyByProjectId(publish.getProjectId());
					if(null != applies && applies.size() > 0){
					   publish.setApplySet(new HashSet<Apply>(applies));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return publishs;
	}	
	
	@Override
	public List<Publish> getPublishByPublisherId(long publisherId) throws Exception{
		List<Publish> publishs = new ArrayList<Publish>();
		try {
			List<Long> ids = getKeysByParams("publish_list_publisherId", new Object[]{publisherId});
			if (null != ids && ids.size() > 0) {
				CopyOnWriteArrayList<Publish> copy_publishs = new CopyOnWriteArrayList<Publish>(getEntityByIds(ids)) ;
				for (Publish publish : copy_publishs) {
	                if(publish.getStatus() == ProjectStatus.Project_Publish_Discussed.getKey()){  //过滤屏蔽发布
	                	copy_publishs.remove(publish);
	                	continue;
	                }
					Project p = projectService.getProjectDetail(publish.getProjectId());
					if(null != p) publish.setProject(p);
					List<Apply> applies = applyService.getApplyByProjectId(publish.getProjectId());
					if(null != applies && applies.size() > 0){
					   for (Apply apply : applies) {
						   if(0 < p.getCycle() && apply.getReviewTime() != null){
							   apply.setCompletedTime(p.getCycle(),apply.getReviewTime());
						   }
					   }
					   publish.setApplySet(new HashSet<Apply>(applies));
					}
					publishs.add(publish);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return publishs;
	}
	
	@Override
	public List<Publish> getPublishByReviewerIdAndApplyStatus(long reviewerId,int applyStatus) throws Exception{
		List<Publish> publishs = new ArrayList<Publish>();
		try {
			CopyOnWriteArrayList<Publish> copyPublish = new CopyOnWriteArrayList<Publish>();
			if(applyStatus == ProjectStatus.Project_Apply_Success.getKey()){
				copyPublish = getHasUndertake(reviewerId);
			}
			else{
				copyPublish = getNoUndertake(reviewerId);
			}
			publishs.addAll(copyPublish);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return publishs;
	}

	/**
	 * 已承接
	 * 1、发布状态为8
	 * 2、申请状态为2 
	 */
	private CopyOnWriteArrayList<Publish> getHasUndertake(long reviewerId) throws Exception {
		CopyOnWriteArrayList<Publish> copyPublish = new CopyOnWriteArrayList<Publish>();
		List<Publish> tmp_publishs = getPublishByPublisherIdAndStatus(reviewerId, ProjectStatus.Project_Publish_Inprogress.getKey());
		if(null != tmp_publishs && tmp_publishs.size() > 0 ){
			copyPublish.addAll(tmp_publishs);
			for(Publish publish : tmp_publishs) {
				 Set<Apply> applySet = publish.getApplySet();
				 if(null != applySet && applySet.size() > 0){
					 for(Apply apply : applySet) {
						  if(apply.getStatus() != ProjectStatus.Project_Apply_Success.getKey()){
							copyPublish.remove(publish);
						  }
					 }
				 }
			}
			
		}
		return copyPublish;
	}

	/**
	 * 未承接
	 * 1、发布状态为1
	 * 2、申请状态为1
	 * 3、发布没有到期
	 */
	private CopyOnWriteArrayList<Publish> getNoUndertake(long reviewerId) throws Exception {
		CopyOnWriteArrayList<Publish> copyPublish = new CopyOnWriteArrayList<Publish>();
		List<Publish> tmp_publishs = getPublishByPublisherIdAndStatus(reviewerId, ProjectStatus.Project_Publish_Success.getKey());
		if(null != tmp_publishs && tmp_publishs.size() > 0){
			copyPublish.addAll(tmp_publishs);
			for (Publish publish : copyPublish) {
				Set<Apply> applySet = publish.getApplySet();
				if(null != applySet && applySet.size() > 0){
				   for (Apply apply : applySet) {
					   if(apply.getStatus() != ProjectStatus.Project_Apply_CheckPending.getKey()){
						  copyPublish.remove(publish); 
					   }
				   }
				}
			}
		}
		return copyPublish;
	}
	

	/**
	 * 更新对象唯一入口
	 * @param publish
	 * @return
	 */	
	@Override
	public Publish updatePublish(long projectId, ProjectStatus ps,Timestamp endDate) throws Exception {
		Publish p_publish = null;
		try {
			Publish publish = getPublishByProjectId(projectId);
			if (null != publish){
				Integer publish_status = ps == ProjectStatus.Project_Publish_Resumit ? ProjectStatus.Project_Publish_Success.getKey() : ps.getKey();
				publish.setStatus(publish_status);
				if(null != endDate){
				   publish.setEndDate(endDate);
				   Project p = projectService.getEntityById(projectId);
				   p.setValidityEndTime(endDate);
				   projectService.update(p);
				}
			    boolean update_status = update(publish);//更新发布项目
			    if (update_status){
					//完成项目发布项目所属组织项目数+1
					if(publish_status == 4){
						Undertaken undertaken = undertakenService.getUndertakenByProjectId(projectId);
						if(undertaken != null){
							Organization o = tongRenOrganizationService.getEntityById(undertaken.getRecipientOrganizationId());
							if(o !=null){
								logger.info("update organization performSize begin organizatinId="+o.getId());
								o.setPerformSize(o.getPerformSize()+1);
								tongRenOrganizationService.update(o);
							}
						}
					}
				    p_publish = getEntityById(publish.getId());
				    //往MQ发送消息
			        Project p_project = projectService.getProjectDetail(publish.getProjectId());
			        if (null != p_project){
			        	 SendMQ(p_project, p_publish, ps);
			      }	
			   }
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_publish;
	}
	
	@Override
	public Publish create(long publisherId, int status, Timestamp startDate,Timestamp endDate, Project project) throws Exception{
		Publish p_publish = null;
		try {
			Publish p = new Publish();
			p.setPublisherId(publisherId);
			p.setStatus(status);
			p.setStartDate(startDate);
			p.setEndDate(endDate);
			Timestamp t = new Timestamp(System.currentTimeMillis());
			p.setCreateTime(t);
			p.setUpdateTime(t);
			if(project.getOrganizationId() != 0) p.setOrganizationId(project.getOrganizationId());
			if(project.getId() != 0) p.setProjectId(project.getId());
			Publish tmp_p = getPublishByProjectId(project.getId());
			if (null == tmp_p){
				p_publish = save(p);
				if (null != p_publish){
					Project p_project = projectService.getProjectDetail(project.getId());
					p_publish.setStatus(ProjectStatus.Project_Create_Official.getKey());;
					projectService.update(p_project);
					//往MQ发送消息
					SendMQ(p_project, p_publish, ProjectStatus.Project_Publish_Success);
				}
			}
			else{
				logger.info("update publish...");
				tmp_p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				update(tmp_p);
				return tmp_p;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_publish;
	}
	
	@Override
	protected Class<Publish> getEntity() {
		return Publish.class;
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
	public Publish resubmit_v3(long projectId, ProjectStatus ps,String startDate, String endDate, int cycle) throws Exception {
		
		Publish new_publish = null ;
		try {
			Publish publish = getPublishByProjectId(projectId);
			Timestamp updateDate = new Timestamp(new Date().getTime());
			Project project = projectService.getEntityById(projectId);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			if(publish != null && project != null && publish.getStatus() == 3){
				//修改项目 发布参数
				publish.setStatus(ps.getKey());
				if(StringUtils.isNotBlank(startDate)){
					publish.setStartDate(new Timestamp(format.parse(startDate).getTime()));
					project.setValidityStartTime(new Timestamp(format.parse(startDate).getTime()));
				}
				publish.setUpdateTime(updateDate);
				project.setUpdateTime(updateDate);
				if(cycle != 0){project.setCycle(cycle);}
				publish.setEndDate(new Timestamp(format.parse(endDate).getTime()));
				project.setValidityEndTime(new Timestamp(format.parse(endDate).getTime()));
				
				projectService.update(project);
			    boolean update_status = update(publish);//更新发布项目
				if(update_status){
					 new_publish = getEntityById(publish.getId());
				     Project p_project = projectService.getProjectDetail(publish.getProjectId());
				     //往MQ发送消息
				     SendMQ(p_project, new_publish, ps);
				}
				List<Apply> applies = applyService.getMyApplysByProjectId(publish.getPublisherId(), projectId, null);
				if(null != applies){
					for (Apply apply : applies) {
						boolean del_action_status = applyService.deleteEntityById(apply.getId()); //删除之前对我项目的申请
						if(del_action_status){
							logger.info("success applyId:"+apply.getId()+",Apply:"+apply.toString());
						}
						else{
							logger.warn("fail applyId:"+apply.getId()+",Apply:"+apply.toString());
						}
					}
				}
			}else{
				logger.info("project or publish or pulishStatus is null found... ");
			}
		} catch (Exception e) {
			logger.info("resubmit is error...projectId--"+projectId);
			logger.info(e.getMessage(),e);
			throw e;
		}
		return new_publish;
	}
	private void SendMQ(Project p_project,Publish publish,ProjectStatus ps){
        if (null != p_project){
        	publish.setProject(p_project);
        	JmsMsgBean jmsMsgBean=new JmsMsgBean();
        	jmsMsgBean.setOpType(CommonConstants.Publish);
        	jmsMsgBean.setOperator(ps.getType());
        	jmsMsgBean.setContent(publish);
        	jmsSendService.sendJmsMsg(jmsMsgBean);				
      }	
	}
}
