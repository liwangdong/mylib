package com.wonder.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewUtil {

	/**
	 * 切换视图的显示
	 * 
	 * @param v
	 *            切换后可见为true else false
	 */
	public static boolean toggle(View v) {
		if (v.getVisibility() == View.GONE) {
			v.setVisibility(View.VISIBLE);
			return true;
		} else {
			v.setVisibility(View.GONE);
			return false;
		}
	}

	public interface ToggleCount {
		/**
		 * 
		 * @param count
		 *            由 1 开始
		 * @return false 执行第一次
		 */
		public boolean atCount(Integer count);

	} 

	/**
	 * source 切换切换视图的显示
	 * 
	 * @param source
	 * @param target
	 */
	public static void setToggle(View source, final View target) {
		source.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				toggle(target);
			}
		});
	}

	public interface RoundToggle {
		/**
		 * 
		 * @param source
		 * @param target
		 * @param visiable
		 *            target的可见型
		 */
		public void before(View source, final View target, boolean visiable);

		public void after(View source, final View target, boolean visiable);
	}

	public static void setToggle(final View source, final View target,
			final RoundToggle round) {
		source.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (target.getVisibility() == View.VISIBLE) {
					round.before(source, target, true);
				} else {
					round.before(source, target, false);
				}
				round.after(source, target, toggle(target));
			}
		});
	}

	public static void setToggleAndPerform(View source, final View target) {
		source.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				toggle(target);
			}
		});
		source.performClick();
	}

	public static void setToggleAndPerform(final View source,
			final View target, final RoundToggle round) {
		source.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (target.getVisibility() == View.VISIBLE) {
					round.before(source, target, true);
				} else {
					round.before(source, target, false);
				}
				round.after(source, target, toggle(target));
			}
		});
		source.performClick();
	}

	/**
	 * 对象绑定视图
	 * 
	 * @param v
	 * @param value
	 */
	public static void bindView(View v, Object value) {
		if (v == null || value == null)
			return;
		if (v instanceof TextView) {
			if (value instanceof CharSequence) {
				((TextView) v).setText((CharSequence) value);
			} else {
				((TextView) v).setText(value.toString());
			}
		}
		if (v instanceof ImageView) {
			if (value instanceof String) {
				ImageLoader.getInstance().displayImage((String)value,(ImageView) v);
			} else if (value instanceof Drawable) {
				((ImageView) v).setImageDrawable((Drawable) value);
			} else if (value instanceof Bitmap) {
				((ImageView) v).setImageBitmap((Bitmap) value);
			} else if (value instanceof Integer) {
				((ImageView) v).setImageResource((Integer) value);
			}
		}
	}

//	/**
//	 * 对象绑定视图,同时通过全局的数据修复
//	 * 
//	 * @param v
//	 * @param value
//	 */
//	public static void bindView(View v, Object value, String type) {
//		if (v == null || value == null)
//			return;
//		if (v instanceof TextView) {
//			ValueFix fix=IocContainer.getShare().get(ValueFix.class);
//			if(fix!=null){
//				value=fix.fix(value, type);
//			}
//			if (value instanceof CharSequence) {
//				((TextView) v).setText((CharSequence) value);
//			} else {
//				((TextView) v).setText(value.toString());
//			}
//		}
//		if (v instanceof ImageView) {
//			if (value instanceof String) {
//				ValueFix fix=IocContainer.getShare().get(ValueFix.class);
//				DisplayImageOptions options = null;
//				if(fix!=null){
//					options=fix.imageOptions(type);
//				}
//				ImageLoader.getInstance().displayImage((String)value,(ImageView) v,options);
//			} else if (value instanceof Drawable) {
//				((ImageView) v).setImageDrawable((Drawable) value);
//			} else if (value instanceof Bitmap) {
//				((ImageView) v).setImageBitmap((Bitmap) value);
//			} else if (value instanceof Integer) {
//				((ImageView) v).setImageResource((Integer) value);
//			}
//		}
//		
//	}

	public static boolean isEmpty(TextView... texts) {
		if (texts == null)
			return false;
		for (int i = 0; i < texts.length; i++) {
			boolean empty = TextUtils.isEmpty(texts[i].getText().toString()
					.trim());
			if (empty) {
				return empty;
			}
		}
		return false;
	}
	/**
	 * 获取edittext中内容
	 * 
	 * @param et
	 * @return
	 */
	public static String getText(EditText et) {
		return et.getText().toString().trim();
	}

	/**
	 * 判断edittext是否为空
	 * 
	 * @param editTexts
	 * @return
	 */
	public static boolean isEmpty(EditText... editTexts) {
		for (EditText editText : editTexts) {
			String content = editText.getText().toString().trim();
			if (content == null || content.length() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将edittext恢复为空
	 * 
	 * @param editTexts
	 */
	public static void setEmpty(EditText... editTexts) {
		for (EditText editText : editTexts) {
			editText.setText("");
		}
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(EditText mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(getText(mobiles));
		return m.matches();
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param editText
	 */
	public static void hideSoftKeyboard(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	/**
	 * 判断网络连接
	 * */
	public static boolean isNetworkConnect(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		cm.getActiveNetworkInfo();
		if (cm.getActiveNetworkInfo() != null) {
			return cm.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * 打开网络对话框
	 */
	public static void whetherOpenNet(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle("网络木有连接")
				.setMessage("是否打开网络连接")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								context.startActivity(new Intent(
										Settings.ACTION_WIRELESS_SETTINGS));
							}
						}).setNeutralButton(android.R.string.cancel, null)
				.show();
	}
}
