/*
 * 文件名： TongRenMessageProcessingServiceImpl.java
 * 创建日期： 2015年11月16日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.message.service.impl;
import java.sql.Timestamp;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.message.exception.MessageException;
import com.ginkgocap.tongren.organization.message.model.MessageCreate;
import com.ginkgocap.tongren.organization.message.model.MessageReceive;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageCreateService;
import com.ginkgocap.tongren.organization.message.service.MessageReceiveService;
import com.ginkgocap.tongren.organization.message.service.MessageSendService;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;
import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.ProjectStatus;
import com.ginkgocap.tongren.project.manage.model.Publish;
import com.ginkgocap.tongren.project.manage.service.ApplyService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;


 /**
 * 消息处理实现类 
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月16日
 */
@Service("tongRenMessageProcessingService")
public class TongRenMessageProcessingServiceImpl implements TongRenMessageProcessingService {
	
	private static final Logger logger = LoggerFactory.getLogger(TongRenMessageProcessingServiceImpl.class);

	private static int error_object_null = 100; // 对象为空
	
	
	@Autowired
	private MessageCreateService messageCreateService;//消息主题实体service
	@Autowired
	private MessageSendService messageSendService;//消息发送记录service
	@Autowired
	private MessageReceiveService messageReceiveService;//消息接收记录service
	@Autowired
	private OrganizationMemberService organizationMemberService;//组织成员service
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;//组织service
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;//组织成员角色service
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;//桐人发送消息service
	@Autowired
	private PublishService publishService;//项目发布接口
	@Autowired
	private ApplyService applyService;//项目申请接口
	@Autowired
	private UndertakenService undertakenService;//承接项目接口
	@Autowired
	private OrganizationRoleService organizationRoleService;
	@Autowired
	private MessageService messageService;
	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService#messageOrganizationInvitation(int, long)
	 */
	@Override
	public boolean messageOrganizationInvitation(int status,
			long messageReceiveId) throws Exception {
		MessageReceive messageReceive = messageReceiveService.getEntityById(messageReceiveId);
		if(messageReceive == null)
			return false;
		MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
		if(messageSend == null)
			return false;
		MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());	
		if(messageCreate == null)
			return false;
		
		long orgMemberIds = organizationMemberService.getMappingByParams(
				"OrganizationMember_Map_useridAndOrganid",
				new Object[] { messageReceive.getOrganizationId(),
						messageReceive.getUserId() });// 邀请人查询成员时候，查询的是消息的接收人组织和信息
		
		if (orgMemberIds == 0){
			logger.debug("No query to member ID");
			return false;
		}
		OrganizationMember organizationMember = organizationMemberService.getEntityById(orgMemberIds);//获得成员表的对象信息
		Organization organization = tongRenOrganizationService.getEntityById(messageSend.getOrganizationId());
		if (organizationMember == null && organization == null){
			logger.debug("Members and organization objects are empty");
			return false;
		}
			
		if(status == 1){
			//OrganizationMemberRole organizationMemberRole = organizationMemberRoleService.addMemberRole(organization.getCreaterId(), organization.getId(), OrganizationRoles.MEMBER, organizationMember.getId());
			OrganizationRole or = organizationRoleService.getOrganizationRoleByOrganizationIdAndRoleName(organization.getId(), OrganizationRoles.MEMBER.name()); 
			OrganizationMemberRole organizationMemberRole = organizationMemberRoleService.addMemberRole(organization.getCreaterId(), organization.getId(), or.getId(), organizationMember.getId());
			if(organizationMemberRole == null){
				logger.debug("members to add role failure");
				return false;
			}
			organizationMember.setStatus(3);
			organizationMember.setApplyTime(new Timestamp(System.currentTimeMillis()));
			if (!organizationMemberService.update(organizationMember)){
				logger.debug("Update member state failure");
				return false;
			}
			List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(messageReceive.getOrganizationId());
			if(orgMemberList != null && orgMemberList.size() != 0){
				for (OrganizationMember organizationMember2 : orgMemberList) {
					tongRenSendMessageService.sendAgreeORDisagreeToJoin(messageReceive.getUserId(), organizationMember2.getUserId(), organizationMember2.getOrganizationId(), 1, 4);	
				}
				
			}else{
				logger.info("Members are empty, no message is available");
			}
			
		}else{
			if (!organizationMemberService.deleteEntityById(orgMemberIds)){
				logger.debug("Delete member of organization");
				return false;
			}
			tongRenSendMessageService.sendAgreeORDisagreeToJoin(messageReceive.getUserId(), organization.getCreaterId(), organization.getId(), 2, 5);	
		}
		return messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService#messageOrganizationApplication(int, long)
	 */
	@Override
	public boolean messageOrganizationApplication(int status,
			long messageReceiveId) throws Exception {
		
		MessageReceive messageReceive = messageReceiveService.getEntityById(messageReceiveId);
		if(messageReceive == null)
			return false;
		MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
		if(messageSend == null)
			return false;
		MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());	
		if(messageCreate == null)
			return false;
		long orgMemberIds = organizationMemberService.getMappingByParams(
				"OrganizationMember_Map_useridAndOrganid",
				new Object[] { messageSend.getOrganizationId(),
						messageSend.getUserId() });// 申请人查询成员时候，查询的是消息的发送人组织和信息
		
