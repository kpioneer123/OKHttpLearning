package com.haocai.download.file;

import android.content.Context;
import android.os.Environment;

import com.haocai.download.utils.Md5Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Xionghu on 2018/7/3.
 * Desc:
 */

public class FileStorageManager {
    private static final FileStorageManager sManager = new FileStorageManager();

    private Context mContext;

    private FileStorageManager() {

    }

    public static FileStorageManager getInstance() {
        return sManager;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public File getFileByName(String url) {
        File parent;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            parent = mContext.getExternalCacheDir();
        } else {
            parent = mContext.getCacheDir();
        }
        String fileName = Md5Utils.generateCode(url);

        File file = new File(parent, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
