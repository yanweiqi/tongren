package com.ginkgocap.tongren.cache.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ginkgocap.tongren.cache.service.RedisManager;
import com.ginkgocap.tongren.cache.util.SerializeUtils;

public class RedisSessionDAO extends AbstractSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

	private RedisManager redisManager;
	
	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
	}
	
	/**
	 * save session
	 * @param session
	 * @throws UnknownSessionException
	 */
	private void saveSession(Session session) throws UnknownSessionException{
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		byte[] key = getByteKey(session.getId());
		
		Object value = SerializeUtils.serialize(session);
		session.setTimeout(redisManager.getExpiredTime()*1000);		
		redisManager.set(new String(key), value,  redisManager.getExpiredTime());
	}

	@Override
	public void delete(Session session) {
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		redisManager.del(new String(getByteKey(session.getId())));
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		/*
		Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");
		if(keys != null && keys.size()>0){
			for(byte[] key:keys){
				Session s = (Session)SerializeUtils.deserialize(redisManager.get(key));
				sessions.add(s);
			}
		}*/
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		String sessionID = redisManager.getSessionID();
		if(sessionId == null){
			logger.error("session id is null");
			return null;
		}
		else{
			sessionId = SerializeUtils.serialize(sessionID);
		}
		
		System.out.println(sessionID);	
		//Object o = redisManager.get(sessionID);
		
	    //Session s = sessionId.equals(obj)? null: (Session) SerializeUtils.deserialize(getByteKey(sessionId));
		return null;
	}

	/**
	 * 获得byte[]型的key
	 * @param key
	 * @return
	 */
	private byte[] getByteKey(Serializable sessionId){
		String key = sessionId+"";
		return key.getBytes();
	}
	
	
    public Serializable getSessionId(String sessionId) {
        return sessionId;
    }
	
	public RedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(RedisManager redisManager) {
		this.redisManager = redisManager;
	}	
	
}
