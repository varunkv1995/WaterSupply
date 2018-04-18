package com.tech.imageloader.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.tech.imageloader.view.ImageViewer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoLoad implements Runnable, IPhotoLoad {
    private FetchListener listener;
    private String src;
    private ImageView viewer;

    PhotoLoad(FetchListener listener, String src) {
        this.listener = listener;
        this.src = src;
    }

    @Override
    public String getSrc() {
        return src;
    }

    @Override
    public void run() {
        Bitmap preFetch = listener.preFetch(src);
        if (preFetch != null) {
            listener.onDone(this, preFetch);
            return;
        }
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            listener.onFetch(src, bitmap);
            listener.onDone(this, bitmap);
        } catch (Throwable e) {
            if (e instanceof OutOfMemoryError)
                listener.onError();
        }
    }

    @Override
    public ImageView getViewer() {
        return viewer;
    }

    public void setViewer(ImageView viewer) {
        this.viewer = viewer;
    }
}
