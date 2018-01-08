package com.zhangke.eventbusdemo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhangke on 2017/12/4.
 */

public class MessageEvent extends MessageEventA {

    private String message;

    public MessageEvent(String message, int number) {
        super(number);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
