/*
 * 文件名： MessageVO.java
 * 创建日期： 2015年10月26日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.message.vo;

import java.sql.Timestamp;


 /**
 *  消息VO对象
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月26日
 */
public class MessageVO implements java.io.Serializable{

	/**
	 * ---serialVersionUID
	 */
	private static final long serialVersionUID = 8535238105352792784L;
	
	private Long messageReceiveID;//我的消息ID，接收消息ID
	
	private String title;//消息标题
	
	private String content;//内容
	
	private Long sendUserID;
	
	private String sendUserName;//发送人姓名
	
	private String sendUserPic;//发送人头像
	
	private Timestamp sendTime;//发送时间
	
	private long organizationId;//消息所属组织id
	
	private int MessageType;//消息类型0普通消息、1项目消息、2邀请消息、3申请消息、4退出消息、5文档类消息
	
	private String MessageTypeRes;//消息类型结果
	
	private int messageStatus; //消息状态 0已阅读、1未阅读 
	
	/**
	 * @return 返回 messageReceiveID。
	 */
	public Long getMessageReceiveID() {
		return messageReceiveID;
	}

	/**
	 * ---@param messageReceiveID 要设置的 messageReceiveID。
	 */
	public void setMessageReceiveID(Long messageReceiveID) {
		this.messageReceiveID = messageReceiveID;
	}

	/**
	 * @return 返回 sendUserName。
	 */
	public String getSendUserName() {
		return sendUserName;
	}

	/**
	 * ---@param sendUserName 要设置的 sendUserName。
	 */
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	/**
	 * @return 返回 title。
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ---@param title 要设置的 title。
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return 返回 content。
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ---@param content 要设置的 content。
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return 返回 sendUserID。
	 */
	public Long getSendUserID() {
		return sendUserID;
	}

	/**
	 * ---@param sendUserID 要设置的 sendUserID。
	 */
	public void setSendUserID(Long sendUserID) {
		this.sendUserID = sendUserID;
	}

	/**
	 * @return 返回 sendUserPic。
	 */
	public String getSendUserPic() {
		return sendUserPic;
	}

	/**
	 * ---@param sendUserPic 要设置的 sendUserPic。
	 */
	public void setSendUserPic(String sendUserPic) {
		this.sendUserPic = sendUserPic;
	}

	/**
	 * @return 返回 sendTime。
	 */
	public Timestamp getSendTime() {
		return sendTime;
	}

	/**
	 * ---@param sendTime 要设置的 sendTime。
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return 返回 messageType。
	 */
	public int getMessageType() {
		return MessageType;
	}

	/**
	 * ---@param messageType 要设置的 messageType。
	 */
	public void setMessageType(int messageType) {
		MessageType = messageType;
	}

	/**
	 * @return 返回 messageTypeRes。
	 */
	public String getMessageTypeRes() {
		return MessageTypeRes;
	}

	/**
	 * ---@param messageTypeRes 要设置的 messageTypeRes。
	 */
	public void setMessageTypeRes(String messageTypeRes) {
		MessageTypeRes = messageTypeRes;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public int getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(int messageStatus) {
		this.messageStatus = messageStatus;
	}
	
}
