package com.zhangke.imageloader.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhangke on 2018/4/4.
 */

public class FileUtils {


    /**
     * 获取图片缓存路径
     *
     * @return
     */
    public static File getCacheDir() {
        //得到缓存的目录  android/data/data/com.dongnao.imageloderFrowork/cache/Image
        File file = new File(Environment.getExternalStorageDirectory(), "cache/Image");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取图片路径
     *
     * @param unipue
     * @return
     */
    public static File getCacheFile(String unipue) {
        File cacheDir = getCacheDir();
        File file = new File(cacheDir, unipue);
        return file;
    }
}
