package com.example.writelog.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    private static ScheduledExecutorService threadPool;

    private static final int POOL_SIZE = 8;//线程池最大线程数

    private ThreadPoolUtil() {
        threadPool = Executors.newScheduledThreadPool(POOL_SIZE);
    }

    private static ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil();

    public static ThreadPoolUtil getInstance() {
        if (threadPool == null || threadPool.isShutdown()) {
            threadPool = Executors.newScheduledThreadPool(POOL_SIZE);
        }
        return threadPoolUtil;
    }

    public void execute(Runnable task) {
        threadPool.execute(task);
    }

    public ScheduledFuture scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        return threadPool.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }

    public ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        return threadPool.scheduleWithFixedDelay(runnable, initialDelay, period, unit);
    }

    public void shutdown() {
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdownNow();
        }
    }

}
