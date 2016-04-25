/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.message.model.MessageCreate;
import com.ginkgocap.tongren.organization.message.model.MessageReceive;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageCreateService;
import com.ginkgocap.tongren.organization.message.service.MessageReceiveService;
import com.ginkgocap.tongren.organization.message.service.MessageSendService;
import com.ginkgocap.tongren.organization.message.service.MessageService;

/**
 * @author liny
 *
 */
@Service("messageReceive")
public class MessageReceiveServiceImpl extends AbstractCommonService<MessageReceive>
				implements MessageReceiveService  {

	private static final Logger logger = LoggerFactory.getLogger(MessageReceiveServiceImpl.class);
	
	@Autowired
	private MessageCreateService messageCreateService;
	
	@Autowired
	private MessageSendService messageSendService;
	
	@Autowired
	private MessageService messageService;

	@Override
	public Map<String, Object> doWork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<MessageReceive> getEntity() {
		return MessageReceive.class;
	}

	@Override
	public void updateReceiveTime(long userId,long organizationId,int messageType) throws Exception{
		try{	
			MessageReceive messageReceive = null;
			//获取组织邀请 具体消息
			List<MessageSend> messageList = messageService.getMessageByOrgUidStatus(userId, organizationId, messageType);
			messageReceive = messageList.get(0).getMessageReceive();
			if(null != messageReceive ){
				Timestamp t = new Timestamp(System.currentTimeMillis());
				MessageSend messageSend =  messageSendService.getEntityById(messageReceive.getMessageId());
				if(null != messageSend){
					messageSend.setSendTime(t);
					messageSendService.update(messageSend);
					MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());
					if(null != messageCreate){
						messageCreate.setCreateTime(t);
						messageCreateService.update(messageCreate);
					}
				}
				messageReceive.setReceiveTime(t);
				update(messageReceive);
			}
		}catch(Exception e){
			logger.error("updateReceiveTime is error.."+"userId..."+userId+"..organizationId.."+organizationId +"..messageType"+messageType,e);
			e.printStackTrace();
		}
	}
}
