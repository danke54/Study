package com.example.itemtouchhelperdemo;

/**
 * Created by zhangke on 16/7/3.
 */
public class Message {
    /**
     * 头像
     */
    private int image;
    /**
     * 标题
     */
    private String title;


    public Message(int image, String title){
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
