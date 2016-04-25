/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.util.DaoSortType;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.message.model.MessageCreate;
import com.ginkgocap.tongren.organization.message.model.MessageReceive;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageCreateService;
import com.ginkgocap.tongren.organization.message.service.MessageReceiveService;
import com.ginkgocap.tongren.organization.message.service.MessageSendService;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 桐人消息接口实现类
 * @author liny
 *
 */
@Service("messageService")
public class MessageServiceImpl extends AbstractCommonService<MessageSend> implements MessageService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	private MessageCreateService messageCreateService;//消息主题实体service
	@Autowired
	private MessageSendService messageSendService;//消息发送记录service
	@Autowired
	private MessageReceiveService messageReceiveService;//消息接收记录service
	@Autowired
	private OrganizationMemberService organizationMemberService;//组织成员service
	@Autowired
	private UserService userService;
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	@Autowired
	private OrganizationRoleService organizationRoleService;
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
	
	private static long MIN_DATE_PARTITION=1448899200000l;
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#sendMessage(java.lang.String, java.lang.String, long, long, long, long, long, long, int)
	 */
	@Override
	@Transactional
	public boolean sendMessage(String title, String content, long sendUserId,
			long sendOrganizationId, long sendDepartmentId, long receiveUserId,
			long receiveOrganizationId, long receiveDepartmentId,
			int messageType,long projectId) throws Exception {
		
		MessageCreate messageCreate = new MessageCreate();
		MessageSend messageSend = new MessageSend();
		MessageReceive messageReceive = new MessageReceive();
		try {
			//消息实体
			messageCreate.setTitle(title);
			messageCreate.setContent(content);
			messageCreate.setCreateId(sendUserId);
			messageCreate.setMessageType(messageType);
			messageCreate.setMessageStatus(1);
			messageCreate.setOrganizationId(sendOrganizationId);
			messageCreate.setCreateTime(new Timestamp(System.currentTimeMillis()));
			messageCreate.setProjectId(projectId);
			messageCreate = messageCreateService.save(messageCreate);
			//发送消息实体
			messageSend.setDepartmentId(sendDepartmentId);
			messageSend.setMessageId(messageCreate.getId());
			messageSend.setOrganizationId(sendOrganizationId);
			messageSend.setUserId(sendUserId);
			messageSend.setSendTime(new Timestamp(System.currentTimeMillis()));
			messageSend.setStatus(1);
			messageSend.setDepartmentId(sendDepartmentId);
			messageSend = messageSendService.save(messageSend);
			//接收消息实体
			messageReceive.setDepartmentId(receiveDepartmentId);
			messageReceive.setMessageId(messageSend.getId());
			messageReceive.setOrganizationId(receiveOrganizationId);
			messageReceive.setReceiveTime(new Timestamp(System.currentTimeMillis()));
			messageReceive.setUserId(receiveUserId);
			messageReceive.setStatus(1);
			messageReceive = messageReceiveService.save(messageReceive);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sendMessage is error",e);
			throw new Exception();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#getMessageList(long)
	 */
	@Override
	public List<MessageCreate> getMessageList(long userId) {
		List<MessageSend> messageSendList = new ArrayList<MessageSend>();
		List<MessageCreate> messageCreateList = new ArrayList<MessageCreate>();
		try {
			List<Long> receIds = messageReceiveService.getKeysByParams("MessageReceive_List_userid_id", userId);
			List<MessageReceive> messageReceiveList = messageReceiveService.getEntityByIds(receIds);
			
			
			if(messageReceiveList == null || messageReceiveList.size() == 0)
				return null;
			for (MessageReceive messageReceive : messageReceiveList) {
				MessageSend messageSend =  messageSendService.getEntityById(messageReceive.getMessageId());
				if(messageSend != null)
					messageSendList.add(messageSend);
			}
			if(messageSendList == null || messageSendList.size() == 0)
				return null;
			for (MessageSend messageSend : messageSendList) {
				MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());
				if(messageCreate != null)
					messageCreateList.add(messageCreate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return messageCreateList;
	}


	/*
	 * 
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#deleteMessage(long, long)
	 */
	@Override
	public boolean deleteMessage(long userId,long organizationId){
		List<Long> receIds = messageReceiveService.getKeysByParams("MessageReceive_List_userid_orgid_id", new Object[]{userId,organizationId});
		List<Long> delrIds = new ArrayList<Long>();
		List<Long> delsIds = new ArrayList<Long>();
		List<Long> delcIds = new ArrayList<Long>();
		if(receIds.size() == 0 || receIds == null)
			return false;
		List<MessageReceive> messageReceiveList = messageReceiveService.getEntityByIds(receIds);
		for (MessageReceive messageReceive : messageReceiveList) {
			MessageSend messageSend =  messageSendService.getEntityById(messageReceive.getMessageId());
			MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());
			messageSend.setMessageCreate(messageCreate);
			messageSend.setMessageReceive(messageReceive);
			if(messageCreate.getMessageType() == 0){
				delrIds.add(messageReceive.getId());
				delsIds.add(messageSend.getId());
				delcIds.add(messageCreate.getId());
			}
		}
		if(receIds == null || receIds.size() ==0)
			return false;
		if(delrIds != null || delrIds.size()> 0)
			messageReceiveService.deleteEntityByIds(delrIds);
		if(delsIds != null || delsIds.size() > 0)
			messageSendService.deleteEntityByIds(delsIds);
		if(delcIds != null || delcIds.size() > 0)
			messageCreateService.deleteEntityByIds(delcIds);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#searchMessageByKeywordAndUserID(long, java.lang.String)
	 */
	@Override
	public List<MessageSend> searchMessageByKeywordAndUserID(long userId,
			String keyword,long organizationId) {
		List<MessageSend> mesList = this.getMessageListByOrgUid(userId, organizationId);
		List<MessageSend> mesNewList = new ArrayList<MessageSend>();
		if(mesList == null || mesList.size() == 0)
			return null;
		for (MessageSend messageSend : mesList) {
			if(!StringUtils.isEmpty(keyword)){
				if (messageSend.getMessageCreate().getTitle().contains(keyword)
						|| messageSend.getMessageCreate().getContent().contains(keyword)
						|| messageSend.getMessageCreate().getTitle().equals(keyword)
						|| messageSend.getMessageCreate().getContent().equals(keyword)) {
					mesNewList.add(messageSend);
				}
			}
		}
		return mesNewList;
	}

	/**
	 * 获取我的消息裂表，组织id（organizationId）不为0时，获取我在 组织下面的消息列表
	 */
	@Override
	public List<MessageSend> getMessageListByOrgUid(long userId,long organizationId) {
		List<MessageSend> messageSendList = new ArrayList<MessageSend>();
		List<Long> receIds = new ArrayList<Long>();
		List<MessageReceive> messageReceiveList=null;
		if(organizationId == 0){//个人消息，对应我的桐人消息页面
			receIds = messageReceiveService.getKeysByParams("MessageReceive_List_userid_id", userId);
		}else{//对应组织消息页面
			receIds = messageReceiveService.getKeysByParams("MessageReceive_List_userid_orgid_id", new Object[]{userId,organizationId});
		}
		if(receIds.size() == 0 || receIds == null){
			return messageSendList;
		}
		messageReceiveList = messageReceiveService.getEntityByIds(receIds);
		/**
		 * 消息接收倒序排序
		 */
		Collections.sort(messageReceiveList,new Comparator<MessageReceive>() {
			@Override
			public int compare(MessageReceive o1, MessageReceive o2) {
				return o2.getReceiveTime().compareTo(o1.getReceiveTime());
			}
		});
			
		for (MessageReceive messageReceive : messageReceiveList) {
			MessageSend messageSend =  messageSendService.getEntityById(messageReceive.getMessageId());
			MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());
			messageSend.setMessageCreate(messageCreate);
			messageSend.setMessageReceive(messageReceive);
			if(messageCreate!= null && messageCreate.getMessageType() != 0){
				messageSendList.add(messageSend);
			}
		}
		return messageSendList;
	}
	
	/**
	 * 此接口没用
	 */
	@Deprecated
	public void sendMessageAfterUploadObjProject(long userId, long organizationId, long projectId, String docTile) {
		List<OrganizationMember> orgMemberList = null;
		User user = null;
		try {
			orgMemberList = organizationMemberService.getNormalMember(organizationId);
			user = userService.selectByPrimaryKey(userId);
			if (orgMemberList != null && user != null) {

				for (OrganizationMember organizationMember : orgMemberList) {
					try {
						sendMessage(projectId + CommonConstants.PROJECT_MEMBER + user.getName() + CommonConstants.SUBMITING + docTile
								+ CommonConstants.DOCUMENT, projectId + CommonConstants.PROJECT_MEMBER + user.getName() + CommonConstants.SUBMITING
								+ docTile + CommonConstants.DOCUMENT, userId, organizationId, 0, organizationMember.getUserId(),
								organizationMember.getOrganizationId(), 0, 5, 0);
					} catch (Exception e) {
						logger.error("sendMessage error [ 发送人ID:" + userId + "] [接收人ID:" + organizationMember.getUserId() + "]", e.getMessage());
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sendMessageAfterUploadObjProject is error", e.getMessage());
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#deleteReceiveMessage(long)
	 */
	@Override
	public boolean deleteReceiveMessage(long messageReceiveID) throws Exception {
		return messageReceiveService.deleteEntityById(messageReceiveID);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#getMessageByOrgUidStatus(long, long, int)
	 */
	@Override
	public List<MessageSend> getMessageByOrgUidStatus(long userId, long organizationId, int messageType) throws Exception {
		List<MessageSend> messageSendList = new ArrayList<MessageSend>();
		List<Long> receIds = new ArrayList<Long>();
		receIds = messageReceiveService.getKeysByParams("MessageReceive_List_userid_orgid_id", new Object[] { userId, organizationId });
		if (receIds.size() == 0 || receIds == null)
			return messageSendList;
		List<MessageReceive> messageReceiveList = messageReceiveService.getEntityByIds(receIds);
		for (MessageReceive messageReceive : messageReceiveList) {
			MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
			MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());
			messageSend.setMessageCreate(messageCreate);
			messageSend.setMessageReceive(messageReceive);
			if (messageCreate.getMessageType() == messageType) {// 根据消息类型匹配结果封装list
				messageSendList.add(messageSend);
			}
		}
		return messageSendList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#sendMessage(java.lang.String, java.lang.String, long, long, long, long, long, long, int, long, int)
	 */
	@Override
	public boolean sendMessage(String title, String content, long sendUserId, long sendOrganizationId, long sendDepartmentId, long receiveUserId,
			long receiveOrganizationId, long receiveDepartmentId, int messageType, long projectId, int cycle) throws Exception {
		MessageCreate messageCreate = new MessageCreate();
		MessageSend messageSend = new MessageSend();
		MessageReceive messageReceive = new MessageReceive();
		try {
			// 消息实体
			messageCreate.setTitle(title);
			messageCreate.setContent(content);
			messageCreate.setCreateId(sendUserId);
			messageCreate.setMessageType(messageType);
			messageCreate.setMessageStatus(1);
			messageCreate.setOrganizationId(sendOrganizationId);
			messageCreate.setCreateTime(new Timestamp(System.currentTimeMillis()));
			messageCreate.setProjectId(projectId);
			messageCreate.setCycle(cycle);
			messageCreate = messageCreateService.save(messageCreate);
			// 发送消息实体
			messageSend.setDepartmentId(sendDepartmentId);
			messageSend.setMessageId(messageCreate.getId());
			messageSend.setOrganizationId(sendOrganizationId);
			messageSend.setUserId(sendUserId);
			messageSend.setSendTime(new Timestamp(System.currentTimeMillis()));
			messageSend.setStatus(1);
			messageSend.setDepartmentId(sendDepartmentId);
			messageSend = messageSendService.save(messageSend);
			// 接收消息实体
			messageReceive.setDepartmentId(receiveDepartmentId);
			messageReceive.setMessageId(messageSend.getId());
			messageReceive.setOrganizationId(receiveOrganizationId);
			messageReceive.setReceiveTime(new Timestamp(System.currentTimeMillis()));
			messageReceive.setUserId(receiveUserId);
			messageReceive.setStatus(1);
			messageReceive = messageReceiveService.save(messageReceive);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sendMessage is error", e);
			throw new Exception();
		}
	}

	/**
	 * 删除项目关联的消息
	 */
	@Override
	public void deleteMessageByProjectId(long projectId) throws Exception {
		List<Long> ids = messageCreateService.getKeysByParams("MessageCreate_List_projectId", projectId);
		List<Long> idsSend = new ArrayList<Long>();
		List<Long> idsReceive = new ArrayList<Long>();
		if (ids == null) {
			logger.error("search messageCreate by projectId 【" + projectId + "】 result is null");
		} else {
			List<MessageCreate> messageCreateList = messageCreateService.getEntityByIds(ids);
			if (null != messageCreateList && messageCreateList.size() > 0) {
				for (MessageCreate messageCreate : messageCreateList) {
					Long messageSendId = messageSendService.getMappingByParams("MessageSend_List_messageId", new Object[] { messageCreate.getId() });
					if (messageSendId != null) {
						idsSend.add(messageSendId);
						MessageSend messageSend = messageSendService.getEntityById(messageSendId);
						if (messageSend != null) {
							Long messageReceiveId = messageReceiveService.getMappingByParams("MessageReceive_List_messageId",
									new Object[] { messageSend.getId() });
							if (messageReceiveId != null)
								idsReceive.add(messageReceiveId);
						}
					}
				}
			}
			messageCreateService.deleteEntityByIds(ids);
			messageSendService.deleteEntityByIds(idsSend);
			messageReceiveService.deleteEntityByIds(idsReceive);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.MessageService#getMessageCreateListId(long, long, com.ginkgocap.tongren.common.model.MessageType)
	 */
	@Override
	public List<Long> getMessageCreateListId(long projectId, long createId, MessageType messageType) throws Exception {
		List<Long> longs = new ArrayList<Long>();
		try {
			longs = messageCreateService.getKeysByParams("MessageCreate_List_projectId_createId_messageType", new Object[] { projectId, createId,
					messageType.getType() });
		} catch (Exception e) {
			logger.error("getMessageCreateListId failed! "+projectId+","+createId+","+messageType.getType(),e);
		}
		return longs;
	}
	@Override
	public List<MessageSend> getMyMessages(long userId,long time,int pageSize) throws Exception {

		List<MessageSend> list = new ArrayList<MessageSend>();
		//根据时间查询消息
		try{
			Calendar queryDate=Calendar.getInstance();
			queryDate.setTimeInMillis(time);
			List<Long> receIds = new ArrayList<Long>();
			SimpleDateFormat sf=new SimpleDateFormat("yyyyMM");
			
			while(list.size() != pageSize && queryDate.getTimeInMillis()>=MIN_DATE_PARTITION){
				
				receIds = getKeysByParams("MessageReceive_List_userid_date",DaoSortType.DESC, new Object[] {userId,sf.format(queryDate.getTime())});
				if(CollectionUtils.isNotEmpty(receIds)){
					
					List<MessageReceive> messageReceiveList = messageReceiveService.getEntityByIds(receIds);
					for (MessageReceive messageReceive : messageReceiveList) {
						MessageSend messageSend =  messageSendService.getEntityById(messageReceive.getMessageId());
						MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());
						messageSend.setMessageCreate(messageCreate);
						messageSend.setMessageReceive(messageReceive);
						if(messageCreate!= null && messageCreate.getMessageType() != 0){
							list.add(messageSend);
						}
						if(list.size() == pageSize){
							break;
						}
					}
				}
				if(list.size() != pageSize){
					queryDate.add(Calendar.MONTH, -1);
				}
			}
		}catch(Exception e){
			logger.error("getMyMessages is error.."+userId);
			e.printStackTrace();
		}
		return list;
	}

	@Override
	protected Class<MessageSend> getEntity() {
		return this.getEntity();
	}

	@Override
	public Map<String, Object> doWork() {
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		return null;
	}

	@Override
	public String doError() {
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		return null;
	}

	@Override
	public boolean updateMessageStatus(long messageReceiveID) throws Exception {
		
		try{
			
			MessageReceive  messageReceive = messageReceiveService.getEntityById(messageReceiveID);
			
			if(messageReceive == null){
				logger.info("messageReceive is not found messageReceiveID = "+ messageReceiveID);
				return false;
			}
			if(messageReceive.getStatus() == 1){
				logger.info("messageReceiveStatus is 1");
				return false;
			}
			messageReceive.setStatus(0);
			messageReceiveService.update(messageReceive);
			return true;
		}catch(Exception e){
			logger.info("updateMessageStatus is error..." + messageReceiveID);
			e.printStackTrace();
			return false;
		}
		
	}
	
}
