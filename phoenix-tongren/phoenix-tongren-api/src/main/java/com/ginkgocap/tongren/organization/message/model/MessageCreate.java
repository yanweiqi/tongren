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
 * 
 * 组织消息创建
 * @author liny
 *
 */
@Entity
@Table(name="tb_organization_message_create")
public class MessageCreate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String title;//标题
	
	private String content;//消息内容
	
	private Timestamp createTime;//创建时间
	
	private Timestamp updateTime;//更新时间
	
	private long createId;//创建者ID
	
	private long organizationId;//组织者ID
	
	private int MessageStatus;//消息状态 0草稿、1正稿
	
	private int messageType;//消息类型 0、普通消息  1、邀请组织消息 2、申请组织消息 3、申请退出组织消息 
									//4、加入组织消息 5、拒绝加入组织消息 6、退出组织消息 7、拒绝退出组织消息
									//8、解散组织 9、被组织踢 10、分配任务 11、退回任务 12、重发任务 
									//13、放弃项目 14、结束项目 15、某组织想承接项目 16、提项目文档 
									//17、分配子任务 18、项目邀请组织承接 19、同意承接项目 20、拒绝承接项目
									//21、延期项目申请 22、同意项目延期申请 23 、拒绝延期项目申请
	
	private Long projectId;//项目ID
	
	private int cycle;//延期项目申请的天数
	

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "MessageCreateID")
	@GenericGenerator(name = "MessageCreateID", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "MessageCreateID") })
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
	 * @return the title
	 */
	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	@Column(name = "content")
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the createTime
	 */
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the createId
	 */
	@Column(name = "create_id")
	public long getCreateId() {
		return createId;
	}

	/**
	 * @param createId the createId to set
	 */
	public void setCreateId(long createId) {
		this.createId = createId;
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
	 * @return the messageStatus
	 */
	@Column(name = "message_status")
	public int getMessageStatus() {
		return MessageStatus;
	}

	/**
	 * @param messageStatus the messageStatus to set
	 */
	public void setMessageStatus(int messageStatus) {
		MessageStatus = messageStatus;
	}

	/**
	 * @return the messageType
	 */
	@Column(name = "message_type")
	public int getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return 返回 projectId。
	 */
	@Column(name = "project_id")
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * ---@param projectId 要设置的 projectId。
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return 返回 cycle。
	 */
	@Column(name = "cycle")
	public int getCycle() {
		return cycle;
	}

	/**
	 * ---@param cycle 要设置的 cycle。
	 */
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
}
