package com.ginkgocap.tongren.organization.attendance.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceSystem;
import com.ginkgocap.tongren.organization.attendance.service.AttendanceSystemService;

@Service("attendanceSystemService")
public class AttendanceSystemServiceImpl  extends AbstractCommonService<AttendanceSystem> implements AttendanceSystemService{
	
	private static final Logger logger = LoggerFactory.getLogger(AttendanceSystemServiceImpl.class);
	/**
	 * 增加组织考勤制度
	 * @param attendanceSystem
	 * @return 1 增加成功 2 考勤设置存在 3 参数错误
	 */
	@Override
	public String add(AttendanceSystem attendanceSystem) {
		if(attendanceSystem==null||attendanceSystem.getOrganizationId()==0){
			return "3";
		}
		List<AttendanceSystem> list=getByOrgId(attendanceSystem.getOrganizationId());
		if(list!=null&&list.size()>0){
			return "2";
		}else{
			save(attendanceSystem);
			return "1";
		}
	}

	/***
	 * 修改考勤设置后，默认第二天生效
	 * 修改只是新增一条记录(如果修改原来记录，之前考勤规则就丢失了，将无法计算修改前的考勤信息)
	 * @return 1 更新成功，2 考勤不存在，无法更新 ,3更新失败
	 */
	@Override
	public String modify(AttendanceSystem attendanceSystem) {
		logger.info("modify AttendanceSystem "+attendanceSystem.getOrganizationId());
		 List<AttendanceSystem> list=getByOrgId(attendanceSystem.getOrganizationId());
		 if(list!=null&&list.size()>0){
			 AttendanceSystem as=list.get(0);
			 //是否同一一天
			 if(DateUtil.isSameDay(as.getCreateTime(),attendanceSystem.getCreateTime())){
				 attendanceSystem.setId(as.getId());
				 try {
					BeanUtils.copyProperties(as, attendanceSystem);
					update(attendanceSystem); 
				} catch (Exception e) {
					logger.error("update failed! "+attendanceSystem,e);
					return "3";
				} 
			 }else{
				 save(attendanceSystem); 
			 }
			 return "1";
		 }else{
			 logger.info("not found AttendanceSystem ,update failed!"+attendanceSystem.getOrganizationId());
			 return "2";
		 }
		
	}

	//根据组织id获取所有的考勤设置集合
	@Override
	public List<AttendanceSystem> getByOrgId(long orgId) {
		List<Long> ids=this.getKeysByParams("attendanceSystem_list_organizationId",orgId);
		List<AttendanceSystem>  list = getEntityByIds(ids);
		if(list!=null){
			Collections.sort(list,new Comparator<AttendanceSystem>(){
	
				@Override
				public int compare(AttendanceSystem a1, AttendanceSystem a2) {
					//倒序排序
					return a2.getCreateTime().compareTo(a1.getCreateTime());
				}});
		}
		logger.info("found attendanceSystems "+ids+",by orgId"+orgId);
		return list;
	}

	@Override
	public AttendanceSystem getById(long id) {
		return this.getEntityById(id);
		
	}

	@Override
	protected Class<AttendanceSystem> getEntity() {	
		return AttendanceSystem.class;
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

	/**
	  * 根据组织id获取当前组织的考勤模板
	  * 考勤表中有多条记录，设置靠前模板后第二天生效，因此取前一天之前的创建时间最近的记录，如果所有记录创建时间都大于等于当天，则去默认考勤设置，即创建时间最小的记录
	  * @param orgId
	  * @return
	  */
	@Override
	public AttendanceSystem getCurretAttendanceSystem(long orgId) {
		List<AttendanceSystem> list=getByOrgId(orgId);
		if(list==null ||list.size()==0){
			return null;
		}else if(list.size()==1)
		{
			return list.get(0);
		}else{
			for(int k=0;k<list.size();k++){
				Date d=DateUtil.trunck(list.get(k).getCreateTime());
				Date current=DateUtil.trunck(new Date());
				
				if(current.getTime()>d.getTime()){
					return list.get(k);
				}
			}
			//所有靠前模板的创建时间都是大于昨天的情况，则返回创建时间最小的一条，即默认的考勤设置
			return list.get(list.size()-1);
		}
		
	}

}
