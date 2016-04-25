/**
 * Copyright (c) 2015 金桐网.
 * All Rights Reserved. 保留所有权利.
 */

package com.ginkgocap.tongren.security.service;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.ginkgocap.tongren.organization.user.entity.ShiroUser;
import com.ginkgocap.ywxt.user.model.User;

/**
 * shiro框架自定义DBRealm
 * 
 * @author yanweiqi
 *
 */
public class ShiroDbRealm extends AuthorizingRealm {
	private final Logger logger = Logger.getLogger(getClass());
	private User user;

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		logger.info("进行验证");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		ShiroUser shiroUser = new ShiroUser();
		token.setUsername(shiroUser.getLoginName());
		token.setPassword(shiroUser.getPassword().toCharArray());
		
		if(null != user){
		   String host = token.getHost();
		   logger.debug("登陆IP:"+host);
		   shiroUser.setName(user.getName());
		   SimpleAuthenticationInfo s = new SimpleAuthenticationInfo(shiroUser ,shiroUser.getPassword(),ByteSource.Util.bytes(shiroUser.getSalt()), shiroUser.getName());
		   return s;
		}
		else{
		  throw new DisabledAccountException();
		}
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.debug("查询用户授权");
		//ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		//TODO 根据UserId查询组织权限表
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
/*
		for(Role role: user.list()){
			// 基于Role的权限信息
			info.addRole(role.getName());
			// 基于Permission的权限信息
			info.addStringPermissions(role.getPermissionList());
		}
*/		
		return info;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
  

}
