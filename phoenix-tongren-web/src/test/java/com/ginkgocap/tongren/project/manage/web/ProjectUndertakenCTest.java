/*
 * 文件名： ProjectUndertakenCTest.java
 * 创建日期： 2015年12月7日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.web;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.ginkgocap.tongren.base.PostUtil;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年12月7日
 */
public class ProjectUndertakenCTest {

	private String cookie = "sessionID=\"d2ViMjI2ODE0NDk4MTI5Mjg5OTE=\"";
	//private String baseUrl = "http://localhost/";
	private String baseUrl = "http://192.168.101.131:6789/";
	
	@Test
	public void ss(){
		PostUtil pt = new PostUtil(cookie, baseUrl);
		Map<String, String> params = new HashMap<String, String>();
//		String URI = "/project/undertaken/postponeProject.json";
		params.put("organiztionId", "3915543052812338");
		params.put("projectId", "3915320670814233");
		params.put("cycle", "12");
		params.put("content", "12121");

		try {
			pt.testURI("project/undertaken/postponeProject.json", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
