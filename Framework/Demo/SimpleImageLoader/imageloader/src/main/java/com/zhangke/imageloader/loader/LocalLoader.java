package com.zhangke.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.zhangke.imageloader.request.BitmapRequest;
import com.zhangke.imageloader.utils.BitmapDecoder;
import com.zhangke.imageloader.utils.ImageViewHelper;

import java.io.File;

/**
 * 本地加载器
 *
 * Created by zhangke on 2018/4/4.
 */

public class LocalLoader extends AbstractLoader {
    @Override
    public Bitmap onLoad(BitmapRequest request) {
        //得到本地图片的路径
        final String path= Uri.parse(request.getImageUrl()).getPath();
        File file=new File(path);
        if(!file.exists())
        {
            return null;
        }
        BitmapDecoder decoder=new BitmapDecoder() {
            @Override
            protected Bitmap decodeBitmapByOptions(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(path,options);
            }

        };

        return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView())
                ,ImageViewHelper.getImageViewHeight(request.getImageView()));
    }
}
