package com.wonder.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wonder.customView.ToastViewUtil;
import com.wonder.mylib.R;

/**
 * 自动升级管理工具
 * @author wonder
 *
 */
public class UpgradeUtil {

	private UpgradeUtil(){}
	private static UpgradeUtil instance = new UpgradeUtil();
	public static UpgradeUtil getInstance()
	{
		return instance;
	}
	
	private static final String PATH_UPGRADE_APK = Environment
			.getExternalStorageDirectory() + "/duobei/upgrade/duobei.apk";
	private static final int ID_NOTIFI = 1234;
	private RemoteViews views;
	private NotificationManager notifyMgr;
	private Notification notifi;
	private int fileLen;
	private Context context;

	private int getVersionCode() {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * json格式：
	 * {
	 * 		"apkurl":"xxx",
	 *  	"fileLen":123123,
	 * 		"newversion":12,
	 * 		"featrue": "1. xxx \n2. xxx"}
	 * 
	 * @param context
	 * @param apkUrl
	 */
	public void autoUpgrade(Context context, final String apkUrl) {
		this.context = context;
		int currVer = getVersionCode();
		// 来自json解析
		int serverVer = 10;
//		final String apkUrl = "http://192.168.1.102:8080/test.apk";
		fileLen = 10113868;
		if (currVer < serverVer) {
			new AlertDialog.Builder(context).setTitle("升级")
					.setMessage("1. 新增了xxx特性 \n2. 修复xx bug")
					.setNegativeButton("取消", null)
					.setPositiveButton("升级", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new UpgradeTask().execute(apkUrl);
						}
					}).show();
		}
	}

	class UpgradeTask extends AsyncTask<String, Integer, Void> {

		private int loadedLen;

		@Override
		protected void onPreExecute() {
			showNotify();
		}

		@Override
		protected Void doInBackground(String... params) {
			// 下载apk
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				int responseCode = conn.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK) {
					// 提示
					return null;
				}
				is = conn.getInputStream();
				int len = 0;
				byte[] buffer = new byte[1024];
				File apkFile = new File(PATH_UPGRADE_APK);
				if (!apkFile.getParentFile().exists()) {
					apkFile.getParentFile().mkdirs();
				}

				loadedLen = 0;
				fos = new FileOutputStream(PATH_UPGRADE_APK);
				// 控制流量，每10%更新一次
				int num = 1;
				while (-1 != (len = is.read(buffer))) {
					fos.write(buffer, 0, len);
					loadedLen += len;
					if (loadedLen * 100 / fileLen >= 10 * num) {
						num++;
						publishProgress(loadedLen);
					}
				}
				fos.flush();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int loadedLen = values[0];

			views.setTextViewText(R.id.textView1, "已下载：" + loadedLen * 100
					/ fileLen + "%");
			views.setProgressBar(R.id.progressBar1, fileLen, loadedLen, false);
			notifyMgr.notify(ID_NOTIFI, notifi);
		}

		@Override
		protected void onPostExecute(Void result) {
			ToastViewUtil.showToast(context, "下载完成，点击通知更新",
					Toast.LENGTH_SHORT);
			finishNotify();
		}

	}

	public void finishNotify() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + PATH_UPGRADE_APK),
				"application/vnd.android.package-archive");
		PendingIntent ci = PendingIntent.getActivity(context, 0, intent, 0);
		notifi.contentIntent = ci;

		views.setTextViewText(R.id.textView1, "点击更新");
		views.setViewVisibility(R.id.progressBar1, View.GONE);
		notifyMgr.notify(ID_NOTIFI, notifi);
	}

	private void showNotify() {
		notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// startActivity(intent); //这种方法的 有没有别的途径 请指教

		views = new RemoteViews(context.getPackageName(), R.layout.custom_notifi);
		notifi = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher).setTicker("新消息")
				.setWhen(System.currentTimeMillis()).setContent(views).build();
		notifyMgr.notify(ID_NOTIFI, notifi);
	}

}
