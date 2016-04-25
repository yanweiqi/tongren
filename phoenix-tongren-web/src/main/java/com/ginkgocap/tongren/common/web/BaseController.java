/**
 * 
 */
package com.ginkgocap.tongren.common.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.utils.Body;
import com.ginkgocap.tongren.common.utils.Head;
import com.ginkgocap.tongren.common.utils.JsonUtils;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.tongren.common.utils.RequestJSON;
import com.ginkgocap.tongren.common.utils.ResponseJSON;
import com.ginkgocap.tongren.common.utils.ResponseUtils;
import com.ginkgocap.tongren.common.utils.RoUtil;
import com.ginkgocap.tongren.common.web.bean.LocalCacheBeanWrap;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.OrganizationMember;
import com.ginkgocap.tongren.organization.manage.service.OrganizationMemberService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.metadata.service.SensitiveWordService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

/**
 * 基础controller
 * @author liny
 *
 */
public class BaseController {

	private final Logger logger  = LoggerFactory.getLogger(getClass());
	
	protected SysCode returnCode;//客户端返回代码
	protected String returnResult;//返回结果
	
	@Autowired
	private OrganizationMemberService organizationMemberService;

	@Autowired
	private FileIndexService fileIndexService;
	
	private static Map<String, String> taskCacheMap = Collections.synchronizedMap(new LRUMap(5000));
	
	@Autowired
	private SensitiveWordService sensitiveWordService;
	
	@Autowired
	private UserService userService;//用户接口
	
	/**
     * 构建输出到终端JSON 串HEAD <br/>
     * 
     * @param [method]-[后台调用方法名] <br/>
     * @param [sNumber]-[序列号] <br/>
     * @param [version]-[版本号] <br/>
     * @return [Head] 输出到终端JSON 串 HEAD<br/>
     */
    protected Head getResponseHead(String method, String sNumber, String version) {
        return new Head(method, sNumber, version);
    }
    
    /**
     * 构建输出到终端JSON 串BODY <br/>
     * @param [SysCode]-[返回消息消息码 和消息] <br/>
     * @param [result]-[返回JSON 结果] <br/>
     * @return [Body] 输出到终端JSON串 BODY<br/>
     */
    protected Body getResponseBody(SysCode code, Object responseData) {
        Body body = new Body();
        body.setMessage(code.getMessage());
        body.setCode(code.getCode());
        body.setResponseData(responseData);
        return body;
    }
    /**
     * 构建返回给前端的JSON对象 <br/>
     * 
     * @param [mac]-[加密信息] <br/>
     * @param [method]-[后台调用方法] <br/>
     * @param [sNumber]-[序列号] <br/>
     * @param [version]-[版本号] <br/>
     * @param [code]-[返回消息消息码和消息] <br/>
     * @param [result]-[返回结果] <br/>
     * @return [ResponseJSON] 返回给前段JSON对象<br/>
     */
    protected ResponseJSON getResponseJSON(String method,SysCode code, Object result) {
        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setCode(code.getCode());
        responseJSON.setMessage(code.getMessage());
        return responseJSON;
    }
 
    public ParamInfo parseRequest(HttpServletRequest request,HttpServletResponse response, String method, String[] paramKey) throws ValiaDateRequestParameterException {
    	ParamInfo info = new ParamInfo();
    	String jsonParam = request.getParameter("requestJson");
    	
    	if("mobile".equals(request.getAttribute("accessType"))){
    		logger.info("parse with mobile");
    		if(request.getAttribute("jsonParam")==null){
    			jsonParam =  parseFromRequest(request);
    			request.setAttribute("jsonParam", jsonParam);
    		}else{
    			jsonParam=(String) request.getAttribute("jsonParam");
    		}
    		info.getParams().put("clientType", "1");//表示app端访问
    	}
    	
    	if(jsonParam == null){
    		return info;
    	}
        logger.info("parseRequest method:" + method + "; jsonParam:" + jsonParam);
        String jsonParamFZ = "{\"body\":"+jsonParam+"}";
        try {
        	RequestJSON requestJSON = JSON.parseObject(jsonParamFZ, RequestJSON.class);
        	info.setRequestJSON(requestJSON);
        	if (paramKey != null && paramKey.length > 0) {
                for (int i = 0; i < paramKey.length; i++) {
                	String key = paramKey[i];
                    String value = requestJSON.getBody().getString(key);
                    if (!RoUtil.isEmpty(value)) {
                        info.getParams().put(key, value);
                        logger.info("text-- the key:" + key + " ,value :" + value );
                    } else {
                        logger.warn("warn-- the key:" + key + " is empty or is null!");
                    }
                }
            }
		} 
        catch (Exception e) {
        	logger.error("parseRequest failed!",e);
			info.setResponse(getResponseJSON(method,SysCode.ERROR_CODE, SysCode.ERROR_CODE.getMessage()));
			info.setState(SysCode.ERROR_CODE);
			throw new ValiaDateRequestParameterException(request, response, SysCode.ERROR_CODE.getCode(), SysCode.ERROR_CODE.getMessage());
		}
        info.setResponse(getResponseJSON(method,SysCode.SUCCESS, null));
        info.setState(SysCode.SUCCESS);
        logger.info("builder parameters SUCCESS !");
        return info;
    }
        
