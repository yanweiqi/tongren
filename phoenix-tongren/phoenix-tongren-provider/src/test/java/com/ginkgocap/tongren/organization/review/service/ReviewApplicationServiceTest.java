package com.ginkgocap.tongren.organization.review.service;

import static org.junit.Assert.fail;

import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.review.model.ReviewApplication;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;

public class ReviewApplicationServiceTest extends SpringContextTestCase {

	@Autowired
	private ReviewApplicationService reviewApplicationService;
	
	@Test
	public void testCreate() throws Exception {
		logger.info("=========================保存开始============================");
		ReviewApplication r = new ReviewApplication();
		Timestamp t = new Timestamp(System.currentTimeMillis());
		r.setApplyDate(t);
		r.setStartTime(t);
		r.setEndTime(t);
		
		
		r.setReviewProcessId(3904845790838789L);
		r.setReviewGenreId(3904845816004613L);
		r.setOrganizationId(3903677022863450L);
		r.setApplyRereason("dfsfsfsfdsdf");

		ReviewApplication rr = 	reviewApplicationService.create(r);
		logger.info("=========================保存结束============================");
		
		logger.info("==================保存数据清理开始============================");
	//	reviewApplicationService.recallRecords(rr.getId());
		logger.info("=========================保存数据清理开始=====================");
		
	}

	@Test
	public void testGetMyApplyFor() throws Exception {
		Page<ReviewApplicationListVO> page = new Page<ReviewApplicationListVO>();
		page.addParam("orgId", Long.valueOf("3912366140162053"));
		page.addParam("userId", Long.valueOf("13594"));
		page.addParam("type", Integer.parseInt("2"));
		page.setSize(Integer.parseInt("2"));
		//type 0 全部   1:已提申请  2:已完成申请
		page.setIndex(1);
		reviewApplicationService.getMyApplyFor(page);
		if(page.getResult()!=null){
			System.out.println(page.getResult().size());
			for(ReviewApplicationListVO rv:page.getResult()){
				System.out.println(rv.getReviewGenreName()+","+rv.getApplyRereason());
			}
		}else{
			System.out.println(page.getResult());
		}
		
	}

	@Test
	public void testRecallRecords() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserReviewListByOrgId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveRecordsVO() {
	}

}
