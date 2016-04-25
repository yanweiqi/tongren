package com.ginkgocap.tongren.organization.review.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.model.Page;
import org.apache.commons.collections.CollectionUtils;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewRecords;
import com.ginkgocap.tongren.organization.review.service.ReviewApplicationService;
import com.ginkgocap.tongren.organization.review.service.ReviewGenreService;
import com.ginkgocap.tongren.organization.review.service.ReviewObjectService;
import com.ginkgocap.tongren.organization.review.service.ReviewProcessService;
import com.ginkgocap.tongren.organization.review.service.ReviewRecordsService;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;
import com.ginkgocap.tongren.organization.review.vo.ReviewRecordsVO;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("reviewRecordsService")
public class ReviewRecordsServiceImpl extends  AbstractCommonService<ReviewRecords> implements ReviewRecordsService{
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewRecordsServiceImpl.class);
	
	@Autowired
	private ReviewObjectService reviewObjectService;
	@Autowired
	private ReviewProcessService reviewProcessService;
	@Autowired
	private ReviewGenreService reviewGenreService;
	@Autowired
	private UserService userService;
	@Autowired
	private ReviewApplicationService reviewApplicationService;
	
	@Override
	public boolean signOff(long applicationId,long userId, int type) throws Exception {
		
		boolean  result = false;
		ReviewRecords r = null;
		int index=-1;
		try{
			List<ReviewRecords> lst = getRecordsListByApplicationId(applicationId);
			if(lst != null && !lst.isEmpty()){
				for(int i=0;i<lst.size();i++){
					if(lst.get(i).getReviewUserId()==userId){
						index=i;
						break;
					}
				}
				if(index==0||lst.get(index-1).getReviewStatus()==2){
					if(lst.get(index).getReviewStatus()==0){
						result=true;
					}else{
						return false;
					}
					
				}else{
					return false;
				}
			}else{
				return false;
			}
			if(result){
				Long  id = getMappingByParams("ReviewRecords_List_ApplicationId_ReviewUserId",new Object[]{applicationId,userId});
				if(id!=null){
					r = getEntityById(id);
					r.setReviewStatus(type);
					r.setReviewDate(new Timestamp(System.currentTimeMillis()));
					if(type==2){ //如果操作通过的时候 把当前权限移除给下位审核人
						r.setIsReview(0);
						index++;
						if(index<lst.size()){
							lst.get(index).setIsReview(1);
							update(lst.get(index));
						}
					}
					result = update(r);
					
				}
			}
		}catch(Exception e){
			if(r != null){
				r.setReviewStatus(0);
				update(r);
			}
			logger.error(e.getMessage()+"审核通过,驳回失败"+type+"userId"+userId+"applicationId"+applicationId,e);
			throw e;
		}
				return result;
	}

	@Override
	public List<ReviewRecords> getRecordsByProcessId(long id,int status) {
		
			List<ReviewRecords> recordsList = new ArrayList<ReviewRecords>();
			List<Long> ids = getKeysByParams("ReviewRecords_List_ProcessId_status", new Object[]{id,status});
			if(ids != null && !ids.isEmpty()){
				recordsList = getEntityByIds(ids);
			}
		return recordsList;
	}

	@Override
	public List<ReviewRecords> getRecordsListByApplicationId(long applicationId) {
		
		List<ReviewRecords> lst =new ArrayList<ReviewRecords>();
		List<Long> ids = getKeysByParams("ReviewRecords_List_ApplicationId",applicationId);
		if(ids != null  && !ids.isEmpty()){
			lst = getEntityByIds(ids);
		}
		return lst;
	}
	
	/***
	 * 查询我的审核列表
	 * @author hanxifa
	 * @param page
	 * 查询条件包含  orgId 组织id，userId 用户id，type 查询类型(0所有的 1 未审核的 2 已经审核)
	 */
	@Override
	public Page<ReviewApplicationListVO> getReviewListPage(Page<ReviewApplicationListVO> page) {
		String sql =null; 
		Long orgId = Long.valueOf(page.getParam("orgId").toString());
		Long userId = Long.valueOf(page.getParam("userId").toString());
		String type = page.getParam("type").toString();// 0所有的 1 未审核的 2 已经审核
		Object[] queryArayy=new Object[] { userId, orgId}; 
		List<Long> ids=null;
		if ("1".equals(type)) {//未审核的 
			sql="ReviewRecords_List_ReviewUserId_organizationId_reviewStatus";
		} else if ("2".equals(type)) {//已审核的 
			sql="ReviewRecords_List_ReviewUserId_organizationId_reviewStatus2";
		}else{//全部
			sql="ReviewRecords_List_ReviewUserId_organizationId";
		}
		page.setTotalCount(count(sql,queryArayy));
		ids=getKeysByParams(sql, queryArayy, page.getStart(), page.getSize());
		if(ids!=null&&ids.size()>0){
			List<ReviewApplicationListVO> list=new ArrayList<ReviewApplicationListVO>();
			for(Long recordsId:ids){
				try {
					ReviewRecords rrc=getEntityById(recordsId);
					ReviewApplicationListVO rlo=reviewApplicationService.getReviewApplicationDetail(rrc.getApplicationId());
					if(rlo.getStatus()!=1){//撤回申请时 不覆盖状态
						rlo.setStatus(rrc.getReviewStatus());
					}
					list.add(rlo);
				} catch (Exception e) {
					logger.error("query ReviewApplicationDetail failed! "+recordsId,e);
				}
			}
			page.setResult(list);
			return page;
		}
		return null;
	}
	@Override
	public List<ReviewRecordsVO> getRecordsDetailByApplicationId(long applicationId) throws Exception {
		List<ReviewRecordsVO> detail = new ArrayList<ReviewRecordsVO>();
		List<ReviewRecords> recordsList = getRecordsListByApplicationId(applicationId);
		logger.info("ReviewRecords count: " + recordsList.size() + " by " + applicationId );
		for (ReviewRecords rrs : recordsList) {
			ReviewRecordsVO rrvo = new ReviewRecordsVO();
			BeanUtils.copyProperties(rrvo, rrs);
			rrvo.setUserName(getUserNameById(rrs.getReviewUserId()));
			rrvo.setUserPicPath(getUserPicURL(rrs.getReviewUserId()));
			rrvo.setLevel(detail.size() + 1);
			detail.add(rrvo);
		}
		return detail;
	}
	private String getUserNameById(long id){
		if(id>0){
			User user = userService.selectByPrimaryKey(id);
			if(user!=null){
				return user.getName();
			}
		}
		return null;
	}
	@Override
	protected Class<ReviewRecords> getEntity() {return ReviewRecords.class;}
	@Override
	public Map<String, Object> doWork() {return null;}
	@Override
	public Map<String, Object> doComplete() {return null;}
	@Override
	public String doError() {return null;}
	@Override
	public Map<String, Object> preProccess() {return null;}

	@Override
	public List<ReviewRecords> getMyReviewRecordsList(long userId ,long organizationId) throws Exception{
		
		logger.info("getMyReviewRecordsList begin orgId-->"+organizationId,"userId--->"+userId);
		List<ReviewRecords> result = new ArrayList<ReviewRecords>();
		try{
			List<Long> ids = getKeysByParams("ReviewRecords_List_ReviewUserId_organizationId_reviewStatus3", new Object[]{userId,organizationId});
			if(ids != null && CollectionUtils.isNotEmpty(ids)){
				result = getEntityByIds(ids);
			}
		}catch(Exception e){
			logger.error("getMyReviewRecordsList is error...",e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
}
