package com.ginkgocap.tongren.project.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.project.manage.Exception.ApplyException;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
/**
 * 项目申请service实现类
 *  
 * @author yanweiqi
 * @version V1.0
 * @since 2015年11月9日
 */
@Service("applyService")
public class ApplyServiceImpl extends AbstractCommonService<Apply> implements ApplyService {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplyServiceImpl.class);
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired 
	private PublishService publishService;
	
	@Override
	public List<Apply> getAppliesByProposerIdAndStatus(long proposerId, Integer status) throws Exception {
		CopyOnWriteArrayList<Apply> applies = new CopyOnWriteArrayList<Apply>();
		try {
			List<Long> ids = getKeysByParams("apply_list_proposerId_status", new Object[]{proposerId});
			if(null != ids && ids.size() > 0){
				applies.addAll(buildApply(getEntityByIds(ids)));
				if(null != status){
					for (Apply a : applies) {
						if(status.intValue() != a.getStatus()){
							applies.remove(a);
						}
					}
				}
			}
 		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return applies;
	}
	
	@Override
	public List<Apply> getAppliesByReviewerIdAndStatus(long reviewerId,Integer status) throws Exception {
		List<Apply> applies = null;
		List<Long> ids = null;
		try {
			if(null != status){
				ids = getKeysByParams("apply_list_reviewerId_status", new Object[]{reviewerId,status});
			}
			else{
				ids = getKeysByParams("apply_list_reviewerId", new Object[]{reviewerId});
			}
			if(null != ids && ids.size() > 0){
			    applies = buildApply(getEntityByIds(ids));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return applies;
	}

	private List<Apply> buildApply(List<Apply> applies) {
		if(applies == null || applies.size() == 0){
			logger.warn("applies is empty "+applies);
			return null;
		}
		List<Apply> new_applies = new ArrayList<Apply>();
		for (Apply a : applies) {
			Organization organization = tongRenOrganizationService.getEntityById(a.getOrganizationId());
			if(null != organization) a.setOrganizationName(organization.getName());
			new_applies.add(a);
		}
		return new_applies;
	}
	
	private Apply buildApply(Long id) {
		Apply apply = getEntityById(id);
		Organization o = tongRenOrganizationService.getEntityById(apply.getOrganizationId());
		if(null != o) apply.setOrganizationName(o.getName());
		return apply;
	}
	
	@Override
	public List<Apply> isUndertakeByProjectId(long projectId, int stauts) throws Exception {
		List<Apply> applies = null;
		try {
			List<Long> ids  = getKeysByParams("apply_list_projectId_status", new Object[]{projectId,stauts});
			if (null != ids) {
				applies = buildApply(getEntityByIds(ids));		
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return applies;
	}
	
	@Override
	public List<Apply> getApplyByProjectId(long projectId) throws Exception{
		List<Apply> applies = new ArrayList<Apply>();
		try {
			List<Long> ids = getKeysByParams("apply_list_projectId", new Object[]{projectId});
			if(null != ids && ids.size() > 0){
				applies = buildApply(getEntityByIds(ids));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return applies;
	}
	
	@Override
	public Map<String, Object> accept(long reviewerId, long projectId, long proposerId,long organizationId) throws Exception {
		boolean accept_status = false;
		Map<String, Object> acceptMap = new HashMap<String, Object>();
		try {
			Apply apply = getMyApplyByProjectIdAndProposerId(reviewerId, projectId, ProjectStatus.Project_Apply_CheckPending.getKey(), proposerId,organizationId);
			if (null != apply){
				apply.setStatus(ProjectStatus.Project_Apply_Success.getKey());
				apply.setReviewTime(new Timestamp(System.currentTimeMillis()));
				accept_status = update(apply);     //同意某个人的申请
				if(accept_status){
				   logger.info("projectId:"+apply.getProjectId()+",proposerId:"+apply.getProposerId()+",apply:sucess");	
				   Publish publish = publishService.updatePublish(apply.getProjectId(), ProjectStatus.Project_Publish_Inprogress,null);	
				   logger.info("projectId:"+apply.getProjectId()+"publish status:"+ProjectStatus.getValueBykey(publish.getStatus()));
				   Apply p_apply = getEntityById(apply.getId());
				   acceptMap.put(CommonConstants.ACCEPT_SUCCESS, p_apply);
				   List<Apply> applies = getMyApplysByProjectId(reviewerId, projectId, ProjectStatus.Project_Apply_CheckPending.getKey());
				   List<Apply> refuse_List_apply_success = new ArrayList<Apply>();
				   List<Apply> refuse_List_apply_fail = new ArrayList<Apply>();
				   if(null != applies){            //拒绝多个人的申请
					   for (Apply a : applies) {
							Apply refuse_apply = refuse(reviewerId, projectId, a.getProposerId(),a.getOrganizationId());
							if(refuse_apply != null){
							   refuse_List_apply_success.add(refuse_apply);
							}
							else{
							   refuse_List_apply_fail.add(refuse_apply);
							   logger.warn("refuse apply proposerId:"+a.getProposerId()+",status:"+CommonConstants.REFUSE_FAIL);
							}
						}
				   }
				   acceptMap.put(CommonConstants.REFUSE_SUCCESS, refuse_List_apply_success);
				   acceptMap.put(CommonConstants.REFUSE_FAIL, refuse_List_apply_fail);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return acceptMap;
	}

	@Override
	public Apply refuse(long reviewerId, long projectId,long proposerId,long organizationId) throws Exception {
		boolean status = false;
		Apply p_apply  = null;
		try {
			Apply apply = getMyApplyByProjectIdAndProposerId(reviewerId, projectId, ProjectStatus.Project_Apply_CheckPending.getKey(), proposerId,organizationId);
			if (null != apply) {
				apply.setStatus(ProjectStatus.Project_Apply_Fail.getKey());
				apply.setReviewTime(new Timestamp(System.currentTimeMillis()));
				status = update(apply);
				if(status){
				   p_apply = buildApply(apply.getId());
				}				
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_apply;
	}

	@Override
	public List<Apply> getMyApplysByProjectId(long reviewerId,long projectId, Integer stauts) throws Exception {
		List<Apply> applies = null;
		List<Long> ids = null;
		try {
			if(null != stauts){
			   ids = getKeysByParams("apply_list_reviewerId_projectId_status", new Object[]{reviewerId,projectId,stauts});
			}
			else{
			   ids = getKeysByParams("apply_list_reviewerId_projectId", new Object[]{reviewerId,projectId});
			}
			if(null != ids&&ids.size()>0){
			   applies = buildApply(getEntityByIds(ids));			
			}
		} catch (Exception e) {
	        logger.error(e.getMessage(),e);
	        throw e;
		}
		return applies;
	}

	@Override
	public Apply getMyApplyByProjectIdAndProposerId(long reviewerId,long projectId, int stauts, long proposerId,long organizationId)	throws Exception {
		Apply apply = null;
		try {
			Long id = getMappingByParams("apply_map_reviewerId_projectId_status_proposerId_organizationId", new Object[]{reviewerId,projectId,stauts,proposerId,organizationId});
			if (null != id) {
				apply = buildApply(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return apply;
	}
	
	@Override
	public Apply create(long proposerId, 
						long organizationId,
						Timestamp applyTime, 
						long projectId, 
						long reviewerId)throws ApplyException,Exception {
		Apply p_apply = null;
		try {
			//检测项目是否存在
			Project p =  projectService.getEntityById(projectId);
			if(null != p){
			   Apply apply = getMyApplyByProjectIdAndProposerId(reviewerId, projectId, ProjectStatus.Project_Apply_CheckPending.getKey(), proposerId,organizationId);//判断申请是否申请过
			   if(null == apply){
				   List<Apply> applies = isUndertakeByProjectId(projectId, ProjectStatus.Project_Apply_Success.getKey());//判断项目是否被承接
				   if( null == applies ){
					   Apply a = new Apply();
					   a.setProposerId(proposerId);
					   if(organizationId != 0) a.setOrganizationId(organizationId);
					   a.setApplyTime(applyTime);
					   a.setProjectId(projectId);
					   a.setStatus(ProjectStatus.Project_Apply_CheckPending.getKey());
					   a.setReviewerId(reviewerId);
					   p_apply = save(a);	
				   }
				   else{
					   throw new ApplyException(ApiCodes.ProjectHasBeenToUndertake.getCode(), ApiCodes.ProjectHasBeenToUndertake.getDescription());
				   }
			   }
			   else{
				   throw new ApplyException(ApiCodes.ProjectApplyRepeat.getCode(), ApiCodes.ProjectApplyRepeat.getDescription());
			   }
			}
			else{
				throw new ApplyException(ApiCodes.ProjectIsNotExist.getCode(), ApiCodes.ProjectIsNotExist.getDescription());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_apply;
	}
	
	@Override
	protected Class<Apply> getEntity() {
		return Apply.class;
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
}
