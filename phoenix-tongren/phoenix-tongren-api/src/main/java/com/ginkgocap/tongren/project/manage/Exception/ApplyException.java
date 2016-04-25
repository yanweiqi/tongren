package com.ginkgocap.tongren.project.manage.Exception;

public class ApplyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private String errorMessage;
	
	public ApplyException(String errorCode,String errorMessage){
		super(errorCode+","+errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ApplyException() {
		super();
	}

	public ApplyException(String message, Throwable cause) {
		super(message, cause);
		message=errorCode;
	}
	public ApplyException(String message) {
		super(message);
		message=errorCode;
	}
	 
	public ApplyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
