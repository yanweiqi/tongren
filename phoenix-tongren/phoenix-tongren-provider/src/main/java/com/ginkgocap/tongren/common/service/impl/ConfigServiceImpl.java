package com.ginkgocap.tongren.common.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.ConfigService;
/**
 * 配置service
 * java代码直接引用的配置，统一在此service获取
 * @author hanxifa
 *
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService{
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
	
	Map<String, String> config=new ConcurrentHashMap<String, String>();
	
	public ConfigServiceImpl(){
		initConfig();
	}
	
	@Override
	public String getStrVal(String key, String def) {
		String val=config.get(key);
		if(val==null){
			return def;
		}
		return val;
	}

	@Override
	public String getStrVal(String key) {
		// TODO Auto-generated method stub
		return getStrVal(key,null);
	}

	@Override
	public Integer getIntVal(String key, Integer def) {
		String val=getStrVal(key,null);
		if(val!=null){
			return Integer.parseInt(val);
		}
		return def;
	}

	@Override
	public Integer getIntVal(String key) {
		return getIntVal(key ,null);
	}

	@Override
	public Long getLongVal(String key, Long def) {
		String val=getStrVal(key,null);
		if(val!=null){
			return Long.parseLong(val);
		}
		return def;
	}

	@Override
	public Long getLongVal(String key) {
		return getLongVal(key,null);
	}
	
	@SuppressWarnings("unchecked")
	private void initConfig(){
		 SAXReader reader = new SAXReader();
		 try {
			Document   document =reader.read(getClass().getResource("/config.xml"));
			List<Element> nodes=document.getRootElement().elements();
			System.out.println(document.getRootElement().getName());
			for(Element el:nodes){
				String eName=el.getName();
				List<Element> items= el.elements("item");
				for(Element item:items){
					String itemKey=item.attributeValue("key");
					String itemVal=item.attributeValue("value");
					config.put(eName+"."+itemKey, itemVal);
					logger.info("add config: "+eName+"."+itemKey+","+itemVal);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("parse config.xml failed! ",e);
		}
	}
	
	public static void main(String[] args) {
		ConfigServiceImpl cf=new ConfigServiceImpl();
		cf.initConfig();
		System.out.println(cf.config);
	}

}
