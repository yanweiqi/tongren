package com.ginkgocap.tongren.project.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.tongren.project.manage.service.OperationService;
import com.ginkgocap.tongren.project.manage.vo.OperationVO;
import com.ginkgocap.tongren.project.task.model.Task;
import com.ginkgocap.tongren.project.task.service.AssignTaskService;
import com.ginkgocap.tongren.project.task.service.ProjectTaskService;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
@Controller
@RequestMapping("/operation")
public class OperationController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(OperationController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private AssignTaskService assignTaskService;
	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private FileIndexService fileIndexService;
	
	/**
	 * 查询项目分配任务
	 * 
	 */
	@RequestMapping(value="/getProjectOperation.json",method =RequestMethod.POST)
	public void getOperactionByProjectId(HttpServletRequest request, HttpServletResponse response){
		
				ParamInfo params = null;
				String paramsKey[] = {"projectId|R"};
				List<OperationVO>  lst = new ArrayList<OperationVO>();
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
			try{
				RequestInfo ri=validate(request,response,paramsKey);
				if(ri==null){
					return;
				}
					params=ri.getParams();
					String projectId = params.getParam("projectId");
					logger.info("select AssignTask taskId--->"+projectId);
					lst = operationService.getOperationByProjectId(Long.valueOf(projectId));
					
				if(lst != null && !lst.isEmpty()){
					for(OperationVO o :lst){
						Task task = new Task();
						String str = "";
						User u = userService.selectByPrimaryKey(o.getOperationUid());
						if(!o.getOperationCode().equals("06")){
						task = projectTaskService.getEntityById(Long.valueOf(o.getRemark()));//获取主任务
					}
						if(o.getOperationCode().equals("01")){
							str = u.getName()+" 申请项目延期 "+o.getRemark()+" 天";
						}
						if(o.getOperationCode().equals("02")){
							str = u.getName()+" 创建了 "+task.getTitle()+" 子任务";
						}
						if(o.getOperationCode().equals("03")){
							StringBuilder sb = assignTaskService.getPerformerName(Long.valueOf(o.getRemark())); //获取被分配人名称
							str = u.getName()+" 分配了 "+task.getTitle()+" 子任务给"+ sb;
						}
						if(o.getOperationCode().equals("04")){
							StringBuilder sb = assignTaskService.getPerformerName(Long.valueOf(o.getRemark())); //获取被分配人名称
							str = sb+" 开始了 "+task.getTitle();
						}
						if(o.getOperationCode().equals("05")){
							str = u.getName()+" 完成了 "+task.getTitle();
						}
						if(o.getOperationCode().equals("06")){
							List<String> file = getUrlByTask(o.getRemark());
							if(file!=null && !file.isEmpty()){
								str = u.getName()+" 上传了 "+file.get(0);
								o.setDocPath(file.get(1));
						}
						}
							o.setName(str);
						}
					responseData.put("projectId",projectId);
					responseData.put("result", lst);
					renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
				}else{
					notification.put(SysCode.BIGDATA_EMPTY.getCode(), SysCode.BIGDATA_EMPTY.getMessage());
					renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null, notification)));
				}
				
			}catch(Exception e){
				e.printStackTrace();
				logger.error("getProjectOperation error projectId--->",e.getMessage());
				notification.put(SysCode.BIGDATA_EMPTY.getCode(), SysCode.NO_ERR.getMessage());
				renderResponseJson(response, params.getResponse(SysCode.NO_ERR, genResponseData(null, notification)));
				return;
			}
	}
	/**
	 * 根据taskId获取url信息
	 * @param taskId
	 * @return
	 */
	private List<String> getUrlByTask(String taskId) throws Exception{
		List<String> result = new ArrayList<String>();
		List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
		String path ="";
		if(list!=null&&list.size()>0){
			FileIndex s = (FileIndex) list.get(0);
			path = s.getFilePath() == null ? null : FileInstance.FTP_FULL_URL.trim() + s.getFilePath()+"/"+s.getFileTitle();
			result.add(s.getFileTitle());
			result.add(path);
		}
		return result;
	}
}
