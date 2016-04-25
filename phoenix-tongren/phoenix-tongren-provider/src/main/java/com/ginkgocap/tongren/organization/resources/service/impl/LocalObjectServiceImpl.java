/*
 * 文件名： LocalObjectServiceImpl.java
 * 创建日期： 2015年10月29日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.organization.resources.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.resources.model.LocalObject;
import com.ginkgocap.tongren.organization.resources.service.LocalObjectService;


 /**
 *  本地资源文件实现接口类
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月29日
 */
@Service("localObjectService")
public class LocalObjectServiceImpl extends AbstractCommonService<LocalObject> implements LocalObjectService{

	@Override
	protected Class<LocalObject> getEntity() {
		return LocalObject.class;
	}

	@Override
	public Map<String, Object> doWork() {
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		return null;
	}

	@Override
	public String doError() {
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		return null;
	}

}
