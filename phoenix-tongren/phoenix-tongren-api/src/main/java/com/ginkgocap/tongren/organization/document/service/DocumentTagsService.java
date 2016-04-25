package com.ginkgocap.tongren.organization.document.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.document.model.DocumentTags;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;

/**
 * 标签service
 * @author hanxifa
 *
 */
public interface DocumentTagsService extends CommonService<DocumentTags>{

	/**
	 * 获取此用户下的所有文档标签
	 * @param userId
	 * @return
	 */
	public List<DocumentTags> getAllTags(long userId,long orgId);
	
	/**
	 * 增加一个标签
	 * @param tags
	 * @return
	 */
	public String addTags(DocumentTags tags,long orgId,int type);
	
	/**
	 * 删除一个标签，此标签的下的所有资源不再有个标签
	 * @param userId
	 * @param tagsId
	 * @return
	 */
	public String delTagsById(long userId,long tagsId);
	
	/**
	 * 增加一个资源到指定的标签下
	 * @param userId
	 * @param tagsId
	 * @param sourceId
	 * @return
	 */
	public String addSourceToTags(long userId,long tagsId,long sourceId,long orgId);
	
	/**
	 * 获取一个标签下的所有资源
	 * @param userId
	 * @param tagsId
	 * @return
	 */
	public Page<BasicBean>  getAllSoucesFromTags(Page<BasicBean> page);
	
	/**
	 * 在一个标签中删除指定的资源
	 * @param userId
	 * @param tagsId
	 * @param tagSourceId 对应TagSource.id
	 * @return
	 */
	public String delSourceFromTags(long userId,long tagSourceId);
	
	/**
	 * 获取资源关联的标签
	 * @param userId
	 * @param sourceId
	 * @return
	 */
	public List<DocumentTags> getAssociateTagsBySourceId(long userId,long sourceId,long orgId);
	/**
	 * 增加资源到金桐脑推荐的标签下
	 * @param userId
	 * @param tagName
	 * @param sourceId
	 * @param orgId
	 * @return
	 */
	public String addSourceToRecTags(long userId, String tagName, long sourceId,long orgId);

	/**
	 * 删除资源关联的标签
	 * @param sourceId
	 * @param orgId
	 */
	public void delAllTagsOfSource(long sourceId,long orgId);
	
}
