/*
 * 文件名： UndertakenException.java
 * 创建日期： 2015年11月18日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.task.exception;

import java.io.Serializable;


 /**
 *  承接项目异常处理
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月18日
 */
public class UndertakenException extends Exception implements Serializable{
	
	
	/**
	 * ---serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer errorCode = -1;
	
	/**
	 * @return 返回 errorCode。
	 */
	public int getErrorCode() {
		return errorCode;
	}

	public UndertakenException(Throwable cause) {
		super(cause);
	}

	public UndertakenException(String message, Throwable cause) {
		super(message, cause);
	}

	public UndertakenException(String message) {
		super(message);
	}

	
	public UndertakenException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
