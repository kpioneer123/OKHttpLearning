package com.haocai.download;

import android.support.annotation.NonNull;

import com.haocai.download.db.DownloadEntity;
import com.haocai.download.db.DownloadHelper;
import com.haocai.download.file.FileStorageManager;
import com.haocai.download.http.DownloadCallback;
import com.haocai.download.http.HttpManager;
import com.haocai.download.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Xionghu on 2018/7/4.
 * Desc:
 */

public class DownloadManager {

    public final static int MAX_THREAD = 2;
    public final static int LOCAL_PROGRESS_THREAD_SIZE = 1;
    private static ExecutorService SLOCAL_PROGRESS_POOL = Executors.newFixedThreadPool(1);
    private static ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        private AtomicInteger mInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
            return thread;
        }
    });

    private long mLength;
    private HashSet<DownloadTask> mHashSet = new HashSet<>(); //防止多次加载同一任务
    private List<DownloadEntity> mCache;

    private DownloadManager() {

    }

    //单例模式 静态内部类
    public static DownloadManager getInstance() {
        return Holder.sManager;
    }
////双重校验锁
//    private static volatile DownloadManager sManager;
//    public static DownloadManager getInstance() {
//        if (sManager == null) {
//            synchronized (DownloadManager.class) {
//                if (sManager == null) {
//                    sManager = new DownloadManager(); //只在创建的时候加锁
//                    //1.sManager 分配内存
//                    //2.sManager 调用构造方法 init  (但是 虚拟机 字节码重排序 可能将该操作放在步骤3后 而导致空指针异常)
//                    //3.sManager 指向内存分配区域
//                    return sManager;
//                }
//            }
//        }
//        return sManager;
//    }

    public void init(DownloadConfig config) {
        sThreadPool = new ThreadPoolExecutor(config.getCoreThreadSize(), config.getMaxThreadSize(), 60, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    private AtomicInteger mInteger = new AtomicInteger(1);

                    @Override
                    public Thread newThread(@NonNull Runnable runnable) {
                        Thread thread = new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
                        return thread;
                    }
                }

        );
        SLOCAL_PROGRESS_POOL = Executors.newFixedThreadPool(config.getLocalProgressThreadSize());
    }

    private void finish(DownloadTask task) {
        mHashSet.remove(task);
    }

    public void download(final String url, final DownloadCallback callback) {

        final DownloadTask task = new DownloadTask(url, callback);
        if (mHashSet.contains(task)) {
            callback.fail(HttpManager.TASK_RUNNING_ERROR_CODE, "任务已经执行了");
            return;
        }
        mHashSet.add(task);
        mCache = DownloadHelper.getInstance().getAll(url);

        if (mCache == null || mCache.size() == 0) {
            HttpManager.getInstance().asyncRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    finish(task);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful() && callback != null) {
                        callback.fail(HttpManager.NETWORK_ERROR_CODE, "网络出现问题了");
                        return;
                    }
                    mLength = response.body().contentLength();
                    if (mLength == -1) {
                        callback.fail(HttpManager.CONTENT_LENGTH_ERROR_CODE, "content length -1");
                        return;
                    }
                    processDownload(url, mLength, callback, mCache);
                    finish(task);
                }
            });
        } else {
            Logger.debug("kpioneer", "Cache.size ：" + mCache.size() + "");
            //todo 处理已经下载过的数据
            for (int i = 0; i < mCache.size(); i++) {
                DownloadEntity entity = mCache.get(i);
                if (i == mCache.size() - 1) {
                    mLength = entity.getEnd_position() + 1;
                }
                long startSize = entity.getStart_position();
                //       +entity.getProgress_position();
                long endSize = entity.getEnd_position();
                sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback, entity));
            }
        }
        SLOCAL_PROGRESS_POOL.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);

                        File file = FileStorageManager.getInstance().getFileByName(url);
                        long fileSize = file.length();
                        int progress = (int) (fileSize * 100.0 / mLength);

                        if (progress >= 100) {
                            callback.progress(progress);
                            return;
                        }
                        callback.progress(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void processDownload(String url, long length, DownloadCallback callback, List<DownloadEntity> entities) {
        long threadDownloadSize = length / MAX_THREAD;

        if (mCache == null && entities.size() == 0) {
            mCache = new ArrayList<>();
        }
        for (int i = 0; i < MAX_THREAD; i++) {
            DownloadEntity entity = new DownloadEntity();
            long startSize = i * threadDownloadSize;
            long endSize = i == MAX_THREAD - 1 ? length - 1 : (i + 1) * threadDownloadSize - 1;
            entity.setStart_position(startSize);
            entity.setEnd_position(endSize);
            entity.setDownload_url(url);
            entity.setThread_id(i + 1);
            sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback, entity));
        }
    }

    private static class Holder {
        private static DownloadManager sManager = new DownloadManager();

    }
}
