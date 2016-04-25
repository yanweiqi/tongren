package com.ginkgocap.tongren.project.manage.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.util.CacheStore;
import com.ginkgocap.tongren.common.util.CacheStoreManage;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;
import com.ginkgocap.tongren.project.manage.vo.ProjectVO;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;

/**
 * 
 * @author yanweiqi
 *
 */
@Service
public class PublishManageService {
	
	private static final Logger logger = LoggerFactory.getLogger(PublishManageService.class);
	
	private static final String BEAN_PAGE = "bean_page_publish";
	
	@Autowired
	private PublishService publishService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private CodeRegionService codeRegionService;
	@Autowired
	private ProjectResourceAttachmentService projectResourceAttachmentService;
	@Autowired
	private ApplyService applyService;
	
	/**
	 * 递归 查找所有子id集合 
	 * @param subCodeIds
	 * @param id
	 */
	private void getAllSubCodeIds(List<Long> subCodeIds,long id,int type){
		if(type ==1 ){
			List<CodeRegion> list = codeRegionService.selectByParentId(id);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					CodeRegion cr=list.get(i);
					subCodeIds.add(cr.getId());
					getAllSubCodeIds(subCodeIds,cr.getId(),type);
				}
			}
		}else{
			List<Code> levelList = codeService.selectChildNodeById(id);
			if(CollectionUtils.isNotEmpty(levelList)){
				for(int i = 0;i<levelList.size();i++){
					Code code = levelList.get(i);
					subCodeIds.add(code.getId());
					getAllSubCodeIds(subCodeIds, code.getId(),type);
				}
			}
		}
	}
	
	/**
	 * 根据区域、行业获取发布项目分页
	 * @param pageNumber
	 * @param area
	 * @param industry
	 * @return
	 */
	public Page<Project> discoverPagePublishProject(int pageNumber,String area,String industry) throws Exception{
		Page<Project> pageProject = new Page<Project>();
		List<Publish> publishList  = new ArrayList<Publish>();
		List<Project> projects = new ArrayList<Project>();
		CacheStoreManage.getInstance();
		CacheStore<List<Publish>> cacheStore = CacheStoreManage.getByKey(BEAN_PAGE);
		try {
			if(null == cacheStore || System.currentTimeMillis() >= cacheStore.getTimeOut()){
			   //查询未过期的项目
			   publishList = getPublishs();
			   cacheStore = new CacheStore<List<Publish>>();
			   cacheStore.setT(publishList);
			   cacheStore.setTimeOut(1000*60*5 + System.currentTimeMillis());
			   CacheStoreManage.put(BEAN_PAGE, cacheStore);
			}	
			CopyOnWriteArrayList<Publish> publishs = new CopyOnWriteArrayList<Publish>( cacheStore.getT());
			Collections.sort(new ArrayList<Publish>(publishs),new Comparator<Publish>(){
				@Override
				public int compare(Publish p1,Publish p2){
					return p1.getCreateTime().compareTo(p2.getCreateTime());
				}
			});
			List<Long> subCodeIds=new ArrayList<Long>();
			List<Long> industryIds=new ArrayList<Long>();
			if(StringUtils.isNotEmpty(area)){
				Long areaId=Long.parseLong(area);
				subCodeIds.add(areaId);
				getAllSubCodeIds(subCodeIds,areaId,1);
				Collections.sort(subCodeIds);
			}
			if(StringUtils.isNotBlank(industry)){
				Long industryId=Long.parseLong(industry);
				industryIds.add(industryId);
				getAllSubCodeIds(industryIds,industryId,2);
				Collections.sort(industryIds);
			}
			
			for (Publish publish : publishs) {
				
				if(StringUtils.isNotEmpty(area) && StringUtils.isNotEmpty(industry)){
					int areaIndex=Collections.binarySearch(subCodeIds, Long.parseLong(publish.getProject().getArea()));
					int industryIndex = Collections.binarySearch(industryIds, Long.parseLong(publish.getProject().getIndustry()));
//					if(areaIndex<0 || !publish.getProject().getIndustry().equals(industry)){
//						publishs.remove(publish);
//						continue;
//					}
//					if(industryIndex<0 || !publish.getProject().getIndustry().equals(area)){
//						publishs.remove(publish);
//						continue;
//					}
					if(areaIndex < 0 || industryIndex < 0){
						publishs.remove(publish);
						continue;
					}
					
				}
				else if(StringUtils.isEmpty(area) && StringUtils.isNotEmpty(industry)){
					int industryIndex = Collections.binarySearch(industryIds, Long.parseLong(publish.getProject().getIndustry()));
					if(industryIndex < 0){
						publishs.remove(publish);
						continue;
					}
				}
				else if(StringUtils.isNotEmpty(area) && StringUtils.isEmpty(industry)){
					int index=Collections.binarySearch(subCodeIds, Long.parseLong(publish.getProject().getArea()));
					if(index<0){
						publishs.remove(publish);
						continue;
					}
				}
				ProjectVO vo = new ProjectVO();
				Project p = publish.getProject();
				BeanUtils.copyProperties(vo, p);
				conversionParameters(p, vo);
				//根据项目id获取 文档集合
				List<ResourceAttachment> resourceAttachments = projectResourceAttachmentService.getListResourceAttachment(p.getId());
				if(null != resourceAttachments){
					vo.setResourceAttachments(resourceAttachments);
				}
				projects.add(vo);
			}
			
			if(projects.size() > 0){
			   pageProject.setSize(20);//初始页面大小
			   int fromIndex = pageProject.getSize() * (pageNumber-1);
			   if(fromIndex<projects.size()){
				   int toIndex   = fromIndex + pageProject.getSize();
				   if(toIndex>projects.size()){
					   toIndex=projects.size();
				   }
				   pageProject.setResult( projects.subList(fromIndex, toIndex));
			   }
			   pageProject.setIndex(pageNumber);
			   pageProject.setTotalCount(publishs.size());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return pageProject;
	}
	public List<Publish> getPublishs(){
		//查询未过期的项目
		List<Long> ids = publishService.getKeysByParams("publish_list_status", new Object[]{1});
		List<Publish> publishs = publishService.getEntityByIds(ids);
		for (Publish publish : publishs) {
			Project p = projectService.getEntityById(publish.getProjectId());
			publish.setProject(p);
		}
		return publishs;
	}
	/**
	 * 根据项目行业、地区  id 获取对应的名称
	 * @param project
	 * @param vo
	 */
	public void conversionParameters(Project project,ProjectVO vo){
		try{
		String industryName = projectService.convertIndustryId(Long.valueOf(project.getIndustry()));
		String areaName = projectService.convertAreaId(Long.valueOf(project.getArea()));
		vo.setIndustry(industryName);
		vo.setArea(areaName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
