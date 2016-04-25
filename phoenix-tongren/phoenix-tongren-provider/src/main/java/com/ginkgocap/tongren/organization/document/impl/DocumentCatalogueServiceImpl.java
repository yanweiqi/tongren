package com.ginkgocap.tongren.organization.document.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.parasol.directory.exception.DirectoryServiceException;
import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.exception.DirectoryTypeServiceException;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.model.DirectoryType;
import com.ginkgocap.parasol.directory.service.DirectoryService;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.parasol.directory.service.DirectoryTypeService;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;
import com.ginkgocap.tongren.organization.document.service.DocumentCatalogueService;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.service.OrganizationKnowledgeService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationPersonsimpleService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.resources.service.ResourcesService;

/**
 * 文档管理实现类
 * @author hanxifa
 *
 */
@Service("documentCatalogueService")
public class DocumentCatalogueServiceImpl extends AbstractCommonService<DocumentCatalogue> implements DocumentCatalogueService{

	
	private static final Logger logger = LoggerFactory.getLogger(DocumentCatalogueServiceImpl.class);
	
	private final static Long APP_ID=8l;//桐人项目的appid为8
	
	private final static int SOURCE_TYPE_ID=601;//
	
	private final static String NO_GROUP_DIR_NAME="未分组";//未分组目录的名称
	
	@Autowired
	private DirectoryService directoryService;
	
	@Autowired
	private DirectoryTypeService directoryTypeService;
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	
	@Autowired
	private DirectorySourceService directorySourceService;
	
	@Autowired
	private ResourcesService resourcesService;//资源接口
	
	@Autowired
	private OrganizationKnowledgeService organizationKnowledgeService;
	
	@Autowired
	private OrganizationPersonsimpleService organizationPersonsimpleService;
	
	
	
