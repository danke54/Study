package com.zhangke.imageloader.request;

import com.zhangke.imageloader.loader.Loader;
import com.zhangke.imageloader.loader.LoaderManager;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhangke on 2018/4/3.
 */

public class RequestDispatcher extends Thread {

    /**
     * 这是一个阻塞时队列
     */
    private BlockingQueue<BitmapRequest> requestQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                BitmapRequest bitmapRequest = requestQueue.take();

                Loader loader = LoaderManager.getInstance().getLoader(bitmapRequest.getImageUrl());
                loader.loadImage(bitmapRequest);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
