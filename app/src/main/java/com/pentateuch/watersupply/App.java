package com.pentateuch.watersupply;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by varu on 03-04-2018.
 */

public class App extends Application {
    private SharedPreferences mPreferences;

    private static App instance;

    public static App getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPreferences = getSharedPreferences("User", MODE_PRIVATE);

    }

    public void setValue(String key,String value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void setValue(String key,Boolean value){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public <T> T getValue(String key,T value){
        if(value instanceof String){
            return (T) mPreferences.getString(key, (String) value);
        }
        if(value instanceof Boolean){
            Boolean aBoolean = mPreferences.getBoolean(key, (Boolean) value);
            return (T) aBoolean;
        }
        return value;
    }

}
