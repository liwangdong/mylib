package com.wonder.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

/**
 * 工具类
 * @author wonder
 *
 */
public class Utils {

	static final String LOG_TAG = "PullToRefresh";

	public static void warnDeprecation(String depreacted, String replacement) {
		Log.w(LOG_TAG, "You're using the deprecated " + depreacted
				+ " attr, please switch over to " + replacement);
	}

	/**
	 * 跳转拨号
	 * @param activity
	 * @param tel
	 */
	public static void playTel(Activity activity, String tel) {
		/*
		 * Intent intent = new Intent(); //系统默认的action，用来打开默认的电话界面
		 * intent.setAction(Intent.ACTION_CALL);
		 * 
		 * //需要拨打的号码 intent.setData(Uri.parse("tel:"+tel));
		 * activity.startActivity(intent); 直接拨出电话
		 */

		// 跳转到拨打电话页面
		Uri uri = Uri.parse("tel:" + tel);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		activity.startActivity(it);
	}

	/**
	 * 一个自定义日期与当前系统日期相减
	 * 
	 * @param date
	 *        自定义日期
	 * @return
	 */
	public static String subSystemTime(String startTime, String endTime) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = df.parse(startTime);
			endDate = df.parse(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		long year, month, day, hour, minute, second;

		long milliSecondSub = endDate.getTime() - startDate.getTime();
		// long milliSecondSub = startDate.getTime()-System.currentTimeMillis();

		if (milliSecondSub < 0) {
			milliSecondSub = -milliSecondSub;
			sb.append("-");
		}

		second = milliSecondSub / 1000 % 60;
		minute = milliSecondSub / 1000 / 60 % 60;
		hour = milliSecondSub / 1000 / 60 / 60 % 24;
		day = milliSecondSub / 1000 / 60 / 60 / 24;

		// sb.append(day).append("天").append(hour).append("小时").append(minute).append("分钟").append(second).append("秒");
		sb.append(day);
		return sb.toString();
	}

	public static String getCurrDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = sdf.format(new java.util.Date());
		return date1;
	}

	public static String getNextDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		String date2 = sdf.format(calendar.getTime());
		return date2;
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 * 设置的需要判断的时间 //格式如2012-09-08
	 * 
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */

	// String pTime = "2012-03-12";
	public static String getWeek(String pTime) {

		String Week = "周";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}

		return Week;
	}

	/**
	 * @param 将图片内容解析成字节数组
	 * @param instream
	 * @return byte[]
	 * @throws exception
	 */
	public static byte[] readstream(InputStream instream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		while ((len = instream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		byte[] data = outstream.toByteArray();
		outstream.close();
		instream.close();
		return data;

	}

	/**
	 * @param 将字节数组转换为imageview可调用的bitmap对象
	 * @param bytes
	 * @param opts
	 * @return bitmap
	 */
	public static Bitmap getpicfrombytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

}
