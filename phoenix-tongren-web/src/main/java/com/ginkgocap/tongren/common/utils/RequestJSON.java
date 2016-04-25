/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;



/**
 * 用户前台向后台传递的JSON对象
 * @author liny
 *
 */
public class RequestJSON implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5830929601291750929L;
	

	private String requestJson;
	private String mac;
	private Head head;
	private JSONObject body;
	
	public String toString() {
		return "mac:" + mac + ",head:" + head + ",body:" + body;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * @return the head
	 */
	public Head getHead() {
		return head;
	}

	/**
	 * @param head the head to set
	 */
	public void setHead(Head head) {
		this.head = head;
	}

	/**
	 * @return the body
	 */
	public JSONObject getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(JSONObject body) {
		this.body = body;
	}

	/**
	 * @return 返回 requestJson。
	 */
	public String getRequestJson() {
		return requestJson;
	}

	/**
	 * ---@param requestJson 要设置的 requestJson。
	 */
	public void setRequestJson(String requestJson) {
		this.requestJson = requestJson;
	}
	
	

}
