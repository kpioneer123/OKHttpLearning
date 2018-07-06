package com.haocai.mylibrary;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Xionghu on 2018/6/21.
 * Desc:
 */

public class PostHttp {
    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder().
                add("username", "kpioneer").
                add("userage", "26").build();

        Request request = new Request.Builder().url("http://localhost:8080/JavaWeb/HelloServlet").post(body).build();
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
