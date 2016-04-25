/*
 * 文件名： ProjectEnclosureServiceImpl.java
 * 创建日期： 2015年10月29日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;
import com.ginkgocap.tongren.project.manage.service.ProjectResourceAttachmentService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;


 /**
 *  项目附件表接口实现
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月29日
 */
@Service("projectResourceAttachmentService")
public class ProjectResourceAttachmentServiceImpl  extends AbstractCommonService<ResourceAttachment> implements ProjectResourceAttachmentService{

	private static final Logger logger = LoggerFactory.getLogger(ProjectResourceAttachmentServiceImpl.class);	
	
	@Autowired
	private FileIndexService fileIndexService;
	
	@Override
	protected Class<ResourceAttachment> getEntity() {
		return ResourceAttachment.class;
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
	 * @see com.ginkgocap.tongren.project.manage.service.ProjectResourceAttachmentService#insertProjectAttachment(long, java.util.List, long)
	 */
	@Override
	public List<ResourceAttachment> insertProjectAttachment(long projectId, List<String> taskids,long createrId) {
		ResourceAttachment p_resourceAttachment = new ResourceAttachment();
		List<ResourceAttachment> returnList = new ArrayList<ResourceAttachment>();
		if(taskids == null || taskids.size() == 0){
			logger.info("Without success, the reason is taskids NULL");
			return returnList;
		}
		try {
			for (String string : taskids) {
				ResourceAttachment resourceAttachment = new ResourceAttachment();
				resourceAttachment.setCreaterId(createrId);
				resourceAttachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
				resourceAttachment.setProjectId(projectId);
				resourceAttachment.setTaskId(string);
				resourceAttachment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				p_resourceAttachment = save(resourceAttachment);
				returnList.add(p_resourceAttachment);
			}
		} catch (Exception e) {
			logger.error("insertProjectAttachment is error",e.getMessage());
			if(returnList.size() > 0){
				for (ResourceAttachment resourceAttachment : returnList) {
					deleteEntityById(resourceAttachment.getId());
				}
			}
		}
		return returnList;
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.project.manage.service.ProjectResourceAttachmentService#getListResourceAttachment(long)
	 */
	@Override
	public List<ResourceAttachment> getListResourceAttachment(long projectId)
			throws Exception {
		List<ResourceAttachment> returnList = new ArrayList<ResourceAttachment>();
		List<Long> ids = getKeysByParams("ResourceAttachment_List_ProjectId", projectId);
		if(ids == null || ids.size() == 0){
			return null;
		}
		List<ResourceAttachment> listA = getEntityByIds(ids);
		for (ResourceAttachment resourceAttachment : listA) {
			FileIndex findex= getFileIndexByTaskId(resourceAttachment.getTaskId());
			resourceAttachment.setFileIndex(findex);
			returnList.add(resourceAttachment);
		}
		return returnList;
	}

}
