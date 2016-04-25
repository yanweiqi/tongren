/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.message.model.MessageReceive;

/**
 * 消息接收接口
 * @author liny
 *
 */
public interface MessageReceiveService extends CommonService<MessageReceive>{
	
	public void updateReceiveTime(long userId,long organizationId,int messageType) throws Exception;

}
