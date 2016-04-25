/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.ginkgocap.tongren.organization.user.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.ywxt.user.model.User;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
public class LoginController extends BaseController{

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView loginToIndex(HttpServletRequest request, HttpServletResponse response) {
		User user = getUser(response, request);
		Map<String, String> map = new HashMap<String, String>();
		map.put(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, user.getName());
		return new ModelAndView("index",map);
	}

}
