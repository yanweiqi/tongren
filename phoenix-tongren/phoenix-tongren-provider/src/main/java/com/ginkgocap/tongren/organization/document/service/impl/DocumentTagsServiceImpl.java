package com.ginkgocap.tongren.organization.document.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.parasol.tags.exception.TagServiceException;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.document.model.DocumentTags;
import com.ginkgocap.tongren.organization.document.service.DocumentTagsService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationKnowledgeService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationPersonsimpleService;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.resources.service.ResourcesService;

@Service("documentTagsService")
public class DocumentTagsServiceImpl extends AbstractCommonService<DocumentTags> implements DocumentTagsService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentTagsServiceImpl.class);

	private final static Long APP_ID = 8l;// 桐人项目的appid为8
	
	private final static String GINTONG_REC_TAG_PREFIX = "G_";// 金桐脑推荐的标签的前缀为G_
	private final static String TONGREN_TAG_PREFIX = "T_";// 桐人的标签的前缀为T_

	private final static int TAG_SOURCE_ID = 602;// 文档标签的typeid固定为602

	@Autowired
	private TagService tagService;

	@Autowired
	private TagSourceService tagSourceService;
	
	
	@Autowired
	private ResourcesService resourcesService;//资源接口
	
	@Autowired
	private OrganizationKnowledgeService organizationKnowledgeService;
	
	@Autowired
	private OrganizationPersonsimpleService organizationPersonsimpleService;
	

	/**
	 * 获取一个用户下的所有文档标签
	 */
	@Override
	public List<DocumentTags> getAllTags(long userId,long orgId) {
		try {
			List<Tag> allTags = tagService.getTagsByUserIdAppidTagType(userId, APP_ID, orgId);
			if (allTags != null && allTags.size() > 0) {
				List<DocumentTags> list = new ArrayList<DocumentTags>(allTags.size());
				for (Tag t : allTags) {
					DocumentTags dt = new DocumentTags();
					dt.setId(t.getId());
					String[] array=getTagNameAndType(t.getTagName());
					dt.addExtend("type", array[0]);					
					dt.setName(array[1]);
					dt.setUserId(t.getUserId());
					list.add(dt);
				}
				return list;
			} else {
				logger.info("not found tags by userId " + userId);
				return null;
			}
		} catch (TagServiceException e) {
			logger.error("getAllTags failed " + userId, e);
		}
		return null;
	}
	/**
	 * 将带有前缀的标签转化为类型和标签名称
	 * @param tagName
	 * @return
	 */
	private String[] getTagNameAndType(String tagName){
		String[] array=new String[2];
		if(tagName!=null&&tagName.startsWith(GINTONG_REC_TAG_PREFIX)){
			array[0]="G";
			array[1]= tagName.substring(2);
		}else if (tagName!=null&&tagName.startsWith(TONGREN_TAG_PREFIX)){
			array[0]="T";
			array[1]= tagName.substring(2);
		}else {//没有前缀则默认为桐人标签
			array[0]="T";
			array[1]= tagName;
		}
		return array;
	}
	/**
	 * 增加一个标签
	 * @param type 1 桐人的标签，2 金桐网推荐的标签
	 * @return 101 增加成功 102 增加失败
	 * 
	 */
	@Override
	public String addTags(DocumentTags tags,long orgId,int type) {
		Tag tag = new Tag();
		tag.setAppId(APP_ID);
		if(type==1){
			tag.setTagName(TONGREN_TAG_PREFIX+tags.getName());
		}else if(type==2){
			tag.setTagName(GINTONG_REC_TAG_PREFIX+tags.getName());
		}else{
			throw new RuntimeException("type must be in (1,2) "+type+" is invalid!");
		}
		tag.setTagType(orgId);
		tag.setUserId(tags.getUserId());
		try {
			logger.info("will save tag--> "+tag.getTagName());
			long tid = tagService.createTag(tag.getUserId(), tag);
			return "101_" + tid;
		} catch (TagServiceException e) {
			logger.error("add tag failed " + tags.getUserId() + "," + tags.getName(), e);
			return "102";
		}

	}

	/**
	 * 删除一个标签
	 * 
	 * @return 101 删除成功 202 删除 失败
	 */
	@Override
	public String delTagsById(long userId, long tagsId) {
		try {
			List<TagSource> list=tagSourceService.getTagSourcesByAppIdTagId(APP_ID, tagsId, 0, 1000);
			if(list!=null&&list.size()>0){
				for(TagSource ts:list){
					tagSourceService.removeTagSourcesByTagId(APP_ID, ts.getId());
				}
			}
			tagService.removeTag(userId, tagsId);
			
			logger.info("have del tag by " + userId + "," + tagsId);
			return "101";
		} catch (Exception e) {
			logger.error("delTagsById failed!  " + userId + "," + tagsId, e);
			return "202";
		}
	}

	/**
	 * 增加一个资源到指定的标签下
	 * 
	 * @return 201 增加成功 202目录id不存在对应的目录 203增加失败！
	 */
	@Override
	public String addSourceToTags(long userId, long tagsId, long sourceId,long orgId) {
		try {
			TagSource tagSource = new TagSource();
			tagSource.setAppId(APP_ID);
			tagSource.setCreateAt(System.currentTimeMillis());
			tagSource.setSourceId(sourceId);
			tagSource.setSourceType(orgId);
			tagSource.setTagId(tagsId);
			tagSource.setUserId(userId);
			Tag ctag = tagService.getTag(userId, tagsId);
			if (ctag != null) {
				tagSource.setTagName(ctag.getTagName());
			} else {
				return "202";
			}
			tagSourceService.createTagSource(tagSource);
			return "201";
		} catch (Exception e) {
			logger.error("addSourceToTags failed! " + userId + "," + tagsId + "," + sourceId, e);
			return "203";
		}
	}

	@Override
	public void delAllTagsOfSource(long sourceId,long orgId) {
		try {
			logger.info("will delete tag source "+orgId+","+sourceId);
			List<TagSource> listTagSources= tagSourceService.getTagSourcesByAppIdSourceIdSourceType(APP_ID, sourceId, orgId);
			for(TagSource ts:listTagSources){
				tagSourceService.removeTagSource(APP_ID, ts.getUserId(), ts.getId());
			}
			logger.info("delete tag source success! "+orgId+","+sourceId);
		} catch (Exception e) {
			logger.error("delete tag source failed! "+orgId+",sourceId"+sourceId,e);
		}
	}
	/**
	 * 增加资源到金桐脑推荐的标签下
	 * @param userId
	 * @param tagsId
	 * @param sourceId
	 * @param orgId
	 * @return
	 */
	@Override
	public String addSourceToRecTags(long userId, String tagName, long sourceId,long orgId) {
		try {
			List<DocumentTags> listAllTags=getAllTags(userId,orgId);
			long tagId=0l;
			if(listAllTags!=null){
				for(DocumentTags dts:listAllTags){//金桐网推荐的标签前台传递的是name不是id，需要判断此标签是否已经存在
					if("G".equals(dts.getExtend().get("type"))&&dts.getName().equals(tagName)){
						tagId=dts.getId();
						break;
					}
				}
			}
			if(tagId==0){//不存在则创建
				DocumentTags tags=new DocumentTags();
				tags.setUserId(userId);
				tags.setName(tagName);
				String str=addTags(tags,orgId, 2) ;
				if(str.startsWith("101")){
					tagId=Long.parseLong(str.substring(4));
				}else{
					return "202";
				}
			}
			TagSource tagSource = new TagSource();
			tagSource.setAppId(APP_ID);
			tagSource.setCreateAt(System.currentTimeMillis());
			tagSource.setSourceId(sourceId);
			tagSource.setSourceType(orgId);
			tagSource.setTagId(tagId);
			tagSource.setUserId(userId);
			tagSource.setTagName(tagName);
			tagSourceService.createTagSource(tagSource);
			return "201";
		} catch (Exception e) {
			logger.error("addSourceToTags failed! " + userId + "," + tagName + "," + sourceId, e);
			return "203";
		}
	}
	/**
	 * 分页查找标签下的资源
	 */
	@Override
	public Page<BasicBean> getAllSoucesFromTags(Page<BasicBean> page) {
		Long tagId = (Long) page.getParam("tagsId");
		Long userId=(Long) page.getParam("userId");
		try {
			int count = tagSourceService.countTagSourcesByAppIdTagId(APP_ID, tagId);
			page.setTotalCount(count);
			List<TagSource> list = tagSourceService.getTagSourcesByAppIdTagId(APP_ID, tagId, page.getStart(), page.getSize());
			if (list != null && list.size() > 0) {
				List<BasicBean> loList=new ArrayList<BasicBean>();
				Tag tag=null;
				if(userId!=null&&ConfigService.ORG_DEF_USER_ID==userId.longValue()){//组织下的标签资源
					tag=tagService.getTag(userId, tagId);
				}
				
				for(TagSource ts:list){
					BasicBean basicBean=null;
					if(tag!=null&&String.valueOf(tag.getTagType()).startsWith("30")){//人脉
						basicBean=organizationPersonsimpleService.getEntityById(ts.getSourceId());
					}else  if(tag!=null&&String.valueOf(tag.getTagType()).startsWith("40")){//知识
						basicBean=organizationKnowledgeService.getEntityById(ts.getSourceId());
					}else {
						if(userId!=null&&ConfigService.ORG_DEF_USER_ID==userId.longValue()){//组织资源
							basicBean=resourcesService.getOrgObjectById(ts.getSourceId(),true);
						}else{//我的资源
							basicBean=resourcesService.getLocalObjectById(ts.getSourceId(),true);
						}
					}
					if(basicBean!=null){
						basicBean.addExtend("tagSourceId",ts.getId());
						String[] array=getTagNameAndType( ts.getTagName());
						basicBean.addExtend("tagName",array[1]);
						loList.add(basicBean);
					}
				}
				page.setResult(loList);
			}
		} catch (Exception e) {
			logger.info("query tag source failed! " + tagId, e);
		}
		return page;
	}

	/**
	 * 删除标签下的某一个资源
	 * tagSourceId 对应TagSource.id
	 * 401 删除成功 ，402 删除失败
	 */
	@Override
	public String delSourceFromTags(long userId, long tagSourceId) {
		try {
			tagSourceService.removeTagSource(APP_ID, userId, tagSourceId);
			return "401";
		} catch (TagSourceServiceException e) {
			logger.error("delete tagsource failed! "+userId+","+tagSourceId,e);
			return "402";
		}
	}
	/**
	 * 获取资源关联的标签
	 */
	@Override
	public List<DocumentTags> getAssociateTagsBySourceId(long userId, long sourceId,long orgId) {
		try {
			List<TagSource> listTagSources= tagSourceService.getTagSourcesByAppIdSourceIdSourceType(APP_ID, sourceId, orgId);
			if(listTagSources!=null&&listTagSources.size()>0){
				 List<DocumentTags> rslist=new ArrayList<DocumentTags>();
				for(TagSource ts:listTagSources){
					DocumentTags dt=new DocumentTags();
					String[] array=getTagNameAndType( ts.getTagName());
					dt.addExtend("type",array[0]);
					dt.setName(array[1]);
					dt.setId(ts.getTagId());
					rslist.add(dt);
				}
				return rslist;
			}
		} catch (TagSourceServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected Class<DocumentTags> getEntity() {
		// TODO Auto-generated method stub
		return DocumentTags.class;
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
