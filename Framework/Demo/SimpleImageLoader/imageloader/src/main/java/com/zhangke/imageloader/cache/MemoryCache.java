package com.zhangke.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.zhangke.imageloader.request.BitmapRequest;

/**
 * 内存缓存策略
 * Created by zhangke on 2018/4/4.
 */

public class MemoryCache implements BitmapCache {
    private LruCache<String, Bitmap> mLruCache;

    public MemoryCache() {
        int maxSize = (int) (Runtime.getRuntime().freeMemory() / 1024 / 8);
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        mLruCache.put(request.getImageUriMD5(), bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return mLruCache.get(request.getImageUriMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        mLruCache.remove(request.getImageUriMD5());
    }
}
