package com.haocai.okhttpdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.haocai.download.DownloadManager;
import com.haocai.download.file.FileStorageManager;
import com.haocai.download.http.DownloadCallback;
import com.haocai.download.utils.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "kpioneer";
    public static final String IMAGE_URL = "https://img4.mukewang.com/szimg/5ac2dfe100014a9005400300.jpg";
    public static final String APK_URL = "http://imtt.dd.qq.com/16891/50CC095EFBE6059601C6FB652547D737.apk?fsname=com.tencent.mm_6.6.7_1321.apk&csr=1bbd";
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        File file = FileStorageManager.getInstance().getFileByName("http://www.imooc.com");

//        HttpManager.getInstance().asyncRequest("https://img4.mukewang.com/szimg/5ac2dfe100014a9005400300.jpg", new DownloadCallback() {
//            @Override
//            public void success(File file) {
//                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ivPic.setImageBitmap(bitmap);
//                    }
//                });
//                Logger.debug(TAG,"success:"+file.getAbsolutePath());
//            }
//
//            @Override
//            public void fail(int errorCode, String errorMessage) {
//                Logger.debug(TAG,"fail "+"errorCode "+errorMessage);
//            }
//
//            @Override
//            public void progress(int progress) {
//
//            }
//        });


    }

    private void downloadAPK() {
        DownloadManager.getInstance().download(APK_URL, new DownloadCallback() {
            @Override
            public void success(File file) {
                Logger.debug(TAG, "file path = " + file.getAbsolutePath());
                if (count < 1) {
                    count++;
                    return;
                }
                installApk(file);

//                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ivPic.setImageBitmap(bitmap);
//                    }
//                });
//                Logger.debug(TAG, "success:" + file.getAbsolutePath());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
                Logger.debug(TAG, "fail " + "errorCode " + errorMessage);
            }

            @Override
            public void progress(int progress) {
                Logger.debug(TAG, "progress " + progress);
                progressBar.setProgress(progress);
            }
        });
    }


    @OnClick({R.id.btn_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                downloadAPK();
                break;
        }
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsoluteFile().toString()), "application/vnd.android.package-archive");
        MainActivity.this.startActivity(intent);
    }
}
