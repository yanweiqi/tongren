package com.ginkgocap.tongren.common.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author yanweiqi
 *
 */
public class CacheStoreManage {
	
	private static final Logger logger = LoggerFactory.getLogger(CacheStoreManage.class);

	private static ConcurrentHashMap<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();
	
	private static CacheStoreManage csm = null;
	
	private CacheStoreManage(){
		super();
	}
	
	public static CacheStoreManage getInstance(){
		if(csm == null){
			csm = new CacheStoreManage();
		}
		return csm;
	}
	
	public synchronized static void clearAll(){
		cacheMap.clear();
	}
	
	/**
	 * 清除某一类型的缓存
	 * @param key
	 */
	public synchronized static void cleanByKey(String key){
		for ( Map.Entry<String, Object> entry: cacheMap.entrySet()) {
			if(entry.getKey().equals(key)){
				if(cacheMap.containsKey(key)){
					logger.info("remove key:"+key);
					cacheMap.remove(key);
				}
				else{
					logger.info("remove key:"+key+"is not exist");
				}
			}
		}
	}
	
	/**
	 * 装载缓存
	 * @param key
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T> CacheStore<T> put(String key,T t){
		return (CacheStore<T>) cacheMap.put(key, t);
	}
	
	/**
	 * 根据Key获取缓存对象
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T> CacheStore<T> getByKey(String key){
		if (hasCache(key)) {
		   return (CacheStore<T>) cacheMap.get(key);
		}
		else{
			logger.info("key:"+key+",status:not exist");
		}
		return null;
	}
	
	/**
	 * 根据key判断缓存对象是否存在
	 * @return
	 */
	public synchronized static boolean hasCache(String key){
		return cacheMap.containsKey(key);
	}
	
	/**
	 * 获取缓存中的所有key
	 * @return
	 */
	public static Set<String> getAllKey(){
		Set<String> keys = cacheMap.keySet();
		return keys;
	}
}
