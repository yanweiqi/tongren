package com.ginkgocap.tongren.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ginkgocap.tongren.common.context.WebConstants;
import com.ginkgocap.tongren.common.utils.RedisKeyUtils;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.user.model.User;

public class SessionFilter implements Filter {
	
	private boolean isDebug = false;
	private String excludedURL = "";
	private static Set<String> excludedSet = new HashSet<String>();
	
	private Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	
	@Override
	public void destroy() {
	}

	/**
	 * 从用户的请求中获取sessionID
	 * @param request
	 * @return
	 */
	private User getUser(HttpServletRequest request,HttpServletResponse resp) {
		
		String sessionIdVaule = isDebug ? request.getParameter("sessionID") : request.getHeader("sessionID");
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("sessionID".equals(cookie.getName())){
				sessionIdVaule = cookie.getValue();
				break;
			}
		}
		if (StringUtils.isNotBlank(sessionIdVaule)) {
			Cookie sessionID = new Cookie("sessionID", sessionIdVaule);
			resp.addCookie(sessionID);
			resp.setHeader("sessionID", sessionIdVaule);
			String key = RedisKeyUtils.getSessionIdKey(sessionIdVaule);
			return getUser(request, key,sessionIdVaule);
		}
		else{
			return null;
		}
	}

	/**
	 * 获取用户信息
	 * @param request
	 * @param key
	 * @return user
	 * @author yanweiqi
	 */
	private User getUser(HttpServletRequest request, String key,String sessionID) {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		Cache cache = (Cache) wac.getBean("cache");
		User user = (User) cache.getByRedis(key);
		if (user != null) {
			cache.setByRedis(key, user, 60 * 60 * 24);
		}
		return user;
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		boolean isPass = true;
		HttpServletRequest req   = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;  
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String requestURL = req.getRequestURI();
		User user = getUser(req,resp);
		if (null != user) {
			HttpSession session = req.getSession(); 
			session.setAttribute(WebConstants.CURRENT_USER, user);
			logger.debug(user.getUserName()+"进入桐人!");
			isPass = true;
			User u = (User)session.getAttribute(WebConstants.CURRENT_USER);
			logger.debug(u.getName());
		}
		else if(isPassURL(requestURL)){
			isPass = true;
		}
		else{
			logger.debug("被拦截的URL:"+requestURL);
			response.getWriter().write("{\"notification\":{\"notifCode\": \"10000\",\"notifInfo\": \"用户未登录\"}}");
			return;
		}
		if(isPass) chain.doFilter(request, response);
	}

	private boolean isPassURL(String requestURL) {
		boolean isPass = false;
		for (String exceludeURL : excludedSet) {
		   if(requestURL.contains(exceludeURL)){
			  isPass = true;
			  break;
		   }
		}
		return isPass;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.isDebug = "true".equalsIgnoreCase(config.getInitParameter("debug"));
		this.excludedURL = config.getInitParameter("excludedURL") ;
		if(StringUtils.isNotEmpty(excludedURL) && excludedSet.isEmpty()){
		   String[] arrayExcluded = excludedURL.split(",");
		    excludedSet.addAll(Arrays.asList(arrayExcluded));
		}
	}

}
