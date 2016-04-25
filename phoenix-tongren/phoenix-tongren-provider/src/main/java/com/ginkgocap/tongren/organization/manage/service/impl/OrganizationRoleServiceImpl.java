package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.authority.service.NoPermissionException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

@Service("organizationRoleService")
public class OrganizationRoleServiceImpl extends AbstractCommonService<OrganizationRole> implements OrganizationRoleService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleServiceImpl.class);
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
		
	@Override
	public List<OrganizationRole> getOrganizationRoles(long organizationId) throws Exception{
		List<OrganizationRole> roles = null;
		List<Long> ids = getKeysByParams("organizationRole_List_id", new Object[]{organizationId});
		if(null != ids && ids.size()>0){
			roles = getEntityByIds(ids);
		}
		return roles;
	}
	
	@Override
	public OrganizationRole addRole(long createrId, long organizationId,String roleName,String description) throws Exception{
		OrganizationRole  p_or = null;
		try {
			 OrganizationRole check_or = getOrganizationRoleByOrganizationIdAndRoleName(organizationId,roleName);
			 if(null == check_or){
				 OrganizationRole  or = new OrganizationRole();
			     Timestamp t = new Timestamp(System.currentTimeMillis());
				 or.setCreateTime(t);
				 or.setUpdateTime(t);
				 or.setCreaterId(createrId);
				 or.setOrganizationId(organizationId);
				 or.setDescription(description);
				 or.setRoleName(roleName);
				 p_or = save(or);
			 }
			 else{
				 logger.warn("organizationId:"+organizationId+",roleName:"+roleName+","+ApiCodes.OrganizationRoleIsExist.getMessage());
			 }
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_or;
	}
	
	@Override
	public List<OrganizationRole> createDefault(long createId,long organizationId) throws Exception{
		List<OrganizationRole> orList = new ArrayList<OrganizationRole>();
		try {
			for (OrganizationRoles ors : OrganizationRoles.values()) {
				 OrganizationRole  or = new OrganizationRole();
				 Timestamp t = new Timestamp(System.currentTimeMillis());
				 or.setCreateTime(t);
				 or.setUpdateTime(t);
				 or.setCreaterId(createId);
				 or.setOrganizationId(organizationId);
				 or.setDescription(ors.getValue());
				 or.setRoleName(ors.name());
				 orList.add(save(or)) ;
			}
		} 
		catch (Exception e) {
			for (OrganizationRole or:orList) {
				deleteEntityById(or.getId());
			}
			logger.error(e.getMessage()+"创建默认角色失败!",e);
			throw e;
		}
		return orList;
	}

	@Override
	public List<OrganizationRole> getMyOrganizationRole(long userId,long organizationId) throws Exception{	
		List<OrganizationRole> organizationRoles = null;
		try {
			OrganizationMember om = organizationMemberService.getMemberByOrganizationIdAndUserId(organizationId, userId);
			if(null != om){
				List<Long> roleIds = organizationMemberRoleService.getMyRoleIds(om.getId(), organizationId);
				if(null != roleIds){
					organizationRoles =	getEntityByIds(roleIds);
				} 
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return organizationRoles;
	}	
	
	@Override
	public OrganizationRole getOrganizationRoleByOrganizationIdAndRoleName(long organizationId,String roleName) throws Exception{
		OrganizationRole organizationRole = null;
		try {
			Long id = getMappingByParams("organizationRole_map_roleName_organizationId", new Object[]{roleName,organizationId});
			if(id != null){
			   organizationRole = getEntityById(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return organizationRole;
	}
	
	@Override
	public OrganizationRole updateRole(String roleName, String description,long roleId) throws NoPermissionException {
		OrganizationRole organizationRole = getEntityById(roleId);
		if(null != organizationRole){
		   Timestamp t = new Timestamp(System.currentTimeMillis());
		   organizationRole.setUpdateTime(t);
		   if(StringUtils.isNotBlank(description)) organizationRole.setDescription(description);
		   if(StringUtils.isNoneBlank(roleName))   organizationRole.setRoleName(roleName);
		   boolean isUpdateStatus = update(organizationRole);
		   if(isUpdateStatus){
			  return getEntityById(roleId);
		   }
		}
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
	public Class<OrganizationRole> getEntity() {
		return OrganizationRole.class;
	}
	
	@Override
	public Map<String, Object> doWork() {
		return null;
	}
	
	private static Map<String, Integer> roleWeight=new HashMap<String, Integer>();
	static{
		roleWeight.put("CREATER", 10);
		roleWeight.put("ADMIN", 9);
	}
	@Override
	public int getRoleWeihtByName(String roleName) {
		if(roleWeight.containsKey(roleName)){
			return roleWeight.get(roleName);
		}
		return 0;
	}

}
