package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.model.BasicBean;
import com.ginkgocap.tongren.common.util.ChineseToEnglish;

/**
 * 组织
 * @author yanweiqi
 */
@Entity
@Table(name="tb_organization")
public class Organization extends BasicBean implements Serializable{
	
	private static final long serialVersionUID = 2209748151579526684L;
	private long id;
	private String name;         //组织名称
	private int classification;  //组织类型
	private String introduction; //组织简介
	private String logo;         //组织LOGO的taskId
	private Timestamp createTime;//组织创建时间
	private Timestamp updateTime;//更新时间
	private long createrId;      //创建者ID
	private int area;	//地区
	private int industry;//行业
	private int status ;//状态  0：正常   1：解散
	private int performSize;//完成项目数
	private String nameSpelling;//汉语拼音
	
	
	@Id
	@GeneratedValue(generator = "OrganizationId")
	@GenericGenerator(name = "OrganizationId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="classification")
	public int getClassification() {
		return classification;
	}
	public void setClassification(int classification) {
		this.classification = classification;
	}
	@Column(name="introduction")
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Column(name="logo")
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@Column(name="create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="create_id")
	public long getCreaterId() {
		return createrId;
	}
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	@Column(name="area")
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	@Column(name="industry")
	public int getIndustry() {
		return industry;
	}
	public void setIndustry(int industry) {
		this.industry = industry;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name="perform_size")
	public int getPerformSize() {
		return performSize;
	}
	public void setPerformSize(int performSize) {
		this.performSize = performSize;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Column(name="name_spelling")
	public String getNameSpelling() {
		if(nameSpelling==null||nameSpelling.length()==0){
			nameSpelling=ChineseToEnglish.convertToSpell(name);
		}
		return nameSpelling;
	}
	public void setNameSpelling(String nameSpelling) {
		this.nameSpelling = nameSpelling;
	}
	
	
}
