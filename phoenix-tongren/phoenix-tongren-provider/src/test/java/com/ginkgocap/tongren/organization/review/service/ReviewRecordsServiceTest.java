package com.ginkgocap.tongren.organization.review.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.organization.review.model.ReviewRecords;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;

public class ReviewRecordsServiceTest extends SpringContextTestCase{

	@Autowired
	public ReviewRecordsService reviewRecordsService;
	
	@Test
	public void testGetReviewListPage() throws Exception {
		Page<ReviewApplicationListVO> page=new Page<ReviewApplicationListVO>();
		page.setIndex(1);
		// 查询条件包含  orgId 组织id，userId 用户id，type 查询类型(0所有的 1 未审核的 2 已经审核)
		page.addParam("orgId", "3905511481409546");
		page.addParam("userId", "13594");
		page.addParam("type", "1");
		
		reviewRecordsService.getReviewListPage(page);
		List<ReviewApplicationListVO>  list=page.getResult();
		if(list!=null){
		for(ReviewApplicationListVO rao:list){
			System.out.println(rao.getApplyRereason());
		}
		}else{
			System.out.println("list is null ---------------------------"+null);
		}
	}
	@Test
	public void testGetMyReviewRecordsList() throws Exception {
		long userId = 13594;
		long organizationId =3905511481409546l;
		
		List<ReviewRecords> list = reviewRecordsService.getMyReviewRecordsList(userId, organizationId);
		System.out.println(list);
		
	}
	 
}
