/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.qyh.demo.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 格式化日期时间字符串
	 * @param date
	 * @param format 常用格式1.yyyy-MM-dd HH:mm:ss 2.yyyy-MM-dd
	 * @return
	 */
	public static String formatDateTime(Date date, String format) {
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}
	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
    
 /**
  * 得到当前日期的开始时间
  * @return
  */
 	@SuppressWarnings("static-access")
 	public static Date getCurrentDateTimeStart() {
 		Calendar cl = Calendar.getInstance();
 		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 0,
 				0, 0);
 		return cl.getTime();
 	}

 	/**
 	 * 得到当前日期的结束时间
 	 * @return
 	 */
 	@SuppressWarnings("static-access")
 	public static Date getCurrentDateTimeEnd() {
 		Calendar cl = Calendar.getInstance();
 		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 23,
 				59, 59);
 		return cl.getTime();
 	}

 	/**
 	 * 得到当前周开始时间
 	 * @return
 	 */
 	public static Date getCurrentWeekStartDate() {
 		Calendar cl = Calendar.getInstance();
 		int day = cl.get(Calendar.DAY_OF_WEEK);
 		if (day == 1) {
 			day = -6;
 		} else {
 			day = -1 * (day - 2);
 		}
 		cl.add(Calendar.DAY_OF_WEEK, day);
 		Date firstDay = cl.getTime();
 		return getDateTimeStart(firstDay);
 	}

 	/**
 	 * 得到当前周结束时间
 	 * @return
 	 */
 	public static Date getCurrentWeekEndDate() {
 		Calendar cl = Calendar.getInstance();
 		int day = cl.get(Calendar.DAY_OF_WEEK);
 		if (day == 1) {
 			day = 0;
 		} else {
 			day = 8 - day;
 		}
 		cl.add(Calendar.DAY_OF_WEEK, day);
 		Date endDay = cl.getTime();
 		return getDateTimeEnd(endDay);
 	}

 	/**
 	 * 得到当前月开始时间
 	 * @return
 	 */
 	public static Date getCurrentMonthStartDate() {
 		Calendar cl = Calendar.getInstance();
 		cl.set(Calendar.DAY_OF_MONTH, cl
 				.getActualMinimum(Calendar.DAY_OF_MONTH));
 		return getDateTimeStart(cl.getTime());
 	}

 	/**
 	 * 得到当前月结束时间
 	 * @return
 	 */
 	public static Date getCurrentMonthEndDate() {
 		Calendar cl = Calendar.getInstance();
 		cl.set(Calendar.DAY_OF_MONTH, cl
 				.getActualMaximum(Calendar.DAY_OF_MONTH));
 		return getDateTimeEnd(cl.getTime());
 	}

 	/**
 	 * 得到指定日期的开始时间
 	 * @param date
 	 * @return
 	 */
 	@SuppressWarnings("static-access")
 	public static Date getDateTimeStart(Date date) {
 		Calendar cl = Calendar.getInstance();
 		cl.setTime(date);
 		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 0,
 				0, 0);
 		return cl.getTime();
 	}

 	/**
 	 * 得到指定下一个月的零点
 	 * @param date
 	 * @return
 	 */
 	@SuppressWarnings("static-access")
 	public static Date getNextMonth(Date date) {
 		Calendar cl = Calendar.getInstance();
 		cl.setTime(date);
 		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH) + 1 , cl.get(cl.DAY_OF_MONTH), 0,0, 0);
 		return cl.getTime();
 	}
 	
 	/**
 	 * 得到指定日期的结束时间
 	 * @param date
 	 * @return
 	 */
 	@SuppressWarnings("static-access")
 	public static Date getDateTimeEnd(Date date) {
 		Calendar cl = Calendar.getInstance();
 		cl.setTime(date);
 		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 23,
 				59, 59);
 		return cl.getTime();
 	}

 	/**
 	 * 得到两个日期的间隔月
 	 * @param s
 	 * @param e
 	 * @return
 	 */
 	public static int getBetweenMonth(Date s, Date e) {
 		if (s.after(e)) {
 			Date t = s;
 			s = e;
 			e = t;
 		}
 		Calendar start = Calendar.getInstance();
 		start.setTime(s);
 		Calendar end = Calendar.getInstance();
 		end.setTime(e);
 		Calendar temp = Calendar.getInstance();
 		temp.setTime(e);
 		temp.add(Calendar.DATE, 1);

 		int y = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
 		int m = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);

 		if ((start.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {// 前后都不破月
 			return y * 12 + m + 1;
 		} else if ((start.get(Calendar.DATE) != 1)
 				&& (temp.get(Calendar.DATE) == 1)) {// 前破月后不破月
 			return y * 12 + m;
 		} else if ((start.get(Calendar.DATE) == 1)
 				&& (temp.get(Calendar.DATE) != 1)) {// 前不破月后破月
 			return y * 12 + m;
 		} else {// 前破月后破月
 			return (y * 12 + m - 1) < 0 ? 0 : (y * 12 + m - 1);
 		}
 	}
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	/**
	 * 得到两个日期的间秒
	 * @param oldDate 原来时间
	 * @param nowDate 现在时间
	 * @return
	 * @throws Exception
	 */
	public static int getBetweenSecond(String oldDate,String nowDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date old = sdf.parse(oldDate);
		Date now = sdf.parse(nowDate);
		return (new Long((now.getTime() - old.getTime()) / 1000)).intValue();
	}
	
	/**
	 * 根据生日获取年龄
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public static String getAge(Date birthDay) throws Exception { 
        Calendar cal = Calendar.getInstance(); 

        if (cal.before(birthDay)) { 
            throw new IllegalArgumentException( 
                "The birthDay is before Now.It's unbelievable!"); 
        } 

        int yearNow = cal.get(Calendar.YEAR); 
        int monthNow = cal.get(Calendar.MONTH)+1; 
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); 
        
        cal.setTime(birthDay); 
        int yearBirth = cal.get(Calendar.YEAR); 
        int monthBirth = cal.get(Calendar.MONTH); 
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH); 

        int age = yearNow - yearBirth; 

        if (monthNow <= monthBirth) { 
            if (monthNow == monthBirth) { 
                //monthNow==monthBirth 
                if (dayOfMonthNow < dayOfMonthBirth) { 
                    age--; 
                } 
            } else { 
                //monthNow>monthBirth 
                age--; 
            } 
        } 
        return age +""; 
    }
	/**
	 * 得到指定时间后多少毫秒后的新时间
	 * @param time
	 * @param millisecond
	 * @return
	 */
	public static String getDateAfterWhatMS(Date time,long millisecond,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time.getTime()+millisecond);
	}
	/**
	 * 得到多少天数的新时间
	 * @param time
	 * @param day
	 * @return
	 */
	public static String getDateAfterWhatMS(Date time,int day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		return sdf.format(System.currentTimeMillis()+ day*24l*60l*60l*1000l);
	}
	public static Date getDateAfterWhatMSForDate(Date time,long day){
		long times =time.getTime()+ day*24*60*60*1000;
		return new Date(times);
	}
	/**
	 * 将日期转成scheduler识别的corn表达式
	 * @param String	date
	 * @return
	 * @throws ParseException 
	 */
	public static String parseDateAsCorns(String date) throws ParseException{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String parseDate = format.format(format.parse(date));
		StringBuilder sb = new StringBuilder();
		String yearMonthDay = parseDate.toString().split(" ")[0];
		String hoursMinSec = parseDate.toString().split(" ")[1];
		sb.append(hoursMinSec.split(":")[2].substring(0, 2)).append(" ");
		sb.append(hoursMinSec.split(":")[1]).append(" ");
		sb.append(hoursMinSec.split(":")[0]).append(" ");
		sb.append(yearMonthDay.split("-")[2]).append(" ");
		sb.append(yearMonthDay.split("-")[1]).append(" ");
		sb.append("?").append(" ");
		sb.append(yearMonthDay.split("-")[0]).append(" ");
		
		return sb.toString();
	}
	/**
	 * 将日期转成scheduler识别的corn表达式
	 * @param Date	date
	 * @return
	 * @throws ParseException 
	 */
	public static String parseDateAsCorns(Date date) throws ParseException{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String parseDate = format.format(date);
		StringBuilder sb = new StringBuilder();
		String yearMonthDay = parseDate.toString().split(" ")[0];
		String hoursMinSec = parseDate.toString().split(" ")[1];
		sb.append(hoursMinSec.split(":")[2].substring(0, 2)).append(" ");
		sb.append(hoursMinSec.split(":")[1]).append(" ");
		sb.append(hoursMinSec.split(":")[0]).append(" ");
		sb.append(yearMonthDay.split("-")[2]).append(" ");
		sb.append(yearMonthDay.split("-")[1]).append(" ");
		sb.append("?").append(" ");
		sb.append(yearMonthDay.split("-")[0]).append(" ");
		return sb.toString();
	}
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		System.out.println(getDateAfterWhatMSForDate(new Date(),365));
	}
}
