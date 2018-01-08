package com.zhangke.servicedemo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

public class HandlerThreadActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        button = findViewById(R.id.btn);

        HandlerThread handlerThread = new HandlerThread("WorkThread");
        handlerThread.start();

        final Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 这里可以执行耗时操作,因为在子线程中
                try {
                    System.out.println("resid:" + msg.what+"正在下载");
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("resid:" + msg.what+"下载完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            private int resid = 0;
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(++resid);
            }
        });
    }
}
