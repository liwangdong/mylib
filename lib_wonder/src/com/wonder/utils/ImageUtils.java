package com.wonder.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
public class ImageUtils {   
	
	
	/**
	 * 从sd卡读取图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap readBitMap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		Bitmap bm = BitmapFactory.decodeFile(path, options);
		return bm;
	}
	
	/**
	 * 从SDCard读取图片时压缩
	 * @param srcPath
	 * @return
	 */
	public static Bitmap compressImageFromFile(String srcPath,
			float ww, float hh )
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
//		float hh = 800f;//
//		float ww = 480f;//
		int be = 1;
		// 缩放到可以在480x800的屏幕上完整显示图片
		if (w >= h && w >= ww)
		{
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh)
		{
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 将图片压缩到刚好小于100kb为止
	 * @param image
	 * @return 压缩完的图片
	 */
	public static Bitmap compressBmpFromBmp(Bitmap image)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 压缩到刚好小于100kb为止
		while (baos.toByteArray().length / 1024 > 100)
		{
			baos.reset();
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}
	
	/**
	 * 图片旋转
	 * 
	 * @param bmp
	 * @param degree
	 * @return
	 */
	public static Bitmap postRotateBitamp(Bitmap bmp, float degree) {
		// 获得Bitmap的高和宽
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		// 产生resize后的Bitmap对象
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
		return resizeBmp;
	}

	/**
	 *  图片翻转
	 * @param bmp
	 * @param flag
	 * @return
	 */
	public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
		float[] floats = null;
		switch (flag) {
		case 0: // 水平反转
			floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
			break;
		case 1: // 垂直反转
			floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
			break;
		}

		if (floats != null) {
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}

		return bmp;
	} 
	
	/**
	 * 图片转换 字节转drawable
	 * @param byteArray
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] byteArray){
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}
	/**
	 * 图片转换 图片转字节
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm){ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * drawable 转bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newbmp;
	}
	/**
	 * bitmap转为base64
	 * @param bitmap
	 * @return String
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * @param base64Data
	 * @return bitmap
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	/**
	 * 获取获得带倒影的图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}
 	/**
     * 图片去色,返回灰度图片
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    } 
	    
    /**
     * 把图片变成圆角
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    
   /**
     * 使圆角功能支持BitampDrawable
     * @param bitmapDrawable 
     * @param pixels 
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }
	    
	
    public static Bitmap makeReflectionBitmap(Bitmap originalImage){
    	  final int reflectionGap = 4;
          int width = originalImage.getWidth();
          int height = originalImage.getHeight();
          // 实现图片的反转
          Matrix matrix = new Matrix();
          matrix.preScale(1, -1);
          matrix.postRotate(360f);
          
          // 创建反转后的图片Bitmap对象，图片高是原图的一半。
          Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                  height/2, width, height/2, matrix, false);
          // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
          
          Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                  (height + height/2), Config.ARGB_8888);
          // 创建画布对象，将原图画于画布，起点是原点位置。
          Canvas canvas = new Canvas(bitmapWithReflection);
          canvas.drawBitmap(originalImage, 0, 0, null);//(originalImage, matrix, new Paint());
          //将反转后的图片画到画布中。
          Paint defaultPaint=new Paint();
          canvas.drawRect(0, height,width,height+reflectionGap,defaultPaint);
          canvas.drawBitmap(reflectionImage, 0 , height+reflectionGap, null);
          Paint paint=new Paint();
          //创建线性渐变LinearGradient 对象。
          LinearGradient shader=new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight()+reflectionGap,0X70ffffff,0X00ffffff,TileMode.MIRROR);
          paint.setShader(shader);
          paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
          canvas.drawRect(0,height,width,bitmapWithReflection.getHeight()+reflectionGap, paint);
          return bitmapWithReflection;
    	     }
    public static Bitmap readBitMap(Context context, int resId){  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inPreferredConfig = Bitmap.Config.RGB_565;   
        options.inPurgeable = true;  
        options.inInputShareable = true;  
   	try {
		// 4. inNativeAlloc 属性设置为true，可以不把使用的内存算到VM里
		BitmapFactory.Options.class.getField("inNativeAlloc")
				.setBoolean(options, true);
	} catch (Exception e) {
		e.printStackTrace();
	}
          //获取资源图片  
       InputStream is = context.getResources().openRawResource(resId);  
           return BitmapFactory.decodeStream(is,null,options);  
    }  

	public static Bitmap getScaleBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
		Bitmap scale = null;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float wRatio = maxWidth / width;
		float hRatio = maxHeight / height;
		float Ratio = 1;
		if (maxWidth == 0 && maxHeight == 0) {
			Ratio = 1;
		} else if (maxWidth == 0) {//
			if (hRatio < 1)
				Ratio = hRatio;
		} else if (maxHeight == 0) {
			if (wRatio < 1)
				Ratio = wRatio;
		} else if (wRatio < 1 || hRatio < 1) {
			Ratio = (wRatio <= hRatio ? wRatio : hRatio);
		}
		if (Ratio < 1) {
			width =(int) (width*Ratio);
			height =(int) (height*Ratio);
		}
//		Matrix m=new Matrix();
//		float scaleWidth=(float)maxWidth/width;
//		float scaleHeight=(float)maxWidth/height;
//		m.postScale(scaleWidth, scaleHeight);
//		scale=Bitmap.createBitmap(bitmap,0,0, width, height,m, true);
		scale=Bitmap.createScaledBitmap(bitmap, width, height, true);
	
		return scale;
	}

	public static Bitmap getBitmapByString(String imageData) {
		if (imageData != null) {
			byte[] data = Base64.decode(imageData, Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		return null;
	}

	public static String getStringByImage(Bitmap image) { 
		// Log.w("AddPersonalInfoActivity", " Image: " + image);
		if (image == null) {
			return null;
		}
		image=getScaleBitmap(image, 400, 600);// 控制图片大小
		String string = null; 
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 90, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}
 
	/**
	 * 离线头像灰度处理
	 * @param myImage
	 */
	public static void colorFilter(Drawable myImage){
		myImage.mutate();  
		myImage.clearColorFilter();  
		myImage.setColorFilter(new  ColorMatrixColorFilter(BT_SELECTED));  
		
	}
	//像素过滤矩阵       
	private final static float [] BT_SELECTED =  new   float [] {         
	    0.308f,  0.609f,  0.082f,  0 ,  0 ,       
	    0.308f,  0.609f,  0.082f,  0 ,  0 ,  
	    0.308f,  0.609f,  0.082f,  0 ,  0 ,  
	    0 ,  0 ,  0 ,  1 ,  0   
	};  
	/**
	 * 保存bitmap为图片 默认缩放 
	 * @param bitName
	 * @param mBitmap
	 * @throws IOException
	 */
	public static File saveBitmap2JPG(String fileName,Bitmap mBitmap) throws IOException {
		return saveBitmap2JPG(fileName,mBitmap,true);
	}
	public static File saveBitmap2JPG(String fileName,Bitmap mBitmap,boolean isZoom) throws IOException {
		File f=null;
		if (mBitmap != null) {  
			if(isZoom)
				mBitmap=getScaleBitmap(mBitmap, 600, 800);// 控制图片大小
		      f = new File(FileUtil.getImageDir(),fileName);
		    f.createNewFile();
		    FileOutputStream fOut = null;
		    try {
		            fOut = new FileOutputStream(f);
		    } catch (FileNotFoundException e) {
		            e.printStackTrace();
		    }
		    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		    try {
		            fOut.flush();
		    } catch (IOException e) {
		            e.printStackTrace();
		    }
		    try {
		            fOut.close();
		    } catch (IOException e) {
		            e.printStackTrace();
		    } 
		}
		return f;
	}
	
