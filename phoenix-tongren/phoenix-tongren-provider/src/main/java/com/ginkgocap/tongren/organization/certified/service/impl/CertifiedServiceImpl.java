package com.ginkgocap.tongren.organization.certified.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.certified.model.Certified;
import com.ginkgocap.tongren.organization.certified.service.CertifiedService;


/**
 * 组织认证接口实现
 * @author hanxifa
 *
 */
@Service("certifiedService")
public class CertifiedServiceImpl  extends AbstractCommonService<Certified> implements CertifiedService{

	private static final Logger logger = LoggerFactory.getLogger(CertifiedServiceImpl.class);
	
	/***
	 * @return 1_id 增加成功  2数据库中已经存在该组织id对应的认证信息  3 更新失败
	 */
	public String add(Certified certified) {
		if(certified!=null){
			Certified cdb=getByOrgId(certified.getOrganizationId());
			if(cdb!=null){
				return "2";
			}
			cdb=save(certified);
			return "1_"+cdb.getId();
		}else{
			logger.info("save Certified failed! certified is null");
		}
		return "3";
	}

	@Override
	public Certified getById(long id) {
		return this.getEntityById(id);
	}
	
	/**
	 * 根据组织id获取组织认证信息
	 * @param id
	 * @return 查询不到则返回null
	 */
	@Override
	public Certified getByOrgId(long orgId) {
		String sqlId="certified_list_organizationId";
		List<Long> list=this.getKeysByParams(sqlId, orgId);
		if(list.size()>=1){
			 if(list.size()>1){
				logger.warn("find mutil certified by orgId "+orgId+" use first "+list.get(0));
			 }
			return this.getById(list.get(0));
		}
		return null;
	}

	/***
	 * 修改组织认证状态
	 * @param id 认证组织id
	 * @param status 更新状态  只能为 1 2 3 
	 * @param userId 操作的用户id 
	 * @return 1 更新成功 2 数据库中不存在记录 3 当前状态不能更新 4 状态值只能为 1 2 3 
	 */
	@Override
	public String updateStatus(long id, int status,long userId) {
		if(status!=1&&status!=2&&status!=3){
			logger.info("status is invalid "+status);
			return "4";
		}
		Certified cdb=null;
		cdb=this.getById(id);
		if(cdb==null){
			logger.info("not found Certified by id"+id);
			return "2";
		}
		if(cdb.getStatus().equals("1"))
		{
			if(status!=2&&status!=3){
				return "3";
			}
		}else if(cdb.getStatus().equals("2")){//审核通过状态不能修改
			return "3";
		}else if(cdb.getStatus().equals("3")){
			if(status!=1&&status!=3){//审核不通过状态可以修改为待审核状态，也可以状态不变
				return "3";
			}
		}
		cdb.setStatus(status+"");
		cdb.setOperUserId(userId);
		cdb.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		update(cdb);
		return "1";
	}
	/**
	 * 修改组织认证信息
	 * @param certified
	 * @return 1 更新成功 2 数据库中不存在记录 3 当前状态不能更新  4 状态值只能为 1 2 3 
	 */
	public String modify(Certified certified){
		Certified cdb=null;
		cdb=this.getById(certified.getId());
		if(cdb==null){
			cdb=getByOrgId(certified.getOrganizationId());
		}
		if(cdb==null){
			logger.info("not found Certified,id:"+certified.getId()+",orgId:"+certified.getOrganizationId());
			return "2";
		}
		String status=updateStatus(cdb.getId(),Integer.valueOf(certified.getStatus()),certified.getOperUserId());
		if(status.equals("1")==false){
			return status;
		}
		cdb.setBusinessLicense(certified.getBusinessLicense());
		cdb.setFullName(certified.getFullName());
		cdb.setIdentityCard(certified.getIdentityCard());
		cdb.setIntroduction(certified.getIntroduction());
		cdb.setLegalPerson(certified.getLegalPerson());
		cdb.setLegalPersonMobile(certified.getLegalPersonMobile());
		cdb.setLogo(certified.getLogo());
		cdb.setOperUserId(certified.getOperUserId());
		cdb.setOrganizationId(certified.getOrganizationId());
		cdb.setOrganizationType(certified.getOrganizationType());
		cdb.setStatus(certified.getStatus());
		cdb.setUpdateTime(certified.getUpdateTime());
		update(cdb);
		return "1";
	}

	@Override
	protected Class<Certified> getEntity() {
		// TODO Auto-generated method stub
		return Certified.class;
	}

	@Override
	public Map<String, Object> doWork() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
