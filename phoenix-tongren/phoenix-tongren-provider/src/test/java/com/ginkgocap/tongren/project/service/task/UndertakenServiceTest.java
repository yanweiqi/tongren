/*
 * 文件名： UndertakenServiceTest.java
 * 创建日期： 2015年11月12日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.service.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.project.manage.model.Undertaken;
import com.ginkgocap.tongren.project.manage.service.UndertakenService;


 /**
 * 承接任务
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年11月12日
 */
public class UndertakenServiceTest extends SpringContextTestCase{

	@Autowired
	private UndertakenService undertakenSerivce;
	/**
	 * 功能描述：   承接项目      
	 *                                                       
	 * @param projectId 承接项目ID
	 * @param recipientId 承接人ID
	 * @param recipientOrganizationId 承接人组织ID
	 * @param startTime 承接项目开始时间
	 * @param endTime 承接项目结束时间
	 * @param publishId 发布人ID
	 * @param publishOrganizationId 发布人所属组织ID
	 * @param 承接项目状态 (0未启动、1启动、2暂停、3完成、4放弃) 
	 * @return Undertaken 实体
	 * @throws Exception                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年11月11日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	@Test
	public void create(){
		long projectId = 3908370281267205L;
		long recipientId = 13594L;
		long recipientOrganizationId = 3908349615931417L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 2);
		Date endDate = calendar.getTime();
		long publishId = 128036L;
		long publishOrganizationId =3905158115491845L;
		int status = 1;
		Timestamp startTime = Timestamp.valueOf(sdf.format(startDate));
		Timestamp endTime = Timestamp.valueOf(sdf.format(endDate));
		int projectT = (int) ((endTime.getTime()-startTime.getTime())/(1000*60));
		System.out.println(projectT);
		try {
			Undertaken undertaken = undertakenSerivce.create(projectId, recipientId, recipientOrganizationId, startTime, endTime, publishId, publishOrganizationId, status);
			System.out.println(undertaken.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getUndertaken(){
		Undertaken undertaken = undertakenSerivce.getEntityById(3908416217284613L);
		System.out.println(undertaken.getId());
	}
	
	public void delUndertaken(){
		boolean isOk = undertakenSerivce.deleteEntityById(3908416217284613L);
		System.out.println(isOk);
	}
}
