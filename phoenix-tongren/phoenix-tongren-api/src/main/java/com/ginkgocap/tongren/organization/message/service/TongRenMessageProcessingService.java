/*
 * 文件名： TongRenMessageProcessing.java
 * 创建日期： 2015年11月16日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.message.service;

import com.ginkgocap.tongren.organization.message.exception.MessageException;


 /**
 * 消息处理接口
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月16日
 */
public interface TongRenMessageProcessingService {

	/**
	 * 功能描述：邀请组织成员消息操作         
	 *                                                       
	 * @param status 1:通过 2:忽略
	 * @param messageReceiveId 我的消息中消息ID
	 * @return 操作是否成功     
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean messageOrganizationInvitation(int status,long messageReceiveId) throws Exception;
	/**
	 * 功能描述： 申请退出消息操作        
	 *                                                       
	 * @param status 1:通过 2:忽略
	 * @param messageReceiveId 我的消息中消息ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean messageOrganizationSignOut(int status,long messageReceiveId) throws Exception;
	/**
	 * 功能描述： 申请加入组织消息处理        
	 *                                                       
	 * @param status 1:通过 2:忽略
	 * @param messageReceiveId 我的消息中消息ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean messageOrganizationApplication(int status,long messageReceiveId) throws Exception;
	/**
	 * 功能描述： 项目邀请人承接项目消息处理申请        
	 *                                                       
	 * @param status 1:通过 2:忽略 
	 * @param messageReceiveId 我的消息中消息ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月18日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public String messageInvitationProjectProcessing(int status,long messageReceiveId) throws Exception;
	/**
	 * 功能描述：延期项目消息处理         
	 *                                                       
	 * @param status 1:通过 2:忽略 
	 * @param messageReceiveId 我的消息中消息ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean messageExtensioProject(int status,long messageReceiveId) throws Exception;
	/**
	 * 功能描述： 根据messageReceiveId查找此消息的类型    
	 *                                                       
	 * @param messageReceiveId 接收消息ID
	 * @return messageType 消息类型(详细查看com.ginkgocap.tongren.common.model.MessageType)
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月17日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public int getMessageTypeByMessageReceiveId(long messageReceiveId) throws MessageException;
}
