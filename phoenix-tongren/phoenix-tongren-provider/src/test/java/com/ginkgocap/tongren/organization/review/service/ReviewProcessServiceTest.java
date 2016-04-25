package com.ginkgocap.tongren.organization.review.service;

import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.model.ReviewProcess;

public class ReviewProcessServiceTest extends SpringContextTestCase {
	
	@Autowired
	private ReviewProcessService reviewProcessService;
	
	@Test
	public void testCreate() throws Exception {
		long organizationId = 3903677022863450L;
		String reviewName = "test reviewName weichao";
		String reviewNo = reviewProcessService.getNomber();
		String description =  "test reviewName weichao......";
		
		ReviewProcess rp = new ReviewProcess();
		Timestamp t = new Timestamp(System.currentTimeMillis());
		rp.setCreateTime(t);
		rp.setUpdateTime(t);
		rp.setOrganizationId(organizationId);
		rp.setReviewName(reviewName);
		rp.setReviewNo(reviewNo);
		rp.setDescription(description);
		
		List<ReviewGenre> reviewGenres = new ArrayList<ReviewGenre>();
		for (int i=0; i<3; i++) {
			ReviewGenre rg = new ReviewGenre();
			rg.setName(i+"test");
			reviewGenres.add(rg);
		}
		
		List<ReviewObject> reviewObjects = new ArrayList<ReviewObject>();
		for (int i=0; i<3; i++) {
			ReviewObject ro = new ReviewObject();
			ro.setReviewLevel(i);
			ro.setReviewUserId(1111+i);
			ro.setRoleId(2222+i);
			reviewObjects.add(ro);
		}
		logger.info("================保存数据开始==================");
		long id = reviewProcessService.create(rp, reviewGenres, reviewObjects);
		logger.info("================保存数据结束 id="+id+"==================");
	}

	@Test
	public void testGetReviewByOrgId() throws Exception{
		reviewProcessService.checkProcessName(3903677022863450L, "test reviewName weichao");
	}

	@Test
	public void testGetReviewById() {
		fail("Not yet implemented");
	}



	@Test
	public void testUpdateReviewProcess() {
		fail("Not yet implemented");
	}

}
