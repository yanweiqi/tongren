package com.ginkgocap.tongren;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

import com.ginkgocap.tongren.organization.application.model.Module;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.ywxt.framework.dal.dao.Dao;
import com.ginkgocap.ywxt.framework.dal.dao.exception.DaoException;
import com.ginkgocap.ywxt.framework.dal.dao.impl.CompositeDaoImpl;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    
    
	@Test
    public void testSaveModule() throws DaoException {
    		Dao dao = new CompositeDaoImpl();
    		List<Long> ids_list = new ArrayList<Long>();
    		//create object , get object
    		for (int i = 0; i < 100; i++) {
    			Module module = new Module();
    			module.setCreaterId(1l);
    			module.setCreateTime(new Timestamp(System.currentTimeMillis()));
    			Serializable id = dao.save(module);
    			System.out.println(dao.get(Module.class, id));
    		}
    		
    		List<Long> idsList = dao.getIdList("Module_List_createId_id", 1l );
			List<Module> modules = dao.getList(Module.class, idsList);
			for (Module module : modules) {
				System.out.println(module);
			}
			Module module = (Module) dao.get(Module.class, 3892347872215225l);
			System.out.println(module);	
    }
	
	@Test
    public void testSaveOrganization() throws DaoException {
    		Dao dao = new CompositeDaoImpl();

    		for (int i = 0; i < 100; i++) {
    			Organization module = new Organization();
    			module.setCreaterId(1L);
    			module.setCreateTime(new Timestamp(System.currentTimeMillis()));
    			Serializable id = dao.save(module);
    			System.out.println(dao.get(Organization.class, id));
    		}

    }
    
}
