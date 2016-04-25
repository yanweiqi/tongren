/*
 * 文件名： DeliveryService.java
 * 创建日期： 2015年11月19日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service;

import java.sql.Timestamp;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.model.Delivery;


 /**
 *  项目完成记录实体
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月19日
 */
public interface DeliveryService extends CommonService<Delivery> {

	/**
	 * 功能描述：  创建项目完成记录       
	 *                                                       
	 * @param delivererId 交付人ID
	 * @param deliveryOrganizationId 交付组织ID
	 * @param projectUndertakenId 项目承接ID
	 * @param deliverTime 交付时间
	 * @param status 0按期完成、1延期完成
	 * @param createUserId 项目创建人ID
	 * @param createOrganizationId 项目创建组织ID
	 * @return
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月19日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public Delivery create(long delivererId,long deliveryOrganizationId,long projectUndertakenId,Timestamp deliverTime,int status,long createUserId,long createOrganizationId)throws Exception;
}
