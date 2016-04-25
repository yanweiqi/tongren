package com.ginkgocap.tongren.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.Constants;
import com.ginkgocap.ywxt.util.sso.session.SessionManager;

/**
 * 
 * @author yusq
 * @2013/5/8
 */
public class Constant {
	
	public static final String WEB = "web";
	/**
	 * 维护人类型标识
	 */
	public static final int _MAIN_STRING = 0;
	/**
	 * 浏览人类型标识
	 */
	public static final int _VIEW_STRING = 1;
	
	public static final String TB_CUSTOMER_GROUP = "tb_business_customer_group"; //客户分组表
	public static final String TB_PEOPLE_GROUP = "tb_business_people_group";	 //人脉分组表
	
	
	
	/**
	 * 消息业务类型：1需求中心，2业务需求，3项目，4人脉，5客户，6人员管理，7知识库,9人员管理
	 */
	
	public static final String BM_REQUIRE = "1";	 //需求中心
	public static final String BM_BUSINESS_REQUIRE = "2";	 //业务需求
	public static final String BM_PROJECT = "3";	 //项目
	public static final String BM_PEOPLE = "4";	 //人脉
	public static final String BM_CUSTOMER = "5";	 //客户
	public static final String BM_EMPLOYEE = "6";	 //人员管理
	public static final String BM_KNOWLEDGE = "7";	 //知识库
	public static final String BM_CONTRACT = "9";	 //人员管理
	

	/**
	 * 消息子任务类型：0通知消息 1告知消息
	 */
	public static final int BM_MSGTYPE_NOTICE = 0;	 //通知消息
	public static final int BM_MSGTYPE_INFORM = 1;	 //告知消息
	
	public static final String BM_STATUS_UNREAD= "0";	 //
	public static final String BM_STATUS_READ = "1";	 //
	
	/**
	 * 消息权息类型：0表示其他权限，1转出权限,2转入权限，3无需权限
	 */
	public static final int BM_NETTYPE_OTHER = 0;	 //其他权限
	public static final int BM_NETTYPE_EXTERNAL = 1; //转出权限
	public static final int BM_NETTYPE_INTERNAL = 2; //转入权限
	public static final int BM_NETTYPE_NOTHING = 3;	 //无需权限
	
	
	/**
	 * 分享接受方 0 互联网个人好友 1 业务系统事业部 2机构好友 3互联网
	 */
	public static final String SHARE_FRIEND = "0";
	public static final String SHARE_INC = "1";
	public static final String SHARE_ORG = "2";
	public static final String SHARE_NET = "3";
	
	
	/**
	 * 分享业务状态
	 */
	public static final String SHARE_STATUS_NEW = "6";		//初始状态
	public static final String SHARE_STATUS_FAIL = "5";	//分享失败（拒绝转出或者拒绝转入）
	public static final String SHARE_STATUS_PASS = "4";	//转出成功（转出成功或者转入成功）
	
	/**
	 * 分享业务状态
	 */
	public static final int SHARE_OUT = 1;		//业务转出
	public static final int SHARE_IN = 2;	//业务转入
	
	/**
	 * 1：人脉,2:客户，3:需求业务,4:项目,5:知识,
	 */
	public static final String SHARE_PEOPLE = "1";	 //人脉
	public static final String SHARE_CUSTOMER = "2";	 //客户
	public static final String SHARE_REQUIRE = "3";	 //需求中心
	public static final String SHARE_PROJECT = "4";	 //项目
	public static final String SHARE_KNOWLEDGE = "5";	 //人员管理
	public static final int REMIND_STATUS_NEW = 0;
	public static final int REMIND_STATUS_FINISH = 1;
	
