package com.zhangke.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.zhangke.imageloader.request.BitmapRequest;

/**
 * 双缓存
 * Created by zhangke on 2018/4/4.
 */

public class DoubleCache implements BitmapCache {

    private MemoryCache memoryCache;
    private DiskCache diskCache;

    public DoubleCache(Context context) {
        memoryCache = new MemoryCache();
        diskCache = DiskCache.getInstance(context);
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        memoryCache.put(request, bitmap);
        diskCache.put(request, bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = memoryCache.get(request);
        if (bitmap == null) {
            bitmap = diskCache.get(request);
            if (bitmap != null) {
                //放入内存，方便再获取
                memoryCache.put(request, bitmap);
            }
        }
        return bitmap;

    }

    @Override
    public void remove(BitmapRequest request) {
        memoryCache.remove(request);
        diskCache.remove(request);
    }
}
