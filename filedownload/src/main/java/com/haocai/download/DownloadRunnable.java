package com.haocai.download;

import android.os.Process;

import com.haocai.download.db.DownloadEntity;
import com.haocai.download.db.DownloadHelper;
import com.haocai.download.file.FileStorageManager;
import com.haocai.download.http.DownloadCallback;
import com.haocai.download.http.HttpManager;
import com.haocai.download.utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Created by Xionghu on 2018/7/4.
 * Desc:
 */

public class DownloadRunnable implements Runnable {

    private long mStart;

    private long mEnd;

    private String mUrl;

    private DownloadCallback mCallback;

    private DownloadEntity mEntity;

    public DownloadRunnable(long mStart, long mEnd, String mUrl, DownloadCallback mCallback, DownloadEntity mEntity) {
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mUrl = mUrl;
        this.mCallback = mCallback;
        this.mEntity = mEntity;
    }

    public DownloadRunnable(long mStart, long mEnd, String mUrl, DownloadCallback mCallback) {
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mUrl = mUrl;
        this.mCallback = mCallback;
    }

    @Override
    public void run() {

        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Response response = HttpManager.getInstance().syncRequest(mUrl, mStart, mEnd);

        if (response == null && mCallback != null) {
            mCallback.fail(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
            return;
        }
        File file = FileStorageManager.getInstance().getFileByName(mUrl);
        long finishProgress = mEntity.getProgress_position() == null ? 0 : mEntity.getProgress_position();
        long progress = 0;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(mStart);
            byte[] buffer = new byte[1024 * 500];
            int len;
            InputStream inputStream = response.body().byteStream();
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                progress += len;
                mEntity.setProgress_position(progress);
                Logger.debug("kpioneer", "progress ----->" + progress);
            }
            mEntity.setProgress_position(mEntity.getProgress_position() + finishProgress);
            randomAccessFile.close();
            mCallback.success(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DownloadHelper.getInstance().insert(mEntity);
        }
    }
}
