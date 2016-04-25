package com.ginkgocap.tongren.common.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author yanweiqi
 * @since 2015-10-28 
 * @version 桐人 V1.0
 */
public class TreeNode<T> implements Serializable {
    
	private static Logger logger = LoggerFactory.getLogger(TreeNode.class);
	private static final long serialVersionUID = -7084382746950521108L;
	private long nid;         //当前节点ID
	private long pid;         //父ID
	private String nodeName;
	private T t;
	private List<TreeNode<T>> children = new LinkedList<TreeNode<T>>();
	
	public TreeNode(T t) {
		this.t = t;
	}

	public long getNid() {
		return nid;
	}

	public void setNid(long nid) {
		this.nid = nid;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}	
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 功能描述：在当前节点上存储子节点
	 * @param currentNode 当前节点
	 * @param childreNode 子节点
	 */
	public void storeChildrenNode(TreeNode<T> currentNode,TreeNode<T> childreNode){    
    	if(currentNode.getChildren().size() > 0){
    		List<TreeNode<T>> currentNodes =  currentNode.getChildren();
    		for (TreeNode<T> note : currentNodes) {
    		    if(note.getNid() == childreNode.getPid()){
    			   note.getChildren().add(childreNode);
    			}
			    else{
			    	storeChildrenNode(note, childreNode);
				} 
			}
    	}
	}
	
	
	/**
	 * 根据节点id查询子节点
	 * @param nid 节点Id
	 * @param TreeNode<T> 当前节点
	 * @return TreeNode<T> 子节点
	 */
    public TreeNode<T> findChildrenTreeNodeByNid(long nid,TreeNode<T> treeNode){
    	TreeNode<T> result_node = null;
    	if(treeNode.getChildren().size() > 0){
    	   List<TreeNode<T>> childrenTreeNodes =  treeNode.getChildren();
    	   for (TreeNode<T> note : childrenTreeNodes) {    		   
    		   logger.debug("=================="+note.getNid()+","+nid);
    		   logger.debug(String.valueOf(note.getNid() == nid));
			   if(note.getNid() == nid){
				  result_node = note;
			   }
			   else{
				  findChildrenTreeNodeByNid(nid, note);
			   }
		   }
    	}
    	return result_node;
    }
    
    /**
     * 根据节点nodeName查询子节点
     * @param nodeName 节点名称
	 * @param TreeNode<T> 当前节点
	 * @return TreeNode<T> 子节点
     */
    public TreeNode<T> findChildrenTreeNodeByName(String nodeName,TreeNode<T> treeNode){
    	if(treeNode.getChildren().size() > 0){
    	   List<TreeNode<T>> childrenTreeNodes =  treeNode.getChildren();
    	   for (TreeNode<T> note : childrenTreeNodes) {
    		   logger.debug("=================="+note.getNodeName()+","+nodeName);
    		   logger.debug(String.valueOf(nodeName.equals(note.getNodeName())));
			   if(nodeName.equals(note.getNodeName())){
	              return note;
			   }
			   else{
				   findChildrenTreeNodeByName(nodeName, note);
			   }
		   }
    	}
    	return null;
    }
    
    public void convertTreeNodeToMap(TreeNode<T> treeNode,Map<String, T> nodeMap){
    	nodeMap.put(treeNode.getNodeName(), treeNode.getT());	
    	if(treeNode.getChildren().size() > 0){
    	   List<TreeNode<T>> childrenTreeNodes =  treeNode.getChildren();
    	   for (TreeNode<T> node : childrenTreeNodes) {
    		   if(node != null){
    	          String name = node.getNodeName();
    	          T t = node.getT();
    			  nodeMap.put(name, t);
    			  JSONObject jsonObject = JSONObject.fromObject(node);
    			  logger.debug(jsonObject.toString());
    			  convertTreeNodeToMap(node,nodeMap);
    		   }
    	   }
    	}
    }
}
