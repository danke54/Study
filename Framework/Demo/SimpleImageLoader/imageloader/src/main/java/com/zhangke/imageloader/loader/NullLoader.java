package com.zhangke.imageloader.loader;

import android.graphics.Bitmap;

import com.zhangke.imageloader.request.BitmapRequest;

/**
 * Created by zhangke on 2018/4/4.
 */

class NullLoader extends AbstractLoader {
    @Override
    public Bitmap onLoad(BitmapRequest request) {
        return null;
    }
}
