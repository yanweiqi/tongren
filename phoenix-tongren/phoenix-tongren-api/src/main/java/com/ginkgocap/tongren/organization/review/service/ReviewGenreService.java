package com.ginkgocap.tongren.organization.review.service;

import java.util.List;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.organization.review.model.ReviewGenre;

public interface ReviewGenreService extends CommonService<ReviewGenre>{
	
	/**
	 * 查询组织名称下的子类型
	 * @param pid  申请流程id
	 * 
	 */
	public List<ReviewGenre> getGenreByProId(long pid);

	/**
	 * 根据id查询流程类型
	 * @param id
	 * @return
	 */
	public ReviewGenre getGenreById(long id);
}
