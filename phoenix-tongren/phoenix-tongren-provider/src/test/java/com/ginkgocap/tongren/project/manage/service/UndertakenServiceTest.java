/*
 * 文件名： UndertakenServiceTest.java
 * 创建日期： 2015年11月23日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.task.exception.UndertakenException;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月23日
 */
public class UndertakenServiceTest extends SpringContextTestCase{

	@Autowired
	private UndertakenService undertakenService;
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;
	
	@Test
	public void getUndertakenListTest(){
		long recipientId = 13594;
		int status = -1;
		try {
			List<Undertaken> list = undertakenService.getUndertakenList(recipientId, status);
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void undertakenProjectTest(){
		long projectId = 3932602637746231L;
		long recipientId = 13583L;
		long recipientOrganizationId = 3915315348242467L;
		try {
			Undertaken undertaken = undertakenService.undertakenProject(projectId, recipientId, recipientOrganizationId);
			System.out.println(undertaken.getId());
		} catch (UndertakenException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSend()throws Exception{
		String uids = "[13583,13626,13366]";
		String organizationIds = "[3915315348242467,3915563583930443,0]";
		List<Long> orgs = JSON.parseArray(organizationIds, Long.class);
		List<Long> userIds = JSON.parseArray(uids, Long.class);
		long projectId = 3932640424230927L;
		
		tongRenSendMessageService.sendInvitationProjectMes(13594, userIds, orgs, projectId, "式");
	}
	
	@Test
	public void getUndertakenByOrganizationIdTest(){
		boolean isOk = false;
		long organizationId = 3923597358006287L;
		try {
			isOk = undertakenService.getUndertakenByOrganizationId(organizationId);
			System.out.println(isOk);
		} catch (UndertakenException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getUndertakenByOrganizationIdByStatusTest(){
		long organizationId = 0;
		int status = 0;
		try {
			List<Undertaken> list = undertakenService.getUndertakenByOrganizationIdByStatus(organizationId, status);
			System.out.println(list.size());
		} catch (UndertakenException e) {
			e.printStackTrace();
		}
	}
}
