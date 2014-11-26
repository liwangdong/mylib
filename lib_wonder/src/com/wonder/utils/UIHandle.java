package com.wonder.utils;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wonder.mylib.R;

/**
 * 
 * @author lmr
 * 
 */
public class UIHandle {
	private static MyProgressDialog progressDialog = null;
	private static Toast toast = null;
	public static int Toast_LENGTH_LONG = Toast.LENGTH_LONG;
	public static int Toast_LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static boolean isDebug = false;

	/********************************** 自定义消息框 **************************************/

	/**
	 * 含有确定按钮
	 */
	public static void showMessageDialog(final Activity activity, String msg) {

		showMessageDialog(activity, null, msg, null, null, null, null, null,
				null);
	}

	/**
	 * 含有确定按钮和标题
	 */
	public static void showMessageDialog(final Activity activity, String title,
			String msg) {

		showMessageDialog(activity, title, msg, null, null, null, null, null,
				null);
	}

	/**
	 * 含有确定按钮和确定监听
	 */
	public static void showMessageDialog(final Activity activity, String msg,
			OnClickListener listener) {
		showMessageDialog(activity, null, msg, null, null, "确定", listener,
				null, null);
	}

	/**
	 * 含有确定取消按钮及各自监听
	 */
	public static void showMessageDialog(final Activity activity, String msg,
			OnClickListener confirmListener, OnClickListener cancelListener) {
		showMessageDialog(activity, null, msg, null, null, "确定",
				confirmListener, "取消", cancelListener);
	}

	/**
	 * 含有确定取消按钮及各自监听及自定义内容布局
	 */
	public static void showMessageDialog(final Activity activity, String title,
			View view, OnClickListener confirmListener,
			OnClickListener cancelListener) {
		showMessageDialog(activity, title, null, view, null, "确定",
				confirmListener, "取消", cancelListener);
	}

	/**
	 * 含有确定取消按钮及各自监听及自定义LISTVIEW内容布局
	 */
	public static void showMessageDialog(final Activity activity, String title,
			View listview, OnClickListener confirmListener) {
		showMessageDialog(activity, title, null, null, listview, "确定",
				confirmListener, "取消", null);
	}

	public static void showMessageDialog(final Activity activity,
			final String title, final String msg, final View view,
			final View listView, final String confirmStr,
			final OnClickListener confirmListener, final String cancelStr,
			final OnClickListener cancelListener) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CommonDialog dialog = new CommonDialog(activity);
				if (title != null) {
					dialog.setTitle(title, null);
				}
				if (msg != null) {
					dialog.setMessage(msg);
				}

				if (view != null) {
					dialog.setMessage(view);
				}

				if (listView != null) {
					dialog.setListViw(listView);
				}

