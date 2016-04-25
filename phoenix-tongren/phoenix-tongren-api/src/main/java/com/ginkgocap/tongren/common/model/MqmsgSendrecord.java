package com.ginkgocap.tongren.common.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * mq消息发送记录表
 * @author hanxifa
 *
 */
@Entity 
@Table(name="tb_mq_msg_sendrecord")
public class MqmsgSendrecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long msgId;
	
	private String content;
	
	private String topic;
	
	private String tags;//发送消息的标签
	
	private Timestamp createTime;
	
	private Timestamp lastSendTime;
	
	private int sendStatus;//0发送成功 1 发送失败
	
	private int sendCount;//发送次数

	@Id
	@GeneratedValue(generator = "MqmsgSendrecordId")
	@GenericGenerator(name = "MqmsgSendrecordId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "MqmsgSendrecordId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "msg_id")
	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	@Column(name = "msg_content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "topic")
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Column(name = "tags")
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_send_time")
	public Timestamp getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(Timestamp lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	@Column(name = "send_status")
	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	@Column(name = "send_count")
	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	
	
}
