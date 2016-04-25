package com.ginkgocap.tongren.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static Logger log = LoggerFactory.getLogger(DateUtil.class);
	/**
	 * 取当前日期的 0点 0分 0秒
	 * @param d
	 * @return
	 */
	public static Date trunck(Date d){
		Calendar c1=Calendar.getInstance();
		c1.setTimeInMillis(d.getTime());
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}
	/**
	 * 两个时间是否同一天
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1,Date d2){
		d1=trunck(d1);
		d2=trunck(d2);
		return d1.getTime()==d2.getTime();
	}
	/**
	 * 给定日期增加相应的天数
	 * @param d 
	 * @param m 增加的天数，可为负数
	 * @return
	 */
	public static Date addDay(Date d,int m){
		Calendar c1=Calendar.getInstance();
		c1.setTimeInMillis(d.getTime());
		c1.add(Calendar.DAY_OF_MONTH, m);
		return c1.getTime();
	}
	/**
	 * 给定的日期是否是工作日，目前规定除周六日外都算工作日
	 * @param date
	 * @return
	 */
	static int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
	public static boolean isWorkDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        int week= weekDays[w];
        return !(week==6||week==7);
	}
	/**
	 * 把年月 格式化为日期对象
	 * @param month
	 * @return
	 */
	public static Date parseYearMonth(String month)
	{
		String pattern = "[0-9]{4}-[0-9]{2}";
		if(month==null||month.matches(pattern)==false){
			log.info("month format is invailed!  "+month);
			return null;
		}
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
		try {
			return sf.parse(month);
		} catch (ParseException e) {
			log.error("parse month strt failed "+month,e);
			return null;
		}
	}
	
	public static String formatDateToDay(Date date){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(date);
	}
	public static String formatDate(Date date){
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}
	public static void main(String[] args) {
		System.out.println(parseYearMonth("2015-05"));
	}
}
