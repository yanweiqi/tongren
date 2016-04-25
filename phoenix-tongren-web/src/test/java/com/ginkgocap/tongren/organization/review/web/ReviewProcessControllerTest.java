package com.ginkgocap.tongren.organization.review.web;


import org.junit.Test;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.user.web.UserController;

/**
 * 
 * @author liweichao
 *
 */
public class ReviewProcessControllerTest extends SpringContextTestCase<UserController> {

	private static final String sessionID = "d2ViODI0NTE0NDgwMDQwNzk1MTA=";  
	
	@Test
	public void testGetProcessByOrgId() {
		String  URI = "/reviewProcess/getProcessByOrgId.json";
		requestJson.put("oid", 3910172804382729L);
		requestController(sessionID, URI, requestJson);
	}

}
