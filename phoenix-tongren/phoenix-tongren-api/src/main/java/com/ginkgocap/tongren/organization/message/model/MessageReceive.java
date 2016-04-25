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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 组织消息接收记录
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_message_receive")
public class MessageReceive implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long messageId;//消息发送ID
	
	private long organizationId;//接收组织ID
	
	private long departmentId;//接收部门ID
	
	private long userId;//接收者ID
	
	private int status;//0已阅读、1未阅读
	
	private Timestamp receiveTime;//接收时间

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "MessageReceiveID")
	@GenericGenerator(name = "MessageReceiveID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "MessageReceiveID") })
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
	 * @return the receiveTime
	 */
	@Column(name = "receive_time")
	public Timestamp getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	
}
