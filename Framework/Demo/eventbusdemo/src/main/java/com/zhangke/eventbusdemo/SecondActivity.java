package com.zhangke.eventbusdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        System.out.println("SecondActivity：收到消息");
    }

    public void send(View view) {

        EventBus.getDefault().post(new MessageEvent("hello，eventbus", (int) Math.random() * 10));
    }

    public void sendSticky(View view) {
        EventBus.getDefault().postSticky(new StickyEvent("hello，sticky event"));
    }

    public void startSticky(View view) {
        startActivity(new Intent(this, ThirdActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
