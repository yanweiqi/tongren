/*
 * 文件名： MakePrimaryKey.java
 * 创建日期： 2015年10月29日
 * Copyright(C) 2015, by linY.
 * 作者: 林阳 [linyang@gintong.com]
 *
 */
package com.ginkgocap.tongren.common.utils;
import java.sql.Timestamp;

 /**
 *  获得主键方法
 * @author 林阳 [linyang@gintong.com]
 * @version 
 * @since 2015年10月29日
 */
public class MakePrimaryKey {
	@SuppressWarnings("deprecation")
	public static String getPrimaryKey(){
        int year=new Timestamp(System.currentTimeMillis()).getYear();
        int month=(new Timestamp(System.currentTimeMillis()).getMonth()+1);
        int day=new Timestamp(System.currentTimeMillis()).getDate();
        int hours=new Timestamp(System.currentTimeMillis()).getHours();
        int minte=new Timestamp(System.currentTimeMillis()).getMinutes();
        long time=new Timestamp(System.currentTimeMillis()).getTime();
        
        String num=String.valueOf(year).substring(1)+String.valueOf(month)+String.valueOf(day)
        +String.valueOf(hours)+String.valueOf(minte)+String.valueOf(time).substring(8, 13);
        return num;
    }
}
