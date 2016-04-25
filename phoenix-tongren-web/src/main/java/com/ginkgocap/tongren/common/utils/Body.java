/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

import java.io.Serializable;

/**
 * 返回前端JSON对象Body部分
 * @author liny
 * 
 */
public class Body implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1962564838103704183L;
	
	private String code;
	private String message;
	private Object responseData;
	
	public String toString() {
		return "code:" + code + ", message:" + message + ", responseData:" + responseData;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return responseData;
	}

	/**
	 * @param result the result to set
	 */
	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
	
	
}
