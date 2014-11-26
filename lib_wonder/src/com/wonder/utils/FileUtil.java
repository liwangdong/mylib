package com.wonder.utils; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;

import com.wonder.app.MyApp;


/**
 * 文件操作相关  
 */ 
public class FileUtil {
	
	private static final String TAG=FileUtil.class.getSimpleName(); 
	
	/***
	 * 获取项目文件夹
	 * @return
	 */
	public static File getDir(){
		Context context=MyApp.getInstance();
		String packname=context.getPackageName();
		String name=packname.substring(packname.lastIndexOf(".")+1, packname.length());
		File dir=null; 
		if(hasSDCard()){
			dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()  + "/snxun/"+name); 
		}else{
			dir=context.getCacheDir();
		}
		dir.mkdirs();
		return dir;
	}
	
	/**
	 * 获取项目缓存文件
	 * @return
	 */
	public static File getCacheDir(){
		File file=new File(getDir().getAbsolutePath()+File.separator+"cache");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	} 
	
	/**
	 * 获取项目使用过程中产生的图片文件夹
	 * @return
	 */
	public static File getImageDir(){
		File file=new File(getDir().getAbsolutePath()+File.separator+"image");
		file.mkdirs();
		return file;
	}
	/**
	 * 获取系统异常日志路径
	 * @return
	 */
	public static File getCaughtLogDir(){
		File file=new File(getDir().getAbsolutePath()+File.separator+"log");
		file.mkdirs();
		return file;
	}
	
	/**
	 * 删除文件夹
	 * @param dirf
	 */
	public static void deleteDir(File dirf){
		if(dirf.isDirectory()){
			File[] childs=dirf.listFiles();
			for (int i = 0; i < childs.length; i++) {
				deleteDir(childs[i]);
			}
		}
		dirf.delete();
	}
	
	/**
	 * uri装换文件
	 * @param context
	 * @param uri
	 * @return
	 */
	public static File uriToFile(Activity context,Uri uri){
	    String[] proj = { MediaStore.Images.Media.DATA };  
	    Cursor actualimagecursor =context.managedQuery(uri,proj,null,null,null);  
	    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	    actualimagecursor.moveToFirst();  
	    String img_path = actualimagecursor.getString(actual_image_column_index);  
	    File file = new File(img_path);  
	    return file;
	}
	/**
	 * 写入文件
	 * @param in
	 * @param file
	 */
	public static void write(InputStream in,File file){
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileOutputStream out=new FileOutputStream(file);
			byte[] buffer=new byte[1024];
			while (in.read(buffer)>-1) {
				out.write(buffer);
			}
			out.flush();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 写入文件
	 * @param in
	 * @param file
	 */
	public static void write(String in,File file,boolean append){
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file, append);
			fw.write(in);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读文件
	 * @param file
	 * @return
	 */
	public static String read(File file){
		if(!file.exists()){
			return "";
		}
		try {
			FileReader reader=new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			StringBuffer buffer=new StringBuffer();
			String s;
			while ((s = br.readLine()) != null) {
				buffer.append(s);
			}
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 判断存储卡是否存在
	 */
	public static Boolean hasSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
//	/**
//	 * 获取存储卡路径，路径带/
//	 * 如果没有存储卡，返回空字符串
//	 */
//	public static String getSDCardPath()
//	{
//		if(hasSDCard())
//			return Environment.getExternalStorageDirectory()+File.separator;
//		else
//			return "";
//	} 
 
	/** 
	 * 打开文件
	 *  
	 **/
	public static void OpenFile(File f, Context c) {
		if (!f.exists() || c == null)
			return;

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		c.startActivity(intent);
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * @param file
	 */
	public static String getMIMEType(File file)
	{
		String type="*/*";
		String fName=file.getName();
		//获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
		/* 获取文件的后缀名 */
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end=="")return type;
		//在MIME和文件类型的匹配表中找到对应的MIME类型。
		for(int i=0;i<MIME_MapTable.length;i++){
			if(end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	
	/**
	 * 获取文件后缀名
	 * @param filename 文件名称
	 * @return
	 */
	public static String getFileExtension(String filename)
	{
		if(StringUtils.isEmpty(filename))
			return "";
		int dotIndex = filename.lastIndexOf(".");
		if(dotIndex < 0) return "";
		/* 获取文件的后缀名 */
		return filename.substring(dotIndex+1,filename.length()).toLowerCase();
	}

	/**
	 * 建立一个MIME类型与文件后缀名的匹配表
	 */
	public static final String[][] MIME_MapTable={
		//{后缀名，	MIME类型}
		{".3gp",	"video/3gpp"},
		{".apk",	"application/vnd.android.package-archive"},
		{".asf",	"video/x-ms-asf"},
		{".avi",	"video/x-msvideo"},
		{".bin",	"application/octet-stream"},
		{".bmp",  	"image/bmp"},
		{".c",		"text/plain"},
		{".class",	"application/octet-stream"},
		{".conf",	"text/plain"},
		{".cpp",	"text/plain"},
		{".doc",	"application/msword"},
		{".exe",	"application/octet-stream"},
		{".gif",	"image/gif"},
		{".gtar",	"application/x-gtar"},
		{".gz",		"application/x-gzip"},
		{".h",		"text/plain"},
		{".htm",	"text/html"},
		{".html",	"text/html"},
		{".jar",	"application/java-archive"},
		{".java",	"text/plain"},
		{".jpeg",	"image/jpeg"},
		{".jpg",	"image/jpeg"},
		{".js",		"application/x-javascript"},
		{".log",	"text/plain"},
		{".m3u",	"audio/x-mpegurl"},
		{".m4a",	"audio/mp4a-latm"},
		{".m4b",	"audio/mp4a-latm"},
		{".m4p",	"audio/mp4a-latm"},
		{".m4u",	"video/vnd.mpegurl"},
		{".m4v",	"video/x-m4v"},	
		{".mov",	"video/quicktime"},
		{".mp2",	"audio/x-mpeg"},
		{".mp3",	"audio/x-mpeg"},
		{".mp4",	"video/mp4"},
		{".mpc",	"application/vnd.mpohun.certificate"},		
		{".mpe",	"video/mpeg"},	
		{".mpeg",	"video/mpeg"},	
		{".mpg",	"video/mpeg"},	
		{".mpg4",	"video/mp4"},	
		{".mpga",	"audio/mpeg"},
		{".msg",	"application/vnd.ms-outlook"},
		{".ogg",	"audio/ogg"},
		{".pdf",	"application/pdf"},
		{".png",	"image/png"},
		{".pps",	"application/vnd.ms-powerpoint"},
		{".ppt",	"application/vnd.ms-powerpoint"},
		{".prop",	"text/plain"},
		{".rar",	"application/x-rar-compressed"},
		{".rc",		"text/plain"},
		{".rmvb",	"audio/x-pn-realaudio"},
		{".rtf",	"application/rtf"},
		{".sh",		"text/plain"},
		{".tar",	"application/x-tar"},	
		{".tgz",	"application/x-compressed"}, 
		{".txt",	"text/plain"},
		{".wav",	"audio/x-wav"},
		{".wma",	"audio/x-ms-wma"},
		{".wmv",	"audio/x-ms-wmv"},
		{".wps",	"application/vnd.ms-works"},
		//{".xml",	"text/xml"},
		{".xml",	"text/plain"},
		{".z",		"application/x-compress"},
		{".zip",	"application/zip"},
		{"",		"*/*"}	
	};
	
	/**
	 * 读取文件
	 * @param file 完成文件路径
	 * @return
	 */
	public static byte[] ReadFileToBytes(String file) 
	{
		FileInputStream fIn = null;
		
		byte[] buffer=null;
		try {
			File f=new File(file);
			fIn = new FileInputStream(f);
			buffer = new byte[fIn.available()];
			fIn.read(buffer);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if(fIn!=null)
					fIn.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return buffer;
	}
	
	/**
	 * 读取文件
	 * @param file 完成文件路径
	 * @return
	 */
	public static String ReadFile(String file)
	{
		return ReadFile(file,"UTF-8");
	}
	
	/**
	 * 读取文件
	 * @param file 完成文件路径
	 * @param charset 字符集,默认UTF-8
	 * @return
	 */
	public static String ReadFile(String file,String charset) 
	{
		File f=new File(file);
		if(!f.exists())
			return null;
		Charset cs;
		try
		{
			cs=Charset.forName(charset);
		}
		catch (Exception e) {
			cs=Charset.forName("UTF-8");
		}
		FileInputStream fIn = null;
		InputStreamReader isr=null;
		StringBuilder result=new StringBuilder();
		BufferedReader br = null;
		try {
			fIn = new FileInputStream(f);
			isr=new InputStreamReader(fIn, cs);
			br=new BufferedReader(isr);
			String temp;
			while((temp=br.readLine())!=null)
			{
				result.append(temp);
				result.append("\r\n");
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if(br!=null)
					br.close();
				if(isr!=null)
					isr.close();
				if(fIn!=null)
					fIn.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return result.toString();
	}
	
	/**
	 * 保存文件内容
	 */
	public static void WriteFile(String file, byte[] data) {
		FileOutputStream fOut = null;
		try {
			File f=new File(file);
			fOut = new FileOutputStream(f,true);
			fOut.write(data);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if(fOut!=null)
					fOut.close();
			} catch (IOException e) {

				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	/**
	 * 保存文件内容
	 */
	public static void WriteFile(String file, String data) {
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;
		try {
			isExistFolderFromFile(file);	// 文件夹存在与否检测，不存在则创建
			
			File f=new File(file);
			fOut = new FileOutputStream(f,true);
			osw = new OutputStreamWriter(fOut);
			osw.write(data);
			osw.flush();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			try {
				if(osw!=null)
				 osw.close();
				if(fOut!=null)
					fOut.close();
			} catch (IOException e) {

				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	/**
	 * 删除文件
	 * @param filename 文件名
	 * @return
	 */
	public static boolean deleteFile(String filename) {
		File f = new File(filename);
		
		if (!f.exists()) {
			return true;
		}
		
		boolean flag = f.delete();
		
		return flag;
	}
	
	/**
	 * inputstream 存为文件，旧文件存在的话会被删除
	 * @param inputStream
	 * @param destFile
	 * @return
	 */
	public static boolean copyToFile(InputStream inputStream, File destFile)
	{
		try
		{
			if (destFile.exists())
			{
				destFile.delete();
			}
			else
			{
				if(!isExistFolderFromFile(destFile.getPath()))
					return false;
			}
			OutputStream out = new FileOutputStream(destFile);
			try
			{
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0)
				{
					out.write(buffer, 0, bytesRead);
				}
			} finally
			{
				out.close();
			}
			return true;
		} catch (IOException e)
		{
			return false;
		}
	}

	/**
	 * 判断文件的文件夹是否存在
	 * @param strfilename 文件的完整文件名
	 * @return
	 */
	public static boolean isExistFolderFromFile(String strfilename)
	{
		if(StringUtils.isEmpty(strfilename))
			return false;
		int index=strfilename.lastIndexOf(File.separator);
		if(index<=0)
			return false;
		String fdir=strfilename.substring(0,index);
		return isExistFolder(fdir);
	}

	/**
	 * 判断是否存在文件夹,不存在则会偿试进行创建
	 */
	public static boolean isExistFolder(String strFolder) {
		if (strFolder == null)
			return true;
		boolean bReturn = false;

		File f = new File(strFolder);
		if (!f.exists()) {
			/* 创建文件夹 */
			if (f.mkdirs()) {
				bReturn = true;
			} else {
				bReturn = false;
			}
		} else {
			bReturn = true;
		}
		return bReturn;
	}
	
	/**
	 * 获取文件名
	 * @param fullfilename　文件的完成路径名或URL地址
	 * @return 文件名,如果完整路径里不包括/或\则直接返回fullfilename
	 */
	public static String getFileName(String fullfilename)
	{
		if(StringUtils.isEmpty(fullfilename))
			return "";
		int index=-1;
		if(fullfilename.contains(File.separator))
		{
			index=fullfilename.lastIndexOf(File.separator);
		}
		else
			index=fullfilename.lastIndexOf("\\");
		if(index==-1)
			return fullfilename;
		else
		if(index>=fullfilename.length()-1)
			return "";			
		return fullfilename.substring(index+1);
	}
	
	/**
	 * 存储内容
	 * @param filename 文件名，如果不包括/路径，则存储到存储卡目录下
	 * @param filecontent 要存储的内容
	 */
	public static void saveContent(String filename,String filecontent)
	{
		if(StringUtils.isEmpty(filename)||StringUtils.isEmpty(filecontent))
			return;
		String filefullpath;
		if(filename.contains(File.separator))
			filefullpath=filename;
		else
			filefullpath=getDir().getAbsolutePath()+File.separator+filename;
		WriteFile(filefullpath, filecontent);
	}
	
	/**
	 * 格式化大小
	 */
	public static String FormatFileSize(Context c,long size)
	{
		return Formatter.formatFileSize(c, size); 
	}
	
 
	/**
	 * 传入原图名称，，获得一个以时间格式的新名称
	 * 
	 * @param fileName 原图名称 
	 */
	public static String generateFileName(String fileName) { 
		return generateFileName(fileName,null);
	}
	/**
	 * 传入原图名称，，获得一个以时间格式的新名称
	 * 
	 * @param fileName 原图名称 
	 */
	public static String generateFileName(String fileName,String policenum) {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String formatDate = format.format(new Date());
		int random = new Random().nextInt(10000);
		int position = fileName.lastIndexOf(".");
		String extension = fileName.substring(position);
		if(policenum!=null)
			formatDate= policenum+"@"+formatDate;
		return formatDate + random + extension;
	}
}
