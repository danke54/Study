package com.zhangke.servicedemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhangke.servicedemo.service.BindService;
import com.zhangke.servicedemo.service.FirstService;

public class FirstServiceActivity extends AppCompatActivity {

    private Intent intent;
    private Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_service);
    }

    /**
     * 启动服务
     */
    public void startService(View view) {
        intent = new Intent(this, FirstService.class);
        startService(intent);
    }

    public void stopService(View view) {
        stopService(intent);
    }

    public void bindService(View view) {
        intent1 = new Intent(this, BindService.class);
        bindService(intent1, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("绑定成功");
            BindService.LocalService localService = (BindService.LocalService) service;
            BindService service1 = localService.getService();

            System.out.println("绑定成功，获取随机数：" + service1.getRandomNumber());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("取消绑定成功");
        }
    };
}
