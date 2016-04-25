package com.ginkgocap.tongren.project.manage.service;

import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
/**
 * 
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年12月24日
 */
public class DeliveryServiceTest extends SpringContextTestCase{

	@Autowired
	private DeliveryService deliverService;
	
	@Test
	public void create(){
		
		try {
			deliverService.create(111, 1111, 111, null, 1, 11, 11);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
