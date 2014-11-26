package com.wonder.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wonder.android.volley.MyVolley;

public class MyApp extends Application implements DbUpgradeListener { 
	
	public static final String DBNAME="";
	public static final int DBVERSION=1;
	private static MyApp instance;   
	private static DbUtils db=null;
	
	@Override
	public void onCreate() { 
		super.onCreate();
		
		instance=this; 
		db=DbUtils.create(this,DBNAME,DBVERSION,this);
//		db.configDebug(true);//打开xutils的数据库工具调试模式，可查看SQL语句
		init();
	}
	
	public static MyApp getInstance() {
		return instance;
	}
	
	private void init() {
		initImageLoader(getApplicationContext());
		initVolley(getApplicationContext());
    }
	
	/**
	 * 初始化ImageLoader工具
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

	}
	
	/**
	 * 初始化volley工具
	 * @param context
	 */
	public static void initVolley(Context context){
		MyVolley.init(context);
	}
	
	
	
	public String getClientVersion() {
		PackageManager manager = instance.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(instance.getPackageName(), 0); 
			return info.versionName;
		} catch (Exception e) {
			return "1.0";
		}
	} 
	/**
	 * 获取 DButils
	 * @return DbUtils
	 */
	public DbUtils getDbUtils(){
		return db;
	}
	
	/**
	 * 本地数据库版本升级回调
	 */
	@Override
	public void onUpgrade(DbUtils arg0,  int oldVersion, int newVersion) { 
		
	} 
	
}
