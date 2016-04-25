package com.ginkgocap.tongren.organization.document.model;

import java.io.Serializable;
import java.util.List;

import com.ginkgocap.tongren.common.model.BasicBean;

/**
 * 文档目录实体
 * @author hanxifa
 *
 */
public class DocumentCatalogue extends BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//唯一 id
	
	private String name;//目录名称
	
	private long pid;//父id
	
	private long organizationId;//组织id

	private List<DocumentCatalogue> children; //此目录下的子目录
	
	private long userId;//目录创建者id

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public List<DocumentCatalogue> getChildren() {
		return children;
	}

	public void setChildren(List<DocumentCatalogue> children) {
		this.children = children;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "DocumentCatalogue [id=" + id + ", name=" + name + ", pid=" + pid + ", organizationId=" + organizationId + ", userId=" + userId + "]";
	}
	
	
	
	
}
