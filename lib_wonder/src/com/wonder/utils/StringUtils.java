package com.wonder.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author asu
 * 
 */
public class StringUtils {
	// 判断字符串为空
	public static boolean isEmptyOrNull(String str) {
		return str == null || str.trim().length() <= 0
				|| str.trim().equals("null") || str.trim().equals("NULL")
				|| str.trim().equals("");
	}

	public static boolean isEmptyOrZero(String str) {
		return isEmptyOrNull(str) || "0".equals(str);
	}

	// 判断字符是否不为空
	public static boolean isNotEmpty(String str) {
		return !isEmptyOrNull(str);
	}

	// 判断是否纯数字
	public static boolean isIntNumber(String str) {
		if (isEmptyOrNull(str)) {
			return false;
		}
		return str.matches("[0-9]+");
	}

	// 判断字符串str是不是纯数字的字符串
	public static boolean isFloatNum(String str) {
		if (isEmptyOrNull(str)) {
			return false;
		}
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	// 判断字符串str是否符合pattern类型的正则表达式
	public static boolean isPattern(String str, String pattern) {
		// /^[-|+]?\\d*([.]\\d{0,2})?$
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.find();
	}

	// 获取字符串的子串
	public static String getSubString(String str) {
		int nIndex = str.indexOf(0);
		if (nIndex != -1) {
			return str.substring(0, nIndex);
		}
		return str;
	}

	// 格式化整型
	// num 整型
	// pattern 模式如：000 00.00
	public static String fomatInteger(int number, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(number);
	}

	// 采用GBK对字符进行编码
	public static String encodeWithGBK(byte[] buf) {
		return encode(buf, 0, buf.length, "GBK");
	}

	// 采用GBK对字符进行编码
	public static String encodeWithGBK(byte[] buf, int from, int len) {
		return encode(buf, from, len, "GBK");
	}

	// 对字符编码
	public static String encode(byte[] buf, int offset, int len, String charset) {
		try {
			return new String(buf, offset, len, charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 调整字符串长度
	public static String adjustStringLength(String str, int length) {
		if (str == null) {
			return null;
		}
		if (str.length() > length) {
			String result = str.substring(0, length) + "...";
			return result;
		} else {
			return str;
		}
	}

	/**
	 * 将格式为20120909121212转换为2012-09-09 12:12:12
	 * 
	 * @param str
	 * @return
	 */
	public static String converDateToString(String str) {
		if (isEmptyOrNull(str)) {
			return "时间格式错误";
		}

		StringBuffer sb = new StringBuffer(str.trim());
		if (str.length() > 3)
			sb.insert(4, '-');

		if (str.length() > 6)
			sb.insert(7, '-');

		if (str.length() > 8)
			sb.insert(10, ' ');

		if (str.length() > 10)
			sb.insert(13, ':');

		if (str.length() > 12) {
			sb.insert(16, ':');
		}

		return sb.toString();
	}
	
	public static String convertStreamToString(InputStream is) {
		if (is == null) {
			return null;
		} 
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static String[] splitStringToArray(String content, String split) {
		String[] result = null;
		if (content != null && !isEmpty(content)) {
			result = content.split(split);
		}
		return result;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s){
		boolean flag=false;
		if(s==null||s.length()==0){
			flag=true;
		} 
		return flag;
	}
	/**
	 * 从身份证得出年龄
	 * @param s：身份证号
	 * @return
	 */
	public static String getAge(String s){ 
		String currtime=TimeUtil.getCurDefaultDateTime().substring(0,4);
        String rStr=null;
        int leh = s.length(); 
        if (leh != 18 && leh != 15) {
            System.out.println("身份证为空");
        }else {
            if (leh == 18) {  
                rStr= (Integer.valueOf(currtime)-Integer.valueOf(s.substring(6, 10)))+"";
            }else { 
                rStr=(Integer.valueOf(currtime)- Integer.valueOf("19" + s.substring(6, 8)))+"";
            }
        }
        return rStr;
    }
	


}