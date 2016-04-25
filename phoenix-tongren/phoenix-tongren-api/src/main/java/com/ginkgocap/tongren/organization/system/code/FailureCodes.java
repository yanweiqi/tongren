package com.ginkgocap.tongren.organization.system.code;


/**
 * 错误代码
 * @author yanweiqi
 */
public enum FailureCodes{
	
	BadRequest("0000400","Bad Request","请求错误"),                            
	Unauthorized("0000401","Unauthorized ","未被授权的"),                      
	PaymentRequired("0000402","Payment Required ","付款要求"),                 
	ForBidden("00403","Forbidden","禁用的"),                                 
	NotFound("00404","Not Found","未找到"),                                                            
	NotAcceptable("00406","Not Acceptable ","不接受的"),                     
	ProxyAuthenticationRequired("00407","Proxy Authentication Required","需要代理身份认证"),
	RequestTimedOut("00408","Request Timed-Out","请求超时"),               
	Conflict("00409","Conflict ","冲突"),                                    
	Gone("00410","Gone ","离开的"),                                          
	LengthRequired("00411","Length Required","要求长度"),                    
	PreconditionFailed("00412","Precondition Failed","前提条件不正确"),      
	RequestEntityTooLarge("00413","Request Entity Too Large ","请求的实体太大"), 
	RequestUriTooLarge("00414","Request, URI Too Large ","请求的URI太大") ,  
	UnSupportedDataType("00415","Unsupported Data Type ","不支持的数据类型");

	private String code;
	private String message;
	private String description;
	
	private FailureCodes(String code, String message, String description) {
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