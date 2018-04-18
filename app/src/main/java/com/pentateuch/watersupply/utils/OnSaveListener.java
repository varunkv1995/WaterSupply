package com.pentateuch.watersupply.utils;

public interface OnSaveListener<T> {
    void onSaved(T value);
    void onCancel();
}
