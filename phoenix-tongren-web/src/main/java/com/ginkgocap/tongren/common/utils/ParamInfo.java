/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ginkgocap.tongren.organization.system.code.SysCode;

/**
 * 请求参数解析信息
 * @author liny
 *
 */
public class ParamInfo {

	private final Logger logger  = LoggerFactory.getLogger(getClass());

    private SysCode state;

    private Map<String, String> params;

    private ResponseJSON response;

    private RequestJSON requestJSON;
    
    public boolean isLoginWithMobile(){
    	return "1".equals(getParam("clientType"));
    }


    public ParamInfo() {
        params = new HashMap<String, String>();
    }

    public String toString() {
        return "state:" + state + ",response:" + response + ",requestJSON" + requestJSON;
    }

    public String getParam(String key) {
        if (params != null && params.size() > 0) {
            return params.get(key);
        }
        return null;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public ResponseJSON getResponse() {
        return response;
    }

   
	@SuppressWarnings("unchecked")
	public ResponseJSON getResponse(SysCode code, Map<String,Object> responseData) {
		response = new ResponseJSON();
		response.setCode(code.getCode());
        response.setMessage(code.getMessage());
		if(null != responseData.get("responseData")) response.setResponseData((Map<String, Object>)responseData.get("responseData"));
		if(null != responseData.get("notification")) response.setNotification((Map<String, Object>)responseData.get("notification"));
        return response;
    }

    /**
     * 描述：〈参数错误时返回response〉 <br/>
     * 作者：linY <br/>
     * 生成日期：2015年1月20日 <br/>
     * 
     * @param param 附加回传信息，便于客户端判断哪个参数传错了
     * @return
     */
    public ResponseJSON getResponse(String param) {
    	response = new ResponseJSON();
    	response.setMessage(param);
        logger.info("the response json is :" + response);
        return response;
    }

    public void setResponse(ResponseJSON response) {
        this.response = response;
    }

    public RequestJSON getRequestJSON() {
        return requestJSON;
    }

    public void setRequestJSON(RequestJSON requestJSON) {
        this.requestJSON = requestJSON;
    }

    public SysCode getState() {
        return state;
    }

    public void setState(SysCode state) {
        this.state = state;
    }
}
