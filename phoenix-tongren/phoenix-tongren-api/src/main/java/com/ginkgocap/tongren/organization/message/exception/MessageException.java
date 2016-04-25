/*
 * 文件名： MessageException.java
 * 创建日期： 2015年11月17日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.message.exception;


 /**
 * messageException异常处理类
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月17日
 */
public class MessageException extends Exception {

	private int errorCode = -1;
	
	/**
	 * @return 返回 errorCode。
	 */
	public int getErrorCode() {
		return errorCode;
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7821388402674737444L;

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	
	public MessageException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
