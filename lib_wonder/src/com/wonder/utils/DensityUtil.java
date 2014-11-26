package com.wonder.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class DensityUtil {

    private static final String TAG = "DensityUtil";
	/**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    public static Display getDisplay(Context context){
    	WindowManager windowMgr = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowMgr.getDefaultDisplay();
    }
    /**
	 * 获取屏幕宽高（方法1）
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		return getDisplay(context).getHeight(); // 屏幕高（像素，如：800px）  
	}
	public static int getScreenWidth(Context context) {
		return getDisplay(context).getWidth(); // 屏幕宽（像素，如：480px）  
	}
	
//    /**
//     * 获取屏幕密度的其他方法
//     * @param context
//     */
//    public static void getScreenDensity(Context context){
//    	
//    	// 获取屏幕密度（方法2）
//    	DisplayMetrics dm = new DisplayMetrics();
//    	dm = context.getResources().getDisplayMetrics();
//    	float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//    	int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
//    	float xdpi = dm.xdpi;
//    	float ydpi = dm.ydpi;
//    	Log.e(TAG + " DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
//    	Log.e(TAG + " DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
//    	int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
//    	int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
//    	Log.e(TAG + " DisplayMetrics(111)", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);
//    	// 获取屏幕密度（方法3）
//    	dm = new DisplayMetrics();
//    	getDisplay(context).getMetrics(dm);
//    	density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//    	densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
//    	xdpi = dm.xdpi;
//    	ydpi = dm.ydpi;
//    	Log.e(TAG + " DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
//    	Log.e(TAG + " DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
//    	int screenWidthDip = dm.widthPixels; // 屏幕宽（dip，如：320dip）
//    	int screenHeightDip = dm.heightPixels; // 屏幕宽（dip，如：533dip）
//    	Log.e(TAG + " DisplayMetrics(222)", "screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);
//    	screenWidth = (int)(dm.widthPixels * density + 0.5f); // 屏幕宽（px，如：480px）
//    	screenHeight = (int)(dm.heightPixels * density + 0.5f); // 屏幕高（px，如：800px）
//    	Log.e(TAG + " DisplayMetrics(222)", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight); 
//    }
}