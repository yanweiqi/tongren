package com.ginkgocap.tongren.organization.application.service.impl;

import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.application.dao.OrganizationDao;
import com.ginkgocap.tongren.organization.application.model.Module;
import com.ginkgocap.tongren.organization.application.service.ModuleService;
import com.ginkgocap.tongren.organization.system.model.OrganizationModules;

@Service("moduleService")
public class ModuleServiceImpl extends AbstractCommonService<Module> implements ModuleService{
	
	@Resource
	private OrganizationDao organizationDao;
	
	private static final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);
	
	@Override
	public void createDefault(long createrId,long organizationId) {
		
		for(OrganizationModules e_module: OrganizationModules.values()){
			Module module = new Module();
			Timestamp t = new Timestamp(System.currentTimeMillis());
			module.setCreateTime(t);
			module.setUpdateTime(t);
			module.setName(e_module.name());
			module.setStatus(e_module.getStatus());
			module.setCreaterId(createrId);
			module.setDescription(e_module.getValue());
			module.setOrganizationId(createrId);
			save(module);
		}
	}
	
	public void testOrgName(){
		logger.info("orgName is --> "+organizationDao.getOrgNameById(3915306552786959l));
	}
	
	@Override
	protected Class<Module> getEntity() {
		return Module.class;
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
