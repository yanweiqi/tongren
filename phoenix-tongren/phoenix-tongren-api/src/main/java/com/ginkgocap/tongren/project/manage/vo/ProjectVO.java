package com.ginkgocap.tongren.project.manage.vo;

import java.io.Serializable;
import java.util.Set;

import com.ginkgocap.tongren.project.manage.model.Apply;
import com.ginkgocap.tongren.project.manage.model.Project;
/**
 * 项目展现VO
 * @author Administrator
 *
 */

public class ProjectVO extends Project implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String createName;
	

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
