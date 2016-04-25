/*
 * 文件名： ResourcePathExposer.java
 * 创建日期： 2015年10月13日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.common.utils;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;


 /**
 *  通过Spring框架在ServletContext层面注入静态资源根路径信息
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月13日
 */
public class ResourcePathExposer implements ServletContextAware{
	
	private ServletContext application;

	private String bigdataQueryHost;// 大数据访问接口

	/*
	 * 初始化方法
	 */
	public void init() {
		
		getServletContext().setAttribute("bigdataQueryHost", bigdataQueryHost);
		getServletContext().setAttribute("contextPath",
				getServletContext().getContextPath());
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		application = servletContext;

	}

	public ServletContext getServletContext() {
		return this.application;
	}

	

	public void setBigdataQueryHost(String bigdataQueryHost) {
		this.bigdataQueryHost = bigdataQueryHost;
	}


	public String getBigdataQueryHost() {
		return bigdataQueryHost;
	}

	
	
}