	//缓存中用到的 String  key
	//将和登录用户相关的部门、员工、公司部门员工关系、所有子事业部等信息放到session中
	 public final static String SELECT_DEPT_KEYWORD = "select.dept";
    public final static String SELECT_ALL_DEPT_KEYWORD = "select.all.dept";
    public final static String SELECT_ALL_DEPT_STR_KEYWORD = "select.all.dept.str";
    public final static String SELECT_EMPLOYEE_KEYWORD = "select.employee";
    public final static String SELECT_DEPT_EMPLOYEE_KEYWORD = "select.dept.employee";
    
    public final static String SELECT_ALL_USER = "select.all.user";
    
    public final static String SOCIAL_TYPE_BUSS = "事务沟通";
    public final static String SOCIAL_TYPE_SHARE = "发分享";
    public final static String SOCIAL_TYPE_TASK = "发任务";
    
    public final static String SOCIAL_TASK_PRO = "项目";
    public final static String SOCIAL_TASK_REQ= "业务需求";
    public final static String SOCIAL_TASK_OTH = "其他任务";
    
    
    public final static int SOCIAL_SENDSMS_YES = 1; //是否发送短信
    public final static int SOCIAL_SENDSMS_NO = 0; //是否发送短信
    
    public static final int PROGRAM_DAY_NUM = 1;
	
    
    /***match bigdata redis key Beging ***/
    	/***机构匹配投需求***/
    public static final String REDIS_APP_PEOPLE_RECOMMENDS_REQS = "APP_people_recommends_reqs";
    	/***机构匹配投关系 ***/
    public static final String REDIS_APP_PEOPLE = "APP_people";
    /***需求匹配知识 ***/
    public static final String REDIS_APP_REQ_KNOWLEDGE = "APP_req_knowledge";
    /***需求匹配投融资 ***/
    public static final String REDIS_APP_REQ_MATCH = "APP_req_match";
    /***需求匹配投关系 ***/
    public static final String REDIS_APP_REQ_PEOPLE = "APP_req_people";
    	/***业务需求匹配投知识 ***/
    public static final String REDIS_APP_BUSINESS_REQ_KNOWLEDGE = "APP_business_req_knowledge";
	    /***业务需求匹配投需求 ***/
    public static final String REDIS_APP_BUSINESS_REQ_MATCH = "APP_business_req_match";
	    /***业务需求匹配投关系 ***/
    public static final String REDIS_APP_BUSINESS_REQ_PEOPLE = "APP_business_req_people";
    	/***项目匹配投知识 ***/
    public static final String REDIS_APP_PRJ_REQ_KNOWLEDGE = "APP_prj_req_knowledge";
    	/***项目匹配投需求 ***/
    public static final String REDIS_APP_PRJ_REQ_MATCH = "APP_prj_req_match";
    	/***项目匹配投关系 ***/
    public static final String REDIS_APP_PRJ_REQ_PEOPLE = "APP_prj_req_people";
    /***match bigdata redis key End ***/
    
    /**
	  * 静态资源对象常量key值
	  */
	 public static final String CONST_KEY = "CONST_KEY";
	/**
	 * 当前登录用户
	 * @return
	 */
	 public static User getUser() {
	        return (User) SessionManager.getCurrentSession()
					.getAttribute(Constants.USER_SNAPSHOT_KEYWORD);
	    }
	 
	 /**
	  * 需要强制更新的版本<br/>
	  * 说明：传null、""、VERSIONS中包含的值都会提示更新
	  */
	 public static final String[]  VERSIONS = {"0.045"};
	 public static boolean checkUpdateVersions(String version){
		 if(StringUtils.isEmpty(version)){
			 return true;
		 }
		 boolean result = false;
		 for(String obj : VERSIONS){
			 if(obj.equals(version)){
				 result = true;
				 break;
			 }
		 }
		 return result;
	 }
	 
	 
	 /**
	  * 当前登录用户所属公司、部门信息
	  * @return
	  */
	/* public static Map<String, Object> getDeptMap() {
	        return (Map<String, Object>)SessionManager.getCurrentSession().getAttribute(Constants.SELECTDEPT_SNAPSHOT_KEYWORD);
	    }*/
}

