package com.ginkgocap.tongren.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class LoadPropertiesUtil {
	
	
	public static String getPropertiesValue(String propertiesName,String key){
		
	    InputStream inStream = LoadPropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
	    Properties prop = new Properties();
	    String value = "";
        try {
            prop.load(inStream);
            value = prop.getProperty(key);
            if(StringUtils.isNoneBlank(value)){
            	inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
	}

}
