package com.ginkgocap.tongren.organization.manage.web;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.util.ChineseToEnglish;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationTypeService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.manage.vo.OrgTaskVo;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationMemberVO;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationVO;
import com.ginkgocap.tongren.organization.manage.vo.RecommendVO;
import com.ginkgocap.tongren.organization.resources.model.OrganizationObject;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;
import com.ginkgocap.tongren.project.manage.vo.ProjectVO;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.tongren.resources.service.ResourcesService;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import com.ginkgocap.ywxt.user.service.UserService;
import com.google.common.collect.Maps;

/**
*  组织控制层
* @author  李伟超
* @version 
* @since 2015年10月15日
*/
@Controller
@RequestMapping("/organization") 
public class OrganizationController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	@Autowired
	private OrganizationMemberService organizationMemberService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private OrganizationRoleService organizationRoleService;
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private FriendsRelationService friendsRelationService;
	@Autowired
	private CodeRegionService codeRegionService;
	@Autowired
	private CodeService codeService;
	@Autowired
	private ResourcesService resourcesService;
	@Autowired
	private UndertakenService undertakenService;
	@Autowired
	private PublishService publishService;
	@Autowired
	private ApplyService  applyService;
	@Autowired
	private OrganizationTypeService organizationTypeService;
	@Autowired
	private ProjectService projectService;
	/**
	@Autowired
	private OrganizationEsService organizationEsService;**/
	
	
	@RequestMapping(value ="/getIndexContent.json",method = RequestMethod.GET)
	public void getIndexContent(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = Maps.newHashMap();
		Map<String, Object> notification = Maps.newHashMap();
		ParamInfo params = new ParamInfo();
		String index_url_project = "/project/manage/getPagePublishValidity.json";
		String index_url_message = "/message/userMessage.json";
		try{
			User user = getUser(response, request);
			if (null != user){
				List<OrganizationMember> organizationMembers =  getOrganizationMember(response, request); //在组织成员中是否存在
				List<Publish> publishs = publishService.getPublishByPublisherId(user.getId()); //是否发布过项目
				List<Apply> applies = applyService.getAppliesByProposerIdAndStatus(user.getId(), null); //是否申请项目 
				if(null != organizationMembers && organizationMembers.size() > 0){
				   responseData.put(WebConstants.Index_URL_DATA, index_url_message);		   
				}
				else if(null != publishs && publishs.size() > 0){
				   responseData.put(WebConstants.Index_URL_DATA, index_url_message);	
				}
				else if(null != applies && applies.size() > 0){
					responseData.put(WebConstants.Index_URL_DATA, index_url_message);
				}
				else{
				   responseData.put(WebConstants.Index_URL_DATA, index_url_project);
				}
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}
			else{
				notification.put(WebConstants.NOTIFCODE,ApiCodes.LoginFailure.getCode());
				notification.put(WebConstants.NOTIFINFO,ApiCodes.LoginFailure.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
			notification.put(WebConstants.NOTIFCODE,ApiCodes.InternalError.getCode());
			notification.put(WebConstants.NOTIFINFO,ApiCodes.InternalError.getMessage());
			renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(null, notification)));
		}
	}
	/**
	 * 获取铜人菜单资源数
	 * 
	 */
	@RequestMapping(value="/getMenuSize.json",method =RequestMethod.POST)
	public void getMenuSize(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {};
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			User user = ri.getUser();
			responseData = tongRenOrganizationService.getMenuSize(user.getId(),responseData);
			
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
		}
	}
	
	/**
	 * 功能描述 :创建组织
	 * @param name 组织名称
	 * @param introduction 组织简介
	 * @param classification 组织类型
	 * @param industry 组织行业
	 * @param taskId  上传图片logo
	 * @param area 地区
	 */
	@RequestMapping(value="/create.json",method =RequestMethod.POST)
	public void createOrganization(HttpServletRequest request, HttpServletResponse response){
		
		ParamInfo params = null;
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = {"name|R","introduction|R","classification|R","industry|R","taskId|R","area|R"};
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();

			String name = params.getParam("name");
			String introduction = params.getParam("introduction");
			String classification = params.getParam("classification");
			String industry = params.getParam("industry");
			String taskId = params.getParam("taskId");
			String area = params.getParam("area");
			
			Organization o = new Organization();
			o.setName(name);
			o.setIntroduction(introduction);
			o.setClassification(Integer.parseInt(classification));
			o.setArea(Integer.parseInt(area));
			o.setIndustry(Integer.parseInt(industry));
			o.setCreateTime(new Timestamp(System.currentTimeMillis()));
			o.setCreaterId(user.getId());
			o.setStatus(0);
			o.setLogo(taskId);
			Organization org =  tongRenOrganizationService.create(o);
		if(org != null){
			responseData.put(String.valueOf(org.getId()), org);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
		}else{
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null,notification)));
		}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
		}
		catch(Exception e){
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}

	/**
	 *	功能描述： 组织信息详情 
	 * 
	 */
	 @RequestMapping(value="/getOrganizationById.json",method=RequestMethod.POST)
	 public void getOrganizationById(HttpServletRequest request,HttpServletResponse response){
		 
			 String paramsKey[] = {"oid|R"};
			 ParamInfo params = null;
			 Map<String, Object> responseData = new HashMap<String, Object>();
			 Map<String, Object> notification = new HashMap<String, Object>();
			 List<Project> projects = new ArrayList<Project>();
			 List<Undertaken> lst = new ArrayList<Undertaken>();
			 List<OrganizationMemberVO> memberList = new ArrayList<OrganizationMemberVO>();
		 try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			User user=ri.getUser();
			String oid = params.getParam("oid");
		 	Organization o =  tongRenOrganizationService.getOrganizationById(Long.valueOf(oid));
		 	if(o != null){
		 		o.setLogo(getPathByTaskId(o.getLogo()));
		 		user = userService.selectByPrimaryKey(o.getCreaterId());//获取组织创建者信息
		 		memberList = organizationMemberService.getOrganizationAllMemberInfo(o.getId(), 1);//获取组织成员信息
				projects =  projectService.getMyProjectByCreateId(user.getId());//获取我创建的项目信息
		 		lst =  undertakenService.getUndertakenByOrganizationIdByStatus(o.getId(), 1);//获取组织完成项目信息
		 		packageData(lst, responseData);//封装组织完成的项目信息
		 		responseData.put("success", o);
		 		responseData.put("memberList", memberList);
		 		responseData.put("createProjects", projects);
		 		responseData.put("createProjectSize", projects.size());
		 		responseData.put("memberSize", memberList.size());
		 		if(user != null){responseData.put("createName", user.getName());}
				responseData.put("areaName", packageParameter(String.valueOf(o.getArea()), 1));
				responseData.put("industryName", packageParameter(String.valueOf(o.getIndustry()),2));
				responseData.put("classificationName", packageParameter(String.valueOf(o.getClassification()),3));
				
		 		renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
		 	}else{
		 		renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null,notification)));
		 	}
	 	}catch(ValiaDateRequestParameterException e){
	 		logger.info(e.getMessage(),e);	
	 	}
	 	catch(Exception e){
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			e.printStackTrace();
			return;	 
		 }
	 }
	/**
	 * 功能描述 : 解散组织
	 * @param oid 组织Id
	 */
	@RequestMapping(value="/disband.json",method=RequestMethod.POST)
	public void disband(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = {"oid|R"};
		ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			String oid = params.getParam("oid");
			boolean flag = undertakenService.getUndertakenByOrganizationId(Long.parseLong(oid));
			if(flag){
				warpMsg(SysCode.UNFINISHED_PROJECT_ERROR,"组织下还存在未完成的项目 不能解散",params,response);
				return;
			}
			boolean status = tongRenOrganizationService.disband(Long.parseLong(oid));
			if(status){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
			    renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
			}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
	 	}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	/**
	 * 功能描述 : 查看我创建的组织
	 * @param oid 组织Id
	 */
	@RequestMapping(value="/getMyCreateOrganizations",method=RequestMethod.POST)
	public void getMyCreateOrganizations(HttpServletRequest request, HttpServletResponse response){
		
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			String paramsKey[] = {"status|R"};
			ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			String status = params.getParam("status");
			List<OrganizationVO> lst = tongRenOrganizationService.getMyCreateOrganizations(user.getId(), Integer.valueOf(status));
			if(!lst.isEmpty()){
				for(OrganizationVO vo : lst){
					vo.setPath(getPathByTaskId(vo.getLogo()));  //组织logo
					vo.setAreaName(packageParameter(String.valueOf(vo.getArea()),1)); //组织地域名称集合
					vo.setIndustryName(packageParameter(String.valueOf(vo.getIndustry()), 2));//组织行业集合
					vo.setClassificationName(packageParameter(String.valueOf(vo.getClassification()), 3)); //组织类型集合
				}
				responseData.put("success", lst);
				responseData.put("size", lst.size());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
	 	}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	/**
	 * 功能描述 : 查看我参与的组织
	 * @param oid 组织Id
	 */
	@RequestMapping(value="/getMyJoinOrganization",method=RequestMethod.POST)
	public void GetMyJoinOrganization(HttpServletRequest request, HttpServletResponse response){
		
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			String paramsKey[] = {};
			ParamInfo params =null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
	
			List<OrganizationVO> lst = tongRenOrganizationService.getMyJoinOrganization(user.getId());
			if(!lst.isEmpty()){
				for(OrganizationVO vo : lst){
					vo.setPath(getPathByTaskId(vo.getLogo()));
					vo.setAreaName(packageParameter(String.valueOf(vo.getArea()),1)); //组织地域名称集合
					vo.setIndustryName(packageParameter(String.valueOf(vo.getIndustry()),2));//组织行业集合
					vo.setClassificationName(packageParameter(String.valueOf(vo.getClassification()),3)); //组织类型集合
				}
				responseData.put("success", lst);
				responseData.put("size", lst.size());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
	 	}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	/**
	 * 功能描述 : 修改组织信息
	 * @param oid 组织Id
	 */
	@RequestMapping(value="/updateOrganization",method=RequestMethod.POST)
	public void updateOrganization(HttpServletRequest request, HttpServletResponse response){
			
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			String paramsKey[] = {"oid|R","name","introduction","classification","industry","area","taskId"};
			ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			String oid = params.getParam("oid");
			String name = params.getParam("name");
			String area = params.getParam("area");
			String industry = params.getParam("industry");
			String introduction = params.getParam("introduction");
			String classification = params.getParam("classification");
			String taskId = params.getParam("taskId");
			
			logger.info("update Organization oid--->"+oid);
			Organization o  = tongRenOrganizationService.getOrganizationById(Long.valueOf(oid));
			if(o != null){
				if(StringUtils.isNotBlank(name)){
					String spe = ChineseToEnglish.convertToSpell(name);
					o.setName(name);
					o.setNameSpelling(spe);
					}
				if(StringUtils.isNotBlank(introduction)){
					o.setIntroduction(introduction);
				}
				if(StringUtils.isNotBlank(taskId)){
					o.setLogo(taskId);
				}
				if(StringUtils.isNotBlank(area)){
					o.setArea(Integer.parseInt(area));
				}
				if(StringUtils.isNotBlank(industry)){
					o.setIndustry(Integer.parseInt(industry));
				}
				if(StringUtils.isNotBlank(classification)){
					o.setClassification(Integer.parseInt(classification));
				}
				
				o.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				boolean status = tongRenOrganizationService.update(o);
				if(status){
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
				    renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
				}
			}else{
				notification.put(oid, "组织不存在");
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
			}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
	 	}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	
	/**
	 * 
	 * 组织概括接口
	 * 
	 */
	@RequestMapping(value="/getOrganizationSumup.json",method=RequestMethod.POST)
	public void getOrganizationSumup(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = {"oid|R"};
		List<OrganizationMemberVO> userList = new ArrayList<OrganizationMemberVO>();
		ParamInfo params = null;
		try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
				params=ri.getParams();
				String oid = params.getParam("oid");
				logger.info("getOrganizationSumup   oid--->"+oid);
				Organization o  = tongRenOrganizationService.getOrganizationById(Long.valueOf(oid));
				List<OrganizationMember> lst= organizationMemberService.getNormalMember(Long.valueOf(oid));
				if(lst != null && lst.size()>0){
					for(OrganizationMember member :lst){
						User user = userService.selectByPrimaryKey(member.getUserId());
						OrganizationMemberVO vo = new OrganizationMemberVO();
						vo.setUserName(user.getName());
						List<OrganizationRole> roles = organizationRoleService.getMyOrganizationRole(member.getUserId(),member.getOrganizationId());
						if (roles != null && !roles.isEmpty()) {
							vo.setRoleName(roles.get(0).getDescription());
						}
						if(StringUtils.isEmpty(user.getPicPath())){
							vo.setLogo(FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC);
						}else{
							vo.setLogo(FileInstance.FTP_WEB + FileInstance.FTP_URL+ user.getPicPath());
						}
						vo.setUserId(member.getUserId());
						vo.setAddTime(member.getApplyTime());
						userList.add(vo);
					}
				}
				if(o != null){
					o.setLogo(getPathByTaskId(o.getLogo()));
					responseData.put("organization", o);
					responseData.put("userList", userList);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
				}
		}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
	 	}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
	 		    return;
		}
	}
	/**
	 * 获取 组织任务列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getOrgTaskList.json",method=RequestMethod.POST)
	public void getOrgTaskList(HttpServletRequest request,HttpServletResponse response){
		String paramsKey[] = { "time|R","orgId|R", "type","pageSize" };
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			int pageSize=20;
			try{
				pageSize=Integer.parseInt(params.getParam("pageSize"));
			}catch(Exception e){}
			
			Map<String, String> qp=new HashMap<String, String>();
			// type 任务类型 -1 全部 ,0 准备中,1已开始 ,2已完成 3 已驳回
			String type="-1";
			if(params.getParam("type")!=null&&params.getParam("type").trim().length()>0)
			{
				type=params.getParam("type");
			}
			qp.put("orgId", params.getParam("orgId"));
			qp.put("type", type);
			List<Task> list=projectTaskService.getOrgTaskList(Long.parseLong(params.getParam("time")), pageSize, qp);
			List<OrgTaskVo> listTask=new ArrayList<OrgTaskVo>();
			if(list!=null&&list.size()>0){
				for(Task task:list){
					OrgTaskVo myTaskVo=new OrgTaskVo();
					BeanUtils.copyProperties(myTaskVo, task);
					myTaskVo.setAttachUrl(getPathByTaskId(task.getAttachId()));
					if(task.getTaskStatus()==6){
						myTaskVo.setRejectReason(assignTaskService.getRejectReasonByTaskId(task.getId()));
					}
					listTask.add(myTaskVo);
				}
			}
			logger.info("orgnization task is "+list.size());
			Map<String, Object> notification = new HashMap<String, Object>();
			Map<String, Object> responseData = new HashMap<String, Object>();
			responseData.put("tasks", listTask);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询组织任务列表失败！参数：" + request.getParameter("requestJson"), e);
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params, response);
		}
	}
	/**
	 * 获取分页有效组织
	 * 
	 * 
	 */
	@RequestMapping(value="/queryOrganizationPage.json",method=RequestMethod.POST)
	public void queryOrganizationPage(HttpServletRequest request, HttpServletResponse response){
		
				Page<RecommendVO> page = new Page<RecommendVO>();
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				String paramsKey[] = { "status|R", "index|R", "size|R" };
				ParamInfo params = null;
		try {
				RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
				User user = ri.getUser();
				params = ri.getParams();
				int index = Integer.parseInt(params.getParam("index"));
				int size = Integer.valueOf(params.getParam("size"));
			if(index<=0 || size == 0){
				warpMsg(SysCode.PARAM_IS_ERROR,"", params, response);
				return;
			}
				page.setIndex(index);
				String status = params.getParam("status");
				page.addParam("status", status);
				queryOrgAndFriend(page,size,user.getId());
				
			if (!page.getResult().isEmpty()) {
				responseData.put("pageResult", page);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
			} else {
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY,genResponseData(null, notification)));
			}
		} catch (ValiaDateRequestParameterException e) {
				logger.info(e.getMessage(), e);
		} catch (Exception e) {
				e.printStackTrace();
				warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
				return;
		}
	}
	
	private  void   queryOrgAndFriend(Page<RecommendVO> page,int totalSize,long userId) throws Exception{
		
		float  beliel = 0.5f; 
		page.setSize((int) (totalSize*beliel));
		//查询有效组织
		Page<RecommendVO> pageOrg = tongRenOrganizationService.queryOrganizationPage(page);
		page.setResult(pageOrg.getResult());
		page.setTotalCount(pageOrg.getTotalCount());
		
		List<RecommendVO> list = page.getResult();
		if(list != null && !list.isEmpty()){
			for (RecommendVO vo : list) {   
				vo.setTaskId(getPathByTaskId(vo.getTaskId()));
			}
		}else{
			 list = new ArrayList<RecommendVO>();
		}
		page.setSize(totalSize-list.size());
		//查询用户好友
		List<User> lst = friendsRelationService.findAllFriendsByNameAndChar(userId, null, null, -1, page.getStart(), page.getSize());
		if (!lst.isEmpty()) {
			for (User u : lst) {
				RecommendVO vo = new RecommendVO();
				vo.setId(u.getId());
				vo.setName(u.getName());
				vo.setTaskId(FileInstance.FTP_WEB + FileInstance.FTP_URL+ u.getPicPath());
				vo.setType(1);
				list.add(vo);
			}
			page.setResult(list);
		}
		
	}
	/**
	 * 根据名称获取组织或用户
	 * 
	 * 
	 */
	@RequestMapping(value="/queryOrganzationOrFriend.json",method=RequestMethod.POST)
	public void queryOrganzationOrFriend(HttpServletRequest request, HttpServletResponse response){
		
		Page<RecommendVO> pageResult = new Page<RecommendVO>();
		List<RecommendVO> list = new ArrayList<RecommendVO>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = { "name|R" };
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			String name = params.getParam("name");
			//查询有效组织
		    list = tongRenOrganizationService.getOrganizationByName(name);
			for (RecommendVO vo : list) {
				vo.setTaskId(getPathByTaskId(vo.getTaskId()));
			}
			//查询用户好友
			List<User> userList= friendsRelationService.findAllFriendsByNameAndChar(user.getId(),null,name,-1,0,4);
			if (!userList.isEmpty()) {
				for (User u : userList) {
					RecommendVO vo = new RecommendVO();
					vo.setId(u.getId());
					vo.setName(u.getName());
					vo.setTaskId(getUserPicURL(u));
					vo.setType(1);
					list.add(vo);
				}
			}
			if (list != null && !list.isEmpty()) {
				pageResult.setResult(list);
				responseData.put("pageResult", pageResult);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
			} else {
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY,genResponseData(null, notification)));
			}
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
			return;
		}
	}
	
	/**
	 * 分页查询组织和好友
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/queryOrganzationOrFriendPage.json",method=RequestMethod.POST)
	public void queryOrganzationOrFriendPage(HttpServletRequest request, HttpServletResponse response){
		
		Page<RecommendVO> pageResult = new Page<RecommendVO>();
		List<RecommendVO> list = new ArrayList<RecommendVO>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		//Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = { "name","index|R" };
		int pageSize=12;
		pageResult.setSize(pageSize);
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			//查询不区分大小写
			String name="";
			if(params.getParam("name")!=null){
				name= params.getParam("name").toLowerCase();
			}
			int index=Integer.parseInt(params.getParam("index"));
			
			List<Long> orgIds=tongRenOrganizationService.queryOranizationByNameOrSpell(name);
			List<User> userList=queryFriends(user.getId(),name);
			
			
			List<Object> allList=new ArrayList<Object>();
			int totalCount=userList.size()+orgIds.size();
			//总条数
			pageResult.setTotalCount(totalCount);
			if(totalCount>0){
				//合并组织数据和用户数据
				for(int k=0;k<userList.size()||k<orgIds.size();k++){
					if(k<orgIds.size()){
						allList.add(orgIds.get(k));
					}
					if(k<userList.size()){
						allList.add(userList.get(k));
					}
				}
				
				if(index<1){
					index=1;
				}else if(index>pageResult.getPageCount()){	
					index=pageResult.getPageCount();
				}
				pageResult.setIndex(index);
				int end=pageResult.getEnd();
				end=end>=allList.size()?allList.size():end;
				List<Object> subList=allList.subList(pageResult.getStart(),end);
				for(Object obj:subList){
					if(obj instanceof Long){
						Organization org= tongRenOrganizationService.getEntityById((Long)obj);
						RecommendVO vo = new RecommendVO();
						vo.setId(org.getId());
						String nameSpell=org.getNameSpelling();
						if(params.getParam("name")!=null){//查询关键字标红色
							String qstr=params.getParam("name");
							if(nameSpell!=null&&nameSpell.startsWith(qstr.toLowerCase())){
								String tmpStr="<font style=\"color:red\">"+org.getName().charAt(0)+"</font>";
								org.setName(tmpStr+org.getName().substring(1));
							}else if(org.getName().startsWith(qstr.toLowerCase())){
								String tmpStr=org.getName().replaceFirst(qstr, "<font style=\"color:red\">"+qstr+"</font>");
								org.setName(tmpStr);
							}
						}
						
						vo.setName(org.getName());
						
						vo.setTaskId(getPathByTaskId(org.getLogo()));
						vo.setOrgCreateId(org.getCreaterId());
						vo.setCreateTime(org.getCreateTime());
						vo.setOrgId(org.getId());
						vo.setCreateName(getUserNameById(org.getCreaterId()));
						vo.setType(0);
						
						list.add(vo);
					}else if(obj instanceof User){
						User u=(User)obj;
						RecommendVO vo = new RecommendVO();
						vo.setId(u.getId());
						vo.setName(u.getName());
						vo.setTaskId(getUserPicURL(u));
						vo.setType(1);
						list.add(vo);
					}
				}
				//对结果进行排序，先组织后人
				Collections.sort(list, new Comparator<RecommendVO>() {
					@Override
					public int compare(RecommendVO o1, RecommendVO o2) {
						return o1.getType() - o2.getType();
					}
				});
			}
			
			pageResult.setResult(list);
			responseData.put("pageResult", pageResult);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
			return;
		}
	}
	
	/**
	 * 查询匹配的好友
	 * @param userId
	 * @param qstr 查询字符串
	 * @return
	 */
	private List<User> queryFriends(long userId,String qstr){
		List<User>  rsList=new ArrayList<User>();
		List<User> userList= friendsRelationService.findAllFriendsByUserId(userId);
		if(qstr==null||qstr.trim().length()==0){
			return userList;
		}
		if(userList!=null&&userList.size()>0){
			for(User u:userList){
				String uname=u.getName();
				String spelle=ChineseToEnglish.convertToSpell(uname);
				if(uname.startsWith(qstr)||spelle.startsWith(qstr)){
					if(uname.startsWith(qstr)){
						String tmpStr=uname.replaceFirst(qstr, "<font style=\"color:red\">"+qstr+"</font>");
						u.setName(tmpStr);
					}else if(spelle.startsWith(qstr)){
						String tmpStr="<font style=\"color:red\">"+uname.charAt(0)+"</font>";
						u.setName(tmpStr+uname.substring(1));
					} 
					rsList.add(u);
				}
			}
		}
		return rsList;
	}
	/**
	 * 封装组织实体类参数 id->name
	 * @param ids
	 * @param type
	 * @return
	 	*/
	private List<String> packageParameter(String parameters,int type){
		
		//logger.info("packageParameter begin***"+parameters+"***"+"type"+type);
		String[] ids = parameters.split("-");
		List<String> result = new ArrayList<String>();
		try{
			if(ids.length >0){
				for(String id:ids){
					//根据地区id查询中文名称
					if(type == 1){ 
						CodeRegion region = codeRegionService.selectByPrimaryKey(Long.valueOf(id));
						if(region != null){
							result.add(region.getCname());
							continue;
						}
					}
					//根据行业id查询行业名称
					if(type == 2){
						Code code = codeService.selectByPrimarKey(Long.valueOf(id));
						if(code != null){
							result.add(code.getName());
							continue;
						}
					}
					//根据类型id查询 名称
					if(type == 3){
						result.add(organizationTypeService.getOrganizationTypeName(id));
						continue;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("packageParameter error**",e.getMessage());
		}
		return result;
	}
	/**
	 * 组织任务、资源、成员数量统计
	 * @author liweichao
	 * @param organizationId
	 * 
	 */
	@RequestMapping(value="/countOrganizationSumup.json",method=RequestMethod.POST)
	public void countOrganizationSumup(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = { "organizationId|R" };
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			Long organizationId = Long.valueOf(params.getParam("organizationId"));
			int  taskSize = projectTaskService.getOrgTaskCount(organizationId);
			List<OrganizationObject> resoucesList = resourcesService.getOrgObject(organizationId, 0);
			List<OrganizationMember> memberList = organizationMemberService.getNormalMember(organizationId);
			responseData.put("taskSize", taskSize);
			if(CollectionUtils.isNotEmpty(resoucesList)){
				responseData.put("resoucesSize", resoucesList.size());
			}else{
				responseData.put("resoucesSize", 0);
			}
			if(CollectionUtils.isNotEmpty(memberList)){
				responseData.put("memberSize", memberList.size());
			}else{
				responseData.put("memberSize", 0);
			}
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
			return;
		}
	}
	private void packageData(List<Undertaken> list, Map<String, Object> responseData){
		
		if(list == null || CollectionUtils.isEmpty(list))
			return;
		List<ProjectVO> projectList =new ArrayList<ProjectVO>();
	try{	
		for(Undertaken u:list){
			Project project = projectService.getEntityById(u.getProjectId());
			if(project == null){
				continue;
			}
			ProjectVO vo = new ProjectVO();
			BeanUtils.copyProperties(vo, project);
			projectList.add(vo);
		}
		responseData.put("finishProject", projectList);
	}catch(Exception e){
		logger.error("packageData is error.....");
		e.printStackTrace();
		}
	}
	
	/**
	 * 功能描述 ：组织推荐 
	 * @param type 0:全部  1：完成项目
	 * @param index 第几页 size 返回多少条
	 */
	@RequestMapping(value="/organizationRecommendation.json",method=RequestMethod.POST)
	public void OrganizationRecommendation(HttpServletRequest request, HttpServletResponse response){
		
		Page<OrganizationVO> page = new Page<OrganizationVO>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = { "type|R", "index|R", "size|R" };
		ParamInfo params = null;
		try{
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String type = params.getParam("type");
			page.addParam("type", type);
			page.setIndex(Integer.parseInt(params.getParam("index")));
			page.setSize(Integer.valueOf(params.getParam("size")));
			
			if(type.equals("0") || type.equals("1")){  //判断前台参数
				Page<OrganizationVO> pageResult = tongRenOrganizationService.getOrganizationListByType(page);
				if(pageResult !=null && pageResult.getResult().isEmpty() == false){
					//转化 组织行业 地区名称
					for(OrganizationVO vo :pageResult.getResult()){
						vo.setPath(getPathByTaskId(vo.getLogo()));  //组织logo
						vo.setAreaName(packageParameter(String.valueOf(vo.getArea()),1)); //组织地域名称集合
						vo.setIndustryName(packageParameter(String.valueOf(vo.getIndustry()), 2));//组织行业集合
						vo.setClassificationName(packageParameter(String.valueOf(vo.getClassification()), 3)); //组织类型集合
					}
				}
				responseData.put("pageResult", pageResult);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
			}else{
				warpMsg(SysCode.PARAM_IS_ERROR,"type is not 0,1",params,response);
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
		}
	}
	
	
	 /**
	  * 初始化所有的 组织名称的汉语拼音字段 
	  * @param request
	  * @param response
	  */
	@RequestMapping(value = "/updateAllNameSpell.do", method = RequestMethod.GET)
	public void initOrgnizationSpellName(HttpServletRequest request, HttpServletResponse response) {
		logger.info("begin updateAllNameSpell");
		try {
			tongRenOrganizationService.updateAllNameSpell();
		} catch (Exception e) {
			logger.error("updateAllNameSpell failed! ", e);
		}
		logger.info("success updateAllNameSpell");
	}
	/**
	 * 功能描述 : 查看我创建和在组织中是管理员的所有组织
	 * @param oid 组织Id
	 */
	@RequestMapping(value="/getMyCreateAndAdminOrganizations",method=RequestMethod.POST)
	public void getMyCreateAndAdminOrganizations(HttpServletRequest request, HttpServletResponse response){
		
			Map<String, Object> responseData = new HashMap<String, Object>();
			Map<String, Object> notification = new HashMap<String, Object>();
			String paramsKey[] = {"status|R","manager"};
			ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			String status = params.getParam("status");
			boolean manager = Boolean.parseBoolean(params.getParam("manager"));
			List<OrganizationVO> lst = tongRenOrganizationService.getMyCreateOrganizations(user.getId(), Integer.valueOf(status),manager);
			if(!lst.isEmpty()){
				for(OrganizationVO vo : lst){
					vo.setPath(getPathByTaskId(vo.getLogo()));  //组织logo
					vo.setAreaName(packageParameter(String.valueOf(vo.getArea()),1)); //组织地域名称集合
					vo.setIndustryName(packageParameter(String.valueOf(vo.getIndustry()), 2));//组织行业集合
					vo.setClassificationName(packageParameter(String.valueOf(vo.getClassification()), 3)); //组织类型集合
				}
				responseData.put("success", lst);
				responseData.put("size", lst.size());
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
		}catch(ValiaDateRequestParameterException e){
			logger.info(e.getMessage(),e);	
	 	}catch(Exception e){
	 		e.printStackTrace();
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	/**
	 * 组织移交
	 * 
	 */
	@RequestMapping(value="/creatorTurnOver.json",method=RequestMethod.POST)
	public void creatorTurnOver(HttpServletRequest request, HttpServletResponse response){
		
			Map<String, Object> responseData = new HashMap<String, Object>();
			String paramsKey[] = {"organizationId|R","receiveId|R"};
			ParamInfo params = null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			long receiveId = Long.parseLong(params.getParam("receiveId"));
			long organizationId = Long.parseLong(params.getParam("organizationId"));
			
			String result = tongRenOrganizationService.creatorTurnOver(organizationId, receiveId);
			if(result.equals("1")){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}else{
				String[] resStrArr={"无效的组织","组织创建者无效"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(result)-2],params,response,responseData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
