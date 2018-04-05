package com.zhangke.imageloader.request;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.ContentValues.TAG;

/**
 * Created by zhangke on 2018/4/3.
 */

public class RequestQueue {

    private int threadCount;

    /**
     * 使用一个阻塞式队列添加图片请求
     */
    private BlockingQueue<BitmapRequest> requestQueue = new PriorityBlockingQueue<>();
    /**
     * 用于生成一个请求号
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private RequestDispatcher[] mDispachers;

    public RequestQueue(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * 添加请求
     *
     * @param request
     */
    public void addRequest(BitmapRequest request) {
        if (requestQueue.contains(request)) {
            Log.i(TAG, "该请求已添加到队列中");
            return;
        }

        request.setSerialNo(atomicInteger.incrementAndGet());
        requestQueue.add(request);
    }

    /**
     * 开启请求
     */
    public void start() {
        stop();
        startDispatchers();
    }

    /**
     * 开启转发器
     */
    private void startDispatchers() {
        mDispachers = new RequestDispatcher[threadCount];
        for (int i = 0; i < threadCount; i++) {
            RequestDispatcher p = new RequestDispatcher(requestQueue);
            mDispachers[i] = p;
            mDispachers[i].start();
        }
    }

    /**
     * 停止
     */
    private void stop() {
        // TODO 结束请求

    }
}
