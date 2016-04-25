package com.ginkgocap.tongren.organization.manage.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.ginkgocap.tongren.organization.manage.model.Organization;


/**
 * 组织详情VO
 * @author Administrator
 *
 */
public class OrganizationVO extends Organization implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4688399288482802086L;
	
	private	String roleName;
	
	private Timestamp addTime; //成员加入时间
	
	private int memberSize;  //组织成员数
	
	private String path; //组织logo路径
	
	private List<String> industryName;//行业名称
	
	private List<String> classificationName;//类型名称
	
	private List<String> areaName;//地区名称
	
	private String createName;//组织创建者名称
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public int getMemberSize() {
		return memberSize;
	}

	public void setMemberSize(int memberSize) {
		this.memberSize = memberSize;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public List<String> getIndustryName() {
		return industryName;
	}

	public void setIndustryName(List<String> industryName) {
		this.industryName = industryName;
	}

	public List<String> getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(List<String> classificationName) {
		this.classificationName = classificationName;
	}

	public List<String> getAreaName() {
		return areaName;
	}

	public void setAreaName(List<String> areaName) {
		this.areaName = areaName;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
