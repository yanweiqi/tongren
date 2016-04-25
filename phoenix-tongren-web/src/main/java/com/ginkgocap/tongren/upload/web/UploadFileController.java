/*
 * 文件名： UploadFileController.java
 * 创建日期： 2015年10月15日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.upload.web;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.upload.FtpApche;
import com.ginkgocap.tongren.common.upload.FtpUpload;
import com.ginkgocap.tongren.common.util.FileEncodeUtil;
import com.ginkgocap.tongren.common.utils.Doc2Html;
import com.ginkgocap.tongren.common.utils.FFmpegUtils;
import com.ginkgocap.tongren.common.utils.JsonUtils;
import com.ginkgocap.tongren.common.utils.MD5Util;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.UploadType;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.resources.exception.ResourceException;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.organization.resources.model.ResourcesRelated;
import com.ginkgocap.tongren.organization.resources.service.ResourcesRelatedService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.system.code.TaskCode;
import com.ginkgocap.tongren.resources.service.ResourcesService;
import com.ginkgocap.tongren.upload.util.ImageScale;
import com.ginkgocap.tongren.upload.vo.UploadImgVO;
import com.ginkgocap.tongren.upload.vo.UploadItem;
import com.ginkgocap.tongren.upload.vo.UploadObjVO;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.ImageUtils;
import com.ginkgocap.ywxt.util.MakePrimaryKey;
import com.ginkgocap.ywxt.util.MakeTaskId;


 /**
 *  上传业务
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月15日
 */
@Controller
@RequestMapping("/file")
public class UploadFileController extends BaseController {
	
	@Autowired
	private FileIndexService fileIndexService;
	@Autowired
	private ResourcesService resourcesService;
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;
	@Autowired
	private OperationService operationService;//操作接口
	
	@Autowired
	private ResourcesRelatedService resourcesRelatedService;//关联
	
	private final static Logger logger = LoggerFactory.getLogger(UploadFileController.class);
	
	private String userHome = System.getProperty("user.home");//本地路径
	
	private static long MAX_IMAGE_SIZE = 5242880L;
	
	private static long MAX_OBJ_SIZE = 20971520L;
	
    private int boxWidth = 140;

    private int boxHeight = 140;
	
    public UploadFileController() {
        File tempDir = new File(userHome + "/temp");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }
    /**
	 * 根据taskId获取url信息
	 * @param taskId
	 * @return
	 */
	private FileIndex getUrlByTaskId(String taskId) throws Exception{
		List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
		FileIndex s = new FileIndex();
		if(list!=null&&list.size()>0){
			s = (FileIndex) list.get(0);
		}
		return s;
	}
	
