package com.tech.imageloader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageViewer extends LinearLayout {
    private ImageView imageView;

    public ImageViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

    }

    public ImageViewer(Context context) {
        super(context);
        imageView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        addView(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
