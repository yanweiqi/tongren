package com.ginkgocap.tongren.organization.review.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;
import com.ginkgocap.tongren.organization.review.service.ReviewGenreService;

@Service("reviewGenreService")
public class ReviewGenreServiceImpl extends AbstractCommonService<ReviewGenre> implements ReviewGenreService{
	
	//private static final Logger logger = LoggerFactory.getLogger(ReviewGenreServiceImpl.class);
	
	@Override
	public List<ReviewGenre> getGenreByProId(long pid) {
		
			List<ReviewGenre> list = new ArrayList<ReviewGenre>();
		try{
				List<Long> ids = getKeysByParams("reviewGenre_List_reviewId",pid);
			if(ids != null && !ids.isEmpty()){
				list =  getEntityByIds(ids);
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
			return list;
	}

	public ReviewGenre getGenreById(long id) {
		return this.getEntityById(id);
	}
	
	@Override
	protected Class<ReviewGenre> getEntity() {return ReviewGenre.class;}
	@Override
	public Map<String, Object> doWork() {return null;}
	@Override
	public Map<String, Object> doComplete() {return null;}
	@Override
	public String doError() {return null;}
	@Override
	public Map<String, Object> preProccess() {return null;}
	

}
