package com.wonder.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
/**
 * 日期工具类
 * @author Iverson
 *
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_DATE_TIME = FORMAT_DATE + " "
			+ FORMAT_TIME;
	public static final String DEFAULT_DATE_TIME = "yyyyMMddHHmmss";
	public static final String DISPLAY_DATE_TIME = FORMAT_DATE+" HH:mm";

	public static String dateToString(Date date, String formatString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
		return dateFormat.format(date);
	}

	public static Date stringToDate(String dateString, String formatString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getCurDefaultDateTime() {
		Date date = new Date();
		return dateToString(date, DEFAULT_DATE_TIME);
	} 
	public static String getCurFormatDateTime() {
		Date date = new Date();
		return dateToString(date, FORMAT_DATE_TIME);
	} 
	public static String getCurFormatDate() {
		Date date = new Date();
		return dateToString(date, FORMAT_DATE);
	} 
	public static String percent(int x,int y){ 
		double baix=x*1.0;
		double baiy=y*1.0;
		double fen=baix/baiy;
		DecimalFormat df=new DecimalFormat("##%"); 
		return df.format(fen); 
	}
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getTime(timesamp);
			break;
		}
		return result;
	}
	
	/**
	 * 比较时间大小
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
           
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1在dt2前");
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return false;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }
	
	//判断时间date1是否在时间date2之前
	 //时间格式 2005-4-21 16:16:34
	 public static boolean isDateBefore(String date1,String date2){
	  try{
	   DateFormat df = DateFormat.getDateTimeInstance();
	   return df.parse(date1).before(df.parse(date2));
	  }catch(ParseException e){
	   System.out.print("[SYS] " + e.getMessage());
	   return false;
	  }
	 }
	//判断当前时间是否在时间date2之前
	 //时间格式 2005-4-21 16:16:34
	public static boolean isDateBefore(String date2) {
		try {
			Date date1 = new Date();
			System.out.println(date1);
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			System.out.print("[SYS] " + e.getMessage());
			return false;
		}
	}
	public static boolean checkTime(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME);
		Date d1 = new Date();
		Date d2;
		try {  
			if(StringUtils.isEmpty(time))
				return false; 
			d2 = dateFormat.parse(time);
			long timesamp =Math.abs(d1.getTime() - d2.getTime()) ; 
			long minutes  = timesamp / (1000 * 60);
			if(minutes<10){
				return true;
			}
		} catch (ParseException e) { 
			e.printStackTrace();
		} 
		return false;
	} 
	
	public static String defaultToFormat(String defaultDateTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME); 
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(FORMAT_DATE_TIME);
		Date d2;
		try {  
			if(StringUtils.isEmpty(defaultDateTime)){
				return ""; 
			}
			d2 = dateFormat.parse(defaultDateTime);
			return dateFormat2.format(d2); 
		} catch (ParseException e) { 
			e.printStackTrace();
		} 
		return "";
	}
	public static String formatToDefault(String formatDateTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME); 
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(FORMAT_DATE_TIME);
		Date d2;
		try {  
			if(StringUtils.isEmpty(formatDateTime)){
				return ""; 
			}
			d2 = dateFormat2.parse(formatDateTime);
			return dateFormat.format(d2); 
		} catch (ParseException e) { 
			e.printStackTrace();
		} 
		return "";
	}
}