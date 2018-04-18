package com.tech.imageloader.core;

import android.graphics.Bitmap;

public interface DisplayListener {
    void onDisplay(IPhotoLoad photoLoad, Bitmap bitmap);
}
