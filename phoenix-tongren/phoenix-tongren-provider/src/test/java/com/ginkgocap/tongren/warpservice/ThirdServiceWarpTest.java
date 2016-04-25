package com.ginkgocap.tongren.warpservice;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;

public class ThirdServiceWarpTest extends SpringContextTestCase{
	
	private final Logger logger  = LoggerFactory.getLogger(getClass());
	@Autowired
	private ThirdServiceWarp thirdServiceWarp;
	
	@Test
	public void testGetUrlByTaskId(){
		String url=thirdServiceWarp.getUrlByTaskId("TVRRME56WTJNRE15TnpNMk56QXdNREk0T0RFM09USTI=");
		logger.info("url is "+url);
		url=thirdServiceWarp.getUrlByTaskId("TVRRME56WTJNRE15TnpNMk56QXdNREk0T0RFasdfasf=");
		logger.info("url2 is "+url);
		
	}

}