	/**
	 * 101创建成功  102创建失败 103 此组织下的文档根目录已经存在 104指定的父id不存在
	 */
	@Override
	public String addCatalog(DocumentCatalogue catalogue) {
		try {
			Directory directory = new Directory();
			directory.setAppId(APP_ID);
			directory.setName(catalogue.getName());
			directory.setUpdateAt(System.currentTimeMillis());
			directory.setUserId(catalogue.getUserId());
			Directory pdir = directoryService.getDirectory(APP_ID, catalogue.getUserId(), catalogue.getPid());
			if (pdir != null) {
				if(pdir.getName().equals(NO_GROUP_DIR_NAME)){
					logger.warn("未分组目录下不能创建目录");
					return "105";
				}
				directory.setTypeId(pdir.getTypeId());
			} else {
				logger.info("not found parent dir by id" + directory.getPid());
				return "104";
			}
			directory.setPid(catalogue.getPid());
			long dirId = directoryService.createDirectoryForChildren(catalogue.getPid(), directory);
			return "101_" + dirId;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("create directory failed！ " + catalogue, e);
			return "102";
		}

	}
	/**
	 * 根据组织id查询目录类型数据
	 * @param orgId
	 * @return
	 */
	private DirectoryType getDirectoryTypeByOrgId(long orgId){
		DirectoryType dt=null;
		try {
			dt=directoryTypeService.getDirectoryTypeByName(APP_ID, orgId+ "");
			if(dt==null){
				logger.info("not found DirectoryType by orgId:"+orgId);
			}
			return dt;
		} catch (DirectoryTypeServiceException e) {
			logger.error("query DirectoryType failed! by orgId: "+orgId,e);
		}
		return dt;
	}
	/**
	 * 查询某个用户在某个组织下的文档目录的顶层节点，并返回顶层节点下的所有一级子节点
	 * 如不存在根节点，则创建根节点，并且创建默认的未分组节点
	 */
	@Override
	public DocumentCatalogue getRootCatalogs(long userId, long organizationId) {
		DirectoryType dt=getDirectoryTypeByOrgId(organizationId);
		if(dt==null){
			logger.info("will create root Dir orgId:"+organizationId+",userId:"+userId);
			String rs=createRootDir(userId,organizationId);
			if(rs.equals("201")==false){
				return null;
			}
			dt=getDirectoryTypeByOrgId(organizationId);
		}
		
		try {
			List<Directory> list=directoryService.getDirectorysForRoot(APP_ID, userId, dt.getId());
			if(list==null ||list.size()==0){
				logger.info("will create root Dir orgid:"+organizationId+",userId:"+userId);
				String rs=createRootDir(userId,organizationId);
				if(rs.equals("201")==false){
					return null;
				}
				list=directoryService.getDirectorysForRoot(APP_ID, userId, dt.getId());
			}
			if(list!=null&&list.size()==1){
				DocumentCatalogue documentCatalogue=convertToCatalog(list.get(0));
				documentCatalogue.setOrganizationId(organizationId);
				documentCatalogue.setChildren(getCatalogsById(userId, documentCatalogue.getId()));
				return documentCatalogue;
			}else{
				if(list!=null &&list.size()>1){
					logger.warn("found more then one Directory by "+dt.getId()+",userId:"+userId);
				}else{
					logger.warn("not found Directory by "+dt.getId()+",userId:"+userId);
				}
			}
		} catch (DirectoryServiceException e) {
			logger.error("get DirectorysForRoot failed,organizationId:"+organizationId,e);
		}
		return null;
	}
	/**
	 * 
	 * @param userId
	 * @param organizationId
	 * @return 201 创建成功 202 组织不存在 203 组织下的根目录已经存在 204创建根目系统异常
	 */
	private String createRootDir(long userId, long organizationId) {
		try {
			Organization organzation = tongRenOrganizationService.getEntityById(organizationId);
			if (organzation == null) {
				logger.info("not found organization by id "+organizationId);
				//return "202";
			}
			
			DocumentCatalogue catalogue = new DocumentCatalogue();
			if(organzation!=null){
				catalogue.setName(organzation.getName());
			}else{
				catalogue.setName(organizationId+"");
			}
			catalogue.setOrganizationId(organizationId);
			catalogue.setUserId(userId);
			
			//创建目录分类
			long dirTypeId=0;
			DirectoryType dt=getDirectoryTypeByOrgId(catalogue.getOrganizationId());
			if(dt!=null){
				logger.info("组织下的文档根目录已经存在 "+catalogue.getOrganizationId());
				dirTypeId=dt.getId();
			}else{
				DirectoryType directoryType = new DirectoryType();
				directoryType.setAppId(APP_ID);
				directoryType.setName(catalogue.getOrganizationId() + "");
				directoryType.setUpdateAt(System.currentTimeMillis());
				dirTypeId = directoryTypeService.createDirectoryType(APP_ID, directoryType);
			}
			
			
			//创建根目录
			Directory directory = new Directory();
			directory.setAppId(APP_ID);
			directory.setName(catalogue.getName());
			directory.setUpdateAt(System.currentTimeMillis());
			directory.setUserId(catalogue.getUserId());
			directory.setTypeId(dirTypeId);
			long  dirId=directoryService.createDirectoryForRoot(dirTypeId, directory);
			//创建未命名目录
			Directory noGroupDir = new Directory();
			noGroupDir.setAppId(APP_ID);
			noGroupDir.setName(NO_GROUP_DIR_NAME);
			noGroupDir.setUpdateAt(System.currentTimeMillis());
			noGroupDir.setUserId(catalogue.getUserId());
			noGroupDir.setTypeId(dirTypeId);
			noGroupDir.setPid(dirId);
			long defaultDirId=directoryService.createDirectoryForChildren(dirId, noGroupDir);
			if(organzation!=null){
				String copyRs=copyAllOrganizatonDocToDefaultDir(userId,organizationId,defaultDirId);
				logger.info("copyAllOrganizatonDocToDefaultDir "+copyRs);
			}
			 
		} catch (Exception e) {
			logger.error("create root dir failed! userId:"+userId+",orgId:"+organizationId,e);
			return "204";
		}
		return "201";
	}
	
