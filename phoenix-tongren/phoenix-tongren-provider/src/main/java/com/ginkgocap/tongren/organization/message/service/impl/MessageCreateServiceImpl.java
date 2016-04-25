/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.message.model.MessageCreate;
import com.ginkgocap.tongren.organization.message.service.MessageCreateService;

/**
 * @author liny
 *
 */
@Service("messageCreate")
public class MessageCreateServiceImpl extends AbstractCommonService<MessageCreate>
		implements MessageCreateService {

	//private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleServiceImpl.class);

	@Override
	public Map<String, Object> doWork() {
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
	protected Class<MessageCreate> getEntity() {
		// TODO Auto-generated method stub
		return MessageCreate.class;
	}

}
