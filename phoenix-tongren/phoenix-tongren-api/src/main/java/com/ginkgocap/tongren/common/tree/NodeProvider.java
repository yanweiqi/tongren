package com.ginkgocap.tongren.common.tree;

import java.util.List;

/**
 * 
 * @author hanxifa
 * 定义了数中结点数据提供方法
 */
public interface NodeProvider {

	public Node getNodeById(long id);
	
	public List<Node>  getChildNodesById(long id);
	
}
