package com.haocai.download;

/**
 * Created by Xionghu on 2018/7/6.
 * Desc:
 */

public class DownloadConfig {

    private int coreThreadSize;
    private int maxThreadSize;
    private int localProgressThreadSize;

    public DownloadConfig(Builder builder) {
        coreThreadSize = builder.coreThreadSize == 0 ? DownloadManager.MAX_THREAD : builder.coreThreadSize;
        maxThreadSize = builder.maxThreadSize == 0 ? DownloadManager.MAX_THREAD : builder.maxThreadSize;
        localProgressThreadSize = builder.localProgressThreadSize == 0 ? DownloadManager.LOCAL_PROGRESS_THREAD_SIZE : builder.localProgressThreadSize;
    }

    public int getCoreThreadSize() {
        return coreThreadSize;
    }

    public int getMaxThreadSize() {
        return maxThreadSize;
    }

    public int getLocalProgressThreadSize() {
        return localProgressThreadSize;
    }

    public static  class Builder {
        private int coreThreadSize;
        private int maxThreadSize;
        private int localProgressThreadSize;

        public DownloadConfig builder() {
            return new DownloadConfig(this);
        }

        public Builder setCoreThreadSize(int coreThreadSize) {
            this.coreThreadSize = coreThreadSize;
            return this;
        }

        public Builder setMaxThreadSize(int maxThreadSize) {
            this.maxThreadSize = maxThreadSize;
            return this;
        }

        public Builder setLocalProgressThreadSize(int localProgressThreadSize) {
            this.localProgressThreadSize = localProgressThreadSize;
            return this;
        }
    }
}
