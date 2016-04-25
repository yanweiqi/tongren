package com.ginkgocap.tongren.warpservice;

/**
 * 第三方service的包装类
 * @author hanxifa
 *
 */
public interface ThirdServiceWarp {
	
	/**
	 * taskid返回资源的url地址
	 * @param taskId
	 * @return
	 */
	public String getUrlByTaskId(String taskId);
}
