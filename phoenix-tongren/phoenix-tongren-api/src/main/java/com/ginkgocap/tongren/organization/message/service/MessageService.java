/**
 * 
 */
package com.ginkgocap.tongren.organization.message.service;

import java.util.List;

import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.organization.message.model.MessageCreate;
import com.ginkgocap.tongren.organization.message.model.MessageSend;

/**
 * 桐人消息接口
 * @author liny
 *
 */
public interface MessageService {
	
	/**
	 * 桐人消息发送接口
	 * @param title 消息标题
	 * @param content 消息内容
	 * @param sendUserId 发送者ID
	 * @param sendOrganizationId 发送者组织ID
	 * @param sendDepartmentId 发送人部门ID
	 * @param receiveUserId 接收人ID
	 * @param receiveOrganizationId 接收者组织ID
	 * @param receiveDepartmentId 接收者部门ID
	 * @param messageType 消息类型(详细查看com.ginkgocap.tongren.common.model.MessageType)
	 * @return
	 */
	public boolean sendMessage(String title,String content,long sendUserId,long sendOrganizationId,long sendDepartmentId,long receiveUserId,long receiveOrganizationId,long receiveDepartmentId,int messageType,long projectId) throws Exception;
	/**
	 * 消息发送接口带延期天数
	 * @param title 消息标题
	 * @param content 消息内容
	 * @param sendUserId 发送者ID
	 * @param sendOrganizationId 发送者组织ID
	 * @param sendDepartmentId 发送人部门ID
	 * @param receiveUserId 接收人ID
	 * @param receiveOrganizationId 接收者组织ID
	 * @param receiveDepartmentId 接收者部门ID
	 * @param projectId 项目ID
	 * @param cycle 延期天数
	 * @param messageType 消息类型(详细查看com.ginkgocap.tongren.common.model.MessageType)
	 * @return
	 */
	public boolean sendMessage(String title,String content,long sendUserId,long sendOrganizationId,long sendDepartmentId,long receiveUserId,long receiveOrganizationId,long receiveDepartmentId,int messageType,long projectId,int cycle) throws Exception;
	/**
	 * 功能描述： 根据用户id查询我的消息
	 *    
	 * @param userId 用户ID
	 * @return 集合                                                
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月13日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public List<MessageCreate> getMessageList(long userId);
	
//	/**
//	 * 功能描述：操作申请消息是通过或忽略
//	 *                                                       
//	 * @param status 1:通过 2:忽略
//	 * @param messageReceiveId 我的消息中消息ID
//	 * @return 操作是否成功                                                                                                 
//	 * @author 林阳 [linyang@gintong.com]
//	 * @since 2015年10月13日
//	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
//	 *
//	 */
//	public boolean operateApplicationMessage(int status,long messageReceiveId)throws Exception ;
	/**
	 * 功能描述：根据用户ID删除我的消息         
	 *                                                       
	 * @param userId
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean deleteMessage(long userId,long organizationId);
	/**
	 * 功能描述：   根据用户ID和关键字查询消息信息
	 * @param userId
	 * @param keyword
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月20日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<MessageSend> searchMessageByKeywordAndUserID(long userId,String keyword,long organizationId);
	/**
	 * 功能描述：  根据用户ID和组织ID查询       
	 *                                                       
	 * @param userId 用户ID
	 * @param organizationId 组织ID
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月27日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<MessageSend> getMessageListByOrgUid(long userId,long organizationId);
	/**
	 * 功能描述：   根据用户ID组织ID和消息类型    查询消息 
	 *                                                       
	 * @param userId 用户ID
	 * @param organizationId 组织ID
	 * @param messageType 消息类型(详细查看com.ginkgocap.tongren.common.model.MessageType)
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月13日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<MessageSend> getMessageByOrgUidStatus(long userId,long organizationId,int messageType) throws Exception;
	/**
	 * 功能描述：  上传文件后发送消息           
	 *                                                       
	 * @param userId
	 * @param organizationId
	 * @param projectId
	 * @param docTile   文件标题                                                                                              
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月9日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public void sendMessageAfterUploadObjProject(long userId,long organizationId,long projectId,String docTile);
	/**
	 * 功能描述：  根据我的消息ID（接收消息ID）删除某一个消息       
	 *                                                       
	 * @param messageReceiveID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean deleteReceiveMessage(long messageReceiveID) throws Exception;
	/**
	 * 功能描述：  根据项目ID删除多余的消息       
	 *                                                       
	 * @param projectId
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年12月2日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public void deleteMessageByProjectId(long projectId) throws Exception;
	/**
	 * 功能描述：根据消息状态项目ID和创建ID查询消息创建实体集合         
	 *                                                       
	 * @param projectId 项目ID
	 * @param createId 创建者ID
	 * @param messageType 消息类型对象
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年12月7日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<Long> getMessageCreateListId(long projectId,long createId,MessageType messageType)throws Exception;
	
	/**
	 * 功能描述 ：分页获取用户的消息集合
	 * 
	 * @param userId 用户id
	 * @param time 时间戳
	 * @param pageSize 返回多少条
	 * @return page<MessageSend>
	 */ 
	public List<MessageSend> getMyMessages(long userId,long time,int pageSize)throws Exception;
	
	/**
	 * 
	 * 修改消息状态
	 * 
	 */
	public boolean updateMessageStatus(long messageReceiveID)throws Exception;
}
