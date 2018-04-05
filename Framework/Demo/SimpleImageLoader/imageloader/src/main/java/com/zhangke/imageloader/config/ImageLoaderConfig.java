package com.zhangke.imageloader.config;

import com.zhangke.imageloader.cache.BitmapCache;
import com.zhangke.imageloader.cache.MemoryCache;
import com.zhangke.imageloader.policy.LoadPolicy;
import com.zhangke.imageloader.policy.ReversePolicy;

/**
 * ImageLoader全局配置
 * <p>
 * <pre>
 * ImageLoaderConfig config = builder.setBitmapCache()
 *          .setLoadPolicy()
 *          .setThreadCount()
 *          .setLoadingImage()
 *          .setErrorImage()
 *          .build();
 *
 *
 *  </pre>
 */


public class ImageLoaderConfig {

    /**
     * 加载策略
     */
    private LoadPolicy loadPolicy;
    /**
     * 缓存策略
     */
    private BitmapCache bitmapCache;
    /**
     * 占位图策略
     */
    private DisplayConfig displayConfig;
    /**
     * 并发策略
     */
    private int threadCount;

    private ImageLoaderConfig() {

        loadPolicy = new ReversePolicy();
        bitmapCache = new MemoryCache();
        threadCount = Runtime.getRuntime().availableProcessors();
        displayConfig = new DisplayConfig();
    }

    /**
     * 灵活配置
     */
    public static class Builder {
        private ImageLoaderConfig config;

        public Builder() {
            config = new ImageLoaderConfig();
        }

        public Builder setLoadPolicy(LoadPolicy loadPolicy) {
            config.loadPolicy = loadPolicy;
            return this;
        }

        public Builder setBitmapCache(BitmapCache bitmapCache) {
            config.bitmapCache = bitmapCache;
            return this;
        }

        public Builder setLoadingImage(int resId) {
            config.displayConfig.loadingImage = resId;
            return this;
        }

        public Builder setErrorImage(int resId) {
            config.displayConfig.errorImage = resId;
            return this;
        }


        public Builder setThreadCount(int threadCount) {
            config.threadCount = threadCount;
            return this;
        }

        public ImageLoaderConfig build() {
            return config;
        }

    }

    public LoadPolicy getLoadPolicy() {
        return loadPolicy;
    }

    public BitmapCache getBitmapCache() {
        return bitmapCache;
    }

    public DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
