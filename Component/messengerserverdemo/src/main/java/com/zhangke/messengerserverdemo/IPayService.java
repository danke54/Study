package com.zhangke.messengerserverdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * 服务：通过Messenger实现进程间通信
 */
public class IPayService extends Service {
    public static final String TAG = "IPayService";
    /**
     * 客户端发送的消息标识
     */
    public static final int MSG_CODE_FROM_CLIENT = 1;
    /**
     * 服务端发送的消息标识
     */
    public static final int MSG_CODE_FROM_SERVER = 2;

    public IPayService() {}

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("远程绑定服务...");
        /**
         * 3、通过Messenger获取binder对象，并通过service的onBind方法将binder对象返回到客户端
         */
        return mMessenger.getBinder();
    }

    /**
     * 2、声明并创建一个Messenger对象，并将Handler作为参数传入
     */
    final Messenger mMessenger = new Messenger(new PayHandler());

    /**
     * 1、声明一个Handler类，用于处理客户端Message
     */
    class PayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_FROM_CLIENT:

                    int price = msg.arg1;
                    int count = msg.arg2;
                    String desc = msg.getData().getString("desc");

                    int totalPrice = price * count;
                    Log.e(TAG, "总价：" + totalPrice + "\t" + desc);

                    /**
                     * 实现双向通信，向客户端做出响应
                     */
                    sendResult(msg, totalPrice);

                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

        /**
         * 实现双向通信，该方法用于向客户端发送消息
         */
        private void sendResult(Message msg, int totalPrice) {
            try {

                Messenger clientMessenger = msg.replyTo;
                Message message = Message.obtain(null, MSG_CODE_FROM_SERVER);
                Bundle resultData = new Bundle();
                resultData.putString("result", "支付成功，一共花费 " + totalPrice + " 元");
                message.setData(resultData);
                clientMessenger.send(message);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
