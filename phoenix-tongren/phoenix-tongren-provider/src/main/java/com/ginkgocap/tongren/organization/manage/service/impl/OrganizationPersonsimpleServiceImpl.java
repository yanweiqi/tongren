package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.ConfigService;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.document.model.DocumentCatalogue;
import com.ginkgocap.tongren.organization.document.service.DocumentCatalogueService;
import com.ginkgocap.tongren.organization.document.service.DocumentTagsService;
import com.ginkgocap.tongren.organization.manage.dao.OrganizationManagerDao;
import com.ginkgocap.tongren.organization.manage.model.OrganizationPs;
import com.ginkgocap.tongren.organization.manage.service.OrganizationPersonsimpleService;
import com.ginkgocap.ywxt.person.model.PersonSimple;
import com.ginkgocap.ywxt.person.service.PersonSimpleService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("organizationPersonsimpleService")
public class OrganizationPersonsimpleServiceImpl  extends AbstractCommonService<OrganizationPs> 
implements OrganizationPersonsimpleService{

	private static final Logger logger = LoggerFactory.getLogger(OrganizationPersonsimpleServiceImpl.class);
	
	@Autowired
	private PersonSimpleService personSimpleService;
	
	@Autowired
	private DocumentCatalogueService documentCatalogueService; 
	
	@Autowired
	private OrganizationManagerDao organizationManagerDao;
	@Autowired
	private UserService userService;
	@Autowired
	private DocumentTagsService documentTagsService;
	
	
	@Override
	public OrganizationPs createOrganizationPs(long personId, long userId,long orgId,String fromType) throws Exception{

		PersonSimple ps = null;
		OrganizationPs ops = null;
		OrganizationPs organizationPs = null;
		List<Long> personIds = new ArrayList<Long>();
		personIds.add(personId);
		try{
			logger.info("createOrganizationPs  fromType --->" + fromType );
			if(StringUtils.isBlank(fromType) || fromType.equals("null")){
				User user = userService.selectByPrimaryKey(personId);
				if(user == null){
					logger.info("get selectByPrimaryKey user is null:" + personIds);
					return ops;
				}
				organizationPs = new OrganizationPs();
				organizationPs.setUserId(userId);
				organizationPs.setGender(user.getSex() == 1? 0:1);
				organizationPs.setPersonId(personId);
				organizationPs.setPersonName(user.getName());
				organizationPs.setPicPath(user.getPicPath());
				organizationPs.setCompany(user.getCompanyName());
				organizationPs.setCreateTime(new Timestamp(System.currentTimeMillis()));
				organizationPs.setOrganizaitonId(orgId);
				ops = save(organizationPs);
				
			}else{
				List<PersonSimple> result = personSimpleService.findByPeIdsAndPeTypeAndCtime(personIds, 2, null);
				if(CollectionUtils.isEmpty(result)){
					logger.info("get findByPeIdsAndPeTypeAndCtime is null:" + personIds);
					return ops;
				}
				ps = result.get(0);
				
				organizationPs = new OrganizationPs();
				organizationPs.setUserId(userId);
				organizationPs.setGender(ps.getGender());
				organizationPs.setPersonId(ps.getPersonid());
				organizationPs.setPersonName(ps.getName1()+ps.getName2());
				organizationPs.setPicPath(ps.getPicpath());
				organizationPs.setCompany(ps.getCompany());
				organizationPs.setCreateTime(new Timestamp(System.currentTimeMillis()));
				organizationPs.setOrganizaitonId(orgId);
				ops = save(organizationPs);
			}
			
			if(ops != null){
				orgId=Long.parseLong("30"+orgId);
				DocumentCatalogue dc=documentCatalogueService.getDefaultCatalog(ConfigService.ORG_DEF_USER_ID, orgId);
				long[] cidArray=new long[]{dc.getId()};
				String code=documentCatalogueService.setDocumentToDirsIfAbsent(ConfigService.ORG_DEF_USER_ID, orgId, ops.
						getId(), cidArray);
				logger.info("save persion info to default dir "+dc.getId()+""+code);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("createOrganizationPs is error personId :"+personId+",userId :"+userId+"orgId :"+orgId);
		}
		return ops;
	}

	@Override
	public OrganizationPs getPsByPsIdAndOrganizationId(long personId, long orgId) throws Exception {
		
		OrganizationPs ps = null;
		
		try{
			Long id = getMappingByParams("OrganizationPs_personId_orgId",new Object[]{personId,orgId});
			if(id != null){
				ps = getEntityById(id);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("getPsByPsIdAndOrganizationId is error personId :"+personId,",orgId :" +orgId);
		}
		return ps;
	}
	
	@Override
	public OrganizationPs getOrgPsById(long organizationPsId) throws Exception {
		
		OrganizationPs ps = null ;
		try{
			ps = getEntityById(organizationPsId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ps;
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
	protected Class<OrganizationPs> getEntity() {
		return OrganizationPs.class;
	}

	@Override
	public Page<OrganizationPs> getOrganizationPsPage(Page<OrganizationPs> page) throws Exception {
		
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
			List<OrganizationPs> result = organizationManagerDao.getOrganizationPsByOrgidAndName(map);
			int count=organizationManagerDao.getOrganizationPsByOrgidAndNameCount(map);
			if(CollectionUtils.isNotEmpty(result)){
				for(OrganizationPs o:result){
					if(StringUtils.isEmpty(o.getPicPath())){
						o.setPicPath(FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC);
					}else{
						o.setPicPath(FileInstance.FTP_WEB + FileInstance.FTP_URL+ o.getPicPath());
					}
				}
			}
			page.setResult(result);
			page.setTotalCount(count);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public boolean delectPersonsimple(long personId) throws Exception {
		
			boolean  result = false;
		try{
			OrganizationPs ps  = getEntityById(personId);
			
			if(ps == null){
				logger.info("delectPersonsimple organizationPs is null -->"+personId);
				return false;
			}
			
			long orgId = ps.getOrganizaitonId();
			result = deleteEntityById(personId);
			
			documentCatalogueService.delResourceOfCatalog(personId, orgId, ConfigService.ORG_DEF_USER_ID);
			documentTagsService.delAllTagsOfSource(personId, orgId);
			
		}catch(Exception e){
			logger.info("delectPersonsimple is error personId -->" +personId);
			e.printStackTrace();
		}
		
			return result;
	}
}