	/**
	 * 给照片添加文字水印
	 * @param str 水印内容
	 * @param photo 原图
	 * @return 处理后的图片
	 */
	public static Bitmap createTextWatermark(String str,Bitmap photo){
	       int width = photo.getWidth(), height = photo.getHeight();
//	       System.out.println("宽:"+width+"高:"+height);
	       Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //建立一个空的BitMap  
	       Canvas canvas = new Canvas(newBitmap);//初始化画布,将原图像和水印分别绘制在此画布上
	        
	       Paint photoPaint = new Paint(); //建立画笔  
	       photoPaint.setDither(true); //获取更清晰的图像采样  
	       photoPaint.setFilterBitmap(true);//过滤一些  
	        
	       Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());//创建一个指定的新矩形的坐标  
	       Rect dst = new Rect(0, 0, width, height);//创建一个指定的新矩形的坐标  
	       canvas.drawBitmap(photo, src, dst, photoPaint);//将photo 缩放或扩大到 dst使用的填充区  
	        
	       //按图片分辨率动态改变字体大小(例：标准为分辨率640*480时字体大小为35.0f)
	       float ratioWidth=(float)width/640;
	       float ratioHeight=(float)height/480;
	       float RATIO = Math.min(ratioWidth, ratioHeight);//最小纵横比
	       
	       Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔  
	       textPaint.setTextSize(35.0f*RATIO);//水印字体大小  
	       textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度  
	       textPaint.setColor(Color.RED);//水印字体采用的颜色  
	       textPaint.setShadowLayer(3f, 1, 1,color.background_dark);//阴影的设置  
	       
//	       float strWidth = textPaint.measureText(str);//得到字符串所占的宽度
	       Rect textBounds = new Rect();
	       textPaint.getTextBounds(str, 0, str.length(), textBounds );
	       int strWidth = textBounds.width();//得到字符串所占的宽度
//	       int strHeight = textBounds.height();//得到字符串所占的高度
	       
	       canvas.drawText(str, width-strWidth, height, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制 
	       canvas.save(Canvas.ALL_SAVE_FLAG); 
	       canvas.restore();
		return newBitmap; 
	}
	
	/**
	 * 添加图片水印（将两图合并）
	 * @param src 原图
	 * @param watermark 第二张图
	 * @return 合成后的图
	 */
	public static Bitmap createIconWatermark(Bitmap src, Bitmap watermark){
		if (src == null)
		{
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储

		return newb;
	}
}
