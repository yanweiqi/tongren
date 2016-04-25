/*
 * 文件名： ResourcesServiceImpl.java
 * 创建日期： 2015年10月30日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.resource.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;
import com.ginkgocap.tongren.organization.document.service.DocumentCatalogueService;
import com.ginkgocap.tongren.organization.document.service.DocumentTagsService;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.organization.resources.service.LocalObjectService;
import com.ginkgocap.tongren.organization.resources.service.OrganizationObjectService;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;
import com.ginkgocap.tongren.project.manage.service.ProjectResourceAttachmentService;
import com.ginkgocap.tongren.resources.service.ResourcesService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;


 /**
 *  资源实现接口
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月30日
 */
@Service("resourcesService")
public class ResourcesServiceImpl extends AbstractCommonService<LocalObject> implements ResourcesService {

	private static  Logger logger = LoggerFactory.getLogger(ResourcesServiceImpl.class);
	
	@Autowired
	private LocalObjectService localObjectService;//本地资源接口
	@Autowired
	private OrganizationObjectService organizationObjectService;//组织资源
	@Autowired
	private ProjectResourceAttachmentService projectEnclosureService;//项目附件资源
	
	@Autowired
	private DocumentCatalogueService documentCatalogueService;
	
	@Autowired
	private DocumentTagsService documentTagsService;
	
