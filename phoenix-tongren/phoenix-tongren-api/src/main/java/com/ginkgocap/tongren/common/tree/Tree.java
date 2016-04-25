package com.ginkgocap.tongren.common.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * @author hanxifa
 * @param <T>
 */
public class Tree<T extends Node> {

	private NodeProvider nodeProvider=null;
	
	private Node node;


	public Tree(NodeProvider nodeProvider) {
		super();
		this.nodeProvider = nodeProvider;
	}
	

	public Node getTreeNode()
	{
		return this.node;
	}
	/**
	 * 返回所有节点，不包含父子关系
	 * @return
	 */
	public List<Node> iteratorAllNodes(){
		List<Node> allNode=new ArrayList<Node>();
		allNode.add(node);
		addChildNodes(allNode,node);
		
		List<Node> allNoChildrenNode=new ArrayList<Node>();
		for(Node acn: allNode){
			Node node=null;
			try {
				node = acn.getClass().newInstance();
				BeanUtils.copyProperties(node, acn);
				node.clearChildren();
				allNoChildrenNode.add(node);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return allNoChildrenNode;
	}
	
	private void addChildNodes( List<Node> list,Node pnode){
		List<Node> childs=pnode.getChildren();
		if(childs!=null&&childs.size()>0){
			for(Node cn:childs){
				list.add(cn);
				addChildNodes(list,cn);
			}
		}
	}
	/**
	 * 递归查找指定id的子节点
	 * @param id
	 * @return
	 */
	public Node lookUpChildById(long id){
		return lookUpChild(node,id);
	}
	
	private Node lookUpChild(Node pnode,long id){
		if(pnode.getId()==id){
			return pnode;
		}
		List<Node> list=pnode.getChildren();
		if(list!=null&&list.size()>0){
			for(Node cn:list){
				return lookUpChild(cn, id);
			}
		}
		return null;
	}
	/**
	 * 根据最顶层的id构建树
	 * @param id
	 */
	public void buildTree(long id){
		Node node=nodeProvider.getNodeById(id);
		if(node!=null){
			buildChildred(node);
		}
		this.node=node;
	}
	
	private void buildChildred(Node parentNode){
		List<Node> childs=	nodeProvider.getChildNodesById(parentNode.getId());
		if(childs!=null&&childs.size()>0){
			for(Node cn:childs){
				parentNode.addChild(cn);
				buildChildred(cn);
			}
		}
	}
}
