package com.zhangke.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片解码器
 * <p>
 * Created by zhangke on 2018/4/4.
 */

public abstract class BitmapDecoder {

    /**
     * 解析图片
     *
     * @param imageViewWidth
     * @param imageViewHeight
     * @return
     */
    public Bitmap decodeBitmap(int imageViewWidth, int imageViewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeBitmapByOptions(options);
        calculateSampleSizeByOptions(options, imageViewWidth, imageViewHeight);
        return decodeBitmapByOptions(options);
    }

    /**
     * 图片缩放比例
     *
     * @param options
     * @param imageViewWidth
     * @param imageViewHeight
     */
    private void calculateSampleSizeByOptions(BitmapFactory.Options options, int imageViewWidth, int imageViewHeight) {
        //计算缩放的比例
        //图片的原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        //  reqWidth   ImageView的  宽
        if (width > imageViewWidth || height > imageViewHeight) {
            //宽高的缩放比例
            int heightRatio = Math.round((float) height / (float) imageViewHeight);
            int widthRatio = Math.round((float) width / (float) imageViewWidth);

            //有的图是长图、有的是宽图
            inSampleSize = Math.max(heightRatio, widthRatio);
        }

        //全景图
        //当inSampleSize为2，图片的宽与高变成原来的1/2
        //options.inSampleSize = 2
        options.inSampleSize = inSampleSize;

        //每个像素2个字节
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //Bitmap占用内存  true
        options.inJustDecodeBounds = false;
        //当系统内存不足时可以回收Bitmap
        options.inPurgeable = true;
        options.inInputShareable = true;

    }

    protected abstract Bitmap decodeBitmapByOptions(BitmapFactory.Options options);
}
