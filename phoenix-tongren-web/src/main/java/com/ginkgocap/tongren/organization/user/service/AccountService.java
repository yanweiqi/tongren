/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ginkgocap.tongren.organization.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 
 * @author yanweiqi
 */
@Service
public class AccountService {

	private static Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private Cache cache;

	/**
	 * 按sessionID获得用户.
	 */
	public User getUserByName(String sessionID) {
		String key = RedisKeyUtils.getSessionIdKey(sessionID);
		User user = (User) cache.getByRedis(key);
		if(null != user){
			return user;
		}
		else{
			logger.info("sessionID:"+sessionID+" user isn't exist in redis cache!");
		}
		return user;
	}


}
