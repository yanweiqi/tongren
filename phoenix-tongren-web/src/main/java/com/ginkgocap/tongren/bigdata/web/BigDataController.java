/*
 * 文件名： BigDataController.java
 * 创建日期： 2015年10月13日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.bigdata.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ginkgocap.tongren.bigdata.vo.BigDataReturnJsonVO;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.HttpClientHelper;


 /**
 *  大数据控制层
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月13日
 */
@Controller
@RequestMapping("/data")
public class BigDataController extends BaseController {
	private final Logger logger  = LoggerFactory.getLogger(getClass());//日志记录对象
	
	public static final String BIG_INTERFACE_URL_TEST = "http://192.168.101.41:8090";//大数据接口测试URL
//	public static final String BIG_INTERFACE_URL_ONLINE = "http://123.59.74.94:8090";//大数据接口正式环境URL
	
	/**
	 * 功能描述：   获得大数据推荐金桐用户
	 *                                                       
	 * @param request
	 * @param response                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @throws ValiaDateRequestParameterException 
	 * @since 2015年10月13日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@RequestMapping(value = "/getRecommendedUser.json")
	public void getRecommendedUser(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		ParamInfo params = null;
		String[] paramKey = {"pageNo|R","pageSize|R"};
		try {
			RequestInfo requestInfo = validate(request, response, paramKey);
			if(null == requestInfo) return;
			params = requestInfo.getParams();
			User user = requestInfo.getUser();
			String pageNo = params.getParam("pageNo");
			String pageSize = params.getParam("pageSize");
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			pairs.add(new BasicNameValuePair("keywords", user.getCity()));//地区（市级别）可多选，按照‘，’隔开
			pairs.add(new BasicNameValuePair("max", pageSize));//最多返回条数
			pairs.add(new BasicNameValuePair("page", pageNo));//当前页数
			String responseJson = HttpClientHelper.post(BIG_INTERFACE_URL_TEST+ "/API/recommend.do", pairs);
			List<BigDataReturnJsonVO> bigDataPeoples = new ArrayList<BigDataReturnJsonVO>();
			bigDataPeoples = getPeoplesList(responseJson);
			if (bigDataPeoples == null || bigDataPeoples.size() == 0){ 
				notification.put("notifyCode", SysCode.BIGDATA_EMPTY.getCode());
				notification.put("notifyMessage", SysCode.BIGDATA_EMPTY.getMessage());
	            renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
				return;
			}
			responseData.put("userId", bigDataPeoples);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			return;
			
		} catch (Exception e) {
			notification.put("notifyCode", SysCode.SYS_ERR.getCode());
			notification.put("notifyMessage", SysCode.SYS_ERR.getMessage());
            renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
			logger.error("/data/getRecommendedUser.json is failed",e.getMessage());
			return;
		}
		
	
	}
	/**
	 * 功能描述：  人数据json解析
	 * @param responseJson
	 * @return list                                                                                                
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月15日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public List<BigDataReturnJsonVO> getPeoplesList(String responseJson){
		BigDataReturnJsonVO bigDataVO = null;
		List<BigDataReturnJsonVO> maps = new ArrayList<BigDataReturnJsonVO>();
		if (responseJson != null && responseJson.length() > 0) {
			JSONObject jo = JSONObject.fromObject(responseJson);
			JSONArray jas = JSONArray.fromObject(jo.get("users"));
			if (jas != null) {
				for (int i = 0; i < jas.size(); i++) {
					JSONObject datas = (JSONObject) jas.get(i);
					JSONArray  data  = JSONArray.fromObject(datas.get("datas"));
					for (int j = 0; j < data.size(); j++) {
						JSONObject ob = JSONObject.fromObject(data.get(j));
						bigDataVO = new BigDataReturnJsonVO();
						bigDataVO.setName(ob.optString("name"));//名称
						if(ob.optString("pic_path") == null || ob.optString("pic_path").equals("")){
							bigDataVO.setPath("");//头像URL
						}else{
							bigDataVO.setPath(FileInstance.FTP_WEB+FileInstance.FTP_URL+ob.optString("pic_path"));//头像URL
						}
						
						bigDataVO.setId(Long.parseLong(ob.optString("id")));//用户ID
						maps.add(bigDataVO);
					}
				}
			}
		}
		return maps;
	}

}
