package com.haocai.download;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Xionghu on 2018/7/3.
 * Desc:
 */

public class RangeHttp {
    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://img.mukewang.com/user/54584ca90001a85802200220-100-100.jpg").
                addHeader("Range","bytes=0-2").
                build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("content-length :"+response.body().contentLength());
            if (response.isSuccessful()) {
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + " : " + headers.value(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
