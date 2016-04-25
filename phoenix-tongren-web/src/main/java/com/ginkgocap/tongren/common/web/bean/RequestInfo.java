package com.ginkgocap.tongren.common.web.bean;

import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.ywxt.user.model.User;

public class RequestInfo {
	private User user;
	private ParamInfo params;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ParamInfo getParams() {
		return params;
	}
	public void setParams(ParamInfo params) {
		this.params = params;
	}
	
}
