package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;
import java.util.Map;
import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationMemberVO;

/**
 * @author yanweiqi
 *
 */
public interface OrganizationMemberService extends CommonService<OrganizationMember> {
	
	/**
	 * 添加创建者
	 * @param userId
	 * @param organizationId
	 * @return
	 */
	public OrganizationMember addCreater(long userId,long organizationId) throws Exception;

	/**
	 * 查询组织成员
	 * @param userId 用户ID
	 * @param organizationId 组织ID
	 */
	public OrganizationMember getMemberByOrganizationIdAndUserId(long organizationId,long userId);

	/**
	 * 查询组织所有成员信息
	 * @param organizationId 组织Id
	 * @param type 1:所有组织成员  包含申请退出  3:不包含申请退出
	 */
	public List<OrganizationMemberVO> getOrganizationAllMemberInfo(long organizationId,int type);

	/**
	 * 根据状态查询组织成员
	 * 
	 */
	public List<OrganizationMember> getOrganizationMember(long organizationId,int status)throws Exception ;
	
	/**
	 * 查询组织正式成员集合
	 */
	public List<OrganizationMember> getNormalMember(long organizationId)throws Exception ;
	
	/**
	 * 邀请成员
	 * @param orgId 组织Id
	 * @param maps key ： 成员加入方式 1我的好友、2搜索、3系统推荐   value 邀请成员ids集合
	 * @param content 发送邀请消息内容
	 * @return  
	 */
	public Map<String,Object> invite(long orgId,Map<Integer,String[]> maps,String content)throws Exception;
	
	/**
	 * 添加组织成员
	 * @param userId
	 * @param organizationId
	 * @param createrId
	 * @param joinStatus
	 * @param joinType :添加组织成员类型 1:组织邀请  2：申请加入
	 * @return OrganizationMember:
	 */
	public OrganizationMember addMember(long userId,long organizationId,long createrId,int joinStatus,int joinType) throws Exception;

	/**
	 * 
	 * 退出组织
	 * @param orgId 组织Id
	 * @param userId 用户Id
	 * @return  1:已提交申请   2:申请退出成功 3：申请退出失败 4：组织成员不存在该组织下
	 * 
	 */
	public String exit(long orgId,long userId)throws Exception ;
	
	/**
	 * 根据userId获取用户的成员信息
	 * @param userId
	 * @return
	 */
    public List<OrganizationMember> getMyOrganizationMembers(long userId) ;
    
    /**
     * 根据OrganizationId,UserId获取用户的详细信息
     * @param organizationId
     * @param userId
     * @return OrganizationMember
     */
    public OrganizationMember getMyOrganizationMemberDetail(long organizationId,long userId) throws Exception;
    
    /**
     * 功能描述：根据用户ID和组织ID和传输的消息操作状态（通过消息、忽略消息）操作组织成员-         
     *                                                       
     * @param statusMessage 1:通过 2:忽略
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return ture 操作成功                 false 操作失败                                                                                               
     * @author 林阳 [linyang@gintong.com]
     * @since 2015年10月28日
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     *
     */
    public boolean operateOrganizationMember(int statusMessage,long userId,long organizationId) throws Exception;
    
    
    /**
     * 功能描述：删除成员
     * 
     */
    public boolean delMember(long organizationId,long userId)throws Exception ;
    
    /**
     * 功能描述 :删除成员以及关联信息
     */
    public boolean delMemberAndRoleRelation(long organizationId,long userId) throws Exception;
    
    
    /**
     * 功能描述:根据Id获取组织成员详细信息
     * @param id
     * @return
     * @throws Exception
     */
    public OrganizationMember getOrganizationMemberDetailById(long id) throws Exception;
    
    /**
     * 功能描述 ：根据oid查询组织下的成员 不包含 组织邀请成员
     * 
     */
    public List<OrganizationMemberVO> getOrganizationMemberAll(long oid) throws Exception; 
    
}
