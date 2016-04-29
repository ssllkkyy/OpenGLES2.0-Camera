package com.example.t_rtf_camera;

/**
 * Created by james.li on 2016/4/29.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends Activity {

    final private String TAG = "MyYUVViewer";
    final private String FILE_NAME = "yuv_320_240.yuv";
    private int width = 352;
    private int height = 288;
    private int size = width * height * 3/2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nativeTest();
        SurfaceView surfaceview = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceview.getHolder();
        holder.addCallback(new Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                Log.d(TAG,"surfaceCreated");
                byte[]yuvArray = new byte[size];
                readYUVFile(yuvArray, FILE_NAME);
                nativeSetVideoSurface(holder.getSurface());
                nativeShowYUV(yuvArray,width,height);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }});
    }

    private boolean readYUVFile(byte[] yuvArray,String filename){
        try {
            // 如果手机插入了SD卡，而且应用程序具有访问SD的权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡对应的存储目录
                File sdCardDir = Environment.getExternalStorageDirectory();
                // 获取指定文件对应的输入流
                FileInputStream fis = new FileInputStream(
                        sdCardDir.getCanonicalPath() +"/" + filename);
                fis.read(yuvArray, 0, size);
                fis.close();
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private native void nativeTest();
    private native boolean nativeSetVideoSurface(Surface surface);
    private native void nativeShowYUV(byte[] yuvArray,int width,int height);
    static {
        System.loadLibrary("showYUV");
    }
}
