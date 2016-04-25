package com.ginkgocap.tongren.common.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ginkgocap.tongren.common.CommonService;
import com.ginkgocap.tongren.common.FileInstance;
import com.ginkgocap.tongren.common.model.CommonConstants;
import com.ginkgocap.tongren.common.model.LocalCacheBeanWrap;
import com.ginkgocap.tongren.common.util.DaoSortType;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.framework.dal.dao.Dao;
import com.ginkgocap.ywxt.framework.dal.dao.exception.DaoException;
import com.ginkgocap.ywxt.metadata.model.Code;
import com.ginkgocap.ywxt.metadata.model.CodeRegion;
import com.ginkgocap.ywxt.metadata.service.code.CodeService;
import com.ginkgocap.ywxt.metadata.service.region.CodeRegionService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;


/**
 * @author yanweiqi
 * @param <T>
 */
public abstract class AbstractCommonService<T> implements CommonService<T>{
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractCommonService.class);
	
	@Autowired
	private Dao compositeDao;
	
	private Map<String ,Object> parameterMap;
	
	protected abstract Class<T> getEntity();
	
	@Autowired
	private UserService userService;//用户接口
	
	@Autowired
	private FileIndexService fileIndexService;
	
	@Autowired
	private CodeRegionService codeRegionService;
	
	@Autowired
	private CodeService codeService;

	@SuppressWarnings("unchecked")
	@Override
	public T save(T t) {
		Serializable id;
		T p_t = null;
		try {
			id = (Serializable) compositeDao.save(t);
			if(id == null){
			   logger.info("保存对象返回对象Id null,无法获取对象");
			}
			else{
			   p_t = (T) compositeDao.get(getEntity(), id);
			}
			
		} catch (DaoException e) {
			logger.error("save failed!,params:"+t,e);
			throw new RuntimeException(e);
		}	
		return p_t;
	}

	@Override
	public boolean update(T t) {
	    boolean isSuccess = false;
		try {
			isSuccess = compositeDao.update(t);
		} catch (DaoException e) {
			logger.error("update failed!,params:"+t,e);
			throw new RuntimeException(e);
		}
		return isSuccess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getEntityByIds(List<Long> ids) {
		List<T> entitys = null;
		try {
			if(ids == null || ids.size() ==0){
				logger.error("ids can not be empty or null");
			}else{
				entitys = compositeDao.getList(getEntity(),ids);
			}
		} catch (DaoException e) {
			logger.error("getEntityByIds failed!,params:"+convertToString(ids),e);
			throw new RuntimeException(e);
		}
		return entitys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getEntityById(long id) {
		T p_t = null;
		try {
			p_t = (T)compositeDao.get(getEntity(), id);
		} catch (DaoException e) {
			logger.error("getEntityById failed!,params:"+id,e);
			throw new RuntimeException(e);
		}
		return p_t;
	}
	@Override
	public boolean deleteEntityById(long id){
		boolean d_c = false;
		try {
			d_c = compositeDao.delete(getEntity(), id);
		} catch (Exception e) {
			logger.error("deleteEntityById failed!,params:"+id,e);
			throw new RuntimeException(e);
		}
		return d_c;
	}
	@Override
	public boolean deleteEntityByIds(List<Long> ids){
		boolean d_c = false;
		try {
			if(ids == null || ids.size() ==0){
				logger.error("ids can not be empty or null");
			}else{
				d_c = compositeDao.deleteList(getEntity(), ids);
			}
		} catch (Exception e) {
			logger.error("deleteEntityByIds failed!,params:"+convertToString(ids),e);
			throw new RuntimeException(e);
		}
		return d_c;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getKeysByParams(String list_name,Object params) {
		List<Long> list = null;
		try {
			list = compositeDao.getIdList(list_name, params);
		} catch (DaoException e) {
			logger.error("getKeysByParams failed! sql:"+list_name+",params:"+params,e);
			throw new RuntimeException(e);
		}
		return list;
	}
	@Override
	public Long getMappingByParams(String mapping_name,Object... params){
		Long id = null;
		try {
			id = (Long) compositeDao.getMapping(mapping_name, params);
			if(id == null){
				logger.info("返回id为空！");
			}
		} catch (DaoException e) {
			logger.error("getMapping failed! sql:"+mapping_name+",params:"+convertToString(params),e);
			throw new RuntimeException(e);
		}
		return id;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getKeysByParams(String list_name,Object... params){
		List<Long> list = null;
		try {
			list = compositeDao.getIdList(list_name, params);
		} catch (DaoException e) {
			logger.error("getKeysByParams failed! sql:"+list_name+",params:"+convertToString(params),e);
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getKeysByParams(String list_name,final DaoSortType dst,Object... params){
		List<Long> list = null;
		try {
			list = compositeDao.getIdList(list_name, params);
			if(list!=null){
				Collections.sort(list,new Comparator<Long>() {
					@Override
					public int compare(Long o1, Long o2) {
						if(dst.equals(DaoSortType.ASC)){
							return o1.compareTo(o2);
						}else if(dst.equals(DaoSortType.DESC)){
							return o2.compareTo(o1);
						}
						return  0;
					}
				});
			}
		} catch (DaoException e) {
			logger.error("getKeysByParams failed! sql:"+list_name+",params:"+convertToString(params),e);
			throw new RuntimeException(e);
		}
		return list;
	}
	
	@Override
	public int count(String list_name,Object... params){
		int c = 0;
		try {
			c = compositeDao.count(list_name, params);
		} catch (DaoException e) {
			logger.error("count failed! sql:"+list_name+",params:"+convertToString(params),e);
			throw new RuntimeException(e);
		}
		return c;
	}
	/**
	 * 
	 * @param list_name sql语句的id
	 * @param params sql语句对应的参数集合
	 * @param begin 起始的记录行 从0开始
	 * @param count 查询的条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getKeysByParams(String list_name,Object[] params,int begin,int count){
		List<Long> list = null;
		try {
			list = compositeDao.getIdList(list_name, params,begin,count);
		} catch (DaoException e) {
			logger.error("getKeysByParams failed! sql:"+list_name+",begin:"+begin+",count:"+count+",params:"+convertToString(params),e);
			throw new RuntimeException(e);
		}
		return list;
	}

	
	/**
	 * 功能描述:处理业务逻辑
	 * @return map
	 */
	public abstract Map<String,Object> doWork();
		
	/**
	 * 功能描述:处理业务逻辑之后的操作
	 * @return map
	 */
	public abstract Map<String,Object> doComplete();
	
	/**
	 * 功能描述:出错后的业务逻辑
	 * @return String
	 */
	public abstract String doError();
	
	/**
	 * 功能描述:预处理逻辑
	 * @return map
	 */
	public abstract Map<String, Object> preProccess();
	
	/**
	 * 功能描述:处理参数传递
	 * @param parameterMap
	 * @return
	 */
	public void setParameterMap(Map<String, Object> parameterMap){
		this.parameterMap = parameterMap;
	}
	
	public Map<String ,Object> getParameterMap(){
		return parameterMap;
	}

	public Dao getCompositeDao() {
		return compositeDao;
	}

	public void setCompositeDao(Dao compositeDao) {
		this.compositeDao = compositeDao;
	}

	private String convertToString(Object[] args){
		StringBuffer sb=new StringBuffer();
		if(args!=null&&args.length>0){
			for(Object o:args){
				sb.append(o.toString()).append(",");
			}
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private String convertToString(List args){
		StringBuffer sb=new StringBuffer();
		if(args!=null&&args.size()>0){
			for(Object o:args){
				sb.append(o.toString()).append(",");
			}
		}
		return sb.toString();
	}
	
	public String getLoginUserNameById(long id){
		if(id<=0){
			return null;
		}
		User user = userService.selectByPrimaryKey(id);
		if(user != null){
			return user.getName();
		}else{
			logger.info("not found user by id "+id);
			return null;
		}
	}
	
	protected String getUserPicURL(User u){
		String path=null;
		if(u == null){
			path= FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC;
		}else{
			if(!StringUtils.isEmpty(u.getPicPath())){
				path= FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + u.getPicPath();
			}else{
				logger.info("user picpath is null " + u.getId()+","+u.getName());
				path= FileInstance.FTP_WEB.trim() + FileInstance.FTP_URL.trim() + CommonConstants.DEFOULT_USER_PIC;
			}
		}
		return path;
	}
	
	/**
	 * 根据id获取用户对象
	 * @param userId
	 * @return
	 */
	Map<String, LocalCacheBeanWrap<FileIndex>> fileIndexCache=new ConcurrentHashMap<String, LocalCacheBeanWrap<FileIndex>>();
	protected FileIndex getFileIndexByTaskId(String taskId){
		if(taskId!=null){
			LocalCacheBeanWrap<FileIndex> lcb=fileIndexCache.get(taskId);
			if(lcb==null||lcb.isValid()==false){
				List<FileIndex> list=fileIndexService.selectByTaskId(taskId, "1");
				if(null != list && list.size()>0){
					FileIndex fileIndex = (FileIndex) list.get(0);
					lcb=new LocalCacheBeanWrap<FileIndex>(fileIndex,30*60*1000);
					fileIndexCache.put(taskId, lcb);
				}
				
			}
			return lcb.getBean();
		}else{
			return null;
		}
	}
	
	
	/**
	 * 根据id获取区信息
	 * @param userId
	 * @return
	 */
	Map<Long, LocalCacheBeanWrap<CodeRegion>> codeRegionCache=new ConcurrentHashMap<Long, LocalCacheBeanWrap<CodeRegion>>();
	protected CodeRegion getCodeRegionById(Long codeRegionId){
		if(codeRegionId!=null){
			LocalCacheBeanWrap<CodeRegion> lcb=codeRegionCache.get(codeRegionId);
			if(lcb==null||lcb.isValid()==false){
				CodeRegion codeRegion= codeRegionService.selectByPrimaryKey(codeRegionId);
				lcb=new LocalCacheBeanWrap<CodeRegion>(codeRegion,24*60*60*1000);
				codeRegionCache.put(codeRegionId, lcb);
			}
			return lcb.getBean();
		}else{
			return null;
		}
	}
	
	/**
	 * 根据id获取区信息
	 * @param userId
	 * @return
	 */
	Map<Long, LocalCacheBeanWrap<Code>> codeCache=new ConcurrentHashMap<Long, LocalCacheBeanWrap<Code>>();
	protected Code getCodeById(Long codeId){
		if(codeId!=null){
			LocalCacheBeanWrap<Code> lcb=codeCache.get(codeId);
			if(lcb==null||lcb.isValid()==false){
				Code code= codeService.selectByPrimarKey(codeId);
				lcb=new LocalCacheBeanWrap<Code>(code,24*60*60*1000);
				codeCache.put(codeId, lcb);
			}
			return lcb.getBean();
		}else{
			return null;
		}
	}
	protected String getUserPicURL(long userId){
		User user = userService.selectByPrimaryKey(userId);
		return getUserPicURL(user);
	}
}
