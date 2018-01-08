package com.zhangke.eventbusdemo;

/**
 * Created by zhangke on 2017/12/4.
 */

public class StickyEvent {

    private String message;

    public StickyEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
