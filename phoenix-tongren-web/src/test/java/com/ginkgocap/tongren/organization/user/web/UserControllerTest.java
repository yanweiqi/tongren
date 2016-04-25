package com.ginkgocap.tongren.organization.user.web;


import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.user.model.GinTongUser;

/**
 * 
 * @author yanweiqi
 *
 */
public class UserControllerTest extends SpringContextTestCase<UserController> {

	private static final String sessionID = "d2ViNjc0MTE0NDY0NjUzNDc2MTQ=";  //闫伟旗 sessionId
	
	@Test
	public void testGetUserBySessionId() {
		GinTongUser user = getUser(sessionID);
		logger.info(user.getName());
	}

}
