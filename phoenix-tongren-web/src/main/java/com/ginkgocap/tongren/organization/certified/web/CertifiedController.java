package com.ginkgocap.tongren.organization.certified.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.certified.entity.CertifiedView;
import com.ginkgocap.tongren.organization.certified.model.Certified;
import com.ginkgocap.tongren.organization.certified.service.CertifiedService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.sms.service.ShortMessageService;
import com.ginkgocap.ywxt.user.model.User;


/**
 * 考勤模板
 * @author hanxifa
 *
 */
@Controller
@RequestMapping("/certified") 
public class CertifiedController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(CertifiedController.class);
	
	@Autowired
	private CertifiedService certifiedService;
	
	@Autowired
	private ShortMessageService shortMessageService;
	
	@Autowired
	private Cache cache;
	
	@Autowired
	private FileIndexService fileIndexService;
	
	/***
	 * 申请组织认证
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/apply.json",method = RequestMethod.POST)
	public void apply(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"verifyCode|R","organizationId|R","fullName|R","introduction|R","organizationType|R","legalPerson|R","legalPersonMobile|R","logo|R","businessLicense|R","identityCard|R"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			String requestVerifyCode=params.getParam("verifyCode");
			if(containsCode(user.getId(),requestVerifyCode)==false){
				warpMsg(SysCode.PARAM_IS_ERROR,"验证吗不正确",params,response);	
				return;
			}
			
			Certified certified=new Certified();
			certified.setBusinessLicense(params.getParam("businessLicense"));
			certified.setFullName(params.getParam("fullName"));
			certified.setIdentityCard(params.getParam("identityCard"));
			certified.setIntroduction(params.getParam("introduction"));
			certified.setLegalPerson(params.getParam("legalPerson"));
			certified.setLegalPersonMobile(params.getParam("legalPersonMobile"));
			certified.setLogo(params.getParam("logo"));
			certified.setOperUserId(user.getUid());
			certified.setOrganizationId(Long.parseLong(params.getParam("organizationId")));
			certified.setOrganizationType(Integer.parseInt(params.getParam("organizationType")));
			certified.setCreateTime(new Timestamp(System.currentTimeMillis()));
			certified.setUpdateTime(certified.getCreateTime());
			certified.setStatus("1");
			String status=certifiedService.add(certified);
			if(status.charAt(0)=='1'){
				certified.setId(Long.parseLong(status.substring(2)));
				responseData.put("certified", warpToView(certified));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else if(status.equals("2")){
				warpMsg(SysCode.PARAM_IS_ERROR,"数据库中已经存在该组织对应的认证信息",params,response);		
				return ;
			}
			else if(status.equals("3")){
				warpMsg(SysCode.ERROR_CODE,"组织认证信息增加失败",params,response);		
				return ;
			}
		}catch(Exception e){
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			e.printStackTrace();
			return;
		}
	}
	
	
	/***
	 * 更新组织认证信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/update.json",method = RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"verifyCode|R","organizationId|R","fullName|R","introduction|R","organizationType|R","legalPerson|R","legalPersonMobile|R","logo|R","businessLicense|R","identityCard|R"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			String requestVerifyCode=params.getParam("verifyCode");
			if(containsCode(user.getId(),requestVerifyCode)){
				warpMsg(SysCode.PARAM_IS_ERROR,"验证吗不正确",params,response);	
				return;
			}
			Certified certified=new Certified();
			certified.setBusinessLicense(params.getParam("businessLicense"));
			certified.setFullName(params.getParam("fullName"));
			certified.setIdentityCard(params.getParam("identityCard"));
			certified.setIntroduction(params.getParam("introduction"));
			certified.setLegalPerson(params.getParam("legalPerson"));
			certified.setLegalPersonMobile(params.getParam("legalPersonMobile"));
			certified.setLogo(params.getParam("logo"));
			certified.setOperUserId(user.getUid());
			certified.setOrganizationId(Long.parseLong(params.getParam("organizationId")));
			certified.setOrganizationType(Integer.parseInt(params.getParam("organizationType")));
			certified.setCreateTime(new Timestamp(System.currentTimeMillis()));
			certified.setUpdateTime(certified.getCreateTime());
			certified.setStatus("1");//更新后状态自动变为待审核
			String status=certifiedService.modify(certified);
			if(status.equals("1")){
				responseData.put("certified", warpToView(certified));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
			}else{
				String[] resStrArr={"更新成功","数据库中不存在记录","当前状态不能更新","状态值只能为 1 2 3"};
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-1],params,response,responseData);
			}
		}catch(Exception e){
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			return;
		}
	}
	
	/***
	 * 更新组织认证状态
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/updateStatus.json",method = RequestMethod.POST)
	public void updateStatus(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"id|R","status|R"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			//1 更新成功 2 数据库中不存在记录 3 当前状态不能更新  4 状态值只能为 1 2 3
			String[] resStrArr={"更新成功","数据库中不存在记录","当前状态不能更新","状态值只能为 1 2 3"};
			String status=certifiedService.updateStatus(Long.parseLong(params.getParam("id")), Integer.parseInt(params.getParam("status")), user.getId());
			responseData.put("status", status);
			if("1".equals(status)){
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				return;
			}else{
				warpMsg(SysCode.ERROR_CODE,resStrArr[Integer.parseInt(status)-1],params,response,responseData);
			}
		}catch(Exception e){
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			e.printStackTrace();
			return;
		}
	}
	
	
	/***
	 * 申请组织认证
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/get.json",method = RequestMethod.POST)
	public void get(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"organizationId|R"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			params=ri.getParams();
			Certified certified=certifiedService.getByOrgId(Long.parseLong(params.getParam("organizationId")));
			
			if(certified!=null){
				responseData.put("certified", warpToView(certified));
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				return;
			}else{
				warpMsg(SysCode.BIGDATA_EMPTY,"没有找到组织认证信息",params,response);
			}
		}catch(Exception e){
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			e.printStackTrace();
			return;
		}
	}
	
	
	/**
	 * 获取验证吗
	 * @param request
	 * @param response
	 */
	@RequestMapping(value ="/sendVerifyCode.json",method = RequestMethod.POST)
	public void sendVerifyCode(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> notification = new HashMap<String, Object>();
		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = {"legalPersonMobile|R"};
		ParamInfo params=null;
		try{
			RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
			User user=ri.getUser();
			params=ri.getParams();
			String phone=params.getParam("legalPersonMobile");
			phone=phone.trim();
			boolean isVal=false;
			if(phone.length()==11){
				try{
					Long.parseLong(phone);
					isVal=true;
				}catch(Exception e){
				}
			}
			if(!isVal){
				warpMsg(SysCode.PARAM_IS_ERROR,"手机号各位错误！",params,response);		
				logger.info("手机号格式错误！request json "+request.getParameter("requestJson"));
				return ;
			}
			String vcode=genrateVerifyCode(user.getId());
			vcode="【金桐】你的 短信验证码为"+vcode+",有效期2分钟请及时验证";
			//1 发送成   0 发送失败  -1 下发号码不正确  -2 短信内容为空
			int rtype=shortMessageService.sendMessage(phone, vcode);
			logger.info("sendMessage rtype is "+rtype);
			if(1==rtype){
				//responseData.put("verifyCode", vcode);
				logger.info("verifyCode is "+vcode);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, notification)));
				return;
			}
			else if(-1==rtype){
				warpMsg(SysCode.PARAM_IS_ERROR,"下发号码不正确",params,response);		
			}else if(-2==rtype){
				warpMsg(SysCode.PARAM_IS_ERROR," 短信内容为空",params,response);		
			}else{
				warpMsg(SysCode.PARAM_IS_ERROR," 发送失败",params,response);		
			}
		}catch(Exception e){
			warpMsg(SysCode.SYS_ERR,SysCode.SYS_ERR.getMessage(),params,response);
			e.printStackTrace();
			return;
		}
	}
	private CertifiedView warpToView(Certified certified) throws Exception{
		if(certified==null){
			return null;
		}
		CertifiedView certifiedView=new CertifiedView();
		BeanUtils.copyProperties(certifiedView, certified);
		certifiedView.setBusinessLicenseUrl(getUrlByTaskId(certified.getBusinessLicense()));
		certifiedView.setIdentityCardUrl(getUrlByTaskId(certified.getIdentityCard()));
		certifiedView.setLogoUrl(getUrlByTaskId(certified.getLogo()));
		return certifiedView;
	}
	/**
	 * 根据taskId获取url信息
	 * @param taskId
	 * @return
	 */
	private String getUrlByTaskId(String taskId){
		List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
		if(list!=null&&list.size()>0){
			FileIndex fi=list.get(0);
			return FileInstance.FTP_FULL_URL.trim()+ fi.getFilePath()+ "/"+fi.getFileTitle();
		}
		return null;
	}
	/**
	 * 随机生成验证码 并放入redis
	 * @param key
	 * @return
	 */
	private String genrateVerifyCode(long userId)
	{
		String code=String.valueOf(System.currentTimeMillis()).substring(9);
		String cacheCey = RedisKeyUtils.getSessionIdKey(userId+"_verifyCode");
		cache.setByRedis(cacheCey, code, 60*2);//有效时间120秒
		return code;
	}
	/**
	 * 验证cache是否包含指定的验证码 
	 * @param key
	 * @return
	 */
	private boolean containsCode(long userId,String code)
	{
		String cacheCey = RedisKeyUtils.getSessionIdKey(userId+"_verifyCode");
		Object val=cache.getByRedis(cacheCey);
		if(val!=null){
			if(val.toString().equals(code)){
				cache.remove(cacheCey);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
