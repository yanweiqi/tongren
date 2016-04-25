/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.organization.message.model.MessageCreate;
import com.ginkgocap.tongren.organization.message.model.MessageSend;

/**
 * @author liny
 *
 */
public class MessageServiceTest extends SpringContextTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(MessageServiceTest.class);
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageCreateService messageCreateService;
	
	@Test
	public void deleteMessageByProjectIdTest(){
		long projectId = 3932599429103666L;
		try {
			messageService.deleteMessageByProjectId(projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testDel(){
		long projectId = 3932635068104709L;
		
		List<Long> idList = messageCreateService.getKeysByParams("MessageCreate_List_projectId", projectId);
		for(Long l :idList){
			System.out.println("id="+l);
		}
	}
	public void sendMessageTest(){
		String title = "测试消息1";
		String content = "测试消息内容1";
		long sendUserId = 10055340;
		String sendOrganizationId = "3899738856620077";
		String sendDepartmentId = "1222";
		long receiveUserId = 10055340;
		String receiveOrganizationId = "3899738856620077";
		String receiveDepartmentId = "1222";
		int messageType = 0;
		try {
			messageService.sendMessage(title, content, sendUserId, Long.parseLong(sendOrganizationId), 
					Long.parseLong(sendDepartmentId), receiveUserId, Long.parseLong(receiveOrganizationId), Long.parseLong(receiveDepartmentId), messageType,0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		
	}
	public void getMessageListTest(){
		
		List<MessageCreate> mList = messageService.getMessageList(1000);
		for (MessageCreate messageCreate : mList) {
			System.out.println(messageCreate.getTitle());
			System.out.println(messageCreate.getContent());
		}
	}
	
	public void getList(){
//		messageService.operateApplicationMessage(3, 1L, 3897516164907013L);
	}
	@Test
	public void delMes(){
		long userId = 12767;
		long organizationId = 3916643277471749L;
		messageService.deleteMessage(userId, organizationId);
	}
	
	public void searchListTest(){
		List<MessageSend> list = messageService.searchMessageByKeywordAndUserID(13583, "ceshi", 3899738856620077L);
		System.out.println(list.size());
	}
	@Test
	public void getMessageByUidAndOrgidTest(){
		List<MessageSend> list = messageService.getMessageListByOrgUid(36, 0);
		System.out.println(list.size());
	}
	
	public void getMessageByOrgUidStatusTest(){
		try {
			List<MessageSend> list = messageService.getMessageByOrgUidStatus(13594, 3908700700147733L, 8);
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getMessageCreateListIdTest(){
		List<Long> longs;
		try {
			longs = messageService.getMessageCreateListId(3915660568821805L, 13594, MessageType.APPLICATION_EXTENSION_PROJECT);
			System.out.println(longs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
