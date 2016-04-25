package com.ginkgocap.tongren.organization.review.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;
import com.ginkgocap.tongren.organization.review.service.ReviewObjectService;

@Service("reviewObjectService")
public class ReviewObjectServiceImpl extends  AbstractCommonService<ReviewObject> implements ReviewObjectService{

	//private static final Logger logger = LoggerFactory.getLogger(ReviewObjectServiceImpl.class);
	
	@Override
	protected Class<ReviewObject> getEntity() {return ReviewObject.class;}
	@Override
	public Map<String, Object> doWork() {return null;}
	@Override
	public Map<String, Object> doComplete() {return null;}
	@Override
	public String doError() {return null;}
	@Override
	public Map<String, Object> preProccess() {return null;}
	@Override
	public List<ReviewObject> getReviewList(long reviewProcessId) {
		
		List<ReviewObject> lst = new ArrayList<ReviewObject>();
		try{
			List<Long> objectIds = getKeysByParams("reviewObject_List_reviewId", reviewProcessId);
			for(Long id: objectIds){
				ReviewObject o = getEntityById(id);
				if(o != null){
					lst.add(o);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return lst;
	}

}
