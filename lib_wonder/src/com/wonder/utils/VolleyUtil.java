package com.wonder.utils;

import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;

import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.wonder.android.volley.MyVolley;

public class VolleyUtil {
	public static void httpGet(String JsonUrl,Listener<String> listener){
		RequestQueue queue = MyVolley.getRequestQueue();
		StringRequest request = new 
				MyStringRequest(Method.GET, 
		   				JsonUrl, listener, null);
			 queue.add(request);
	}
	static class MyStringRequest extends StringRequest {
		//改变编码
		@Override
		protected Response<String> parseNetworkResponse(NetworkResponse response) {
			String string = null;
			try {
				string = new String(response.data, HTTP.UTF_8);
				// new String(response.data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return Response.success(string,
					HttpHeaderParser.parseCacheHeaders(response));
		}

		public MyStringRequest(int method, String url,
				Listener<String> listener, ErrorListener errorListener) {
			super(method, url, listener, errorListener);
			// TODO Auto-generated constructor stub
		}

	}
}
