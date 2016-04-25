package com.ginkgocap.tongren.organization.system.code;


/**
 * @author yanweiqi
 */
public enum ServerErrorCodes{
	
	InternalServerError("00500","Internal Server Error","内部服务器错误"),         
	NotImplemented("00501","Not Implemented ","为实现"),                           
	BadGateway("00502","Bad Gateway","无效网关"),                                  
	ServerUnavailable("00503","Server Unavailable","服务器错误"),                  
	GatewayTimedOut("00504","Gateway Timed-Out ","网关超时"),                      
	HttpVersionNotSupported("00505","HTTP Version not supported","不支持http版本");

	private String code;
	private String message;
	private String description;
		
	private ServerErrorCodes(String code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}