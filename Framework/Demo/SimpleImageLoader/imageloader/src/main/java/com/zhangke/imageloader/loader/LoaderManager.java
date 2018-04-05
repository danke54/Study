package com.zhangke.imageloader.loader;

import android.util.Log;

import com.zhangke.imageloader.request.BitmapRequest;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by zhangke on 2018/4/4.
 */

public class LoaderManager {
    //缓存所有支持的Loader类型
    private Map<String, Loader> mLoaderMap = new HashMap<>();

    private static LoaderManager mInstance = new LoaderManager();

    private LoaderManager() {
        register("http", new NetLoader());
        register("https", new NetLoader());
        register("file", new LocalLoader());
    }

    public static LoaderManager getInstance() {
        return mInstance;
    }

    private void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }

    public Loader getLoader(String url) {
        String schema = parseSchema(url);
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return new NullLoader();
    }


    private String parseSchema(String url) {
        if (url.contains("://")) {
            return url.split("://")[0];
        } else {
            Log.i(TAG, "不支持此类型");
        }

        return null;
    }


}
