package com.zhangke.imageloader.loader;

import android.graphics.Bitmap;

import com.zhangke.imageloader.request.BitmapRequest;

/**
 * 加载图片
 * <p>
 * Created by zhangke on 2018/4/4.
 */

public interface Loader {

    /**
     * 加载图片
     *
     * @param request
     */
    void loadImage(BitmapRequest request);


}
