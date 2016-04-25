package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.MessageType;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationMemberVO;
import com.ginkgocap.tongren.organization.message.model.MessageReceive;
import com.ginkgocap.tongren.organization.message.model.MessageSend;
import com.ginkgocap.tongren.organization.message.service.MessageReceiveService;
import com.ginkgocap.tongren.organization.message.service.MessageService;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 组织成员实现类
 * 
 * @author yanweiqi
 */
@Service("organizationMemberService")
public class OrganizationMemberServiceImpl extends AbstractCommonService<OrganizationMember> implements OrganizationMemberService {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationMemberServiceImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private TongRenOrganizationService tongRenOrganizationService;
	@Autowired
	private OrganizationRoleService organizationRoleService;
	@Autowired
	private OrganizationDepMemberService organizationDepMemberService;
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;
	@Autowired
	private OrganizationDepService organizationDepService;
	@Autowired
	private FileIndexService fileIndexService;
	@Autowired
	private MessageReceiveService messageReceiveService;
	@Autowired
	private MessageService messageService;
	

	@Override
	public OrganizationMember getMemberByOrganizationIdAndUserId(long organizationId, long userId) {
		OrganizationMember member = null;
		Long id = null;
		id = getMappingByParams("OrganizationMember_Map_useridAndOrganid", new Object[] { organizationId, userId });
		if (id != null) {
			member = getEntityById(id);
		}
		return member;
	}

	@Override
	public List<OrganizationMember> getMyOrganizationMembers(long userId) {
		List<OrganizationMember> organizationMembers = null;
		List<Long> ids = getKeysByParams("OrganizationMember_List_userId", userId);
		if(null != ids){
		   organizationMembers = getEntityByIds(ids);
		}
		return organizationMembers;
	}

	@Override
	public List<OrganizationMember> getOrganizationMember(long organizationId, int status) {
		List<OrganizationMember> lst = new ArrayList<OrganizationMember>();
		List<Long> ids = getKeysByParams("OrganizationMember_List_organizationId_status", new Object[] { organizationId, status });
		if (ids != null&&ids.size()>0) {
			lst = getEntityByIds(ids);
		}
		return lst;
	}

	@Override
	public List<OrganizationMember> getNormalMember(long organizationId) {
		List<OrganizationMember> lst = new ArrayList<OrganizationMember>();
		List<Long> ids = getKeysByParams("OrganizationMember_List_organizationId", organizationId);
		if (ids != null) {
			for (Long id : ids) {
				OrganizationMember om = getEntityById(id);
				if (om.getStatus() == 1 || om.getStatus() == 2) {//1 组织创建者主动要求时的状态，2 组织创建者没有主动要求，成员申请时状态为3   正式成员 4 申请退出的
					continue;
				}
				lst.add(om);
			}
		}
		return lst;
	}

