package com.ginkgocap.tongren.organization.review.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewObject;

public interface ReviewObjectService extends CommonService<ReviewObject>{
	
	public List<ReviewObject> getReviewList(long reviewProcessId);

}
