package com.haocai.okhttpdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.haocai.download.DownloadConfig;
import com.haocai.download.DownloadManager;
import com.haocai.download.db.DownloadHelper;
import com.haocai.download.file.FileStorageManager;
import com.haocai.download.http.HttpManager;

/**
 * Created by Xionghu on 2018/7/3.
 * Desc:
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageManager.getInstance().init(this);
        HttpManager.getInstance().init(this);
        DownloadHelper.getInstance().init(this);
        Stetho.initializeWithDefaults(this);
        DownloadConfig config = new DownloadConfig.Builder().
                setCoreThreadSize(2).
                setMaxThreadSize(4).
                setLocalProgressThreadSize(1).
                builder();
        DownloadManager.getInstance().init(config);
    }
}
