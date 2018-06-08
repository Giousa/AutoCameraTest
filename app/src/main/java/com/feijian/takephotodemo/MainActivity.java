package com.feijian.takephotodemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


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
