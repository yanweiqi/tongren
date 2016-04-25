package com.ginkgocap.tongren.organization.manage.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.organization.manage.model.OrganizationType;
import com.ginkgocap.tongren.organization.manage.service.OrganizationTypeService;

@Service("organizationTypeService")
public class OrganizationTypeServiceImpl implements OrganizationTypeService{
	
	private final Logger logger = LoggerFactory.getLogger(OrganizationTypeServiceImpl.class);
	
	
	private static  List<OrganizationType> orgTypeList = Collections.synchronizedList(new ArrayList<OrganizationType>(10));
	
	private static  List<OrganizationType> orgTypeList2 = Collections.synchronizedList(new ArrayList<OrganizationType>(10));
	
	public OrganizationTypeServiceImpl(){
		initOrgTypeList();
	}
	private void initOrgTypeList(){
		orgTypeList.add(new OrganizationType(10, "金融机构",0));
		orgTypeList.add(new OrganizationType(2, "一般企业",0));
		orgTypeList.add(new OrganizationType(3, "政府组织",0));
		orgTypeList.add(new OrganizationType(4, "中介机构",0));
		orgTypeList.add(new OrganizationType(5, "专业传媒",0));
		
		orgTypeList2.add(new OrganizationType(6, "期刊报纸",5));
		orgTypeList2.add(new OrganizationType(7, "研究机构",5));
		orgTypeList2.add(new OrganizationType(8, "电视广播",5));
		orgTypeList2.add(new OrganizationType(9, "互联网媒体",5));
		 
	}
	
	@Override
	public List<OrganizationType> getOrganizationData(int id) {
		if(id==1){
			return orgTypeList;
		}
		if(id==5){
			return orgTypeList2;
		}
		return new ArrayList<OrganizationType>();
	}

	@Override
	public String getOrganizationTypeName(String id) {
		String name = null;
		for(OrganizationType ot :orgTypeList){
			if(String.valueOf(ot.getId()).equals(id) ){
				name = ot.getName();
				break;
			}
		}
		if(name==null){
			for(OrganizationType ot :orgTypeList2){
				if(String.valueOf(ot.getId()).equals(id) ){
					name = ot.getName();
					break;
				}
			}
		}
		if(name==null){
			logger.error("getOrganizationTypeName is error  id---->"+id);
		}
		return name;
	}

}
