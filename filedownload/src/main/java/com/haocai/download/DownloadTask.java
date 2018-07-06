package com.haocai.download;

import com.haocai.download.http.DownloadCallback;

/**
 * Created by Xionghu on 2018/7/5.
 * Desc:
 */

public class DownloadTask {

    private String mUrl;

    private DownloadCallback callback;

    public DownloadTask(String mUrl, DownloadCallback callback) {
        this.mUrl = mUrl;
        this.callback = callback;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public DownloadCallback getCallback() {
        return callback;
    }

    public void setCallback(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadTask that = (DownloadTask) o;

        if (mUrl != null ? !mUrl.equals(that.mUrl) : that.mUrl != null) return false;
        return callback != null ? callback.equals(that.callback) : that.callback == null;
    }

    @Override
    public int hashCode() {
        int result = mUrl != null ? mUrl.hashCode() : 0;
        result = 31 * result + (callback != null ? callback.hashCode() : 0);
        return result;
    }
}
