package com.ginkgocap.tongren.project.manage.web;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.util.CacheStoreManage;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;
import com.ginkgocap.tongren.project.manage.service.ProjectResourceAttachmentService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.vo.ProjectVO;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.google.common.collect.Maps;

/**
 * 
 * @author yanweiqi
 *
 */
@Controller
@RequestMapping("/project/manage/")
public class ProjectController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	private static final String BEAN_PAGE = "bean_page_publish";
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectResourceAttachmentService projectResourceAttachmentService;
	
	@RequestMapping(value="/create.json",method=RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"name|R|L50|F","introduction|R|L200|F","area|R","industry|R","taskId","organizationId","remuneration|R|N","validityStartTime|R","validityEndTime|R","cycle|R|L3|N"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			Project p = buildProjectParameters(params, user);
			if(p.getValidityStartTime().compareTo(p.getValidityEndTime()) < 0){
			   Project project =  projectService.create(p);
			   CacheStoreManage.cleanByKey(BEAN_PAGE);
			   responseData.put("project", project);
			   renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
			}
			else{
		       logger.info("validate failed! "+ApiCodes.ProjectCreateCycleError.getDescription());
		       notification.put(WebConstants.NOTIFCODE, ApiCodes.ProjectCreateCycleError.getCode());
			   notification.put(WebConstants.NOTIFINFO,ApiCodes.ProjectCreateCycleError.getDescription());
			   renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(null, notification)));
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	@RequestMapping(value="/getMyAllProjects.json",method=RequestMethod.POST)
	public void getMyAllProjects(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		try{
			RequestInfo requestInfo = validate(request, response, null);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			List<Project> projects =  projectService.getMyProjectByCreateId(user.getId());
			responseData.put("projects", projects);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	
	@RequestMapping(value="/getMyProjectsUnPublish.json",method=RequestMethod.POST)
	public void getMyProjectsUnPublish(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = null;
		try{
			RequestInfo requestInfo = validate(request, response, null);
			if(null == requestInfo) return;
			User user = requestInfo.getUser();
			params = requestInfo.getParams();
			List<Project> projects =  projectService.getMyProjectsByCreateIdAndStatus(user.getId(), ProjectStatus.Project_Create_Draft.getKey());
			responseData.put("projects", projects);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}

	private Project buildProjectParameters(ParamInfo params, User user) {
		Project p = new Project();
		String name = params.getParam("name");
		String introduction = params.getParam("introduction");
		String area = params.getParam("area");
		String industry = params.getParam("industry");
		String taskId = StringUtils.isNoneEmpty(params.getParam("taskId"))?params.getParam("taskId"):null;
		long createrId = user.getId();
		long organizationId = StringUtils.isNotEmpty(params.getParam("organizationId"))? Long.valueOf(params.getParam("organizationId")):0L;
		double remuneration = Double.valueOf(params.getParam("remuneration"));
		Timestamp validityStartTime = Timestamp.valueOf(params.getParam("validityStartTime"));
		Timestamp validityEndTime  =  Timestamp.valueOf(params.getParam("validityEndTime"));
		int cycle = Integer.valueOf(params.getParam("cycle")) ;

		p.setName(name);
		p.setIntroduction(introduction);
		p.setArea(area);
		p.setIndustry(industry);
		if(null != taskId) p.setDocument(taskId);
		p.setCreaterId(createrId);
		p.setOrganizationId(organizationId);
		p.setRemuneration(remuneration);
		p.setValidityEndTime(validityEndTime);
		p.setValidityStartTime(validityStartTime);
		p.setCycle(cycle);
		p.setStatus(ProjectStatus.Project_Create_Draft.getKey());
		Timestamp t = new Timestamp(System.currentTimeMillis());
		p.setCreateTime(t);
		p.setUpdateTime(t);
		return p;
	}
	/**
	 * 查看项目详情
	 * 
	 */
	@RequestMapping(value="/getProjectDetail.json",method=RequestMethod.POST)
	public void getProjectDetail(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ProjectVO project = new ProjectVO();
		ParamInfo params = null;
		String[] paramsKey = new String[]{"projectId|R"};
		try{
			RequestInfo requestInfo = validate(request, response, paramsKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			Long projectId = Long.valueOf(params.getParam("projectId"));
			
			project =  projectService.getProjectDetail(projectId);
			
			if(project != null){
				User user = userService.selectByPrimaryKey(project.getCreaterId());
				List<ResourceAttachment> docList = project.getResourceAttachments();;
				if(docList != null && !docList.isEmpty()){
					for(ResourceAttachment r:docList){
						//获取文档全路径
						String path  = getPathByTaskId(r.getTaskId());
						r.getFileIndex().setFilePath(path);
					}
				}
				if(user != null){
					project.setCreateName(user.getName());
				}
				responseData.put("projectVO", project);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));	
			}else{
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null,notification)));
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
}
