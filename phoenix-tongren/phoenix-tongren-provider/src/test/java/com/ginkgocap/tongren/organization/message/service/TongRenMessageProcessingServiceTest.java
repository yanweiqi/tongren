/*
 * 文件名： TongRenMessageProcessingServiceTest.java
 * 创建日期： 2015年11月16日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.message.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月16日
 */
public class TongRenMessageProcessingServiceTest extends SpringContextTestCase{
	
	@Autowired
	private TongRenMessageProcessingService tongRenMessageProcessingService;
	
	@Test
	public void messageOrganizationInvitationTest(){
		try {
			boolean isOk = tongRenMessageProcessingService.messageOrganizationInvitation(1, 3909857044594690L);
			System.out.println(isOk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testMessageExtensioProject(){
		try {
			boolean isOk = tongRenMessageProcessingService.messageExtensioProject(1, 3923520027623459L);
			System.out.println(isOk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testMessageInvitationProjectProcessing(){
		try {
			String isOk = tongRenMessageProcessingService.messageInvitationProjectProcessing(1, 3932587685052551L);
			System.out.println(isOk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
