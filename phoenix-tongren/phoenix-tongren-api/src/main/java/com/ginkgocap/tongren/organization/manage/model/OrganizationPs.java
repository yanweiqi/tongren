package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.model.BasicBean;
/**
 * 组织人脉表
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_organization_personsimple")
public class OrganizationPs extends BasicBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private long personId; //人脉id
	
	private String personName;//人脉名称
	
	private int gender;//性别 0：男   1：女
	
	private String picPath;//人脉头像
	
	private String company;//公司名称
	
	private long userId;//用户id
	
	private Timestamp createTime; //创建时间
	
	private long organizaitonId;//组织id
	
	@Id
	@GeneratedValue(generator = "OrganizationPsId")
	@GenericGenerator(name = "OrganizationPsId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationPsId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "person_id")
	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}
	@Column(name = "person_name")
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	@Column(name = "gender")
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	@Column(name = "pic_path")
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	@Column(name = "company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	@Column(name = "user_id")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Column(name = "organization_id")
	public long getOrganizaitonId() {
		return organizaitonId;
	}

	public void setOrganizaitonId(long organizaitonId) {
		this.organizaitonId = organizaitonId;
	}
}
