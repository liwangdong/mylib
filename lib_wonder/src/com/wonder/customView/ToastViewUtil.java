package com.wonder.customView;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wonder.mylib.R;

public final class ToastViewUtil {
	private static Toast result;
	public static void showToast(Context context, String msg, int duration)
	{

		if (result != null)
		{
			result.cancel();
		}
		result = new Toast(context);
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.custom_toast, null);
		TextView tv = (TextView) v.findViewById(R.id.textView1);
		tv.setText(msg);

		result.setView(v);
		result.setGravity(Gravity.TOP, 20, 20);
		result.setDuration(Toast.LENGTH_SHORT);
		result.show();
	
	}
	
	private ToastViewUtil(){}
}
