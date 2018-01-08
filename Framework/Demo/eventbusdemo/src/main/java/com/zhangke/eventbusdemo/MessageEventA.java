package com.zhangke.eventbusdemo;

/**
 * Created by zhangke on 2017/12/11.
 */

public class MessageEventA {

    private int number;

    public MessageEventA(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
