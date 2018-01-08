package com.zhangke.servicedemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by zhangke on 2017/11/16.
 */


/**
 * 通过IntentService完成下载任务
 */
public class DownloadService extends IntentService {
    public static final String DOWN_URL = "down_url";
    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(DOWN_URL);
        downlaod(url);
    }

    /**
     * 模拟下载任务
     */
    private void downlaod(String url) {
        Log.e("zhangke", "url = " + url + "  正在下载中...");
        SystemClock.sleep(3000);
        Log.e("zhangke", "url = " + url + "  下载完成");
    }

}
