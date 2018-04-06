package com.pentateuch.watersupply;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pentateuch.watersupply.model.User;

/**
 * Created by varu on 03-04-2018.
 */

public class App extends Application {
    private static App instance;
    private SharedPreferences mPreferences;
    private User user;

    public static App getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPreferences = getSharedPreferences("User", MODE_PRIVATE);

    }

    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setValue(String key, Boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void setValue(String key, Object value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public <T> T getValue(String key, T value) {
        if (value instanceof String) {
            return (T) mPreferences.getString(key, (String) value);
        }
        if (value instanceof Boolean) {
            Boolean aBoolean = mPreferences.getBoolean(key, (Boolean) value);
            return (T) aBoolean;
        }
        return value;
    }

    public <T> T getValueFromJson(String key, T value) {
        String json = mPreferences.getString(key, "");
        Gson gson = new Gson();
        Object jsonObject = gson.fromJson(json, value.getClass());
        if (jsonObject == null)
            return value;
        return (T) jsonObject;
    }

    public void clearSetting(){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
