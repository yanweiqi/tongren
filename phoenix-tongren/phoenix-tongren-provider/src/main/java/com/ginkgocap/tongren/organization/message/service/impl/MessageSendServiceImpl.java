/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageSendService;

/**
 * @author liny
 *
 */
@Service("messageSendService")
public class MessageSendServiceImpl extends AbstractCommonService<MessageSend> implements MessageSendService {

	//private static final Logger logger = LoggerFactory.getLogger(MessageReceiveServiceImpl.class);
	
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
	protected Class<MessageSend> getEntity() {
		// TODO Auto-generated method stub
		return MessageSend.class;
	}

}
