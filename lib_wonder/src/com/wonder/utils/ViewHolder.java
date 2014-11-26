package com.wonder.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 
 * 
 * @author xch
 * @e-mail 413622944@qq.com
 * @version v1.0
 * @copyright 2010-2015
 * @create-time 2014年8月5日 下午3:59:08
 * 
 */
public class ViewHolder {
	@SuppressWarnings("unchecked")
	public static<T extends View> T get(View view,int id){
		//匹配多种viewHolder视图缓存 
		//根据view id判断视图缓存与当前视图是否匹配 id为空时 默认使用getTag获取缓存
		SparseArray<View> viewHolder=view.getId()!=-1?
				(SparseArray<View>)view.getTag(view.getId()):(SparseArray<View>)view.getTag();  
		if(viewHolder==null){
			viewHolder=new SparseArray<View>();
			if(view.getId()!=-1){
				view.setTag(view.getId(),viewHolder);
			}else{
				view.setTag(viewHolder);
			} 
//			LogUtils.e("0:::父节点");
		}
		View childView=viewHolder.get(id);
		if(childView==null){
			childView=view.findViewById(id);
			viewHolder.put(id, childView); 
//			LogUtils.e(id+":::子节点");
		}
//		LogUtils.e(id+":::进来了");
		return (T)childView;
	}
}
