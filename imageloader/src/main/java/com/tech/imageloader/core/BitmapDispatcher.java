package com.tech.imageloader.core;

import android.graphics.Bitmap;

public class BitmapDispatcher implements Runnable {
    private Bitmap bitmap;
    private IPhotoLoad photoLoad;

    BitmapDispatcher(Bitmap bitmap, IPhotoLoad photoLoad) {
        this.bitmap = bitmap;
        this.photoLoad = photoLoad;
    }

    @Override
    public void run() {
        photoLoad.getViewer().setImageBitmap(bitmap);
    }
}
