package com.zhangke.imageloader.request;

import android.widget.ImageView;

import com.zhangke.imageloader.config.DisplayConfig;
import com.zhangke.imageloader.SimpleImageLoader;
import com.zhangke.imageloader.policy.LoadPolicy;
import com.zhangke.imageloader.utils.MD5Utils;

import java.lang.ref.SoftReference;

/**
 * 图片请求
 * Created by zhangke on 2018/4/3.
 */

public class BitmapRequest implements Comparable<BitmapRequest> {
    /**
     * 编号
     */
    private int serialNo;


    /**
     * ImageView 对象
     */
    private SoftReference<ImageView> imageViewSoft;
    /**
     * 图片url
     */
    private String imageUrl;
    /**
     * 图片名称
     */
    private String imageUriMD5;
    /**
     * 下载完成监听
     */
    public SimpleImageLoader.ImageListener imageListener;
    /**
     * 显示策略
     */
    private DisplayConfig displayConfig;

    /**
     * 加载策略
     */
    private LoadPolicy loadPolicy = SimpleImageLoader.getInstance().getImageLoaderConfig().getLoadPolicy();

    /**
     * 创建一个图片请求
     *
     * @param imageView
     * @param imageUrl
     * @param displayConfig
     * @param imageListener
     */
    public BitmapRequest(ImageView imageView,
                         String imageUrl,
                         DisplayConfig displayConfig,
                         SimpleImageLoader.ImageListener imageListener) {

        this.imageViewSoft = new SoftReference<ImageView>(imageView);
        //设置可见的Image的Tag，要下载的图片路径
        imageView.setTag(imageUrl);
        this.imageUrl = imageUrl;
        this.imageUriMD5 = MD5Utils.toMD5(imageUrl);
        if (displayConfig != null) {
            this.displayConfig = displayConfig;
        }
        this.imageListener = imageListener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitmapRequest that = (BitmapRequest) o;

        // TODO ImageView也应该作为一个比较参数
        if (serialNo != that.serialNo) return false;
        return loadPolicy != null ? loadPolicy.equals(that.loadPolicy) : that.loadPolicy == null;

    }

    @Override
    public int hashCode() {
        int result = loadPolicy != null ? loadPolicy.hashCode() : 0;
        result = 31 * result + serialNo;
        return result;
    }

    @Override
    public int compareTo(BitmapRequest o) {
        return loadPolicy.compareto(o, this);
    }


    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }


    public ImageView getImageView() {
        return imageViewSoft.get();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUriMD5() {
        return imageUriMD5;
    }

    public SimpleImageLoader.ImageListener getImageListener() {
        return imageListener;
    }
}
