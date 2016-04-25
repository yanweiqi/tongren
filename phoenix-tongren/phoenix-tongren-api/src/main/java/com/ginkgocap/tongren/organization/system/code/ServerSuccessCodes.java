package com.ginkgocap.tongren.organization.system.code;


/**
 * @author yanweiqi
 */
public enum ServerSuccessCodes{
	
	OK("00200","OK","成功的"),
	Created("00201","Created","创建成功"),
	Accepted("00202","Accepted","已接受"),
	NonAuthoritativeInformation("00203","Non-AuthoritativeInformation","无消息"),
	NoContent("00204","No-Content","没有内容"),
	ResetContent("00205","Reset-Content","重置内容"),
	PartialContent("00206","Partial-Content","部分内容");

	private String code;
	private String message;
	private String description;
	
	private ServerSuccessCodes(String code, String message, String description) {
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