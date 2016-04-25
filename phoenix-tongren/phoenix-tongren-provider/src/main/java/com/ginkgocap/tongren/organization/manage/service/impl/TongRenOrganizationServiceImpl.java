package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.JmsMsgBean;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.util.ChineseToEnglish;
import com.ginkgocap.tongren.common.util.DaoSortType;
import com.ginkgocap.tongren.organization.application.model.OrganizationModuleApplication;
import com.ginkgocap.tongren.organization.application.service.OrganizationModuleApplicationService;
import com.ginkgocap.tongren.organization.authority.model.OrganizationRoleAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationRoleAuthorityService;
import com.ginkgocap.tongren.organization.manage.dao.OrganizationManagerDao;
import com.ginkgocap.tongren.organization.manage.model.Organization;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMemberRole;
import com.ginkgocap.tongren.organization.manage.model.OrganizationRole;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberRoleService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationRoleService;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.organization.manage.vo.OrganizationVO;
import com.ginkgocap.tongren.organization.manage.vo.RecommendVO;
import com.ginkgocap.tongren.organization.message.service.TongRenSendMessageService;
import com.ginkgocap.tongren.organization.system.model.OrganizationRoles;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 组织实现类
 * @author yanweiqi
 */
@Service("tongRenOrganizationService")
public class TongRenOrganizationServiceImpl extends AbstractCommonService<Organization>  implements TongRenOrganizationService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationRoleServiceImpl.class);
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private JmsSendService jmsSendService;
	
	@Autowired
	private ProjectTaskService projectTaskService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Autowired
	private OrganizationRoleService organizationRoleService;
	
	@Autowired
	private OrganizationDepService organizationDepService;

	@Autowired
	private OrganizationModuleApplicationService moduleApplicationService;
	
	@Autowired
	private OrganizationRoleAuthorityService organizationRoleAuthorityService;
	
	@Autowired
	private OrganizationMemberRoleService organizationMemberRoleService;	
	
	@Autowired
	private TongRenSendMessageService tongRenSendMessageService;
	
	@Autowired
	private FileIndexService fileIndexService;
	@Autowired
	private UndertakenService undertakenService;
	@Autowired
	private OrganizationManagerDao organizationManagerDao;
	
	@Override
	public List<Organization> getMyOrganizationIds(long userId) {
		
			List<Organization> list = new ArrayList<Organization>();
			List<Long> ids = getKeysByParams("OrganizationId_List_userId_status",DaoSortType.DESC,new Object[]{userId});
			if(ids != null ){
				for(Long omId : ids){
					OrganizationMember om= organizationMemberService.getEntityById(omId);
					Organization  organization = getEntityById(om.getOrganizationId());
					
					if(organization.getStatus() == 1){
						continue;
					}
					if(organization.getCreaterId() == userId){
						continue;
					}
					if(om.getStatus() == 1 || om.getStatus() == 2){
						continue;
					}
					list.add(organization);
				}
			}
		   	return list ;
	}
	
	@Override
	public List<OrganizationVO> getMyJoinOrganization(long userId) throws Exception{
		
		List<OrganizationVO> lst = new ArrayList<OrganizationVO>();
		try{
				List<Organization> list = getMyOrganizationIds(userId);
				List<String> roleName = new ArrayList<String>();
				if(list != null && list.size() >0 ){
					for(Organization o :list){
						  roleName.clear();
						  OrganizationVO vo =new OrganizationVO();
						  BeanUtils.copyProperties(vo, o);
							
						  List<OrganizationRole> roles =	organizationRoleService.getMyOrganizationRole(userId, o.getId());
						  List<OrganizationMember> userList = organizationMemberService.getNormalMember(o.getId());//组织正式成员
						  OrganizationMember  member= organizationMemberService.getMemberByOrganizationIdAndUserId(o.getId(),userId);
						  if(member != null){
							  vo.setAddTime(member.getApplyTime()); //我加入组织时间
						  }
//						  if(roles != null && CollectionUtils.isNotEmpty(roles)){
//							  vo.setRoleName(roles.get(0).getDescription());
//						  }
						  if (roles != null && !roles.isEmpty()) {// 组织成员所属角色
								for(OrganizationRole orole:roles){
									String memberRoleName=orole.getDescription();
									String rName = memberRoleName==null ||memberRoleName.equals("") ? orole.getRoleName():memberRoleName;
									if(orole.getRoleName().equals("ADMIN")){
										vo.addExtend("adminRole", rName);
										vo.addExtend("adminRoleId", orole.getId());
									}else if(orole.getRoleName().equals("CREATER")){
										vo.addExtend("createrRole", rName);
										vo.addExtend("createrRoleId", orole.getId());
									}else{
										vo.setRoleName(rName);
									}
								}
							}
						  	vo.setMemberSize(userList.size());
						  	lst.add(vo);
					}
				}
				}catch(Exception e){
					e.printStackTrace();
					logger.error("getMyJoinOrganization is Exception",e.getMessage());
		}
					return lst;
	}

	@Override
	public List<OrganizationVO> getMyCreateOrganizations(long createrId,int status) throws Exception {
		 return getMyCreateOrganizations(createrId,status,false);
	}		
	
	public List<OrganizationVO> getMyCreateOrganizations(long createrId,int status,boolean isContainAdmin) throws Exception {
		
			List<Long> ids = getKeysByParams("Organization_List_createrId_status", new Object[]{createrId,status});
			if(isContainAdmin){
				List<Long> adminList = organizationManagerDao.getUserCreateRoleOrAdminRole(createrId);
				if(CollectionUtils.isNotEmpty(adminList)){
					ids.addAll(adminList);	
				}
			}
			List<OrganizationVO> lst = new ArrayList<OrganizationVO>();
			if(ids != null && ids.size() > 0){
				List<Organization> orgList = new ArrayList<Organization>(ids.size());
				for (Long id : ids) {
					orgList.add(getEntityById(id));
				}
				Collections.sort(orgList,new Comparator<Organization>(){
					@Override
					public int compare(Organization o1, Organization o2) {
						return o2.getCreateTime().compareTo(o1.getCreateTime());
					}});
				if(orgList != null && !orgList.isEmpty()){
					for(Organization o :orgList){
						OrganizationVO vo =new OrganizationVO();
						BeanUtils.copyProperties(vo, o);
						List<OrganizationRole> roles = organizationRoleService.getMyOrganizationRole(createrId, o.getId());
//						if (roles != null && !roles.isEmpty()) {// 组织成员所属角色
//							String roleName = roles.get(0).getDescription()==null || roles.get(0).getDescription().equals("") ? roles.get(0).getRoleName():roles.get(0).getDescription();
//							vo.setRoleName(roleName);
//						}
						
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
								}
							}
						}
						List<OrganizationMember> userList = organizationMemberService.getNormalMember(o.getId());//组织正式成员
						vo.setMemberSize(userList.size());
						lst.add(vo);
					}
				}
			}
					return lst;
	}		


	@Override
	public Organization create(Organization o) throws Exception{
		StopWatch stopWatch = new StopWatch();
		Organization organization = null;
		List<OrganizationDep> organizationDeps   = null;
		List<OrganizationRole> organizationRoles = null;
		Map<String,List<OrganizationRoleAuthority>>  maps = new HashMap<String,List<OrganizationRoleAuthority>>(); 
		OrganizationMember om = null;
		OrganizationMemberRole omr = null;
		List<OrganizationModuleApplication> omas = null;
		try {	
			stopWatch.start();
			logger.info("User "+ o.getCreaterId()+" Create organization  begin start time:"+stopWatch.getStartTime()+"ms");
			// 创建组织
			organization = save(o);
			//往MQ发送创建组织消息
			JmsMsgBean jmsMsgBean=new JmsMsgBean();
			jmsMsgBean.setOpType("organization");//组织类型
			jmsMsgBean.setOperator(1);//添加
			jmsSendService.sendJmsMsg(jmsMsgBean);
			//创建组织默认部门
			 organizationDeps = organizationDepService.createDefault(organization.getCreaterId(), organization.getId());
			//创建默认角色
			organizationRoles = organizationRoleService.createDefault(organization.getCreaterId(), organization.getId());
			//创建角色默认权限
			for (OrganizationRole organizationRole : organizationRoles) {
				List<OrganizationRoleAuthority> oras  = organizationRoleAuthorityService.createDefault(organizationRole);
				maps.put(organizationRole.getRoleName(), oras);
			}
			//创建组织成员
			om = organizationMemberService.addCreater(organization.getCreaterId(), organization.getId());
			
			//添加创建者角色
			OrganizationRole organizationRole = organizationRoleService.getOrganizationRoleByOrganizationIdAndRoleName(organization.getId(), OrganizationRoles.CREATER.name());
			omr = organizationMemberRoleService.addMemberRole(om, organizationRole.getId());
			
			//创建默认组织应用模块,依赖模块的初始化
			omas = moduleApplicationService.createDefault(organization.getCreaterId(), organization.getId());
			stopWatch.stop();
			logger.info("Create organization begin use time "+stopWatch.getTime()+"ms");
			
		} 
		catch (Exception e) {
			if(null != o){
				deleteEntityById(o.getId());
				logger.info("organization id "+ o.getId() +" del......");
			}
			if(null != organizationDeps){
				for (OrganizationDep odep : organizationDeps) {
					organizationDepService.deleteEntityById(odep.getId());
					logger.info("OrganizationDep id :"+odep.getId()+" del....");
				}				
			}
            if(null != organizationRoles){
    			for (OrganizationRole or : organizationRoles) {
    				organizationRoleService.deleteEntityById(or.getId());
    				logger.info("OrganizationRole id :"+or.getId()+" del....");
    			}            	
            }
            if(!maps.isEmpty()){
    			for (Map.Entry<String, List<OrganizationRoleAuthority>> entry: maps.entrySet()) {
    				List<OrganizationRoleAuthority> oras = entry.getValue();
    				for (OrganizationRoleAuthority ora : oras) {
    					organizationRoleAuthorityService.deleteEntityById(ora.getId());
    					logger.info("OrganizationRole name :"+entry.getKey()+",OrganizationRoleAuthority id:"+ora.getAuthorityId()+" del.....");
    				}
    			}
            }

			if(null != om)  {
				organizationMemberService.deleteEntityById(om.getId());
				logger.info("organizationMember id "+om.getId()+" del....");
			}
			if(null != omr) {
				organizationMemberRoleService.deleteEntityById(omr.getId());
				logger.info("organizationMember id "+omr.getId()+" del....");
			}
            if(null != omas){
            	for (OrganizationModuleApplication oma : omas) {
            		moduleApplicationService.deleteEntityById(oma.getId());
				}
            }
			logger.error(e.getMessage(),e);
			throw e;
		}
		return organization;
	}

	@Override
	public boolean disband(long id) throws Exception {

		boolean flag = false;
		Organization o = getEntityById(id);
		try{
			if (o != null) {
				o.setStatus(1);
				o.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				flag = update(o);
				if(flag){
					//往MQ发送解散组织消息
					JmsMsgBean jmsMsgBean=new JmsMsgBean();
					jmsMsgBean.setOpType("organization");//组织类型
					jmsMsgBean.setOperator(2);//解散
					jmsMsgBean.setContent(o);
					jmsSendService.sendJmsMsg(jmsMsgBean);
					tongRenSendMessageService.sendDissolutionORKick(o.getCreaterId(), id);
				    return flag;
				}
			}
		}catch(Exception e){
			if(o != null){
				o.setStatus(0);
				update(o);
			}
			logger.error(e.getMessage(),e);
			throw e;
		}
		return flag;
	}
	@Override
	public Organization getOrganizationById(long oid) {
		
		Organization o  = new Organization();
		try{
			o = getEntityById(oid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return o;
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
	protected Class<Organization> getEntity() {
		return Organization.class;
	}

	@Override
	public Page<RecommendVO> queryOrganizationPage(Page<RecommendVO> page) throws Exception {
		
		
		List<Long> ids=null;
		List<RecommendVO> list = new ArrayList<RecommendVO>();
		
		String sql = "Organization_List_status";
		String status = page.getParam("status").toString();
		Object[] queryArayy=new Object[]{status}; 
		
		page.setTotalCount(count(sql,queryArayy));
		ids=getKeysByParams(sql, queryArayy, page.getStart(), page.getSize());
		if(ids != null && !ids.isEmpty()){
			List<Organization> orgList = getEntityByIds(ids);
			for(Organization o :orgList){
				RecommendVO vo = copyObject(o);
				list.add(vo);
			}
			page.setResult(list);
		}
		return page;
	}

	@Override
	public List<RecommendVO> getOrganizationByName(String orgName)throws Exception {
		
		List<RecommendVO> list = new ArrayList<RecommendVO>();
		
		try{
			List<Long> ids = getKeysByParams("Organization_List_Name", orgName);
			
			if(ids != null && !ids.isEmpty()){
				List<Organization> orgList = getEntityByIds(ids);
				for(Organization o :orgList){
					if(o.getStatus() == 1){
						continue;
					}
					RecommendVO vo = copyObject(o);
					list.add(vo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据名称或者拼音查询所有的组织id
	 * @param orgName
	 * @return
	 * @throws Exception
	 */
	public List<Long> queryOranizationByNameOrSpell(String orgName) throws Exception {
		for(int i=0;i<orgName.length();i++){
			if(orgName.charAt(i)=='%'){
				throw new Exception("query name is not allow contains '%' "+orgName);
			}
		}
		List<Long> ids=getKeysByParams("organization_List_Name_Spell",orgName+"%",orgName+"%",new Integer(0));
		if(ids==null){
			ids=new ArrayList<Long>();
		}
		return ids;
		//organization_List_Name_Spell
	}
	public RecommendVO copyObject(Organization o){
		RecommendVO vo = new RecommendVO();
		User u = userService.selectByPrimaryKey(o.getCreaterId());
		vo.setId(o.getId());
		vo.setName(o.getName());
		vo.setTaskId(o.getLogo());
		vo.setOrgCreateId(o.getCreaterId());
		vo.setCreateTime(o.getCreateTime());
		vo.setOrgId(o.getId());
		if(u != null){vo.setCreateName(u.getName());}//组织创建者名称
		vo.setType(0);
		return vo;
	}

	@Override
	public Organization getOrganizationDetailById(long id) throws Exception {
		Organization organization = null;
		try {
			organization = getEntityById(id);
			List<FileIndex> list = fileIndexService.selectByTaskId(organization.getLogo(), "1");
			if(list != null && list.size()>0){
				FileIndex s = (FileIndex) list.get(0);
				organization.setLogo(s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath()+ "/"+s.getFileTitle());
			}else{
				organization.setLogo(FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return organization;
	}

	@Override
	public Page<OrganizationVO> getOrganizationListByType(Page<OrganizationVO> page) throws Exception{

		String sql = "";
		int status = 0; //查询组织状态
		List<Long> ids= new ArrayList<Long>();
		String type = page.getParam("type").toString();
		List<OrganizationVO> list = new ArrayList<OrganizationVO>();
		if(type.equals("0")){//全部 按照创建时间倒序\
			sql = "Organization_List_status";
		}else{
			sql = "organization_List_PerformSize";//完成项目数 倒序
		}
	try{	
		Object[] queryArayy=new Object[]{status}; 
		page.setTotalCount(count(sql,queryArayy));
		ids=getKeysByParams(sql, queryArayy, page.getStart(), page.getSize());
		if(CollectionUtils.isNotEmpty(ids)){
			for(Long id:ids){
				Organization  o = getEntityById(id);
				if(o != null){
					OrganizationVO vo = new OrganizationVO();
					BeanUtils.copyProperties(vo, o);
					list.add(vo);
				}
			}
		}
	}catch(Exception e){
		logger.error(e.getMessage(),e);
	}
		page.setResult(list);
		return page;
	}

	/**
	 * 更新历史数据，把所有name_spelling字段为空的数据，按照name字段转换并，更新到数据库
	 */
	@Override
	public void updateAllNameSpell() throws Exception {
		String sql = "organization_List_Spell";
		List<Long> ids = getKeysByParams(sql, "");
		if (ids != null) {
			for (Long id : ids) {
				try {
					Organization org = getEntityById(id);
					if (org != null) {
						String spe=ChineseToEnglish.convertToSpell(org.getName());
						org.setNameSpelling(spe);
						update(org);
					} else {
						logger.warn("not found organization " + id);
					}
				} catch (Exception e) {
					logger.error("update Organization failed "+id);
				}
			}
		}

	}

	@Override
	public String creatorTurnOver(long organizationId , long receiveId) throws Exception {
		
		try{
			Organization organization = getEntityById(organizationId);
			if(organization == null || organization.getStatus() == 1){
				return "2";
			}
			Timestamp newTime = new Timestamp(System.currentTimeMillis());
			
			OrganizationMember receiveIdMember = organizationMemberService.getMemberByOrganizationIdAndUserId(organizationId,receiveId);
			OrganizationMember creatorMember = organizationMemberService.getMemberByOrganizationIdAndUserId(organizationId,organization.getCreaterId());
			
			OrganizationMemberRole receiveIdMemberRole = organizationMemberRoleService.getMemberRoleByOrganizationIdAndMemerId(organizationId,receiveIdMember.getId());
			OrganizationMemberRole creatorMemberRole = organizationMemberRoleService.getMemberRoleByOrganizationIdAndMemerId(organizationId,creatorMember.getId());

			//校验组织创建者角色权限
			OrganizationRole  creatorRole = organizationRoleService.getEntityById(creatorMemberRole.getRoleId());
			if(!creatorRole.getRoleName().equals("CREATER")){
				return "3";
			}
			//修改组织成员信息
			List<Long> ids = organizationMemberService.getKeysByParams("OrganizationMember_List_organizationId", organizationId);
			if(ids != null ){
				List<OrganizationMember> list = organizationMemberService.getEntityByIds(ids);
				for(OrganizationMember member :list){
					member.setCreaterId(receiveId);
					member.setUpdateTime(newTime);
					organizationMemberService.update(member);
					
				}
			}
			//丿组织角色列表
			List<OrganizationRole> creatorUserRoles = organizationRoleService.getOrganizationRoles(organizationId);
			OrganizationRole memberRole=null;
			for(OrganizationRole role :creatorUserRoles){
				//修改组织成员角色信息
				if(role.getRoleName() .equals("MEMBER")){
					memberRole=role;
				}
				//修改组织角色信息
				role.setUpdateTime(newTime);
				role.setCreaterId(receiveId);
				organizationRoleService.update(role);
			}
			
			long createRoleId=creatorMemberRole.getRoleId();//创建者角色id
			

			creatorMemberRole.setUpdateTime(newTime);
			creatorMemberRole.setRoleId(memberRole.getId());
			
			receiveIdMemberRole.setUpdateTime(newTime);
			receiveIdMemberRole.setRoleId(createRoleId);
			
			organizationMemberRoleService.update(creatorMemberRole);
			organizationMemberRoleService.update(receiveIdMemberRole);
			logger.info("creatorTurnOver oid :"+ organizationId +",createId :"+organization.getCreaterId() +",receiveId :" + receiveId);
			
			organization.setUpdateTime(newTime);
			organization.setCreaterId(receiveId);
			update(organization);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("creatorTurnOver is error...");
		}
		return "1";
	}

	@Override
	public Map<String, Object> getMenuSize(long userId,Map<String, Object> responseData) throws Exception {
		try{
			responseData.put("createProjectSize", organizationManagerDao.getUserCreateProjectSize(userId));
			responseData.put("undertakenProjectSize", organizationManagerDao.getUserUndertakenProjectSize(userId));
			responseData.put("createOrganizationSize", organizationManagerDao.getUserCreateOrganizationSize(userId));
			responseData.put("joinOrganizationSize", organizationManagerDao.getUserJoinOrganizationSize(userId));
		}catch(Exception e){
			logger.info("getMenuSize is error userId --->" + userId);
			e.printStackTrace();
		}
		return responseData;
	}
}
