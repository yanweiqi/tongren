/**
 * 
 */
package com.ginkgocap.tongren.common.utils;

import java.util.Collection;

/**
 * 通用工具类
 * @author liny
 *
 */
public class RoUtil {
	/**
	 * 
	 * 判断对象是否为空 <br/>
	 * 
	 * @param obj 目标对象 <br/>
	 * @return true 为空 false 不为空 <br/>
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj){
		if(obj==null){
			return true;
		}
		
		if(obj.toString().trim().isEmpty()){
			return true;
		}
		
		if(obj instanceof Collection){
			return ((Collection)obj).size()==0;
		}
		
		return false;
	}
}
