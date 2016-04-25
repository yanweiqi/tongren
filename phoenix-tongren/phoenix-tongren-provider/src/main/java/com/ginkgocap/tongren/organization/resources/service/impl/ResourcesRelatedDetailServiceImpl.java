package com.ginkgocap.tongren.organization.resources.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.resources.exception.ResourceException;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelatedDetail;
import com.ginkgocap.tongren.organization.resources.service.ResourcesRelatedDetailService;

@Service("resourcesRelatedDetailService")
public class ResourcesRelatedDetailServiceImpl extends AbstractCommonService<ResourcesRelatedDetail> implements ResourcesRelatedDetailService {

	private static final Logger logger = LoggerFactory.getLogger(ResourcesRelatedDetailServiceImpl.class);
	/**
	 * 根据资源id 获取关联详情
	 */
	@Override
	public List<ResourcesRelatedDetail> getResourcesRelatedsByRelatedId(long relatedId) throws ResourceException {
		List<Long> ids=this.getKeysByParams("ResourcesRelatedDetail_list_resourcesRelatedId", new Object[]{relatedId});
		if(ids!=null&&ids.size()>0){
			 List<ResourcesRelatedDetail> list=getEntityByIds(ids);
			 for(ResourcesRelatedDetail rrd:list){
				 if(rrd.getRelatedType()==1){//查询人脉详情
					 
				 }else if(rrd.getRelatedType()==2){//组织详情
					 
				 }else if(rrd.getRelatedType()==3){//知识详情
					 
				 }else if(rrd.getRelatedType()==4){//事件详情
					 
				 }else{
					 logger.warn("invalid related type "+"id:"+rrd.getId()+",rtype:"+rrd.getRelatedType());
				 }
			 }
			 return list;
		}
		return null;
	}

	/**
	 * 根据关联主表的id删除关联详细信息
	 */
	@Override
	public void delResourcesRelatedDetail(long resourcesRelatedId) throws ResourceException {
		List<Long> ids=this.getKeysByParams("ResourcesRelatedDetail_list_resourcesRelatedId", new Object[]{resourcesRelatedId});
		if(ids!=null){
			for(Long id:ids){
				try{
					//getEntityById(id);
					deleteEntityById(id);
					logger.info("delete ResourcesRelatedDetail success! "+id);
				}catch(Exception e){
					logger.error("delete ResourcesRelatedDetail failed! "+id,e);
				}
			}
		}
		
	}
	@Override
	protected Class<ResourcesRelatedDetail> getEntity() {
		// TODO Auto-generated method stub
		return ResourcesRelatedDetail.class;
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
