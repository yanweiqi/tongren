package com.ginkgocap.tongren.organization.review.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.common.utils.GenerateUtil;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.model.ReviewProcess;
import com.ginkgocap.tongren.organization.review.model.ReviewRecords;
import com.ginkgocap.tongren.organization.review.service.ReviewGenreService;
import com.ginkgocap.tongren.organization.review.service.ReviewObjectService;
import com.ginkgocap.tongren.organization.review.service.ReviewProcessService;
import com.ginkgocap.tongren.organization.review.service.ReviewRecordsService;

@Service("reviewProcessService")
public class ReviewProcessServiceImpl extends AbstractCommonService<ReviewProcess> implements ReviewProcessService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReviewProcessServiceImpl.class);

	@Autowired
	private ReviewGenreService reviewGenreService;
	@Autowired
	private ReviewObjectService reviewObjectService;
	@Autowired
	private ReviewRecordsService reviewRecordsService;

	@Override
	public String getNomber() {
		return GenerateUtil.generateNumbering();
	}

	@Override
	public boolean checkProcessName(long orgId, String name) throws Exception {
		boolean status = false;
		try {
			Long id = this.getMappingByParams("ReviewProcess_List_orgId_name",
					new Object[] { orgId, name });
			logger.info("checkName id--->" + id);
			if (id == null) {
				status = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public List<ReviewProcess> getReviewByOrgId(long orgId) {

		List<ReviewProcess> result = new ArrayList<ReviewProcess>();
		// 根据组织 id查询组织下的 审批流程集合
		List<Long> ids = this.getKeysByParams("ReviewProcess_List_orgId", orgId);
		if (ids != null && !ids.isEmpty()) {
			for (Long id : ids) {
				ReviewProcess r = this.getEntityById(id);
				if (r != null) {
					List<ReviewGenre> genreList = reviewGenreService.getGenreByProId(id);
					List<ReviewObject>	objList = reviewObjectService.getReviewList(id);
					r.setObjectList(objList);
					r.setGenreList(genreList);
					result.add(r);
				}
			}
		}
		return result;
	}

	@Override
	public ReviewProcess getReviewById(long reviewId) {

		//		List<ReviewGenre> genreLst = new ArrayList<ReviewGenre>();
		//		List<ReviewObject> objectLst = new ArrayList<ReviewObject>();
		// 查询审核流程
		ReviewProcess p = getEntityById(reviewId);
		// 查询审核名称下的类型
		if (p != null) {
			// 查询审核流程下的类型
			List<ReviewGenre> genreIds = reviewGenreService.getGenreByProId(reviewId);
			// 查询审核流程下的审核对象
			List<ReviewObject> objList = reviewObjectService.getReviewList(reviewId);
			p.setGenreList(genreIds);
			p.setObjectList(objList);
		}
		return p;
	}

	@Override
	public long create(ReviewProcess p, List<ReviewGenre> genreList,List<ReviewObject> objList) throws Exception {

			ReviewProcess process = null;
			List<ReviewGenre> list = new ArrayList<ReviewGenre>();
			List<ReviewObject> lst = new ArrayList<ReviewObject>();
			
		try{
			        process = save(p);
			if (genreList != null && !genreList.isEmpty() && process != null) {
				for (ReviewGenre r : genreList) {
					r.setCreateId(p.getCreateId());
					r.setCreateTime(new Timestamp(System.currentTimeMillis()));
					r.setPid(p.getId());
					ReviewGenre  g  = reviewGenreService.save(r);
					list.add(g);
				}
				
			}
			if (objList != null && !objList.isEmpty() && process != null) {
				for (ReviewObject o : objList) {
					o.setOrganizationId(p.getId());
					o.setCreateTime(new Timestamp(System.currentTimeMillis()));
					o.setCreateId(p.getCreateId());
					o.setReviewProcess(p.getId());
					ReviewObject obj = reviewObjectService.save(o);
					lst.add(obj);
					
				}
			}
		}catch(Exception e){
			
			if(process != null){
					deleteEntityById(process.getId());  //删除审核流程
			}
			if(list != null && !list.isEmpty()){
				for (ReviewGenre r : list) {
					deleteEntityById(r.getId());    //删除流程下的类型
				}
			}
			if(lst != null && !lst.isEmpty()){
				for (ReviewObject r : lst) {
					deleteEntityById(r.getId());  //删除流程下的对象
				}
			}
			logger.error(e.getMessage()+"创建审批流程失败!",e);
			throw e;
			
		}
		return process.getId();
	}

	@Override
	public boolean updateReviewProcess(ReviewProcess p) throws Exception {
		
		boolean  flag = update(p);
		return flag;
	}

	@Override
	public boolean delect(long reviewProcessId) throws Exception {

		boolean flag = false;
		// 根据流程Id查询申请中的记录
		List<ReviewRecords> recordsList = reviewRecordsService.getRecordsByProcessId(reviewProcessId, 0);
		if (!recordsList.isEmpty()) {
			return flag;
		}
		// 查询审核流程
		ReviewProcess p = this.getEntityById(reviewProcessId);
		if (p != null) {
			// 查询审核名称下的类型 然后删除
			List<ReviewGenre> genreList = reviewGenreService.getGenreByProId(reviewProcessId);
			if (!genreList.isEmpty()) {
				for (ReviewGenre g : genreList) {
					reviewGenreService.deleteEntityById(g.getId());
				}
			}
			// 查询审核下审批对象 然后删除
			List<ReviewObject> objectIds = reviewObjectService.getReviewList(reviewProcessId);
			if (!objectIds.isEmpty()) {
				for (ReviewObject o : objectIds) {
					reviewGenreService.deleteEntityById(o.getId());
				}
			}
			flag = deleteEntityById(reviewProcessId);
		}
		return flag;
	}

	@Override
	protected Class<ReviewProcess> getEntity() {
		return ReviewProcess.class;
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

}
