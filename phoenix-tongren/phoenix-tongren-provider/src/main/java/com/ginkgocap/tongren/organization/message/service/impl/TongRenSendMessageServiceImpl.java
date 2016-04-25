/*
 * 文件名： SendMessageServiceImpl.java
 * 创建日期： 2015年11月10日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.message.service.impl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;


 /**
 * 消息发送实现 
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月10日
 */
@Service("tongRenSendMessageService")
public class TongRenSendMessageServiceImpl implements TongRenSendMessageService {

	private Logger logger = LoggerFactory.getLogger(TongRenSendMessageServiceImpl.class);
	
	@Autowired
	private UserService userService;//用户接口
	@Autowired
	private MessageService messageService;//消息接口
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;//组织接口
	@Autowired
	private OrganizationMemberService organizationMemberService;//组织成员service
	@Autowired
	private ProjectService projectService;//项目接口
	@Autowired
	private PublishService publishService;//发布项目接口
	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendOrganizationInvitation(int, int, int)
	 */
	@Override
	public boolean sendOrganizationInvitation(long sendUID, long receiveUID,
			long organizationId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		if(user == null && organization == null){
			logger.error("sendOrganizationInvitation method is reported to be wrong,the reason is the user or organization is NULL ");
			return false;
		}
		String tc = "\"" + user.getName() +"\"" + CommonConstants.ORGANIZATION_INVITATION + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
		
		return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.INVITATION.getType(),0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendOrganizationApplication(int, int, int)
	 */
	@Override
	public boolean sendOrganizationApplication(long sendUID, long receiveUID,
			long organizationId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		if(user == null && organization == null){
			logger.error("sendOrganizationApplication method is reported to be wrong,the reason is the user or organization is NULL ");
			return false;
		}
		String tc = "\"" + user.getName() +"\"" + CommonConstants.ORGANIZATION_APPLICATION + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
		
		return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.APPLICATION.getType(),0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendOrganizationSignOut(int, int, int)
	 */
	@Override
	public boolean sendOrganizationSignOut(long sendUID, long receiveUID,
			long organizationId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		if(user == null && organization == null){
			logger.error("sendOrganizationSignOut method is reported to be wrong,the reason is the user or organization is NULL ");
			return false;
		}
		String tc = "\"" + user.getName() +"\"" + CommonConstants.SIGN_OUT + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
		
		return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.SIGNOUT.getType(),0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendAgreeORDisagreeToJoin(int, int, int, int, int)
	 */
	@Override
	public boolean sendAgreeORDisagreeToJoin(long sendUID, long receiveUID,
			long organizationId, int status, int type) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		if(user == null && organization == null){
			logger.error("sendAgreeORDisagreeToJoin method is reported to be wrong,the reason is the user or organization is NULL ");
			return false;
		}
		if(status == 1){//同意
			if(type == 4){
				
				String tc = "\"" + user.getName() +"\"" + CommonConstants.JOIN_IN + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
				
				return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.AGREEJIONIN.getType(),0);
			}
			if(type == 6){
				
				String tc = "\"" + user.getName() +"\"" + CommonConstants.SIGN_OUT + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
				
				return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.AGREESIGNOUT.getType(),0);
			}
		}
		if(status == 2){//拒绝
			if(type == 5){
				
				String tc = "\"" + user.getName() +"\"" + CommonConstants.REFUSE_JOIN_IN + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
				
				return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.REFUSEAGREEJOININ.getType(),0);
			}
			if(type == 7){
				
				String tc = "\"" + user.getName() +"\"" + CommonConstants.REFUSE_SIGN_OUT + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
				
				return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.REFUSEAGREESIGNOUT.getType(),0);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendDissolutionORKick(int, int, int, int)
	 */
	@Override
	public boolean sendDissolutionORKick(long sendUID,
			long organizationId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(organizationId);
		if(user == null && organization == null && orgMemberList == null){
			logger.error("sendDissolutionORKick method is reported to be wrong,the reason is the user or organization or orgMemberList is NULL ");
			return false;
		}
			String tc = "\"" + user.getName() +"\"" + CommonConstants.DISSOLUTION + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION;//拼装发送标题和内容
			for (OrganizationMember organizationMember : orgMemberList) {
				try {
					messageService.sendMessage(tc, tc, sendUID, organizationId, 0, organizationMember.getUserId(), organizationId, 0, MessageType.DISSOLUTION.getType(),0);
				} catch (Exception e) {
					logger.error("sendDissolutionORKick method sendMessage to send a message to the wrong",e.getMessage());
				}
				
			}
			return true;
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService#sendKickMemberMes(long, long, long)
	 */
	@Override
	public boolean sendKickMemberMes(long sendUID, long receiveUID,
			long organizationId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		if(user == null && organization == null){
			logger.error("sendDissolutionORKick method is reported to be wrong,the reason is the user or organization is NULL ");
			return false;
		}
		String tc =  CommonConstants.YOU_HAVE_BEEN + "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION + CommonConstants.DEL;//拼装发送标题和内容
		return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.KICKED.getType(),0);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendOrganizationalTasksMes(int, int, int, int)
	 */
	@Override
	public boolean sendOrganizationalTasksMes(long sendUID, long receiveUID,
			long organizationId, int type,String taskName) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		Organization organization =  tongRenOrganizationService.getOrganizationById(organizationId);
		if(user == null && organization == null){
			logger.error("sendOrganizationalTasksMes method is reported to be wrong,the reason is the user or organization is NULL ");
			return false;
		}
		if(type == 10){
			
			String tc = "\"" + organization.getName()  +"\"" + CommonConstants.ORGANIZATION_MEMBER + "\"" + user.getName() +"\"" + CommonConstants.TO_YOU_ASSIGNMENT_TASK + "\"" + taskName + "\"" + CommonConstants.TASK;//拼装发送标题和内容
			
			return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.ASSIGNMENT_TASK.getType(),0);
		}
		if(type == 11){
			
			String tc = "\"" + organization.getName()  +"\"" + CommonConstants.ORGANIZATION_MEMBER + "\"" + user.getName() +"\"" + CommonConstants.TO_YOU_RETURN_TASK + "\"" + taskName + "\"" + CommonConstants.TASK;//拼装发送标题和内容
			
			return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.RETURN_TASK.getType(),0);
		}
		if(type == 12){
			
			String tc = "\"" + organization.getName()  +"\"" + CommonConstants.ORGANIZATION_MEMBER + "\"" + user.getName() +"\"" + CommonConstants.TO_YOU_REPEAT_TASK + "\"" + taskName + "\"" + CommonConstants.TASK;//拼装发送标题和内容
			
			return messageService.sendMessage(tc, tc, sendUID, organizationId, 0, receiveUID, organizationId, 0, MessageType.REPEAT_TASK.getType(),0);
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendProjectMes(int, int, java.lang.String, int)
	 */
	@Override
	public boolean sendProjectMes(long sendUID, long organizationId,
			String projectName, int type,long projectId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(organizationId);
		if(type == 13){
			String tc = "\"" + user.getName() +"\"" + CommonConstants.GIVE_UP + "\"" + projectName +"\"" + CommonConstants.PROJECT;//拼装发送标题和内容
			for (OrganizationMember organizationMember : orgMemberList) {
				try {
					messageService.sendMessage(tc, tc, sendUID, organizationId, 0, organizationMember.getUserId(), organizationId, 0, MessageType.GIVE_UP_PROJECT.getType(),projectId);
				} catch (Exception e) {
					logger.error("sendProjectMes method sendMessage to send a message to the wrong",e.getMessage());
				}
			}
			return true;
		}
		if(type == 14){
			String tc = "\"" + user.getName() +"\"" + CommonConstants.END + "\"" + projectName +"\"" + CommonConstants.PROJECT;//拼装发送标题和内容
			for (OrganizationMember organizationMember : orgMemberList) {
				try {
					messageService.sendMessage(tc, tc, sendUID, organizationId, 0, organizationMember.getUserId(), organizationId, 0, MessageType.END_PROJECT.getType(),projectId);
				} catch (Exception e) {
					logger.error("sendProjectMes method sendMessage to send a message to the wrong",e.getMessage());
				}
			}
			return true;
		}
		return false;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.SendMessageService#sendSubDocument(int, int, java.lang.String)
	 */
	@Override
	public boolean sendSubDocumentMes(long sendUID, long organizationId,
			String fileName,long projectId) throws Exception {
		User user = userService.selectByPrimaryKey(sendUID);
		List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(organizationId);
		Project project = projectService.getEntityById(projectId);
		String projectName = "";
		String tc = "";
		if(project == null){
			Organization organization = tongRenOrganizationService.getOrganizationById(organizationId);
			if(organization != null){
				tc = "\"" + organization.getName() +"\"" + CommonConstants.ORGANIZATION_MEMBER + user.getName() + CommonConstants.SUBMITING + "\"" + fileName +"\"" + CommonConstants.DOCUMENT;//拼装发送标题和内容
			}else{
				logger.info("organization is null,organizationId is 【"+organizationId+"】");
				tc = "\"" + "某某某" +"\"" + CommonConstants.ORGANIZATION_MEMBER + user.getName() + CommonConstants.SUBMITING + "\"" + fileName +"\"" + CommonConstants.DOCUMENT;//拼装发送标题和内容
			}
			for (OrganizationMember organizationMember : orgMemberList) {
				try {
					messageService.sendMessage(tc, tc, sendUID, organizationId, 0, organizationMember.getUserId(), organizationId, 0, MessageType.ORGANIZATION_DOCUMENT.getType(),projectId);
				} catch (Exception e) {
					logger.error("sendSubDocument method sendMessage to send a message to the wrong",e.getMessage());
				}
			}
		}else{
			projectName = project.getName();
			tc = "\"" + projectName +"\"" + CommonConstants.PROJECT_MEMBER + user.getName() + CommonConstants.SUBMITING + "\"" + fileName +"\"" + CommonConstants.DOCUMENT;//拼装发送标题和内容
			for (OrganizationMember organizationMember : orgMemberList) {
				try {
					messageService.sendMessage(tc, tc, sendUID, organizationId, 0, organizationMember.getUserId(), organizationId, 0, MessageType.PROJECT_DOCUMENT.getType(),projectId);
				} catch (Exception e) {
					logger.error("sendSubDocument method sendMessage to send a message to the wrong",e.getMessage());
				}
			}
		}
		
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService#sendAssignmentSubTaskMes(int, int, int, int)
	 */
	@Override
	public boolean sendAssignmentSubTaskMes(long sendUID, long receiveUID,
			long organizationId,long projectId,String taskName) throws Exception {
		Project project = projectService.getEntityById(projectId);
		User user = userService.selectByPrimaryKey(sendUID);
		String tc = "\"" + project.getName() + "\"" + CommonConstants.PROJECT_MEMBER + "\"" + user.getName() + "\"" + CommonConstants.TO_YOU_ASSIGNMENT_TASK +  "\"" + taskName + "\"" + CommonConstants.SUB_TASK;
		return messageService.sendMessage(tc, tc, sendUID, organizationId, 0,receiveUID, organizationId, 0, MessageType.ASSIGNMENT_SUB_TASK.getType(),projectId);
	}

	@Override
	public boolean sendOrganizationToUndertakeProjects(long sendUID,
			long receiveUID, long organizationId, long projectId) throws Exception {
		Project project = projectService.getEntityById(projectId);
		User user = userService.selectByPrimaryKey(sendUID);
		if(user == null && project == null){
			logger.error("sendOrganizationToUndertakeProjects method is reported to be wrong,the reason is the user or project is NULL ");
			return false;
		}
		String tc = "\"" + user.getName() + "\"" +"组织/个人想要承接您的" + "\"" + project.getName() + "\"" + "项目";
		return messageService.sendMessage(tc, tc, sendUID, organizationId, 0,receiveUID, organizationId, 0, MessageType.ORGANIZATION_TO_UNDERTAKE_PROJECTS.getType(),projectId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService#sendInvitationProjectMes(long, long, long, long)
	 */
	@Override
	public boolean sendInvitationProjectMes(long sendUID, List<Long> receiveUID,
			List<Long> organizationId, long projectId,String content) throws Exception {
		Project project = projectService.getEntityById(projectId);
		User user = userService.selectByPrimaryKey(sendUID);
		if(user == null && project == null){
			logger.error("sendInvitationProjectMes method is reported to be wrong,the reason is the user or project is NULL ");
			return false;
		}
		for(int i =0;i<receiveUID.size();i++){
			try{
				long orgId  = organizationId.get(i);
				String tc = "\"" + user.getName() + "\"" +"邀请您承接" + "\"" + project.getName() + "\"" + "项目";
				if(orgId != 0){
					Organization  o = tongRenOrganizationService.getEntityById(orgId);
					tc = "\"" + user.getName() + "\"" +"邀请您的"+o.getName()+"承接" + "\"" + project.getName() + "\"" + "项目";
					messageService.sendMessage(tc, content, sendUID, organizationId.get(i), 0,receiveUID.get(i), organizationId.get(i), 0, MessageType.INVITATION_PROJECT.getType(),projectId);
				}else{
					messageService.sendMessage(tc, content, sendUID, organizationId.get(i), 0,receiveUID.get(i), organizationId.get(i), 0, MessageType.INVITATION_PROJECT.getType(),projectId);
				}
			}catch(Exception e){
				logger.info("sendInvitationProjectMes is error...");
			}
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService#sendIsAgreeInvitationProjectMes(long, long, long, long, int)
	 */
	@Override
	public boolean sendIsAgreeInvitationProjectMes(long sendUID,long receiveUID, long organizationId, long projectId, int status)throws Exception {
		Project project = projectService.getEntityById(projectId);
		User user = userService.selectByPrimaryKey(sendUID);
		if(user == null && project == null){
			logger.error("sendInvitationProjectMes method is reported to be wrong,the reason is the user or project is NULL ");
			return false;
		}
		if(status == 1){//同意
			try {
				messageService.deleteMessageByProjectId(projectId);
			} catch (Exception e) {
				logger.error("Delete the excess invitation to undertake project information from the project ID");
			}
			
			String tc = "\"" + user.getName() + "\"" +"同意承接" + "\"" + project.getName() + "\"" + "项目";
			return messageService.sendMessage(tc, tc, sendUID, organizationId, 0,receiveUID, organizationId, 0, MessageType.AGREE_TO_UNDERTAKE_PROJECTS.getType(),projectId);
		}
		if(status == 2){//拒绝
			String tc = "\"" + user.getName() + "\"" +"拒绝承接" + "\"" + project.getName() + "\"" + "项目";
			return messageService.sendMessage(tc, tc, sendUID, organizationId, 0,receiveUID, organizationId, 0, MessageType.REFUSEAGREE_TO_UNDERTAKE_PROJECTS.getType(),projectId);
		}
		return false;
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService#sendExtensioProjectMes(long, long, long, int, java.lang.String)
	 */
	@Override
	public boolean sendExtensioProjectMes(long sendUID, long organiztionId,
			long projectId, int cycle, String content) throws Exception {
		Project project = projectService.getEntityById(projectId);
		User user = userService.selectByPrimaryKey(sendUID);
		if(user == null && project == null){
			logger.error("sendExtensioProjectMes method is reported to be wrong,the reason is the user or project is NULL ");
			return false;
		}
		String tc = "\"" + user.getName() + "\"" +"对" + "\"" + project.getName() + "\"" + "项目进行了"+"\""+cycle+"天"+"\""+"延期申请";
		return messageService.sendMessage(tc, content, sendUID, organiztionId, 0, project.getCreaterId(), 0, 0, MessageType.APPLICATION_EXTENSION_PROJECT.getType(), projectId, cycle);
	}
	/*
	 * (non-Javadoc)
	 * @see com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService#sendIsAgreeExtensioProjectMes(long, long, long, long, int)
	 */
	@Override
	public boolean sendIsAgreeExtensioProjectMes(long sendUID, long receiveUID,
			long organizationId, long projectId, int status,int cycle) throws Exception {
		Project project = projectService.getEntityById(projectId);
		User user = userService.selectByPrimaryKey(sendUID);
		if(status == 1){//同意延期
			String tc = "\"" + user.getName() + "\"" +"同意" + "\"" + project.getName() + "\"" + "项目延期" + "\"" + cycle + "\"天";
			if(organizationId == 0){//个人承接的项目情况
				return messageService.sendMessage(tc, tc, sendUID, 0, 0, receiveUID, 0, 0, MessageType.AGREE_APPLICATION_EXTENSION_PROJECT.getType(), projectId, cycle);
			}else{
				List<OrganizationMember> orgMemberList = organizationMemberService.getNormalMember(organizationId);
				for (OrganizationMember organizationMember : orgMemberList) {
					try {
						messageService.sendMessage(tc, tc, sendUID, 0, 0, organizationMember.getUserId(), organizationId, 0, MessageType.AGREE_APPLICATION_EXTENSION_PROJECT.getType(), projectId, cycle);
					} catch (Exception e) {
						logger.error("sendIsAgreeExtensioProjectMes method sendMessage to send a message to the wrong",e.getMessage());
					}
				}
				return true;
			}
		}
		if(status == 2){//拒绝
			String tc = "\"" + user.getName() + "\"" +"拒绝" + "\"" + project.getName() + "\"" + "项目延期";
			return messageService.sendMessage(tc, tc, sendUID, 0, 0, receiveUID, organizationId, 0, MessageType.REFUSEAGREE_APPLICATION_EXTENSION_PROJECT.getType(), projectId, cycle);
		}
		return false;
	}

	@Override
	public boolean sendTaskRemind(long sendUID, long receiveUID,long organizationId, long projectId,String taskName) throws Exception {
		
			String tc = "";
		try{
			User user = userService.selectByPrimaryKey(sendUID);
			tc = "\"" + user.getName() + "\"" +"提醒你" + "\"" + "尽快完成" + "\"" + taskName;
		}catch(Exception e){
			logger.info("sendTaskRemind is error...");
			e.printStackTrace();
		}
		return messageService.sendMessage(tc, tc, sendUID, 0, 0, receiveUID, organizationId, 0, MessageType.TASK_REMIND.getType(), projectId);
	}

}