	/**
	 * 功能描述：   图片剪裁     
	 * @param cropX 选择的坐标
	 * @param cropY 选择的坐标
	 * @param cropWidth 选择的宽度
	 * @param cropHeight 选择的高度
	 * @param imageWidth 图片宽度
	 * @param imageHeight 图片高度                                                      
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月5日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/cutImg.json", method = RequestMethod.POST)
	public void cutImg(HttpServletRequest request, HttpServletResponse response){
		String[] paramsKey = {"taskId|R","cropX|R","cropY|R","cropWidth|R","cropHeight|R","imageWidth|R","imageHeight|R"};
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params=null;
		try {
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			User u = ri.getUser();
			String taskId=params.getParam("taskId");
			String cropX=params.getParam("cropX");
			String cropY=params.getParam("cropY");
			String cropWidth=params.getParam("cropWidth");
			String cropHeight=params.getParam("cropHeight");
			String imageWidth=params.getParam("imageWidth");
			String imageHeight=params.getParam("imageHeight");
			
			FileIndex fileIndex = getUrlByTaskId(taskId);
			if(fileIndex == null){
				notification.put("notifyCode", SysCode.SYS_ERR.getCode());
				notification.put("notifyMessage", SysCode.SYS_ERR);
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
				return;
			}
			UploadImgVO vo =cutFileProcessing(u,fileIndex, Float.parseFloat(cropX), Float.parseFloat(cropY), Float.parseFloat(cropWidth), Float.parseFloat(cropHeight), Float.parseFloat(imageWidth), Float.parseFloat(imageHeight));
			
			responseData.put("taskId", vo.getTaskId());
			responseData.put("imgFullpath", vo.getImgFullpath());
			renderResponseJson(
					response,
					params.getResponse(SysCode.SUCCESS,
							genResponseData(responseData, notification)));
			return;
			
		} catch (Exception e) {
			logger.error("get cutImg failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", e.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(responseData, notification)));
			return;
		}
	}
	/**
	 * 功能描述：  剪裁文件处理             
	 *                                                       
	 * @param taskId
	 * @param cropX 选择的坐标
	 * @param cropY 选择的坐标
	 * @param cropWidth 选择的宽度
	 * @param cropHeight 选择的高度
	 * @param imageWidth 图片宽度
	 * @param imageHeight 图片高度            
	 * @return 
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月5日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	private UploadImgVO  cutFileProcessing(User user,FileIndex fileIndex,float cropX,float cropY,float cropWidth,float cropHeight,float imageWidth,float imageHeight) throws Exception{
		FtpUpload uploader = new FtpUpload();
		String localPath= "";
		try {
			localPath = downFileObj(fileIndex.getFilePath(),fileIndex.getFileTitle());
			String srcPath=localPath+fileIndex.getFileTitle();
			int[] imageSize=getImageWidthAndHeight(srcPath);
			if(imageSize!=null){
				imageWidth=imageSize[0];
				imageHeight=imageSize[1];
			}
			logger.info("imageWidth is "+imageWidth+",imageHeight is "+imageHeight);
			if(cropX+cropWidth>imageWidth){
				logger.info("剪切超出图片宽度");
				throw new Exception("剪切超出图片宽度");
			}
			if(cropY+cropHeight>imageHeight){
				logger.info("剪切超出图片高度");
				throw new Exception("剪切超出图片高度");
			}
			
			// 截取图片的起点坐标
			Integer x1 = Math.round(cropX);
			Integer y1 = Math.round(cropY);
			// 图片截取的长和宽
			Integer width = Math.round(cropWidth);
			Integer height = Math.round(cropHeight);
			//String cutPath=localPath+"cut_"+fileIndex.getFileTitle();
			ImageUtils.scissor(x1, width, y1, height,srcPath ,srcPath);// 新路径
			logger.info("src path "+srcPath);
	        uploader.connect(FileInstance.FTP_IP.trim(), 21, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "");
	        File file = new File(srcPath);
	        FileInputStream in = new FileInputStream(file);
	        String path = uploader.uploadFile(UploadType.IMAGE, in, file.getName());
//	        boolean b= uploader.uploadFileProcessing(srcPath, newPath);
	        if(path!= null && !"".equals(path)){
	        	 //往file库下插入数据
            	fileIndex.setId(MakePrimaryKey.getPrimaryKey());
            	fileIndex.setFilePath(FilenameUtils.getFullPathNoEndSeparator(path));
            	fileIndex.setFileSize(file.length());
            	fileIndex.setCtime(DateFunc.getDate());
            	fileIndex.setAuthor(user.getId());
            	fileIndex.setAuthorName(user.getName());
            	fileIndex.setStatus(true);
            	String taskId = MakeTaskId.getTaskId();
            	fileIndex.setTaskId(taskId);
            	logger.info("insert fileIndex begin");
            	fileIndexService.insert(fileIndex);  
            	logger.info("insert fileIndex end");
            	
            	UploadImgVO vo = new UploadImgVO();
            	vo.setFileSize(file.length());
            	vo.setImgFullpath(FileInstance.FTP_WEB+FileInstance.FTP_URL.trim()+"/tongren"+path);
            	vo.setTaskId(taskId);
	        	return vo;
	        }else{
	        	throw new Exception("上传失败");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			new File(localPath+File.separator+fileIndex.getFileTitle()).delete();
			try{
				uploader.disconnect();
			}catch(Exception e){
				logger.info("disconnect "+FileInstance.FTP_URL.trim()+" failed!");
			}
		}
		
	}
	/**
	 * 功能描述：获取图片高度宽度         
	 *                                                       
	 * @param path
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月26日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public int[] getImageWidthAndHeight(String path){
		try {
			InputStream is = new FileInputStream(path);
			BufferedImage buff = ImageIO.read(is);
			int [] array={0,0};
			array[0]=buff.getWidth(); //得到图片的宽度
			array[1]=buff.getHeight(); //得到图片的高度
			is.close(); //关闭Stream
			return array;
		} catch (Exception e) {
			logger.warn("获取图片的宽度和高度失败！"+path);
			return null;
		}//通过文件名称读
	}
	/**
	 * 功能描述： 保存文件信息到本地资源和组织资源        
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月26日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/saveFileObjs.json")
	public void saveFileObjs(HttpServletRequest request, HttpServletResponse response){
		//recommendTag为金桐脑力推荐的标签
		String[] paramKey = {"taskIds|R","projectId|R","organizationId|R","organizationTaskId|R","catalogId","tagId","recommendTag"};
		
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			String taskId = params.getParam("taskIds");
			Long projectId = Long.parseLong(params.getParam("projectId"));
			Long organizationId = Long.parseLong(params.getParam("organizationId"));
			Long organizationTaskId = Long.parseLong(params.getParam("organizationTaskId"));
			
			long[] catalogArray =null;
			long[] tagIdArray =null;
			if(params.getParam("catalogId")!=null){
				String[] cls=params.getParam("catalogId").split(",");
				catalogArray=new long[cls.length];
				int index=0;
				for(String cid:cls){
					catalogArray[index++]=Long.parseLong(cid);
				}
			}
			if(params.getParam("tagId")!=null){
				String[] cls=params.getParam("tagId").split(",");
				tagIdArray=new long[cls.length];
				int index=0;
				for(String cid:cls){
					tagIdArray[index++]=Long.parseLong(cid);
				}
			}
			List<String> taskIds = JSON.parseArray(taskId, String.class);
			if(taskIds == null||taskIds.size()== 0){
				notification.put("notifyCode", SysCode.TASKID_LIST_ERR.getCode());
				notification.put("notifyMessage", SysCode.TASKID_LIST_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.TASKID_LIST_ERR, genResponseData(null, notification)));
				return;
			}
			for (String string : taskIds) {
				//插入本地我的资源和组织资源
		     	LocalObject localObject = new LocalObject();
		     	localObject.setCreateId(user.getId());
		     	localObject.setCreateTime(new Timestamp(System.currentTimeMillis()));
		     	localObject.setOrganizationId(organizationId);
		     	localObject.setProjectId(projectId);
		     	localObject.setTaskId(string);
		     	localObject.setTitle(getFileNameByTaskId(string));
		     	localObject.addExtend("catalogId", catalogArray);
		     	localObject.addExtend("tagId", tagIdArray);
		     	localObject.addExtend("recommendTag", params.getParam("recommendTag"));
		     	
		     	try {
		     		resourcesService.addResourcesLocalAndOrg(localObject);
				} catch (Exception e) {
					logger.error("addResourcesLocalAndOrg insert error taskId is 【"+string+"】");
				}
		     	try {
		     		Operation operation =new Operation();
	             	operation.setOperactionTime(new Timestamp(System.currentTimeMillis()));
	             	operation.setOperationCode(TaskCode.SUBDOC.getCode());
	             	operation.setOperationUid(user.getId());
	             	operation.setProjectId(projectId);
	             	operation.setRemark(string);
	             	operation.setOrganizationTaskId(organizationTaskId);
	             	operationService.createOperation(operation);
				} catch (Exception e) {
					logger.error("createOperation insert error taskId is 【"+string+"】");
				}
		     	
			}
			try {//保存资源后发送消息
				List<FileIndex> list=fileIndexService.selectByTaskId(taskIds.get(0), "1");
				String title ="";
				if(list!=null&&list.size()>0){
					FileIndex s = (FileIndex) list.get(0);
					title = s.getFileTitle() == null ? "【某某某文档】" : s.getFileTitle();
				}
				tongRenSendMessageService.sendSubDocumentMes(user.getId(), organizationId, title, projectId);
			} catch (Exception e) {
				logger.error("Failed to submit a document,taskId is 【"+taskIds.get(0) + "】");
			}
			
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(null, null)));
			return;
		} catch (Exception e) {
			logger.error("get saveFileObjs failed! param:"+request.getParameter("requestJson"),e);
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
     	
	}
	
	/**
	 * 功能描述： 保存文件信息到本地资源和组织资源        
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月26日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 * [{"relateName":"朋友","relatedType":1,"resourceId":3906224919937034,"detail":[{"relatedId":142375665021900020,"relatedType":1,"subType":1},{"relatedId":21352,"relatedType":1,"subType":2}]}]
	 */
	@RequestMapping(value = "/saveFileObjs_v3.json")
	public void saveFileObjsv3(HttpServletRequest request, HttpServletResponse response){
		//title文档标题
		//relatedInfo 关联信息
		String[] paramKey = {"taskIds|R","projectId|R","organizationId|R","organizationTaskId|R","catalogId","tagId","recommendTag","title|R","relatedInfo"};
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			String taskId = params.getParam("taskIds");
			Long projectId = Long.parseLong(params.getParam("projectId"));
			Long organizationId = Long.parseLong(params.getParam("organizationId"));
			Long organizationTaskId = Long.parseLong(params.getParam("organizationTaskId"));
			
			long[] catalogArray =null;
			long[] tagIdArray =null;
			if(params.getParam("catalogId")!=null){
				String[] cls=params.getParam("catalogId").split(",");
				catalogArray=new long[cls.length];
				int index=0;
				for(String cid:cls){
					catalogArray[index++]=Long.parseLong(cid);
				}
			}
			if(params.getParam("tagId")!=null){
				String[] cls=params.getParam("catalogId").split(",");
				tagIdArray=new long[cls.length];
				int index=0;
				for(String cid:cls){
					tagIdArray[index++]=Long.parseLong(cid);
				}
			}
			
			List<String> taskIds = JSON.parseArray(taskId, String.class);
			if(taskIds == null||taskIds.size()== 0){
				notification.put("notifyCode", SysCode.TASKID_LIST_ERR.getCode());
				notification.put("notifyMessage", SysCode.TASKID_LIST_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.TASKID_LIST_ERR, genResponseData(null, notification)));
				return;
			}
			for (String string : taskIds) {
				//插入本地我的资源和组织资源 
		     	LocalObject localObject = new LocalObject();
		     	localObject.setCreateId(user.getId());
		     	localObject.setCreateTime(new Timestamp(System.currentTimeMillis()));
		     	localObject.setOrganizationId(organizationId);
		     	localObject.setProjectId(projectId);
		     	localObject.setTaskId(string);
		     	localObject.setTitle(params.getParam("title"));
		     	localObject.addExtend("catalogId", catalogArray);
		     	localObject.addExtend("tagId", tagIdArray);
		     	
		     	try {
		     		long[] resouceId=resourcesService.addResourcesLocalAndOrg(localObject);
		     		//保存我的资源的关联信息
		     		saveRelatedInfo(resouceId[0],1,params.getParam("relatedInfo"),user.getId());
				} catch (Exception e) {
					logger.error("addResourcesLocalAndOrg insert error taskId is 【"+string+"】");
				}
		     	try {
		     		Operation operation =new Operation();
	             	operation.setOperactionTime(new Timestamp(System.currentTimeMillis()));
	             	operation.setOperationCode(TaskCode.SUBDOC.getCode());
	             	operation.setOperationUid(user.getId());
	             	operation.setProjectId(projectId);
	             	operation.setRemark(string);
	             	operation.setOrganizationTaskId(organizationTaskId);
	             	operationService.createOperation(operation);
				} catch (Exception e) {
					logger.error("createOperation insert error taskId is 【"+string+"】");
				}
		     	
			}
			try {//保存资源后发送消息
				List<FileIndex> list=fileIndexService.selectByTaskId(taskIds.get(0), "1");
				String title ="";
				if(list!=null&&list.size()>0){
					FileIndex s = (FileIndex) list.get(0);
					title = s.getFileTitle() == null ? "【某某某文档】" : s.getFileTitle();
				}
				tongRenSendMessageService.sendSubDocumentMes(user.getId(), organizationId, title, projectId);
			} catch (Exception e) {
				logger.error("Failed to submit a document,taskId is 【"+taskIds.get(0) + "】");
			}
			
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(null, null)));
			return;
		} catch (Exception e) {
			logger.error("get saveFileObjs failed! param:"+request.getParameter("requestJson"),e);
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
     	
	}
	
	/**
	 * 功能描述： 保存文件信息到本地资源和组织资源        
	 * 修改关联的资源                                                     
	 * @param request
	 * relatedInfo:[{"relateName":"朋友","relatedType":1,"resourceId":3906224919937034,"detail":[{"relatedId":142375665021900020,"relatedType":1,"subType":1},{"relatedId":21352,"relatedType":1,"subType":2}]}]
	 */
	@RequestMapping(value = "/updateRelatedInfo_v3.json")
	public void updateRelatedInfo(HttpServletRequest request, HttpServletResponse response){
		//title文档标题
		//relatedInfo 关联信息
		String[] paramKey = {"resourceId|R","relatedInfo|R"};
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		try {
			RequestInfo ri=validate(request,response,paramKey);
			if(ri == null){
				return;
			}
			params=ri.getParams();
			User user  =ri.getUser();
			long resourceId=Long.parseLong(params.getParam("resourceId"));
			List<ResourcesRelated> list=resourcesRelatedService.getResourcesRelatedByResourceId(resourceId,1,false);
			for(ResourcesRelated rrd:list){
				resourcesRelatedService.delResourcesRelated(rrd.getId());
			}
			saveRelatedInfo(resourceId,1,params.getParam("relatedInfo"),user.getId());
			
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(null, null)));
			return;
		} catch (Exception e) {
			logger.error("updateRelatedInfo failed! param:"+request.getParameter("requestJson"),e);
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
     	
	}
	/**
	 * @param resourceId 资源id
	 * @param type 资源类型，我的资源1 ，2 组织资源
	 * @param rinfo 关联数据，格式如：
	 * [{"relateName":"朋友","relatedType":1,"resourceId":3906224919937034,"detail":[{"relatedId":142375665021900020,"relatedType":1,"subType":1},{"relatedId":21352,"relatedType":1,"subType":2}]}]
	 * @throws ResourceException
	 */
	private void saveRelatedInfo(long resourceId,int type,String rinfo,long userId) throws ResourceException{
		if(StringUtils.isEmpty(rinfo)||resourceId<=0||type<=0){
			return ;
		}
		List<ResourcesRelated> list=JSON.parseArray(rinfo,ResourcesRelated.class);
		logger.info("related size is "+list.size());
		for(ResourcesRelated resourcesRelated:list){
			logger.info("begin save "+resourcesRelated.getRelateName());
			resourcesRelated.setCreateTime(new Timestamp(System.currentTimeMillis()));
			resourcesRelated.setResourceId(resourceId);
			resourcesRelated.setResourceType(type);
			resourcesRelated.setUserId(userId);
			//List<ResourcesRelatedDetail> detailList=resourcesRelated.getDetail();
			//resourcesRelated.setRelatedType(relatedType);
			resourcesRelatedService.saveResourcesRelatedInfo(resourcesRelated);
		}
	}
    /**
     * 功能描述：  下载文件      
     * @param taskId 文件taskId                                                      
     * @param request
     * @param response                                                                                                 
     * @author 林阳 [linyang@gintong.com]
     * @since 2015年11月3日
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     *
     */
	@RequestMapping(value = "/downFile.json")
    public void downFile(HttpServletRequest request, HttpServletResponse response){
    	String[] paramsKey = {"taskId|R"};
    	Map<String, Object> notification = new HashMap<String, Object>();
    	ParamInfo params=null;
    	String sid=request.getParameter("sessionID");
		if(sid!=null&&sid.trim().length()>0){
			request.setAttribute("sessionID_Param", sid);
		}
    	try {
    		RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			String taskId=params.getParam("taskId");
			FileIndex fileIndex = getUrlByTaskId(taskId);
			String path=downFileObj(fileIndex.getFilePath(),fileIndex.getFileTitle());
			if(path!=null){
				 OutputStream ros = response.getOutputStream();  
				    try {  
				    	response.reset();  
				    	response.setCharacterEncoding("UTF-8");
				    	logger.info(fileIndex.getFileTitle());
				    	response.setHeader("content-disposition", "attachment;filename=" +new String(fileIndex.getFileTitle().getBytes("UTF-8"),"ISO8859-1"));
				    	response.setContentType("application/octet-stream; charset=utf-8");  
				        ros.write(FileUtils.readFileToByteArray(new File(path+File.separator+fileIndex.getFileTitle())));  
				        ros.flush();  
				        return;
				    } finally {  
				        if (ros != null) {  
				        	ros.close();  
				        }  
				        new File(path+File.separator+fileIndex.getFileTitle()).delete();
				    }  
			}else{
				logger.info(" path is null "+params.getParams());
			}
            
		} catch (Exception e) {
			logger.error("get downFile failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
    }
    
	/**
	 * 获取文件详情
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getFileInfo.json")
	public void getFileInfo(HttpServletRequest request, HttpServletResponse response) {
		String[] paramsKey = { "resourceId|R", "type|R"};// type 1 我的资源，2 组织资源
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String resourceId = params.getParam("resourceId");
			String type=params.getParam("type");
			BasicBean resource=null;
			if ("1".equals(type)) {
				resource= resourcesService.getLocalObjectById(Long.parseLong(resourceId), true);
			}else if("2".equals(type)){
				resource = resourcesService.getOrgObjectById(Long.parseLong(resourceId), true);
			}else{
				logger.warn(type+" must be in (1,2)");
			}
			responseData.put("success", resource);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
		} catch (Exception e) {
			logger.error("getFileInfo failed! param:" + request.getParameter("requestJson"), e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	@RequestMapping(value = "/reviewFile.json")
    public void reviewFile(HttpServletRequest request, HttpServletResponse response){
    	String[] paramsKey = {"taskId|R"};
    	Map<String, Object> notification = new HashMap<String, Object>();
    	ParamInfo params=null;
    	try {
    		String sid=request.getParameter("sessionID");
    		if(sid!=null&&sid.trim().length()>0){
    			request.setAttribute("sessionID_Param", sid);
    		}
    		RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			String taskId=params.getParam("taskId");
			FileIndex fileIndex = getUrlByTaskId(taskId);
			String path=downFileObj(fileIndex.getFilePath(),fileIndex.getFileTitle());
			if(path!=null){
				 OutputStream ros = response.getOutputStream();
				 String fullPath=null;
				try {
					fullPath = path + fileIndex.getFileTitle();
					String ftitle = fileIndex.getFileTitle().toLowerCase();
					// 预览
					String[] allowReviewType = new String[] { "doc", "xls", "docx", "ppt", "pptx", "pdf", "bmp", "jpg", "gif", "png", "jpeg", "txt",
							"log", "sql", "xml" };
					if (isEndWithIn(ftitle, allowReviewType)) {
						if (isEndWithIn(ftitle, new String[] { "bmp", "jpg", "gif", "png", "jpeg", "pdf" })) {// 图片pdf预览
							response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileIndex.getFileTitle(), "UTF-8"));
							if (ftitle.endsWith("pdf")) {
								response.setContentType("application/pdf");
							} else {
								response.setContentType("image/jpeg ");
							}
							byte[] bs = FileUtils.readFileToByteArray(new File(fullPath));
							response.setContentLength(bs.length);
							ros.write(bs);
							ros.flush();
							return;
						} else if (isEndWithIn(ftitle, new String[] { "doc", "docx", "xls", "ppt", "ppt" })) {// word预览
							File onlineFile = new File(WebConstants.ONLINE_VIEW_PATH, fileIndex.getId());
							if (ftitle.charAt(ftitle.length() - 1) == 'x') {
								// 去掉扩展名x
								File file2 = new File(fullPath.substring(0, fullPath.length() - 1));
								new File(fullPath).renameTo(file2);
								fullPath = file2.getAbsolutePath();
							}
							if (onlineFile.exists() == false) {
								onlineFile.mkdirs();
								File htmlf = Doc2Html.convert(fullPath, onlineFile.getAbsolutePath(), String.valueOf(fileIndex.getId()));
								if (htmlf == null) {// 预览失败重定向大到失败页面
									onlineFile.delete();
									// warpMsg(SysCode.ERROR_CODE,"预览失败",params,response,responseData);
									String fname = URLEncoder.encode(fileIndex.getFileTitle(), "utf-8");
									response.sendRedirect(request.getContextPath() + "/onlinefiles/reviewfaild.jsp?fileName=" + fname);
									return;
								}
							}
							response.sendRedirect(request.getContextPath() + "/onlinefiles/" + fileIndex.getId() + "/" + fileIndex.getId() + ".html");
							return;
						}else if (isEndWithIn(ftitle, new String[] { "txt", "log", "sql", "xml" })) {// 文本文件预览
							String encode = FileEncodeUtil.getFileEncode(fullPath);
							logger.info(fileIndex.getFileTitle() + "  encode is " + encode);
							StringBuilder result = new StringBuilder();
							result.append("<html>");
							result.append("<head>");
							result.append("<title>文本预览:" + fileIndex.getFileTitle() + "</title>");
							result.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(encode).append("\">");
							result.append("</head>");
							result.append("<body>");
							BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath), Charset.forName(encode)));
							String line = bf.readLine();
							while (line != null) {
								for (int i = 0; i < line.length(); i++) {
									char ch = line.charAt(i);
									if (ch == 9) {
										result.append("&nbsp;&nbsp;");
									} else if (ch == 32) {
										result.append("&nbsp;");
									} else if (ch == '"') {
										result.append("&quot;");
									} else if (ch == '&') {
										result.append("&amp;");
									} else if (ch == '<') {
										result.append("&lt;");
									} else if (ch == '>') {
										result.append("&gt;");
									} else {
										result.append(ch);
									}
								}
								result.append("<br/>");
								result.append("\r\n");
								line = bf.readLine();
							}
							result.append("</body>");
							result.append("</html>");
							bf.close();
							response.setCharacterEncoding(encode);
							ros.write(result.toString().getBytes(encode));
							//response.getWriter().print();
							//response.getWriter().flush();
							//response.getWriter().close();
							return;
						} 
					} else {// 下载
						response.reset();
						response.setCharacterEncoding("UTF-8");
						response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileIndex.getFileTitle(), "UTF-8"));
						response.setContentType("application/octet-stream; charset=utf-8");
						ros.write(FileUtils.readFileToByteArray(new File(fullPath)));
						ros.flush();
					}
					return;
				} finally {  
				        if (ros != null) {  
				        	ros.close();  
				        }  
				        new File(fullPath).delete();
				    }  
			}else{
				logger.info(" path is null "+params.getParams());
			}
            
		} catch (Exception e) {
			logger.error("get downFile failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
    }
	
	/**
	 * 判断一个字符串是否已某些字符结尾
	 * @param str
	 * @param suffix
	 * @return
	 */
	private boolean isEndWithIn(String str,String[] suffix){
		if(str==null||suffix==null){
			return false;
		}
		 
		str=str.toLowerCase();
		for(String suf:suffix){
			if(str.endsWith(suf.toLowerCase())){
				return  true;
			}
		}
		return false;
	}
    /**
     * 功能描述：         
     *                                                       
     * @param filePath
     * @return                                                                                                 
     * @author 林阳 [linyang@gintong.com]
     * @throws Exception 
     * @since 2015年11月3日
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     *
     */
	private static String downFileObj(String filePath,String fileName) throws Exception{
    	String path = System.getProperty("user.dir") + File.separator;
    	logger.info("downFileObj path is " + path);
    	if(FtpApche.downFile(FileInstance.FTP_IP, FileInstance.FTP_PORT, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, filePath, fileName, path))
    		return path;
    	return null;
    }
	/**
	 * 功能描述：   创建项目上传附件    
	 *                                                       
	 * @param uploadItem
	 * @param request
	 * @param response
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月9日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/uploadAttachmentProject.json", method = RequestMethod.POST)
	@ResponseBody
	public Object uploadAttachmentProject(UploadItem uploadItem, HttpServletRequest request, HttpServletResponse response){
		ParamInfo info = new ParamInfo();
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
		if(uploadFiles == null || uploadFiles.size() == 0){//没添加附件的情况
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_EMPTY.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_EMPTY);
			return info.getResponse(SysCode.UPLOAD_FILE_IS_EMPTY,notification);
		}
		if(!checkObjFileSize(uploadFiles,MAX_OBJ_SIZE)){
			logger.error(SysCode.TOO_BIG_FILES.getMessage());
			notification.put("notifyCode", SysCode.TOO_BIG_FILES.getCode());
			notification.put("notifyMessage", SysCode.TOO_BIG_FILES);
			return info.getResponse(SysCode.TOO_BIG_FILES,notification);
		}
		if(uploadFiles.size() > 10){//附件最多10个
			notification.put("notifyCode", SysCode.TOO_MANY_FILES.getCode());
			notification.put("notifyMessage", SysCode.TOO_MANY_FILES);
			return info.getResponse(SysCode.TOO_MANY_FILES,notification);
		}
		User user = getUser(response, request);//获得user方法
		if(user == null){
			notification.put("notifyCode", SysCode.SECURITY_ERR.getCode());
			notification.put("notifyMessage", SysCode.SECURITY_ERR);
			return info.getResponse(SysCode.SECURITY_ERR,notification);
		}
		List<UploadObjVO> taskIDList = new ArrayList<UploadObjVO>();
		try {
			taskIDList = uploadAttachmentProject(uploadItem, user);
			if(taskIDList == null || taskIDList.size() == 0){
				notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
    			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR);
                return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
			}else{
				
				responseData.put("fileObjects", taskIDList);
        		responseData.put("total", taskIDList.size());
        		responseData = genResponseData(responseData, null);
        		return info.getResponse(SysCode.SUCCESS, responseData);
			}
		} catch (Exception e) {
			logger.error("上传单个文件出错:" + e.getMessage(), e);
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR);
            return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
		}
	}
	/**
	 * 功能描述：  上传项目附件 
	 *                                                       
	 * @param uploadItem
	 * @param user
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月30日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<UploadObjVO> uploadAttachmentProject(UploadItem uploadItem,User user)
            throws Exception{
		List<UploadObjVO> list = new ArrayList<UploadObjVO>();
        if (uploadItem == null)
            return null;
        List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
        if (uploadFiles != null) {
            FtpUpload uploader = new FtpUpload();
            uploader.connect(FileInstance.FTP_IP.trim(), 21, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "");
            try {
            	 CommonsMultipartFile file = null;
                 for (int i = 0, n = uploadFiles.size(); i < n; i++) {
                     file = uploadFiles.get(i);
                     if (!file.isEmpty()) {
                     	String fileName =file.getOriginalFilename();
                         String path = uploader.uploadFile(UploadType.OBJECT, file.getInputStream(),fileName);
                         //往file库下插入数据
                         FileIndex fileIndex =new FileIndex();//文件存储对象
                     	fileIndex.setId(MakePrimaryKey.getPrimaryKey());
                     	fileIndex.setFilePath(FilenameUtils.getFullPathNoEndSeparator(path));
                     	fileIndex.setFileTitle(FilenameUtils.getName(fileName));
                     	fileIndex.setFileSize(file.getSize());
                     	fileIndex.setFileType(UploadType.OBJECT.getCode());
                     	fileIndex.setCtime(DateFunc.getDate());
                     	fileIndex.setAuthor(user.getId());
                     	fileIndex.setAuthorName(user.getName());
                     	fileIndex.setStatus(true);
                     	String taskId = MakeTaskId.getTaskId();
                     	fileIndex.setTaskId(taskId);
                     	logger.info("insert fileIndex begin");
                     	fileIndexService.insert(fileIndex);  
                     	logger.info("insert fileIndex end");
                     	//项目附件资源
//                     	ProjectEnclosure projectEnclosure = new ProjectEnclosure();
//                     	projectEnclosure.setCreateId(user.getId());
//                     	projectEnclosure.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                     	projectEnclosure.setProjectId(projectId);
//                     	projectEnclosure.setTaskId(taskId);
//                     	resourcesService.saveProjectEnclosure(projectEnclosure);
                     	//封装页面VO对象 
                     	//封装页面VO对象 
                     	UploadObjVO uploadObjVO = new UploadObjVO();
                     	uploadObjVO.setFileSize(file.getSize());
                     	uploadObjVO.setImgFullpath(FileInstance.FTP_URL.trim()+"/tongren"+path);
                     	uploadObjVO.setTaskId(taskId);
                     	uploadObjVO.setFileName(FilenameUtils.getName(fileName));
                     	list.add(uploadObjVO);
                     }
                 }
			} catch (Exception e) {
				throw e;
				
			}finally{
				uploader.disconnect();
			}
        }
        return list;
	}
    /**
     * 功能描述：  项目中上传提交文件       
     * @param uploadItem
     * @param request
     * @param response
     * @return                                                                                                 
     * @author 林阳 [linyang@gintong.com]
     * @since 2015年10月30日
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     *
     */
    @RequestMapping(value = "/uploadObjProject.json", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadObjProject(UploadItem uploadItem, HttpServletRequest request, HttpServletResponse response){
        ParamInfo info = new ParamInfo();
        Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
		if(uploadFiles == null || uploadFiles.size() == 0){//没添加附件的情况
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_EMPTY.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_EMPTY.getMessage());
			return info.getResponse(SysCode.UPLOAD_FILE_IS_EMPTY,notification);
		}
		if(!checkObjFileSize(uploadFiles,MAX_OBJ_SIZE)){
			logger.error(SysCode.TOO_BIG_FILES.getMessage());
			notification.put("notifyCode", SysCode.TOO_BIG_FILES.getCode());
			notification.put("notifyMessage", SysCode.TOO_BIG_FILES.getMessage());
			return info.getResponse(SysCode.TOO_BIG_FILES,notification);
		}
		if(uploadFiles.size() > 10){//附件最多10个
			notification.put("notifyCode", SysCode.TOO_MANY_FILES.getCode());
			notification.put("notifyMessage", SysCode.TOO_MANY_FILES.getMessage());
			return info.getResponse(SysCode.TOO_MANY_FILES,notification);
		}
		System.out.println(uploadItem.getFileData().get(0).getOriginalFilename());
		System.out.println(uploadItem.getFileData().get(0).getOriginalFilename().length());
		if(uploadItem.getFileData().get(0).getOriginalFilename().trim().length() > 50){
			notification.put("notifyCode", SysCode.TOO_LENGTH.getCode());
			notification.put("notifyMessage", SysCode.TOO_LENGTH.getMessage());
			return info.getResponse(SysCode.TOO_LENGTH,notification);
		}
		User user = getUser(response, request);//获得user方法
		if(user == null){
			notification.put("notifyCode", SysCode.SECURITY_ERR.getCode());
			notification.put("notifyMessage", SysCode.SECURITY_ERR.getMessage());
			return info.getResponse(SysCode.SECURITY_ERR,notification);
		}
		List<UploadObjVO> returnList = new ArrayList<UploadObjVO>();
		try {
			returnList = uploadObj(uploadItem, user);
			if(returnList == null || returnList.size() == 0){
				notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
    			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR.getMessage());
                return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
			}else{
				responseData.put("fileObjects", returnList);
        		responseData.put("total", returnList.size());
        		responseData = genResponseData(responseData, null);
        		
        		return info.getResponse(SysCode.SUCCESS, responseData);
			}
		} catch (Exception e) {
			logger.error("上传单个文件出错:" + e.getMessage(), e);
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR.getMessage());
            return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
		}
    }
    /**
     * 功能描述：   桐人上传图片统一方法      
     * @param uploadItem
     * @param request
     * @param response
     * @return                                                                                                 
     * @author 林阳 [linyang@gintong.com]
     * @since 2015年10月30日
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     *
     */
    @RequestMapping(value = "/uploadImg.json", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadImg(UploadItem uploadItem, HttpServletRequest request, HttpServletResponse response){
    	ParamInfo info = new ParamInfo();
        Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		
		List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
		if(uploadFiles == null || uploadFiles.size() == 0){//没添加附件的情况
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_EMPTY.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_EMPTY);
			return info.getResponse(SysCode.UPLOAD_FILE_IS_EMPTY,notification);
		}
		if(!checkObjFileSize(uploadFiles,MAX_IMAGE_SIZE)){
			logger.error(SysCode.TOO_BIG_FILES.getMessage());
			notification.put("notifyCode", SysCode.TOO_BIG_FILES.getCode());
			notification.put("notifyMessage", SysCode.TOO_BIG_FILES);
			return info.getResponse(SysCode.TOO_BIG_FILES,notification);
		}
			
		User user = getUser(response, request);//获得user方法
		if(user == null){
			notification.put("notifyCode", SysCode.SECURITY_ERR.getCode());
			notification.put("notifyMessage", SysCode.SECURITY_ERR);
			return info.getResponse(SysCode.SECURITY_ERR,notification);
		}
		List<UploadImgVO> returnList = new ArrayList<UploadImgVO>();
		try {
			returnList = uploadImg(uploadItem, user);
			if(returnList == null || returnList.size() == 0){
				notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
    			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR);
                return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
			}else{
				responseData.put("fileObjects", returnList);
        		responseData.put("total", returnList.size());
        		responseData = genResponseData(responseData, null);
        		return info.getResponse(SysCode.SUCCESS, responseData);
			}
		} catch (Exception e) {
			logger.error("上传单个文件出错:" + e.getMessage(), e);
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR);
            return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
		}
    }
	
	/**
	 * 功能描述：   上传图片文件
	 * @param uploadItem
	 * @param user
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月30日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<UploadImgVO> uploadImg(UploadItem uploadItem, User user)
			throws Exception {
		List<UploadImgVO> list = new ArrayList<UploadImgVO>();
		if (uploadItem == null)
            return list;
		List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
        if (uploadFiles != null) {
            FtpUpload uploader = new FtpUpload();
            uploader.connect(FileInstance.FTP_IP.trim(), 21, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "");
            try {
            	 CommonsMultipartFile file = null;
                 for (int i = 0, n = uploadFiles.size(); i < n; i++) {
                     file = uploadFiles.get(i);
                     if (!file.isEmpty()) {
                    	 String origiName = file.getOriginalFilename();
	                     String name = MD5Util.MD5(origiName).length()>20?MD5Util.MD5(origiName).substring(0,15):MD5Util.MD5(origiName);
	                     name = name + System.currentTimeMillis()+"."+FilenameUtils.getExtension(origiName);
	                     
	                     String path = uploader.uploadFile(UploadType.IMAGE, file.getInputStream(),name);
                         //往file库下插入数据
                         FileIndex fileIndex =new FileIndex();//文件存储对象
                     	fileIndex.setId(MakePrimaryKey.getPrimaryKey());
                     	fileIndex.setFilePath(FilenameUtils.getFullPathNoEndSeparator(path));
                     	fileIndex.setFileTitle(FilenameUtils.getName(name));
                     	fileIndex.setFileSize(file.getSize());
                     	fileIndex.setFileType(UploadType.OBJECT.getCode());
                     	fileIndex.setCtime(DateFunc.getDate());
                     	fileIndex.setAuthor(user.getId());
                     	fileIndex.setAuthorName(user.getName());
                     	fileIndex.setStatus(true);
                     	String taskId = MakeTaskId.getTaskId();
                     	fileIndex.setTaskId(taskId);
                     	logger.info("insert fileIndex begin");
                     	fileIndexService.insert(fileIndex);  
                     	logger.info("insert fileIndex end");
                     	
                     	//封装页面VO对象 
                     	UploadImgVO uploadImgVO = new UploadImgVO();
                     	uploadImgVO.setFileSize(file.getSize());
                     	uploadImgVO.setImgFullpath(FileInstance.FTP_WEB+FileInstance.FTP_URL.trim()+"/tongren"+path);
                     	uploadImgVO.setTaskId(taskId);
                     	list.add(uploadImgVO);
                     }
                 }
			} catch (Exception e) {
				throw e;
				
			}finally{
				uploader.disconnect();
			}
        }
        return list;
	}
	/**
	 * 功能描述：  上传obj文件       
	 *                                                       
	 * @param uploadItem
	 * @param user
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月30日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<UploadObjVO> uploadObj(UploadItem uploadItem,User user)
            throws Exception{
		List<UploadObjVO> list = new ArrayList<UploadObjVO>();
        if (uploadItem == null)
            return list;
        List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
        if (uploadFiles != null) {
            FtpUpload uploader = new FtpUpload();
            uploader.connect(FileInstance.FTP_IP.trim(), 21, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "");
            try {
            	 CommonsMultipartFile file = null;
                 for (int i = 0, n = uploadFiles.size(); i < n; i++) {
                     file = uploadFiles.get(i);
                     if (!file.isEmpty()) {
                     	String fileName =file.getOriginalFilename();
                         String path = uploader.uploadFile(UploadType.OBJECT, file.getInputStream(),fileName);
                         //往file库下插入数据
                         FileIndex fileIndex =new FileIndex();//文件存储对象
                     	fileIndex.setId(MakePrimaryKey.getPrimaryKey());
                     	fileIndex.setFilePath(FilenameUtils.getFullPathNoEndSeparator(path));
                     	fileIndex.setFileTitle(FilenameUtils.getName(fileName));
                     	fileIndex.setFileSize(file.getSize());
                     	fileIndex.setFileType(UploadType.OBJECT.getCode());
                     	fileIndex.setCtime(DateFunc.getDate());
                     	fileIndex.setAuthor(user.getId());
                     	fileIndex.setAuthorName(user.getName());
                     	fileIndex.setStatus(true);
                     	String taskId = MakeTaskId.getTaskId();
                     	fileIndex.setTaskId(taskId);
                     	logger.info("insert fileIndex begin");
                     	fileIndexService.insert(fileIndex);  
                     	logger.info("insert fileIndex end");
//                     	//插入本地我的资源和组织资源
//                     	LocalObject localObject = new LocalObject();
//                     	localObject.setCreateId(user.getId());
//                     	localObject.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                     	localObject.setOrganizationId(organizationId);
//                     	localObject.setProjectId(projectId);
//                     	localObject.setTaskId(taskId);
//                     	resourcesService.addResourcesLocalAndOrg(localObject);
                     	//封装页面VO对象 
                     	UploadObjVO uploadObjVO = new UploadObjVO();
                     	uploadObjVO.setFileSize(file.getSize());
                     	uploadObjVO.setImgFullpath(FileInstance.FTP_URL.trim()+"/tongren"+path);
                     	uploadObjVO.setTaskId(taskId);
                     	uploadObjVO.setFileName(FilenameUtils.getName(fileName));
                     	list.add(uploadObjVO);
                     }
                 }
			} catch (Exception e) {
				throw e;
				
			}finally{
				uploader.disconnect();
			}
        }
        return list;
	}
	/**
	 * 功能描述：    判断图片大小     
	 *                                                       
	 * @param uploadFiles文件集合，有一个超过大小则返回false
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月30日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	protected boolean checkObjFileSize(List<CommonsMultipartFile> uploadFiles,long maxFile){
		if(uploadFiles != null){
			for (CommonsMultipartFile commonsMultipartFile : uploadFiles) {
				if(commonsMultipartFile.getSize() > maxFile){
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	/**
	 * 功能描述： 图片类型转成，枚举类型对象        
	 *                                                       
	 * @param imgType
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月29日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	protected UploadType getCoUpload(String imgType){
		if(StringUtils.isBlank(imgType)){
			return UploadType.TEMP;
		}else{
			if(imgType.equals("1"))
				return UploadType.IMAGE;
			else if(imgType.equals("2"))
				return UploadType.AUDIO;
			else if(imgType.equals("3"))
				return UploadType.OBJECT;
			else if(imgType.equals("4"))
				return UploadType.OR_PIC;
			else
				return UploadType.TEMP;
		}
	}
	/**
	 * 功能描述：   上传文件接口 
	 * @param uploadItem
	 * @param request
	 * @param response
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @throws ValiaDateRequestParameterException 
	 * @since 2015年10月15日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@RequestMapping(value = "/uploadFile.json", method = RequestMethod.POST)
    @ResponseBody
	public Object uploadFile(UploadItem uploadItem, HttpServletRequest request, HttpServletResponse response){
		long startTime=System.currentTimeMillis();   //获取开始时间
		String method = "uploadFile";
        ParamInfo info = new ParamInfo();
        Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		try {
			info = parseRequest(request,response, method, null);
		} catch (ValiaDateRequestParameterException e1) {
			logger.info(e1.getMessage(),SysCode.ERROR_CODE);
			notification.put("notifyCode", e1.getErrCode());
			notification.put("notifyMessage", e1.getErrMessage());
			return info.getResponse(SysCode.ERROR_CODE,notification);
		}
		User user = getUser(response, request);//获得user方法
		if(user == null){
			notification.put("notifyCode", SysCode.SECURITY_ERR.getCode());
			notification.put("notifyMessage", SysCode.SECURITY_ERR);
			return info.getResponse(SysCode.SECURITY_ERR,notification);
		}
        try {
        	List<UploadItem> fileObjects = upload(getCoUpload(uploadItem.getImgType()), uploadItem, false,false);
        	List<UploadItem> fileObjects1 = new ArrayList<UploadItem>();
        	
        	
        	long endTime=System.currentTimeMillis(); //获取结束时间
            System.out.println("上传文件共使用时间："+(endTime-startTime));
        	if(fileObjects.size() > 0){
        		for (UploadItem uploadItem2 : fileObjects) {
            		FileIndex fileIndex =new FileIndex();//文件存储对象
                	fileIndex.setId(MakePrimaryKey.getPrimaryKey());
                	fileIndex.setFilePath(uploadItem2.getFilepath());
                	fileIndex.setFileTitle(uploadItem2.getFilename());
                	fileIndex.setFileSize(Long.parseLong(uploadItem2.getFileSize()));
                	fileIndex.setFileType(Integer.parseInt(uploadItem2.getImgType()));
                	fileIndex.setCtime(DateFunc.getDate());
                	fileIndex.setAuthor(user.getId());
                	fileIndex.setAuthorName(user.getName());
                	fileIndex.setStatus(true);
                	
                	String taskId = MakeTaskId.getTaskId();
                	fileIndex.setTaskId(taskId);
                	uploadItem2.setTaskId(taskId);
                	
                	fileIndexService.insert(fileIndex);
                	
                	fileObjects1.add(uploadItem2);
    			}
        		responseData.put("fileObjects", JsonUtils.toJsonString(fileObjects1));
        		responseData.put("total", fileObjects1.size());
        		responseData = genResponseData(responseData, null);
        		return info.getResponse(SysCode.SUCCESS, responseData);
        	}else {
        		notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
    			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR);
                return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
            }
        	
		} catch (Exception e) {
			logger.error("上传单个文件出错:" + e.getMessage(), e);
			notification.put("notifyCode", SysCode.UPLOAD_FILE_IS_ERR.getCode());
			notification.put("notifyMessage", SysCode.UPLOAD_FILE_IS_ERR);
            return info.getResponse(SysCode.UPLOAD_FILE_IS_ERR, notification);
		}
	}
	/**
	 * 功能描述：  上传文件       
	 * @param type 上传类型
	 * @param uploadItem 上传内容
	 * @param isTemp 是否临时文件
	 * @param genTimestamp 是否生成时间戳
	 * @return 正式文件路径
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月15日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<UploadItem> upload(UploadType type, UploadItem uploadItem, boolean isTemp, boolean genTimestamp)
            throws Exception {
		 List<UploadItem> list = new ArrayList<UploadItem>();

	        if (uploadItem == null)
	            return list;

	        List<CommonsMultipartFile> uploadFiles = uploadItem.getFileData();
	        if (uploadFiles != null) {

	            FtpUpload uploader = new FtpUpload();
	            uploader.connect(FileInstance.FTP_IP.trim(), 21, FileInstance.FTP_USER_NAME, FileInstance.FTP_PASSWORD, "/");
//	            uploader.connect("192.168.101.9", 21, "chxuftp", "chxuftp1230", "/");
	            CommonsMultipartFile file = null;
	            for (int i = 0, n = uploadFiles.size(); i < n; i++) {
	                file = uploadFiles.get(i);
	                if (!file.isEmpty()) {
	                    switch (type) {
	                    case OBJECT: {
	                    	String fileName = System.currentTimeMillis()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
	                        String path = uploader.uploadFile((isTemp ? UploadType.TEMP : type), file.getInputStream(),fileName);
	                        if (genTimestamp) {
//	                            list.add(path + "?" + System.currentTimeMillis());
	                            uploadItem.setFilename(fileName);
	                            uploadItem.setFilepath(path + "?" + System.currentTimeMillis());
	                            uploadItem.setImgFullpath(FileInstance.FTP_URL.trim()+"/ftpdir"+path);
	                            uploadItem.setImgType(String.valueOf(UploadType.OBJECT.getCode()));
	                            uploadItem.setImgUrl(FileInstance.FTP_URL.trim());
	                            uploadItem.setFileSize(String.valueOf(file.getSize()));
	                            uploadItem.setFileData(null);
	                            list.add(uploadItem);
	                        } else {
	                        	uploadItem.setFilename(fileName);
	                            uploadItem.setFilepath(path);
	                            uploadItem.setImgFullpath(FileInstance.FTP_URL.trim()+"/tongren"+path);
	                            uploadItem.setImgType(String.valueOf(UploadType.OBJECT.getCode()));
	                            uploadItem.setImgUrl(FileInstance.FTP_URL.trim());
	                            uploadItem.setFileSize(String.valueOf(file.getSize()));
	                            uploadItem.setFileData(null);
	                            list.add(uploadItem);
	                        }
	                        break;
	                    }
	                    case IMAGE: {
	                    	String origiName = file.getOriginalFilename();
	                        String name = MD5Util.MD5(origiName).length()>20?MD5Util.MD5(origiName).substring(0,15):MD5Util.MD5(origiName);
	                        name = name + System.currentTimeMillis()+"."+FilenameUtils.getExtension(origiName);
	                        String path = uploader.uploadFile((isTemp ? UploadType.TEMP : type), file.getInputStream(),name);
	                        
//	                        String path = uploader.uploadFile((isTemp ? UploadType.TEMP : type), file.getInputStream(),
//	                                file.getOriginalFilename());
	                        if (genTimestamp) {
							uploadItem.setFilename(name);
							uploadItem.setFilepath(path + "?"
									+ System.currentTimeMillis());
							uploadItem.setImgFullpath(FileInstance.FTP_URL
									.trim() +"/tongren"+ path);
							uploadItem.setImgType(String
									.valueOf(UploadType.IMAGE.getCode()));
							uploadItem.setImgUrl(FileInstance.FTP_URL.trim());
							uploadItem.setFileSize(String.valueOf(file.getSize()));
							uploadItem.setFileData(null);
							list.add(uploadItem);
	                        } else {
	                        	uploadItem.setFilename(name);
								uploadItem.setFilepath(path);
								uploadItem.setImgFullpath(FileInstance.FTP_URL
										.trim() +"/tongren"+path);
								uploadItem.setImgType(String
										.valueOf(UploadType.IMAGE.getCode()));
								uploadItem.setImgUrl(FileInstance.FTP_URL.trim());
								uploadItem.setFileSize(String.valueOf(file.getSize()));
								uploadItem.setFileData(null);
								list.add(uploadItem);
	                        }
	                        break;
	                    }
	                    case OR_PIC: {
	                        // 上传至FTP临时目录
	                    	String origiName = file.getOriginalFilename();
	                        String name = MD5Util.MD5(origiName).length()>20?MD5Util.MD5(origiName).substring(0,15):MD5Util.MD5(origiName);
	                        name = name + System.currentTimeMillis()+"."+FilenameUtils.getExtension(origiName);
	                        String path = uploader.uploadFile((isTemp ? UploadType.TEMP : type), file.getInputStream(),name);

	                        name = FilenameUtils.getBaseName(path);
	                        String extension = FilenameUtils.getExtension(path);
	                        // 存至本地服务器临时目录
	                        File srcFile = new File(userHome + "/temp/" + name + "." + extension);
	                        file.transferTo(srcFile);

	                        // 转换成缩略图 ---140X
	                        File destFile140 = new File(userHome + "/temp/" + name + "_MIN.140X." + extension);
	                        ImageScale imageScale140 = new ImageScale();
	                        imageScale140.resizeFix(srcFile, destFile140, boxWidth, boxHeight);

	                        // 将缩略图上传至FTP临时目录
	                        InputStream in140 = new FileInputStream(destFile140);
	                        String newFilePath140 = FilenameUtils.getFullPath(path) + name + "_MIN.140X." + extension;
	                        newFilePath140 = uploader.uploadFile(in140, newFilePath140);
	                        if (in140 != null) {
	                            in140.close();
	                        }
	                       
//	                        // 转换成缩略图 ---60X
//	                        File destFile60 = new File(userHome + "/temp/" + name + "_MIN.60X." + extension);
//	                        ImageScale imageScale60 = new ImageScale();
//	                        imageScale60.resizeFix(srcFile, destFile60, 60, 60);
//
//	                        // 将缩略图上传至FTP临时目录
//	                        InputStream in60 = new FileInputStream(destFile60);
//	                        String newFilePath60 = FilenameUtils.getFullPath(path) + name + "_MIN.60X." + extension;
//	                        newFilePath60 = uploader.uploadFile(in60, newFilePath60);
//	                        if (in60 != null) {
//	                            in60.close();
//	                        }
//	                     // 转换成缩略图 ---34X
//	                        File destFile34 = new File(userHome + "/temp/" + name + "_MIN.34X." + extension);
//	                        ImageScale imageScale34 = new ImageScale();
//	                        imageScale34.resizeFix(srcFile, destFile34, 34, 34);
//
//	                        // 将缩略图上传至FTP临时目录
//	                        InputStream in34 = new FileInputStream(destFile34);
//	                        String newFilePath34 = FilenameUtils.getFullPath(path) + name + "_MIN.34X." + extension;
//	                        newFilePath34 = uploader.uploadFile(in34, newFilePath34);
//	                        if (in34 != null) {
//	                            in34.close();
//	                        }
	                        uploadItem.setFilename(name);
							uploadItem.setFilepath(path);
							uploadItem.setImgFullpath(FileInstance.FTP_URL
									.trim()+"/tongren"+path);
							uploadItem.setImgType(String
									.valueOf(UploadType.OR_PIC.getCode()));
							uploadItem.setImgUrl(FileInstance.FTP_URL.trim());
							uploadItem.setFileSize(String.valueOf(file.getSize()));
							uploadItem.setFileData(null);
							list.add(uploadItem);
//	                        list.add(newFilePath60);
//	                        list.add(newFilePath34);
	                        FileUtils.deleteQuietly(srcFile);
//	                        FileUtils.deleteQuietly(destFile140);
//	                        FileUtils.deleteQuietly(destFile60);
//	                        FileUtils.deleteQuietly(destFile34);

	                        break;
	                    }
	                    case AUDIO: {
	                        // 上传至FTP临时目录
	                        String path = uploader.uploadFile(isTemp ? UploadType.TEMP : type, file.getInputStream(),
	                                file.getOriginalFilename());

	                        String name = FilenameUtils.getBaseName(path);
	                        String extension = FilenameUtils.getExtension(path);

	                        // 存至本地服务器临时目录
	                        File srcFile = new File(userHome + "/temp/" + name + "." + extension);
	                        file.transferTo(srcFile);

	                        // spx 转换 wav
	                        File wavFile = new File(userHome + "/temp/" + name + ".wav");
//	                        new JSpeexDec().decode(srcFile, wavFile);

	                        // wav 转换 mp3
	                        File mp3File = new File(userHome + "/temp/" + name + ".mp3");
	                        FFmpegUtils.convertWavToMp3(wavFile.getPath(), mp3File.getPath());

	                        // 上传转码后的文件
	                        InputStream in = new FileInputStream(mp3File);
	                        String newFilePath = FilenameUtils.getFullPath(path) + name + ".mp3";
	                        uploader.uploadFile(in, newFilePath);
	                        if (in != null) {
	                            in.close();
	                        }

	                        uploadItem.setFilename(name);
							uploadItem.setFilepath(path);
							uploadItem.setImgFullpath(FileInstance.FTP_URL
									.trim() + path);
							uploadItem.setImgType(String
									.valueOf(UploadType.AUDIO.getCode()));
							uploadItem.setImgUrl(FileInstance.FTP_URL.trim());
							uploadItem.setFileSize(String.valueOf(file.getSize()));
							uploadItem.setFileData(null);
							list.add(uploadItem);

	                        FileUtils.deleteQuietly(srcFile);
	                        FileUtils.deleteQuietly(wavFile);
	                        FileUtils.deleteQuietly(mp3File);

	                        break;
	                    }
	                    default:
	                        break;
	                    }
	                }
	            }
	            uploader.disconnect();
	        }
	        return list;
	}
}
