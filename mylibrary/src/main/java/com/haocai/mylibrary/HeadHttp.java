package com.haocai.mylibrary;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.haocai.mylibrary.utils.Constants.BaiduURL;

/**
 * Created by Xionghu on 2018/6/21.
 * Desc:
 */

public class HeadHttp {
    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(BaiduURL).addHeader("User-Agent", "from kpioneer").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Headers headers = response.headers();
                for(int i = 0;i<headers.size();i++){
                    System.out.println(headers.name(i) + " : "+ headers.value(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
