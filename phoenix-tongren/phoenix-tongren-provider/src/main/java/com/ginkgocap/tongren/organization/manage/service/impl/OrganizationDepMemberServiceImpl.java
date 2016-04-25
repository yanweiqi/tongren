package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.manage.exception.DepMemberException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDepMember;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;

/**
 * @author yanweiqi
 *
 */
@Service("organizationDepMemberService")
public class OrganizationDepMemberServiceImpl extends AbstractCommonService<OrganizationDepMember> implements OrganizationDepMemberService {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationDepMemberServiceImpl.class);
	
	@Autowired
	private OrganizationDepService organizationDepService;
	
	@Autowired
	private OrganizationMemberService organizationMemberService;
	
	@Override
	public OrganizationDepMember getDepMemberByOrganizationIdAndMemberId(long organizationId, long organizationMemberId) throws Exception {
		OrganizationDepMember depMember = null;
		try {
			Long id = getMappingByParams("organizationDepMember_map_organizationId_memberId", new Object[]{organizationId,organizationMemberId});
			if (null != id) {
				depMember = getEntityById(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+","+ApiCodes.DepIsNotExist,e);
			throw e;
		}
		return depMember;
	}
	
	@Override
	public boolean delDepMemberByMemberId(long organizationId,long depId,long organizationMemberId) throws Exception {
		boolean status = false;
		try {
			 logger.info("Ready Delete OrganizationId:"+organizationId+",DepId:"+depId+",MemberId:"+organizationMemberId);
			 OrganizationDepMember del_DepMember = getDepMemberByOrganizationIdAndDepIdAndMemberId(organizationId, depId, organizationMemberId);
			 status = deleteEntityById(del_DepMember.getId());
			 logger.info("Delete OrganizationId:"+organizationId+",DepId:"+depId+",MemberId:"+organizationMemberId+",status:OK");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return status;
	}
	
	/**
	 * 根据成员ID、组织ID、部门ID删除部门成员
	 * @param organizationId       组织ID
	 * @param depId                部门ID
	 * @return boolean             
	 * @throws Exception
	 */
	public boolean delDepMemberByDepId(long organizationId,long depId) throws Exception{
		List<OrganizationDepMember> depMembers = null;
		boolean  status = false;
		try {
			depMembers = getDepMemberByDepId(organizationId, depId);
			if(null != depMembers){
			   for (OrganizationDepMember depMember : depMembers) {
				   logger.info("Ready Delete OrganizationId:"+organizationId+",DepId:"+depMember.getDepId()+",MemberId:"+depMember.getOrganizationMemberId());
				   OrganizationDepMember del_DepMember = getDepMemberByOrganizationIdAndDepIdAndMemberId(organizationId, depMember.getDepId(), depMember.getOrganizationMemberId());
				   status = deleteEntityById(del_DepMember.getId());
				   depMembers.remove(del_DepMember);
				   logger.info("Delete OrganizationId:"+organizationId+",DepId:"+depMember.getDepId()+",MemberId:"+depMember.getOrganizationMemberId()+",status:OK");
			   }
			}
		} catch (Exception e) {
			for (OrganizationDepMember depMember : depMembers) {
				logger.info("Delete OrganizationId:"+organizationId+",DepId:"+depMember.getDepId()+",MemberId:"+depMember.getOrganizationMemberId()+",status:Fail");
			}
			logger.error(e.getMessage()+","+ApiCodes.DepMemberDelFail.getMessage(),e);
			throw new DepMemberException(ApiCodes.DepMemberDelFail.getCode(), ApiCodes.DepMemberDelFail.getMessage());
		}
		return status;
	}

	@Override
	public List<OrganizationDepMember> getDepMemberByDepId(long organizationId,long depId) throws Exception {
		List<OrganizationDepMember> depMembers = null;
		try {
			Map<String, OrganizationDep> nodeMap = new HashMap<String, OrganizationDep>();  
			TreeNode<OrganizationDep> treeNode = organizationDepService.getSubTreeDepById(depId, organizationId);
			if (null != treeNode) {
				treeNode.convertTreeNodeToMap(treeNode, nodeMap);
				depMembers = new ArrayList<OrganizationDepMember>();
				for (Map.Entry<String, OrganizationDep> entry : nodeMap.entrySet()) {
					List<OrganizationDepMember> tmp_depMembers = getDepMemberByOrganizationIdAndDepId(organizationId, Long.valueOf(entry.getKey()));
					depMembers.addAll(tmp_depMembers) ;
				}
			}
			else{
				logger.info("Get Organization:"+organizationId+",depId:"+depId+",status:"+ApiCodes.DepIsNotExist.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+","+ApiCodes.DepMemberGetFail,e);
			throw e;
		}
		return depMembers;
	}
	
	@Override
	public List<OrganizationDepMember> getDepMemberByOrganizationIdAndDepId(long organizationId,long depId) throws Exception{
		List<OrganizationDepMember> depMembers = null;
		try {
			List<Long> ids = getKeysByParams("organizationDepMember_list_organizationId_depId", new Object[]{organizationId,depId});
			if(null != ids&&ids.size()>0){
			   depMembers = getEntityByIds(ids);
			   for (OrganizationDepMember organizationDepMember : depMembers) {
					OrganizationMember member = organizationMemberService.getOrganizationMemberDetailById(organizationDepMember.getOrganizationMemberId());
					organizationDepMember.setUserId(member.getUserId());
					organizationDepMember.setUserName(member.getUser().getName());
					organizationDepMember.setUserPic(member.getUser().getPicPath());
		   	   }
			}else{
				logger.info("not found member id by "+organizationId+","+depId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return depMembers;
	}

	@Override
	public OrganizationDepMember getDepMemberByOrganizationIdAndDepIdAndMemberId(long organizationId, long depId, long organizationMemberId)throws DepMemberException,Exception {
		OrganizationDepMember depMember = null;
		try {
			Long id = getMappingByParams("organizationDepMember_map_organizationId_departmentId_organizationMemberId", new Object[]{organizationId,depId,organizationMemberId});
			if(null != id){
			   depMember = getEntityById(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+","+ApiCodes.DepMemberIsNotExist,e);
			throw e;
		}
		return depMember;
	}
	
	@Deprecated
	@Override
	public OrganizationDepMember add(OrganizationMember om,OrganizationDep od) throws Exception{
		OrganizationDepMember depMember = null;
		try {
			OrganizationDepMember odm = new OrganizationDepMember();
			Timestamp t = new Timestamp(System.currentTimeMillis());
			odm.setCreateTime(t);
			odm.setUpdateTime(t);
			odm.setCreaterId(om.getCreaterId());
			odm.setOrganizationId(om.getId());
			odm.setDepId(od.getId());
			depMember = save(odm);			
		} catch (Exception e) {
			logger.error(e.getMessage()+",",e);
			throw e;
		}
		return depMember;
	}
	
	@Override
	public OrganizationDepMember add(long createrId, long organizationId,long depId, long organizationMemberId) throws DepMemberException, Exception {
		OrganizationDepMember depMember = null;
		try {
			//OrganizationDepMember check_odm = getDepMemberByOrganizationIdAndDepIdAndMemberId(organizationId, depId, organizationMemberId);
			OrganizationDepMember check_odm = getDepMemberByOrganizationIdAndMemberId(organizationId, organizationMemberId);
			if(null != check_odm){
				Timestamp t = new Timestamp(System.currentTimeMillis());
				check_odm.setDepId(depId);
				check_odm.setUpdateTime(t);
				boolean update_status = update(check_odm);
				if(update_status){
					depMember = getEntityById(check_odm.getId());
				}
			}
			else{
				OrganizationDepMember odm = new OrganizationDepMember();
				Timestamp t = new Timestamp(System.currentTimeMillis());
				odm.setCreateTime(t);
				odm.setUpdateTime(t);
				odm.setCreaterId(createrId);
				odm.setOrganizationId(organizationId);
				odm.setDepId(depId);
				odm.setOrganizationMemberId(organizationMemberId);
				depMember = save(odm);
			}
		}
		catch(DepMemberException e){
			logger.error(e.getMessage(),e);
			throw e;
		}
		catch (Exception e) {
			logger.error(e.getMessage()+",",e);
			throw e;
		}
		return depMember;
	}
	
	@Override
	protected Class<OrganizationDepMember> getEntity() {
		return OrganizationDepMember.class;
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

}
