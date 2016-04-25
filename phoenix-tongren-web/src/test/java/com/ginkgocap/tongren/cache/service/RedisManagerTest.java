package com.ginkgocap.tongren.cache.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.cache.mock.UserMock;
import com.ginkgocap.ywxt.user.model.User;

public class RedisManagerTest extends SpringContextTestCase {


	@Autowired
	private RedisManager redisManager;
	
	private String key = "wwwww";
	
	@Before
	public void  testBefor() {
		/*
		redisManager  = new RedisManager();
		redisManager.setHost("192.168.101.131");
		redisManager.setPort(6379);
		redisManager.setExpire(1000*60);
		redisManager.setTimeout(0);
		*/
		//redisManager.init();
	}
	
	@Test
	public void testSet(){		
		
		UserMock u = new UserMock();
		u.setId("123");
		u.setLocked(true);
		u.setPassword("111");
		u.setSalt("222");
		u.setUsername("jack");
		
		redisManager.set(key, u);
		
		Object uu = redisManager.get(key);
		//UserMock um = (UserMock) SerializeUtils.deserialize(uu);
		System.out.println(uu.toString());
	}
	
	@Test
	public void testGet(){
		//byte[] u = redisManager.get("sessionId:user:d2ViNzYyOTE0NDQzODE5MTY2MTE=".getBytes());
		User u = (User) redisManager.get("sessionId:user:d2ViMzI1MTE0NDU4NTI3OTEyOTU=");
		//UserMock um = (UserMock) SerializeUtils.deserialize(u);
		System.out.println(u.toString());
		//User user = (User) SerializeUtils.deserialize(u);
		//System.out.println(user.toString());
	}

	
}
