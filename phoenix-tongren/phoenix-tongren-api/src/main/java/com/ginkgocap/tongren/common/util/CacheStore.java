package com.ginkgocap.tongren.common.util;

import java.io.Serializable;

public class CacheStore<T> implements Serializable{

	private static final long serialVersionUID = 6591061597772013536L;

    private String key;			//缓存ID 
    private T t;		        //缓存数据 
    private long   timeOut = 0L;//过期时间 
    private boolean expired; 	//是否终止 
    
    public CacheStore() { 
         super(); 
    }

	public CacheStore(String key, T t, long timeOut, boolean expired) {
		super();
		this.key = key;
		this.t = t;
		this.timeOut = timeOut;
		this.expired = expired;
	}



	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
    
	
    
}
