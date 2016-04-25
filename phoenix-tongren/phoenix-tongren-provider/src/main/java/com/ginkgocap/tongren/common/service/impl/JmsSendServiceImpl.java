package com.ginkgocap.tongren.common.service.impl;

import javax.jms.Destination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.model.JmsMsgBean;

/**
 * 发送jms消息
 * @author hanxifa
 *
 */

public class JmsSendServiceImpl implements JmsSendService{

	protected static final Logger logger = LoggerFactory.getLogger(JmsSendServiceImpl.class);
	

	
	@Autowired
	private Destination organizationQueueDestination;
	
	@Autowired
	private Destination projectQueueDestination;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 
	 * @return 1 发送成功 2 发送失败
	 */
	@Override
	public String sendJmsMsg(JmsMsgBean jmsMsgBean) {
//		//jmsMqTemplate.setConnectionFactory(connectionFactory);
//		try {
//			final String msgStr = JSON.json(jmsMsgBean);
//			logger.info("msg json str is "+msgStr);
//			// 组织消息
//			if (jmsMsgBean.getType() >= 10 && jmsMsgBean.getType() < 20) {
//
//				jmsTemplate.send(organizationQueueDestination, new MessageCreator() {
//					@Override
//					public Message createMessage(Session session) throws JMSException {
//						return session.createTextMessage(msgStr);
//					}
//				});
//				
//			} else if (jmsMsgBean.getType() >= 20 && jmsMsgBean.getType() < 30) {//项目消息
//				jmsTemplate.send(projectQueueDestination, new MessageCreator() {
//					@Override
//					public Message createMessage(Session session) throws JMSException {
//						return session.createTextMessage(msgStr);
//					}
//				});
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("send msg faild "+jmsMsgBean.getType()+","+jmsMsgBean.getContent(),e);
//			return "2";
//		}
//		return "1";
		return null;
	}

	@Override
	public void resendFailedRecord() {
		// TODO Auto-generated method stub
		
	}


	 

}
