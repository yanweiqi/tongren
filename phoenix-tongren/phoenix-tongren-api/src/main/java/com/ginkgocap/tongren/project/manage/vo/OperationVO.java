package com.ginkgocap.tongren.project.manage.vo;

import com.ginkgocap.tongren.project.manage.model.Operation;

/**
 * 查看任务进度视图类
 * @author Administrator
 *
 */
public class OperationVO extends Operation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String docPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
}
