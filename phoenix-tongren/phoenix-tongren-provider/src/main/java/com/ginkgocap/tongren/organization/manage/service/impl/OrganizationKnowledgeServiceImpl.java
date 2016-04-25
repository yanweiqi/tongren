package com.ginkgocap.tongren.organization.manage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;
import com.ginkgocap.tongren.organization.document.service.DocumentCatalogueService;
import com.ginkgocap.tongren.organization.document.service.DocumentTagsService;
import com.ginkgocap.tongren.organization.manage.dao.OrganizationManagerDao;
import com.ginkgocap.tongren.organization.manage.model.OrganizationKd;
import com.ginkgocap.tongren.organization.manage.service.OrganizationKnowledgeService;

@Service("organizationKnowledgeService")
public class OrganizationKnowledgeServiceImpl extends AbstractCommonService<OrganizationKd> implements OrganizationKnowledgeService {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationKnowledgeServiceImpl.class);
	
	@Autowired
	private DocumentCatalogueService documentCatalogueService;
	
	@Resource
	private OrganizationManagerDao organizationManagerDao;
	@Autowired
	private DocumentTagsService documentTagsService;
	
	
	@Override
	public OrganizationKd createOrganizationKnowledge(OrganizationKd kd) {
		
		long organizationId = kd.getOrganizationId();
		try{
		
		kd = save(kd);
		if(kd != null){
			organizationId = Long.parseLong("40"+organizationId);
			DocumentCatalogue dc=documentCatalogueService.getDefaultCatalog(ConfigService.ORG_DEF_USER_ID, organizationId);
			long[] cidArray=new long[]{dc.getId()};
			String code=documentCatalogueService.setDocumentToDirsIfAbsent(ConfigService.ORG_DEF_USER_ID, organizationId, kd.getId(), cidArray);
			logger.info("save persion info to default dir "+dc.getId()+""+code);
		}
		}catch(Exception e){
			
			e.printStackTrace();
			logger.info("createOrganizationKnowledge is error knowledgeId:"+kd.getKnowledgeId()+",userId :"+kd.getUserId()+",orgId" +organizationId);
		}
		return kd;
	}


	@Override
	public OrganizationKd getKdByKnowledgeIdAndOrgId(long knowledgeId,long orgId) throws Exception {
		
			OrganizationKd kd = null;
		try{
			Long id = getMappingByParams("OrganizationKd_knowledgeId_orgId",new Object[]{knowledgeId,orgId});
			
			if(id == null){return kd;}
			kd = getEntityById(id);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return kd;
	}
	


	@Override
	public OrganizationKd getKdById(long orgKdId) throws Exception {
		
		OrganizationKd kd = null;
		try{
			kd = getEntityById(orgKdId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return kd;
	}
	
	
	
	@Override
	protected Class<OrganizationKd> getEntity() {
		return OrganizationKd.class;
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
	public Page<OrganizationKd> getOrganizationKdPage(Page<OrganizationKd> page) throws Exception {
		
		try{
			String paramStr = null;
			if( page.getParam("paramStr")!=null){
				paramStr=page.getParam("paramStr").toString();
			}
			Long organizationId = Long.valueOf(page.getParam("organizationId").toString());
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("orgId", organizationId);
			
			if(paramStr!=null&&paramStr.length()>0){
				map.put("lstr", paramStr);
			}
			map.put("start", page.getStart());
			map.put("size", page.getSize());
			List<OrganizationKd> result = organizationManagerDao.getOrganizationKdByOrgidAndName(map);
			int count=organizationManagerDao.getOrganizationKdByOrgidAndNameCount(map);
			
			page.setResult(result);
			page.setTotalCount(count);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}


	@Override
	public boolean delectOrganizationKnowledge(long id) throws Exception {
		
		boolean  result = false;
		try {
			OrganizationKd kd = getEntityById(id);
			if(kd == null){
				logger.info("delectOrganizationKnowledge organizationKd is null, knowledgeId--->"+ id);
				return false;
			}
			long orgId = kd.getOrganizationId();
			result = deleteEntityById(id);
			
			documentCatalogueService.delResourceOfCatalog(id, orgId, ConfigService.ORG_DEF_USER_ID);
			documentTagsService.delAllTagsOfSource(id, orgId);
			
		} catch (Exception e) {
			logger.info("delectOrganizationKnowledge is error knowledgeId --->" + id);
			e.printStackTrace();
		}
		return result;
	}
}
