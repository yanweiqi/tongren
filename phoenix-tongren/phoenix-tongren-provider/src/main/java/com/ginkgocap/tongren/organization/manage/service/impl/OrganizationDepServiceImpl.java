package com.ginkgocap.tongren.organization.manage.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.manage.exception.DepException;
import com.ginkgocap.tongren.organization.manage.model.OrganizationDep;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepMemberService;
import com.ginkgocap.tongren.organization.manage.service.OrganizationDepService;
import com.ginkgocap.tongren.organization.system.code.ApiCodes;

@Service("organizationDepService")
public class OrganizationDepServiceImpl extends AbstractCommonService<OrganizationDep> implements OrganizationDepService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationDepServiceImpl.class);
	
	@Autowired
	private OrganizationDepMemberService organizationDepMemberService;
	
	@Override
	public List<TreeNode<OrganizationDep>> getTreeDepByOrganizationId(long organizationId) throws Exception {
		List<TreeNode<OrganizationDep>> treeNodes = new LinkedList<TreeNode<OrganizationDep>>();
		List<OrganizationDep> deps = getDepsByOrganizationId(organizationId);
		try {
			if(null != deps){
				for (OrganizationDep dep : deps) {
					 TreeNode<OrganizationDep> treeNode = new TreeNode<OrganizationDep>(dep);
					 treeNode.setPid(dep.getPid());
					 treeNode.setNid(dep.getId());
					 treeNode.setNodeName(String.valueOf(dep.getId()));
					 treeNode.setT(dep);
					 String[] level = dep.getLevel().split("_");
					 if( level.length == 1 && dep.getPid() == 0){ 
					    treeNodes.add(treeNode);
					 }
					 else{
		                 for (TreeNode<OrganizationDep> node : treeNodes) {
							if(node.getNid() == treeNode.getPid()){
							   node.getChildren().add(treeNode);	
							}
							else{
								treeNode.storeChildrenNode(node, treeNode);
							}
						 }
					 }
				}
			}			
		} catch (Exception e) {
			logger.error(e.getMessage()+","+ApiCodes.DepIsNotExist,e);
			throw e;
		}
		return treeNodes;
	}
	
	@Override
	public TreeNode<OrganizationDep> getSubTreeDepById(long organizationDepId,long organizationId) throws Exception {
		try {
			List<TreeNode<OrganizationDep>> treeNodes = getTreeDepByOrganizationId(organizationId);	
			//JSONArray jsonArray = JSONArray.fromObject(treeNodes);
			//logger.info(jsonArray.toString());
			for (TreeNode<OrganizationDep> treeNode : treeNodes) {
				OrganizationDep dep = treeNode.getT();
				logger.debug("-------------"+dep.getId()+","+organizationDepId);
				logger.debug(String.valueOf(organizationDepId == dep.getId()));
	 			if(organizationDepId == dep.getId()){
	 			   return treeNode;
				}
	 			else{
	 			   TreeNode<OrganizationDep> node =  treeNode.findChildrenTreeNodeByNid(organizationDepId, treeNode);
	 			   if(node != null) return node;
	 			}
			}
			return null;
			
		} catch (Exception e) {
			logger.error(e.getMessage()+","+ApiCodes.DepIsNotExist,e);
			throw e;
		}
	}
	
	@Override
	public Map<String, OrganizationDep> delDepById(long id,long organizationId) throws Exception {
		ConcurrentHashMap<String, OrganizationDep> nodeMap = new ConcurrentHashMap<String, OrganizationDep>();
		try {
			OrganizationDep dep = getEntityById(id);
			if(null != dep){
			   nodeMap.put(String.valueOf(dep.getId()), dep);
			   TreeNode<OrganizationDep> treeNode = getSubTreeDepById(id, organizationId);
			   if(null != treeNode){
				  treeNode.convertTreeNodeToMap(treeNode, nodeMap); 
				  //JSONArray jsonMap = JSONArray.fromObject(nodeMap);
				  //logger.info(jsonMap.toString());
			   }
			   for (Map.Entry<String, OrganizationDep> entry: nodeMap.entrySet()) {
				  logger.info("Ready delete dep id :"+entry.getKey());
				  deleteEntityById(Long.valueOf(entry.getKey()));
				  nodeMap.remove(entry.getKey());
			   }
			}
		} catch (Exception e) {
			   for (Map.Entry<String, OrganizationDep> entry: nodeMap.entrySet()) {
				  logger.info("Ready delete dep id :"+entry.getKey()+",status:fail");
			   }
			   logger.error(e.getMessage(),e);
			   throw e;
		}
		return nodeMap;
	}
	
	@Override
	public OrganizationDep updateDepById(String depName, String description,long id) throws Exception {
		OrganizationDep dep = null;
		try {
			OrganizationDep cur_dep = getEntityById(id);
			if(null != cur_dep){
			   Timestamp t = new Timestamp(System.currentTimeMillis());
			   cur_dep.setName(depName);
			   cur_dep.setDescription(description);
			   cur_dep.setUpdateTime(t);
			   boolean status = update(cur_dep);
			   if(status) dep = cur_dep;
			}
			else{
				logger.info("update dep id "+id+":"+ApiCodes.DepIsNotExist);
				throw new DepException(ApiCodes.DepIsNotExist.getCode(), ApiCodes.DepIsNotExist.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+",update dep id "+id+" "+ApiCodes.DepNotUpdate,e);
			throw e;
		}
		return dep;
	}
	
	@Override
	public OrganizationDep add(long createrId, String depName,String discription, long organizationId, long pid) throws Exception {
		OrganizationDep p_dep = null;
		//保存之前判断
		OrganizationDep dep = getDepByOrganizationIdAndDepNameAndPid(organizationId, depName,pid);
		if(null == dep){
		   try {
			   List<OrganizationDep> deps = getDepByPid(pid);
			   String cur_level = buildLevel(deps,pid);
	           OrganizationDep new_dep = new OrganizationDep();
	           new_dep.setCreaterId(createrId);
	           new_dep.setOrganizationId(organizationId);
	           new_dep.setName(depName);
	           new_dep.setPid(pid);
	           new_dep.setDescription(discription);
	           new_dep.setLevel(cur_level);
	           Timestamp t = new Timestamp(System.currentTimeMillis());
	           new_dep.setCreateTime(t);
	           new_dep.setUpdateTime(t);
	           p_dep = save(new_dep); 
		   } catch (Exception e) {
			  logger.error(e.getMessage()+","+ApiCodes.DepNotSava.getDescription(),e);
			  throw new DepException(ApiCodes.DepIsExist.getCode(), ApiCodes.DepIsExist.getMessage());
		   }	
		}
		else{
			throw new DepException(ApiCodes.DepIsExist.getCode(), ApiCodes.DepIsExist.getMessage());
		}
		return p_dep;
	}

	private String buildLevel(List<OrganizationDep> deps,long pid) {
		String cur_level = null;
		if(null != deps && deps.size()>0){
		    Collections.sort(deps);
		    OrganizationDep maxDep = deps.get(deps.size()-1);
		    String[] pre_level = maxDep.getLevel().split("_");
		    pre_level[pre_level.length-1] = String.valueOf( Integer.valueOf(pre_level[pre_level.length-1])+1 );
		    cur_level = Arrays.toString(pre_level).replace(",", "_").replaceAll(" ", "");
		    cur_level = cur_level.substring(1, cur_level.length()-1);
		}
		else{
			OrganizationDep p_dep = getEntityById(pid);
			cur_level =  p_dep.getLevel()+"_"+1;
		}
	    return cur_level;
	}
	
	@Override
	public OrganizationDep getDepByOrganizationIdAndDepNameAndPid(long organizationId,String depName,long pid) throws Exception{
		OrganizationDep dep = null;
		try {
			 Long id = getMappingByParams("organizationDep_map_organizationId_depName_pid", new Object[]{organizationId,depName,pid});
			 if(null != id){
				 dep = getEntityById(id);
			 }
		} catch (Exception e) {
			logger.error(e.getMessage()+",获取部门异常...",e);
			throw  e;
		}
		return dep;
	}
	
	@Override
	public List<OrganizationDep> getDepByPid(long pid) throws Exception{
		List<OrganizationDep> deps = null;
		try {
			List<Long> ids = getKeysByParams("organizationDep_list_pid", new Object[]{pid});
			if (null != ids) {
				deps = getEntityByIds(ids);
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+",获取部门异常...",e);
			throw e;
		}
		return deps;
	}

	@Override
	public List<OrganizationDep> getDepsByOrganizationId(long organizationId) throws Exception {
		List<OrganizationDep> organizationDeps = null;
	    List<Long> ids = getKeysByParams("organizationDep_List_organizationId", new Object[]{organizationId});
	    if(null != ids){
	       organizationDeps = getEntityByIds(ids);
	       Collections.sort(organizationDeps);
	    }
		return organizationDeps;
	}
	
	@Override
	protected Class<OrganizationDep> getEntity() {
		return OrganizationDep.class;
	}

	@Override
	public Map<String, Object> doWork() {
		return null;
	}

	@Override
	public Map<String, Object> doComplete() {
		return null;
	}

	@Override
	public String doError() {
		return null;
	}

	@Override
	public Map<String, Object> preProccess() {
		return null;
	}

	@Override
	public List<OrganizationDep> createDefault(long createrId, long organizationId) throws Exception{
		List<OrganizationDep> organizationDeps = new ArrayList<OrganizationDep>();
		try {
			OrganizationDep market = new OrganizationDep();
			Timestamp t = new Timestamp(System.currentTimeMillis());
			market.setCreateTime(t);
			market.setUpdateTime(t);
			market.setName("市场营销");
			market.setStatus(0);
			market.setPid(0L);
			market.setLevel("1");
			market.setCreaterId(createrId);
			market.setDescription("市场营销为默认部门");
			market.setOrganizationId(organizationId);
			OrganizationDep p_market = save(market);
			if(null != p_market) organizationDeps.add(p_market);
			OrganizationDep manpower = new OrganizationDep();
			manpower.setCreateTime(t);
			manpower.setUpdateTime(t);
			manpower.setName("人力资源");
			manpower.setStatus(0);
			manpower.setPid(0L);
			manpower.setLevel("2");
			manpower.setCreaterId(createrId);
			manpower.setDescription("人力资源为默认部门");
			manpower.setOrganizationId(organizationId);
			OrganizationDep p_manpower = save(manpower);
			if(null != p_manpower) organizationDeps.add(p_manpower);
			
			OrganizationDep development = new OrganizationDep();
			development.setCreateTime(t);
			development.setUpdateTime(t);
			development.setName("技术开发");
			development.setStatus(0);
			development.setPid(0L);
			development.setLevel("3");
			development.setCreaterId(createrId);
			development.setDescription("技术开发为默认部门");
			development.setOrganizationId(organizationId);
			OrganizationDep p_development = save(development);
			if(null != p_development) organizationDeps.add(p_development);
			
			OrganizationDep product = new OrganizationDep();
			product.setCreateTime(t);
			product.setUpdateTime(t);
			product.setName("产品");
			product.setStatus(0);
			product.setPid(0L);
			product.setLevel("4");
			product.setCreaterId(createrId);
			product.setDescription("产品为默认部门");
			product.setOrganizationId(organizationId);
			OrganizationDep p_product = save(product);
			if(null != p_product) organizationDeps.add(p_product);
		} 
		catch (Exception e) {
			for (OrganizationDep dep : organizationDeps) {
				deleteEntityById(dep.getId()); //清理现场
			}
			logger.error(e.getMessage()+"创建默认部门失败!",e);
			throw e;
		}
		return organizationDeps;
	}

}
