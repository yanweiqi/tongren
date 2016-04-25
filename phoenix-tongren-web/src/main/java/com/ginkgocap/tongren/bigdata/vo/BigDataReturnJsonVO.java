/*
 * 文件名： BigDataReturnJsonVO.java
 * 创建日期： 2015年10月14日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.bigdata.vo;


 /**
 *  大数据放回json封装对象
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月14日
 */
public class BigDataReturnJsonVO {

	private long id;//用户ID
	private String name;//用户名称
	private String path;//用户头像路径
	/**
	 * @return 返回 id。
	 */
	public long getId() {
		return id;
	}
	/**
	 * ---@param id 要设置的 id。
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}
	/**
	 * ---@param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return 返回 path。
	 */
	public String getPath() {
		return path;
	}
	/**
	 * ---@param path 要设置的 path。
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
