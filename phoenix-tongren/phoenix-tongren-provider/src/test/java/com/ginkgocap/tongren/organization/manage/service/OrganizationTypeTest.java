package com.ginkgocap.tongren.organization.manage.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.manage.model.OrganizationType;

public class OrganizationTypeTest extends SpringContextTestCase{
	
	@Autowired
	private OrganizationTypeService oprganizationTypeService;
	
	@Test
	public void testGetData(){
		int id =1;
		List<OrganizationType>  list = 	oprganizationTypeService.getOrganizationData(1);
		System.out.println("***"+list);
	}
	@Test
	public void testGetName(){
		for(int i=1;i<9;i++){
			String name  = oprganizationTypeService.getOrganizationTypeName(String.valueOf(i));
			System.out.println("|||"+name);
		}
	}

}
