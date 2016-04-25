package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.manage.exception.MemberRoleException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;

/**
 * 组织成员角色
 * @author yanweiqi
 */
@Service("organizationMemberRoleService")
public class OrganizationMemberRoleServiceImpl extends AbstractCommonService<OrganizationMemberRole>  implements OrganizationMemberRoleService {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationMemberRoleServiceImpl.class);
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Override
	public List<OrganizationMemberRole> getMemberRoleByOrganizationIdAndRoleId(long organizationId, long roleId) throws Exception {
		List<OrganizationMemberRole> memberRoles = null;
		try {
			String sql_list_name = "organizationMemberRole_list_organizationId_roleId";
			List<Long> ids = getKeysByParams(sql_list_name, new Object[]{organizationId,roleId});
			if(null != ids && ids.size() > 0){
				memberRoles = getEntityByIds(ids);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return memberRoles;
	}
	
	@Override
	public OrganizationMemberRole getMemberRoleByOrganizationIdAndMemerId(long organizationId,long memberId) throws Exception {
		return getMemberRoleByOrganizationIdAndMemerId(organizationId,memberId,3);
	}
	/**
	 * type 1 创建者 2 管理员 3 返回级别最高的一个
	 */
	public OrganizationMemberRole getMemberRoleByOrganizationIdAndMemerId(long organizationId,long memberId,int type) throws Exception{
		logger.info("get member role "+organizationId+","+memberId+","+type);
		OrganizationMemberRole memberRole = null;
		try {
			List<Long> memberRoleId=getKeysByParams("OrganizationMemberRole_list_memberId_organizationId2", new Object[]{memberId,organizationId});
			if(memberRoleId!=null){
				String lastRoleName=null;
				
				for(Long mrid:memberRoleId){
					OrganizationMemberRole omr=getEntityById(mrid);
					OrganizationRole role=organizationRoleService.getEntityById(omr.getRoleId());
					if(type==1&&role.getRoleName().equals("CREATER")){
						memberRole=omr;
						break;
					}else if(type==2&&role.getRoleName().equals("ADMIN")){
						memberRole=omr;
						break;
					}else if(type==3){//按创建者 管理员 普通成员角色返回
						if(memberRole==null||organizationRoleService.getRoleWeihtByName(lastRoleName)<organizationRoleService.getRoleWeihtByName(role.getRoleName())){
							memberRole=omr;
							lastRoleName=role.getRoleName();
						}
					}
						
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+","+ApiCodes.MemberRoleGetFail.getMessage(),e);
			throw e;
		}
		logger.info("memberRole is "+(memberRole==null?null:memberRole.getId()));
		return memberRole;
	}
	
	/**
	 * 一个组织成员最多可有两种角色，管理员角色和普通成员角色
	 * 设置组织成员角色，5种情况
	 * 1  目前不是任何角色，直接增加
	 * 2  目前是普通角色，传入的角色id为普通角色，修改为成员角色为传入角色id
	 * 3 目前是普通角色，传入的角色id为管理员  直接增加
	 * 4 目前是管理员角色，传入的角色id为普通角色 直接增加
	 * 5 目前是管理员角色  传入的角色id为管理员  修改update时间字段
	
	 */
	@Override
	public OrganizationMemberRole addMemberRole(long createrId, long organizationId,long roleId,long memberId) throws MemberRoleException, Exception {
		OrganizationMemberRole p_omrg = null;
		try {	
			OrganizationMemberRole memberRole = null;
			OrganizationRole organizationRole = organizationRoleService.getEntityById(roleId);
			if(organizationRole.getRoleName().equals("ADMIN")){//设置管理员角色
				 memberRole = getMemberRoleByOrganizationIdAndMemerId(organizationId,memberId,2);
			}else{
				 memberRole = getMemberRoleByOrganizationIdAndMemerId(organizationId,memberId);
			}
			if(null == memberRole){
				logger.info("create role "+organizationId+","+memberId+","+roleId);
				p_omrg=saveMemberRole(createrId,organizationId,roleId,memberId);
			}
			else{
				Timestamp t = new Timestamp(System.currentTimeMillis());
				memberRole.setUpdateTime(t);
				memberRole.setRoleId(roleId);
				boolean update_status = update(memberRole);
				if(update_status){
				   p_omrg = getEntityById(memberRole.getId());
				   logger.info("Update status:success"+".Id:"+p_omrg.getId()+","+"MemberId:"+p_omrg.getOrganizationMemberId()+",RoleId"+p_omrg.getRoleId()+",OrganizationId:"+p_omrg.getOrganizationId()); 
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_omrg;
	}
	
	private OrganizationMemberRole saveMemberRole(long createrId, long organizationId,long roleId,long memberId){
		OrganizationMemberRole p_omrg = null;
		OrganizationMemberRole omrg = new OrganizationMemberRole();
		   Timestamp t = new Timestamp(System.currentTimeMillis());
		   omrg.setCreateTime(t);
		   omrg.setUpdateTime(t);
		   omrg.setCreaterId(createrId);
		   omrg.setOrganizationId(organizationId);
		   omrg.setOrganizationMemberId(memberId);
		   omrg.setRoleId(roleId);	
		   p_omrg = save(omrg);
		   logger.info("Save status:success"+".Id:"+p_omrg.getId()+","+"MemberId:"+p_omrg.getOrganizationMemberId()+",RoleId"+p_omrg.getRoleId()+",OrganizationId:"+p_omrg.getOrganizationId());
		   return p_omrg;
	}
	
	@Override
	public Long getOrganizationMemberRoleIdByOrganizationIdAndRoleIdAndMemberId(long organizationId,long roleId,long memberId){
		return getMappingByParams("organizationMemberRole_map_organizationId_roleId_memberId", new Object[]{organizationId,roleId,memberId});
	}
	
	@Override
	public OrganizationMemberRole getMemberRoleIdByOrganizationIdAndRoleIdAndMemberId(long organizationId,long roleId,long memberId) throws Exception{
		OrganizationMemberRole or = null;
		Long id = getOrganizationMemberRoleIdByOrganizationIdAndRoleIdAndMemberId(organizationId, roleId, memberId);
		if(null != id) or = getEntityById(id);
		return or;
	}
	
	@Override
	public OrganizationMemberRole addMemberRole(OrganizationMember om, long roleId) throws Exception{
		Long id = getOrganizationMemberRoleIdByOrganizationIdAndRoleIdAndMemberId(om.getOrganizationId(), roleId,om.getId());
		OrganizationMemberRole omr = null;
		if(null == id){
			try {
				OrganizationMemberRole omrg = new OrganizationMemberRole();
				Timestamp t = new Timestamp(System.currentTimeMillis());
				omrg.setCreateTime(t);
				omrg.setUpdateTime(t);
				omrg.setCreaterId(om.getCreaterId());
				omrg.setOrganizationId(om.getOrganizationId());
				omrg.setOrganizationMemberId(om.getId());
				omrg.setRoleId(roleId);
				omr = save(omrg);
			} catch (Exception e) {
				logger.error(e.getMessage()+"保存成员角色错误!",e);
				throw e;
			}
		}
		else{
			logger.warn("organizationId:"+om.getOrganizationId()+",memberId"+om.getId()+"," +ApiCodes.OrganizationMemberHasBeanOneRoler.getDescription());
		}
		return omr;
	}
	
	@Override
	public List<Long> getMyRoleIds(long memberId,long organizationId) {
		return getKeysByParams("OrganizationMemberRole_list_memberId_organizationId", new Object[]{memberId,organizationId});
	}
	
	/**
	 * 取消成员的管理员角色
	 */
	@Override
	public void deleteAdminRole(long organizationId, long memberId) throws Exception {
		OrganizationMemberRole adminRole=getMemberRoleByOrganizationIdAndMemerId(organizationId,memberId,2);
		if(adminRole!=null){
			deleteEntityById(adminRole.getId());
			logger.info("delete admin role success --〉 "+organizationId+","+memberId);
		}else{
			logger.warn("not exist admin role--〉 "+organizationId+","+memberId);
		}
		
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
	protected Class<OrganizationMemberRole> getEntity() {
		return OrganizationMemberRole.class;
	}

	@Override
	public void setParameterMap(Map<String, Object> parameterMap) {		
	}

	@Override
	public List<OrganizationMemberRole> getAllAdminRole(long organizationId) throws Exception {
		logger.info("get All AdminRole "+organizationId);
		OrganizationRole adminRole= organizationRoleService.getOrganizationRoleByOrganizationIdAndRoleName(organizationId, "ADMIN");
		return getMemberRoleByOrganizationIdAndRoleId(organizationId,adminRole.getId());
	}

	
	
}
