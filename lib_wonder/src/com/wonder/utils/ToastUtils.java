package com.wonder.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Object synObj = new Object();

	private static Toast toast;

	public static void showToast(final Context context, final String message) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
								// toast.cancel();
								toast.setText(message);  
								toast.setDuration(Toast.LENGTH_LONG);
							} else {
								toast = Toast.makeText(context, message,Toast.LENGTH_LONG);
							}
							toast.show();
						}
					}
				});
			}
		}).start();
	}
}
