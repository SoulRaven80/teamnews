package com.soulraven.teamnews.lazylist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.soulraven.teamnews.util.stream.Utils;
import com.soulraven.teamnews.R;
import com.soulraven.teamnews.util.http.RemoteResourceFetcher;
import com.soulraven.teamnews.lazylist.cache.FileCache;
import com.soulraven.teamnews.lazylist.cache.MemoryCache;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler = new Handler(); //handler to display images in UI thread
    private int stub_id = R.drawable.logo;

    public ImageLoader(final Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    public void displayImage(final String url, final ImageView imageView) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = getBitmap(url);
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, "Error setting the bitmap image", e);
            Log.e(TAG, e.getMessage());
            imageView.setVisibility(View.GONE);
        }
    }

    public void displayImageOld(final String url, final ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(final String url, final ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    public Bitmap getBitmap(final String urlString) {
        String url = null;
        try {
            url = urlString.substring(urlString.lastIndexOf("http"));
        }
        catch (Exception e) {
            Log.e(TAG, urlString, e);
        }

        Bitmap bitmap = null;
        /*
        bitmap = memoryCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
*/
        //from SD cache
        final File f = fileCache.getFile(url);
        if (f.exists()) {
            bitmap = decodeFile(f);
            if (bitmap != null) {
                return bitmap;
            }
        }
        //from web
        try {
            RemoteResourceFetcher<Bitmap> fetcher = new RemoteResourceFetcher<Bitmap>() {
                @Override
                protected Bitmap processStream(final InputStream stream) {
                    try {
                        OutputStream os = new FileOutputStream(f);
                        // download & save to disk
                        Utils.copyStream(stream, os);
                        os.close();
                        return decodeFile(f);
                    } catch (Exception e) {
                        Log.e(TAG, "Error in output stream", e);
                    }
                    return null;
                }
            };
            fetcher.fetchResource(url);
            bitmap = fetcher.get();
            if (bitmap != null) {
                memoryCache.put(url, bitmap);
            }
        } catch (Throwable ex) {
            Log.e(TAG, "Error getting the bitmap", ex);
            if (ex instanceof OutOfMemoryError) {
                memoryCache.clear();
            }
        }
        return bitmap;
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(final InputStream stream) {
        try {
            //decode with inSampleSize
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = getImageScale(stream, options);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            stream.close();
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error decoding file", e);
        }
        return null;
    }

    private Bitmap decodeFile(final File f) {
        try {
            //decode with inSampleSize
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = getImageScale(f, options);
            options.inJustDecodeBounds = false;
            FileInputStream stream = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            stream.close();
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error decoding file", e);
        }
        return null;
    }

    private int getImageScale(final File f, final BitmapFactory.Options options) throws IOException {
        return getImageScale(new FileInputStream(f), options);
    }

    private int getImageScale(final InputStream stream, final BitmapFactory.Options options) throws IOException {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        stream.close();

        //Find the correct scale value. It should be the power of 2.
        return calculateInSampleSize(options, 60, 60);
    }

    private int calculateInSampleSize(
            final BitmapFactory.Options options, final int reqWidth, final int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(final String u, final ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(final PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad)) {
                    return;
                }
                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if (imageViewReused(photoToLoad)) {
                    return;
                }
                handler.post(new BitmapDisplayer(bmp, photoToLoad));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(final PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        return (tag == null || !tag.equals(photoToLoad.url));
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(final Bitmap b, final PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad)) {
                return;
            }
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            }
            else {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
