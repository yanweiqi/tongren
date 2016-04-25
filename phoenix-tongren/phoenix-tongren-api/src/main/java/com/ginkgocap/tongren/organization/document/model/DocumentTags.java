package com.ginkgocap.tongren.organization.document.model;

import java.io.Serializable;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 标签的实体bean
 * @author hanxifa
 *
 */
public class DocumentTags extends BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long userId;
	
	private String name;//标签名称

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	


}
