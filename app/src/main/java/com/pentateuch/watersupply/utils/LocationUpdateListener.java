package com.pentateuch.watersupply.utils;

public interface LocationUpdateListener {
    void onChange(String... texts);
    void onCancel();
}
