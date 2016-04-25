package com.ginkgocap.tongren.organization.application.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.application.model.Module;
import com.ginkgocap.tongren.organization.application.model.OrganizationModuleApplication;
import com.ginkgocap.tongren.organization.application.service.OrganizationModuleApplicationService;
import com.ginkgocap.tongren.organization.application.service.ModuleService;

@Service("organizationModuleApplicationService")
public class OrganizationModuleApplicationServiceImpl extends AbstractCommonService<OrganizationModuleApplication> implements OrganizationModuleApplicationService{

	private static final Logger logger = LoggerFactory.getLogger(OrganizationModuleApplicationServiceImpl.class);
	
	@Autowired
	private ModuleService moduleService;
	
	@Override
	public List<OrganizationModuleApplication> createDefault(long createrId, long organizationId) throws Exception{
		List<Long> ids = moduleService.getKeysByParams("Module_List_createId_id", 0L);
		List<OrganizationModuleApplication> organizationModuleApplications = new ArrayList<OrganizationModuleApplication>();
		try {
			for (Long id : ids) {
				Module m = moduleService.getEntityById(id);
				OrganizationModuleApplication ma = new OrganizationModuleApplication();
				Timestamp t = new Timestamp(System.currentTimeMillis());
				ma.setCreateTime(t);
				ma.setUpdateTime(t);
				ma.setCreaterId(createrId);
				ma.setModuleId(m.getId());
				ma.setOrganizationId(organizationId);
				OrganizationModuleApplication p_ma = save(ma);
				organizationModuleApplications.add(p_ma);
			}			
		} 
		catch (Exception e) {
			for (OrganizationModuleApplication oma : organizationModuleApplications) {
				deleteEntityById(oma.getId());
			}
			logger.error(e.getMessage()+"创建默认应用模块出错!",e);
			throw e;
		}
		return organizationModuleApplications;
	}
	
	@Override
	protected Class<OrganizationModuleApplication> getEntity() {
		return OrganizationModuleApplication.class;
	}

	@Override
	public Map<String, Object> doWork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		// TODO Auto-generated method stub
		return null;
	}

}
