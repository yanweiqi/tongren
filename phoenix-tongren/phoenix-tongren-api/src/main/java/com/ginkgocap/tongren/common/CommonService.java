package com.ginkgocap.tongren.common;

import java.util.List;
import java.util.Map;

import com.ginkgocap.tongren.common.util.DaoSortType;

/**
 * 类名称：CommonDAO  
 * 类描述： commonDAO基类
 * 创建人：YWQ  
 */
public interface CommonService<T> {

	/**
	 * 功能描述： 根据id List集合查询，实体List。
	 * @param sql
	 * @param  List<Long>
	 * @return:List<T>
	 */
	public List<T> getEntityByIds(List<Long> ids);
		
	/**
	 * 功能描述：根据Id查询实体。
	 * @param id
	 * @return:T
	 */
	public T getEntityById(long id);
	
	/**
	 * 根据Id返回实体对象id List
	 * @param Object
	 * @return List
	 */
	public List<Long> getKeysByParams(String list_name,Object params);
	/**
	 * 
	 * 功能描述：   多参数返回唯一Id
	 *                                                       
	 * @param mapping_name
	 * @param params
	 * @return  long                                                                                             
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月13日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public Long getMappingByParams(String mapping_name,Object... params);

	/**
	 * 功能描述：   根据Id返回实体对象id List      
	 *                                                       
	 * @param list_name xmlname
	 * @param params 集合
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月13日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	public List<Long> getKeysByParams(String list_name,Object... params);
	
	/**
	 * 
	 * @param listName
	 * @param dst返回的数据 增加排序功能
	 * @param params
	 * @return
	 */
	public List<Long> getKeysByParams(String listName,DaoSortType dst,Object... params);
	
	/**
	 * 
	 * @param list_name sql语句的id
	 * @param params sql语句对应的参数集合
	 * @param begin 起始的记录行 从0开始
	 * @param count 查询的条数
	 * @return
	 */
	public List<Long> getKeysByParams(String list_name,Object[] params,int begin,int count);
	
	/**
	 * 功能描述：保存实体
	 * @param T
	 * @param id
	 * @return T
	 */
	public T save(T t);
	
	/**
	 * 功能描述 ：更新实体
	 * @param T
	 * @param id
	 * @return T
	 */
	public boolean update(T t);
	
	/**
	 * 功能描述:处理业务逻辑
	 * @return map
	 */
	public  Map<String,Object> doWork();
		
	/**
	 * 功能描述:处理业务逻辑之后的操作
	 * @return map
	 */
	public  Map<String,Object> doComplete();
	
	/**
	 * 功能描述:出错后的业务逻辑
	 * @return String
	 */
	public  String doError();
	
	/**
	 * 功能描述:预处理逻辑
	 * @return map
	 */
	public  Map<String, Object> preProccess();
	
	/**
	 * 功能描述:参数传递
	 * @param parameterMap
	 * @return
	 */
	public void setParameterMap(Map<String, Object> parameterMap);
	
	/**
	 * 功能描述:参数传递
	 * @param parameterMap
	 * @return
	 */
	public Map<String ,Object> getParameterMap();

	/**
	 * 功能描述：    删除单个ID     
	 *                                                       
	 * @param id
	 * @return true(删除成功) or false(删除失败)                                                                                               
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	boolean deleteEntityById(long id);
	/**
	 * 功能描述：     删除多个IDS实体    
	 *                                                       
	 * @param ids
	 * @return                                                                                                 
	 * @author 林阳 [linyang@gintong.com]
	 * @since 2015年10月16日
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 *
	 */
	boolean deleteEntityByIds(List<Long> ids);
	

	int count(String list_name, Object[] params);
	
	public String getLoginUserNameById(long id);
	
}
