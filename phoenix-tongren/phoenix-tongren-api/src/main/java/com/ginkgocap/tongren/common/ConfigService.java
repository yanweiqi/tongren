package com.ginkgocap.tongren.common;

/**
 * 文件配置service
 * @author hanxifa
 *
 */
public interface ConfigService {

	public String getStrVal(String key,String def);
	
	public String getStrVal(String key);
	
	public Integer getIntVal(String key,Integer def);
	
	public Integer getIntVal(String key);
	
	public Long getLongVal(String key,Long def);
	
	public Long getLongVal(String key);
	
	public final static long ORG_DEF_USER_ID=5566778899l;//组织中的默认用户id，用户组织资源下的目录和标签管理，
}
