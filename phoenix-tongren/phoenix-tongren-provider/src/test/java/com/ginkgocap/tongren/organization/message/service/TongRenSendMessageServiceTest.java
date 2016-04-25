/*
 * 文件名： TongRenSendMessageServiceTest.java
 * 创建日期： 2015年11月11日
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
 * @since 2015年11月11日
 */
public class TongRenSendMessageServiceTest extends SpringContextTestCase {

	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;
	
	public void sendDissolutionORKickTest(){
		boolean isOk = false;
		try {
			isOk = tongRenSendMessageService.sendDissolutionORKick(13583, 3908032258113541L);
			System.out.println(isOk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void sendOrganizationInvitationTest(){
		long sendUID = 36;
		long receiveUID = 13594;
		long organizationId = 3905486709850117L;
		boolean isOk = false;
		try {
			isOk = tongRenSendMessageService.sendOrganizationInvitation(sendUID, receiveUID, organizationId);
			System.out.println(isOk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void sendExtensioProjectMesTest(){
		long sendUID = 36;
		long organiztionId = 0;
		long projectId = 3908370281267205L;
		int cycle = 30;
		String content = "延期项目测试内容";
		try {
			boolean isOk = tongRenSendMessageService.sendExtensioProjectMes(sendUID, organiztionId, projectId, cycle, content);
			System.out.println(isOk); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void sendSubDocumentMesTest(){
		long sendUID = 13583;
		long organizationId = 3923239613235205L;
		long projectId = 0;
		String fileName = "测试斯蒂芬.doc";
		try {
			boolean isOK = tongRenSendMessageService.sendSubDocumentMes(sendUID, organizationId, fileName, projectId);
			System.out.println(isOK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void sendTaskRemind()throws Exception{
		
		boolean falg = tongRenSendMessageService.sendTaskRemind(13594L, 13583, 3915306552786959L, 3918949494554629L, "测试发送提醒111");
		System.out.println(falg);
		
	}
	
}
