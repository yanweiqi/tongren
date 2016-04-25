/*
 * 文件名： ProjectResourceAttachmentServiceTest.java
 * 创建日期： 2015年12月7日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.project.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.tongren.base.SpringContextTestCase;


 /**
 *  
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年12月7日
 */
public class ProjectResourceAttachmentServiceTest extends SpringContextTestCase {

	@Autowired
	private ProjectResourceAttachmentService projectResourceAttachmentService;
	
	@Test
	public void insertProjectAttachmentTest(){
		long projectId = 12121;
		long createrId = 36;
		List<String> taskids = new ArrayList<String>();
		taskids.add("111111111");
		taskids.add("222222222");
		taskids.add("333333333");
		projectResourceAttachmentService.insertProjectAttachment(projectId, taskids, createrId);
	}
}
