package com.soulraven.teamnews.lazylist.cache;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.net.URLEncoder;

public class FileCache {

    private static final String CACHE_DIR_NAME = "/data/data/com.soulraven.teamnews";
    private static final String TAG = FileCache.class.getSimpleName();
    private File cacheDir;

    public FileCache(final Context context) {
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), CACHE_DIR_NAME);
        }
        else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public File getFile(final String url) {
        String filename;
        try {
            filename = URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, url, e);
            filename = String.valueOf(url.hashCode());
        }
        return new File(cacheDir, filename);
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.exists()) {
                f.delete();
            }
        }
    }
}