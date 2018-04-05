package com.zhangke.imageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.zhangke.imageloader.SimpleImageLoader;
import com.zhangke.imageloader.cache.BitmapCache;
import com.zhangke.imageloader.config.DisplayConfig;
import com.zhangke.imageloader.request.BitmapRequest;


/**
 * 加载图片的基本实现
 * <p>
 * <p>
 * Created by zhangke on 2018/4/4.
 */

public abstract class AbstractLoader implements Loader {
    /**
     * 缓存策略
     */
    private BitmapCache bitmapCache = SimpleImageLoader.getInstance().getImageLoaderConfig().getBitmapCache();
    /**
     * 显示配置
     */

    private DisplayConfig displayConfig = SimpleImageLoader.getInstance().getImageLoaderConfig().getDisplayConfig();

    @Override
    public void loadImage(BitmapRequest request) {
        Bitmap bitmap = bitmapCache.get(request);
        if (bitmap == null) {
            showLoadingImage(request);
            bitmap = onLoad(request);
            cacheBitmap(request, bitmap);
        }
        displayImage(request, bitmap);
    }

    /**
     * 显示图片
     *
     * @param request
     * @param bitmap
     */
    private void displayImage(final BitmapRequest request, final Bitmap bitmap) {
        final ImageView imageView = request.getImageView();
        if (imageView != null) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    if (bitmap == null && displayConfig != null && displayConfig.errorImage > 0) {
                        imageView.setImageResource(displayConfig.errorImage);
                    }

                    if (bitmap != null && imageView.getTag().equals(request.getImageUrl())) {
                        imageView.setImageBitmap(bitmap);
                    }

                    if (request.getImageListener() != null) {
                        request.getImageListener().onComplete(imageView, bitmap, request.getImageUrl());
                    }
                }
            });
        }
    }

    /**
     * 缓存图片
     *
     * @param request
     * @param bitmap
     */
    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        if (request != null && bitmap != null) {
            synchronized (AbstractLoader.class) {
                bitmapCache.put(request, bitmap);
            }
        }
    }

    /**
     * 显示默认图片
     *
     * @param request
     */
    private void showLoadingImage(BitmapRequest request) {
        if (displayConfig != null && displayConfig.loadingImage != -1) {
            final ImageView imageView = request.getImageView();
            if (imageView != null) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(displayConfig.loadingImage);
                    }
                });
            }
        }
    }

    /**
     * 加载图片
     *
     * @param request
     * @return
     */
    protected abstract Bitmap onLoad(BitmapRequest request);

}
