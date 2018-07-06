package com.haocai.mylibrary;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.haocai.mylibrary.utils.Constants.BaiduURL;

/**
 * Created by Xionghu on 2018/6/20.
 * Desc:
 */

public class HelloOkhttp {

    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(BaiduURL).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