	@Autowired
	private FileIndexService fileIndexService;//文件接口

	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.resourcesall.service.ResourcesService#addResourcesLocalAndOrg(com.ginkgocap.tongren.organization.resources.model.LocalObject)
	 * 返回本地资源和组织资源的id
	 */
	@Override
	public long[] addResourcesLocalAndOrg(LocalObject localObject)
			throws Exception {
		long[] cidArray= (long[])localObject.getExtend().get("catalogId");
		long[] tagArray=(long[]) localObject.getExtend().get("tagId");
		String recommendTag=(String)localObject.getExtend().get("recommendTag");
		
		OrganizationObject oob = new OrganizationObject();
		BeanUtils.copyProperties(oob, localObject);
		long[] rsid=new long[2];//返回本地资源和组织资源的id
		localObject=localObjectService.save(localObject);
		rsid[0]=localObject.getId();
		if(localObject.getOrganizationId()>0){
			if(cidArray==null||cidArray.length==0){//存放在未分组目录
				DocumentCatalogue dc=documentCatalogueService.getDefaultCatalog(localObject.getCreateId(), localObject.getOrganizationId());
				cidArray=new long[]{dc.getId()};
			}
			String code=documentCatalogueService.setDocumentToDirs(localObject.getCreateId(), localObject.getOrganizationId(), localObject.getId(), cidArray);
			logger.info("setDocumentToDirs  "+code);
			if(tagArray!=null&&tagArray.length>0){
				for(long tagId:tagArray){
					String tcode=documentTagsService.addSourceToTags(localObject.getCreateId(), tagId, localObject.getId(), localObject.getOrganizationId());
					logger.info("addSourceToTags : "+tcode);
				}
			}
			if(recommendTag!=null&&recommendTag.trim().length()>0){
				logger.info("begin add rec tags" +recommendTag );
				JSONArray recTags= JSON.parseArray(recommendTag);
				for(int i=0;i<recTags.size();i++){
					JSONObject jb=recTags.getJSONObject(i);
					documentTagsService.addSourceToRecTags(localObject.getCreateId(), jb.getString("tagName"), localObject.getId(), localObject.getOrganizationId());
				}
			}
		}else{
			logger.info("addResourcesLocalAndOrg orgId is:"+localObject.getOrganizationId());
		}
		oob=organizationObjectService.save(oob);
		rsid[1]=oob.getId();
		if(oob.getOrganizationId()>0){
			//组织资源的默认目录
			DocumentCatalogue dc=documentCatalogueService.getDefaultCatalog(ConfigService.ORG_DEF_USER_ID, oob.getOrganizationId());
			long catalOgId=dc.getId();
			String code=documentCatalogueService.setDocumentToDirs(ConfigService.ORG_DEF_USER_ID, oob.getOrganizationId(), oob.getId(), new long[]{catalOgId});
			logger.info("setDocumentToDirs  "+code);
		}
		return rsid;
	}
	/*
	 * 
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.resourcesall.service.ResourcesService#getOrgObject(long, long)
	 */
	@Override
	public List<OrganizationObject> getOrgObject(long organizationId,
			long projectId) throws Exception {
		List<Long> orgObjIds = new ArrayList<Long>();
		List<OrganizationObject> orgObjList = new ArrayList<OrganizationObject>();
		if(projectId ==0){//如果项目ID等于0说明查询所有组织的资源文件
			orgObjIds = organizationObjectService.getKeysByParams("ORGANIZATIONOBJECT_LIST_ORGANIZATIONID", organizationId);
		}else{
			orgObjIds = organizationObjectService.getKeysByParams("ORGANIZATIONOBJECT_LIST_ORGANIZATIONID_PROJECTID", new Object[]{organizationId,projectId});
		}
		if(orgObjIds != null && orgObjIds.size() >0)
			orgObjList = organizationObjectService.getEntityByIds(orgObjIds);
			
		return orgObjList;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.resourcesall.service.ResourcesService#getLocalObj(long, long)
	 */
	@Override
	public List<LocalObject> getLocalObj(long organizationId, long createId)
			throws Exception {
		List<Long> locatObjIds = new ArrayList<Long>();
		List<LocalObject> localObjectList = new ArrayList<LocalObject>();
		if(organizationId == 0){//如果组织ID为空，查询的是我的所有资源包含组织下的
			locatObjIds = localObjectService.getKeysByParams("LOCALOBJECT_LIST_CREATEID", createId);
		}else{
			locatObjIds = localObjectService.getKeysByParams("LOCALOBJECT_LIST_ORGANIZATIONID_CREATEID", new Object[]{organizationId,createId});
		}
		if(locatObjIds != null && locatObjIds.size() >0)
			localObjectList = localObjectService.getEntityByIds(locatObjIds);
		return localObjectList;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.resourcesall.service.ResourcesService#deleteResources(java.util.List, int)
	 */
	@Override
	public boolean deleteResources(List<Long> ids, int type) throws Exception {
		if(ids == null || ids.size() == 0)
			return false;
		if(type == 1){//组织资源
			for(Long id:ids){
				OrganizationObject org=organizationObjectService.getEntityById(id);
				if(org.getOrganizationId()>0){
					documentCatalogueService.delResourceOfCatalog(id, org.getOrganizationId(), ConfigService.ORG_DEF_USER_ID);
					documentTagsService.delAllTagsOfSource(id, org.getOrganizationId());
				}
				organizationObjectService.deleteEntityById(id);
				
			}
			//return organizationObjectService.deleteEntityByIds(ids);
			return true;
		}
		else if(type == 2){//我的资源
			for(Long id:ids){
				LocalObject lo=localObjectService.getEntityById(id);
				if(lo.getOrganizationId()>0){
					documentCatalogueService.delResourceOfCatalog(id, lo.getOrganizationId(), lo.getCreateId());
					documentTagsService.delAllTagsOfSource(id, lo.getOrganizationId());
				}
				localObjectService.deleteEntityById(id);
				
			}
			return true;
			//return localObjectService.deleteEntityByIds(ids);
		}
		else 
			return false;
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.resources.service.ResourcesService#saveProjectEnclosure(com.ginkgocap.tongren.project.manage.model.ProjectEnclosure)
	 */
	@Override
	public ResourceAttachment saveProjectEnclosure(
			ResourceAttachment projectEnclosure) throws Exception {
		return projectEnclosureService.save(projectEnclosure);
	}
	
	/**
	 * 获取资源对象信息，
	 * @param lid
	 * @param isWithDetail 是否包含详细信息
	 * @return
	 */
	public LocalObject getLocalObjectById(long lid,boolean isWithDetail){
		LocalObject lo= localObjectService.getEntityById(lid);
		if(lo==null){
			logger.warn("not found localobject by id "+lid);
			return null;
		}
		if(isWithDetail==true){
			lo.addExtend("createName", getLoginUserNameById(lo.getCreateId()));
			List<FileIndex> flist=fileIndexService.selectByTaskId(lo.getTaskId(), "1");
			if(flist!=null&&flist.size()>0){
				FileIndex s = (FileIndex) flist.get(0);
				lo.addExtend("titleName", s.getFileTitle());
				if(StringUtils.hasText(lo.getTitle())==false){
					lo.setTitle(s.getFileTitle());
				}
				lo.addExtend("fileSize", s.getFileSize());
				lo.addExtend("path",s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath()+ "/"+s.getFileTitle());
			}else{
				logger.info("not found file info by task id "+lo.getTaskId());
			}
		}
		return lo;
	}
	
	/**
	 * 获取组织资源对象，
	 * @param lid
	 * @param isWithDetail 是否包含详细信息
	 * @return
	 */
	public OrganizationObject getOrgObjectById(long lid,boolean isWithDetail){
		OrganizationObject  lo= organizationObjectService.getEntityById(lid);
		if(lo==null){
			logger.info("not found localobject by id "+lid);
		}
		if(isWithDetail==true){
			lo.addExtend("createName", getLoginUserNameById(lo.getCreateId()));
			List<FileIndex> flist=fileIndexService.selectByTaskId(lo.getTaskId(), "1");
			if(flist!=null&&flist.size()>0){
				FileIndex s = (FileIndex) flist.get(0);
				lo.addExtend("titleName", s.getFileTitle());
				if(StringUtils.hasText(lo.getTitle())==false){
					lo.setTitle(s.getFileTitle());
				}
				lo.addExtend("fileSize", s.getFileSize());
				lo.addExtend("path",s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath()+ "/"+s.getFileTitle());
			}else{
				logger.info("not found file info by task id "+lo.getTaskId());
			}
		}
		return lo;
	}
	@Override
	protected Class<LocalObject> getEntity() {
		
		return LocalObject.class;
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
