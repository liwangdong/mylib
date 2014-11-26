package com.wonder.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.wonder.mylib.R;

/**
 * 自定义相机demo
* 
*/
public class MyCameraActivity extends Activity implements Callback,
                OnClickListener, AutoFocusCallback {
        SurfaceView mySurfaceView;// surfaceView声明
        SurfaceHolder holder;// surfaceHolder声明
        Camera myCamera;// 相机声明
        String filePath = "/sdcard/wjh.jpg";// 照片保存路径
        boolean isClicked = false;// 是否点击标识
        // 创建jpeg图片回调数据对象
        PictureCallback jpeg = new PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                        // TODO Auto-generated method stub
                        try {// 获得图片
                                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                File file = new File(filePath);
                                BufferedOutputStream bos = new BufferedOutputStream(
                                                new FileOutputStream(file));
                                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 将图片压缩到流中
                                bos.flush();// 输出
                                bos.close();// 关闭
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                }
        };

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
                // 设置拍摄方向
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                setContentView(R.layout.my_camera_demo_activity);
                // 获得控件
                mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
                // 获得句柄
                holder = mySurfaceView.getHolder();
                // 添加回调
                holder.addCallback(this);
                // 设置类型
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                // 设置监听
                mySurfaceView.setOnClickListener(this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                        int height) {
                // TODO Auto-generated method stub
                // 设置参数并开始预览
                Camera.Parameters params = myCamera.getParameters();
                params.setPictureFormat(PixelFormat.JPEG);
                params.setPreviewSize(640, 480);
                myCamera.setParameters(params);
                myCamera.startPreview(); // open display

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                // 开启相机
                if (myCamera == null) {
                        myCamera = Camera.open(); // this method is a static method
                        try {
                                myCamera.setPreviewDisplay(holder); // connection SurfaceHolder
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                // 关闭预览并释放资源
                myCamera.stopPreview(); // close display
                myCamera.release();
                myCamera = null;

        }

        @Override
        public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isClicked) {
                        myCamera.autoFocus(this);// 自动对焦
                        isClicked = true;
                } else {
                        myCamera.startPreview();// 开启预览
                        isClicked = false;
                }

        }

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if (success) {
                        // 设置参数,并拍照
                        Camera.Parameters params = myCamera.getParameters();
                        params.setPictureFormat(PixelFormat.JPEG);
                        params.setPreviewSize(640, 480);
                        myCamera.setParameters(params);
                        myCamera.takePicture(null, null, jpeg);
                }

        }
}