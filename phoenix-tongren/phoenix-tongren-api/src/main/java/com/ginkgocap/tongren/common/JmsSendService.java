package com.ginkgocap.tongren.common;

import com.ginkgocap.tongren.common.model.JmsMsgBean;

/**
 * 消息发送接口
 * @author hanxifa
 *
 */
public interface JmsSendService {

	/**
	 * 发送给消息队列
	 * @param msg
	 * @return
	 */
	public String sendJmsMsg(JmsMsgBean jmsMsgBean);
	
	/**
	 * 失败重新发送
	 */
	public void resendFailedRecord();
	
	
}
