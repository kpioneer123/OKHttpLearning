package com.haocai.mylibrary;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Xionghu on 2018/6/21.
 * Desc:
 */

public class QueryHttp {
    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.
                parse("http://v.juhe.cn/toutiao/index").
                newBuilder().
                addQueryParameter("type", "top").
                addQueryParameter("key", "4e240ba7395a2d3ef9ce1fb8388755b9").build();
        String url = httpUrl.toString();
        System.out.println(httpUrl.toString());
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
