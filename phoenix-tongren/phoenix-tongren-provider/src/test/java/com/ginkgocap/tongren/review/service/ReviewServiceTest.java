package com.ginkgocap.tongren.review.service;

import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.service.ReviewApplicationService;
import com.ginkgocap.tongren.organization.review.service.ReviewGenreService;
import com.ginkgocap.tongren.organization.review.service.ReviewObjectService;
import com.ginkgocap.tongren.organization.review.service.ReviewProcessService;
import com.ginkgocap.tongren.organization.review.service.ReviewRecordsService;

public class ReviewServiceTest extends SpringContextTestCase{
	
	@Autowired
	private ReviewProcessService reviewProcessService;
	@Autowired
	private ReviewGenreService reviewGenreService;
	@Autowired 
	private ReviewObjectService reviewObjectService;
	@Autowired
	private ReviewRecordsService reviewRecordsService;
	@Autowired
	private ReviewApplicationService reviewApplicationService;
	
	@Test
	public  void testSave()throws Exception{
	}
	@Test
	public void testName(){
		
		try {
			boolean status = reviewProcessService.checkProcessName(123123123, "假");
			System.out.println("******"+status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testCreateNo(){
		try {
			List<ReviewGenre>	 map = reviewGenreService.getGenreByProId(3900020621574149L);
			System.out.println(map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSignOff() throws Exception{
		long appId = 3923892959969300l;
		long userId = 13583;
		int type = 2;
		boolean  flag =  reviewRecordsService.signOff(appId, userId, type);
		System.out.println(flag);
	}
	@Test
	public void testDel()throws Exception{
		
		boolean flag = reviewProcessService.delect(3905768869068805L);
		System.out.println(flag);
	}
	
	@Test
	public void testRecallRecords()throws Exception{
		
		long appli = 3913447779860485L; 
		boolean flag = reviewApplicationService.recallRecords(appli);
		System.out.println(flag);
	}
}
