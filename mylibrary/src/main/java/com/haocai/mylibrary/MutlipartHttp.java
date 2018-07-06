package com.haocai.mylibrary;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Xionghu on 2018/6/29.
 * Desc:
 */

public class MutlipartHttp {

    public static void main(String args[]) {
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"),new File("C:\\Users\\Administrator\\Desktop\\girl.jpg"));

        MultipartBody body = new MultipartBody.Builder().
                setType(MultipartBody.FORM).
                addFormDataPart("name","kpioneer").
                addFormDataPart("filename","girl.jpg",imageBody ).build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://localhost:8080/JavaWeb/UploadServlet").post(body).build();
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
