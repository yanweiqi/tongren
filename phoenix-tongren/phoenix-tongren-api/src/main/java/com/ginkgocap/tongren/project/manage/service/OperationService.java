package com.ginkgocap.tongren.project.manage.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.project.manage.model.Operation;
import com.ginkgocap.tongren.project.manage.vo.OperationVO;
/**
 * 项目任务分配Service
 * @author liweichao
 *
 */
public interface OperationService extends CommonService<Operation>{
	
	 /**
	  * 功能描述 ：保存分配任务
	  * @author liweichao
	  * @param operation 项目操作实体 operationCode具体业务查看taskCode类
	  * @return  0:成功   1:项目不存在  2：组织任务为空 3:操作人为空 4:保存失败  
	  */
	public String createOperation(Operation operation)throws Exception;
	
	/**
	 * 功能描述 ：获取项目操作记录 按时间排序
	 * @author liweichao
	 * @param  projectId
	 * @return List<Operation>
	 */
	public List<OperationVO> getOperationByProjectId(long projectId)throws Exception;
	
	/**
	 * 功能描述 ： 获取组织操作记录 按时间排序
	 * @author liweichao
	 * @param organizationTaskId
	 * @return List<Operation>
	 * 
	 */
	public List<Operation> getOpertionByOrgTaskId(long organizationTaskId)throws Exception;
	
	
	/**
	 * 功能描述 ：查询remark等于任务id的操作记录
	 * 
	 */
	public List<Operation> getOperactionByTaskId(long taskId)throws Exception;
}