	@Override
	public OrganizationMember addCreater(long userId, long organizationId) throws Exception {
		OrganizationMember p_om = null;
		try {
			OrganizationMember check_om = getMemberByOrganizationIdAndUserId(organizationId,userId);
			if(null == check_om){
				OrganizationMember om = new OrganizationMember();
				Timestamp t = new Timestamp(System.currentTimeMillis());
				om.setCreateTime(t);
				om.setApplyTime(t);
				om.setUpdateTime(t);
				om.setUserId(userId);
				om.setOrganizationId(organizationId);
				om.setStatus(3);// 默认申请中
				om.setCreaterId(userId);
				p_om = save(om);
				User user = userService.selectByPrimaryKey(userId);
				p_om.setUser(user);
			}
			else{
				logger.warn("userId:"+userId+","+"organizationId:"+organizationId+","+ApiCodes.OrganizationCreaterIsExist.getDescription());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return p_om;
	}

	@Override
	public List<OrganizationMemberVO> getOrganizationAllMemberInfo(long organizationId, int type) {

		List<OrganizationMemberVO> result = new ArrayList<OrganizationMemberVO>();
		List<OrganizationMember> memberList = new ArrayList<OrganizationMember>();
		try {
			// 查询组织所有成员 包含申请退出
			if (type == 1) {
				memberList = getNormalMember(organizationId);
			} else {
				memberList = getOrganizationMember(organizationId, type);// 主动申请加入
			} 

			if (memberList != null && !memberList.isEmpty()) {
				for (OrganizationMember member : memberList) {
					User u = userService.selectByPrimaryKey(member.getUserId());
					if (u != null) {

						OrganizationMemberVO vo = new OrganizationMemberVO();
						//List<OrganizationRole> roles = organizationRoleService.getMyOrganizationRole(u.getId(), organizationId);
						OrganizationMemberRole omr=organizationMemberRoleService.getMemberRoleByOrganizationIdAndMemerId(organizationId, member.getId(), 3);
						OrganizationDepMember odm = organizationDepMemberService.getDepMemberByOrganizationIdAndMemberId(organizationId, member.getId());

						if (omr != null) {// 组织成员所属角色
							OrganizationRole orole=organizationRoleService.getEntityById(omr.getRoleId());
							String roleName = StringUtils.isNotEmpty(orole.getDescription())?orole.getDescription():orole.getRoleName();
							vo.setRoleName(roleName);
							vo.setRoleId(orole.getId());
							vo.setRoleStr(orole.getRoleName());
							vo.setAddTime(member.getApplyTime());
						}
						if (odm != null) {// 组织成员所属部门
							vo.setDepId(odm.getDepId());
						}
						vo.setStatus(member.getStatus());
						vo.setUserName(u.getName());
						vo.setCreateTime(member.getCreateTime());
						vo.setUserId(u.getId());
						if(StringUtils.isEmpty(u.getPicPath())){
							vo.setLogo(FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC);
						}else{
							vo.setLogo(FileInstance.FTP_WEB + FileInstance.FTP_URL+ u.getPicPath());
						}
						
						vo.setMemberId(member.getId());
						result.add(vo);
					}
				}
				//先按角色 ，后按 创建时间 
				Collections.sort(result, new Comparator<OrganizationMemberVO>() {
					@Override
					public int compare(OrganizationMemberVO o1, OrganizationMemberVO o2) {
						int w1=organizationRoleService.getRoleWeihtByName(o1.getRoleStr());
						int w2=organizationRoleService.getRoleWeihtByName(o2.getRoleStr());
						int tmp=0;
						if(w1!=w2){
							tmp= w1-w2;
						}else{
							tmp= o1.getAddTime().compareTo(o2.getAddTime());
						}
						return -tmp;
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> invite(long orgId, Map<Integer, String[]> maps, String content) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrganizationMember> memberList = new ArrayList<OrganizationMember>();
		List<String> list = new ArrayList<String>();
		Organization o = null;
		try {
			o = tongRenOrganizationService.getEntityById(orgId);
			// 创建组织成员
			if (o != null && o.getStatus() != 1) {
				for (Map.Entry<Integer, String[]> entry : maps.entrySet()) {
					for (String id : entry.getValue()) {
						addMember(Long.valueOf(id), orgId, o.getCreaterId(), entry.getKey(),MessageType.INVITATION.getType()); 
					}
				}
			} else {
				map.put("error", "组织不存在");
			}
			if (list.isEmpty()) {
				map.put("sucess", "操作成功");
			} else {
				map.put("success", list);
			}
		} catch (Exception e) {
			if (memberList != null && !memberList.isEmpty()) {
				for (OrganizationMember memberl : memberList) {
					deleteEntityById(memberl.getId());
				}
			}
			logger.error(e.getMessage() + "邀请组织成员失败", e);
			throw e;
		}
		return map;
	}

	@Override
	public OrganizationMember addMember(long userId, long organizationId, long createrId, int joinStatus,int joinType) throws Exception{
		OrganizationMember p_om = null;
		try {
			OrganizationMember check_om = getMemberByOrganizationIdAndUserId(organizationId, userId);
			if(null == check_om){
				OrganizationMember member = new OrganizationMember();
				member.setUserId(userId);
				member.setOrganizationId(organizationId);
				Timestamp t = new Timestamp(System.currentTimeMillis());
				member.setCreateTime(t);
				member.setUpdateTime(t);
				member.setCreaterId(createrId);
				member.setJoinWay(joinStatus); // 组织成员推荐类型1我的好友、2搜索、3系统推荐
				member.setStatus(joinType);// 加入类型 1:邀请加入 2：申请加入 3：正式成员 
				p_om = save(member);
				User user = userService.selectByPrimaryKey(userId);
				p_om.setUser(user);
				
				sendOrganizationMessage(createrId, userId, organizationId, joinType);
			}
			else {
				logger.warn("userId:"+userId+","+"organizationId:"+organizationId+","+ApiCodes.OrganizationMemberIsExist.getDescription());
				//修改组织邀请发送消息 时间
				logger.info("check_om.getStatus()--> "+check_om.getStatus()+",joinType--> "+joinType);
				if(joinType==2&&check_om.getStatus()==1){//申请时如果之前邀请过，状态变为已申请状态
					logger.info("update Status()--> "+joinType);
					check_om.setStatus(joinType); 
					update(check_om);
				}
				if(check_om.getStatus() !=3 || check_om.getStatus() !=4){
					List<MessageSend> messageList = messageService.getMessageByOrgUidStatus(userId, organizationId, joinType);
					if(CollectionUtils.isEmpty(messageList)){
						//重新创建消息
						sendOrganizationMessage(createrId, userId, organizationId, joinType);
						return check_om;
					}
					messageReceiveService.updateReceiveTime(userId, organizationId,joinType);
					return check_om;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return p_om;
	}

	@Override
	public String exit(long orgId, long userId) throws Exception {

		boolean result = false;
		OrganizationMember member = null;
		logger.info("orgId-->" + orgId, "userId" + userId);

		member = getMemberByOrganizationIdAndUserId(orgId, userId);
		if (member != null) {
			if (member.getStatus() == 4) {
				//如果已经申请退出过 更新消息时间
				messageReceiveService.updateReceiveTime(userId, orgId,  MessageType.SIGNOUT.getType());
				return "1";
			}
			member.setStatus(4);
			result = update(member);
			if (result) {
				Organization o = tongRenOrganizationService.getEntityById(orgId);
				try {
					tongRenSendMessageService.sendOrganizationSignOut(userId, o.getCreaterId(), orgId);
				} catch (Exception e) {
					logger.error("sendmessage exit org error", e.getMessage());
				}
				return "2";
			} else {
				return "3";
			}
		} else {
			return "4";
		}
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService
	 * #operateOrganizationMember(int, long, long)
	 */
	@Override
	public boolean operateOrganizationMember(int statusMessage, long userId, long organizationId) throws Exception {
		long orgMemberIds = getMappingByParams("OrganizationMember_Map_useridAndOrganid", new Object[] { organizationId, userId });
		if (orgMemberIds == 0)
			return false;
		OrganizationMember organizationMember = getEntityById(orgMemberIds);
		if (organizationMember == null)
			return false;
		if (organizationMember.getStatus() == 1 && statusMessage == 1) {// 状态为邀请加入&&消息通过
			organizationMember.setStatus(3);
			if (!update(organizationMember))
				return false;
		} else if (organizationMember.getStatus() == 1 && statusMessage == 2) {// 状态为邀请加入&&消息拒绝
			if (!deleteEntityById(orgMemberIds))
				return false;
		} else if (organizationMember.getStatus() == 4 && statusMessage == 1) {// 状态为退出申请&&消息通过
			if (!deleteEntityById(orgMemberIds))
				return false;
		} else if (organizationMember.getStatus() == 4 && statusMessage == 2) {// 状态为退出申请&&消息拒绝
			organizationMember.setStatus(3);
			if (!update(organizationMember))
				return false;
		} else if (organizationMember.getStatus() == 2 && statusMessage == 1) {// 状态为申请加入&&消息通过
			organizationMember.setStatus(3);
			if (!update(organizationMember))
				return false;
		} else if (organizationMember.getStatus() == 2 && statusMessage == 2) {// 状态为申请加入&&消息拒绝
			if (!deleteEntityById(orgMemberIds))
				return false;
		} else {
			return false;
		}
		return true;
	}

	@Override
	public boolean delMember(long organizationId, long userId) throws Exception {
		boolean is_Success = false;
		try {
			Organization o = tongRenOrganizationService.getEntityById(organizationId);
			is_Success = delMemberAndRoleRelation(organizationId, userId);
			if (is_Success) {
				tongRenSendMessageService.sendKickMemberMes(o.getCreaterId(), userId, organizationId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return is_Success;
	}

	@Override
	public Map<String, Object> doWork() {
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		return null;
	}

	@Override
	public String doError() {
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		return null;
	}

	@Override
	protected Class<OrganizationMember> getEntity() {
		return OrganizationMember.class;
	}

	@Override
	public void setParameterMap(Map<String, Object> parameterMap) {

	}

	@Override
	public boolean delMemberAndRoleRelation(long organizationId, long userId) throws Exception {
		boolean del_organizationMember_status = false;
		boolean del_organizationDepMember_status = false;
		boolean del_organizationMemberRole_status = false;
		boolean is_Success = false;
		try {
			OrganizationMember member = getMemberByOrganizationIdAndUserId(organizationId, userId);
			if (member != null) {
				// 查询用户所在部门，并删除用户
				OrganizationDepMember depMember = organizationDepMemberService.getDepMemberByOrganizationIdAndMemberId(organizationId, member.getId());
				if (null != depMember) {
					del_organizationDepMember_status = organizationDepMemberService.delDepMemberByMemberId(organizationId, depMember.getDepId(), member.getId());
				} else {
					logger.info(ApiCodes.UserIsNotExistInDep.getCode() + ":" + ApiCodes.UserIsNotExistInDep.getMessage());
					del_organizationDepMember_status = true;
				}
				// 查询用户在组织中组织成员角色，删除组织成员角色
				OrganizationMemberRole memberRole = organizationMemberRoleService.getMemberRoleByOrganizationIdAndMemerId(organizationId, member.getId());
				if (null != memberRole) {
					del_organizationMemberRole_status = organizationMemberRoleService.deleteEntityById(memberRole.getId());
				} else {
					logger.info(ApiCodes.UserIsNotExistInMemberRole.getCode() + ":" + ApiCodes.UserIsNotExistInMemberRole.getMessage());
					del_organizationMemberRole_status = true;
				}
				del_organizationMember_status = deleteEntityById(member.getId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			is_Success = del_organizationMember_status && del_organizationDepMember_status && del_organizationMemberRole_status;
		}

		return is_Success;
	}

	@Override
	public OrganizationMember getMyOrganizationMemberDetail(long organizationId, long userId) throws Exception {
		OrganizationMember p_om = null;
		try {
			OrganizationMember  tmp_om = getMemberByOrganizationIdAndUserId(organizationId, userId);
			if (null != tmp_om) {
				OrganizationDepMember odm = organizationDepMemberService.getDepMemberByOrganizationIdAndMemberId(organizationId, tmp_om.getId());
				Organization o = tongRenOrganizationService.getOrganizationDetailById(organizationId);
				tmp_om.setOrganization(o);
				if (null != odm) {
					OrganizationDep od = organizationDepService.getEntityById(odm.getDepId());
					tmp_om.setOrganizationDep(od);
				}
				OrganizationMemberRole omr = organizationMemberRoleService.getMemberRoleByOrganizationIdAndMemerId(organizationId, tmp_om.getId());
				if (null != omr) {
					OrganizationRole or = organizationRoleService.getEntityById(omr.getRoleId());
					tmp_om.setOrganizationRole(or);
				}
				p_om = tmp_om;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return p_om;
	}
	
	@Override
	public OrganizationMember getOrganizationMemberDetailById(long id) throws Exception {
		OrganizationMember p_om = null;
		try {
			OrganizationMember  tmp_om = getEntityById(id);
			if (null != tmp_om) {
				OrganizationDepMember odm = organizationDepMemberService.getDepMemberByOrganizationIdAndMemberId(tmp_om.getOrganizationId(), tmp_om.getId());
				Organization o = tongRenOrganizationService.getOrganizationDetailById(tmp_om.getOrganizationId());
				tmp_om.setOrganization(o);
				if (null != odm) {
					OrganizationDep od = organizationDepService.getEntityById(odm.getDepId());
					tmp_om.setOrganizationDep(od);
				}
				OrganizationMemberRole omr = organizationMemberRoleService.getMemberRoleByOrganizationIdAndMemerId(tmp_om.getOrganizationId(), tmp_om.getId());
				if (null != omr) {
					OrganizationRole or = organizationRoleService.getEntityById(omr.getRoleId());
					tmp_om.setOrganizationRole(or);
				}
				User user = userService.selectByPrimaryKey(tmp_om.getUserId());
				if(null != user){
					String userPic = user.getPicPath();
					if(StringUtils.isBlank(userPic)){
						userPic = CommonConstants.DEFOULT_USER_PIC;
					}
					userPic = FileInstance.FTP_WEB + FileInstance.FTP_URL + userPic;
					user.setPicPath(userPic);
					tmp_om.setUser(user);
				}
				p_om = tmp_om;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return p_om;
	}

	@Override
	public List<OrganizationMemberVO> getOrganizationMemberAll(long oid) throws Exception {
		
		List<OrganizationMember> lst = new ArrayList<OrganizationMember>();
		List<OrganizationMemberVO> result = new ArrayList<OrganizationMemberVO>();
		List<Long> ids = getKeysByParams("OrganizationMember_List_organizationId_notStatus", oid);
		if (ids != null) {
			for (Long id : ids) {
				OrganizationMember om = getEntityById(id);
				lst.add(om);
			}
		}
	try{	
		if (lst != null && !lst.isEmpty()) {
			for (OrganizationMember member : lst) {
				
				OrganizationMemberVO vo = new OrganizationMemberVO();
				User u = userService.selectByPrimaryKey(member.getUserId());
				List<OrganizationRole> roles = organizationRoleService.getMyOrganizationRole(u.getId(), oid);
				OrganizationDepMember odm = organizationDepMemberService.getDepMemberByOrganizationIdAndMemberId(oid, member.getId());
				
				//查询待处理的组织成员返回消息id
				if(member.getStatus() == 2 || member.getStatus() == 4){
					List<MessageSend> messageList = messageService.getMessageByOrgUidStatus(member.getUserId(), oid, member.getStatus());
					if(CollectionUtils.isNotEmpty(messageList)){
						MessageReceive messageReceive = messageList.get(0).getMessageReceive();
						vo.setMessageReceiveId(messageReceive.getId());
					}
				}
					if (roles != null && !roles.isEmpty()) {// 组织成员所属角色
						for(OrganizationRole orole:roles){
							String memberRoleName=orole.getDescription();
							String roleName = memberRoleName==null ||memberRoleName.equals("") ? orole.getRoleName():memberRoleName;
							if(orole.getRoleName().equals("ADMIN")){
								vo.addExtend("adminRole", roleName);
								vo.addExtend("adminRoleId", orole.getId());
							}else if(orole.getRoleName().equals("CREATER")){
								vo.addExtend("createrRole", roleName);
								vo.addExtend("createrRoleId", orole.getId());
							}else{
								vo.setRoleName(roleName);
								vo.setRoleId(orole.getId());
							}
							vo.setAddTime(member.getApplyTime());
						}
					}
					if (odm != null) {// 组织成员所属部门
						vo.setDepId(odm.getDepId());
						OrganizationDep odep = organizationDepService.getEntityById(odm.getDepId());
						if(odep != null ){vo.setDepName(odep.getName());}
					}
						vo.setStatus(member.getStatus());
						vo.setUserName(u.getName());
						vo.setCreateTime(member.getCreateTime());
						vo.setUserId(u.getId());
					if(StringUtils.isEmpty(u.getPicPath())){
						vo.setLogo(FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC);
					}else{
						vo.setLogo(FileInstance.FTP_WEB + FileInstance.FTP_URL+ u.getPicPath());
					}
					vo.setMemberId(member.getId());
					result.add(vo);
			}
		}
	}catch(Exception e){
				logger.info("get OrganizationMemberall is error");
				e.printStackTrace();
	}
					return result;
	}
	private void sendOrganizationMessage(long createrId,long userId,long organizationId,int joinType){
		
		try{
			if(joinType == MessageType.INVITATION.getType()){ //发送组织邀请消息
				tongRenSendMessageService.sendOrganizationInvitation(createrId, userId, organizationId);
			}
			if(joinType == MessageType.APPLICATION.getType()){//申请加入组织消息
				tongRenSendMessageService.sendOrganizationApplication(userId,createrId, organizationId);
			}
		}catch(Exception e){
			logger.info("sendOrganizationMessage is error ...");
			e.printStackTrace();
		}
	}
}
