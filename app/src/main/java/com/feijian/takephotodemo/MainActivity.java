package com.feijian.takephotodemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends Activity implements InitTimetoTakePic.OnTakePicListener {

	private final static String TAG = "MainActivity";
	FrameLayout fl_preview;
    private InitTimetoTakePic mItt;

    private boolean isSend = false;
    private Button mCaptureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (Build.VERSION.SDK_INT >= 23){
            File file=new File(Environment.getExternalStorageDirectory(), "/a_picture/pic/"+System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);
            Intent intent = new Intent();
            //设置Action为拍照
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            //将拍取的照片保存到指定URI
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent,1);
        }





        // 创建预览类，并与Camera关联，最后添加到界面布局中

        fl_preview = (FrameLayout) findViewById(R.id.camera_preview);
        mCaptureButton = (Button) findViewById(R.id.button_capture);



        //创建定时拍照任务
        mItt = InitTimetoTakePic.getInstance(MainActivity.this);
        mItt.initView(fl_preview);
        mItt.start();

        mItt.setOnTakePicListener(this);

        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在捕获图片前进行自动对焦

                if(isSend){
                    mItt.startTakePic();
                    isSend = false;
                    mCaptureButton.setText("正在拍照...");
                }else {
                    Toast.makeText(MainActivity.this,"稍等片刻...",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        // 回收Camera资源
//    	releaseCarema();
        super.onDestroy();
        InitTimetoTakePic.getInstance(MainActivity.this).releaseCarema();
    }

    @Override
    public void onStartTakePic() {
        isSend = true;
        mCaptureButton.setText("开始拍照");
    }

    @Override
    public void onFinish() {

    }
}
