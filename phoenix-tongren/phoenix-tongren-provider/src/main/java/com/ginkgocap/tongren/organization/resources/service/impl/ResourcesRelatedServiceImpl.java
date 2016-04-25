package com.ginkgocap.tongren.organization.resources.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.resources.exception.ResourceException;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelated;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelatedDetail;
import com.ginkgocap.tongren.organization.resources.service.ResourcesRelatedDetailService;
import com.ginkgocap.tongren.organization.resources.service.ResourcesRelatedService;

@Service("resourcesRelatedService")
public class ResourcesRelatedServiceImpl  extends AbstractCommonService<ResourcesRelated> implements ResourcesRelatedService {

	protected static final Logger logger = LoggerFactory.getLogger(ResourcesRelatedServiceImpl.class);
	
	@Autowired
	private ResourcesRelatedDetailService resourcesRelatedDetailService;
	
	@Override
	public void saveResourcesRelatedInfo(ResourcesRelated resourcesRelated) throws ResourceException {
		logger.info("begin save relate info "+resourcesRelated.getRelateName());
		ResourcesRelated rrdb=getResourcesRelatedByName(resourcesRelated.getRelateName(),resourcesRelated.getUserId(),resourcesRelated.getResourceId());
		if(rrdb!=null){
			logger.warn(resourcesRelated.getRelateName()+":has exist");
			throw new ResourceException("关联名称已经存在");
		}
		ResourcesRelated related=this.save(resourcesRelated);
		List<ResourcesRelatedDetail> details=resourcesRelated.getDetail();
		
		for(ResourcesRelatedDetail rdetail:details){
			rdetail.setResourcesRelatedId(related.getId());
			rdetail.setRelatedType(related.getRelatedType());
			resourcesRelatedDetailService.save(rdetail);
		}
		
	}

	/**
	 * 根据资源id获取关联的信息
	 * type:资源类型 1 我的资源，2 组织资源
	 */
	@Override
	public List<ResourcesRelated> getResourcesRelatedByResourceId(long resourceId,int type,boolean isDetail) throws ResourceException {
		
		List<Long> ids = getKeysByParams("ResourcesRelated_list_resourceId", new Object[]{resourceId,type});
		if(ids!=null&&ids.size()>0){
			logger.info("found "+ids.size()+" ResourcesRelated by resourceId "+resourceId);
			List<ResourcesRelated> list= getEntityByIds(ids);
			if(isDetail){
				for(ResourcesRelated rrt:list){
					List<ResourcesRelatedDetail> details=resourcesRelatedDetailService.getResourcesRelatedsByRelatedId(rrt.getId());
					rrt.setDetail(details);
				}
			}
			return list;
		}else{
			logger.info("not found ResourcesRelated by resourceId "+resourceId);
			return new ArrayList<ResourcesRelated>();
		}
	}
	
	/**
	 * 根据关联名称查询关联详细信息
	 * @param relateName
	 * @return
	 */
	private ResourcesRelated getResourcesRelatedByName(String relateName,long userId,long resourceId){
		List<Long> ids = getKeysByParams("ResourcesRelated_list_relateName", new Object[]{relateName,userId,resourceId});
		if(ids!=null&&ids.size()>0){
			return getEntityById(ids.get(0));
		}
		return null;
	}

	/**
	 * 删除关联信息
	 * @param rrid 关联信息id
	 */
	@Override
	public void delResourcesRelated(long rrid) throws ResourceException {
		try {
			if (rrid <= 0) {
				throw new ResourceException("invalid rrid " + rrid);
			}
			ResourcesRelated resourcesRelated = getEntityById(rrid);
			if (resourcesRelated != null) {
				resourcesRelatedDetailService.delResourcesRelatedDetail(rrid);
				deleteEntityById(rrid);
			} else {
				logger.warn("not found ResourcesRelated " + rrid);
			}
		} catch (Exception e) {
			throw new ResourceException("del ResourcesRelated failed! "+rrid, e);
		}
	}

	@Override
	protected Class<ResourcesRelated> getEntity() {
		// TODO Auto-generated method stub
		return ResourcesRelated.class;
	}

	@Override
	public Map<String, Object> doWork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
