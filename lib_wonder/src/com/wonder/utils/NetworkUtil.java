package com.wonder.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtil {
	
	public static boolean isMobile(Context context) {
		if (!isConnected(context)) {
			return false;
		}
		
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info != null) {
			return info.getType() == 
					android.net.ConnectivityManager.TYPE_MOBILE;
		}
		return false;
		
	}

	public static boolean isWIFI(Context context) {
		if (!isConnected(context)) {
			return false;
		}

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info != null) {
			return info.getType() == 
				android.net.ConnectivityManager.TYPE_WIFI;
		}
		return false;

	}

	/**
	 * 判断当前设备是否联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info != null) {
			boolean available = info.isAvailable();
			return available;
		}
		return false;
	}

	private NetworkUtil() {
	}
}
