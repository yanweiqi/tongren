package com.ginkgocap.tongren.project.manage.service;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.Exception.ProjectException;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.vo.ProjectVO;

/**
 * 项目创建service
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月9日
 */
public interface ProjectService extends CommonService<Project>{
	
	/**
	 * 创建项目
	 * @param name               项目名称
	 * @param introduction       项目介绍
	 * @param area               区域
	 * @param industry           行业
	 * @param document           文档，taskId
	 * @param createrId          项目创建者
	 * @param organizationId     创建者所属组织
	 * @param remuneration       项目酬劳
	 * @param status             状态
	 * @param validityStartTime  项目开始时间
	 * @param validityEndTime    项目结束时间
	 * @param cycle              周期
	 * @return Project
	 */
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
						  int cycle) throws ProjectException, Exception;
	
	/**
	 * 创建项目
	 * @param project 项目对象
	 * @return Project
	 * @exception Exception
	 */
	public Project create(Project project) throws ProjectException, Exception;
	
	/**
	 * 根据项目名称，组织ID、创建人获取项目
	 * @param name
	 * @param organizationId
	 * @param createrId
	 * @return
	 * @throws Exception
	 */
	public Project getProjectByNameAndOrganizationIdAndcreaterId(String name,long organizationId,long createrId) throws Exception;
	
	/**
	 * 根据创建者ID、项目状态获取项目列表
	 * @param createrId 项目创建者
	 * @param status    项目状态
	 * @return List<Project>
	 * @exception Exception
	 */
	public List<Project> getMyProjectsByCreateIdAndStatus(long createrId,int status) throws Exception;
	
	/**
	 * 根据创建者ID获取项目列表
	 * @param createrId
	 * @return List<Project>
	 * @throws Exception
	 */
	public List<Project> getMyProjectByCreateId(long createrId) throws Exception;
	
	/**
	 * 根据状态获取创建的项目列表
	 * @return List<Project>
	 * @throws Exception
	 */
	public List<Project> getProjectsByStatus(int status) throws Exception;
	
	/**
	 * 根据项目id返回项目详情信息
	 * @param projectId
	 * @return Project
	 */
	public ProjectVO getProjectDetail(long projectId)throws Exception;
	

	/**
	 * 推荐项目参数查询 接口
	 * @param industryId 行业id
	 * @param areaId  地区id
	 * @throws Exception e
	 * @return List<Project> 
	 */
	public List<Publish> getProjectListByParameters(long time,int pageSize,Map<String, String> params) throws Exception;
	
	/**
	 * 
	 * 根据组织id 查询项目列表
	 * @param organizationId 组织id
	 * @return List<Project> 
	 */
	public List<Project> getProjectListByOrganizationId(long organization)throws Exception;
	
	/**
	 * 功能描述 ：根据行业id 返回 行业名称 (从顶层开始)
	 * 
	 */
	public String convertIndustryId(long id);
	
	/**
	 * 功能描述 ：根据地区id 返回 地区名称 (从顶层开始)
	 * 
	 */
	 public String convertAreaId(long id);
}
