package com.zhangke.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zhangke.imageloader.config.DisplayConfig;
import com.zhangke.imageloader.config.ImageLoaderConfig;
import com.zhangke.imageloader.request.BitmapRequest;
import com.zhangke.imageloader.request.RequestQueue;

/**
 * Created by zhangke on 2018/4/3.
 */

public class SimpleImageLoader {

    private ImageLoaderConfig imageLoaderConfig;
    private RequestQueue requestQueue;

    private static SimpleImageLoader instance;

    private SimpleImageLoader() {

    }

    private SimpleImageLoader(ImageLoaderConfig config) {
        this.imageLoaderConfig = config;

        this.requestQueue = new RequestQueue(config.getThreadCount());
        this.requestQueue.start();
    }

    /**
     * SimpleImageLoader初始化
     *
     * @param config
     * @return
     */
    public static SimpleImageLoader init(ImageLoaderConfig config) {
        if (instance == null) {
            synchronized (SimpleImageLoader.class) {
                if (instance == null) {
                    instance = new SimpleImageLoader(config);
                }
            }
        }

        return instance;
    }

    /**
     * 获取SimpleImageLoader实例
     *
     * @return
     */
    public static SimpleImageLoader getInstance() {
        if (instance == null) {
            throw new UnsupportedOperationException("没有初始化");
        }
        return instance;
    }


    /**
     * 展示图片
     *
     * @param imageView
     * @param uri
     */
    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, null, null);
    }

    /**
     * 展示图片
     *
     * @param imageView
     * @param uri
     * @param displayConfig
     * @param listener
     */
    public void displayImage(ImageView imageView, String uri,
                             DisplayConfig displayConfig, ImageListener listener) {

        BitmapRequest bitmapRequest = new BitmapRequest(imageView, uri, displayConfig, listener);
        requestQueue.addRequest(bitmapRequest);
    }

    public ImageLoaderConfig getImageLoaderConfig() {
        return imageLoaderConfig;
    }

    public static interface ImageListener {
        /**
         * @param imageView
         * @param bitmap
         * @param uri
         */
        void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }

}
