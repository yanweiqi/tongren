package com.ginkgocap.tongren.organization.attendance.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceRecords;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceSystem;
import com.ginkgocap.tongren.organization.attendance.model.DayRecordInfo;
import com.ginkgocap.tongren.organization.attendance.model.MonthRecodInfo;
import com.ginkgocap.tongren.organization.attendance.service.AttendanceRecordsService;
import com.ginkgocap.tongren.organization.attendance.service.AttendanceSystemService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 考勤详细信息实现类
 * @author hanxifa
 *
 */
@Service("attendanceRecordsService")
public class AttendanceRecordsServiceImpl extends  AbstractCommonService<AttendanceRecords> implements AttendanceRecordsService{
	private static final Logger logger = LoggerFactory.getLogger(AttendanceRecordsServiceImpl.class);
	@Autowired
	AttendanceSystemService attendanceSystemService;
	
	@Autowired
	UserService userService;
	
	/**
	 * 上班打卡
	 * @return 1 成功  2 失败  3 已经存在打卡记录 4 没有找到该用户对应组织设置的考勤规则
	 */
	@Override
	public String start(AttendanceRecords attendanceRecords) {
		String status="1";
		if(attendanceRecords!=null){
			if(attendanceRecords.getId()>0){
				logger.warn("attendanceRecords.getId() is "+attendanceRecords.getId()+",will be ignored ");
			}
			//根据用户id 和考勤时间查询是否存在考勤记录
			AttendanceRecords arRecords=getAttendanceRecordsOfDate(attendanceRecords.getUserId(),attendanceRecords.getOrganizationId(),attendanceRecords.getDate());
			if(arRecords!=null){
				logger.warn(attendanceRecords.getUserId()+","+attendanceRecords.getDate()+",has find start record");
				return "3";
			}
			AttendanceSystem attendanceSystem=attendanceSystemService.getCurretAttendanceSystem(attendanceRecords.getOrganizationId());
			if(attendanceSystem==null){
				logger.warn(" not found AttendanceSystem by org id "+attendanceRecords.getOrganizationId());
				return "4";
			}
			attendanceRecords.setAttendanceSystemId(attendanceSystem.getId());
			if(attendanceRecords.getWorkTimeOut()!=null){
				logger.info("set WorkTimeOut is null");
				attendanceRecords.setWorkTimeOut(null);
			}
			attendanceRecords.setDate(new Timestamp(DateUtil.trunck(attendanceRecords.getDate()).getTime()));
			attendanceRecords.setCreateTime(new Timestamp(System.currentTimeMillis()));
			attendanceRecords.setUpdateTime(attendanceRecords.getCreateTime());
			AttendanceRecords ar=save(attendanceRecords);
			if(ar!=null){
				status= "1_"+ar.getId();
				attendanceRecords.setId(ar.getId());
			}else{
				status= "2";
			}
		}else{
			logger.info("AttendanceRecords is null");
			status= "2";
		}
		logger.info("save AttendanceRecords status is "+status);
		return status;
	}
	
	/**
	 * 根据用户id获取 该用户在指定时间打卡信息的记录
	 * @param userId
	 * @param date
	 * @return
	 */
	private List<Long> getIdByUserIdAndDate(long userId,Date date){
		//DateUtil.trunck(date) 作用在于实时维护缓存中的数据
		List<Long> rids=getKeysByParams("attendanceRecords_list_userId_date",userId,new Timestamp(DateUtil.trunck(date).getTime()));
		return rids;
	}
	
