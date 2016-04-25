package com.ginkgocap.tongren.bigdata.vo;

import java.io.Serializable;

/**
 * 用户VO
 * @author Administrator
 *
 */
public class UserVO implements Serializable {
	
	private long id;
	
	private String name;
	
	private String path; //头像路径

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	

}
