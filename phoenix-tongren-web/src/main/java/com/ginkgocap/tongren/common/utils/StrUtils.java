package com.ginkgocap.tongren.common.utils;

/**
 * 
 * @author hanxifa
 * 字符串常用操作方法
 *
 */
public class StrUtils {
	public static boolean isIn(String str,String commaSpStr ){
		if(str==null||commaSpStr==null){
			return false;
		}
		String[] arstr=commaSpStr.split(",");
		for(String s:arstr){
			if(s.equals(str)){
				return true;
			}
		}
		return false;
	}
}
