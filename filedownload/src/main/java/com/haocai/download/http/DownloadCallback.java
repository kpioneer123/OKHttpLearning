package com.haocai.download.http;

import java.io.File;

/**
 * Created by Xionghu on 2018/7/3.
 * Desc:
 */

public interface DownloadCallback {
    void success(File file);

    void fail(int errorCode, String errorMessage);

    void progress(int progress);

}
