package com.wonder.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lidroid.xutils.util.LogUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

/**
 * 图片上传工具类 调用 getPhoto 在onactivityResult 调用onPhotoFromCamera或onPhotoFromPick
 * 
 */

public class PhotoUtil {

	/**
	 * 因为处理不同
	 * 
	 * @param takePhotoCode
	 * @param imageCode
	 * @param tempFile
	 *            拍照时的临时文件 需要zoom时
	 * 
	 *            Uri originalUri = data.getData();
	 *            image=ImageUtil.getBitmapFromUrl(originalUri.toString());
	 ********************************************************************************** 
	 *            Bundle extras = data.getExtras(); image =
	 *            (Bitmap)extras.get("data");
	 * 
	 **/
	public static boolean getPhoto(final Activity activity,
			final int takePhotoCode, final int imageCode, final File tempFile) {
		final CharSequence[] items = { "相册", "拍照" };
		AlertDialog dlg = new AlertDialog.Builder(activity).setTitle("选择图片")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(tempFile));
							activity.startActivityForResult(getImageByCamera,
									takePhotoCode);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							activity.startActivityForResult(getImage, imageCode);
						}
					}
				}).create();
		dlg.show();
		return true;
	}

	// 调用相机取得照片
	public static boolean takePhoto(Activity activity, int takePhotoCode,
			File tempFile) {
		Intent getImageByCamera = new Intent(
				"android.media.action.IMAGE_CAPTURE");
		getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(tempFile));
		activity.startActivityForResult(getImageByCamera, takePhotoCode);
		return true;
	}

	// 从相册取得照片
	public static boolean pickPhoto(Activity activity, int imageCode,
			File tempFile) {
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/jpeg");
		activity.startActivityForResult(getImage, imageCode);
		return true;
	}

	/**
	 * 拍照获取图片的方式 用于切割的图片大小被限制在500,500
	 * 
	 * @param context
	 * @param zoomCode
	 * @param temppath
	 *            拍照前生成的临时路径
	 * @return 新的路径
	 */
	public static String onPhotoFromCamera(final Activity context,
			final int zoomCode, final String temppath, final int aspectX,
			final int aspectY, final int outx) {
		try {
			Bitmap btp = getLocalImage(new File(temppath), 1000, 1000);
			compressImage(btp, new File(temppath + "temp.jpg"), 30);
			photoZoom(context, Uri.fromFile(new File(temppath + "temp.jpg")),
					Uri.fromFile(new File(temppath)), zoomCode, aspectX,
					aspectY, outx);
		} catch (Exception e) {
			Toast.makeText(context, "图片加载失败", 1000).show();
		}

		return temppath;
	}

	/**
	 * 图片切割完调用 如果还需要 Bitmap 调用getLocalImage
	 * 
	 * @param path
	 * @param rw
	 * @param rh
	 * @param compress
	 * @return
	 */
	public static File onPhotoZoom(String path, int rw, int rh, int compress) {

		File f = new File(path);
		Bitmap btp = PhotoUtil.getLocalImage(f, rw, rh);
		compressImage(btp, f, compress);
		return f;
	}

	/**
	 * 相册获取图片,用于切割的图片大小被限制在500,500
	 * 
	 * @param context
	 * @param zoomCode
	 * @param temppath
	 *            希望生成的路劲
	 * @param data
	 */
	public static void onPhotoFromPick(final Activity context,
			final int zoomCode, final String temppath, final Intent data,
			final int aspectX, final int aspectY, final int outx) {
		try {
			Bitmap btp = checkImage(context, data);
			compressImage(btp, new File(temppath + "temp.jpg"), 30);
			PhotoUtil.photoZoom(context,
					Uri.fromFile(new File(temppath + "temp.jpg")),
					Uri.fromFile(new File(temppath)), zoomCode, aspectX,
					aspectY, outx);
		} catch (Exception e) {
			Toast.makeText(context, "图片加载失败", 1000).show();
		}
	}

	/**
	 * data 中检出图片
	 * 
	 * @param activity
	 * @param data
	 * @return
	 */
	public static Bitmap checkImage(Activity activity, Intent data) {
		Bitmap bitmap = null;
		try {
			Uri originalUri = data.getData();
			String path = getRealPathFromURI(activity, originalUri);
			File f = activity.getExternalCacheDir();
			String pp = f.getAbsolutePath();
			if (path.indexOf(pp) != -1) {
				path = path.substring(path.indexOf(pp), path.length());
			}
			bitmap = getLocalImage(new File(path), 500, 500);
		} catch (Exception e) {
		}

		return bitmap;
	}

	/**
	 * 通过URI 获取真实路劲
	 * 
	 * @param activity
	 * @param contentUri
	 * @return
	 */
	public static String getRealPathFromURI(Activity activity, Uri contentUri) {
		Cursor cursor = null;
		String result = contentUri.toString();
		String[] proj = { MediaColumns.DATA };
		cursor = activity.managedQuery(contentUri, proj, null, null, null);
		if (cursor == null)
			throw new NullPointerException("reader file field");
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
			if (Integer.parseInt(Build.VERSION.SDK) < 14) {
				cursor.close();
			}
		}
		return result;
	}

	/**
	 * 图片压缩 上传图片时建议compress为30
	 * 
	 * @param bm 需要压缩的图片
	 * @param f 压缩后保存的文件
	 * @param compress 压缩率，30表示压缩掉70%; 如果不压缩是100，表示压缩率为0  
	 */
	public static void compressImage(Bitmap bm, File f, int compress) {
		if (bm == null)
			return;
		File file = f;
		if (file.exists()) {
			file.delete();
		}
		OutputStream os=null; 
		try {
			file.createNewFile();
			os = new FileOutputStream(file);
			bm.compress(android.graphics.Bitmap.CompressFormat.JPEG, compress,os);
			LogUtils.e("照片大小："+file.length()/1024);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 由本地文件获取希望大小的文件
	 * 
	 * @param f
	 * @return
	 */
	public static Bitmap getLocalImage(File f, int swidth, int sheight) {
		File file = f;
		if (file.exists()) {
			try {
				file.setLastModified(System.currentTimeMillis());
				FileInputStream in = new FileInputStream(file);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, options);
				int sWidth = swidth;
				int sHeight = sheight;
				int mWidth = options.outWidth;
				int mHeight = options.outHeight;
				LogUtils.e("图片-w:"+mWidth+",h:"+mHeight);
				int s = 1;
				while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2)) {
					s *= 2;
				}
				options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = s;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inPurgeable = true;
				options.inInputShareable = true;
				try {
					// 4. inNativeAlloc 属性设置为true，可以不把使用的内存算到VM里
					BitmapFactory.Options.class.getField("inNativeAlloc")
							.setBoolean(options, true);
				} catch (Exception e) {
				}
				in.close();
				// 再次获取
				in = new FileInputStream(file);
				Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
				LogUtils.e("图片压缩后-w:"+options.outWidth+",h:"+options.outHeight);
				in.close();
				return bitmap;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 从本地得到等比例缩放后的照片
	 * @param f 图片文件
	 * @param pixel 像素
	 * @return 缩放后的bitmap
	 */
	public static Bitmap getLocalImage(File f, int pixel) {
		File file = f;
		if (file.exists()) {
			file.setLastModified(System.currentTimeMillis());
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 先指定原始大小
				options.inSampleSize = 1;
				// 只进行大小判断
				options.inJustDecodeBounds = true;
				// 调用此方法得到options得到图片的大小
				BitmapFactory.decodeStream(in, null, options);
				// 我们的目标是在给定pixel的画面上显示。
				// 所以需要调用computeSampleSize得到图片缩放的比例
				options.inSampleSize = computeSampleSize(options, pixel);
				// 得到了缩放的比例，开始正式读入BitMap数据
				options.inJustDecodeBounds = false;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				in = new FileInputStream(file);
				// 根据options参数，减少所需要的内存
				Bitmap sourceBitmap = BitmapFactory.decodeStream(in, null,
						options);
				return sourceBitmap;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public static Bitmap getLocalImage(String photoBase64, int swidth,
			int sheight) {
		byte[] bitmapArray = Base64.decode(photoBase64, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
				bitmapArray.length);
		return bitmap;
	}

	/**
	 * 对图片的大小进行判断，并得到合适的缩放比例
	 * @param options
	 * @param target
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int target) {
		int w = options.outWidth;
		int h = options.outHeight;
//		LogUtils.e("w:"+w+"--h:"+h);
		int candidateW = w / target;
		int candidateH = h / target;
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0)
			return 1;
		if (candidate > 1) {
			if ((w > target) && (w / candidate) < target)
				candidate -= 1;
		}
		if (candidate > 1) {
			if ((h > target) && (h / candidate) < target)
				candidate -= 1;
		}
		// if (VERBOSE)
		Log.e("", "for w/h " + w + "/" + h + " returning " + candidate + "("
				+ (w / candidate) + " / " + (h / candidate));
		return candidate;
	}

	/**
	 * aspectY Y对于X的比例 outputX X 的宽
	 * **/
	public static void photoZoom(Activity activity, Uri uri, Uri outUri,
			int requestCode, int aspectX, int aspectY, int outputX) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		if (aspectY > 0) {
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
		}
		intent.putExtra("scale", aspectX == aspectY);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); //
		activity.startActivityForResult(intent, requestCode);
	}

}
