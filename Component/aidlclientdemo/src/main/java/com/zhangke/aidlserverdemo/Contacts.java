package com.zhangke.aidlserverdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangke on 2017/11/17.
 */

public class Contacts implements Parcelable {

    private String name;
    private String phone;

    public Contacts(Parcel in) {
        name = in.readString();
        phone = in.readString();
    }

    public Contacts(String s, String s1) {
        this.name = s;
        this.phone = s1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "[name:" + name + ", phone:" + phone + "]";
    }
}