    /**
     * 描述：〈响应输出内容，包含跨域处理〉 <br/>
     * 
     * @param request 请求
     * @param response 响应
     * @param sysCode 系统代码
     * @param content JSON对象
     */
    protected void renderJson(HttpServletRequest request, HttpServletResponse response, SysCode sysCode, Object content,ResponseJSON responseJson) {
        renderJson(request, response, sysCode, content, null,responseJson);
    }
    
    protected void renderJson(HttpServletRequest request, HttpServletResponse response, SysCode sysCode,
            Object content, Map<Class<?>, String[]> includes,ResponseJSON responseJson) {
        renderJson(request, response, sysCode, content, includes, null,responseJson);
    }
    
    protected void renderJson(HttpServletRequest request, HttpServletResponse response, SysCode sysCode,
            Object content, Map<Class<?>, String[]> includes, Map<Class<?>, String[]> excludes,ResponseJSON responseJson) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", sysCode.getCode());
        result.put("message", sysCode.getMessage());
        result.put("result", content);
        
        String callback = request.getParameter("callback");
        if (StringUtils.isNotEmpty(callback)) {
            ResponseUtils.renderText(response, callback + "(" + JSON.toJSON(result) + ");",responseJson);
        } else {
            ResponseUtils.renderJson(response, JsonUtils.toJsonString(result, includes, excludes, false),responseJson);
        }
    }

    /**
     * 描述：〈响应输出内容，包含跨域处理〉 <br/>
     * 
     * @param request 请求
     * @param response 响应
     * @param content JSON对象
     */
    protected void renderJson(HttpServletRequest request, HttpServletResponse response, Object content,ResponseJSON responseJson) {
        renderJson(request, response, content, null, null,responseJson);
    }
    
    protected void renderJson(HttpServletRequest request, HttpServletResponse response, Object content,
            Map<Class<?>, String[]> includes, Map<Class<?>, String[]> excludes,ResponseJSON responseJson) {
        String callback = request.getParameter("callback");
        if (StringUtils.isNotEmpty(callback)) {
            ResponseUtils.renderText(response, callback + "(" + JsonUtils.toJsonString(content, includes, excludes, true) + ");",responseJson);
        } else {
            ResponseUtils.renderJson(response, JsonUtils.toJsonString(content, includes, excludes, false),responseJson);
        }
    }
    
    /**
     * 描述：〈生成响应的Json内容〉 <br/>
     * 
     * @param response 请求
     * @param responseJson ResponseJSON对象
     */
    public void renderResponseJson(HttpServletResponse response, ResponseJSON responseJson) {
        ResponseUtils.renderJson(response, JsonUtils.toJsonString(responseJson, false),responseJson);
    }

    /**
     * 描述：〈生成响应的Json内容〉 <br/>
     * 
     * @param response 请求
     * @param responseJson 响应JSON对象
     * @param includes 包含属性
     */
    protected void renderResponseJson(HttpServletResponse response, ResponseJSON responseJson, Map<Class<?>, String[]> includes) {
        renderResponseJson(response, responseJson, includes, null);
    }
    /**
     * 
     * @return
     */
    public List<OrganizationMember> getOrganizationMember(HttpServletResponse response,HttpServletRequest request){
    	User user = getUser(response, request);
    	List<OrganizationMember> organizationMembers = organizationMemberService.getMyOrganizationMembers(user.getId());
    	return organizationMembers;
    }
    
    /**
     * 描述：〈生成响应的Json内容〉 <br/>
     * 
     * @param response 请求
     * @param responseJson 响应JSON对象
     * @param includes 包含属性
     * @param excludes 排除属性
     */
    protected void renderResponseJson(HttpServletResponse response, ResponseJSON responseJson,
            Map<Class<?>, String[]> includes, Map<Class<?>, String[]> excludes) {
        ResponseUtils.renderJson(response, JsonUtils.toJsonString(responseJson, includes, excludes, false),responseJson);
    }

    /**
     * 描述：获得IP〉 <br/>
     * @param request
     * @return
     */
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public User getUser(HttpServletResponse response,HttpServletRequest request){
    	String rkey ="REQ_USER";
    	User reqUser= (User) request.getAttribute(rkey);
    	if(reqUser!=null){
    		return reqUser;
    	}
		Cookie[] cookies = request.getCookies();
		String sessionIdVaule = "";
		if(StringUtils.isBlank(sessionIdVaule)&&request.getAttribute("sessionID_Param")!=null){
			sessionIdVaule = (String) request.getAttribute("sessionID_Param");
			logger.info("attribute is sessionID:"+sessionIdVaule);
		}
		if(StringUtils.isBlank(sessionIdVaule)) {
			sessionIdVaule = request.getHeader("sessionID");
			logger.info("from header is sessionID:"+sessionIdVaule);
		}
		if(StringUtils.isBlank(sessionIdVaule)){
			for (Cookie cookie : cookies) {
				if("sessionID".equals(cookie.getName())){
					sessionIdVaule = cookie.getValue();
					break;
				}
			}
			logger.info("from cookie is sessionID:"+sessionIdVaule);
		}
		request.getSession().setAttribute("sessionId",sessionIdVaule);
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		Cache cache = (Cache) wac.getBean("cache");
		String key = RedisKeyUtils.getSessionIdKey(sessionIdVaule);
		User user = (User) cache.getByRedis(key);
        if(user != null){
        	request.setAttribute(rkey, user);
        	return user;
        }else{
        	request.setAttribute(rkey, user);
        	user = (User) cache.getByRedis("user" + sessionIdVaule);
        	if(user!=null){
        		logger.info("access with mobile "+sessionIdVaule+",user:"+user);
        		request.setAttribute("accessType", "mobile");
        	}
        	return user;
        }
    }

    
    
	/**
	 * 讲notification统一包装起来
	 * 
	 * @param model
	 *            讲要返回给客户端的model对象
	 * @param responseDataMap
	 *            协议的消息体部分， 对应 responseData
	 * @param notificationMap
	 *            协议的消息部分， 对应 notification
	 */
	public Map<String, Object> genResponseData(Map<String, Object> responseDataMap,Map<String, Object> notificationMap) {
		if (notificationMap == null) {
			notificationMap = new HashMap<String, Object>();
			notificationMap.put("notifCode", "");
			notificationMap.put("notifInfo", "");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("responseData", responseDataMap);
		model.put("notification", notificationMap);
		return model;
	}
	
	
	/**
	 * 验证用户和参数是否为空
	 * @param request
	 * @param response
	 * @param paramsKey
	 * @return
	 * @throws Exception
	 */
	protected RequestInfo validate(HttpServletRequest request, HttpServletResponse response,String paramsKey[]) throws Exception{
		RequestInfo ri = new RequestInfo();
		User user = validateUserIsLogin(request,response);
		if(user == null){
			 return null;
		}else{
			ri.setUser(user);
		}
		ParamInfo params = validateParams(request,response,paramsKey);
		if(params == null){
			return null;
		}else{
			ri.setParams(params);
		}
		return ri;
		
	}
	
	/**
	 * 功能描述：参数验证判断
	 * 用法举例{"name|R|L50|F|N|S"}
	 * 参数名称name，R必填选项，L是长度效验，F敏感词效验，N数值效验，S特殊字符效验
	 * @param request
	 * @param response
	 * @param paramsKey
	 * @return
	 * @throws ValiaDateRequestParameterException
	 */
	protected ParamInfo validateParams(HttpServletRequest request, HttpServletResponse response,String paramsKey[]) throws ValiaDateRequestParameterException{
		if(paramsKey == null) return new ParamInfo();
		String[] pkeys = new String[paramsKey.length];
		for(int i=0;i<paramsKey.length;i++){
			int index = paramsKey[i].indexOf("|");
			if(index > 0){
				pkeys[i] = paramsKey[i].substring(0,index);
			}else{
				pkeys[i] = paramsKey[i];
			}
		}
		ParamInfo params = parseRequest(request,response,"validateParams",pkeys);
	    String vstr = validateEmpty(params,paramsKey);
	    if(vstr.length() > 0){
	    	logger.info("validate failed! "+vstr);
	    	Map<String, Object> notification = new HashMap<String, Object>();
	    	notification.put("notifyCode", SysCode.PARAM_IS_ERROR);
			notification.put("notifyMessage",vstr);
			renderResponseJson(response, params.getResponse(SysCode.PARAM_IS_ERROR, genResponseData(null, notification)));
			return null;
	    }
	    return params;
	}
	/**
	 * 空值判断
	 * @param params
	 * @param paramsKey
	 * @return
	 */
	protected String validateEmpty(ParamInfo params, String paramsKey[]){
		StringBuilder sb = new StringBuilder();
		for(String key:paramsKey){
			int index = key.indexOf("|");
			if(index < 0){
				continue;
			}
			String[] array = key.split("\\|");
			String rk = array[0];
			for(int i=1;i<array.length;i++){
				String rn  = array[i].toUpperCase();
				String str = null;
				if(rn.startsWith("R")){
					str = validateRequire(params,rk,rn);
				}
				else if(rn.startsWith("L")){//验证长度
					str = validateLen(params,rk,rn);
				}
				else if(rn.startsWith("N")){
					str = validateNumber(params,rk,rn);
				}
				else if(rn.startsWith("F")){
					str = validateSensitiveKeyword(params,rk,rn);
				}
				else if(rn.startsWith("S")){
					str = validateSpecialCharacters(params,rk,rn);
				}
				if(str != null){
					sb.append(str).append(",");
					break;
				}
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private String validateSpecialCharacters(ParamInfo params, String key, String rn) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		String value = params.getParam(key);
		Matcher m = p.matcher(value);
		if(m.find()){
		   return null;
		}
		else{
			return value+" Includes Special Characters";	
		}
	}

	private String validateSensitiveKeyword(ParamInfo params, String key, String rn) {
		String value = params.getParam(key);
		List<String> keywords = sensitiveWordService.sensitiveWord(value);
		if(keywords.size() > 0){
			StringBuilder sb = new StringBuilder("This is "+key+" sensitive keyword ");
			for (String word: keywords) {
				sb.append("["+word+"] ");
			}
			return sb.toString();
		}
		else{
			logger.info("validate sensitive Keyword:"+key+" success");
			return null;
		}
	}

	private String validateNumber(ParamInfo params, String key, String rn) {
		if(rn.equalsIgnoreCase("N")){
		   String value = params.getParam(key);
		   try {
			   Double.valueOf(value);
		   } catch (NumberFormatException e) {
			   return  key+" must be a number,"+value +" is forbidden character";
		   }
		}
		return null;
	}

	private String validateRequire(ParamInfo params,String key,String rn){
		boolean isRequre = false;
		if(rn.equalsIgnoreCase("R")){
			isRequre = true;
		}else if (rn.equalsIgnoreCase("RP")&&!params.isLoginWithMobile()){//RP表示web端必填,并且当前非web端登陆
			isRequre = true;
		}else if(rn.equalsIgnoreCase("RM")&&params.isLoginWithMobile()){//RM表示app端登陆必须填写
			isRequre = true;
		}
		if(isRequre){
			String value = params.getParam(key);
			if(value==null || value.trim().length()==0){
				StringBuilder sb = new StringBuilder();
				sb.append(key).append(" is null");
				return sb.toString();
			}
		}
		return null;
	}
	/**
	 * 验证长度
	 * @param params
	 * @param key
	 * @param rn
	 * @return
	 */
	private String validateLen(ParamInfo params, String key, String rn) {
		int len = Integer.parseInt(rn.substring(1).trim());
		if (params.getParam(key) != null && params.getParam(key).length() > len) {
			return "the lenth of " + key + " must less than " + len;
		}
		return null;
	}
	/**
	 * 验证用户是否登陆
	 * @param request
	 * @param response
	 */
	public User validateUserIsLogin(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = new ParamInfo();
		User user = getUser(response, request);
		if(user == null){
			notification.put("notifyCode", SysCode.USER_NOLOGIN.getCode());
			notification.put("notifyMessage", SysCode.USER_NOLOGIN);
            renderResponseJson(response, params.getResponse(SysCode.USER_NOLOGIN, genResponseData(null, notification)));
			logger.info("Permission authentication failed",SysCode.USER_NOLOGIN);
		}
		return user;
	}
	
	/**
	 * 包装消息体
	 * @param code
	 * @param msg
	 * @param pi
	 * @param rsp
	 */
	public void warpMsg(SysCode code,String msg,ParamInfo pi,HttpServletResponse rsp){
		logger.info("code "+code.getCode()+" msg:"+msg+" requestInfo:"+pi.getParams());
		Map<String, Object> notification = new HashMap<String, Object>();
		notification.put("notifyCode", code.getCode());
		notification.put("notifyMessage", msg);
		renderResponseJson(rsp, pi.getResponse(code, genResponseData(null, notification)));
	}
	protected void warpMsg(SysCode code,String msg,ParamInfo pi,HttpServletResponse rsp,Map<String, Object> responseData){
		logger.info("code "+code.getCode()+" msg:"+msg+" requestInfo:"+pi.getParams());
		Map<String, Object> notification = new HashMap<String, Object>();
		notification.put("notifyCode", code.getCode());
		notification.put("notifyMessage", msg);
		renderResponseJson(rsp, pi.getResponse(code, genResponseData(responseData, notification)));
	}
	/**
	 * 根据taskId获取url信息
	 * @param taskId
	 * @return
	 */
	protected String getPathByTaskId(String taskId) throws Exception{
		if(taskId==null){
			return "";
		}
		String url=taskCacheMap.get(taskId);
		if(url!=null){
			return url;
		}
		List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
		String path ="";
		if(list!=null&&list.size()>0){
			FileIndex s = (FileIndex) list.get(0);
			path = s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath()+"/"+s.getFileTitle();
		}
		taskCacheMap.put(taskId, path);
		return path;
	}
	
	/**
	 * 根据taskid获取附件名称
	 * @param taskId
	 * @return
	 */
	protected String getFileNameByTaskId(String taskId){
		if(taskId==null){
			return "";
		}
		String cacheId="f_"+taskId;
		String name=taskCacheMap.get(cacheId);
		if(name==null){
			List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
			if(list!=null&&list.size()>0){
				FileIndex s = (FileIndex) list.get(0);
				if(s!=null){
					name=s.getFileTitle();
				}
			}
			if(name==null){
				name="";
			}
			taskCacheMap.put(cacheId, name);
		}
		
		return name;
	}
	private String parseFromRequest(ServletRequest req) {
		byte[] readBuffer = new byte[1024*1024*2];// 读取缓存区
		String jsonStr = null;
		// 读取请求字符流
		try {
			int len = req.getInputStream().read(readBuffer);
			if (len > 0) {
				// 删除末尾的特殊字符
				jsonStr = new String(readBuffer, 0, len, "utf-8");
				logger.info("mobile req str is:"+jsonStr);
			} else {
				logger.warn("request is empty,has been ignored");
			}
			return jsonStr;
		} catch (Exception e) {
			logger.error("read json from inputstream faild !", e.getMessage());
			return null;
		}
	}

	/**
	 * 返回用户头像
	 * @param u
	 * @return
	 */
	protected String getUserPicURL(User u){
		if(u == null){
			return FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC;
		}else{
			if(!StringUtils.isEmpty(u.getPicPath())){
				return FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + u.getPicPath();
			}else{
				logger.info("user picpath is null " + u.getId()+","+u.getName());
				return FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC;
			}
		}
	}
	
	/**
	 * 功能描述： 验证图片格式
	 * 
	 * @return
	 * @throws Exception
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月5日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	protected boolean isPicture(String fileName) throws Exception {
		fileName = fileName.toLowerCase();
		String reg = ".+(.jpeg|.jpg|.gif|.bmp|.png|.ico|.tiff|.tif|.dib)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(fileName.toLowerCase());
		return matcher.find();
	}

	/**
	 * 功能描述： 验证文件格式
	 * 
	 * @return
	 * @throws Exception
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月5日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	protected boolean isObject(String fileName) throws Exception {
		fileName = fileName.toLowerCase();
		String reg = ".+(.txt|.doc|.docx|.pdf|.xlsx|.xls)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(fileName.toLowerCase());
		return matcher.find();
	}
	
	/**
	 * 根据id获取用户名称
	 */
	protected String getUserNameById(long userId){
		User user= getUserById(userId);
		if(user==null){
			logger.warn("not found user info by id "+userId);
			return null;
		}else{
			return user.getName();
		}
	}
	
	/**
	 * 根据id获取用户对象
	 * @param userId
	 * @return
	 */
	Map<Long, LocalCacheBeanWrap<User>> userCache=new ConcurrentHashMap<Long, LocalCacheBeanWrap<User>>();
	protected User getUserById(long userId){
		if(userId>0){
			LocalCacheBeanWrap<User> lcb=userCache.get(userId);
			if(lcb==null||lcb.isValid()==false){
				User user= userService.selectByPrimaryKey(userId);
				lcb=new LocalCacheBeanWrap<User>(user,30*60*1000);
				userCache.put(userId, lcb);
			}
			return lcb.getBean();
		}else{
			return null;
		}
	}
}


