/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户后台向前台返回的JSON对象
 * @author liny
 *
 */
public class ResponseJSON implements Serializable {

	private static final long serialVersionUID = 7307946461629795272L;
	
//	private String mac;
//	private Head head;
//	private Body body;
	private Map<String,Object> responseData;
	private Map<String,Object> notification;
	
	private String code;//返回编码
	private String message;//消息
	
	/**
	 * @return 返回 code。
	 */
	public String getCode() {
		return code;
	}

	/**
	 * ---@param code 要设置的 code。
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return 返回 message。
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * ---@param message 要设置的 message。
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return 返回 responseData。
	 */
	public Map<String, Object> getResponseData() {
		return responseData;
	}

	/**
	 * ---@param responseData 要设置的 responseData。
	 */
	public void setResponseData(Map<String, Object> responseData) {
		this.responseData = responseData;
	}

	/**
	 * @return 返回 notification。
	 */
	public Map<String, Object> getNotification() {
		return notification;
	}

	/**
	 * ---@param notification 要设置的 notification。
	 */
	public void setNotification(Map<String, Object> notification) {
		this.notification = notification;
	}

}
