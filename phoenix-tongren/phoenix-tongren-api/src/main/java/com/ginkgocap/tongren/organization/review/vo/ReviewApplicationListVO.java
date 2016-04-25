package com.ginkgocap.tongren.organization.review.vo;

import java.util.ArrayList;
import java.util.List;

import com.ginkgocap.tongren.organization.review.model.ReviewApplication;

/**
 * 申请列表对象定义
 * @author hanxifa
 *
 */
public class ReviewApplicationListVO extends ReviewApplication{

	private static final long serialVersionUID = 1L;
	
	private String reviewGenreName;//流程类型名称
	
	private int progress;//审核进度 0 新申请 1 审核中（没有走到最后一步审核） 2 审核完毕
	
	private int status;//审核状态 0审核中、1、撤回申请 2审核通过、3审核拒绝
	
	private String applyUserName;
	
	private List<ReviewRecordsVO> reviewDetail=new ArrayList<ReviewRecordsVO>();

	public String getReviewGenreName() {
		return reviewGenreName;
	}

	public void setReviewGenreName(String reviewGenreName) {
		this.reviewGenreName = reviewGenreName;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<ReviewRecordsVO> getReviewDetail() {
		return reviewDetail;
	}

	public void setReviewDetail(List<ReviewRecordsVO> reviewDetail) {
		this.reviewDetail = reviewDetail;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}
	
	

}
