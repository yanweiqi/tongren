package com.ginkgocap.tongren.organization.manage.service;



import java.util.List;
import java.util.Map;
import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationVO;
import com.ginkgocap.tongren.organization.manage.vo.RecommendVO;

public interface TongRenOrganizationService extends CommonService<Organization>{
	
	/**
	 * 获取铜人菜单资源数
	 */
	public Map<String, Object> getMenuSize(long userId,Map<String, Object> responseData)throws Exception;
	/**
	 * 查询我参与的组织
	 * @param userId
	 * @param status
	 * @return List: Organization
	 */
	public List<OrganizationVO> getMyJoinOrganization(long userId)throws Exception;

	/**
	 * 获取我参与的组织集合
	 * @param userId
	 * @return List:organizationId
	 */
	public List<Organization> getMyOrganizationIds(long userId)throws Exception;

	/**
	 * 我创建的组织
	 * @param createrId 创建者userId
	 * @param status 0:正常 1:解散
	 * @return List: organizations
	 */
	public List<OrganizationVO> getMyCreateOrganizations(long createrId,int status)throws Exception;

	/**
	 * 解散组织
	 * @param id 组织Id
	 * 
	 */
	public boolean disband(long id)throws Exception;

	/**
	 * 创建组织 
	 * @param o 组织对象  
	 * @return Organization 
	 */
	public Organization create(Organization o) throws Exception;

	/**
	 * 组织详情
	 * @param oid 组织Id
	 * @return Organization
	 */
	public Organization getOrganizationById(long oid)throws Exception;
	
	/**
	 * 查询分页查询有效组织
	 * 
	 */
	public Page<RecommendVO> queryOrganizationPage(Page<RecommendVO> page) throws Exception;
	
	/**
	 * 组织名称查询有效组织集合
	 * 
	 */
	public List<RecommendVO> getOrganizationByName(String orgName) throws Exception;
	
	/**
	 * 根据组织Id获取组织详细信息
	 * @param id 组织ID
	 * @return Organization
	 * @throws Exception
	 */
	public Organization getOrganizationDetailById(long id) throws Exception;
	
	/**
	 * 
	 * 根据类型获取推荐组织
	 * @param type 0:按创建时间倒序  1：完成项目数 排序
	 * 
	 */
	 public Page<OrganizationVO> getOrganizationListByType(Page<OrganizationVO> page) throws Exception;
	 
	 
	/**
	 * 根据名称或者拼音查询所有的组织id
	 * @param orgName
	 * @return
	 * @throws Exception
	 */
	public List<Long> queryOranizationByNameOrSpell(String orgName) throws Exception;
	
	/**
	 * 转化所有name_spell为空 的字段
	 * @throws Exception
	 */
	public void updateAllNameSpell() throws Exception;
	
	/**
	 * 组织创建者移交
	 * @param organizationId 组织id
	 * @param receiveId 接纳人Id
	 */
	
	public String creatorTurnOver(long organizationId , long receiveId)throws Exception;
	
	/**
	 * 获取我在组织是创建者或管理员的组织
	 */
	public List<OrganizationVO> getMyCreateOrganizations(long createrId,int status,boolean isContainAdmin) throws Exception;
	 
}
