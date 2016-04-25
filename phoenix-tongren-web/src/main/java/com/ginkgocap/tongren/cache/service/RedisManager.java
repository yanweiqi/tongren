package com.ginkgocap.tongren.cache.service;

import java.util.Collection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import com.ginkgocap.ywxt.cache.Cache;

public class RedisManager {
			
	private Cache cache;
	
	private long expiredTime;
	
	private String sessionID;
	
	/**
	 * get value from redis
	 * @param key
	 * @return
	 */
	public Object get(String key){
	   return cache.getByRedis(key);
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object set(String key,Object value){
		cache.set(key, value);
		return cache.getByRedis(key);
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public Object set(String key,Object value,Long expiredTime){
		cache.setByRedis(key, value, expiredTime.intValue());
		return cache.getByRedis(key);
	}
	
	/**
	 * del
	 * @param key
	 */
	public void del(String key){
		cache.remove(key);
	}
	
	/**
	 * flush
	 */
	public void flushDB(){
		ShardedJedis shardedJedis = cache.getJedis();
		Collection<Jedis> allShards=shardedJedis.getAllShards();
		for (Jedis j : allShards) {
			j.flushAll();
		}
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
    
	
	
}
