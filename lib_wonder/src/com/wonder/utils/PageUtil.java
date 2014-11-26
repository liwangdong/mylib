package com.wonder.utils;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {
	private static final int PAGESIZE=10;
	private int pageNow;		// 当前页数
	private int pageSize;		// 每页显示多少条记录
	private int totalSize;			// 一共多少记录  
	private int totalPage;			// 共有多少页
	private List<T> mlst;
	private List<T> mCurrentlst;
	
	public PageUtil(){
		mlst=new ArrayList<T>();
		mCurrentlst=new ArrayList<T>();
		pageSize = PAGESIZE;
		pageNow=0;
		totalSize=0;
	}
	public void initData(List<T> lst){
		pageNow = 0; 
		pageSize=PAGESIZE; 
		mlst.clear();
		mCurrentlst.clear();
		if(lst!=null){ 
			mlst.addAll(lst);
		} 
		totalSize = mlst.size();
		getTotalPage();
	}
	public int getTotalPage() { 
		totalPage = totalSize / pageSize;
		if (totalSize % pageSize != 0)
			totalPage++;
		return totalPage;
	} 
	
	public boolean hasNext() { 
		if (isLast())
			return false;
		else
			return true;
	}  
	
	public boolean hasFront(){
		if(isFirst())
			return false;
		else
			return true;
	}
	public boolean isLast() {  
		if (pageNow >= getTotalPage())
			return true;
		else
			return false;
	}   
	public boolean isFirst(){
		if(pageNow<=1)
			return true;
		else
			return false;
	}
	public List<T> getNextPageData(){ 
			pageNow++;
		return getNewPageData(pageNow);
	} 
	public List<T> getFrontPageData(){ 
			pageNow--; 
		return getNewPageData(pageNow);
	} 
	public List<T> getNewPageData(int pageNow){ 
		mCurrentlst.clear();
		if(pageNow>totalPage) return mCurrentlst;
		this.pageNow=pageNow;
		if(isFirst()){
			if(hasNext()){
				mCurrentlst.addAll(mlst.subList(0, pageSize));
			}else{
				mCurrentlst.addAll(mlst);
			}
		}else if(isLast()){
			mCurrentlst.addAll(mlst.subList((pageNow-1)*pageSize, totalSize));
		}else{ 
			mCurrentlst.addAll(mlst.subList((pageNow-1)*pageSize, pageNow*pageSize));
		}
		if(mCurrentlst.isEmpty()) this.pageNow=0;
		return mCurrentlst;
	} 
	
	public List<T> getmCurrentlst() {
		return mCurrentlst;
	}
	public List<T> getMlst() {
		return mlst;
	} 
	public int getPageNow() {
		return pageNow;
	}
	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}
}
