package com.ginkgocap.tongren.mq;

import java.io.IOException;
import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.json.JSON;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.common.JmsSendService;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.JmsMsgBean;
import com.ginkgocap.tongren.organization.manage.service.TongRenOrganizationService;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.service.PublishService;


public class MqTest extends SpringContextTestCase{
	
	@Autowired
	private JmsSendService jmsSendService;
	
	@Autowired
	private TongRenOrganizationService organizationService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private  PublishService publishService;
	
	@Test
	public void testSendProjectInfo(){
		JmsMsgBean jmsMsgBean=new JmsMsgBean();
		jmsMsgBean.setOpType(CommonConstants.Publish);//项目 
		jmsMsgBean.setOperator(21);//增加操作
		jmsMsgBean.setContent(publishService.getEntityById(3922792252964874l));
		jmsSendService.sendJmsMsg(jmsMsgBean);
 
	}
	@Test
	public void testReSend(){
		jmsSendService.resendFailedRecord();
	}
	public static void main(String[] args) throws IOException {
		Project project =new Project();
		project.setArea("北京");
		project.setCreaterId(123901209301293l);
		project.setCreateTime(new Timestamp(System.currentTimeMillis()));
		project.setCycle(50);
		project.setDocument("测试文档");
		project.setIndustry("金融行业");
		project.setIntroduction("项目介绍");
		project.setName("社群项目");
		project.setOrganizationId(3905158115491845l);
		project.setRemuneration(200000);
		project.setStatus(1);
		String str=JSON.json(project);
		System.out.println(str);
	}
}
