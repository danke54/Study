package com.zhangke.imageloader.cache;

import android.graphics.Bitmap;

import com.zhangke.imageloader.request.BitmapRequest;

/**
 * 缓存策略
 * Created by zhangke on 2018/4/3.
 */

public interface BitmapCache {

    /**
     * 缓存bitmap
     *
     * @param bitmap
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 通过请求取Bitmap
     *
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request);

    /**
     * 移除缓存
     *
     * @param request
     */
    void remove(BitmapRequest request);
}