	/**
	 * 我的组织下的所有资源到 默认目录
	 * @param organizationId
	 * @param defaultDirId
	 * @return 301 拷贝成功,302系统异常
	 */
	private String copyAllOrganizatonDocToDefaultDir(long userId,long organizationId,long defaultDirId){
		try {
			if(userId==ConfigService.ORG_DEF_USER_ID){
				List<OrganizationObject> orgObjs=resourcesService.getOrgObject(organizationId, 0);//查询所有组织资源
				if(orgObjs!=null&&orgObjs.size()>0){
					for(OrganizationObject orgObj:orgObjs){
						addLocalObjectToDirectory(defaultDirId,userId,orgObj.getId(),orgObj.getTaskId());
					}
				}else{
					logger.error("not found OrgObject "+userId+",orgId:"+organizationId);
				}
			}else{
				List<LocalObject> localObjS = resourcesService.getLocalObj(organizationId, userId);
				if(localObjS!=null&&localObjS.size()>0){
					for(LocalObject lo:localObjS){
						addLocalObjectToDirectory(defaultDirId,userId,lo.getId(),lo.getTaskId());
					}
				}else{
					logger.error("not found LocalObject "+userId+",orgId:"+organizationId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("copy all doc to dir failed!"+userId+",orgId:"+organizationId+",defaultDirId:"+defaultDirId,e);
			return "302";
		}
		return "301";
	}
	
	/** 将资源增加到某个目录下
	 * @param dirId
	 * @param lo
	 * @throws DirectorySourceServiceException
	 */
	private void addLocalObjectToDirectory(long dirId,long userId,long sourceId,String sourceUrl) throws DirectorySourceServiceException{
		DirectorySource directorySource =new DirectorySource(); 
		directorySource.setAppId(APP_ID);
		directorySource.setCreateAt(System.currentTimeMillis());
		directorySource.setDirectoryId(dirId);
		directorySource.setSourceId(sourceId);
		directorySource.setUserId(userId);
		directorySource.setSourceUrl(sourceUrl);//lo.getTaskId()
		directorySource.setSourceType(SOURCE_TYPE_ID);//文档固定为601
		directorySourceService.createDirectorySources(directorySource);
	}
	/**
	 * 获取指定目录下的子目录，如果没有任何子目录，则返回空
	 */
	@Override
	public List<DocumentCatalogue> getCatalogsById(long userId,long catalogId) {
		
		try {
			List<Directory> list= directoryService.getDirectorysByParentId(APP_ID, userId, catalogId);
			if(list!=null&&list.size()>0){
				List<DocumentCatalogue> rsList=new ArrayList<DocumentCatalogue>(list.size());
				for(Directory dir:list){
					rsList.add(convertToCatalog(dir));
				}
				return rsList;
			}else{
				logger.info("not found sub directorys by pid:"+catalogId+",useId:"+userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get sub directorys failed! userId:"+userId+",pid:"+catalogId,e);
		}
		return new ArrayList<DocumentCatalogue>();
	}

	/**
	 * 把Directory对象转换为DocumentCatalogue 对象
	 * @param dir
	 * @return
	 */
	private DocumentCatalogue convertToCatalog(Directory dir){
		DocumentCatalogue dc=new DocumentCatalogue();
		dc.setId(dir.getId());
		dc.setName(dir.getName());
		dc.setPid(dir.getPid());
		dc.setUserId(dir.getUserId());
		return dc;
	}
	
	/**
	 * 根据用户id和目录id查询该目录下的资源
	 */
	@Override
	public List<BasicBean> getDocumentsById(long userId,long catalogId) {
		try {
			DirectoryType dt=null;
			if(userId==ConfigService.ORG_DEF_USER_ID){//通过DirectoryType判断此目录是人脉的目录还是知识的目录
				Directory dir=directoryService.getDirectory(APP_ID, userId, catalogId);
				dt=directoryTypeService.getDirectoryType(APP_ID, dir.getTypeId());
			}
			
			List<DirectorySource> list=directorySourceService.getDirectorySourcesByDirectoryId(APP_ID, userId, catalogId);
			if(list!=null&&list.size()>0){
				 List<BasicBean> rsList=new ArrayList<BasicBean>(list.size());
				for(DirectorySource ds:list){
					BasicBean lo=null;
					if(dt!=null&&dt.getName().startsWith("30")){//人脉
						lo=organizationPersonsimpleService.getEntityById(ds.getSourceId());
					}else  if(dt!=null&&dt.getName().startsWith("40")){//人脉
						lo=organizationKnowledgeService.getEntityById(ds.getSourceId());
					}else{
						if(userId==ConfigService.ORG_DEF_USER_ID){
							lo=resourcesService.getOrgObjectById(ds.getSourceId(),true);
						}else{
							lo=resourcesService.getLocalObjectById(ds.getSourceId(),true);
						}
						
					}
					if(lo!=null){
						lo.addExtend("directorySourceId", ds.getId());
						rsList.add(lo);
					}else{
						logger.warn("not found LocalObject by id "+ds.getSourceId());
					}
				}
				return rsList;
			}else{
				logger.info("not found DirectorySource by id "+catalogId+",userId:"+userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("get documents failed! userId:"+userId+",catalogId:"+catalogId,e);
		}
		return new ArrayList<BasicBean>();
	}

	/**
	 * 更新目录名称
	 * 401 更新成功,402 该目录不存在，403 系统异常  404目录已经存在
	 */
	@Override
	public String updateCatalogueName(long userId,long catalogId,String name) {
		try {
			Directory dir=directoryService.getDirectory(APP_ID, userId, catalogId);
			if(dir!=null){
				dir.setName(name);
				directoryService.updateDirectory(APP_ID, userId, dir);
				logger.debug("update directory sucess! "+userId+","+catalogId+","+name);
			}else{
				logger.debug("dir is not exist! "+userId+","+catalogId+","+name);
				return "402";
			}
		}catch(DirectoryServiceException dse){
			logger.error("update directory failed! "+userId+","+catalogId+","+name,dse);
			String str=dse.getMessage();
			if(str.contains("already")&&str.contains("exists")){
				return "404" ;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("update directory failed! "+userId+","+catalogId+","+name,e);
			return "403";
		}
		return "401";
	}
	/**
	 * 删除目录
	 * 目录删除后，此目录下的资源将被移动到未分组目录下
	 * @return 501 删除目录成功，502 删除失败
	 */
	@Override
	public String deleteCatalogue(long userId,long catalogId) {
		Directory rootDir;
		try {
			rootDir = directoryService.getDirectory(APP_ID, userId, catalogId);
			if(rootDir!=null){
				deleteCatalogue(rootDir,getDefaultDir(rootDir));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("del Catalogue failed! "+userId+",catalogId:"+catalogId,e);
			return "502";
		}
		
		return "501";
	}

	private void deleteCatalogue(Directory dir,Directory defaultDir) throws DirectoryServiceException, DirectorySourceServiceException{
		if(dir.getId()==defaultDir.getId()){
			logger.info("default dir is not alow del");
			return;
		}
		List<Directory> listDirs=directoryService.getDirectorysByParentId(APP_ID, dir.getUserId(), dir.getId());
		if(listDirs!=null&&listDirs.size()>0){
			for(Directory cdir:listDirs){
				deleteCatalogue(cdir,defaultDir);
			}
		}else{//删除资源
			List<DirectorySource> dslist=directorySourceService.getDirectorySourcesByDirectoryId(APP_ID, dir.getUserId(), dir.getId());
			if(dslist!=null&&dslist.size()>0){
				for(DirectorySource dirsource:dslist){
					deleteResourceOfCatalogue(dirsource.getUserId(),dirsource.getId());
				}
			}
		}
		//删除目录
		directoryService.removeDirectory(APP_ID, dir.getUserId(), dir.getId());
	}
	
	/**
	 * 根据用户ie和组织id获取默认的目录
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public DocumentCatalogue getDefaultCatalog(long userId,long orgId){
		DocumentCatalogue catalog=getRootCatalogs(userId,orgId );
		List<DocumentCatalogue> list=catalog.getChildren();
		for(DocumentCatalogue dc:list){
			if(dc.getName().equals(NO_GROUP_DIR_NAME)){
				return dc;
			}
		}
		return null;
	}
	/**
	 * 根据任意一个目录，获取对应的未分组目录
	 * @param dir
	 * @return
	 * @throws DirectoryServiceException
	 */
	private Directory getDefaultDir(Directory dir) throws DirectoryServiceException{
			List<Directory> list=directoryService.getDirectorysForRoot(APP_ID, dir.getUserId(), dir.getTypeId());
			if(list!=null&&list.size()>0){
				List<Directory> listChild=directoryService.getDirectorysByParentId(APP_ID, dir.getUserId(), list.get(0).getId());
				for(Directory d:listChild){
					if(d.getName().equals(NO_GROUP_DIR_NAME)){
						return d;
					}
				}
			}
			return null;
	}
	/**
	 * 删除目录下的资源
	 * 资源被删除后，将被移动到未分组目录下
	 * @param resourceId 目录资源关系表的id 对应 DirectorySource.id
	 * @return 501 删除成功，502 删除失败 503 不存在对应的目录
	 */
	public String deleteResourceOfCatalogue(long userId, long diresourceId) {
		try {
			DirectorySource directorySource= directorySourceService.getDirectorySourceById(APP_ID, diresourceId);
			List<DirectorySource> listDir = directorySourceService.getDirectorySourcesBySourceId(userId, APP_ID, SOURCE_TYPE_ID, directorySource.getSourceId());
			if (listDir != null && listDir.size() > 0) {
				if(listDir.size()>1){//存在多个目录则直接删除
					directorySourceService.removeDirectorySources(APP_ID, userId, diresourceId);
				}else if(listDir.size()==1){
					DirectorySource ds =listDir.get(0);
					Directory cdir = directoryService.getDirectory(APP_ID, ds.getUserId(), ds.getDirectoryId());
					if (cdir.getName().equals(NO_GROUP_DIR_NAME)) {//只在一个目录，并且是未分组目录，直接移动到默认目录下
						Directory defaultDir=getDefaultDir(cdir);
						directorySourceService.moveDirectorySources(userId, APP_ID, defaultDir.getId(), new Long[]{diresourceId});
					}else{
						logger.info("delete has been cancel "+diresourceId+","+userId);
					}
				}
				
			} else {
				logger.warn("not found dir by " + userId + "," + diresourceId);
				return "503";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("delete resource failed! " + userId + "," + diresourceId);
			return "502";
		}
		return "501";
	}
	
	/**
	 * 在某个目录下的的资源文件中，选中一个文件，然后选择目录移动
	 * 获取用户在某个组织下的所有文档树，不包含资源
	 * @param sourceId 资源id 业务库中资源id 对应 DirectorySource.sourceId
	 */
	@Override
	public DocumentCatalogue getCatalogueTree(long userId, long organizationId,long sourceId) {
		DocumentCatalogue rootDc=getRootCatalogs(userId,organizationId);
		if(rootDc!=null){
			try {
				addChildCatalogure(rootDc,sourceId);
			} catch (DirectorySourceServiceException e) {
				e.printStackTrace();
				logger.error("getCatalogueTree failed! "+userId+",orgid:"+organizationId,e);
				return null;
			}
		}
		return rootDc;
	}
	/**
	 * 递归加载所有子目录
	 * @param catalogue
	 * @throws DirectorySourceServiceException 
	 */
	private void addChildCatalogure(DocumentCatalogue catalogue,long sourceId) throws DirectorySourceServiceException{
		List<DocumentCatalogue>  childList=getCatalogsById(catalogue.getUserId(),catalogue.getId());
		if(sourceId>0){//判断资源id是否在指定的目录下
			DirectorySource dir=directorySourceService.getDirectorySource(catalogue.getUserId(), catalogue.getId(), APP_ID, SOURCE_TYPE_ID, sourceId);
			if(dir!=null){
				catalogue.addExtend("inDir", true);
			}else{
				catalogue.addExtend("inDir", false);
			}
		}
		if(childList!=null&&childList.size()>0){
			catalogue.setChildren(childList);
			for(DocumentCatalogue child:childList){
				addChildCatalogure(child,sourceId);
			}
		}
	}
	/**
	 * 删除一个资源和所有目录关联关系，删除我的资源情况下调用
	 * @param orgId
	 * @param resourceId
	 */
	public void delResourceOfCatalog(long resourceId,long orgId,long userId){
		try {
			 List<DirectorySource>  list=directorySourceService.getDirectorySourcesBySourceId(userId, APP_ID, SOURCE_TYPE_ID, resourceId);
			 for(DirectorySource ds:list){
				 directorySourceService.removeDirectorySources(APP_ID, userId, ds.getId());
			 }
		} catch (DirectorySourceServiceException e) {
			logger.error("delResourceOfCatalog failed! "+resourceId+","+orgId+","+userId,e);
			throw new RuntimeException("delResourceOfCatalog failed! "+resourceId+","+orgId+","+userId, e);
		}
	}
	/**
	 * 设置文档目录
	 * @return 601 设置成功，602 toDirIds参数为空 603 没有找到对应的资源id信息
	 */
	@Override
	public String setDocumentToDirs(long userId, long organizationId, long sourceId, long[] toDirIds) {
		if(toDirIds==null||toDirIds.length==0){
			return "602";
		}
		try {
			List<DirectorySource> listDir = directorySourceService.getDirectorySourcesBySourceId(userId, APP_ID, SOURCE_TYPE_ID, sourceId);
			//获取当前资源所在目录的id列表
			long[] currentDirIds=null;
			if(listDir!=null&&listDir.size()>0){
				currentDirIds=new long[listDir.size()];
				for(int i=0;i<listDir.size();i++){
					currentDirIds[i]=listDir.get(i).getDirectoryId();
				}
			}
			//需要删除的目录
			long[] delDirIds=subtractArray(currentDirIds,toDirIds);
			if(delDirIds!=null){
				for(long delid:delDirIds){
					DirectorySource directorySource= directorySourceService.getDirectorySource(userId, delid, APP_ID, SOURCE_TYPE_ID, sourceId);
					if(directorySource!=null){
						directorySourceService.removeDirectorySources(APP_ID, userId, directorySource.getId());
					}else{
						logger.warn("not found directorySource by "+userId+","+delid+","+sourceId);
					}
				}
			}
			long[] appendDirIds=subtractArray(toDirIds,currentDirIds);
			if(appendDirIds!=null&&appendDirIds.length>0){
				if(String.valueOf(organizationId).startsWith("30")){//人脉
					for(long appDirId:appendDirIds){
						addLocalObjectToDirectory(appDirId, userId, sourceId,"");
					}
				}else if(String.valueOf(organizationId).startsWith("40")){//知识
					for(long appDirId:appendDirIds){
						addLocalObjectToDirectory(appDirId, userId, sourceId,"");
					}
				}else{
					if(userId==ConfigService.ORG_DEF_USER_ID){
						OrganizationObject orgObject=resourcesService.getOrgObjectById(sourceId, false);
						if(orgObject!=null){
							for(long appDirId:appendDirIds){
								addLocalObjectToDirectory(appDirId, userId, orgObject.getId(),orgObject.getTaskId());
							}
						}
					}else{
						LocalObject lo=resourcesService.getLocalObjectById(sourceId,false);
						if(lo!=null){
							for(long appDirId:appendDirIds){
								addLocalObjectToDirectory(appDirId, userId, lo.getId(),lo.getTaskId());
							}
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("setDocumentToDirs failed! "+userId+","+organizationId+","+sourceId,e);
		}
		return "601";
	}
	
	/**
	 * 如果资源不在指定的目录下，则是设置目录，否则直接返回
	 * 防止如果重复设置默认目录，会把之前已经设置的目录覆盖掉
	 * @param userId
	 * @param organizationId
	 * @param sourceId
	 * @param toDirIds
	 * @return
	 */
	@Override
	public String setDocumentToDirsIfAbsent(long userId, long organizationId, long sourceId, long[] toDirIds) {
		try {
			List<DirectorySource> listDir = directorySourceService.getDirectorySourcesBySourceId(userId, APP_ID, SOURCE_TYPE_ID, sourceId);
			if(listDir==null||listDir.size()==0){
				logger.info("will setDocumentToDirs "+userId+","+organizationId+","+sourceId);
				setDocumentToDirs(userId, organizationId, sourceId, toDirIds);
				return "201";
			}else{
				logger.info("cancel setDocumentToDirs "+userId+","+organizationId+","+sourceId);
				return "202";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("setDocumentToDirsIfAbsent failed! "+organizationId+","+sourceId, e);
			throw new RuntimeException("setDocumentToDirsIfAbsent failed! "+organizationId+","+sourceId, e);
		}
	}
	/**
	 * 两个数组做减法
	 * @param minuendArray 被减数组
	 * @param subtrahendArray  减数数组
	 * @return 
	 */
	private long[] subtractArray(long[] minuendArray,long[] subtrahendArray){
		if(minuendArray==null){
			return null;
		}
		if(subtrahendArray==null||subtrahendArray.length==0){
			return minuendArray;
		}
		long[] rsArray=new long [minuendArray.length];
		int index=0;
		for(long data:minuendArray){
			boolean isin=false;
			for(long sdata:subtrahendArray){
				if(data==sdata){
					isin=true;
					break;
				}
			}
			if(!isin){
				rsArray[index++]=data;
			}
		}
		if(index>0){
			return Arrays.copyOf(rsArray, index);
		}else{
			return null;
		}
	}
	@Override
	protected Class<DocumentCatalogue> getEntity() {
		return DocumentCatalogue.class;
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
