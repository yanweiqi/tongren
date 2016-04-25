package com.ginkgocap.tongren.review.web;

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
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.BaseController;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.service.ReviewGenreService;
import com.ginkgocap.tongren.organization.system.code.SysCode;

/**
 * 审批类型控制层
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/reviewGenre") 
public class ReviewGenreController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(ReviewGenre.class);
	
	@Autowired
	private ReviewGenreService reviewGenreService;
	/**
	 * 
	 * 查询流程下的类型
	 * 
	 */
	@RequestMapping(value="/getGenre.json",method =RequestMethod.POST)
	public void getGenre(HttpServletRequest request, HttpServletResponse response){
				String paramsKey[] = {"pid"};
				Map<String, Object> responseData = new HashMap<String, Object>();
				Map<String, Object> notification = new HashMap<String, Object>();
				ParamInfo params = null;
		try{
				RequestInfo ri=validate(request,response,paramsKey);
			if(ri==null){
				return;
			}
				params=ri.getParams();
				String pid = params.getParam("pid");
				logger.info("getGenre pid--->"+pid);

				List<ReviewGenre> list = reviewGenreService.getGenreByProId(Long.valueOf(pid));
			if(list.isEmpty()){
				renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY, genResponseData(null,notification)));
			}else{
				responseData.put(pid, list);
				renderResponseJson(response, params.getResponse(SysCode.SUCCESS, genResponseData(responseData, null)));
			}	 
		}catch(ValiaDateRequestParameterException e){
				logger.info(e.getMessage(),e);	
		}catch(Exception e){
				logger.error("getGenre is error");
				e.printStackTrace();
				notification.put("notifyCode", SysCode.SYS_ERR.getCode());
				notification.put("notifyMessage", SysCode.SYS_ERR);
				renderResponseJson(response, params.getResponse(SysCode.SYS_ERR, genResponseData(null, notification)));
		}
	}

}
