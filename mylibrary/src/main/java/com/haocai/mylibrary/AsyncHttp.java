package com.haocai.mylibrary;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.haocai.mylibrary.utils.Constants.BaiduURL;

/**
 * Created by Xionghu on 2018/6/21.
 * Desc: 异步请求
 */

public class AsyncHttp {
    public static void sendRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendAsyncRequest(String url) {
        System.out.println(Thread.currentThread().getId());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(Thread.currentThread().getId());
                }
            }
        });
    }

    public static void main(String args[]) {
        //sendRequest("http://www.baidu.com");
        sendAsyncRequest(BaiduURL);
    }
}
