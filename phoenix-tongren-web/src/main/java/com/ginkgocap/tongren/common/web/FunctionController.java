package com.ginkgocap.tongren.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ginkgocap.tongren.bigdata.vo.AreaVO;
import com.ginkgocap.tongren.bigdata.vo.UserVO;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.exception.ValiaDateRequestParameterException;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.util.ChineseToEnglish;
import com.ginkgocap.tongren.common.utils.ParamInfo;
import com.ginkgocap.tongren.common.web.bean.RequestInfo;
import com.ginkgocap.tongren.organization.manage.model.OrganizationType;
import com.ginkgocap.tongren.organization.manage.service.OrganizationTypeService;
import com.ginkgocap.tongren.organization.system.code.SysCode;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.SensitiveWordService;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;

/**
 * 
 * 公用方法
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/function")
public class FunctionController extends BaseController {

	private final Logger logger = LoggerFactory
			.getLogger(FunctionController.class);

	@Autowired
	private SensitiveWordService sensitiveWordService;

	@Autowired
	private CodeRegionService codeRegionService;

	@Autowired
	private CodeService codeService;
	@Autowired
	private FriendsRelationService friendsRelationService;
	@Autowired
	private OrganizationTypeService organizationTypeService;

	/**
	 * 关键词过滤
	 */
	@RequestMapping(value = "/filtration.json", method = RequestMethod.POST)
	public void filtration(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = { "keyword|R" };
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String keyword = params.getParam("keyword");

			List<String> result = sensitiveWordService.sensitiveWord(keyword);
			if (result == null || result.size() == 0) {
				responseData.put(SysCode.KEYWORK_TRUE.getCode(),SysCode.KEYWORK_TRUE.getMessage());
				renderResponseJson(response,params.getResponse(SysCode.SUCCESS,genResponseData(responseData, notification)));
			} else {
				warpMsg(SysCode.KEYWORK_FALSE, "", params, response);
				return;
			}
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
		}
	}

	/**
	 * 
	 * 获取地区
	 * 
	 * @param id
	 *            类型父级Id
	 * 
	 */
	@RequestMapping(value = "/getArea.json", method = RequestMethod.POST)
	public void getArea(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> responseData = new HashMap<String, Object>();
		String paramsKey[] = { "pid|R" };
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String id = params.getParam("pid");
			List<AreaVO> lst = new ArrayList<AreaVO>();
			List<CodeRegion> result = codeRegionService.selectByParentId(Long
					.parseLong(id));
			if (result.size() != 0) {
				for (CodeRegion c : result) {
					AreaVO a = new AreaVO();
					a.setId(c.getId());
					a.setName(c.getCname());
					lst.add(a);
				}
			}
			if (!lst.isEmpty()) {
				responseData.put(id, lst);
				renderResponseJson(response,params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
			} else {
				warpMsg(SysCode.BIGDATA_EMPTY, "", params, response);
			}
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("getArea is error");
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
		}
	}

	/**
	 * 推荐好友 换一换 搜索好友
	 * 
	 */
	@RequestMapping(value = "/getAllRelations.json", method = RequestMethod.POST)
	public void getAllRelations(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		List<User> lst = new ArrayList<User>();
		String paramsKey[] = { "userName", "pageno|R", "size|R" };
		List<UserVO> list = new ArrayList<UserVO>();
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			String pageno = params.getParam("pageno");
			String userName = params.getParam("userName");
			int pageIndex = Integer.parseInt(pageno);
			String size = params.getParam("size");
			int pageSize = Integer.parseInt(size);
			if (pageIndex <= 0 || pageSize == 0) {
				warpMsg(SysCode.PARAM_IS_ERROR, "", params, response);
				return;
			}
			int startRecord = (pageIndex - 1) * pageSize;
			// 如果搜索好友
			if (StringUtils.isNotBlank(userName)) {
				lst = queryFriends(user.getId(),userName);
				if (lst != null && lst.size() > 0) {
					if (lst.size() > startRecord) {
						int resultSize = 0;
						for(int i = startRecord;i<lst.size(); i++){
							if (resultSize == pageSize) {
								break;
						}
							UserVO uservo = new UserVO();
							uservo.setId(lst.get(i).getId());
							uservo.setName(lst.get(i).getName());
							uservo.setPath(FileInstance.FTP_WEB+ FileInstance.FTP_URL+ lst.get(i).getPicPath());
							list.add(uservo);
							resultSize+=1;
						}
					} else {
						renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY,genResponseData(null, notification)));
						return;
					}
				}
			} else {
				lst = friendsRelationService.findAllFriendsByNameAndChar(user.getId(), null,userName, -1, startRecord, pageSize);
				if (!lst.isEmpty()) {
					for (User u : lst) {
						UserVO uservo = new UserVO();
						uservo.setId(u.getId());
						uservo.setName(u.getName());
						uservo.setPath(FileInstance.FTP_WEB+ FileInstance.FTP_URL + u.getPicPath());
						list.add(uservo);
					}
				} else {
					renderResponseJson(response, params.getResponse(SysCode.BIGDATA_EMPTY,genResponseData(null, notification)));
					return;
				}
			}
			
			responseData.put(String.valueOf(user.getId()), list);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("getAllRelations is error");
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
		}
	}

	/**
	 * 
	 * 获取类型 行业
	 * 
	 */
	@RequestMapping(value = "/getIndustry.json", method = RequestMethod.POST)
	public void getIndustry(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> notification = new HashMap<String, Object>();
		String paramsKey[] = { "id|R", "type" };
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			params = ri.getParams();
			String id = params.getParam("id");
			if (id.indexOf('-') < 0) {
				int prm = -1;
				try {
					prm = Integer.valueOf(id);
				} catch (Exception e) {

				}
				if (prm <= 10 && prm != -1) {
					List<OrganizationType> list = organizationTypeService
							.getOrganizationData(prm);
					responseData.put("data", list);
					renderResponseJson(response, params.getResponse(
							SysCode.SUCCESS,
							genResponseData(responseData, null)));
					return;
				}

			}

			if (id.indexOf("-") > 0) {
				id = id.substring(id.lastIndexOf('-') + 1);
			}
			responseData = getChildNodeById(Long.valueOf(id));
			if (!responseData.isEmpty()) {
				renderResponseJson(
						response,
						params.getResponse(SysCode.SUCCESS,
								genResponseData(responseData, null)));
			} else {
				renderResponseJson(
						response,
						params.getResponse(SysCode.SYS_ERR,
								genResponseData(null, notification)));
			}
			return;
		} catch (Exception e) {
			logger.error("getIndustry is error");
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,
					response);
		}
	}

	/**
	 * 获取下级节点信息
	 * 
	 * @param id
	 * @return
	 */
	private Map<String, Object> getChildNodeById(long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Code> levelList = codeService.selectChildNodeById(id);
		if (levelList != null && levelList.size() > 0) {
			for (Code c : levelList) {
				Map<String, Object> keyAndValue = new HashMap<String, Object>();
				keyAndValue.put("id", c.getNumber());
				keyAndValue.put("name", c.getName());
				keyAndValue.put("remark",
						c.getRemark() == null ? "" : c.getRemark());
				keyAndValue.put("hasChild", c.getHasChild());
				list.add(keyAndValue);
			}
		}
		map.put("data", list);
		return map;
	}
	/**
	 * 查询匹配的好友
	 * @param userId
	 * @param qstr 查询字符串
	 * @return
	 */
	private List<User> queryFriends(long userId,String qstr){
		List<User>  rsList=new ArrayList<User>();
		List<User> userList= friendsRelationService.findAllFriendsByUserId(userId);
		if(userList!=null&&userList.size()>0){
			if(StringUtils.isBlank(qstr)){
				return userList;
			}
			for(User u:userList){
				String uname=u.getName();
				String spelle=ChineseToEnglish.convertToSpell(uname);
				if(uname.startsWith(qstr)||spelle.startsWith(qstr)){
					if(uname.startsWith(qstr)){
						String tmpStr=uname.replaceFirst(qstr, "<font style=\"color:red\">"+qstr+"</font>");
						u.setName(tmpStr);
					}else if(spelle.startsWith(qstr)){
						String tmpStr="<font style=\"color:red\">"+uname.charAt(0)+"</font>";
						u.setName(tmpStr+uname.substring(1));
					} 
					rsList.add(u);
				}
			}
		}
		return rsList;
	}
	/**
	 * 推荐好友 换一换 搜索好友
	 * 
	 */
	@RequestMapping(value = "/getAllRelations_v3.json", method = RequestMethod.POST)
	public void getAllRelationsv3(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> responseData = new HashMap<String, Object>();
		List<User> lst = new ArrayList<User>();
		String paramsKey[] = { "userName", "pageno|R", "size|R" };
		List<UserVO> list = new ArrayList<UserVO>();
		ParamInfo params = null;
		try {
			RequestInfo ri = validate(request, response, paramsKey);
			if (ri == null) {
				return;
			}
			User user = ri.getUser();
			params = ri.getParams();
			String pageno = params.getParam("pageno");
			String userName = params.getParam("userName");
			int pageIndex = Integer.parseInt(pageno);
			String size = params.getParam("size");
			int pageSize = Integer.parseInt(size);
			if (pageIndex <= 0 || pageSize == 0) {
				warpMsg(SysCode.PARAM_IS_ERROR, "", params, response);
				return;
			}
		 
			// 如果搜索好友
			lst = queryFriends(user.getId(),userName);
			Page<UserVO> page=new Page<UserVO>();
			page.setSize(pageSize);
			page.setTotalCount(lst.size());
			page.setIndex(pageIndex);
			int start=page.getStart();
			int end=page.getEnd();
			if(end>lst.size()){
				end=lst.size();
			}
			if(start<lst.size()){
				List<User> ulist=lst.subList(start, end);
				for(User uf:ulist){
					UserVO uservo = new UserVO();
					uservo.setId(uf.getId());
					uservo.setName(uf.getName());
					uservo.setPath(FileInstance.FTP_WEB+ FileInstance.FTP_URL+ uf.getPicPath());
					list.add(uservo);
				}
			}
			page.setResult(list);
			responseData.put("page", page);
			renderResponseJson(response, params.getResponse(SysCode.SUCCESS,genResponseData(responseData, null)));
		} catch (ValiaDateRequestParameterException e) {
			logger.info(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("getAllRelations is error");
			e.printStackTrace();
			warpMsg(SysCode.SYS_ERR, SysCode.SYS_ERR.getMessage(), params,response);
		}
	}
	
}
