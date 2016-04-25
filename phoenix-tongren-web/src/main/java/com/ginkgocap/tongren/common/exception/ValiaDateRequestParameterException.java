package com.ginkgocap.tongren.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ginkgocap.tongren.common.utils.Encodes;

public class ValiaDateRequestParameterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errCode;
	private String errMessage;
	
	
    public ValiaDateRequestParameterException(String message) {
		super(message);
	}

    public ValiaDateRequestParameterException(HttpServletRequest request,HttpServletResponse response, String errCode, String errMessage){
    	super();
		response.setHeader("errorCode", errCode);
		response.setHeader("errorMessage",Encodes.encodeBase64(errMessage.getBytes()));
		this.errCode=errCode;
		this.errMessage=errMessage;
	
    }

	/**
	 * @return 返回 errCode。
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * @return 返回 errMessage。
	 */
	public String getErrMessage() {
		return errMessage;
	}
    
    
}
