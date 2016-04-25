package com.ginkgocap.tongren.project.service.task;

import java.sql.Timestamp;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ginkgocap.tongren.base.SpringContextTestCase;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.manage.vo.OperationVO;
import com.ginkgocap.tongren.project.system.code.TaskCode;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;

public class OperationServiceTest extends SpringContextTestCase{
	
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private OperationService operationService;
	
	@Test
	public void testSave()throws Exception{

		for(int i=0;i<5;i++){
			
			Operation o = new Operation();
			o.setOperactionTime(new Timestamp(System.currentTimeMillis()));
			o.setOperationCode(TaskCode.ALLOT.getCode());
			o.setOperationUid(1+i);
			o.setProjectId(3907659925553157L);
			o.setRemark("啊速度卡上的");
			String type = operationService.createOperation(o);
			System.out.println("|||||"+type);
		}

	}
	
	@Test 
	public void testSelect() throws Exception{
		List<OperationVO>  lst = operationService.getOperationByProjectId(3915660568821805L);
		for(Operation st:lst){
			System.out.println("****"+st.getId());
		}
	}

}
