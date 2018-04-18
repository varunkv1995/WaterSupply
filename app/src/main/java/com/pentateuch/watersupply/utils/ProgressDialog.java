package com.pentateuch.watersupply.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.pentateuch.watersupply.R;

public class ProgressDialog {
    public static final int FLAG_FULL = 1;
    private Context context;
    private PopupWindow window;

    public ProgressDialog(Context context) {
        this.context = context;
        createWindow();
    }

    public ProgressDialog(Context context, int flag) {
        this.context = context;
        switch (flag) {
            case FLAG_FULL:
                createFullWindow();
                break;
        }
    }

    private void createFullWindow() {
        FrameLayout layout = new FrameLayout(context);
        View layer = new View(context);
        layer.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, android.R.attr.actionBarSize, 0, 0);
        layer.setLayoutParams(layoutParams);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        progressBar.setPadding(40, 40, 40, 40);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 40, 40, 40);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        layout.addView(layer);
        layout.addView(progressBar);
        window = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void createWindow() {
        FrameLayout layout = new FrameLayout(context);
        View layer = new View(context);
        layer.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        layer.setAlpha(0.5f);
        layer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setBackgroundResource(R.drawable.ripple_white_border);
        progressBar.setPadding(40, 40, 40, 40);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(40, 40, 40, 40);
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        layout.addView(layer);
        layout.addView(progressBar);
        window = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void showProgressAt(View view) {


        window.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void dismiss() {
        window.dismiss();
    }
}
