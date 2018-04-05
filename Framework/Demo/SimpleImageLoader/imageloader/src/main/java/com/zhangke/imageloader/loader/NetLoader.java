package com.zhangke.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.zhangke.imageloader.request.BitmapRequest;
import com.zhangke.imageloader.utils.BitmapDecoder;
import com.zhangke.imageloader.utils.FileUtils;
import com.zhangke.imageloader.utils.ImageViewHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络加载器
 * <p>
 * Created by zhangke on 2018/4/4.
 */

public class NetLoader extends AbstractLoader {


    @Override
    public Bitmap onLoad(final BitmapRequest request) {
        downloadImage(request.getImageUrl(), FileUtils.getCacheFile(request.getImageUriMD5()));
        BitmapDecoder bitmapDecoder = new BitmapDecoder() {
            @Override
            protected Bitmap decodeBitmapByOptions(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(FileUtils.getCacheFile(request.getImageUriMD5()).getAbsolutePath(), options);
            }
        };
        return bitmapDecoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),
                ImageViewHelper.getImageViewHeight(request.getImageView()));
    }

    /**
     * 下载图片
     *
     * @param url
     * @param file
     */
    private void downloadImage(String url, File file) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            is = conn.getInputStream();
            fos = new FileOutputStream(file);

            byte[] buf = new byte[512];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is != null) {
                    is.close();
                }

                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {

            }

        }

    }




}
