package com.ginkgocap.tongren.common.model;

/**
 * 本地缓存
 * @author hanxifa
 *
 * @param <T>
 */
public class LocalCacheBeanWrap<T>{

	private long cl;//生成时间
	
	private int validity;//有效期 单位毫秒
	
	private T bean;
	public LocalCacheBeanWrap(T bean,int val){
		cl=System.currentTimeMillis();
		this.bean=bean;
		this.validity=val;
	}
	
	public boolean isValid(){
		return System.currentTimeMillis()-cl<validity;
	}
	
	public T getBean(){
		return bean;
	}
	
}