	/**
	 * 获取某个时间段内的打卡信息 时间段范围的规则是 左闭右开区间
	 * @param userId
	 * @param date
	 * @return
	 */
	private  List<AttendanceRecords> getMonthRecords(long userId,Date beginDate,Date endDate){
		//获取打卡记录的id
		endDate=DateUtil.trunck(DateUtil.addDay(endDate,1));
		List<Long> arids=getKeysByParams("attendanceRecords_list_userId_date_date", userId,beginDate,new Timestamp(endDate.getTime()));
		logger.info("MonthRecords is "+arids+",userId:"+userId+",beginDate:"+beginDate+",endDate:"+endDate);
		if(arids==null){
			return null;
		}
		List<AttendanceRecords> list=new ArrayList<AttendanceRecords>();
		for(Long id:arids){
			AttendanceRecords attendanceRecords= getEntityById(id);
			list.add(attendanceRecords);
		}
		return list;
	}
	/**
	 * 下班打卡
	 * 1 成功 2 失败  3没有找到该id对应的打卡记录  4 没有找到该用户对应组织设置的考勤规则 5 该用户一天已经存在多条打卡记录
	 */
	@Override
	public String end(AttendanceRecords attendanceRecords) {
		String status="1";
		if(attendanceRecords!=null){
			AttendanceRecords records=getAttendanceRecordsOfDate(attendanceRecords.getUserId(),attendanceRecords.getOrganizationId(),attendanceRecords.getDate());
			 if(records!=null){
				 attendanceRecords.setId(records.getId());
			 }
			if(attendanceRecords.getId()==0){//id将插入新的打卡记录，表明上班未打卡，
				AttendanceSystem attendanceSystem=attendanceSystemService.getCurretAttendanceSystem(attendanceRecords.getOrganizationId());
				if(attendanceSystem==null){
					logger.warn(" not found AttendanceSystem by org id "+attendanceRecords.getOrganizationId());
					status="4";
				}else{
					attendanceRecords.setDate(new Timestamp(DateUtil.trunck(attendanceRecords.getDate()).getTime()));
					attendanceRecords.setAttendanceSystemId(attendanceSystem.getId());
					attendanceRecords.setCreateTime(new Timestamp(System.currentTimeMillis()));
					attendanceRecords.setUpdateTime(attendanceRecords.getCreateTime());
					save(attendanceRecords);
				}
				
			}else{//已经存在打卡记录，修改本条记录打卡时间
				AttendanceRecords aRecords= getEntityById(attendanceRecords.getId());
				if(aRecords!=null){
					aRecords.setWorkTimeOut(attendanceRecords.getWorkTimeOut());
					aRecords.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					aRecords.setIpAddrEnd(attendanceRecords.getIpAddrEnd());
					aRecords.setLonlatEnd(attendanceRecords.getLonlatEnd());
					update(aRecords);
				}else{
					logger.warn("go off workrecord failded! not found AttendanceRecords by id"+attendanceRecords.getId());
					status="3";
				}
				
			}
		}else{
			status="2";
			logger.info(" go off workrecord attendanceRecords is null");
		}
		return status;
	}
	
 
	/**
	 * 获取某个月份的出勤信息汇总,
	 * @param userId
	 * @param 是否载入月度每天的考勤信息
	 * @param month 格式为 yyyy-MM 2015-08
	 * @return
	 */
	@Override
	public MonthRecodInfo getMonthInfo(long userId, String month,long orgId) {
		Date beginDate=DateUtil.parseYearMonth(month);
		if(beginDate.getTime()>System.currentTimeMillis()){
			logger.info( month+ " is greater than current date ");
			return null;
		}
		logger.info("begin getMonthInfo userId:"+userId+",month "+month);
		//计算 结束日期，本月的情况取当前日期
		Calendar cl=Calendar.getInstance();
		cl.setTime(beginDate);
		cl.add(Calendar.MONTH, 1);
		cl.set(Calendar.DAY_OF_MONTH, 1);
		cl.add(Calendar.DAY_OF_MONTH, -1);
		MonthRecodInfo monthRecodInfo=new MonthRecodInfo();
		Date endDate=DateUtil.trunck(cl.getTime());
		Date currentDate=DateUtil.trunck(new Date());
		if(endDate.getTime()>currentDate.getTime()){
			endDate=currentDate;
		}
		//从数据库中查询该用户 在这个时间段内的打卡记录
		List<AttendanceRecords> lrecords=getMonthRecords(userId,beginDate,endDate);
		//查询月份的详细考勤信息
		List<DayRecordInfo> detailList=new ArrayList<DayRecordInfo>();
		//1号循环到月底
		int comelateDays=0;//迟到天数
		int leaveearlierDays=0; //早退天数
		int  absenceDays=0;//缺卡天数 ，只要上下班存在一次未打卡，表示缺卡
		while(beginDate.getTime()<=endDate.getTime()){
			try{
			AttendanceRecords ar=searchRecords(lrecords,beginDate,orgId);
			DayRecordInfo dayRecordInfo=new DayRecordInfo();
			detailList.add(dayRecordInfo);
			
			dayRecordInfo.setIsWarkDay(DateUtil.isWorkDay(beginDate));
			
			dayRecordInfo.setWorkDay(beginDate);//设置考勤日期
			//dayRecordInfo.getWorkDay().setTime(beginDate.getTime());
			if(ar==null){
				if(dayRecordInfo.getIsWarkDay()){
					dayRecordInfo.setStatus(3);//当前日期是工作日，未打卡 视为缺卡 
					absenceDays++;
				}else{
					dayRecordInfo.setStatus(1);//当前日是未非工作日 ，视为正常  
				}
				
			}else{
				monthRecodInfo.setRealWorkDays(monthRecodInfo.getRealWorkDays()+1);//实际出勤天数+1
				dayRecordInfo.setAttendanceSystemId(ar.getAttendanceSystemId());
				//设置扩展属性
				dayRecordInfo.addExtend("templetInfo", attendanceSystemService.getEntityById(ar.getAttendanceSystemId()));
				dayRecordInfo.addExtend("lonlatStart", ar.getLonlatStart());
				dayRecordInfo.addExtend("lonlatEnd", ar.getLonlatEnd());
				dayRecordInfo.addExtend("ipAddrEnd", ar.getIpAddrEnd());
				dayRecordInfo.addExtend("ipAddrStart", ar.getIpAddrStart());
				dayRecordInfo.setStart(ar.getStartWorkTime());
				dayRecordInfo.setEnd(ar.getWorkTimeOut());
				if(dayRecordInfo.getIsWarkDay()){//工作日，有打卡记录，则计算打卡时间是否正常
					AttendanceSystem as = attendanceSystemService.getById(ar.getAttendanceSystemId());//获取考勤规则模板
					if(as==null){
						dayRecordInfo.setStatus(1);//未发现模板则视为正常
					}else{
						int elastictyMills= as.getElasticityMinutes()*60*1000;//弹性时间换算为毫秒 
						
						//计算上班时间 
						if(ar.getStartWorkTime()==null){
							dayRecordInfo.setStartStatus(3);//上班未打卡
							absenceDays++;
						}else{
							long differenMills=differMills(ar.getStartWorkTime(),as.getStartWorkTime());
							if(differenMills<=0){//上班时间早于规定时间
								dayRecordInfo.setStartStatus(1);
							}else {
								if(differenMills<=elastictyMills){//弹性工作时间内
									dayRecordInfo.setStartStatus(1);
								}else{
									comelateDays++;
									dayRecordInfo.setStartStatus(2);
								}
							}
						}
						//计算下班时间
						if(ar.getWorkTimeOut()==null){
							dayRecordInfo.setEndStatus(3);//下班未打卡
							absenceDays++;
						}else{
							Date startWorkTime=ar.getStartWorkTime();
							long beteenTime=ar.getWorkTimeOut().getTime()-(startWorkTime==null?0:startWorkTime.getTime());//下班时间 减去上班时间
							long differenMills=differMills(ar.getWorkTimeOut(),as.getWorkTimeOut());
							if(differenMills>0){ //下班弹性时间以外
								dayRecordInfo.setEndStatus(1);//下班正常
							}else{
								if(Math.abs(differenMills)<elastictyMills){ //弹性时间内
									if(beteenTime>=9*60*60*1000){    //大于8小时
										dayRecordInfo.setEndStatus(1);//下班正常
									}else{//小于8小时
										dayRecordInfo.setEndStatus(2);//下班早退
										leaveearlierDays++;
									}
								}else{//弹性时间外
									dayRecordInfo.setEndStatus(2);//下班早退
									leaveearlierDays++;
								}
								
							}
						}
						if(dayRecordInfo.getStartStatus()==1&&dayRecordInfo.getEndStatus()==1){
							dayRecordInfo.setStatus(1);
						}else{
							dayRecordInfo.setStatus(2);
						}
					}
				}else{
					dayRecordInfo.setStatus(4);//当前日是未非工作日 ，视为加班 
				}
				
			}
			}catch(Exception e){
				logger.error("处理  "+beginDate+" 天 勤信息失败！",e);
			}
			//时间增加一天
			beginDate.setTime(beginDate.getTime()+86400000);
		}
		
		
		monthRecodInfo.setRecordDetail(detailList);
		monthRecodInfo.setComelateDays(comelateDays);
		//monthRecodInfo.setRealWorkDays(lrecords.size()); 实际出勤天数
		monthRecodInfo.setLeaveearlierDays(leaveearlierDays);
		monthRecodInfo.setAbsenceDays(absenceDays);
		monthRecodInfo.setUserId(userId);
		User user= userService.selectByPrimaryKey(userId);
		if(user!=null){
			logger.info("userId"+user.getId()+",userName is -->"+user.getName());
			monthRecodInfo.setUserName(user.getName());
		}else{
			monthRecodInfo.setUserName(userId+"");
		}
		
		return monthRecodInfo;
	}
	/**
	 * 计算给定日期中的时间与指定时间相差的毫秒数量
	 * @param date
	 * @param hm 格式为：hh:mm 比如09:00
	 * @return 相差的毫秒数量
	 */
	private static long differMills(Date date,String hm){
		int index=hm.indexOf(":");
		String hour=hm.substring(0,index);
		String mins=hm.substring(index+1);
		long hmm=(Long.valueOf(hour)*60+Long.valueOf(mins))*60*1000;
		Calendar c= Calendar.getInstance();
		c.setTime(date);
		long dmils=(c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE))*60*1000;
		return dmils-hmm;
	}
	
	 
	/**
	 * 该list中是否包含指定日期的打卡记录
	 * @param list
	 * @param date
	 * @return
	 */
	private AttendanceRecords searchRecords(List<AttendanceRecords> list,Date date,long orgId){
		for(AttendanceRecords ars:list){
			if(DateUtil.trunck(ars.getDate()).getTime()==date.getTime()&&orgId==ars.getOrganizationId()){
				return ars;
			}
		}
		return null;
	}
	
	 

	@Override
	protected Class<AttendanceRecords> getEntity() {
		return AttendanceRecords.class;
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

	/**
	 * 获取某个用户在某个组织下的打卡记录
	 * @param userid
	 * @param orgId
	 * @param date
	 * @return
	 */
	@Override
	public AttendanceRecords getAttendanceRecordsOfDate(long userId, long orgId, Date date) {
		List<Long> ids = getIdByUserIdAndDate(userId, date);
		if (ids != null && ids.size() > 0) {
			logger.info("found attendanceRecords ids " + ids + ",by userId:" + userId + ",orgId:" + orgId + ",date:" + date);
			for (Long id : ids) {
				AttendanceRecords ar = getEntityById(id);
				if (ar.getOrganizationId() == orgId) {
					ar.addExtend("templetInfo", attendanceSystemService.getEntityById(ar.getAttendanceSystemId()));
					return ar;
				}
			}

		} else {
			logger.warn("not found AttendanceRecords " + ",by userId:" + userId + ",orgId:" + orgId + ",date:" + date);
		}
		return null;
	}

}
