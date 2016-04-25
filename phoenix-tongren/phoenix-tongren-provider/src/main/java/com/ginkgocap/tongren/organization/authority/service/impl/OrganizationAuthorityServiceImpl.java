package com.ginkgocap.tongren.organization.authority.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.tongren.common.model.TreeNode;
import com.ginkgocap.tongren.common.service.impl.AbstractCommonService;
import com.ginkgocap.tongren.organization.authority.model.OrganizationAuthority;
import com.ginkgocap.tongren.organization.authority.service.OrganizationAuthorityService;
import com.ginkgocap.tongren.organization.system.model.OrganizationAuthorities;

/**
 * 
 * @author yanweiqi
 *
 */
@Service("organizationAuthorityService")
public class OrganizationAuthorityServiceImpl extends AbstractCommonService<OrganizationAuthority> implements OrganizationAuthorityService{

	private static final Logger logger = LoggerFactory.getLogger(OrganizationAuthorityServiceImpl.class);
	
	@Override
	public OrganizationAuthority getDefaultAuthoritysByName(String authorityName) throws Exception {
		OrganizationAuthority oa = null;
		try {
			Long id = getMappingByParams("organizationAuthority_map_authorityName",new Object[]{authorityName});
			if(null != id){
			   oa = getEntityById(id);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return oa;
	}
	
	@Override
	public List<OrganizationAuthority> createDefault(long createId, long organizationId) throws Exception{
		List<OrganizationAuthority> p_oas = new ArrayList<OrganizationAuthority>();
		OrganizationAuthority poa = null;		
		for(OrganizationAuthorities oas:OrganizationAuthorities.values()){
			OrganizationAuthority oa = new OrganizationAuthority();
			Timestamp t = new Timestamp(System.currentTimeMillis());
			oa.setCreateTime(t);
			oa.setUpdateTime(t);
			oa.setAuthorityName(oas.name());
			oa.setAuthorityNo(oas.getKey());
			oa.setCreaterId(createId);
			oa.setDescription(oas.getValue());
			oa.setOrganizationId(organizationId);
			oa.setLevel(oas.getKey().split("-").length);
		    int length = oas.getKey().length();
		    if(length > 1){
		    	int index  = oas.getKey().lastIndexOf("-");
			    String p_no = oas.getKey().substring(0,index);
			    Long id = getMappingByParams("organizationAuthority_map_authorityNo", new Object[]{p_no});
			    poa = getEntityById(id);
			    if(poa != null){
			       oa.setPid(poa.getId());
			    }
		    }
		    OrganizationAuthority check_oa_exist = getDefaultAuthoritysByName(oa.getAuthorityName());
		    if(null == check_oa_exist){
		    	OrganizationAuthority p_oa = save(oa);
		    	p_oas.add(p_oa);
		    }
		    else{
		    	logger.warn("AuthorityName:"+oa.getAuthorityName()+",isExist:true");
		    }
		}
		return p_oas;
	}

	@Override
	public List<OrganizationAuthority> getDefaultAuthoritys() throws Exception{
		List<Long> ids = getKeysByParams("organizationAuthority_default_List_organizationId", new Object[]{0l});
		return getEntityByIds(ids);
	}
	
	@Override
	public List<OrganizationAuthority> getDefinedAuthoritysByAuthorityNo(String authorityNo, long organizationId) throws Exception{
		return null;
	}

	@Override
	public List<TreeNode<OrganizationAuthority>> getAuthorityTreeNodes() throws Exception{
		List<TreeNode<OrganizationAuthority>> treeNodes = new LinkedList<TreeNode<OrganizationAuthority>>();
		List<OrganizationAuthority> oas = getDefaultAuthoritys();
		for (OrganizationAuthority oa : oas) {
			 TreeNode<OrganizationAuthority> treeNode = new TreeNode<OrganizationAuthority>(oa);
			 treeNode.setPid(oa.getPid());
			 treeNode.setNid(oa.getId());
			 treeNode.setNodeName(oa.getAuthorityName());
			 treeNode.setT(oa);
			 if(oa.getLevel() == 1 && oa.getPid() == 0){ 
			    treeNodes.add(treeNode);
			 }
			 else{
                 for (TreeNode<OrganizationAuthority> node : treeNodes) {
					if(node.getNid() == treeNode.getPid()){
					   node.getChildren().add(treeNode);	
					}
					else{
						treeNode.storeChildrenNode(node, treeNode);
					}
				 }
			 }
		}
		return treeNodes;
	}
    
    @Override
    public TreeNode<OrganizationAuthority> getChildrenTreeNodeByName(String authorityName) throws Exception{
    	List<TreeNode<OrganizationAuthority>> treeNodes = getAuthorityTreeNodes(); //缺省权限集合
	    for (TreeNode<OrganizationAuthority> treeNode : treeNodes) {
		   OrganizationAuthority oa = treeNode.getT();
		   logger.debug("=================="+oa.getAuthorityName()+","+authorityName);
		   logger.debug(String.valueOf(authorityName.equals(oa.getAuthorityName())) );
		   if(authorityName.equals(oa.getAuthorityName())){
			  return treeNode;
		   }
		   else{
			   TreeNode<OrganizationAuthority> node = treeNode.findChildrenTreeNodeByName(authorityName, treeNode);
			   if(node != null) return node;
		   }
	    }
    	return null;
    }
    
    
	@Override
	public TreeNode<OrganizationAuthority> getChildrenTreeNodeById(long authorityId) throws Exception{
		TreeNode<OrganizationAuthority> result_treeNode = null;
		List<TreeNode<OrganizationAuthority>> treeNodes = getAuthorityTreeNodes(); //缺省权限集合	
		for (TreeNode<OrganizationAuthority> treeNode : treeNodes) {
			OrganizationAuthority oa = treeNode.getT();
			logger.debug("-------------"+oa.getId()+","+authorityId);
			logger.debug(String.valueOf(authorityId == oa.getId()));
 			if(authorityId == oa.getId()){
 			   result_treeNode = treeNode;
			}
 			else{
 			   TreeNode<OrganizationAuthority> node =  treeNode.findChildrenTreeNodeByNid(authorityId, treeNode);
 			   if(node != null) result_treeNode = node;
 			}
		}
		return result_treeNode;
	}
	
	@Override
	public OrganizationAuthority getOrganizationAuthorityById(long authorityId) throws Exception {
		OrganizationAuthority organizationAuthority = null;
		try {
			organizationAuthority = getEntityById(authorityId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		return organizationAuthority;
	}
	
    @Override
	public Map<String, OrganizationAuthority> getOrganizationAuthorityMapByAuthorityId(long authorityId) throws Exception {
		Map<String, OrganizationAuthority> roleAuthorityMap = new LinkedHashMap<String, OrganizationAuthority>();
		TreeNode<OrganizationAuthority> treeNode = getChildrenTreeNodeById(authorityId);
	    if(null == treeNode){
		   OrganizationAuthority oa = getOrganizationAuthorityById(authorityId);
		   roleAuthorityMap.put(oa.getAuthorityName(), oa);
	    }
	    else{
		   treeNode.convertTreeNodeToMap(treeNode, roleAuthorityMap);
	    }
	    return roleAuthorityMap;
	}
	
	@Override
	protected Class<OrganizationAuthority> getEntity() {
		return OrganizationAuthority.class;
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

}
