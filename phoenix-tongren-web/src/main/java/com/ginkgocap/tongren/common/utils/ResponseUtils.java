/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * HttpServletResponse 帮助类
 * @author liny
 *
 */
public class ResponseUtils {
	
	 public static final Logger log = Logger.getLogger(ResponseUtils.class);
	 
	 /**
	     * 描述：〈发送文本，使用UTF-8编码〉 <br/>
	     * 生成日期：2014-1-10 <br/>
	     * 
	     * @param response HttpServletResponse
	     * @param text 文本内容
	     */
	    public static void renderText(HttpServletResponse response, String text,ResponseJSON responseJson) {
	        render(response, "text/plain;charset=UTF-8", text,responseJson);
	    }

	    /**
	     * 描述：〈发送Json，使用UTF-8编码〉 <br/>
	     * 生成日期：2014-1-10 <br/>
	     * 
	     * @param response HttpServletResponse
	     * @param text Json文本
	     */
	    public static void renderJson(HttpServletResponse response, String text,ResponseJSON responseJson) {
	        render(response, "application/json;charset=UTF-8", text,responseJson);
	    }

	    /**
	     * 描述：〈发送xml，使用UTF-8编码〉 <br/>
	     * 生成日期：2014-1-10 <br/>
	     * 
	     * @param response HttpServletResponse
	     * @param text Xml文本
	     */
	    public static void renderXml(HttpServletResponse response, String text,ResponseJSON responseJson) {
	        render(response, "text/xml;charset=UTF-8", text,responseJson);
	    }

	    /**
	     * 描述：〈发送内容，使用UTF-8编码〉 <br/>
	     * 生成日期：2014-1-10 <br/>
	     * 
	     * @param response HttpServletResponse
	     * @param contentType contentType
	     * @param text 内容
	     */
	    public static void render(HttpServletResponse response, String contentType, String text,ResponseJSON responseJson) {
	        response.setContentType(contentType);
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);
	        try {
	        	if(responseJson.getCode() != "000000"){
	        		response.setHeader("errorCode", "-1");
		            response.setHeader("errorMessage", Encodes.encodeBase64(responseJson.getMessage().getBytes()));
	        	}else{
	        		response.setHeader("errorCode", "0");
		            response.setHeader("errorMessage", Encodes.encodeBase64(responseJson.getMessage().getBytes()));
	        	}
	            response.getWriter().write(text);
	            
	        } catch (IOException e) {
	            log.error(e.getMessage(), e);
	        }
	    }
}
