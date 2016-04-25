package com.ginkgocap.tongren.organization.review.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.tongren.common.model.Page;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewApplication;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.model.ReviewProcess;
import com.ginkgocap.tongren.organization.review.model.ReviewRecords;
import com.ginkgocap.tongren.organization.review.service.ReviewApplicationService;
import com.ginkgocap.tongren.organization.review.service.ReviewGenreService;
import com.ginkgocap.tongren.organization.review.service.ReviewObjectService;
import com.ginkgocap.tongren.organization.review.service.ReviewProcessService;
import com.ginkgocap.tongren.organization.review.service.ReviewRecordsService;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationListVO;
import com.ginkgocap.tongren.organization.review.vo.ReviewApplicationVO;
import com.ginkgocap.tongren.organization.review.vo.ReviewRecordsVO;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;

@Service("reviewApplicationService")
public class ReviewApplicationServiceImpl extends AbstractCommonService<ReviewApplication> implements ReviewApplicationService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewApplicationServiceImpl.class);

	@Autowired
	private ReviewRecordsService reviewRecordsSevice;
	@Autowired
	private ReviewObjectService reviewObjectService;
	@Autowired
	private ReviewProcessService reviewProcessService;
	@Autowired
	private ReviewGenreService reviewGenreService;
	@Autowired
	private UserService userService;

	@Override
	public ReviewApplication create(ReviewApplication r) throws Exception{
		ReviewApplication  reviewApplication = null;
		try {
			reviewApplication = save(r);
			if(reviewApplication != null){
				List<ReviewObject> objectIds = reviewObjectService.getReviewList(r.getReviewProcessId());
				createApplication(objectIds, reviewApplication);
			}
		} 
		catch (Exception e) {
			logger.error(e.getMessage()+"创建我的申请失败！",e);
			throw e;
		}
		return reviewApplication;
	}
	@Override
	public ReviewApplication createTemplate(ReviewApplication r,List<ReviewObject> objectIds) throws Exception {
		
		ReviewApplication  reviewApplication = null;
		try{
			reviewApplication = save(r);
			if(reviewApplication != null){
				createApplication(objectIds, reviewApplication);
			}
		}catch(Exception e){
			logger.error(e.getMessage()+"创建模板申请失败！",e);
		}
		return reviewApplication;
	}
	 /**
	  * @author hanxifa 
	  * 查看我的申请
	  * @param userId 用户Id
	  * @param oid 组织id
	  * @param type 0 全部   1:已提申请  2:已完成申请
	  * 
	  */
	@Override
	public Page<ReviewApplicationListVO> getMyApplyFor(Page<ReviewApplicationListVO> page) {
		Long orgId = (Long) page.getParam("orgId");
		Long userId = (Long) page.getParam("userId");
		Integer type = (Integer) page.getParam("type");
		try {
			List<Long>  alist = getKeysByParams("ReviewApplication_List_OrganizationId_And_ApplyId", new Object[]{orgId,userId});
			logger.info("ReviewApplication count: " + alist.size() + " reviewapplications by orgId:" + orgId + ",userId:" + userId + ",type:" + type);
			List<ReviewApplicationListVO> rsList = new ArrayList<ReviewApplicationListVO>();
			for (Long id : alist) {
				ReviewApplicationListVO  applicationListVO=getReviewApplicationDetail(id);
				if(applicationListVO==null){
					logger.info("not found ReviewApplicationListVO by id "+id);
					continue;
				}
				//查询已提交申请但当前状态为完成状态或者 查询已完成申请，但当前状态为未完成
				if ((type == 1 && applicationListVO.getProgress() == 2) || (type == 2 && applicationListVO.getProgress() != 2)) {
					continue;
				}
				rsList.add(applicationListVO);
			}
			/**
			 * 内存分页
			 */
			page.setTotalCount(rsList.size());
			int start = -1, end = page.getEnd()-1;
			if (page.getStart() < rsList.size()) {
				start = page.getStart();
			}
			if (end >= rsList.size()) {
				end = rsList.size() - 1;
			}
			if (start != -1) {
				page.setResult(new ArrayList<ReviewApplicationListVO>(rsList.subList(start, end+1)));
			}
			return page;

		} catch (Exception e) {
			logger.error("query ReviewApplicationList failed! orgId:" + orgId + ",userId:" + userId + ",type:" + type, e);
			return null;
		}
	}

	/**
	 * v3版本的申请列表
	 * @param page
	 * @return
	 */
	public Page<ReviewApplicationListVO> getMyApplyForV3(Page<ReviewApplicationListVO> page) {
		Long orgId = (Long) page.getParam("orgId");
		Long userId = (Long) page.getParam("userId");
		Integer type = (Integer) page.getParam("type");
		try {
			List<Long> alist = getKeysByParams("ReviewApplication_List_OrganizationId_And_ApplyId", new Object[] { orgId, userId });
			logger.info("ReviewApplication count: " + alist.size() + " reviewapplications by orgId:" + orgId + ",userId:" + userId + ",type:" + type);
			List<ReviewApplicationListVO> rsList = new ArrayList<ReviewApplicationListVO>();
			for (Long id : alist) {
				ReviewApplicationListVO applicationListVO = getReviewApplicationDetail(id);

				if (applicationListVO == null) {
					logger.info("not found ReviewApplicationListVO by id " + id);
					continue;
				}
				int progress = applicationListVO.getProgress();
				int status = applicationListVO.getStatus();
				if (type == 0) {// 全部
					rsList.add(applicationListVO);
				} else if (type == 1) {// 审批中的
					if (progress != 2 && (status == 0 || status == 2)) {// 没有进行到最后一部，审核状态为未审核或者审核状态为通过
						rsList.add(applicationListVO);
					}
				} else if (type == 2) {// 已完成
					if (progress == 2 && status == 2) {// 审核进行完成，并且为通过状态
						rsList.add(applicationListVO);
					}
				} else if (type == 3) {// 已驳回
					if (status == 3) {
						rsList.add(applicationListVO);
					}
				} else if (type == 4) {// 已撤回
					if (status == 1) {
						rsList.add(applicationListVO);
					}
				}

			}
			/**
			 * 内存分页
			 */
			page.setTotalCount(rsList.size());
			int start = -1, end = page.getEnd() - 1;
			if (page.getStart() < rsList.size()) {
				start = page.getStart();
			}
			if (end >= rsList.size()) {
				end = rsList.size() - 1;
			}
			if (start != -1) {
				page.setResult(new ArrayList<ReviewApplicationListVO>(rsList.subList(start, end + 1)));
			}
			return page;

		} catch (Exception e) {
			logger.error("query ReviewApplicationList failed! orgId:" + orgId + ",userId:" + userId + ",type:" + type, e);
			return null;
		}
	}
	
	@Override
	public boolean recallRecords(long applicationId) throws Exception {
		
		boolean flag = false;
		ReviewApplication  application  = getEntityById(applicationId);
		try{
			if(application != null){
				application.setApplyStatus(1);
				flag = update(application);
			}
		}catch(Exception e){
			logger.error(e.getMessage()+"撤回申请失败");
			throw e;
		}
		return flag;
	}
	@Override
	protected Class<ReviewApplication> getEntity() {return ReviewApplication.class;}
	@Override
	public Map<String, Object> doWork() {return null;}
	@Override
	public Map<String, Object> doComplete() {return null;}
	@Override
	public String doError() {return null;}
	@Override
	public Map<String, Object> preProccess() {return null;}

	@Override
	public List<ReviewApplication> getUserReviewListByOrgId(long organizationId, long userId) {
		
		List<ReviewApplication> list = new ArrayList<ReviewApplication>();
		List<Long>  ids = getKeysByParams("ReviewApplication_List_OrganizationId_And_ApplyId", new Object[]{organizationId,userId});
		if(ids != null && !ids.isEmpty()){
			list = getEntityByIds(ids);
		}
		return list;
	}
	
	 
	
	public ReviewApplicationVO saveRecordsVO(ReviewProcess  process,ReviewGenre genre,ReviewApplication r,int status){
		
		ReviewApplicationVO vo = new ReviewApplicationVO();
		vo.setProcessId(process.getId());
		vo.setRecordsId(r.getId());
		vo.setReviewNo(process.getReviewNo());
		vo.setCreateTime(process.getCreateTime().toString());
		vo.setProcessName(process.getReviewName());
		vo.setStartTime(r.getStartTime().toString());
		vo.setEndTime(r.getEndTime().toString());
		vo.setApplyRereason(r.getApplyRereason());
		vo.setGenreName(genre.getName());
		vo.setRecordsStatus(status);
		return vo;
	}

	@Override
	public ReviewApplicationListVO getReviewApplicationDetail(long id) throws Exception {

		ReviewApplication rap= getEntityById(id);
		if(rap==null){
			return null;
		}
		ReviewApplicationListVO applicationListVO = new ReviewApplicationListVO();
		// 该申请的审核情况，数据库sql按审核日期,id正序排序
		
		List<ReviewRecordsVO> detail = reviewRecordsSevice.getRecordsDetailByApplicationId(rap.getId());
		applicationListVO.setReviewDetail(detail);
		for(int i=0;i<detail.size();i++){
			ReviewRecordsVO ro=detail.get(i);
			if(ro.getReviewStatus()!=0){
				applicationListVO.setStatus(ro.getReviewStatus());
				if(i==detail.size()-1){
					applicationListVO.setProgress(2);// 审核进行中
				}
				else{
					applicationListVO.setProgress(1);// 审核进行中
				}
			}
		}
		if(rap.getApplyStatus()==1){//撤回申请
			applicationListVO.setStatus(1);
		}
		User user = userService.selectByPrimaryKey(rap.getApplyId());
		if (user != null) {
			applicationListVO.setApplyUserName(user.getName());
		}else{
			logger.info("not found user info by id "+rap.getApplyId());
		}
		BeanUtils.copyProperties(applicationListVO, rap);
		ReviewGenre genre = reviewGenreService.getGenreById(rap.getReviewGenreId());
		if (genre != null) {
			applicationListVO.setReviewGenreName(genre.getName());// 流程名称，比如事假
		}
		return applicationListVO;
	}
	private void createApplication(List<ReviewObject> objectIds,ReviewApplication reviewApplication){
		boolean isIndex=true;
		for (ReviewObject reviewObject : objectIds) {
				ReviewRecords recirds = new ReviewRecords();
				if(isIndex){
					isIndex=false;
					recirds.setIsReview(1);
				}else{
					recirds.setIsReview(0);
				}
				recirds.setReviewStatus(0);
				recirds.setApplicationId(reviewApplication.getId());
				recirds.setReviewUserId(reviewObject.getReviewUserId());
				recirds.setOrganizationId(reviewApplication.getOrganizationId());
				reviewRecordsSevice.save(recirds);
		}
	}
	@Override
	public ReviewApplication afreshSubmit(long oldId ,ReviewApplication r, List<ReviewObject> objectIds) throws Exception {
		
			ReviewApplication  reviewApplication = null;
		try {
			List<ReviewRecords> oldRecordsList = reviewRecordsSevice.getRecordsListByApplicationId(oldId);
			
			if(CollectionUtils.isNotEmpty(oldRecordsList)){
				for(ReviewRecords records :oldRecordsList){
					reviewRecordsSevice.deleteEntityById(records.getId());
				}
			}
			boolean  status = deleteEntityById(oldId);
			
			if(status){
				reviewApplication = save(r);
				if(reviewApplication != null){
					createApplication(objectIds, reviewApplication);
				}
			}
		} catch (Exception e) {
			logger.info("afreshSubmit is error oldId--->" + oldId);
			e.printStackTrace();
		}
		return reviewApplication;
	}
}
