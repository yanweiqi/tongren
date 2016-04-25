package com.ginkgocap.tongren.project.manage.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.util.DaoSortType;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.project.manage.Exception.ProjectException;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.model.ResourceAttachment;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.ProjectResourceAttachmentService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.tongren.project.manage.vo.ProjectVO;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;

/**
 * 项目创建service实现类
 * 
 * @author ywq
 * @version
 * @since 2015年11月9日
 */
@Service("projectService")
public class ProjectServiceImpl extends AbstractCommonService<Project> implements ProjectService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private PublishService publishService;

	@Autowired
	private CodeRegionService codeRegionService;

	@Autowired
	private CodeService codeService;
	@Autowired
	private ApplyService applyService;
	
	@Autowired
	private ProjectResourceAttachmentService projectResourceAttachmentService;
	
	private static long MIN_DATE_PARTITION=1448899200000l;

	@Override
	public List<Project> getMyProjectByCreateId(long createrId) throws Exception {
		List<Project> projects = new ArrayList<Project>();
		try {
			List<Long> ids = getKeysByParams("project_list_createrId",new Object[] { createrId });
			if (null != ids && ids.size() > 0) {
				for (Long id : ids) {
					//Project p = getProjectDetail(id);//获取项目详细信息
					Project p  = getEntityById(id);
					if(p != null){projects.add(p);}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return projects;
	}

	@Override
	public List<Project> getProjectsByStatus(int status) throws Exception {
		List<Project> projects = new ArrayList<Project>();
		try {
			List<Long> ids = getKeysByParams("project_list_status",new Object[] { status });
			if (null != ids && ids.size() > 0) {
				for (Long id : ids) {
					Project p = getProjectDetail(id);
					projects.add(p);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return projects;
	}

	@Override
	public Project create(String name, 
						  String introduction, 
						  String area,
						  String industry, 
						  String document, 
						  long createrId,
						  long organizationId, 
						  double remuneration, 
						  int status,
						  Timestamp validityStartTime, 
						  Timestamp validityEndTime, 
						  int cycle) throws ProjectException,Exception {
		Project p_project = null;
		try {
			Project project = new Project();
			Timestamp t = new Timestamp(System.currentTimeMillis());
			project.setName(name);
			project.setIntroduction(introduction);
			project.setArea(area);
			project.setIndustry(industry);
			project.setDocument(document);
			project.setCreaterId(createrId);
			project.setOrganizationId(organizationId);
			project.setRemuneration(remuneration);
			project.setStatus(status);
			project.setValidityStartTime(validityStartTime);
			project.setValidityEndTime(validityEndTime);
			project.setCreateTime(t);
			project.setUpdateTime(t);
			project.setCycle(cycle);
			p_project = buildProject(document, createrId, create(project));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return p_project;
	}

	private Project buildProject(String document, long createrId, Project project) throws Exception {
		if(null != document){//判断是否有附件
			String[] taskids = document.split(",");
			List<ResourceAttachment> projectAttachments = projectResourceAttachmentService.insertProjectAttachment(project.getId(), Arrays.asList(taskids), createrId);
			if(null == projectAttachments ){
				logger.info("Project Resource Attachment save fail!");
			}
		}
		return getEntityById(project.getId());//未发布之前，发布表不存
	}

	@Override
	public Project create(Project project) throws ProjectException, Exception {
		Project p_project = null;
		try {
			Project check_p = getProjectByNameAndOrganizationIdAndcreaterId(project.getName(), project.getOrganizationId(), project.getCreaterId());
			if(null == check_p){
				p_project = buildProject(project.getDocument(), project.getCreaterId(), save(project));
			}
			else{
				throw new ProjectException(ApiCodes.ProjectIsExist.getCode(), ApiCodes.ProjectIsExist.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return p_project;
	}
	
	@Override
	public Project getProjectByNameAndOrganizationIdAndcreaterId(String name,long organizationId,long createrId) throws Exception{
		String sql_map = "project_map_name_createrId_organizationId";
		Project p = null;
		try {
			Long id = getMappingByParams(sql_map, new Object[]{name,organizationId,createrId});
			if(null != id){
			   p = getEntityById(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p;
	}

	@Override
	public List<Project> getMyProjectsByCreateIdAndStatus(long createrId,int status) throws Exception {
		List<Project> projects = new ArrayList<Project>();
		try {
			List<Long> ids = getKeysByParams("project_list_createrId_status",new Object[] { createrId, status });
			if (null != ids && ids.size() > 0) {
				for (Long id : ids) {
					Project p = getProjectDetail(id);
					projects.add(p);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return projects;
	}
	
	@Override
	public ProjectVO getProjectDetail(long projectId) throws Exception {
		Project project = null;
		ProjectVO vo = new ProjectVO();
		try {
			project = getEntityById(projectId);
			BeanUtils.copyProperties(vo, project);
			if (project != null) {
				conversionParameters(project, vo);
				List<ResourceAttachment> resourceAttachments = projectResourceAttachmentService.getListResourceAttachment(projectId);
				if(null != resourceAttachments){
					vo.setResourceAttachments(resourceAttachments);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectDetail error projectId-->"+projectId,e);
		}
		return vo;
	}
	@Override
	public List<Publish> getProjectListByParameters(long time,int pageSize,Map<String, String> params)throws Exception {
		
		long t1 = System.currentTimeMillis();
		List<Publish> list = new ArrayList<Publish>();
		
		if(pageSize<=0||pageSize>10){
			pageSize=10;
		}
	try{
		String industryId = params.get("industryId");
		String areaId = params.get("areaId");
		Calendar queryDate=Calendar.getInstance();
		queryDate.setTimeInMillis(time);
		List<Long> ids = new ArrayList<Long>();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMM");
		
	while(list.size() != pageSize && queryDate.getTimeInMillis()>=MIN_DATE_PARTITION){//查询日期不能低于2015-12月份
		if(StringUtils.isNotBlank(industryId) && StringUtils.isBlank(areaId)){//根据行业查询
			
			ids = getKeysByParams("project_list_industry",DaoSortType.DESC, new Object[] {industryId,sf.format(queryDate.getTime())});
			
		}else if(StringUtils.isBlank(industryId) && StringUtils.isNotBlank(areaId)){//根据区域查询
			
			ids = getKeysByParams("project_list_area",DaoSortType.DESC, new Object[] {areaId,sf.format(queryDate.getTime())});	
			
		}else{//行业 地区 联合查询
			ids = getKeysByParams("project_list_industry_area",DaoSortType.DESC, new Object[] {industryId,areaId,sf.format(queryDate.getTime())});
		}
		//遍历按照参数查询出的结果  筛选未开始的项目
		if(ids != null && CollectionUtils.isNotEmpty(ids)){
			for(Long id :ids){
				Publish publish = publishService.getPublishByProjectId(id);
				if(publish == null){
					continue;
				}
				if(publish.getStatus() != ProjectStatus.Project_Apply_CheckPending.getKey()){
					continue;
				}
				Project p = getProjectDetail(id);
				if(p != null){
					//根据项目id获取 申请集合
					List<Apply> list_apply = applyService.getApplyByProjectId(id);
					if(null != list_apply && list_apply.size() > 0){
						Set<Apply> applies = new HashSet<Apply>(list_apply);
						publish.setApplySet(applies);
					}
					publish.setProject(p);
					list.add(publish);
				}
				if(list.size() == pageSize){
					break;
				}
			}
		}
		if(list.size()!=pageSize){
			 queryDate.add(Calendar.MONTH, -1);
		}
	}	
	}catch(Exception e){
		logger.error(e.getMessage(),e);
		e.printStackTrace();
	}
		logger.info("查询组织下的任务,time:"+time+",pageSize:"+pageSize+",params:"+params+",resultCount:"+list.size());
		logger.info("query mytaks list is "+(System.currentTimeMillis()-t1));
		return list;
	}
	
	/**
	 * 根据项目行业、地区  id 获取对应的名称
	 * @param project
	 * @param vo
	 */
	public void conversionParameters(Project project,ProjectVO vo){
		try{
		String industryName = convertIndustryId(Long.valueOf(project.getIndustry()));
		String areaName = convertAreaId(Long.valueOf(project.getArea()));
		vo.setIndustry(industryName);
		vo.setArea(areaName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	protected Class<Project> getEntity() {
		return Project.class;
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

	@Override
	public List<Project> getProjectListByOrganizationId(long organization)throws Exception {
		
		List<Long> ids =new ArrayList<Long>();
		List<Project> list = new ArrayList<Project>();
		try{
			ids = getKeysByParams("project_list_organizationId", organization);
			if(ids != null){
				list = getEntityByIds(ids);
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}		
		return list;
	}
	public String convertIndustryId(long id){
		
		StringBuilder sb=new StringBuilder();
		List<Code> list=new ArrayList<Code>();
		Code code=getCodeById(id);
		if(code!=null){
			list.add(code);
			while("15".equals(code.getType())==false){
				code = getCodeById(Long.parseLong(code.getType()));
				list.add(code);
			}

			for(int i=list.size()-1;i>=0;i--){
				if(i >0 ){
					sb.append(list.get(i).getName() +"-");
				}else{
					sb.append(list.get(i).getName());
				}
			}
		}
		return sb.toString();
	}
	
	public String convertAreaId(long id){
		
		StringBuilder sb = new StringBuilder();
		List<String> list=new ArrayList<String>();
		CodeRegion codeRegion= getCodeRegionById(id);
		if(codeRegion==null){
			logger.warn("not found region info by id "+500);
		}
		list.add(codeRegion.getCname());
		//只查询到省，不包括中国 ParentId=1 为中国
		while(codeRegion.getParentId()!=1&&list.size()<3){
			codeRegion= getCodeRegionById(codeRegion.getParentId());
			if(codeRegion != null && list.contains(codeRegion.getCname()) == false){
				list.add(codeRegion.getCname());
			}
		}
		for(int i=list.size()-1;i>=0;i--){
			if(i>0){
				sb.append(list.get(i)+"-");
			}else{
				sb.append(list.get(i));
			}
		}
		return sb.toString();
	}
	
}
