package com.ginkgocap.tongren.common.service.impl;

import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.framework.dal.dao.config.helper.GroupHelper;

@Component
public class ParaConfigOnContext implements ApplicationContextAware {

	protected static final Logger logger = LoggerFactory.getLogger(ParaConfigOnContext.class);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		String dconfig=System.getenv("datasource_dev_config");
		if(dconfig!=null&&dconfig.equals("true")){
			logger.info("datasource_dev_config is true");
			Map<Object, Object> map=GroupHelper.getDataSourceMap();
			Set<Object> keys=map.keySet();
			for(Object key:keys){
				logger.info("set auto reconnect datasource "+key);
				BasicDataSource ds=(BasicDataSource) map.get(key);
	            //上线去掉
	            ds.setTestOnBorrow(true);
	            ds.setTestOnReturn(true);
	            ds.setTestWhileIdle(true);
	            ds.setInitialSize(20);
	            ds.setValidationQuery("select 1");
	            logger.info("success set datasource auto reconnect"+key);
				
			}
		}else{
			logger.info("datasource_dev_config is false");
		}
	}

}
