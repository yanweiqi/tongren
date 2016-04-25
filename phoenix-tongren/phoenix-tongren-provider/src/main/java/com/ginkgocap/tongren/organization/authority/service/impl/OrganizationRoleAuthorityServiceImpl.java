package com.ginkgocap.tongren.organization.authority.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.model.OrganizationRoleAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationAuthorityService;
import com.ginkgocap.tongren.organization.authority.service.OrganizationRoleAuthorityService;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.system.model.AdminRoleHasAuthorities;
import com.ginkgocap.tongren.organization.system.model.CreaterRoleHasAuthorities;
import com.ginkgocap.tongren.organization.system.model.DepartmentLeaderRoleHasAuthorities;
import com.ginkgocap.tongren.organization.system.model.GroupLeaderRoleHasAuthorities;
import com.ginkgocap.tongren.organization.system.model.MemberRoleHasAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;

@Service("organizationRoleAuthorityService")
public class OrganizationRoleAuthorityServiceImpl extends AbstractCommonService<OrganizationRoleAuthority> implements OrganizationRoleAuthorityService{

	private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleAuthorityServiceImpl.class);
	
	@Autowired
	private  OrganizationRoleService organizationRoleService;
	
	@Autowired
	private OrganizationAuthorityService organizationAuthorityService;
	
	private static Map<String, OrganizationAuthority> oaMap = null;
	
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);  
	
	@Override
	public List<OrganizationRoleAuthority> addRoleAuthorities(long createrId,long roleId,long organizationId,List<String> authorityNames) throws Exception {
		List<OrganizationRoleAuthority> organizationRoleAuthoritys = new ArrayList<OrganizationRoleAuthority>();;
		try {
			for (String authorityName : authorityNames) {
			    OrganizationAuthority organizationAuthority = organizationAuthorityService.getDefaultAuthoritysByName(authorityName);
			    if(null != organizationAuthority){
			       OrganizationRole or = organizationRoleService.getEntityById(roleId);
			       if(null != or){
			    	   OrganizationRoleAuthority ora = new OrganizationRoleAuthority();
			    	   ora.setAuthorityId(organizationAuthority.getId());
			    	   ora.setCreaterId(createrId);
			    	   Timestamp t = new Timestamp(System.currentTimeMillis());
			    	   ora.setCreateTime(t);
			    	   ora.setUpdateTime(t);
			    	   ora.setOrganizationId(organizationId);
			    	   ora.setRoleId(roleId);
			    	   OrganizationRoleAuthority p_ora = save(ora);
			    	   organizationRoleAuthoritys.add(p_ora);
			       }
			       else {
			    	   logger.warn("AuthorityName:"+authorityName+",status:is Not Exist");
			       }
			    }
			    else {
			    	logger.warn("roleId:"+roleId+",status:is Not Exist");
				}
			}
		} catch (Exception e) {
			for (OrganizationRoleAuthority ora:organizationRoleAuthoritys) {
				deleteEntityById(ora.getId());
				logger.warn("OrganizationRoleAuthority Id"+ora.getId()+",action:del");
			}
			logger.error(e.getMessage(),e);
			throw e;
		}
		return organizationRoleAuthoritys;
	}
	
	@Override
	public List<Long> getAuthorityByRoleId(long roleId, long organizationId) {
		return getKeysByParams("organizationRoleAuthority_list_roleId_organizationId", new Object[]{roleId,organizationId});
	}
	
	@Override
	public List<OrganizationRoleAuthority> createDefault(final OrganizationRole organizationRole) throws Exception{
		Set<OrganizationAuthorities> setOA = new HashSet<OrganizationAuthorities>();
		final Set<OrganizationRoleAuthority> setORA = new HashSet<OrganizationRoleAuthority>();
		try {
            if(oaMap == null){
               List<OrganizationAuthority> oas = organizationAuthorityService.getDefaultAuthoritys();	
               oaMap = new HashMap<String, OrganizationAuthority>();
               for (OrganizationAuthority organizationAuthority : oas) {
            		oaMap.put(organizationAuthority.getAuthorityName(), organizationAuthority);
			   }
            }
			if(organizationRole.getRoleName().equals(OrganizationRoles.CREATER.name())){
			   setOA = CreaterRoleHasAuthorities.getInstance().getAuthorities(OrganizationRoles.CREATER);
			}
			else if(organizationRole.getRoleName().equals(OrganizationRoles.ADMIN.name())){
			   setOA = AdminRoleHasAuthorities.getInstance().getAuthorities(OrganizationRoles.ADMIN);
			}
			else if(organizationRole.getRoleName().equals(OrganizationRoles.DEPARTMENT_LEADER.name())){
				setOA = DepartmentLeaderRoleHasAuthorities.getInstance().getAuthorities(OrganizationRoles.DEPARTMENT_LEADER);
			}
			else if(organizationRole.getRoleName().equals(OrganizationRoles.GROUP_LEADER.name())){
				setOA = GroupLeaderRoleHasAuthorities.getInstance().getAuthorities(OrganizationRoles.GROUP_LEADER);
			}
			else if(organizationRole.getRoleName().equals(OrganizationRoles.MEMBER.name())){
				setOA = MemberRoleHasAuthorities.getInstance().getAuthorities(OrganizationRoles.MEMBER);
			}
			final CountDownLatch cdl=new CountDownLatch(setOA.size());
			long t1=System.currentTimeMillis();
			for (OrganizationAuthorities or : setOA) {
				 final OrganizationRoleAuthority ora = new OrganizationRoleAuthority();
				 Timestamp t = new Timestamp(System.currentTimeMillis());
				 ora.setCreateTime(t);
				 ora.setUpdateTime(t);
				 ora.setCreaterId(organizationRole.getCreaterId());
				 ora.setOrganizationId(organizationRole.getOrganizationId());
				 /**
				 for(OrganizationAuthority oa: oas){
					 if(oa.getAuthorityName().equals(or.name())){
						long organizationAuthorityId = oa.getId();
						ora.setAuthorityId(organizationAuthorityId);	 
					 }
				 }**/
				 if(oaMap.containsKey(or.name())){
					long organizationAuthorityId = oaMap.get(or.name()).getId();
					ora.setAuthorityId(organizationAuthorityId);	
				 }
				 fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							ora.setRoleId(organizationRole.getId());
							OrganizationRoleAuthority p_ora = save(ora);
							setORA.add(p_ora);
						} finally {
							cdl.countDown();
						}
					}
				});
			}
			logger.info("create organization cost "+(System.currentTimeMillis()-t1)+" ms");
			cdl.await();
		} 
		catch (Exception e) {
			for (OrganizationRoleAuthority ora : setORA) {
				deleteEntityById(ora.getId());
			}
			logger.error(e.getMessage()+"创建默认权限失败!",e);
			throw e;
		}
		List<OrganizationRoleAuthority> list = new ArrayList<OrganizationRoleAuthority>();
		list.addAll(setORA);
		return list;
	}
	
	@Override
	protected Class<OrganizationRoleAuthority> getEntity() {
		return OrganizationRoleAuthority.class;
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
