package com.tech.imageloader.core;

import android.graphics.Bitmap;

public interface FetchListener {
    Bitmap preFetch(String src);

    void onDone(IPhotoLoad photoLoad, Bitmap bitmap);

    void onFetch(String src, Bitmap bitmap);

    void onError();
}
