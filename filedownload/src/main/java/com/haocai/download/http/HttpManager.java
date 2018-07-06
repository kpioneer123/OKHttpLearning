package com.haocai.download.http;

import android.content.Context;

import com.haocai.download.file.FileStorageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Xionghu on 2018/7/3.
 * Desc:
 */

public class HttpManager {

    public static final int NETWORK_ERROR_CODE = 1;

    public static final int CONTENT_LENGTH_ERROR_CODE = 2;

    public static final int TASK_RUNNING_ERROR_CODE = 2;

    private static final HttpManager sManager = new HttpManager();

    private Context mContext;

    private OkHttpClient mClient;

    private HttpManager() {
        mClient = new OkHttpClient();
    }

    public static HttpManager getInstance() {
        return sManager;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * 同步请求
     *
     * @param url
     * @return
     */
    public Response syncRequest(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同步请求 指定下载数据范围
     *
     * @param url
     * @return
     */
    public Response syncRequest(String url, long start, long end) {
        Request request = new Request.Builder().url(url).
                addHeader("Range", "bytes=" + start + "-" + end).
                build();
        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 同步请求
     *
     * @param url
     * @return
     */
    public void asyncRequest(final String url, final Callback callback) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(callback);
    }

    /**
     * 同步请求
     *
     * @param url
     * @return
     */
    public void asyncRequest(final String url, final DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback != null) {
                    callback.fail(NETWORK_ERROR_CODE, "网络请求异常");
                }
                File file = FileStorageManager.getInstance().getFileByName(url);

                byte[] buffer = new byte[1024 * 500];
                int len;
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = response.body().byteStream();
                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    //flush() 是把缓冲区的数据强行输出 防止close 而丢失数据
                    fileOutputStream.flush();
                }
                callback.success(file);
            }
        });
    }
}