				if (confirmStr != null) {
					dialog.setPositiveListener(confirmStr, confirmListener);
				}
				if (cancelStr != null) {
					dialog.setNegetiveListener(cancelStr, cancelListener);
				}
				dialog.show();
			}
		});
	}

	public static class CommonDialog extends Dialog implements
			android.view.View.OnClickListener {
		Context m_context = null;
		Button dlg_bt_titlemap;
		Button dlg_bt_close;
		Button dlg_btcance;
		Button dlg_btrepet;
		Button dlg_btok;
		TextView dlg_tv_title;
		TextView dlg_tv_content;
		LinearLayout dlg_lay_content;
		LinearLayout dlg_laylist_content;
		ScrollView scview;
		LinearLayout llview;
		private View.OnClickListener positiveListener = null;// 确定按钮监听
		private View.OnClickListener neutralListener = null;// neutral按钮监听
		private View.OnClickListener negetiveListener = null;// 取消按钮监听
		public boolean isMiss = true;

		private CommonDialog(Context m_context) {
			super(m_context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dlg_common);

			WindowManager m = getWindow().getWindowManager();
			Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
			WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值//
			p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的0.7
			p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.95
			getWindow().setAttributes(p);
			getWindow().setWindowAnimations(R.style.dialogWindowAnim);
			init();
		}

		public void init() {
			dlg_bt_titlemap = (Button) findViewById(R.id.dlg_bt_titlemap);
			dlg_bt_titlemap.setOnClickListener(this);
			dlg_bt_close = (Button) findViewById(R.id.dlg_bt_close);
			dlg_bt_close.setOnClickListener(this);
			dlg_btcance = (Button) findViewById(R.id.dlg_btcance);
			dlg_btcance.setOnClickListener(this);
			dlg_btrepet = (Button) findViewById(R.id.dlg_btrepet);
			dlg_btrepet.setOnClickListener(this);
			dlg_btok = (Button) findViewById(R.id.dlg_btok);
			dlg_btok.setOnClickListener(this);
			dlg_tv_title = (TextView) findViewById(R.id.dlg_tv_title);
			dlg_tv_content = (TextView) findViewById(R.id.dlg_tv_content);
			dlg_lay_content = (LinearLayout) findViewById(R.id.dlg_lay_content);
			dlg_laylist_content = (LinearLayout) findViewById(R.id.dlg_laylist_content);
			scview = (ScrollView) findViewById(R.id.scview);
			llview = (LinearLayout) findViewById(R.id.llview);

		}

		public void setTitle(CharSequence title, Bitmap bitmap) {

			dlg_tv_title.setText(title);
			if (bitmap != null) {
				BitmapDrawable bd = new BitmapDrawable(
						m_context.getResources(), bitmap);
				dlg_bt_titlemap.setBackgroundDrawable(bd);
			}
		}

		public void setMessage(CharSequence content) {
			dlg_tv_content.setVisibility(View.VISIBLE);
			dlg_tv_content.setText(content);

		}

		public void setMessage(View content) {
			scview.setVisibility(View.VISIBLE);
			dlg_lay_content.setVisibility(View.VISIBLE);
			dlg_lay_content.addView(content);

		}

		public void setListViw(View content) {
			llview.setVisibility(View.VISIBLE);
			dlg_laylist_content.setVisibility(View.VISIBLE);
			dlg_laylist_content.addView(content);

		}

		public void setPositiveListener(String str,
				View.OnClickListener positiveListener) {

			dlg_btok.setText(str);
			dlg_btok.setVisibility(View.VISIBLE);
			this.positiveListener = positiveListener;

		}

		public void setNegetiveListener(String str,
				View.OnClickListener negetiveListener) {

			dlg_btcance.setText(str);
			dlg_btcance.setVisibility(View.VISIBLE);
			this.negetiveListener = negetiveListener;

		}

		public void setNeutralListener(String str,
				View.OnClickListener neutralListener) {

			dlg_btrepet.setText(str);
			dlg_btrepet.setVisibility(View.VISIBLE);

			this.neutralListener = neutralListener;

		}

		@Override
		public void onClick(View v) {
			if (isMiss == true) {
				this.dismiss();
			}
			if (v == dlg_btok && positiveListener != null) {
				positiveListener.onClick(v);
			} else if (v == dlg_btcance && negetiveListener != null) {
				negetiveListener.onClick(v);
			} else if (v == dlg_btrepet && neutralListener != null) {
				neutralListener.onClick(v);
			} else if (v == dlg_bt_close) {
				this.dismiss();
			}
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {

				return false;
			}
			return false;
		}

	}

	/********************************** 自定义消息框 **************************************/

	/********************************** 系统消息框 **************************************/
	/**
	 * 消息框
	 * 
	 * @param msg
	 *            内容
	 */
	public static final void msgBoxOne(final Activity activity, final String msg) {
		msgBoxTwo(activity, "温馨提示", msg, "确定", null, null, null, true);
	}

	/**
	 * 消息框
	 * 
	 * @param msg
	 *            内容
	 * @param okButtonText
	 *            按钮文字
	 */
	public static final void msgBoxOne(final Activity activity,
			final String msg, String okButtonText) {
		msgBoxTwo(activity, "温馨提示", msg, okButtonText, null, null, null, true);
	}

	/**
	 * 消息框
	 * 
	 * @param msg
	 *            内容
	 * @param okButtonText
	 *            按钮文字
	 * @param onClickListener
	 *            按钮监听
	 */
	public static final void msgBoxOne(final Activity activity,
			final String msg, String okButtonText,
			final DialogInterface.OnClickListener onClickListener) {
		msgBoxTwo(activity, "温馨提示", msg, okButtonText, onClickListener, null,
				null, true);
	}

	/**
	 * 消息框
	 * 
	 * @param msg
	 *            内容
	 * @param okButtonText
	 *            按钮文字
	 * @param onClickListener
	 *            按钮监听
	 */
	public static final void msgBoxOne(final Activity activity,
			final String msg, String okButtonText,
			final DialogInterface.OnClickListener onClickListener,
			final boolean cancelOnTouchOutSide) {
		msgBoxTwo(activity, "温馨提示", msg, okButtonText, onClickListener, null,
				null, cancelOnTouchOutSide);
	}

	/**
	 * 消息框
	 * 
	 * @param msg
	 *            内容
	 * @param okButtonText
	 *            按钮文字
	 * @param onClickListener
	 *            按钮监听
	 */
	public static final void msgBoxOne(final Activity activity,
			final String title, final String msg, String okButtonText,
			final DialogInterface.OnClickListener onClickListener) {
		msgBoxTwo(activity, title, msg, okButtonText, onClickListener, null,
				null, true);
	}

	/**
	 * 消息框
	 * 
	 * @param msg
	 *            内容
	 * @param okButtonText
	 *            按钮文字
	 * @param onClickListener
	 *            按钮监听
	 * @param cancelOnTouchOutSide
	 *            点击空白区域对话框是否消失
	 */
	public static final void msgBoxOne(final Activity activity,
			final String title, final String msg, String okButtonText,
			final DialogInterface.OnClickListener onClickListener,
			final boolean cancelOnTouchOutSide) {
		msgBoxTwo(activity, title, msg, okButtonText, onClickListener, null,
				null, cancelOnTouchOutSide);
	}

	/**
	 * 两个按钮的弹出框
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @param leftButtonText
	 *            左边按钮文字
	 * @param leftButtonClickListener
	 *            左边按钮监听
	 * @param rightButtonText
	 *            右边按钮文字
	 * @param rightClickListener
	 *            右边按钮监听
	 */
	public static final void msgBoxTwo(final Activity activity,
			final String title, final String msg, final String leftButtonText,
			final DialogInterface.OnClickListener leftButtonClickListener,
			final String rightButtonText,
			final DialogInterface.OnClickListener rightClickListener) {
		msgBoxThree(activity, title, msg, leftButtonText,
				leftButtonClickListener, null, null, rightButtonText,
				rightClickListener, false);
	}

	/**
	 * 两个按钮的弹出框
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @param leftButtonText
	 *            左边按钮文字
	 * @param leftButtonClickListener
	 *            左边按钮监听
	 * @param rightButtonText
	 *            右边按钮文字
	 * @param rightClickListener
	 *            右边按钮监听
	 * @param cancelOnTouchOutSide
	 *            点击空白区域对话框是否消失
	 */
	public static final void msgBoxTwo(final Activity activity,
			final String title, final String msg, final String leftButtonText,
			final DialogInterface.OnClickListener leftButtonClickListener,
			final String rightButtonText,
			final DialogInterface.OnClickListener rightClickListener,
			final boolean cancelOnTouchOutSide) {
		msgBoxThree(activity, title, msg, leftButtonText,
				leftButtonClickListener, null, null, rightButtonText,
				rightClickListener, cancelOnTouchOutSide);
	}

	/**
	 * 
	 * 消息框
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @param leftStr
	 *            左边按钮文字
	 * @param leftClickListener
	 *            左边按钮监听
	 * @param middleStr
	 *            中间按钮文字
	 * @param middleClickListener
	 *            中间按钮监听
	 * @param rightStr
	 *            右边按钮文字
	 * @param rightClickListener
	 *            右边按钮监听
	 */
	public static final void msgBoxThree(final Activity activity,
			final String title, final String msg, final String leftStr,
			final DialogInterface.OnClickListener leftClickListener,
			final String middleStr,
			final DialogInterface.OnClickListener middleClickListener,
			final String rightStr,
			final DialogInterface.OnClickListener rightClickListener) {
		msgBoxThree(activity, title, msg, leftStr, leftClickListener,
				middleStr, middleClickListener, rightStr, rightClickListener,
				false);
	}

	/**
	 * 消息框
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @param leftStr
	 *            左边按钮文字
	 * @param leftClickListener
	 *            左边按钮监听
	 * @param middleStr
	 *            中间按钮文字
	 * @param middleClickListener
	 *            中间按钮监听
	 * @param rightStr
	 *            右边按钮文字
	 * @param rightClickListener
	 *            右边按钮监听
	 * @param cancelOnTouchOutSide
	 *            点击空白区域对话框是否消失
	 */
	public static final void msgBoxThree(final Activity activity,
			final String title, final String msg, final String leftStr,
			final DialogInterface.OnClickListener leftClickListener,
			final String middleStr,
			final DialogInterface.OnClickListener middleClickListener,
			final String rightStr,
			final DialogInterface.OnClickListener rightClickListener,
			final boolean cancelOnTouchOutSide) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle(title);
				builder.setMessage(msg);

				if (!StringUtils.isEmptyOrNull(leftStr)) {
					builder.setPositiveButton(leftStr, leftClickListener);

				}

				if (!StringUtils.isEmptyOrNull(middleStr)) {
					builder.setNeutralButton(middleStr, middleClickListener);
				}

				if (!StringUtils.isEmptyOrNull(rightStr)) {
					builder.setNegativeButton(rightStr, rightClickListener);
				}

				AlertDialog dialog = builder.create();
				dialog.setCanceledOnTouchOutside(cancelOnTouchOutSide);
				dialog.show();
			}
		});
	}

	/********************************** 系统消息框 **************************************/

	/********************************** 进度条 **************************************/

	/**
	 * 初始化圆形进度条
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 */
	public static final void initCircleProgressDialog(final Activity activity,
			String title, String message) {
		initProgressDialog(activity, title, message, 100, false);
	}

	/**
	 * 
	 * 初始化水平进度条
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            内容
	 * @param max
	 *            最大值 //暂未实现此功能，注释先
	 */
	/*
	 * public static final void initCircleProgressDialog(final Activity
	 * activity, String title, String message, int max) {
	 * initProgressDialog(activity, title, message, max, true); }
	 */

	/**
	 * 初始化滚动条
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            消息
	 * @param progress
	 *            进度
	 * @param isHorizone
	 *            是否水平进度条
	 */
	public static final void initProgressDialog(final Activity activity,
			final String title, final String msg, final int progress,
			final boolean isHorizone) {
		if (activity == null) {
			return;
		}

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}

				progressDialog = new MyProgressDialog(activity);

				progressDialog.setTitle(title);
				progressDialog.setCanceledOnTouchOutside(false);

				/*
				 * if(isHorizone) {//初始化水平滚动条
				 * progressDialog.setProgressStyle(ProgressDialog
				 * .STYLE_HORIZONTAL);
				 * 
				 * }else {//初始化圆形滚动条
				 * progressDialog.setProgressStyle(ProgressDialog
				 * .STYLE_SPINNER); }
				 */

				progressDialog.setMessage(msg);
				try {
					progressDialog.show();
				} catch (Exception e) {
					// TODO: handle exception
					e.getMessage();
				}

			}
		});
	}

	/**
	 * 设置滚动条进度
	 * 
	 * @param progress
	 *            //暂未实现此功能，注释先
	 */
	/*
	 * public static final void setProgress(final Activity activity,final int
	 * progress) { if(activity == null) { return; } activity.runOnUiThread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { if(progressDialog!=null) {
	 * progressDialog.setProgress(progress); } } }); }
	 */
	/**
	 * 设置滚动条内容
	 * 
	 * @param message
	 */
	public static final void setProgressBarMessage(final Activity activity,
			final String message) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.setMessage(message);
				}
			}
		});
	}

	/**
	 * 设置滚动条进度
	 * 
	 * @param progress
	 *            //暂未实现此功能，注释先
	 */

	/*
	 * public static final void setProgress(final Activity activity,final String
	 * msg,final int progress) { if(activity == null) { return; }
	 * activity.runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { if(progressDialog!=null) {
	 * progressDialog.setMessage(msg); progressDialog.setProgress(progress); } }
	 * }); }
	 */

	/**
	 * 取消滚动条
	 * 
	 * @param progress
	 */
	public static final void cancelProgressDialog(final Activity activity) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null) {
					progressDialog.cancel();
				}
			}
		});
	}

	public static class MyProgressDialog extends Dialog {

		private TextView tv_msg = null;
		private String msgStr = "";
		LinearLayout ll_linking;
		BitmapDrawable bd;

		MyProgressDialog(Context context) {
			super(context, R.style.CustomProgressDialog);

			init(context);
		}

		private MyProgressDialog(Context context, int theme) {
			super(context, theme);

			init(context);
		}

		private void init(Context context) {
			this.setContentView(R.layout.dlg_myprogress);
			this.setCanceledOnTouchOutside(false);
			tv_msg = (TextView) findViewById(R.id.tv_msg);
			ll_linking = (LinearLayout) findViewById(R.id.ll_linking);

			// 为进度条自定义动画
			// byte[] buf = Base64.decode(Statics.PROGRESS_IMAGE_BASE64,
			// Base64.DEFAULT);
			InputStream inputStream = context.getResources().openRawResource(
					R.drawable.loading_bg);
			byte[] buf = null;
			try {
				buf = Utils.readstream(inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			bd = new BitmapDrawable(BitmapFactory.decodeByteArray(buf, 0,
					buf.length));
			ImageView iv = new ImageView(context);
			LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
			lp.setMargins(10, 10, 10, 5);
			iv.setLayoutParams(lp);
			iv.setImageDrawable(bd);
			RotateAnimation ra = new RotateAnimation(0, 360,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			ra.setRepeatCount(-1);
			ra.setDuration(1500);
			ra.setInterpolator(new LinearInterpolator());
			iv.setAnimation(ra);
			ll_linking.addView(iv);
		}

		public void setMessage(String msg) {
			tv_msg.setText(msg);
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				return false;
			}
			return super.onKeyDown(keyCode, event);
		}

		public void dismiss() {
			try {
				bd = null;
				super.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}

		};

	}

	/********************************** 进度条 **************************************/

	/***************************** Toast ************************************/

	public static final void toast(final Activity activity, String message) {
		toast(activity, message, Toast.LENGTH_LONG);
	}

	/**
	 * toastime : 1 public static int Toast_LENGTH_LONG; 2 public static int
	 * Toast_LENGTH_SHORT;
	 */
	public static final void toast(final Activity activity,
			final String message, final int toastime) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (toast == null) {
					toast = new Toast(activity);
					TextView tv = new TextView(activity);
					tv.setTextColor(Color.parseColor("#FFFFFF"));
					tv.setTextSize(20);
					tv.setPadding(10, 10, 10, 10);
					tv.setBackgroundColor(Color.argb(150, 0, 0, 0));
					// tv.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));;
					toast.setView(tv);
					toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 100);
					toast.setDuration(toastime);
				}
				// toast.cancel();
				toast.setDuration(toastime);
				((TextView) toast.getView()).setText(message);
				toast.show();
			}
		});
	}

	/***************************** Toast ********************************/

	/**
	 * 下拉选择框
	 * 
	 * @param items
	 * @param clickListener
	 */
	public static final void showItems(final Activity activity,
			final String[] items,
			final DialogInterface.OnClickListener clickListener) {
		activity.runOnUiThread(new Runnable() {
			public void run() {

			}
		});
	}
}
