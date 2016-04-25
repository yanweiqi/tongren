package com.ginkgocap.tongren.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

public  abstract class BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//扩展属性，用于web端展示
	protected Map<String, Object> extend=new HashMap<String, Object>();

	@Transient
	public Map<String, Object> getExtend() {
		return extend;
	}

	@Transient
	public void addExtend(String key,Object val){
		extend.put(key, val);
	}
	 

}