		if (orgMemberIds == 0){
			logger.debug("No query to member ID");
			return false;
		}
		OrganizationMember organizationMember = organizationMemberService.getEntityById(orgMemberIds);//获得成员表的对象信息
		Organization organization = tongRenOrganizationService.getEntityById(messageReceive.getOrganizationId());
		if (organizationMember == null && organization == null){
			logger.debug("Members and organization objects are empty");
			return false;
		}
		if(status == 1){//同意
			OrganizationRole or = organizationRoleService.getOrganizationRoleByOrganizationIdAndRoleName(organization.getId(), OrganizationRoles.MEMBER.name()); 
			OrganizationMemberRole organizationMemberRole = organizationMemberRoleService.addMemberRole(organization.getCreaterId(), organization.getId(), or.getId(), organizationMember.getId());
			//OrganizationMemberRole organizationMemberRole = organizationMemberRoleService.addMemberRole(organization.getCreaterId(), organization.getId(), OrganizationRoles.MEMBER, organizationMember.getId());
			if(organizationMemberRole == null){
				logger.debug("members to add role failure");
				return false;
			}
			organizationMember.setStatus(3);
			organizationMember.setApplyTime(new Timestamp(System.currentTimeMillis()));
			if (!organizationMemberService.update(organizationMember)){
				logger.debug("Update member state failure");
				return false;
			}
			List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(messageReceive.getOrganizationId());
			if(orgMemberList != null && orgMemberList.size() != 0){
				for (OrganizationMember organizationMember2 : orgMemberList) {
					tongRenSendMessageService.sendAgreeORDisagreeToJoin(messageReceive.getUserId(), organizationMember2.getUserId(), organizationMember2.getOrganizationId(), 1, 4);	
				}
			}else{
				logger.warn("Members are empty, no message is available");
			}
		}else{
			if (!organizationMemberService.deleteEntityById(orgMemberIds)){
				logger.debug("Delete member of organization");
				return false;
			}
			tongRenSendMessageService.sendAgreeORDisagreeToJoin(messageReceive.getUserId(), messageSend.getUserId(), organization.getId(), 2, 5);	
		}
		return messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService#messageOrganizationSignOut(int, long)
	 */
	@Override
	public boolean messageOrganizationSignOut(int status, long messageReceiveId)
			throws Exception {
		MessageReceive messageReceive = messageReceiveService.getEntityById(messageReceiveId);
		if(messageReceive == null)
			return false;
		MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
		if(messageSend == null)
			return false;
		MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());	
		if(messageCreate == null)
			return false;
		
		long orgMemberIds = organizationMemberService.getMappingByParams(
				"OrganizationMember_Map_useridAndOrganid",
				new Object[] { messageSend.getOrganizationId(),
						messageSend.getUserId() });// 申请人查询成员时候，查询的是消息的发送人组织和信息
		
		if (orgMemberIds == 0){
			logger.debug("No query to member ID");
			return false;
		}
		OrganizationMember organizationMember = organizationMemberService.getEntityById(orgMemberIds);//获得成员表的对象信息
		
