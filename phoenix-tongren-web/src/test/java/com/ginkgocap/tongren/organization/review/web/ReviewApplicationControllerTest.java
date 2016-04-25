package com.ginkgocap.tongren.organization.review.web;


import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.user.web.UserController;

/**
 * 
 * @author liweichao
 *
 */
public class ReviewApplicationControllerTest extends SpringContextTestCase<UserController> {

	private static final String sessionID = "d2ViMTgyNzE0NDc2Mzk2MDM2NDg=";  
	
	@Test
	public void testCreateApplication() {
		String  URI = "/reviewApplication/createApplication.json";
		ReviewApplication  reviewApplication = new ReviewApplication();
		reviewApplication.setOrganizationId(3908705372602414L);
		reviewApplication.setReviewProcessId(3909221448155166L);
		reviewApplication.setReviewGenreId(3909221448155141L);
		reviewApplication.setApplyRereason("D大调");
		reviewApplication.setStartTime("2015-11-25 00:00:00");
		reviewApplication.setEndTime("2015-12-01 00:00:00");
		JSONObject json = JSONObject.fromObject(reviewApplication);
		requestJson.put("reviewApplication", json.toString());
		requestController(sessionID, URI, requestJson);
	}

}
