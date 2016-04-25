package com.ginkgocap.tongren.common.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.ginkgocap.tongren.common.model.BasicBean;


/**
 * 树的节点
 * @author hanxifa
 *
 */
public abstract class  Node extends BasicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Node> children=new ArrayList<Node>();
	/**
	 * 返回节点的id
	 * @return
	 */
	public abstract long getId();
	
	/**
	 * 返回节点的父ID
	 * @return
	 */
	@Transient
	public abstract long getPid();
	
	@Transient
	public void addChild(Node child){
		if(child!=null){
			children.add(child);
		}
	}

	@Transient
	public  List<Node>  getChildren(){
		return children;
	}
	 
	public void clearChildren(){
		children.clear();
	}
}
