/*
 * 文件名： ProjectEnclosureService.java
 * 创建日期： 2015年10月29日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;


 /**
 *  项目附件表接口
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月29日
 */
public interface ProjectResourceAttachmentService extends CommonService<ResourceAttachment> {

	/**
	 * 功能描述： 插入项目附件资源列表        
	 *                                                       
	 * @param projectId 项目ID
	 * @param taskids 列表结合数据
	 * @param createId 创建人ID
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年12月7日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<ResourceAttachment> insertProjectAttachment(long projectId,List<String> taskids,long createrId);
	
	/**
	 * 功能描述：根据项目ID查询我的附件集合        
	 *                                                       
	 * @param projectId 项目ID
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年12月7日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<ResourceAttachment> getListResourceAttachment(long projectId) throws Exception;
}
