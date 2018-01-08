package com.zhangke.messengerclientdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * 客户端发送的消息标识
     */
    public static final int MSG_CODE_FROM_CLIENT = 1;
    /**
     * 服务端发送的消息标识
     */
    public static final int MSG_CODE_FROM_SERVER = 2;

    private EditText mEtPrice;
    private EditText mEtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mEtCount = (EditText) findViewById(R.id.et_count);
    }

    /**
     * 1、绑定服务
     */
    public void bindService(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.zhangke.messengerserverdemo", "com.zhangke.messengerserverdemo.IPayService"));
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    Messenger mServerMessenger;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            /**
             * 2、通过服务端返回的binder对象创建一个Messenger对象, 通过该Messenger对象可以给服务端发送Message
             */
            mServerMessenger = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServerMessenger = null;
        }
    };

    /**
     * 3、 通过Messenger对象给服务端发送Message
     */
    public void pay(View view) throws RemoteException {
        Message message = Message.obtain(null, MSG_CODE_FROM_CLIENT);

        message.arg1 = Integer.valueOf(mEtPrice.getText().toString().trim());
        message.arg2 = Integer.valueOf(mEtCount.getText().toString().trim());

        Bundle bundle = new Bundle();
        bundle.putString("desc", "优惠价格");
        message.setData(bundle);

        /**
         * 3、实现双向通信，将客户端Messenger对象传递到服务端
         */
        message.replyTo = mClientMessenger;

        mServerMessenger.send(message);
    }


    /**
     * 2、实现双向通信，创建一个客户端Messenger对象，该对象会被传递到服务端，然后服务端会通过该Messenger发送消息给客户端。
     */
    Messenger mClientMessenger = new Messenger(new ResultHander());

    /**
     * 1、实现双向通信，创建一个Handler对象，用于处理服务端返回的消息
     */
    private class ResultHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_FROM_SERVER:
                    String result = msg.getData().getString("result");
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

}
