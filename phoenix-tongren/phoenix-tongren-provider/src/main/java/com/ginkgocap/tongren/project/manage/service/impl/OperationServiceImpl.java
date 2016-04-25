package com.ginkgocap.tongren.project.manage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.model.Project;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.manage.service.ProjectService;
import com.ginkgocap.tongren.project.manage.vo.OperationVO;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("operationService")
public class OperationServiceImpl extends AbstractCommonService<Operation> implements OperationService{

	//private static final Logger logger = LoggerFactory.getLogger(OperationServiceImpl.class);
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private ProjectTaskService projectTaskService;
	
	
	@Override
	public String createOperation(Operation operation) throws Exception{
		
				String result = "0";	
				Operation o = null ; 
		try{
				if(operation.getProjectId() != 0){ //项目任务操作记录
					Project  p = projectService.getEntityById(operation.getProjectId());
					if(p == null ){		//项目不存在
						return result = "1";
					}
				}
				if(operation.getOrganizationTaskId() != 0 ){ //组织任务
					Task  task = projectTaskService.getEntityById(operation.getOrganizationTaskId());
					if(task == null){
						return result = "2";//组织任务为空
					}
				}
				if(operation.getOperationUid() == 0){ //操作人为空
					return result = "3";
				}	
				 o = save(operation);
				if(o == null){	//保存失败
					return result = "4";
				}
		}catch(Exception e){
				if(o != null){
					deleteEntityById(o.getId());
				}
					e.printStackTrace();
				}
					return result;
	}
	@Override
	public List<OperationVO> getOperationByProjectId(long projectId) throws Exception{
	
		List<Operation> cacheList = new ArrayList<Operation>();
		List<OperationVO> voList = new ArrayList<OperationVO>();
		List<Long> ids = getKeysByParams("operation_list_projectId", projectId);
		if(ids != null && !ids.isEmpty()){
			cacheList = getEntityByIds(ids);
			if(!cacheList.isEmpty()){
				for(Operation o:cacheList){
					OperationVO vo = new OperationVO();
					BeanUtils.copyProperties(vo, o);
					voList.add(vo);
				}
			}
		}
		return voList;
	}

	@Override
	protected Class<Operation> getEntity() {
		return Operation.class;
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
	@Override
	public List<Operation> getOperactionByTaskId(long taskId) throws Exception {
		
		List<Operation> list = new ArrayList<Operation>();
		try{
			List<Long> ids = getKeysByParams("operation_list_taskId", taskId);
			if(ids != null && !ids.isEmpty()){
				list = getEntityByIds(ids);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<Operation> getOpertionByOrgTaskId(long organizationTaskId) throws Exception {
		
		List<Operation> list = new ArrayList<Operation>();
		try{
			List<Long> ids = getKeysByParams("operation_list_organizationTaskId", organizationTaskId);
			if(ids != null && !ids.isEmpty()){
				list = getEntityByIds(ids);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
