package com.ginkgocap.tongren.organization.manage.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 组织部门
 * @author yanweiqi
 *
 */
@Entity
@Table(name="tb_organization_department")
public class OrganizationDep extends BasicBean implements Serializable,Comparable<OrganizationDep> {

	private static final long serialVersionUID = 2556072002342132424L;
	private long id;
	private String name;
	private Timestamp createTime;
	private Timestamp updateTime;
	private long createrId;
	private String description;//部门描述
	private long organizationId;//部门组织Id
	private int status;//部门状态
	private long pid;//上级部门Id
	private String level;//部门等级   例 1、1-1、1-2
	private List<OrganizationDepMember> depMembers;
	
	@Id
	@GeneratedValue(generator = "OrganizationDepId")
	@GenericGenerator(name = "OrganizationDepId", strategy = "com.ginkgocap.ywxt.framework.dal.dao.id.util.TimeIdGenerator", parameters = { @Parameter(name = "sequence", value = "OrganizationDepId") })
	@Column(name = "id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "dep_name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "create_time")
	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "update_time")
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "create_id")
	public long getCreaterId() {
		return createrId;
	}
	
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "organization_id")
	public long getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	@Column(name = "status")
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(name = "pid")
	public long getPid() {
		return pid;
	}
	
	public void setPid(long pid) {
		this.pid = pid;
	}
	
	@Column(name = "level")
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Transient 
	public List<OrganizationDepMember> getDepMembers() {
		return depMembers;
	}
	
	public void setDepMembers(List<OrganizationDepMember> depMembers) {
		this.depMembers = depMembers;
	}
	
	@Override
	public int compareTo(OrganizationDep o) {
		//return level.compareTo(o.getLevel());
		int left_level  = Integer.valueOf(level.replace("_", ""));
		int right_level = Integer.valueOf(o.getLevel().replace("_", ""));
		if(left_level > right_level){
			return 1;
		}
		else if(left_level == right_level){
			return 0;
		}
		else {
			return -1;
		}
	}
}
