/*
 * 文件名： SendMessageService.java
 * 创建日期： 2015年11月10日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.message.service;

import java.util.List;



 /**
 * 消息发送类
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月10日
 */
public interface TongRenSendMessageService {

	/**
	 * 功能描述：   邀请加入组织发送消息      
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人
	 * @param organizationId 组织ID
	 * @return true 发送成功     false 发送失败
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendOrganizationInvitation(long sendUID,long receiveUID,long organizationId) throws Exception;
	/**
	 * 功能描述：  申请加入组织发送消息       
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人
	 * @param organizationId 组织ID
	 * @return true 发送成功     false 发送失败
	 * 
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendOrganizationApplication(long sendUID,long receiveUID,long organizationId) throws Exception;
	/**
	 * 功能描述： 申请退出组织发消息      
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人
	 * @param organizationId 组织ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendOrganizationSignOut(long sendUID,long receiveUID,long organizationId)throws Exception;
	/**
	 * 功能描述：  同意或者拒绝加入组织或退出组织发送消息      
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人
	 * @param organizationId 组织ID
	 * @param status 1 同意    2 拒绝
	 * @param type 4(同意加入消息) 5(拒绝加入消息) 6(同意退出组织消息) 7(拒绝退出组织消息)
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendAgreeORDisagreeToJoin(long sendUID,long receiveUID,long organizationId,int status,int type) throws Exception;
	/**
	 * 功能描述：解散组织 
	 *                                                       
	 * @param sendUID
	 * @param organizationId
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendDissolutionORKick(long sendUID,long organizationId) throws Exception;
	/**
	 * 功能描述： 删除成员发送消息（被组织管理者踢出组织发消息）        
	 *                                                       
	 * @param sendUID
	 * @param receiveUID
	 * @param organizationId
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendKickMemberMes(long sendUID,long receiveUID,long organizationId) throws Exception;
	/**
	 * 功能描述：   组织任务发送消息 
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收ID
	 * @param organizationId 组织ID
	 * @param type 10、分配任务   11、退回任务  12、重发任务
	 * @param taskName 任务名称 
	 * @return 
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendOrganizationalTasksMes(long sendUID,long receiveUID,long organizationId,int type,String taskName) throws Exception;
	/**
	 * 功能描述：    项目放弃和结束发送消息     
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param organizationId 项目承接的组织ID
	 * @param projectName 项目名称
	 * @param type 13、放弃项目 14、结束项目 
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendProjectMes(long sendUID,long organizationId,String projectName,int type,long projectId) throws Exception;
	/**
	 * 功能描述：   文档提交发送消息      
	 *                                                       
	 * @param sendUID 发送人
	 * @param organizationId 组织ID
	 * @param fileName 文档名称   
	 * @param projectId 项目ID 
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月10日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendSubDocumentMes(long sendUID,long organizationId,String fileName,long projectId) throws Exception;
	/**
	 * 功能描述： 分配子任务发送消息        
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人ID（承接子任务的人的人ID）
	 * @param organizationId 组织ID
	 * @param projectId 项目ID
	 * @param taskName 子任务名称
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendAssignmentSubTaskMes(long sendUID,long receiveUID,long organizationId,long projectId,String taskName) throws Exception;
	/**
	 * 功能描述：    组织/个人想承接某一个项目发送给项目创建者消息     
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人ID
	 * @param organizationId 组织ID
	 * @param projectId 项目ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendOrganizationToUndertakeProjects(long sendUID,long receiveUID,long organizationId,long projectId)throws Exception;
	/**
	 * 功能描述：邀请个人或者组织承接项目发送消息         
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人ID （如果是组织传组织创建者ID）集合
	 * @param organizationId 组织ID
	 * @param projectId 项目ID
	 * @param content 邀请内容
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendInvitationProjectMes(long sendUID,List<Long> receiveUID,List<Long> organizationId,long projectId,String content) throws Exception;
	/**
	 * 功能描述：   是否同意承接项目发送消息      
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人ID 
	 * @param organizationId 组织ID
	 * @param projectId 项目ID
	 * @param status 1：同意 2：拒绝
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendIsAgreeInvitationProjectMes(long sendUID,long receiveUID,long organizationId,long projectId,int status) throws Exception;
	/**
	 * 功能描述：项目延期发送消息         
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param organiztionId 组织ID
	 * @param projectId 项目ID
	 * @param cycle 延期天数
	 * @param content 延期原因
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendExtensioProjectMes(long sendUID,long organiztionId,long projectId,int cycle,String content) throws Exception;
	/**
	 * 功能描述： 是否同意延期项目发送消息        
	 *                                                       
	 * @param sendUID 发送人ID
	 * @param receiveUID 接收人ID
	 * @param organizationId 组织ID
	 * @param projectId 项目ID
	 * @param status 1：同意 2：拒绝
	 * @param cycle 延期天数
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public boolean sendIsAgreeExtensioProjectMes(long sendUID,long receiveUID,long organizationId,long projectId,int status,int cycle) throws Exception;
	
	/**
	 * 功能描述 ：任务提醒发送消息
	 * 
	 */
	public boolean sendTaskRemind(long sendUID,long receiveUID,long organizationId,long projectId,String taskName)throws Exception;
}
