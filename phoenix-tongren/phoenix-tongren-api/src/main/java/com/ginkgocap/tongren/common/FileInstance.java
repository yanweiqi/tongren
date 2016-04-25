package com.ginkgocap.tongren.common;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/** 
 * 读取配置文件
 * @author wlf
 */
@Component
public class FileInstance implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(FileInstance.class);
    private static  String CONFIG_PROPERTIES = "image_test.properties";
    static{
    	String imageConfig=System.getenv("TONGREN_IMAGE_CONFIG");
    	if(imageConfig!=null&&imageConfig.length()>0){
    		CONFIG_PROPERTIES=imageConfig;
    	}
    	logger.info("image config path is "+CONFIG_PROPERTIES);
    }
    public static String REMOVE_FILE_ROOT_PATH;
    public static String FILE_ENCODING;
    public static String FTP_USER_NAME;// FTP 用户名(测试环境)
    public static String FTP_PASSWORD;// FTP 密码(测试环境)
    public static String FTP_IP;//  FTP 地址
    public static String FTP_URL;// FTP_URL 地址url
   // public static String PROTOCOL = getValueByIndex("PROTOCOL", 6);// 采用协议
    public static String FTP_PORT;// FTP 端口
    public static String ENCODING;//全文编码格式
    public static String DOWNLOAD_PATH;//文件下载本地地址
    public static String BIG_IMG_WEIGHT;//大图片宽度
    public static String BIG_IMG_HEIGHT;//大图片高度
    public static String MID_IMG_WEIGHT;//中图片宽度
    public static String MID_IMG_HEIGHT;//中图片高度
    public static String SM_IMG_WEIGHT;//小图片宽度
    public static String SM_IMG_HEIGHT;//小图片高度
    public static String MM_IMG_WEIGHT;//极小图片宽度
    public static String MM_IMG_HEIGHT;//极小图片高度
    public static String IMG_MAX_SIZE;//图片上传大小最大值2M
    public static String IMG_MAX_HEIGHT;//图片上传大小最大高度
    public static String IMG_MAX_WIDTH;//图片上传大小最大宽度
    public static String FTP_WEB;//web url头
    public static String FTP_FULL_URL;//文件上传后的ULR头
    
    private static  Map<String, String> localCache=new ConcurrentHashMap<String, String>();
    public static String getValue(String key, String value) {
    	String val=localCache.get(key);
    	if(val==null){
    		val=configService.getStrVal("fileconfig."+key, value);
    	}
    	localCache.put(key, val);
    	return val;
    }
    private static String getValue(String key) {
    	return getValue(key,null);
    }
    private static ConfigService configService=null;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logger.info("has init ConfigService ");
		configService=(ConfigService) applicationContext.getBean("configService");
		initVal();
		
	}
	
	private void initVal() {
		REMOVE_FILE_ROOT_PATH = getValue("REMOVE_FILE_ROOT_PATH", "");// 文件上传根路径
		FILE_ENCODING = getValue("FILE_ENCODING","GBK");// 文件編碼格式
		FTP_USER_NAME = getValue("FTP_USER_NAME","admin");// FTP 用户名(测试环境)
		FTP_PASSWORD = getValue("FTP_PASSWORD");// FTP 密码(测试环境)
		FTP_IP = getValue("FTP_IP");// FTP 地址
		FTP_URL = getValue("FTP_URL");// FTP_URL 地址url
		// PROTOCOL = getValue("PROTOCOL", 6);// 采用协议
		FTP_PORT = getValue("FTP_PORT");// FTP 端口
		ENCODING = getValue("ENCODING");// 全文编码格式
		DOWNLOAD_PATH = getValue("DOWNLOAD_PATH");// 文件下载本地地址
		BIG_IMG_WEIGHT = getValue("BIG_IMG_WEIGHT");// 大图片宽度
		BIG_IMG_HEIGHT = getValue("BIG_IMG_HEIGHT");// 大图片高度
		MID_IMG_WEIGHT = getValue("MID_IMG_WEIGHT");// 中图片宽度
		MID_IMG_HEIGHT = getValue("MID_IMG_HEIGHT");// 中图片高度
		SM_IMG_WEIGHT = getValue("SM_IMG_WEIGHT");// 小图片宽度
		SM_IMG_HEIGHT = getValue("SM_IMG_HEIGHT");// 小图片高度
		MM_IMG_WEIGHT = getValue("MM_IMG_WEIGHT");// 极小图片宽度
		MM_IMG_HEIGHT = getValue("MM_IMG_HEIGHT");// 极小图片高度
		IMG_MAX_SIZE = getValue("IMG_MAX_SIZE");// 图片上传大小最大值2M
		IMG_MAX_HEIGHT = getValue("IMG_MAX_HEIGHT");// 图片上传大小最大高度
		IMG_MAX_WIDTH = getValue("IMG_MAX_WIDTH");// 图片上传大小最大宽度
		FTP_WEB = getValue("FTP_WEB");// web url头
		FTP_FULL_URL = getValue("FTP_FULL_URL");// 文件上传后的ULR头
	}
}
