package com.tech.imageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageFetcher implements FetchListener {
    private static final MemoryCache memoryCache = new MemoryCache();
    private static ImageFetcher instance;
    private FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private Handler handler;
    private PhotoLoad photoLoad;
    private ExecutorService executorService;

    public ImageFetcher(Context context) {
        handler = new Handler(Looper.getMainLooper());
        fileCache = new FileCache(context);
        instance = this;
        executorService = Executors.newFixedThreadPool(5);
    }

    public static ImageFetcher with(Context context) {
        if (instance != null) return instance;
        return new ImageFetcher(context);
    }

    public ImageFetcher from(String url) {
        photoLoad = new PhotoLoad(this, url);
        return this;
    }

    public void into(final ImageView viewer) {
        imageViews.put(viewer, photoLoad.getSrc());
        photoLoad.setViewer(viewer);
        load();
    }

    private void load() {
        Bitmap bitmap = memoryCache.get(photoLoad.getSrc());
        if (bitmap != null)
            photoLoad.getViewer().setImageBitmap(bitmap);
        else
            queuePhoto();
    }

    private void queuePhoto() {
        executorService.submit(photoLoad);
    }


    @Override
    public Bitmap preFetch(String src) {
        File f = fileCache.getFile(src);

        //from SD cache
        return decodeFile(f);
    }

    @Override
    public void onDone(IPhotoLoad photoLoad, Bitmap bitmap) {
        BitmapDispatcher dispatcher = new BitmapDispatcher(bitmap, photoLoad);
        handler.post(dispatcher);
    }

    @Override
    public void onFetch(String src, Bitmap bitmap) {
        memoryCache.put(src, bitmap);
    }

    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }


    @Override
    public void onError() {
        memoryCache.clear();
    }
}
