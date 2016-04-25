/*
 * 文件名： DeliveryServiceImpl.java
 * 创建日期： 2015年11月19日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service.impl;

import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.project.manage.model.Delivery;
import com.ginkgocap.tongren.project.manage.service.DeliveryService;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月19日
 */
@Service("deliveryService")
public class DeliveryServiceImpl extends AbstractCommonService<Delivery> implements DeliveryService {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);


	@Override
	public Delivery create(long delivererId, long deliveryOrganizationId,
			long projectUndertakenId, Timestamp deliverTime, int status,
			long createUserId, long createOrganizationId) throws Exception {
		Delivery returnDelivery = null;
		try {
			Delivery t = new Delivery();
			t.setCreateOrganizationId(createOrganizationId);
			t.setCreateUserId(createUserId);
			t.setDelivererId(delivererId);
			t.setDeliverTime(deliverTime);
			t.setDeliveryOrganizationId(deliveryOrganizationId);
			t.setProjectUndertakenId(projectUndertakenId);
			t.setStatus(status);
			returnDelivery = save(t);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return returnDelivery;
	}

	@Override
	protected Class<Delivery> getEntity() {
		return Delivery.class;
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
