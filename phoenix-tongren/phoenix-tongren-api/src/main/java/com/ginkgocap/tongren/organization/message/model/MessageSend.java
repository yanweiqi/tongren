/**
 * 
 */
package com.ginkgocap.tongren.organization.message.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * 组织消息发送记录
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_message_send")
public class MessageSend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long messageId;//消息ID
	
	private Timestamp sendTime;//发送时间
	
	private long userId;//发送者id
	
	private int status;//发送状态 0待发送、1发送成功、2发送失败
	
	private long organizationId;//发送者所属组织ID
	
	private long departmentId;//发送者所属部门ID
	
	private MessageCreate messageCreate;//需要消息实体
	
	private MessageReceive MessageReceive;
	
	/**
	 * @return 返回 messageReceive。
	 */
	@Transient
	public MessageReceive getMessageReceive() {
		return MessageReceive;
	}

	/**
	 * ---@param messageReceive 要设置的 messageReceive。
	 */
	public void setMessageReceive(MessageReceive messageReceive) {
		MessageReceive = messageReceive;
	}

	/**
	 * @return 返回 messageCreate。
	 */
	@Transient
	public MessageCreate getMessageCreate() {
		return messageCreate;
	}

	/**
	 * ---@param messageCreate 要设置的 messageCreate。
	 */
	public void setMessageCreate(MessageCreate messageCreate) {
		this.messageCreate = messageCreate;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "MessageSendID")
	@GenericGenerator(name = "MessageSendID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "MessageSendID") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the messageId
	 */
	@Column(name = "message_id")
	public long getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the sendTime
	 */
	@Column(name = "send_time")
	public Timestamp getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the userId
	 */
	@Column(name = "user_id")
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the status
	 */
	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the organizationId
	 */
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the departmentId
	 */
	@Column(name = "department_id")
	public long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	
}
