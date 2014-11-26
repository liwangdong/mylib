package com.wonder.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.wonder.app.AppManager;
import com.wonder.mylib.R;

/**
 * 基本activity,方便管理
 * @author wonder
 * 
 */
public class BaseActivity extends Activity {

	private ProgressDialog mProgressDialog;
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加Activity到堆栈
		AppManager.getInstance().addActivity(this);
	} 
	/**
	 * 获取一个intent
	 * 
	 * @param className
	 * @return
	 */
	protected Intent getIntent(Class<?> className) {
		return new Intent(this, className);
	}

	/**
	 * 打开指定activity
	 * 
	 * @param className
	 */
	protected void openActivity(Class<?> className) {
		startActivity(getIntent(className));
	}

	public void openActivity(Intent intent) {
		startActivity(intent);
	}
	protected void finishAllActivity() {
		AppManager.getInstance().finishAllActivity();
	}
	
	protected void finishActivity() {
		AppManager.getInstance().finishActivity(this);
	}
	

	/**
	 * 点击返回键触发finish
	 */
	@Override
	public void onBackPressed() {
		AppManager.getInstance().finishActivity(this);
		super.onBackPressed();
	}

	@Override
	public void finish() { 
		this.hideProgressDialog();
		super.finish();
	}

	/**
	 * 显示加载进度条对话框
	 * @param title 对话框标题
	 * @param message 对话框信息
	 */
	public void showProgressDialog(String title, String message) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this, R.style.dialog);
			mProgressDialog.setTitle(title);
			mProgressDialog.setMessage(message);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		} else {
			mProgressDialog.setTitle(title);
			mProgressDialog.setMessage(message);
		}
		mProgressDialog.show();
	}
	
	/**
	 * 隐藏进度条对话框
	 */
	public void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * 显示自定义时长toast
	 * @param msg 消息
	 * @param duration 显示时长（毫秒）
	 */
	public void showToast(String msg, int duration) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(msg);
		else
			mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		mHandler.postDelayed(r, duration);

		mToast.show();
	}

	/**
	 * 显示默认时长toast
	 * @param msg
	 */
	public void showToast(String msg) {
		showToast(msg, 2000);
	}
}