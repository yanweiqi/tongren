package com.ginkgocap.tongren.organization.attendance.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.common.utils.DateUtil;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.attendance.model.AttendanceRecords;
import com.ginkgocap.tongren.organization.attendance.model.DayRecordInfo;
import com.ginkgocap.tongren.organization.attendance.model.MonthRecodInfo;
import com.ginkgocap.tongren.organization.attendance.service.AttendanceRecordsService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;

/**
 * 考勤记录
 * @author hanxifa
 *
 */
@Controller
@RequestMapping("/attendanceRecords") 
public class AttendanceRecordsController extends BaseController{

	private final static Logger logger = LoggerFactory.getLogger(AttendanceRecordsController.class);
	@Autowired
	private AttendanceRecordsService attendanceRecordsService;
	
	/***
	 * 增加上班打卡记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/addBegin.json",method = RequestMethod.POST)
	public void addBegin(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"organizationId|R","ipAddrStart|RP","lonlatStart|RM"};
		ParamInfo params=null;
	//	SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			AttendanceRecords attendanceRecords=new AttendanceRecords();
			attendanceRecords.setOrganizationId(Long.valueOf(params.getParam("organizationId")));
			Timestamp current=new Timestamp(System.currentTimeMillis());
			attendanceRecords.setStartWorkTime(current);//打卡取系统时间
			attendanceRecords.setDate(current);
			
			attendanceRecords.setUserId(user.getId());
			//attendanceRecords.setCreateTime(new Timestamp(System.currentTimeMillis()));
			//attendanceRecords.setUpdateTime(attendanceRecords.getCreateTime());
			if("web".equals(params.getParam("ipAddrStart"))){
				String ipaddr= request.getHeader("X-Real-IP");
				if(!StringUtils.hasText(ipaddr)){
					ipaddr=request.getRemoteAddr();
				}
				logger.info("remoteAddr is :"+ipaddr);
				attendanceRecords.setIpAddrStart(ipaddr);
			}
			attendanceRecords.setLonlatStart(params.getParam("lonlatStart"));
			String rstatus=attendanceRecordsService.start(attendanceRecords);
			String status=rstatus;
			if(rstatus.indexOf("_")>0){
				status=rstatus.substring(0,1);
			}
			//1 成功  2 失败  3 已经存在打卡记录 4 没有找到该用户对应组织设置的考勤规则
			String[] rsStr={"成功","打卡失败","已经存在打卡记录","没有找到该用户对应组织设置的考勤规则"};
			if(status.equals("1")){
				attendanceRecords.setId(Long.parseLong(rstatus.substring(2)));
				responseData.put("record", attendanceRecordsService.getAttendanceRecordsOfDate(user.getId(), attendanceRecords.getOrganizationId(), attendanceRecords.getDate()));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				logger.info("addBegin attendancesRecords success! param: "+params.getParams());
			}else{
				String msg= rsStr[Integer.parseInt(status)-1];
				notification.put("notifyCode", SysCode.ERROR_CODE.getCode());
				notification.put("notifyMessage", msg);
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(responseData, notification)));
				logger.info("addBegin attendancesRecords failed status  "+status+",param:"+params.getParams());
			}
 		   
		}catch(Exception e){
			logger.error("addBegin attendancesRecords failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
		
	}
	
	/***
	 * 增加下班打卡记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/addEnd.json",method = RequestMethod.POST)
	public void addEnd(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		//id为上班打卡时对应打卡记录的ID,如果传入的id不存在，则搜索date 字段对应的上班打卡记录，date为空 则按workTimeOut 搜索 
		String paramsKey[] = {"organizationId|R","ipAddrEnd|RP","lonlatEnd|RM"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			AttendanceRecords attendanceRecords=new AttendanceRecords();
			attendanceRecords.setOrganizationId(Long.valueOf(params.getParam("organizationId")));
			Timestamp current=new Timestamp(System.currentTimeMillis());
			attendanceRecords.setWorkTimeOut(current);
			attendanceRecords.setUserId(user.getId());
			attendanceRecords.setDate(current);
			if("web".equals(params.getParam("ipAddrEnd"))){
				String ipaddr= request.getHeader("X-Real-IP");
				if(!StringUtils.hasText(ipaddr)){
					ipaddr=request.getRemoteAddr();
				}
				logger.info("remoteAddr is :"+ipaddr);
				attendanceRecords.setIpAddrEnd(ipaddr);
			}
			attendanceRecords.setLonlatEnd(params.getParam("lonlatEnd"));
			String status=attendanceRecordsService.end(attendanceRecords);
			//1 成功 2 失败  3没有找到该id对应的打卡记录  4 没有找到该用户对应组织设置的考勤规则 5 该用户一天已经存在多条打卡记录
			String[] rsStr={"成功","下班打卡失败","没有找到该id对应的打卡记录","没有找到该用户对应组织设置的考勤规则 ","该用户一天已经存在多条打卡记录"};
			if(status.equals("1")){
				responseData.put("record", attendanceRecordsService.getAttendanceRecordsOfDate(user.getId(), attendanceRecords.getOrganizationId(), attendanceRecords.getDate()));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				logger.info("add end attendancesRecords success! param: "+params.getParams());
			}else{
				String msg= rsStr[Integer.parseInt(status)-1];
				notification.put("notifyCode", SysCode.ERROR_CODE.getCode());
				notification.put("notifyMessage", msg);
				renderResponseJson(response, params.getResponse(SysCode.ERROR_CODE, genResponseData(responseData, notification)));
				logger.info("add end attendancesRecords failed status  "+status+",param:"+params.getParams());
			}
 		   
		}catch(Exception e){
			logger.error("add end attendancesRecords failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	
	/***
	 * 获取某个用户的月度打卡记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/getMonthInfo.json",method = RequestMethod.POST)
	public void getMonthInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		//userid 可以为逗号分隔的多个
		String paramsKey[] = {"userId|R","month","organizationId|R|L20"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			//User user=ri.getUser();
			params=ri.getParams();
			logger.info("begin query month attendance-records info "+params.getParams());
			String month=params.getParam("month");

			if(!StringUtils.hasText(month)){
				month=DateUtil.formatDateToDay(new Date()).substring(0,7);
			}
			String ymsg=validateMonth(month);
			if(ymsg!=null){
				notification.put("notifyCode", SysCode.PARAM_IS_ERROR.getCode());
				notification.put("notifyMessage", ymsg);
				renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(null, notification)));
				return;
			}
			
			
			//验证日期格式结束
			String userId=params.getParam("userId");
			String[] uidArray=userId.split(",");
			List<MonthRecodInfo> mlist=new ArrayList<MonthRecodInfo>();
			for(String uid:uidArray){
				MonthRecodInfo monthRecodInfo= attendanceRecordsService.getMonthInfo(Long.parseLong(uid), month,Long.parseLong(params.getParam("organizationId")));
				mlist.add(monthRecodInfo);
			}
			responseData.put("records", mlist);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			logger.info("query month attendance-records info success "+request.getParameter("requestJson"));
		}catch(Exception e){
			logger.error("getMonthInfo attendancesRecords failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
	
	/***
	 * 获导出某个月的打卡记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/exportMonth.json")
	public void exportMonth(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		//Map<String, Object> responseData = new HashMap<String, Object>();
		//userid 可以为逗号分隔的多个
		String paramsKey[] = {"userId|R","month","organizationId|R"};
		ParamInfo params=null;
		try{
			String sid=request.getParameter("sessionID");
			if(sid!=null&&sid.trim().length()>0){
				request.setAttribute("sessionID_Param", sid);
			}
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			//User user=ri.getUser();
			params=ri.getParams();
			logger.info("begin query month attendance-records info "+params.getParams());
			String month=params.getParam("month");
			if(!StringUtils.hasText(month)){
				month=DateUtil.formatDateToDay(new Date()).substring(0,7);
			}
			String ymsg=validateMonth(month);
			if(ymsg!=null){
				notification.put("notifyCode", SysCode.PARAM_IS_ERROR.getCode());
				notification.put("notifyMessage", ymsg);
				renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(null, notification)));
				return;
			}
			//验证日期格式结束
			String userId=params.getParam("userId");
			String[] uidArray=userId.split(",");
			List<MonthRecodInfo> mlist=new ArrayList<MonthRecodInfo>();
			for(String uid:uidArray){
				MonthRecodInfo monthRecodInfo= attendanceRecordsService.getMonthInfo(Long.parseLong(uid), month,Long.parseLong(params.getParam("organizationId")));
				mlist.add(monthRecodInfo);
			}
			if(mlist.size()>0){
				String path=exportExcels(mlist,month);
				if(path!=null){
					 OutputStream ros = response.getOutputStream();  
					    try {  
					    	response.reset();  
					    	String fname= java.net.URLEncoder.encode("考勤记录", "UTF-8")+"("+month+").xls";
					    	response.setHeader("Content-Disposition", "attachment; filename="+fname);  
					    	response.setContentType("application/octet-stream; charset=utf-8");  
					        ros.write(FileUtils.readFileToByteArray(new File(path)));  
					        ros.flush();  
					        return;
					    } finally {  
					        if (ros != null) {  
					        	ros.close();  
					        }  
					        new File(path).delete();
					    }  
				}else{
					logger.info(" path is null "+params.getParams());
				}
			}
			else{
				notification.put("notifyCode", SysCode.BIGDATA_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.BIGDATA_EMPTY);
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
			}
			logger.info("query month attendance-records info success "+request.getParameter("requestJson"));
		}catch(Exception e){
			logger.error("getMonthInfo attendancesRecords failed! param:"+request.getParameter("requestJson"),e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	private String validateMonth(String month){
		//验证日期格式开始 
		String pattern = "[0-9]{4}-[0-9]{2}";
		String ymsg=null;
		if(month.matches(pattern)){
			int index=month.indexOf('-');
			int year=Integer.parseInt(month.substring(0,index));
			int m=Integer.parseInt(month.substring(index+1));
			Calendar c=Calendar.getInstance();
			if(year>c.get(Calendar.YEAR)||year<2010){
				ymsg="年份不合法";
			}else if(m>12||m<1){
				ymsg="月份不合法";
			}
		}else{
			ymsg="日期格式不正确(eg. yyyy-mm)";
		}
		long time =DateUtil.parseYearMonth(month).getTime();
		String currentDate=DateUtil.formatDateToDay(new Date());
		long currentTime=DateUtil.parseYearMonth(currentDate.substring(0,7)).getTime();
		if(time>currentTime){
			ymsg="不能大于当前月份";
		}
		return ymsg;
	}
	/**
	 * 打卡记录导出excels
	 * @param mlist
	 * @param yearMonth 格式为 2015-09
	 * @throws Exception 
	 */
	private static String exportExcels(List<MonthRecodInfo> mlist, String yearMonth) {

		String path = System.getProperty("user.dir") + File.separator + System.currentTimeMillis() + "_record.xls";
		logger.info("export path is " + path);
		FileOutputStream os = null;
		WritableWorkbook workbook = null;
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
			SimpleDateFormat sftitle = new SimpleDateFormat("yyyy/MM/dd");
			Date ydate = sf.parse(yearMonth);
			Calendar cbegin = Calendar.getInstance();
			cbegin.setTimeInMillis(ydate.getTime());

			Calendar cend = Calendar.getInstance();
			cend.setTimeInMillis(ydate.getTime());
			cend.add(Calendar.MONTH, 1);
			
			os = new FileOutputStream(path);
			workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("打卡记录", 0);

			int xindex = 0;
			int yindex = 0;
			Label formate = new Label(xindex, yindex, "姓名/时间");
			sheet.addCell(formate);

			// 构造第一行的标题
			while (cbegin.getTimeInMillis() != cend.getTimeInMillis()) {
				xindex++;
				formate = new Label(xindex, yindex, sftitle.format(cbegin.getTime()));
				sheet.addCell(formate);
				cbegin.add(Calendar.DAY_OF_MONTH, 1);
			}
			for (MonthRecodInfo mri : mlist) {
				List<DayRecordInfo> list = mri.getRecordDetail();
				yindex++;
				xindex = 0;
				// 写入 姓名列
				formate = new Label(xindex, yindex, mri.getUserName());
				sheet.addCell(formate);
				// 写入每天的考勤信息
				for (DayRecordInfo dri : list) {
					xindex++;
					String str=dri.toString();
					int si=str.indexOf("状态:")+3;
					logger.info("staus -->"+str.substring(si));
					formate = new Label(xindex, yindex, str.substring(si));
					sheet.addCell(formate);
				}
			}
			workbook.write();
			return path;
		} catch (Exception e) {
			logger.error("genrate excel failded! "+yearMonth,e);
		} finally {
			try {
				workbook.close();
			} catch (Exception e) {
			}
			;
			try {
				os.close();
			} catch (Exception e) {
			}
			;
		}
		return null;
	}
	
	/**
	 * 获取用的打卡记录 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAttendanceRecordsOfDate.json", method = RequestMethod.POST)
	public void getAttendanceRecordsOfDate(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = { "organizationId|R", "date|R" };
		ParamInfo params = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			AttendanceRecords aRecords = attendanceRecordsService.getAttendanceRecordsOfDate(user.getId(),
					Long.parseLong(params.getParam("organizationId")), sf.parse(params.getParam("date")));
			responseData.put("records", aRecords);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
		} catch (Exception e) {
			logger.error("getAttendanceRecordsOfDate  failed! param:" + request.getParameter("requestJson"), e);
			e.printStackTrace();
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR);
			renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			return;
		}
	}
	
}