		if(status == 1){//同意退出
			if (!organizationMemberService.delMemberAndRoleRelation(messageSend.getOrganizationId(), messageSend.getUserId())){
				logger.debug("Delete member of organization");
				return false;
			}
			
			List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(messageReceive.getOrganizationId());
			if(orgMemberList != null && orgMemberList.size() != 0){
				for (OrganizationMember organizationMember2 : orgMemberList) {
					tongRenSendMessageService.sendAgreeORDisagreeToJoin(messageSend.getUserId(), organizationMember2.getUserId(), organizationMember2.getOrganizationId(), 1, 6);	
				}
			}else{
				logger.warn("Members are empty, no message is available");
			}
		}else{
			organizationMember.setStatus(3);
			if (!organizationMemberService.update(organizationMember)){
				logger.debug("Update member state failure");
				return false;
			}
			tongRenSendMessageService.sendAgreeORDisagreeToJoin(messageReceive.getUserId(), messageSend.getUserId(), messageReceive.getOrganizationId(), 2, 7);
		}
		return messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService#getMessageTypeByMessageReceiveId(long)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public int getMessageTypeByMessageReceiveId(long messageReceiveId)
			throws MessageException {
		try {
			MessageReceive messageReceive = messageReceiveService.getEntityById(messageReceiveId);
			if(messageReceive == null){
				throw new MessageException(error_object_null, "The specified MessageReceive by messageReceiveId [" + ObjectUtils.toString(messageReceiveId, null)
						+ "] does not messageReceive object");
			}
			if(messageReceive.getStatus() == 1){
				messageReceive.setStatus(0);
				messageReceiveService.update(messageReceive);
			}
			
			MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
			if(messageSend == null){
				throw new MessageException(error_object_null, "The specified messageSend by messageSendId [" + ObjectUtils.toString(messageReceive.getId(), null)
						+ "] does not messageReceive object");
			}
			MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());	
			if(messageCreate == null){
				throw new MessageException(error_object_null, "The specified messageCreate by messageCreate [" + ObjectUtils.toString(messageSend.getMessageId(), null)
						+ "] does not messageCreate object");
			}
			return messageCreate.getMessageType();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace(System.err);
			}
			throw new MessageException(e);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService#messageInvitationProjectProcessing(int, long)
	 */
	@Override
	public String messageInvitationProjectProcessing(int status,long messageReceiveId) throws Exception {
		MessageReceive messageReceive = messageReceiveService.getEntityById(messageReceiveId);
		if(messageReceive == null)
			return "2";
		MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
		if(messageSend == null)
			return "2";
		MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());	
		if(messageCreate == null)
			return "2";
		Publish publish = publishService.getPublishByStatusAndProject(ProjectStatus.Project_Publish_Success.getKey(), messageCreate.getProjectId());//查询发布的项目
		if (publish == null || publish.getStatus() == 2){//项目不存在或已被删除 
			logger.info("According to project ID no query to project object");
			messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
			return "4";
		}
		if(publish.getStatus() != ProjectStatus.Project_Publish_Success.getKey()){//项目已过期
			logger.info("publish status is not 1..."+publish.getStatus());
			messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
			return "6";
		}
		if(status == 1){//同意创建人发送的项目承接邀请
			//同意消息加入项目申请表
			try {
				Apply apply = applyService.create(messageReceive.getUserId(), messageCreate.getOrganizationId(), new Timestamp(System.currentTimeMillis()), messageCreate.getProjectId(), messageSend.getUserId());
				if(apply == null){
					logger.debug("Agreed to invite the project, to join the application data failed");
					return "3";
				}
			} catch (Exception e) {
				logger.info(e.getMessage(),e);
				return "3";
			}
			
		}else{
			if(!tongRenSendMessageService.sendIsAgreeInvitationProjectMes(messageReceive.getUserId(), messageSend.getUserId(), messageCreate.getOrganizationId(), messageCreate.getProjectId(), status))
				logger.warn("sendIsAgreeInvitationProjectMes is error");
		}
		messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
		return "5";//删除操作消息
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenMessageProcessingService#messageExtensioProject(int, long)
	 */
	@Override
	public boolean messageExtensioProject(int status, long messageReceiveId)
			throws Exception {
		MessageReceive messageReceive = messageReceiveService.getEntityById(messageReceiveId);
		if(messageReceive == null)
			return false;
		MessageSend messageSend = messageSendService.getEntityById(messageReceive.getMessageId());
		if(messageSend == null)
			return false;
		MessageCreate messageCreate = messageCreateService.getEntityById(messageSend.getMessageId());	
		if(messageCreate == null)
			return false;
//		Publish publish = publishService.getPublishByStatusAndProject(ProjectStatus.Project_Publish_Inprogress.getKey(), messageCreate.getProjectId());//查询发布的项目
//		if (publish == null){
//			logger.error("According to project ID no query to project object");
//			return false;
//		}
		if(status == 1){//同意创建人发送的项目承接邀请
			if(!undertakenService.extensionProject(messageCreate.getProjectId(), messageCreate.getCycle())){
				logger.error("Delayed project failure,projectId is 【"+messageCreate.getProjectId()+"】");
				return false;
			}
		}
		if(!tongRenSendMessageService.sendIsAgreeExtensioProjectMes(messageReceive.getUserId(), messageSend.getUserId(), messageSend.getOrganizationId(), messageCreate.getProjectId(), status, messageCreate.getCycle()))
			logger.warn("sendIsAgreeExtensioProjectMes is error");
		return messageReceiveService.deleteEntityById(messageReceiveId);//删除操作消息
	}
}
